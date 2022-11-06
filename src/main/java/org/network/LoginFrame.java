package org.network;

import org.network.panel.BackgroundPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JLayeredPane contentPane;
    private JTextField loginTextField;
    private JTextField passwordTextField;
    private JButton signInBtn;
    private JButton loginBtn;
    public LoginFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);
        setResizable(false);
        contentPane = new JLayeredPane();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel("backgrounds/pokenet_normal.png");
        backgroundPanel.setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);
        backgroundPanel.setLocation(0,0);
        contentPane.add(backgroundPanel,0);

        int padding = 50;
        BackgroundPanel loginBackground = new BackgroundPanel("ui/speechbox.png");
        loginBackground.setSize(340,200);
        loginBackground.setLocation(WindowConfig.WIDTH - loginBackground.getWidth() - padding ,WindowConfig.HEIGHT - loginBackground.getHeight() - padding);
        contentPane.add(loginBackground,0);



        JLabel loginLabel = new JLabel("Login");
        loginLabel.setLocation(loginBackground.getX() + 20,loginBackground.getY() + 20);
        loginLabel.setSize(60,30);
        contentPane.add(loginLabel, 0);

        loginTextField = new JTextField();
        loginTextField.setHorizontalAlignment(SwingConstants.CENTER);
        loginTextField.setLocation(loginBackground.getX() + 120,loginBackground.getY() + 20);
        loginTextField.setSize(200,30);
        contentPane.add(loginTextField,0);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setLocation(loginBackground.getX() + 20,loginBackground.getY() + 60);
        passwordLabel.setSize(60,30);
        contentPane.add(passwordLabel,0);

        passwordTextField = new JTextField();
        passwordTextField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordTextField.setLocation(loginBackground.getX() + 120,loginBackground.getY() + 60);
        passwordTextField.setSize(200,30);
        contentPane.add(passwordTextField,0);

        signInBtn = new JButton();
        signInBtn.setBounds(20, 100, 300, 30);
        signInBtn.setText("회원가입");
        contentPane.add(signInBtn);

        loginBtn = new JButton();
        loginBtn.setBounds(20, 140, 300, 30);
        loginBtn.setText("로그인");
        contentPane.add(loginBtn);

        setVisible(true);

    }
    public void setUserSocket(UserSocket userSocket) {
        signInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userSocket.SignIn(loginTextField.getText(),passwordTextField.getText());
            }
        });
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userSocket.Login(loginTextField.getText(),passwordTextField.getText());
            }
        });
    }
}
