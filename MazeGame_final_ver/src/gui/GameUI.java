package gui;

import game.GameState;
import java.awt.*;
import javax.swing.*;
import model.Cell;
import model.Player;
import util.AudioPlayer;
import java.util.Map;

public class GameUI {
    private int lastLevel = 1;
    private AudioPlayer audioPlayer; // 新增
    private JButton audioButton;     // 新增
    private JPanel mainPanel;
    private MazePanel mazePanel;
    private JLabel timerLabel;
    private JLabel scoreLabel;
    private final GameState gameState;
    private final JButton saveButton = new JButton("SAVE");
    private final JButton backToMenuButton = new JButton("Menu");
    private boolean isMultiplayer = false;

    public JButton getSaveButton() { return saveButton; }
    public JButton getBackToMenuButton() { return backToMenuButton; }

    public GameUI(Cell[][] maze, Player player, GameState gameState) {
        this(maze, player, gameState, false);
    }

    // 新增建構子，支援 AudioPlayer
    public GameUI(Cell[][] maze, Player player, GameState gameState, AudioPlayer audioPlayer, boolean isMultiplayer) {
        this.gameState = gameState;
        this.isMultiplayer = isMultiplayer;
        this.audioPlayer = audioPlayer;
        initializeUI(maze, gameState.getPlayers());
    }

    // 單人模式預設 isMultiplayer=false
    public GameUI(Cell[][] maze, Player player, GameState gameState, AudioPlayer audioPlayer) {
        this(maze, player, gameState, audioPlayer, false);
    }

    // 新增建構子，支援多人模式
    public GameUI(Cell[][] maze, Player player, GameState gameState, boolean isMultiplayer) {
        this.gameState = gameState;
        this.isMultiplayer = isMultiplayer;
        initializeUI(maze, gameState.getPlayers());
    }

    private void initializeUI(Cell[][] maze, Map<Integer, Player> players) {
        // Initialize score label
        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        updateScoreLabel(null); // 初始可設為空或 0 分

        // Initialize timer label
        timerLabel = new JLabel();
        updateTimerLabel();
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Initialize save button
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setFocusPainted(false);
        saveButton.setBackground(new Color(245, 245, 220));
        
        // Initialize back to menu button
        backToMenuButton.setFont(new Font("Arial", Font.BOLD, 16));
        backToMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToMenuButton.setFocusPainted(false);
        backToMenuButton.setBackground(new Color(245, 245, 220));
        
        // Initialize audio button (僅單人模式顯示)
        if (!isMultiplayer && audioPlayer != null) {
            audioButton = new JButton("AUDIO");
            audioButton.setFont(new Font("Arial", Font.BOLD, 16));
            audioButton.setBackground(new Color(245, 245, 220));
            audioButton.setFocusPainted(false);
            audioButton.addActionListener(e -> {
                if (audioPlayer.isPaused()) {
                    audioPlayer.resume();
                    JOptionPane.showMessageDialog(mainPanel, "音樂已繼續播放！", "AUDIO", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    audioPlayer.pause();
                    JOptionPane.showMessageDialog(mainPanel, "音樂已暫停！", "AUDIO", JOptionPane.INFORMATION_MESSAGE);
                }
                // 修正：每次按下 AUDIO 後自動把焦點還給 mazePanel
                if (mazePanel != null) mazePanel.requestFocusInWindow();
            });
        }

        // Initialize maze panel
        mazePanel = new MazePanel(maze, players, timerLabel);
        
        // Create main panel with timer at bottom
        mainPanel = new JPanel(new BorderLayout());
        JPanel timerPanel = new JPanel();
        timerPanel.add(scoreLabel); // 新增分數顯示
        timerPanel.add(timerLabel);
        if (!isMultiplayer) {
            timerPanel.add(saveButton);
            timerPanel.add(backToMenuButton);
            if (audioButton != null) timerPanel.add(audioButton); // 新增 AUDIO 按鈕
        }
        mainPanel.add(mazePanel, BorderLayout.CENTER);
        mainPanel.add(timerPanel, BorderLayout.SOUTH);
    }

    // 新增：分數顯示方法
    public void updateScoreLabel(Map<Integer, Integer> scores) {
        if (scores == null || scores.isEmpty()) {
            scoreLabel.setText("Scores: N/A");
        } else {
            StringBuilder sb = new StringBuilder("Scores: ");
            for (Map.Entry<Integer, Integer> entry : scores.entrySet()) {
                sb.append("P").append(entry.getKey()).append(": ").append(entry.getValue()).append("  ");
            }
            scoreLabel.setText(sb.toString().trim());
        }
    }

    // 新增：取得分數 JLabel
    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    public void updateTimerLabel() {
        timerLabel.setText(String.format("Level: %d | Time: %d sec | Best: %s",
            gameState.getCurrentLevel(),
            gameState.getSeconds(),
            gameState.getBestTime() == Integer.MAX_VALUE ? "-" : gameState.getBestTime() + " sec"));
    }

    public void updateMazePanel(Cell[][] maze, Map<Integer, Player> players) {
        MazePanel newMazePanel = new MazePanel(maze, players, timerLabel);
        mainPanel.remove(mazePanel);
        mainPanel.add(newMazePanel, BorderLayout.CENTER);
        mazePanel = newMazePanel;
        mainPanel.revalidate();
        mainPanel.repaint();
        mazePanel.requestFocusInWindow();
    }

    public void showLevelCompleteDialog() {
        int choice = JOptionPane.showConfirmDialog(mazePanel,
            String.format("Level %d Complete!\nTime: %d seconds\nBest Time: %s\n\nContinue to next level?",
                gameState.getCurrentLevel(),
                gameState.getSeconds(),
                gameState.getBestTime() == Integer.MAX_VALUE ? "-" : gameState.getBestTime() + " sec"),
            "Level Complete!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE);
        // 只顯示對話框，不處理關卡推進，讓 GameController 根據回傳值決定
        // 若需要回傳選擇，可改為 boolean 或 int
        if (choice == JOptionPane.YES_OPTION) {
            // 由 GameController 呼叫 startNextLevel()
        }
    }

    // ─── 改寫 updateFromNetwork ─────────────────────────────
    public void updateFromNetwork(GameState snap, int myId) {
        updateScoreLabel(snap.getScores());
        timerLabel.setText(String.format(
            "Level: %d | Time: %d sec | Best: %s",
            snap.getCurrentLevel(), snap.getSeconds(),
            snap.getBestTime()==Integer.MAX_VALUE? "-" : snap.getBestTime()+" sec"));

        // ① 沒換關 → 只換 players，省掉重建與焦點遺失
        if (snap.getCurrentLevel() == lastLevel) {
            mazePanel.setPlayers(snap.getPlayers());
            mazePanel.repaint();
        }
        // ② 換關 → 重建 MazePanel；鍵盤稍後由 RemoteGameController 重新安裝
        else {
            updateMazePanel(snap.getMaze(), snap.getPlayers());
            lastLevel = snap.getCurrentLevel();
        }
    }

    public MazePanel getMazePanel() { return mazePanel; }
    public JPanel getMainPanel() { return mainPanel; }
    public JButton getAudioButton() { return audioButton; }
}