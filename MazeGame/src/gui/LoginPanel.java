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
        // 設置佈局管理器為 BoxLayout，垂直排列
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 245, 220)); // 米色背景

        add(Box.createVerticalGlue());

        JLabel titleLabel = new JLabel("MAZEGAME");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36)); // 設置字體和大小
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 居中對齊
        add(titleLabel);

        // 添加標題和按鈕之間的間隔
        add(Box.createRigidArea(new Dimension(0, 50))); // 垂直間隔

        // "CONTINUE" 按鈕
        continueButton = new JButton("CONTINUE");
        styleButton(continueButton);
        add(continueButton);

        // 添加按鈕之間的間隔
        add(Box.createRigidArea(new Dimension(0, 20))); // 垂直間隔

        // "NEW GAME" 按鈕
        newGameButton = new JButton("NEW GAME");
        styleButton(newGameButton);
        newGameButton.addActionListener(e -> {
            if (this.onNewGameAction != null) {
                this.onNewGameAction.run();
            }
        });
        add(newGameButton);

        // 添加按鈕之間的間隔
        add(Box.createRigidArea(new Dimension(0, 20))); // 垂直間隔

        // 添加底部空白間隔
        add(Box.createVerticalGlue());

        setPreferredSize(new Dimension(1062, 694)); // 與遊戲面板大小一致
    }

    // 輔助方法來設置按鈕樣式
    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // 居中對齊
        button.setFocusPainted(false); // 去除焦點邊框
        // 設置按鈕的最大尺寸以控制寬度
        button.setMaximumSize(new Dimension(200, 40));
    }

    // 如果需要，可以添加獲取按鈕的方法，以便在外部添加監聽器
    public JButton getContinueButton() {
        return continueButton;
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