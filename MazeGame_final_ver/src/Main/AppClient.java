package Main;

import net.RemoteGameController;
import gui.GameUI;         // ← we need this to cast/zero‐in on updateScoreLabel(...) 
import game.GameState;    // ← if you want to pull scores directly 
import javax.swing.*;
import java.util.Map;

public class AppClient {
    public static void main(String[] args) {
        String host = (args.length > 0 ? args[0] : "localhost");

        try {
            RemoteGameController rc = new RemoteGameController(host, 7777);
            rc.awaitBothPlayers();

            SwingUtilities.invokeLater(() -> {
                try {
                    GameUI ui = rc.getGameUI();

                    GameState gameState = rc.getGameState();
                    Map<Integer,Integer> initialScores = gameState.getScores();

                    System.out.println("Scores before updateScoreLabel(): " + initialScores);


                    ui.updateScoreLabel(initialScores);

                    String labelText = ui.getScoreLabel().getText();
                    System.out.println("ScoreLabel after updateScoreLabel(): " + labelText);


                    JFrame frame = new JFrame("Maze Multiplayer - Player " + rc.getMyId());
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    frame.add(rc.getPanel());
                    frame.setSize(1062, 694); // 調大視窗
                    frame.setLocationByPlatform(true);

                    frame.setVisible(true);

                    // 確保 MazePanel 取得焦點，否則 key bindings 不會觸發
                    rc.getGameUI().getMazePanel().requestFocusInWindow();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error showing game window: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Failed to connect or wait for players:\n" + e.getMessage()
            );
            e.printStackTrace();
        }
    }
}
