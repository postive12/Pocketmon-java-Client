package org.network;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField loginTextField;
    private JTextField passwordTextField;
    private JButton signInBtn;
    private JButton loginBtn;
    public LoginFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 338, 440);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setBounds(20, 20, 60, 30);
        contentPane.add(lblNewLabel);

        loginTextField = new JTextField();
        loginTextField.setHorizontalAlignment(SwingConstants.CENTER);
        loginTextField.setBounds(120, 20, 200, 30);
        contentPane.add(loginTextField);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(20, 60, 60, 30);
        contentPane.add(lblPassword);

        passwordTextField = new JTextField();
        passwordTextField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordTextField.setBounds(120, 60, 200, 30);
        contentPane.add(passwordTextField);

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
