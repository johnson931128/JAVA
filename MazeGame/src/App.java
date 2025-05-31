import game.Continue;
import game.GameController;
import gui.LoginPanel;
import java.awt.Container;
import javax.swing.*;

public class App {
    private static final int MAZE_WIDTH = 21;
    private static final int MAZE_HEIGHT = 21;
    private static final int FRAME_WIDTH = 1062;
    private static final int FRAME_HEIGHT = 694;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maze Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // 創建遊戲控制器和面板 (傳遞 frame 進去)
            GameController game = new GameController(MAZE_WIDTH, MAZE_HEIGHT, frame);
            JPanel gamePanel = game.getGamePanel();

            // 使用數組來持有 loginPanel 的引用，以確保 final 或 effectively final
            final LoginPanel[] loginPanelHolder = new LoginPanel[1];

            // 創建切換邏輯的 Runnable
            Runnable switchAction = () -> {
                switchPanel(frame, loginPanelHolder[0], gamePanel);
                // 將焦點請求放入 invokeLater
                SwingUtilities.invokeLater(() -> {
                    game.getUiMazePanel().requestFocusInWindow(); // 請求內部 MazePanel 的焦點
                });
            };

            // 實例化 LoginPanel 並將其存入持有器
            loginPanelHolder[0] = new LoginPanel(switchAction);
            loginPanelHolder[0].showModeSelect();
            
            // 連結 CONTINUE 按鈕事件
            loginPanelHolder[0].getContinueButton().addActionListener(e -> {
                Continue.SaveData save = Continue.loadGame();
                if (save != null) {
                    // 依照存檔初始化遊戲狀態
                    game.loadGameData(save.level, save.playerX, save.playerY, save.mazeSeed);
                    // 切換到遊戲畫面
                    switchPanel(frame, loginPanelHolder[0], gamePanel);
                    SwingUtilities.invokeLater(() -> {
                        game.getUiMazePanel().requestFocusInWindow();
                    });
                } else {
                    JOptionPane.showMessageDialog(frame, "沒有存檔可繼續，請先開始新遊戲！", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            // 連結 SAVE 按鈕事件
            game.getGameUI().getSaveButton().addActionListener(e -> {
                Continue.saveGame(
                    game.getCurrentLevel(),
                    game.getPlayerX(),
                    game.getPlayerY(),
                    game.getCurrentMazeSeed()
                );
                JOptionPane.showMessageDialog(frame, "存檔成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            });
            
            // 連結 返回Menu 按鈕事件
            game.getGameUI().getBackToMenuButton().addActionListener(e -> {
                switchPanel(frame, gamePanel, loginPanelHolder[0]);
            });
            
            // 將持有器中的面板添加到 frame
            frame.add(loginPanelHolder[0]); // 初始顯示登錄面板
            
            frame.pack(); // 根據內容調整窗口大小 (現在是 LoginPanel 的大小)
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
