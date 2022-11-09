package org.network.gameframes;

import org.network.Main;
import org.network.WindowConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static javax.swing.SwingConstants.SOUTH;

public class GameFrame extends JFrame implements Runnable{
    private JLayeredPane gameLayer = new JLayeredPane();//게임 패널
    private JSplitPane gameFrameMainPanel = new JSplitPane();
    private JSplitPane gameServerInfoPanel = new JSplitPane();

    private JPanel userListPanel = new JPanel();//유저 리스트 패널
    private JPanel userChatPanel = new JPanel();//유저 채팅 패널
    private GameCanvas gameCanvas;//게임 캔버스
    public GameFrame(){
        
        //좌우 나누기 패널
        gameFrameMainPanel = new JSplitPane();
        getContentPane().add(gameFrameMainPanel,BorderLayout.CENTER);
        gameFrameMainPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        gameFrameMainPanel.setDividerLocation((WindowConfig.WIDTH/3)*2);
        gameFrameMainPanel.setEnabled(false);
        setContentPane(gameFrameMainPanel);

        //상하단 나누기 패널
        gameServerInfoPanel = new JSplitPane();
        gameServerInfoPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
        gameServerInfoPanel.setDividerLocation((WindowConfig.HEIGHT/4));
        gameServerInfoPanel.setTopComponent(userListPanel);
        gameServerInfoPanel.setBottomComponent(userChatPanel);
        gameServerInfoPanel.setEnabled(false);
        gameFrameMainPanel.setRightComponent(gameServerInfoPanel);

        userListPanel.setBackground(Color.black);

        userChatPanel.setBackground(Color.white);
        initUserChatPanel();
        

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);
        setResizable(false);
        gameLayer.setLayout(null);
        gameFrameMainPanel.setLeftComponent(gameLayer);

        gameCanvas = new GameCanvas();
        gameCanvas.setBounds(0,0,gameLayer.getWidth(),gameLayer.getHeight());
        gameLayer.add(gameCanvas,JLayeredPane.FRAME_CONTENT_LAYER);
        gameCanvas.repaint();

        JButton testButton = new JButton("캔버스 위 버튼 생성 테스트");
        testButton.setBounds(300,300,300,300);
        gameLayer.add(testButton);
        setVisible(true);
        Thread mainWork = new Thread(this);
        mainWork.run();

    }
    private JTextField txtInput;
    private JTextPane textArea;
    private JButton btnSend;
    private JLabel lblUserName;
    private JButton imgBtn;
    //유저 채팅 패널 초기화
    private void initUserChatPanel() {
        //userChatPanel
        userChatPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        //userChatPanel.add(new JButton("보내기"),"west");
        //userChatPanel.add(new JButton("채팅창"),"center");
        JScrollPane scrollPane = new JScrollPane();
        //scrollPane.setBounds(12, 10, 352, 471);
        userChatPanel.add(scrollPane);

        textArea = new JTextPane();
        textArea.setEditable(true);
        textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
        scrollPane.setViewportView(textArea);

        txtInput = new JTextField();
        //txtInput.setBounds(74, 489, 209, 40);
        userChatPanel.add(txtInput);
        txtInput.setColumns(10);

        btnSend = new JButton("Send");
        btnSend.setFont(new Font("굴림", Font.PLAIN, 14));
        //btnSend.setBounds(295, 489, 69, 40);
        userChatPanel.add(btnSend);

        lblUserName = new JLabel("Name");
        lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
        lblUserName.setBackground(Color.WHITE);
        lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
        lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
        //lblUserName.setBounds(12, 539, 62, 40);
        userChatPanel.add(lblUserName);
        setVisible(true);

        imgBtn = new JButton("+");
        imgBtn.setFont(new Font("굴림", Font.PLAIN, 16));
        //imgBtn.setBounds(12, 489, 50, 40);
        userChatPanel.add(imgBtn);
    }
    @Override
    public void run() {
        while (true){
            gameCanvas.repaint();
            //System.out.println("Painting");
        }
    }

    public class GameCanvas extends Canvas{
        private int cnt, gamecnt;
        private Image background[] = new Image[8];
        private Image characters[] = new Image[8];
        private Image img = new ImageIcon(Main.class.getClassLoader().getResource("backgrounds/pokenet_normal.png")).getImage();
        private Image dblbuff;//더블버퍼링용 백버퍼
        private Graphics gc;//더블버퍼링용 그래픽 컨텍스트

        private Font font;
        public GameCanvas(){
            font=new Font("Default",Font.PLAIN,9);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(img,0,0,WindowConfig.WIDTH,WindowConfig.HEIGHT,this);
        }
        public void update(Graphics g){
            if(gc==null) return;
            dblpaint();//오프스크린 버퍼에 그리기
            g.drawImage(dblbuff,0,0,this);//오프스크린 버퍼를 메인화면에 그린다.
        }
        public void dblpaint(){
            gc.drawImage(img,0,0,WindowConfig.WIDTH,WindowConfig.HEIGHT,this);
        }
    }
}
