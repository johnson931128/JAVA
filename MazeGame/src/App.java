import game.Continue;
import game.GameController;
import gui.LoginPanel;
import java.awt.Container;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maze Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // 創建遊戲控制器和面板 (傳遞 frame 進去)
            GameController game = new GameController(21, 21, frame);
            JPanel gamePanel = game.getGamePanel();

            // 使用數組來持有 loginPanel 的引用，以確保 final 或 effectively final
            final LoginPanel[] loginPanelHolder = new LoginPanel[1];

            // 創建切換邏輯的 Runnable
            Runnable switchAction = () -> {
                Container contentPane = frame.getContentPane();
                // 從持有器中獲取 loginPanel
                contentPane.remove(loginPanelHolder[0]); // 移除登錄面板
                contentPane.add(gamePanel);     // 添加遊戲面板
                contentPane.revalidate();       // 重新驗證佈局
                contentPane.repaint();          // 重繪界面
                // 將焦點請求放入 invokeLater
                SwingUtilities.invokeLater(() -> {
                    game.getUiMazePanel().requestFocusInWindow(); // 請求內部 MazePanel 的焦點
                });
            };

            // 實例化 LoginPanel 並將其存入持有器
            loginPanelHolder[0] = new LoginPanel(switchAction);
            
            // 連結 CONTINUE 按鈕事件
            loginPanelHolder[0].getContinueButton().addActionListener(e -> {
                Continue.SaveData save = Continue.loadGame();
                if (save != null) {
                    // 依照存檔初始化遊戲狀態
                    for (int i = 1; i < save.level; i++) game.startNextLevel();
                    game.setPlayerPosition(save.playerX, save.playerY);
                    // 切換到遊戲畫面
                    Container contentPane = frame.getContentPane();
                    contentPane.remove(loginPanelHolder[0]);
                    contentPane.add(gamePanel);
                    contentPane.revalidate();
                    contentPane.repaint();
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
                    game.getPlayerY()
                );
                JOptionPane.showMessageDialog(frame, "存檔成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            });
            
            // 連結 返回Menu 按鈕事件
            game.getGameUI().getBackToMenuButton().addActionListener(e -> {
                Container contentPane = frame.getContentPane();
                contentPane.remove(gamePanel); // 移除遊戲主畫面
                contentPane.add(loginPanelHolder[0]); // 加回登入/主選單
                contentPane.revalidate();
                contentPane.repaint();
            });
            
            // 將持有器中的面板添加到 frame
            frame.add(loginPanelHolder[0]); // 初始顯示登錄面板
            
            frame.pack(); // 根據內容調整窗口大小 (現在是 LoginPanel 的大小)
            frame.setSize(865,560);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
