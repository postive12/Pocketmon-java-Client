package org.network.gameframes;

import org.network.Main;
import org.network.UserSocket;
import org.network.WindowConfig;
import org.network.packet.LoginPacket;
import org.network.panel.BackgroundPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    public static LoginFrame current;
    private static final long serialVersionUID = 1L;
    private JLayeredPane contentPane;
    private JTextField loginTextField;
    private JTextField passwordTextField;
    private JButton signInBtn;
    private JButton loginBtn;

    private static JLabel loginLog;
    public LoginFrame(){
        current = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);
        setResizable(false);
        contentPane = new JLayeredPane();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        int padding = 50;
        BackgroundPanel loginBackground = new BackgroundPanel("ui/speechbox.png");
        loginBackground.setSize(340,230);
        loginBackground.setLocation(WindowConfig.WIDTH - loginBackground.getWidth() - padding ,WindowConfig.HEIGHT - loginBackground.getHeight() - padding);
        contentPane.add(loginBackground,JLayeredPane.FRAME_CONTENT_LAYER);

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setLocation(loginBackground.getX() + 20,loginBackground.getY() + 20);
        loginLabel.setSize(60,30);
        contentPane.add(loginLabel, JLayeredPane.DEFAULT_LAYER);

        loginTextField = new JTextField();
        loginTextField.setHorizontalAlignment(SwingConstants.CENTER);
        loginTextField.setLocation(loginBackground.getX() + 120,loginBackground.getY() + 20);
        loginTextField.setSize(200,30);
        contentPane.add(loginTextField,JLayeredPane.DEFAULT_LAYER);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setLocation(loginBackground.getX() + 20,loginBackground.getY() + 60);
        passwordLabel.setSize(60,30);
        contentPane.add(passwordLabel,JLayeredPane.DEFAULT_LAYER);

        passwordTextField = new JTextField();
        passwordTextField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordTextField.setLocation(loginBackground.getX() + 120,loginBackground.getY() + 60);
        passwordTextField.setSize(200,30);
        contentPane.add(passwordTextField,JLayeredPane.DEFAULT_LAYER);

        loginBtn = new JButton();
        loginBtn.setBounds(20, 140, 300, 30);
        loginBtn.setLocation(loginBackground.getX() + 20,loginBackground.getY() + 100);
        loginBtn.setSize(300,30);
        loginBtn.setText("로그인");
        contentPane.add(loginBtn,JLayeredPane.DEFAULT_LAYER);

        signInBtn = new JButton();
        signInBtn.setLocation(loginBackground.getX() + 20,loginBackground.getY() + 140);
        signInBtn.setSize(300,30);
        signInBtn.setText("회원가입");
        contentPane.add(signInBtn,JLayeredPane.DEFAULT_LAYER);



        loginLog = new JLabel("로그인 정보가 표시됩니다.");//로그인 정보가 표시되는 라벨 생성
        loginLog.setLocation(loginBackground.getX() + 20,loginBackground.getY() + 180);
        loginLog.setSize(300,30);
        contentPane.add(loginLog,JLayeredPane.DEFAULT_LAYER);
        
        //뒷배경 배치
        BackgroundPanel backgroundPanel = new BackgroundPanel("backgrounds/pokenet_normal.png");//백그라운드 폴더의 배경 생성
        backgroundPanel.setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);//크기는 창크기와 동일하게 설정
        backgroundPanel.setLocation(0,0);//위치는 좌측 상단에 고정
        contentPane.add(backgroundPanel,JLayeredPane.FRAME_CONTENT_LAYER);//프레임 콘텐트 레이어로 배치
        //회원가입 이벤트
        signInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.userSocket.SignIn(loginTextField.getText(),passwordTextField.getText());
            }
        });
        //로그인 이벤트
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.userSocket.Login(loginTextField.getText(),passwordTextField.getText());
            }
        });

        setVisible(true);
    }
    public void setLoginLog(LoginPacket loginPacket){
        try {
            switch (loginPacket.loginPacketType) {
                case LOGIN_REFUSE -> loginLog.setText( "로그인 실패");
                case SIGN_IN_ACCEPT -> loginLog.setText("회원가입 성공");
                case SIGN_IN_REFUSE -> loginLog.setText("회원가입 실패");
            }
        }
        catch (Exception e){
            System.out.println("Login session end but login packet received.");
            return;
        }
    }

    public void close() {
        dispose();
    }
}
