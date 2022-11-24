package org.network.gameframes;

import org.network.UserData;
import org.network.UserSocket;
import org.network.WindowConfig;
import org.network.gamecore.*;
import org.network.packet.UserChatPacket;
import org.network.packet.UserListPacket;
import org.network.panel.BackgroundPanel;
import pocketmon.PocketMonster;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

import java.awt.event.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameFrame extends JFrame implements ListSelectionListener {

    private static JLayeredPane okNoPanel;//유저 ok 패널
    private static JButton okButton;
    private static ActionListener lastOkActionListener;
    private static JLabel okPanelTitle;
    private static DefaultListModel model;
    private static JTextPane textArea;
    //전투 ui 정보
    private static JLabel myPocketMonName;
    private static JLabel opponentPocketMonName;
    private static JLabel myPocketMonHealth;
    private static JLabel opponentPocketMonHealth;
    private static JLabel myPocketMonHealthText;
    private static JLabel opponentPocketMonHealthText;
    private static BackgroundPanel myPocketMonImage;
    private static BackgroundPanel opponentPocketMonImage;

    private JPanel firstPocketMonSelectPanel;
    private JLayeredPane gameLayer = new JLayeredPane();//게임 패널
    //private JSplitPane gameFrameMainPanel = new JSplitPane();
    private JSplitPane gameServerInfoPanel = new JSplitPane();
    private JList<String> userList;
    private JPanel userListPanel = new JPanel();//유저 리스트 패널
    private JPanel userChatPanel = new JPanel();//유저 채팅 패널
    private JLayeredPane battleLogPanel = new JLayeredPane();
    private GameCanvas gameCanvas;//게임 캔버스
    private GameThread gameThread;
    private JTextField txtInput;

    private JButton btnSend;
    private JLabel lblUserName;


    private String currentSelectedUser = "-ALL-";
    public GameFrame(){
        new Music("music/lobby.wav",true).start();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);
        setResizable(false);
        gameLayer.setBackground(Color.CYAN);
        gameLayer.setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);
        setContentPane(gameLayer);

        //상하단 나누기 패널
        gameServerInfoPanel = new JSplitPane();
        gameServerInfoPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
        gameServerInfoPanel.setDividerLocation((WindowConfig.HEIGHT/4));
        gameServerInfoPanel.setTopComponent(userListPanel);
        gameServerInfoPanel.setBottomComponent(userChatPanel);
        gameServerInfoPanel.setSize(WindowConfig.WIDTH/3,WindowConfig.HEIGHT);
        gameServerInfoPanel.setLocation(gameLayer.getWidth()-gameServerInfoPanel.getWidth(),0);
        gameServerInfoPanel.setEnabled(false);
        gameLayer.add(gameServerInfoPanel,JLayeredPane.POPUP_LAYER);
        //gameFrameMainPanel.setRightComponent(gameServerInfoPanel);

        userListPanel.setBackground(Color.white);
        userListPanel();
        userChatPanel.setBackground(Color.white);
        initUserChatPanel();

        initOkNoPanel();

        initBattleLogPanel();
        initSelectFirstPocketMonPanel();

        //gameFrameMainPanel.setLeftComponent(gameLayer);

        gameCanvas = new GameCanvas(this);
        gameCanvas.setBounds(0,0,gameLayer.getWidth(),gameLayer.getHeight());
        gameLayer.add(gameCanvas,JLayeredPane.FRAME_CONTENT_LAYER);

        gameCanvas.repaint();
        setVisible(true);
        
        gameThread = new GameThread(this,gameCanvas);
        gameThread.start();
        //게임 실행 부
        addKeyListener(new GameInputKeyListener());
    }
    private void initBattleLogPanel(){
        battleLogPanel.setBounds(0,0,WindowConfig.WIDTH * 2 / 3,WindowConfig.HEIGHT);
        BackgroundPanel background = new BackgroundPanel("ui/BottomUiPanel.png");
        background.setBounds(0,WindowConfig.HEIGHT*2/3 - 30,WindowConfig.WIDTH * 2 / 3,WindowConfig.HEIGHT/3);
        battleLogPanel.add(background,JLayeredPane.PALETTE_LAYER);
        int y=(WindowConfig.HEIGHT*2/3-30)+(WindowConfig.HEIGHT*1/6);

        //배틀설명 창 생성
        JLabel battletext= new JLabel();
        battletext.setText("배틀상황 설명");
        battletext.setForeground(Color.WHITE);
        battletext.setFont(new Font("굴림",Font.BOLD,18));
        battletext.setBounds(60,400,background.getWidth()/2,WindowConfig.HEIGHT/3);
        battleLogPanel.add(battletext,JLayeredPane.POPUP_LAYER);

        //버튼 4개 배열로 생성
        ImageIcon image = new ImageIcon(getClass().getResource("/ui/button.png"));
        Image changeimg = image.getImage();
        Image im= changeimg.getScaledInstance(background.getWidth()/4,(WindowConfig.HEIGHT*1/6),Image.SCALE_SMOOTH);
        ImageIcon img= new ImageIcon(im);

        JButton[] jbt= new JButton[4];
        for(int i=0;i<4;i++){
            jbt[i]=new JButton(img);
            jbt[i].setFont(new Font("굴림",Font.ITALIC,20));
            jbt[i].setForeground(Color.WHITE);
        }

        jbt[0].setBounds(background.getWidth()/2,WindowConfig.HEIGHT*2/3-30,background.getWidth()/4,(WindowConfig.HEIGHT*1/6));
        jbt[1].setBounds(background.getWidth()/2+background.getWidth()/4,WindowConfig.HEIGHT*2/3-30,background.getWidth()/4,(WindowConfig.HEIGHT*1/6));
        jbt[2].setBounds(background.getWidth()/2,y,background.getWidth()/4,(WindowConfig.HEIGHT*1/6));
        jbt[3].setBounds(background.getWidth()/2+background.getWidth()/4,y,background.getWidth()/4,(WindowConfig.HEIGHT*1/6));
        jbt[0].setText("1");
        jbt[1].setText("2");
        jbt[2].setText("3");
        jbt[3].setText("4");


        for(int i=0;i<4;i++){
            jbt[i].setHorizontalTextPosition(JButton.CENTER);
            battleLogPanel.add(jbt[i],JLayeredPane.POPUP_LAYER);
        }

        //전투 ui 파트
        BackgroundPanel battleBackground = new BackgroundPanel("ui/battleUI.png");
        battleBackground.setBounds(0,0,WindowConfig.WIDTH * 2 / 3,WindowConfig.HEIGHT * 2 / 3 - 30);
        battleLogPanel.add(battleBackground,JLayeredPane.FRAME_CONTENT_LAYER);

        //battle health bar 파트
        
        //자신이 소유한 포켓몬의 ui
        BackgroundPanel myHealthBackground = new BackgroundPanel("ui/HPBar.png");
        myHealthBackground.setBounds(580,400,250,27);
        battleLogPanel.add(myHealthBackground,JLayeredPane.PALETTE_LAYER);

        myPocketMonHealth = new JLabel();
        myPocketMonHealth.setBounds(myHealthBackground.getX() + 58,myHealthBackground.getY() + 8,185,12);
        myPocketMonHealth.setBackground(Color.GREEN);
        myPocketMonHealth.setOpaque(true);
        battleLogPanel.add(myPocketMonHealth,JLayeredPane.POPUP_LAYER);

        //포켓몬 이름
        BackgroundPanel myNameBackground = new BackgroundPanel("ui/partyInactive.png");
        myNameBackground.setBounds(610,355,220,40);
        battleLogPanel.add(myNameBackground,JLayeredPane.PALETTE_LAYER);

        myPocketMonName = new JLabel("피카츄");
        myPocketMonName.setBounds(myNameBackground.getX() + 10,myNameBackground.getY() + 10, 100 ,20);
        myPocketMonName.setForeground(Color.WHITE);
        battleLogPanel.add(myPocketMonName,JLayeredPane.POPUP_LAYER);

        myPocketMonHealthText = new JLabel("100/100");
        myPocketMonHealthText.setBounds(myNameBackground.getX() + 120,myNameBackground.getY() + 10, 100 , 20);
        myPocketMonHealthText.setForeground(Color.WHITE);
        battleLogPanel.add(myPocketMonHealthText,JLayeredPane.POPUP_LAYER);

        myPocketMonImage = new BackgroundPanel("Pocketmon/firi-back.png");
        myPocketMonImage.setBounds(20,250,300,300);
        battleLogPanel.add(myPocketMonImage,JLayeredPane.DEFAULT_LAYER);

        //상대가 소유한 포켓몬 정보
        BackgroundPanel opponentHealthBackground = new BackgroundPanel("ui/HPBar.png");
        opponentHealthBackground.setBounds(10,150,250,27);
        battleLogPanel.add(opponentHealthBackground,JLayeredPane.PALETTE_LAYER);

        opponentPocketMonHealth = new JLabel();
        opponentPocketMonHealth.setBounds(opponentHealthBackground.getX() + 58,opponentHealthBackground.getY() + 7,185,13);
        opponentPocketMonHealth.setBackground(Color.GREEN);
        opponentPocketMonHealth.setOpaque(true);
        battleLogPanel.add(opponentPocketMonHealth,JLayeredPane.POPUP_LAYER);

        //포켓몬 이름
        BackgroundPanel opponentNameBackground = new BackgroundPanel("ui/partyInactive.png");
        opponentNameBackground.setBounds(40,105,220,40);
        battleLogPanel.add(opponentNameBackground,JLayeredPane.PALETTE_LAYER);

        opponentPocketMonName = new JLabel("피카츄");
        opponentPocketMonName.setBounds(opponentNameBackground.getX() + 10,opponentNameBackground.getY() + 10, 100,20);
        opponentPocketMonName.setForeground(Color.WHITE);
        battleLogPanel.add(opponentPocketMonName,JLayeredPane.POPUP_LAYER);

        opponentPocketMonHealthText = new JLabel("100/100");
        opponentPocketMonHealthText.setBounds(opponentPocketMonName.getX() + 120,opponentPocketMonName.getY(), 100 , 20);
        opponentPocketMonHealthText.setForeground(Color.WHITE);
        battleLogPanel.add(opponentPocketMonHealthText,JLayeredPane.POPUP_LAYER);

        opponentPocketMonImage = new BackgroundPanel("Pocketmon/firi-front.png");
        opponentPocketMonImage.setBounds(450,50,300,300);
        battleLogPanel.add(opponentPocketMonImage,JLayeredPane.DEFAULT_LAYER);
        
        //gameLayer.add(battleLogPanel,JLayeredPane.DEFAULT_LAYER);
    }
    private void initSelectFirstPocketMonPanel(){
        firstPocketMonSelectPanel = new JPanel();
        firstPocketMonSelectPanel.setBackground(Color.gray);
        firstPocketMonSelectPanel.setSize(WindowConfig.WIDTH/2,450);
        firstPocketMonSelectPanel.setLocation(100,100);
        //여기에다 추가
        gameLayer.add(firstPocketMonSelectPanel,JLayeredPane.DEFAULT_LAYER);
    }
    private void initOkNoPanel(){
        okNoPanel = new JLayeredPane();
        okNoPanel.setSize(WindowConfig.WIDTH/3,WindowConfig.HEIGHT/3);
        okNoPanel.setLocation(WindowConfig.WIDTH/3 - okNoPanel.getWidth()/2,WindowConfig.HEIGHT/3 - okNoPanel.getHeight()/2);

        BackgroundPanel backgroundPanel = new BackgroundPanel("ui/speechbox.png");
        backgroundPanel.setLocation(0,0);
        backgroundPanel.setSize(okNoPanel.getSize());
        okNoPanel.add(backgroundPanel,JLayeredPane.FRAME_CONTENT_LAYER);

        okPanelTitle = new JLabel(
                "<html>테스트 디버깅 용 텍스트 입니다." +
                "<br>테스트 디버깅 용 텍스트 입니다.</html>"
        );
        okPanelTitle.setHorizontalAlignment(JLabel.CENTER);
        okPanelTitle.setBounds(30,0,okNoPanel.getWidth() - 60,200);
        okPanelTitle.setFont(new Font("Default", Font.BOLD, 14));

        okButton = new JButton("확인");
        JButton noButton = new JButton("닫기");
        noButton.addActionListener(e -> {
            okNoPanel.setVisible(false);
            okButton.removeActionListener(lastOkActionListener);
        });

        okButton.addActionListener(e -> {
            okNoPanel.setVisible(false);

        });

        okButton.setBounds(120,150,100,30);
        noButton.setBounds(230,150,100,30);

        okNoPanel.add(okPanelTitle,JLayeredPane.POPUP_LAYER);
        okNoPanel.add(okButton,JLayeredPane.POPUP_LAYER);
        okNoPanel.add(noButton,JLayeredPane.POPUP_LAYER);
        gameLayer.add(okNoPanel,JLayeredPane.DRAG_LAYER);
        okNoPanel.setVisible(false);
    }

    public static void showOkNoPanel(String title,ActionListener okAction) {
        okPanelTitle.setText("<html>"+title+"</html>");
        lastOkActionListener = okAction;
        okButton.addActionListener(lastOkActionListener);
        okNoPanel.setVisible(true);
    }

    private void userListPanel() {
        model=new DefaultListModel();
        userList = new JList<String>((ListModel<String>) model);
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setBounds(1, 1, 401, 180);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//하나만 선택되게

        //선택되면 귓속말 창
        userList.addListSelectionListener(this);

        userListPanel.add(scrollPane);
        userListPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        userListPanel.setLayout(null);
    }
    public static void updateUserList(UserListPacket userListPacket){
        model.clear();
        for (String username : userListPacket.userlist){
            model.addElement(username);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {//리스트 선택시 실행되는 메소드
        if(!e.getValueIsAdjusting()) {
            currentSelectedUser = userList.getSelectedValue();
        }
    }


    // 화면에 출력
    public static void AppendText(String msg) {
        msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(left, Color.BLACK);
        doc.setParagraphAttributes(doc.getLength(), 1, left, false);
        try {
            doc.insertString(doc.getLength(), msg+"\n", left );
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);

    }
    public static void AppendTextR(String msg) {
        msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(right, Color.BLUE);
        doc.setParagraphAttributes(doc.getLength(), 1, right, false);
        try {
            doc.insertString(doc.getLength(),msg+"\n", right );
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);
    }
    //유저 체력 표시 변경
    public static void setPlayerHealth(boolean isOpponent,int health,int maxHealth)
    {
        int healthLength = 185;
        if (isOpponent){
            opponentPocketMonHealth.setSize(healthLength * health / maxHealth,13);
            opponentPocketMonHealthText.setText(health + "/" + maxHealth);
        }
        else {
            myPocketMonHealth.setSize(healthLength * health / maxHealth,12);
            myPocketMonHealthText.setText(health + "/" + maxHealth);
        }
    }
    public static void setPlayerImage(boolean isOpponent,String path)
    {
        int healthLength = 185;
        if (isOpponent){
            opponentPocketMonImage.setImage(path);
        }
        else {
            opponentPocketMonImage.setImage(path);
        }
    }
    //유저 채팅 패널 초기화
    private void initUserChatPanel() {
        //userChatPanel
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(11, 10, 380, 370);
        userChatPanel.add(scrollPane);
        textArea = new JTextPane();
        textArea.setEditable(true);
        textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
        scrollPane.setViewportView(textArea);

        userChatPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        userChatPanel.setLayout(null);

        txtInput = new JTextField();
        txtInput.setBounds(100,420,200,40);
        userChatPanel.add(txtInput);
        txtInput.setColumns(10);

        txtInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                GameThread.setIsFocusable(false);//게임 포커스 비활성화
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                GameThread.setIsFocusable(true);//게임 포커스 활성화
            }
        });

        btnSend = new JButton("전송");
        btnSend.setFont(new Font("굴림", Font.PLAIN, 14));
        btnSend.setBounds(320, 420, 70, 40);
        userChatPanel.add(btnSend);

        //사용자 이름 보여주기
        lblUserName = new JLabel(UserData.username);
        lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
        lblUserName.setBackground(Color.WHITE);
        lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
        lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
        lblUserName.setBounds(10, 420, 70, 40);
        userChatPanel.add(lblUserName);
        
        TextSendAction action = new TextSendAction();
        btnSend.addActionListener(action);
        txtInput.addActionListener(action);
        txtInput.requestFocus();
    }


    class TextSendAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Send button을 누르거나 메시지 입력하고 Enter key 치면
            if (e.getSource() == btnSend || e.getSource() == txtInput) {
                String msg = null;
                msg = String.format(" %s\n", txtInput.getText());
                AppendTextR(msg);
                UserChatPacket chatPacket = null;
                if(currentSelectedUser.equals(UserData.username) || currentSelectedUser.equals("-ALL-")){
                     chatPacket = new UserChatPacket(UserData.id, UserData.username, msg, "-ALL-");
                }else{
                     chatPacket = new UserChatPacket(UserData.id, UserData.username, msg, currentSelectedUser);
                }
                UserSocket.getInstance().sendObject(chatPacket);
                txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
            }
        }
    }
}
