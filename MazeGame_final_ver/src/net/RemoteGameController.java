package net;

import com.google.gson.Gson;
import game.GameState;
import gui.GameUI;
import java.io.*;
import java.net.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.charset.StandardCharsets;
import javax.swing.*;

public class RemoteGameController {
    private int currentLevel = 1;
    private final Socket socket;
    private final DataOutputStream out;
    private final DataInputStream  in;
    private final Gson gson = new Gson();
    private final CountDownLatch ready = new CountDownLatch(1);
    private boolean latchReleased = false;

    private GameUI ui;
    private GameState gameState;
    private int myId = -1;          // default until server says otherwise

    // ────────────────────────────────────────────────────────────────────────────
    public RemoteGameController(String host, int port) throws IOException {
        // 1. CONNECT ────────────────────────────────────────────────────────────
        socket = new Socket(host, port);
        out    = new DataOutputStream(socket.getOutputStream());
        in     = new DataInputStream(socket.getInputStream());

        // 2. HANDSHAKE ──────────────────────────────────────────────────────────
        Welcome hello = gson.fromJson(readJson(), Welcome.class);
        this.myId = hello.id();

        if ("FULL".equals(hello.type())) {
            JOptionPane.showMessageDialog(null, "Server full");
            throw new IOException("server full");
        }
        System.out.println("You have joined the game as Player " + myId);

        //GameState initState = gson.fromJson(readJson(), GameState.class);

        startReceiverThread();
    }

    // ────────────────────────────────────────────────────────────────────────────
    /** Install arrow keys for everyone; add extra WASD if this client is player 1. */
    private void installKeyBindings() {
        JComponent c = ui.getMazePanel();

        System.out.println("[DEBUG] installKeyBindings for myId=" + myId);

        InputMap  im = c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = c.getActionMap();

        // 先清除舊的 key 綁定，避免重複
        im.clear();
        am.clear();
        // 強制 MazePanel 取得焦點（多次呼叫，確保切換 UI 後也能拿到）
        c.setFocusable(true);
        c.requestFocusInWindow();
        // Player 1 只監聽 WASD，Player 2 只監聽方向鍵
        if (myId == 1) {
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "UP");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "LEFT");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "DOWN");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "RIGHT");
        } else if (myId == 2) {
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,    0), "UP");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,  0), "LEFT");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,  0), "DOWN");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RIGHT");
        }
        // 綁定動作
        am.put("UP", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                System.out.println("[DEBUG] Key pressed: UP by myId=" + myId);
                sendInput("UP");
            }
        });
        am.put("DOWN", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                System.out.println("[DEBUG] Key pressed: DOWN by myId=" + myId);
                sendInput("DOWN");
            }
        });
        am.put("LEFT", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                System.out.println("[DEBUG] Key pressed: LEFT by myId=" + myId);
                sendInput("LEFT");
            }
        });
        am.put("RIGHT", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                System.out.println("[DEBUG] Key pressed: RIGHT by myId=" + myId);
                sendInput("RIGHT");
            }
        });
        // 再次強制請求焦點，確保 key binding 生效
        SwingUtilities.invokeLater(c::requestFocusInWindow);
    }

    private void sendInput(String key) {
        try {
            System.out.println("[DEBUG] Sending input: " + key);
            sendJson(new Input(key));
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to send input: " + e.getMessage());
        }
    }

    // ────────────────────────────────────────────────────────────────────────────
    private void startReceiverThread() {
        new Thread(() -> {
            try {
                while (true) {
                    GameState snap = gson.fromJson(readJson(), GameState.class);
                    this.gameState = snap;
    
                    // Build UI when both players are present
                    if (!latchReleased && snap.getPlayers().size() == 2) {
                        latchReleased = true;
    
                        // ⏳ Only now we build the UI
                        ui = new GameUI(
                            snap.getMaze(),
                            null,          // 不再只傳自己
                            snap,
                            true           // 多人模式
                        );
                        currentLevel = snap.getCurrentLevel();  // ★ 記下當前關卡
                        ui.updateScoreLabel(snap.getScores());
                        installKeyBindings();
                        ready.countDown();
                    }
    
                    // wait until UI is ready before updating it
                    if (latchReleased) {
                        final int levelFromServer = snap.getCurrentLevel();   // 暫存避免 lambda 捕到舊值
                        SwingUtilities.invokeLater(() -> {
                            ui.updateFromNetwork(snap, myId);
                    
                            // ★ 如果偵測到關卡變了 → 重新安裝 key bindings
                            if (levelFromServer != currentLevel) {
                                currentLevel = levelFromServer;
                                installKeyBindings();
                            }
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // 將忽略的 IOException 替換為列印堆疊追蹤
            }
        }, "maze-receiver").start();
    }

    // ── public helpers ─────────────────────────────────────────────────────────
    public JPanel getPanel() {
        if (ui == null) throw new IllegalStateException("UI not ready yet");
        return ui.getMainPanel();
    }
    public int    getMyId()       { return myId; }
    public void   requestFocus()  { ui.getMazePanel().requestFocusInWindow(); }

    // ── plumbing ───────────────────────────────────────────────────────────────
    private record Welcome(String type, int id) {}
    private record Input(String key) {}

    private void sendJson(Object obj) throws IOException {
        String json = gson.toJson(obj);
        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        out.writeInt(data.length);
        out.write(data);
        out.flush();
    }

    private String readJson() throws IOException {
        int len = in.readInt();
        byte[] buf = new byte[len];
        in.readFully(buf);
        return new String(buf, StandardCharsets.UTF_8);
    }

    public void awaitBothPlayers() throws InterruptedException {
        ready.await();  // blocks until countdown reaches 0
    }

    /** Return the UI object (so callers can do ui.updateScoreLabel(...)). */
    public GameUI getGameUI() {
        if (ui == null) throw new IllegalStateException("UI not ready yet");
        return ui;
    }

    /** Return the GameState object (so callers can inspect scores, level, etc.). */
    public GameState getGameState() {
        if (gameState == null) throw new IllegalStateException("GameState not initialized yet");
        return gameState;
    }
}
