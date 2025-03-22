import javax.swing.SwingUtilities;
import GUI.loginGUI;

public class App {
    public static void main(String[] args) {
        // 使用 Swing 事件派發執行緒 (EDT) 啟動 GUI
        SwingUtilities.invokeLater(() -> {
            loginGUI login = new loginGUI();
            login.setVisible(true);
        });
    }
}
