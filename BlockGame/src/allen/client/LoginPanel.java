package allen.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginPanel extends JFrame {

    private String username = "";
    private String password = "";

    public LoginPanel() {

        this.setLayout(null);
        this.setVisible(true);
        this.setSize(300, 150);
        this.setLocation(300, 100);

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(10, 10, 80, 25);
        this.add(userLabel);

        final JTextField userText = new JTextField(20);
        userText.setBounds(100, 10, 160, 25);
        this.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 40, 80, 25);
        this.add(passwordLabel);

        final JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 40, 160, 25);
        this.add(passwordText);

        JButton loginButton = new JButton("login");
        loginButton.setBounds(10, 80, 80, 25);
        this.add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    username = userText.getText();
                    password = passwordText.getText();
                    MainThread.writer.write("login:-" + userText.getText() + "-" + passwordText.getText() + "\n");
                    MainThread.writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        JButton registerButton = new JButton("register");
        registerButton.setBounds(180, 80, 80, 25);
        this.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    MainThread.writer.write("register:-" + userText.getText() + "-" + passwordText.getText() + "\n");
                    MainThread.writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}