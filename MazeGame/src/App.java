import game.GameController;
import gui.LoginPanel;
import java.awt.Container;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maze Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // 創建遊戲控制器和面板 (先創建好，以便傳遞給 LoginPanel)
            GameController game = new GameController(41, 71);
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
            
            // 將持有器中的面板添加到 frame
            frame.add(loginPanelHolder[0]); // 初始顯示登錄面板
            
            frame.pack(); // 根據內容調整窗口大小 (現在是 LoginPanel 的大小)
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
