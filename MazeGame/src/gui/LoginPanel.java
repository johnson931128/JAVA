package gui;

import java.awt.*;
import javax.swing.*;

public class LoginPanel extends JPanel {

    private Runnable onNewGameAction;

    public LoginPanel(Runnable onNewGameAction) {
        this.onNewGameAction = onNewGameAction;
        // 設置佈局管理器為 BoxLayout，垂直排列
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 245, 220)); // 米色背景

        // 添加頂部空白間隔
        add(Box.createVerticalGlue());

        // 標題 "MAZEGAME"
        JLabel titleLabel = new JLabel("MAZEGAME");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36)); // 設置字體和大小
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 居中對齊
        add(titleLabel);

        // 添加標題和按鈕之間的間隔
        add(Box.createRigidArea(new Dimension(0, 50))); // 垂直間隔

        // "CONTINUE" 按鈕
        JButton continueButton = new JButton("CONTINUE");
        styleButton(continueButton);
        add(continueButton);

        // 添加按鈕之間的間隔
        add(Box.createRigidArea(new Dimension(0, 20))); // 垂直間隔

        // "NEW GAME" 按鈕
        JButton newGameButton = new JButton("NEW GAME");
        styleButton(newGameButton);
        newGameButton.addActionListener(e -> {
            if (this.onNewGameAction != null) {
                this.onNewGameAction.run();
            }
        });
        add(newGameButton);

        // 添加按鈕之間的間隔
        add(Box.createRigidArea(new Dimension(0, 20))); // 垂直間隔

        // "SIGN UP" 按鈕
        JButton signUpButton = new JButton("SIGN UP");
        styleButton(signUpButton);
        add(signUpButton);

        // 添加底部空白間隔
        add(Box.createVerticalGlue());

        // 設置面板的首選大小
        setPreferredSize(new Dimension(852, 492)); // 與遊戲面板大小一致
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
    // public JButton getContinueButton() { ... }
    // public JButton getNewGameButton() { ... }
    // public JButton getSignUpButton() { ... }
} 