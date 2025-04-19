import javax.swing.*;
import game.GameController;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maze Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            GameController game = new GameController(41, 71);
            frame.add(game.getGamePanel());
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
