package net;

import com.google.gson.Gson;
import game.GameState;
import model.Cell;
import model.MazeGenerator;
import model.Player;
// ─── imports 加一行 ─────────────────────────────────────────
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Authoritative TCP server for the Maze Game.
 * - Keeps the single truth in a shared GameState.
 * - Accepts up to 2 simultaneous players, rejects the 3rd with {"type":"FULL"}.
 * - Broadcasts the full GameState snapshot after every input or disconnect.
 */
public class GameServer {

    // ─── Config ────────────────────────────────────────────────────────────────
    private static final int PORT        = 7777;
    private static final int MAX_PLAYERS = 2;
    private final ScheduledExecutorService tick = Executors.newSingleThreadScheduledExecutor();
    // ─── Core state & helpers ──────────────────────────────────────────────────
    private final GameState       state   = new GameState(11, 21);
    private final MazeGenerator generator = new MazeGenerator(state.getRows(), state.getCols());
    private final ExecutorService pool    = Executors.newCachedThreadPool();
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final Gson gson = new Gson();

    // ─── Boot strap ────────────────────────────────────────────────────────────
    public static void main(String[] args) throws IOException {
        new GameServer().run();
    }

    public GameServer() {
        state.setMaze(generator.generate());
        // 每 1 秒累加遊戲秒數並廣播
        tick.scheduleAtFixedRate(() -> {
            synchronized (state) {        // 避免寫入衝突
                state.incrementTimer();
                broadcastState();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void run() throws IOException {
        try (ServerSocket ss = new ServerSocket(PORT)) {
            System.out.println("[Server] listening on port " + PORT);
            while (true) {
                Socket s = ss.accept();
                ClientHandler ch = new ClientHandler(s);
                clients.add(ch);              // track for broadcast
                pool.submit(ch);              // run in background
            }
        }
    }

    // ───────────────────────────────────────────────────────────────────────────
    // ◼ Inner class: one per connected client
    // ───────────────────────────────────────────────────────────────────────────
    private class ClientHandler implements Runnable {

        private final Socket socket;
        private final DataInputStream  in;
        private final DataOutputStream out;
        private int playerId = -1;            // assigned during handshake

        ClientHandler(Socket s) throws IOException {
            this.socket = s;
            this.in  = new DataInputStream (s.getInputStream());
            this.out = new DataOutputStream(s.getOutputStream());
        }

        @Override public void run() {
            try {
                handshake();                  // may throw/return early
                mainLoop();                   // blocks until client quits
            } catch (IOException ignored) {
            } finally {
                cleanup();
            }
        }

        // ── 1. Reserve a slot or refuse connection ────────────────────────────
        private void handshake() throws IOException {
            synchronized (state) {
                if (state.getPlayers().size() >= MAX_PLAYERS) {
                    send("{\"type\":\"FULL\"}");
                    throw new IOException("server full");
                }
                playerId = allocateId();
                state.getPlayers().put(playerId, new Player(1,1));
                send("{\"type\":\"WELCOME\",\"id\":" + playerId + "}");
                broadcastState();
            }
            System.out.println("[Server] player " + playerId + " joined");
        }       

        // ── 2. Read inputs, mutate GameState, broadcast ───────────────────────
        private void mainLoop() throws IOException {
            while (true) {
                String json = readJson(in); // Read JSON from input stream
                handleInput(json);
                broadcastState();
            }
        }

        // ── Helper: Read JSON from input stream ───────────────────────────────
        private String readJson(DataInputStream in) throws IOException {
            int length = in.readInt(); // Read the length of the incoming JSON
            byte[] data = new byte[length];
            in.readFully(data); // Read the JSON data
            return new String(data, StandardCharsets.UTF_8);
        }

        // ── 3. Translate key → new pos; reuse your collision logic here ──────
        private void handleInput(String json) {
            String key = gson.fromJson(json, Input.class).key;
            synchronized (state) {
                Player p = state.getPlayers().get(playerId);
                if (p == null) return;
        
                int x = p.getX(), y = p.getY();
                switch (key) {
                    case "UP"    -> y--;
                    case "DOWN"  -> y++;
                    case "LEFT"  -> x--;
                    case "RIGHT" -> x++;
                }
                if (isValid(x, y)) {
                    p.setPosition(x, y);
                    System.out.println("[DEBUG] Player " + playerId + " moved to (" + x + ", " + y + ")");
                } else {
                    System.out.println("[DEBUG] Invalid move for Player " + playerId + " to (" + x + ", " + y + ")");
                }
        
                // --- 額外安全檢查: 確保每個玩家都在 players map 且 id 唯一 ---
                if (state.getPlayers().size() > MAX_PLAYERS) {
                    System.err.println("[ERROR] Too many players in state! Resetting to max allowed.");
                    // 只保留前 MAX_PLAYERS 個 id
                    state.getPlayers().keySet().removeIf(id -> id > MAX_PLAYERS);
                }

                // --- 顏色分配交給 client MazePanel，這裡只需確保 id 唯一 ---

                if (x == state.getCols()-2 && y == state.getRows()-2 &&
                    state.getWinnerId() == 0) {                 // first one there wins
                    state.setWinnerId(playerId);                // remember for one broadcast
                    state.addPoint(playerId);                   // add a point

                    // build a fresh maze for the next round
                    state.incrementLevel();
                    MazeGenerator gen = new MazeGenerator(state.getRows(), state.getCols());
                    state.setMaze(gen.generate());

                    // reset every player to the start cell
                    for (Player pl : state.getPlayers().values()) pl.setPosition(1,1);

                    state.setWinnerId(0);                       // clear flag for new level
                    state.resetTimer();
                }
            }
        }

        // ── 4. Graceful shutdown ──────────────────────────────────────────────
        private void cleanup() {
            try { socket.close(); } catch (IOException ignored) { }
            synchronized (state) {
                state.getPlayers().remove(playerId);
            }
            clients.remove(this);
            broadcastState();
            System.out.println("[Server] player " + playerId + " left");
        }

        void send(String json) throws IOException {
            byte[] data = json.getBytes(StandardCharsets.UTF_8);
            out.writeInt(data.length);       // Write the length first
            out.write(data);                 // Then write the actual data
            out.flush();                     // Ensure it's sent immediately
        }


        // ── Helper: simple ID allocator (1..MAX_PLAYERS) ──────────────────────
        private int allocateId() {
            for (int i = 1; i <= MAX_PLAYERS; i++) {
                if (!state.getPlayers().containsKey(i)) return i;
            }
            return -1; // should never happen due to MAX_PLAYERS guard
        }

        // ── Stub: collision / bounds check;
        private boolean isValid(int x,int y) {
            Cell[][] m = state.getMaze();
            return x >= 0 && y >= 0 &&
                   x < state.getCols() && y < state.getRows() &&
                   !m[y][x].isWall();
        }
        // ── tiny record for JSON parse ────────────────────────────────────────
        record Input(String key) {}
    }

    // ───────────────────────────────────────────────────────────────────────────
    // ◼ Broadcast helper (visible to all ClientHandlers)
    // ───────────────────────────────────────────────────────────────────────────
    private void broadcastState() {
        String snap = gson.toJson(state);
        for (ClientHandler ch : clients) {
            try {
                ch.send(snap);
            } catch (IOException e) {
                clients.remove(ch);             // drop dead connections
            }
        }
    }
}
