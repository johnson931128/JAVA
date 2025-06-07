package gui;

import Main.App;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.BiConsumer;
import javax.swing.*;

public class LoginPanel extends JPanel {
    private ImageIcon backgroundImage;
    private Runnable onNewGameAction;
    private final JButton continueButton;
    private final JButton newGameButton;
    private final JButton multiplayerButton;
    private BiConsumer<String, String> onMultiplayerModeSelectedAction;

    public LoginPanel(Runnable onNewGameAction, BiConsumer<String, String> onMultiplayerModeSelectedAction) {
        this.onNewGameAction = onNewGameAction;
        this.onMultiplayerModeSelectedAction = onMultiplayerModeSelectedAction;
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

        // "MULTIPLE" 按鈕
        multiplayerButton = new JButton("MULTIPLE");
        styleButton(multiplayerButton);
        multiplayerButton.addActionListener(e -> {
            if (onMultiplayerModeSelectedAction != null) {
                onMultiplayerModeSelectedAction.accept("localhost", "7777");
            } else {
                JOptionPane.showMessageDialog(this, "請先啟動伺服器，再點選多人模式！", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        add(multiplayerButton);

        // 添加按鈕之間的間隔
        add(Box.createRigidArea(new Dimension(0, 20))); // 垂直間隔

        // 新增音樂控制按鈕
        JButton audioButton = new JButton("AUDIO");
        styleButton(audioButton);
        audioButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "背景音樂會自動播放，若要暫停請關閉遊戲視窗。", "Audio Info", JOptionPane.INFORMATION_MESSAGE);
        });
        add(audioButton);

        // 添加底部空白間隔
        add(Box.createVerticalGlue());

        setPreferredSize(new Dimension(1062, 694)); // 與遊戲面板大小一致

        backgroundImage = new ImageIcon(getClass().getResource("/image/background.png")); // 或 background.jpg
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
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

    public JButton getMultiplayerButton() {
        return multiplayerButton;
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
            SettingPanel settingPanel = new SettingPanel(App.getMazeWidth(), App.getMazeHeight());

            int result = JOptionPane.showConfirmDialog(
                    this,
                    settingPanel,
                    "Settings",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int mazeWidth = settingPanel.getMazeWidth();
                    int mazeHeight = settingPanel.getMazeHeight();

                    try (FileWriter writer = new FileWriter("src/environment/config.txt")) {
                        writer.write("MAZE_WIDTH=" + mazeWidth + "\n");
                        writer.write("MAZE_HEIGHT=" + mazeHeight + "\n");
                    }

                    App.setMazeWidth(mazeWidth);
                    App.setMazeHeight(mazeHeight);

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
        multiButton.addActionListener(e -> showMultiplayerOptions());
        add(multiButton);
        add(Box.createVerticalGlue());
        setPreferredSize(new Dimension(1062, 694));
        revalidate();
        repaint();
    }

    public void showMultiplayerOptions() {
        this.removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 245, 220));
        add(Box.createVerticalGlue());
        JLabel titleLabel = new JLabel("MULTIPLAYER");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 50)));

        JButton serverButton = new JButton("START SERVER");
        styleButton(serverButton);
        serverButton.addActionListener(e -> {
            if (onMultiplayerModeSelectedAction != null) {
                onMultiplayerModeSelectedAction.accept("server", null);
            }
        });
        add(serverButton);
        add(Box.createRigidArea(new Dimension(0, 20)));

        JButton clientButton = new JButton("CONNECT TO SERVER");
        styleButton(clientButton);
        clientButton.addActionListener(e -> {
            if (onMultiplayerModeSelectedAction != null) {
                String host = JOptionPane.showInputDialog(this, "Enter server IP address:", "localhost");
                if (host != null && !host.trim().isEmpty()) {
                    onMultiplayerModeSelectedAction.accept("client", host.trim());
                } else if (host != null) {
                    JOptionPane.showMessageDialog(this, "IP address cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(clientButton);
        add(Box.createRigidArea(new Dimension(0, 20)));

        JButton backButton = new JButton("BACK");
        styleButton(backButton);
        backButton.addActionListener(e -> showModeSelect());
        add(backButton);

        add(Box.createVerticalGlue());
        setPreferredSize(new Dimension(1062, 694));
        revalidate();
        repaint();
    }
}