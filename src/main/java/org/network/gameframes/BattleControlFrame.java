package org.network.gameframes;

import org.network.UserData;
import org.network.UserSocket;
import org.network.WindowConfig;
import org.network.data.PocketMonData;
import org.network.gamecompnent.GameManager;
import org.network.packet.UserBattlePacket;
import org.network.panel.BackgroundPanel;
import pocketmon.Skill;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BattleControlFrame extends JLayeredPane {
    private JLabel battleText;
    private JButton[] jbt;
    private int myp;
    private JLabel myPocketMonName;
    private JLabel opponentPocketMonName;
    private JLabel myPocketMonHealth;
    private JLabel opponentPocketMonHealth;
    private JLabel myPocketMonHealthText;
    private JLabel opponentPocketMonHealthText;
    private BackgroundPanel myPocketMonImage;
    private BackgroundPanel opponentPocketMonImage;
    public BattleControlFrame(){
        setBounds(0,0, WindowConfig.WIDTH * 2 / 3,WindowConfig.HEIGHT);
        BackgroundPanel background = new BackgroundPanel("ui/BottomUiPanel.png");
        background.setBounds(0,WindowConfig.HEIGHT*2/3 - 30,WindowConfig.WIDTH * 2 / 3,WindowConfig.HEIGHT/3);
        add(background,JLayeredPane.PALETTE_LAYER);

        //전투 ui 텍스트 배경 파트
        BackgroundPanel battleBackground = new BackgroundPanel("ui/battleUI.png");
        battleBackground.setBounds(0,0,WindowConfig.WIDTH * 2 / 3,WindowConfig.HEIGHT * 2 / 3 - 30);
        add(battleBackground,JLayeredPane.FRAME_CONTENT_LAYER);
        //배틀설명 창 생성
        battleText = new JLabel();
        battleText.setText("배틀상황 설명");
        battleText.setForeground(Color.WHITE);
        battleText.setFont(new Font("굴림",Font.BOLD,18));
        battleText.setBounds(60,400,background.getWidth()/2,WindowConfig.HEIGHT/3);
        add(battleText,JLayeredPane.POPUP_LAYER);

        //컨트롤용 버튼 생성

        //버튼 4개 배열로 생성
        ImageIcon image = new ImageIcon(getClass().getResource("/ui/button.png"));
        Image changeimg = image.getImage();
        Image im= changeimg.getScaledInstance(background.getWidth()/4,(WindowConfig.HEIGHT*1/6),Image.SCALE_SMOOTH);
        ImageIcon img= new ImageIcon(im);

        int y=(WindowConfig.HEIGHT*2/3-30)+(WindowConfig.HEIGHT*1/6);

        jbt= new JButton[4];
        for(int i=0;i<4;i++){
            jbt[i]=new JButton(img);
            jbt[i].setFont(new Font("굴림",Font.BOLD,18));
            jbt[i].setForeground(Color.WHITE);
        }

        jbt[0].setBounds(background.getWidth()/2,WindowConfig.HEIGHT*2/3-30,background.getWidth()/4,(WindowConfig.HEIGHT*1/6));
        jbt[1].setBounds(background.getWidth()/2+background.getWidth()/4,WindowConfig.HEIGHT*2/3-30,background.getWidth()/4,(WindowConfig.HEIGHT*1/6));
        jbt[2].setBounds(background.getWidth()/2,y,background.getWidth()/4,(WindowConfig.HEIGHT*1/6));
        jbt[3].setBounds(background.getWidth()/2+background.getWidth()/4,y,background.getWidth()/4,(WindowConfig.HEIGHT*1/6));
        setBattleButtonDefaultState();
        //메뉴 선택 시 지정된 행동
        for(int i=0;i<4;i++){
            jbt[i].setHorizontalTextPosition(JButton.CENTER);
            add(jbt[i],JLayeredPane.POPUP_LAYER);
        }

        //각 포켓몬 정보 창

        //자신이 소유한 포켓몬의 ui
        BackgroundPanel myHealthBackground = new BackgroundPanel("ui/HPBar.png");
        myHealthBackground.setBounds(580,400,250,27);
        add(myHealthBackground,JLayeredPane.PALETTE_LAYER);

        myPocketMonHealth = new JLabel();
        myPocketMonHealth.setBounds(myHealthBackground.getX() + 58,myHealthBackground.getY() + 8,185,12);
        myPocketMonHealth.setBackground(Color.GREEN);
        myPocketMonHealth.setOpaque(true);
        add(myPocketMonHealth,JLayeredPane.POPUP_LAYER);

        //포켓몬 이름
        BackgroundPanel myNameBackground = new BackgroundPanel("ui/partyInactive.png");
        myNameBackground.setBounds(610,355,220,40);
        add(myNameBackground,JLayeredPane.PALETTE_LAYER);

        myPocketMonName = new JLabel("피카츄");
        myPocketMonName.setBounds(myNameBackground.getX() + 10,myNameBackground.getY() + 10, 100 ,20);
        myPocketMonName.setForeground(Color.WHITE);
        add(myPocketMonName,JLayeredPane.POPUP_LAYER);

        myPocketMonHealthText = new JLabel("100/100");
        myPocketMonHealthText.setBounds(myNameBackground.getX() + 120,myNameBackground.getY() + 10, 100 , 20);
        myPocketMonHealthText.setForeground(Color.WHITE);
        add(myPocketMonHealthText,JLayeredPane.POPUP_LAYER);

        myPocketMonImage = new BackgroundPanel("Pocketmon/firi-back.png");
        myPocketMonImage.setBounds(20,250,300,300);
        add(myPocketMonImage,JLayeredPane.DEFAULT_LAYER);

        //상대가 소유한 포켓몬 정보
        BackgroundPanel opponentHealthBackground = new BackgroundPanel("ui/HPBar.png");
        opponentHealthBackground.setBounds(10,150,250,27);
        add(opponentHealthBackground,JLayeredPane.PALETTE_LAYER);

        opponentPocketMonHealth = new JLabel();
        opponentPocketMonHealth.setBounds(opponentHealthBackground.getX() + 58,opponentHealthBackground.getY() + 7,185,13);
        opponentPocketMonHealth.setBackground(Color.GREEN);
        opponentPocketMonHealth.setOpaque(true);
        add(opponentPocketMonHealth,JLayeredPane.POPUP_LAYER);

        //포켓몬 이름
        BackgroundPanel opponentNameBackground = new BackgroundPanel("ui/partyInactive.png");
        opponentNameBackground.setBounds(40,105,220,40);
        add(opponentNameBackground,JLayeredPane.PALETTE_LAYER);

        opponentPocketMonName = new JLabel("피카츄");
        opponentPocketMonName.setBounds(opponentNameBackground.getX() + 10,opponentNameBackground.getY() + 10, 100,20);
        opponentPocketMonName.setForeground(Color.WHITE);
        add(opponentPocketMonName,JLayeredPane.POPUP_LAYER);

        opponentPocketMonHealthText = new JLabel("100/100");
        opponentPocketMonHealthText.setBounds(opponentPocketMonName.getX() + 120,opponentPocketMonName.getY(), 100 , 20);
        opponentPocketMonHealthText.setForeground(Color.WHITE);
        add(opponentPocketMonHealthText,JLayeredPane.POPUP_LAYER);

        opponentPocketMonImage = new BackgroundPanel("Pocketmon/firi-front.png");
        opponentPocketMonImage.setBounds(450,50,300,300);
        add(opponentPocketMonImage,JLayeredPane.DEFAULT_LAYER);

        setVisible(false);
    }
    private void setBattleButtonDefaultState(){
        for (JButton bt : jbt){
            for(ActionListener al : bt.getActionListeners() ) {
                bt.removeActionListener(al);
            }
        }
        jbt[0].setText("공격");
        jbt[1].setText("스킬");
        jbt[2].setText("아이템");
        jbt[3].setText("교체");
        jbt[0].addActionListener(e -> {
            battleText.setText("기본공격을 하였습니다.");
            java.util.List<Integer> args = new ArrayList<>();
            args.add(-1);
            UserBattlePacket userBattlePacket= new UserBattlePacket(UserData.id,UserData.username,"ATTACK","OPPONENT",args);
            UserSocket.getInstance().sendObject(userBattlePacket);
        });
        jbt[1].addActionListener(e -> {
            setBattleButtonSkillState();
        });
        jbt[2].addActionListener(e -> {
            setBattleButtonItemState();
        });
        jbt[3].addActionListener(e -> {
            setBattleButtonChangeState();
        });
    }
    private void setBattleButtonSkillState(){
        for (JButton bt : jbt){
            for(ActionListener al : bt.getActionListeners() ) {
                bt.removeActionListener(al);
            }
        }
        Skill[] skills = PocketMonData.monsterInfo.get(UserData.pocketMonList.get(myp)).getSkill_list();
        for (int i =0;i<4;i++){
            jbt[i].setText(skills[i].getName() + "/" + skills[i].getPower());
            int finalI = i;
            jbt[i].addActionListener(e ->{
                //스킬 패킷 전송
                java.util.List<Integer> args = new ArrayList<>();
                args.add(finalI);
                UserBattlePacket userBattlePacket= new UserBattlePacket(UserData.id,UserData.username,"ATTACK","OPPONENT",args);
                UserSocket.getInstance().sendObject(userBattlePacket);
                setBattleButtonDefaultState();
            });
        }
    }
    private void setBattleButtonItemState(){
        for (JButton bt : jbt){
            for(ActionListener al : bt.getActionListeners() ) {
                bt.removeActionListener(al);
            }
        }
        jbt[0].setText("라즈베리열매/30회복");
        jbt[1].setText("베리베리열매/20회복");
        jbt[2].setText("");
        jbt[3].setText("");

        jbt[0].addActionListener(e -> {
            java.util.List<Integer> args = new ArrayList<>();
            args.add(0);
            UserBattlePacket userBattlePacket= new UserBattlePacket(UserData.id,UserData.username,"ITEM","ME",args);
            UserSocket.getInstance().sendObject(userBattlePacket);
            setBattleButtonDefaultState();
        });
        jbt[1].addActionListener(e -> {
            java.util.List<Integer> args = new ArrayList<>();
            args.add(1);
            UserBattlePacket userBattlePacket= new UserBattlePacket(UserData.id,UserData.username,"ITEM","ME",args);
            UserSocket.getInstance().sendObject(userBattlePacket);
            setBattleButtonDefaultState();
        });
    }
    public void setBattleButtonChangeState(){
        for (JButton bt : jbt){
            for(ActionListener al : bt.getActionListeners() ) {
                bt.removeActionListener(al);
            }
        }
        List<Integer> remain = GameManager.getInstance().remain;

        int count = 0;
        for (int i = 0; i<3;i++){
            if (i==myp){
                continue;
            }
            System.out.println(remain.toString());
            if (!remain.contains(i)){
                String a1=PocketMonData.monsterInfo.get(UserData.pocketMonList.get(i)).getName();
                String a2="is die";
                String a3=a1.concat(a2);
                System.out.println("a3 = "+ a1);
                jbt[count].setText(a3);
            }else{
                jbt[count].setText(PocketMonData.monsterInfo.get(UserData.pocketMonList.get(i)).getName());
            }
            int current = i;
            jbt[count].addActionListener(e ->{
                myp=current;
                List<Integer> args = new ArrayList<>();
                args.add(current);
                myPocketMonName.setText(PocketMonData.monsterInfo.get(UserData.pocketMonList.get(current)).getName());
                myPocketMonImage.setImage(PocketMonData.monsterInfo.get(UserData.pocketMonList.get(current)).getBackPath());
                UserBattlePacket userBattlePacket= new UserBattlePacket(UserData.id,UserData.username,"CHANGE",UserData.username,args);
                UserSocket.getInstance().sendObject(userBattlePacket);
                setBattleButtonDefaultState();
            });
            count++;
        }
        jbt[2].setText("");
        jbt[3].setText("");
    }
    //유저 체력 표시 변경
    public void setPlayerHealth(boolean isOpponent,int health,int maxHealth)
    {
        System.out.println(health+"/"+maxHealth);
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
    public void setPlayerImage(boolean isOpponent,String path)
    {
        //System.out.println(path);
        if (isOpponent){
            opponentPocketMonImage.setImage(path);
        }
        else {
            myPocketMonImage.setImage(path);
        }
    }

    public void setPlayerName(boolean isOpponent, String name) {
        if (isOpponent){
            opponentPocketMonName.setText(name);
        }
        else {
            myPocketMonName.setText(name);
        }
    }

    public void setCurrentPocketMon(int i) {
        myp = i;
    }
    public void setBattleButtonEnable(boolean isEnable){
        for (int i = 0;i < 4;i++){
            jbt[i].setEnabled(isEnable);
        }
    }
}
