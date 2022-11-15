package org.network.gameframes;

import org.network.UserData;
import org.network.UserSocket;
import org.network.WindowConfig;
import org.network.gamecore.*;
import org.network.packet.UserChatPacket;
import org.network.packet.UserListPacket;

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
import java.util.List;
import java.util.Random;
import java.io.*;
import java.net.Socket;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static javax.swing.SwingConstants.BOTTOM;
import static javax.swing.SwingConstants.SOUTH;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameFrame extends JFrame implements ListSelectionListener {
    private JLayeredPane gameLayer = new JLayeredPane();//게임 패널
    //private JSplitPane gameFrameMainPanel = new JSplitPane();
    private JSplitPane gameServerInfoPanel = new JSplitPane();
    private JList<String> userList;
    private JPanel userListPanel = new JPanel();//유저 리스트 패널
    private JPanel userChatPanel = new JPanel();//유저 채팅 패널
    private GameCanvas gameCanvas;//게임 캔버스
    private GameThread gameThread;
    private JTextField txtInput;
    private static JTextPane textArea;
    private JButton btnSend;
    private JLabel lblUserName;
    private static DefaultListModel model;

    private String currentSelectedUser = "-ALL-";
    public GameFrame(){
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
        //textArea.replaceSelection("\n");

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
        //textArea.replaceSelection("\n");

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
                GameThread.setIsFocusable(true);//게임 포커스 활성화
            }
        }
    }
}
