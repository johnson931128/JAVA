package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import EVENT.AuthManager; // Correctly import the AuthManager class

public class loginGUI extends JFrame {
    public loginGUI() {
        setTitle("StudentSystem");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); 

        // 建立元件
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");

        // 設定元件位置
        userLabel.setBounds(100, 50, 100, 30);
        userField.setBounds(200, 50, 200, 30);
        passLabel.setBounds(100, 100, 100, 30);
        passField.setBounds(200, 100, 200, 30);
        loginButton.setBounds(125, 180, 100, 40);
        cancelButton.setBounds(245, 180, 100, 40);

        // 加入元件到視窗
        add(userLabel);
        add(userField);
        add(passLabel);
        add(passField);
        add(loginButton);
        add(cancelButton);

        // 按鈕事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword()); // JPasswordField 要用 getPassword()

                // 呼叫 AuthManager 來驗證帳號密碼
                if (AuthManager.authenticate(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 取消按鈕的動作
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userField.setText(""); // 清空輸入
                passField.setText("");
            }
        });
    }
}


