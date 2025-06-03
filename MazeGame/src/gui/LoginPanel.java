package gui;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import Main.App;

public class LoginPanel extends JPanel {

    private Runnable onNewGameAction;
    private final JButton continueButton;
    private final JButton newGameButton;

    public LoginPanel(Runnable onNewGameAction) {
        this.onNewGameAction = onNewGameAction;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 245, 220));

        add(Box.createVerticalGlue());

        JLabel titleLabel = new JLabel("MAZEGAME");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createRigidArea(new Dimension(0, 50)));

        continueButton = new JButton("CONTINUE");
        styleButton(continueButton);
        add(continueButton);

        add(Box.createRigidArea(new Dimension(0, 20)));

        newGameButton = new JButton("NEW GAME");
        styleButton(newGameButton);
        newGameButton.addActionListener(e -> {
            if (this.onNewGameAction != null) {
                this.onNewGameAction.run();
            }
        });
        add(newGameButton);

        add(Box.createRigidArea(new Dimension(0, 20)));

        add(Box.createVerticalGlue());

        setPreferredSize(new Dimension(1062, 694));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(200, 40));
    }

    public JButton getContinueButton() {
        return continueButton;
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }

    public void showLoginOptions() {
        this.removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 245, 220));
        add(Box.createVerticalGlue());

        JLabel titleLabel = new JLabel("MAZEGAME");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 50)));

        add(continueButton);
        add(Box.createRigidArea(new Dimension(0, 20)));

        JButton newGameButton = new JButton("NEW GAME");
        styleButton(newGameButton);
        newGameButton.addActionListener(e -> {
            if (this.onNewGameAction != null) {
                this.onNewGameAction.run();
            }
        });
        add(newGameButton);
        add(Box.createRigidArea(new Dimension(0, 20)));

        JButton settingButton = new JButton("SETTING");
        styleButton(settingButton);
        settingButton.addActionListener(e -> {
            JPanel settingPanel = new JPanel();
            settingPanel.setLayout(new GridLayout(2, 2, 10, 10));
            settingPanel.add(new JLabel("Maze Width:"));
            JTextField widthField = new JTextField(String.valueOf(App.getMazeWidth()));
            settingPanel.add(widthField);
            settingPanel.add(new JLabel("Maze Height:"));
            JTextField heightField = new JTextField(String.valueOf(App.getMazeHeight()));
            settingPanel.add(heightField);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    settingPanel,
                    "Settings",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int mazeWidth = Integer.parseInt(widthField.getText());
                    int mazeHeight = Integer.parseInt(heightField.getText());
                    App.setMazeWidth(mazeWidth);
                    App.setMazeHeight(mazeHeight);

                    // Write updated values to config.txt
                    try (FileWriter writer = new FileWriter("src/environment/config.txt")) {
                        writer.write("MAZE_WIDTH=" + mazeWidth + "\n");
                        writer.write("MAZE_HEIGHT=" + mazeHeight + "\n");
                    }

                    // Show restart message
                    JOptionPane.showMessageDialog(
                            this,
                            "Settings updated successfully! Please restart the game for changes to take effect.",
                            "Restart Required",
                            JOptionPane.WARNING_MESSAGE
                    );

                    System.exit(0); // Forcefully close the application
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid integers.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to save settings to file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(settingButton);

        add(Box.createVerticalGlue());
        setPreferredSize(new Dimension(1062, 694));
        revalidate();
        repaint();
    }

    public void showModeSelect() {
        this.removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 245, 220));
        add(Box.createVerticalGlue());
        JLabel titleLabel = new JLabel("MAZEGAME");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 50)));
        JButton singleButton = new JButton("SINGLE");
        styleButton(singleButton);
        singleButton.addActionListener(e -> showLoginOptions());
        add(singleButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        JButton multiButton = new JButton("MULTIPLE");
        styleButton(multiButton);
        add(multiButton);
        add(Box.createVerticalGlue());
        setPreferredSize(new Dimension(1062, 694));
        revalidate();
        repaint();
    }
}