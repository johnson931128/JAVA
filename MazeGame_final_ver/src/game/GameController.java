package game;

import gui.GameUI;
import gui.MazePanel;
import java.awt.event.*;
import javax.swing.*;
import model.Cell;
import model.MazeGenerator;
import model.Player;
import util.AudioPlayer;
import java.util.HashMap;
import java.util.Map;
import Main.App;

public class GameController {
    private AudioPlayer audioPlayer;
    private GameState state;
    private GameUI ui;
    private Player player;
    private Cell[][] maze;
    private Timer timer;
    private MazeGenerator mazeGenerator;
    private JFrame frame;
    private long currentMazeSeed;

    public GameController(int initialRows, int initialCols, JFrame frame, AudioPlayer audioPlayer) {
        this.frame = frame;
        this.audioPlayer = audioPlayer;
        state = new GameState(initialRows, initialCols);
        initializeGame();
    }

    // 保留原本建構子（for 多人模式或相容性）
    public GameController(int initialRows, int initialCols, JFrame frame) {
        this(initialRows, initialCols, frame, null);
    }

    private void initializeGame() {
        // Initialize maze generator
        mazeGenerator = new MazeGenerator(state.getRows(), state.getCols());
        this.currentMazeSeed = mazeGenerator.getSeed();
        
        // Generate initial maze
        maze = mazeGenerator.generate();
        
        // Initialize player
        player = new Player(1, 1);
        
        Map<Integer, Player> singlePlayerMap = new HashMap<>();
        singlePlayerMap.put(1, player);
        if (audioPlayer != null) {
            ui = new GameUI(maze, player, state, audioPlayer);
        } else {
            ui = new GameUI(maze, player, state);
        }
        ui.updateMazePanel(maze, singlePlayerMap);
        
        // Setup controls
        setupKeyListener();
        
        // Initialize timer
        setupTimer();
    }

    private void setupTimer() {
        timer = new Timer(1000, e -> {
            state.incrementTimer();
            ui.updateTimerLabel();
        });
        timer.start();
    }

    private void setupKeyListener() {
        ui.getMazePanel().setFocusable(true);
        ui.getMazePanel().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Key Pressed: " + e.getKeyCode());
                handleKeyPress(e.getKeyCode());
            }
        });
        ui.getMazePanel().requestFocusInWindow();
    }

    private void handleKeyPress(int keyCode) {
        int newX = player.getX();
        int newY = player.getY();

        switch (keyCode) {
            case KeyEvent.VK_UP -> newY--;
            case KeyEvent.VK_DOWN -> newY++;
            case KeyEvent.VK_LEFT -> newX--;
            case KeyEvent.VK_RIGHT -> newX++;
        }

        if (isValidMove(newX, newY)) {
            player.setPosition(newX, newY);
            ui.getMazePanel().repaint();
            checkWin();
        }
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < state.getCols() && 
               y >= 0 && y < state.getRows() && 
               !maze[y][x].isWall();
    }

    private void checkWin() {
        if (player.getX() == state.getCols() - 2 && 
            player.getY() == state.getRows() - 2) {
            handleLevelComplete();
        }
    }

    private void handleLevelComplete() {
        timer.stop();
        state.updateBestTime(state.getSeconds());
        // 修正：showLevelCompleteDialog 不再回傳 boolean，僅顯示對話框，需自行判斷是否繼續
        int choice = JOptionPane.showConfirmDialog(ui.getMazePanel(),
            String.format("Level %d Complete!\nTime: %d seconds\nBest Time: %s\n\nContinue to next level?",
                state.getCurrentLevel(),
                state.getSeconds(),
                state.getBestTime() == Integer.MAX_VALUE ? "-" : state.getBestTime() + " sec"),
            "Level Complete!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            startNextLevel();
        } else {
            System.exit(0);
        }
    }

    public void startNextLevel() {
        state.incrementLevel();
        
        // Generate new maze
        mazeGenerator = new MazeGenerator(state.getRows(), state.getCols());
        this.currentMazeSeed = mazeGenerator.getSeed();
        maze = mazeGenerator.generate();
        
        // Reset player position
        player.setPosition(1, 1);
        
        // Reset timer
        state.resetTimer();
        
        // Update UI
        Map<Integer, Player> singlePlayerMap = new HashMap<>();
        singlePlayerMap.put(1, player);
        ui.updateMazePanel(maze, singlePlayerMap);
        setupKeyListener();
        // Restart timer
        timer.start();
    }

    public void setPlayerPosition(int x, int y) {
        player.setPosition(x, y);
        ui.getMazePanel().repaint();
    }

    public JPanel getGamePanel() {
        return ui.getMainPanel();
    }

    public MazePanel getUiMazePanel() {
        return ui.getMazePanel();
    }

    public GameUI getGameUI() {
        return ui;
    }

    public int getCurrentLevel() {
        return state.getCurrentLevel();
    }

    public int getPlayerX() {
        return player.getX();
    }

    public int getPlayerY() {
        return player.getY();
    }

    public int getMazeRows() {
        return state.getRows();
    }

    public int getMazeCols() {
        return state.getCols();
    }

    public long getCurrentMazeSeed() {
        return currentMazeSeed;
    }

    public void loadGameData(int level, int playerX, int playerY, long mazeSeed) {
        state.setCurrentLevel(level);
        state.setRows(state.getInitialRows() + (level - 1) * 2);
        state.setCols(state.getInitialCols() + (level - 1) * 2);

        mazeGenerator = new MazeGenerator(state.getRows(), state.getCols(), mazeSeed);
        this.currentMazeSeed = mazeSeed;
        maze = mazeGenerator.generate();
        
        player.setPosition(playerX, playerY);
        
        state.resetTimer();
        Map<Integer, Player> singlePlayerMap = new HashMap<>();
        singlePlayerMap.put(1, player);
        ui.updateMazePanel(maze, singlePlayerMap);
        setupKeyListener();
        timer.start();
    }
}