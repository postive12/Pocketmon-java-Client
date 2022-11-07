package org.network.gameframes;

import org.network.Main;
import org.network.WindowConfig;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame implements Runnable{
    private JLayeredPane gameLayer = new JLayeredPane();
    private GameCanvas gameCanvas;
    public GameFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);
        setResizable(false);
        gameLayer.setLayout(null);
        gameLayer.setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);
        setContentPane(gameLayer);

        gameCanvas = new GameCanvas();
        gameCanvas.setBounds(0,0,WindowConfig.WIDTH,WindowConfig.HEIGHT);
        gameLayer.add(gameCanvas,JLayeredPane.FRAME_CONTENT_LAYER);
        gameCanvas.repaint();

        JButton testButton = new JButton("캔버스 위 버튼 생성 테스트");
        testButton.setBounds(300,300,300,300);
        gameLayer.add(testButton);
        setVisible(true);
        Thread mainWork = new Thread(this);
        mainWork.run();

    }

    @Override
    public void run() {
        while (true){
            gameCanvas.repaint();
            System.out.println("Painting");
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
