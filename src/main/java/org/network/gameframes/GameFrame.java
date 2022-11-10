package org.network.gameframes;

import org.network.Main;
import org.network.WindowConfig;
import org.network.gamecore.GameCanvas;
import org.network.gamecore.GameInputKeyListener;
import org.network.gamecore.GameThread;
import org.network.gamecore.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GameFrame extends JFrame{
    private JLayeredPane gameLayer = new JLayeredPane();//게임 패널
    private JSplitPane gameFrameMainPanel = new JSplitPane();
    private JSplitPane gameServerInfoPanel = new JSplitPane();

    private JPanel userListPanel = new JPanel();//유저 리스트 패널
    private JPanel userChatPanel = new JPanel();//유저 채팅 패널
    private GameCanvas gameCanvas;//게임 캔버스
    private GameThread gameThread;
    public GameFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);
        setResizable(false);
        //좌우 나누기 패널
        gameFrameMainPanel = new JSplitPane();
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
        userChatPanel.setBackground(Color.blue);

        gameLayer.setBackground(Color.CYAN);
        gameLayer.setSize((WindowConfig.WIDTH/3)*2,(WindowConfig.HEIGHT));
        gameFrameMainPanel.setLeftComponent(gameLayer);

        gameCanvas = new GameCanvas(this);
        gameCanvas.setBounds(0,0,gameLayer.getWidth(),gameLayer.getHeight());
        gameLayer.add(gameCanvas,JLayeredPane.FRAME_CONTENT_LAYER);
        gameCanvas.repaint();
        setVisible(true);
        //게임 실행 부
        gameLayer.addKeyListener(new GameInputKeyListener());
        gameThread = new GameThread(gameLayer,gameCanvas);
        gameThread.start();
    }
    //유저 채팅 패널 초기화
    private void initUserChatPanel() {
        //userChatPanel
    }
}
