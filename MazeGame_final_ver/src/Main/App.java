package Main;

import game.Continue;
import game.GameController;
import gui.LoginPanel;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import util.AudioPlayer;

public class App {
    private static int MAZE_WIDTH;
    private static int MAZE_HEIGHT;
    private static final int FRAME_WIDTH = 1062;
    private static final int FRAME_HEIGHT = 694;

    private static void loadConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/environment/config.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "MAZE_WIDTH" -> MAZE_WIDTH = Integer.parseInt(value);
                        case "MAZE_HEIGHT" -> MAZE_HEIGHT = Integer.parseInt(value);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to load configuration file: " + e.getMessage());
            MAZE_WIDTH = 21; // Default value
            MAZE_HEIGHT = 21; // Default value
        }
    }

    public static int getMazeWidth() {
        return MAZE_WIDTH;
    }

    public static int getMazeHeight() {
        return MAZE_HEIGHT;
    }

    public static void setMazeWidth(int width) {
        MAZE_WIDTH = width;
    }

    public static void setMazeHeight(int height) {
        MAZE_HEIGHT = height;
    }

    public static void main(String[] args) {
        loadConfig();

        // 新增：啟動背景音樂
        AudioPlayer audioPlayer = new AudioPlayer();
        audioPlayer.playLoop("audio/music.mp3");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maze Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            final GameController[] gameHolder = new GameController[1];
            final JPanel[] gamePanelHolder = new JPanel[1];
            final LoginPanel[] loginPanelHolder = new LoginPanel[1];

            Runnable switchAction = () -> {
                gameHolder[0] = new GameController(MAZE_WIDTH, MAZE_HEIGHT, frame, audioPlayer);
                gamePanelHolder[0] = gameHolder[0].getGamePanel();
                switchPanel(frame, loginPanelHolder[0], gamePanelHolder[0]);
                SwingUtilities.invokeLater(() -> gameHolder[0].getUiMazePanel().requestFocusInWindow());

                // 註冊遊戲內按鈕事件
                gameHolder[0].getGameUI().getSaveButton().addActionListener(e -> {
                    Continue.saveGame(
                            gameHolder[0].getCurrentLevel(),
                            gameHolder[0].getPlayerX(),
                            gameHolder[0].getPlayerY(),
                            gameHolder[0].getCurrentMazeSeed(),
                            gameHolder[0].getMazeRows(),
                            gameHolder[0].getMazeCols()
                    );
                    JOptionPane.showMessageDialog(frame, "存檔成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                });

                gameHolder[0].getGameUI().getBackToMenuButton().addActionListener(e -> switchPanel(frame, gamePanelHolder[0], loginPanelHolder[0]));
            };

            loginPanelHolder[0] = new LoginPanel(switchAction, (host, port) -> {
                // 啟動多人模式（client）
                try {
                    ProcessBuilder pb = new ProcessBuilder(
                        "java", "-cp", "lib/gson-2.10.1.jar;MazeGame.jar", "Main.AppClient", "localhost"
                    );
                    pb.inheritIO();
                    pb.start();
                    JOptionPane.showMessageDialog(frame, "已啟動多人模式視窗，請切換至新開啟的遊戲視窗！", "提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "啟動多人模式失敗：" + ex.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
                }
            });
            loginPanelHolder[0].showModeSelect();

            loginPanelHolder[0].getContinueButton().addActionListener(e -> {
                gameHolder[0] = new GameController(MAZE_WIDTH, MAZE_HEIGHT, frame, audioPlayer);
                gamePanelHolder[0] = gameHolder[0].getGamePanel();
                Continue.SaveData save = Continue.loadGame(MAZE_WIDTH, MAZE_HEIGHT);
                if (save != null) {
                    gameHolder[0].loadGameData(save.level, save.playerX, save.playerY, save.mazeSeed);
                    switchPanel(frame, loginPanelHolder[0], gamePanelHolder[0]);
                    SwingUtilities.invokeLater(() -> {
                        gameHolder[0].getUiMazePanel().requestFocusInWindow();
                    });

                    // 註冊遊戲內按鈕事件
                    gameHolder[0].getGameUI().getSaveButton().addActionListener(e2 -> {
                        Continue.saveGame(
                                gameHolder[0].getCurrentLevel(),
                                gameHolder[0].getPlayerX(),
                                gameHolder[0].getPlayerY(),
                                gameHolder[0].getCurrentMazeSeed(),
                                gameHolder[0].getMazeRows(),
                                gameHolder[0].getMazeCols()
                        );


                        JOptionPane.showMessageDialog(frame, "存檔成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                    });

                    gameHolder[0].getGameUI().getBackToMenuButton().addActionListener(e2 -> {

                        switchPanel(frame, gamePanelHolder[0], loginPanelHolder[0]);
                    });
                }
            });

            frame.add(loginPanelHolder[0]);
            frame.pack();
            frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void switchPanel(JFrame frame, JPanel panelToRemove, JPanel panelToAdd) {
        Container contentPane = frame.getContentPane();
        contentPane.remove(panelToRemove);
        contentPane.add(panelToAdd);
        contentPane.revalidate();
        contentPane.repaint();
    }
}