package org.network.gamecore;

import org.network.Main;
import org.network.WindowConfig;
import org.network.gamecompnent.Character;
import org.network.gameframes.GameFrame;

import javax.swing.*;
import java.awt.*;

public class GameCanvas extends Canvas {
    private GameFrame parent;
    private Image background[] = new Image[8];
    private Image characters[] = new Image[8];
    private Image testChar = new ImageIcon(Main.class.getClassLoader().getResource("characters/players/1.png")).getImage();
    private Image img = new ImageIcon(Main.class.getClassLoader().getResource("backgrounds/pokenet_normal.png")).getImage();
    //private GameObject gameObject = new GameObject();
    private Image dblbuff;//더블버퍼링용 백버퍼
    private Graphics gc;//더블버퍼링용 그래픽 컨텍스트

    private Font font;
    public GameCanvas(GameFrame parent){
        this.parent = parent;
        GameObject gameObject = new Character();
        GameObject.Initiate(gameObject);
        font=new Font("Default",Font.PLAIN,9);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(gc==null) {
            dblbuff=createImage(getWidth(),getHeight());//더블 버퍼링용 오프스크린 버퍼 생성. 필히 paint 함수 내에서 해 줘야 한다. 그렇지 않으면 null이 반환된다.
            if(dblbuff==null) System.out.println("오프스크린 버퍼 생성 실패");
            else gc=dblbuff.getGraphics();//오프스크린 버퍼에 그리기 위한 그래픽 컨텍스트 획득
            return;
        }
        update(g);
    }
    public void update(Graphics g){
        if(gc==null) return;
        dblpaint();//오프스크린 버퍼에 그리기
        g.drawImage(dblbuff,0,0,this);//오프스크린 버퍼를 메인화면에 그린다.
    }
    public void dblpaint(){
        gc.drawImage(img,0,0, WindowConfig.WIDTH,WindowConfig.HEIGHT,this);
        drawGameObjects();
    }
    private void drawGameObjects() {
        for (GameObject gameObject : GameObject.gameObjects){
            drawImageAnc(
                    gameObject.getImage(),
                    gameObject.getTransform().x,
                    gameObject.getTransform().y,
                    gameObject.getXSize(this),
                    gameObject.getYSize(this),
                    gameObject.calCurrentImgX(GameThread.getCnt()),
                    gameObject.getCurrentImgLine());
        }
    }

    public void drawImageAnc(Image img, int x, int y, int anc){
        //앵커값을 참조해 이미지 출력 위치를 보정한다.
        //예) anc==0 : 좌상단이 기준, anc==4 : 이미지 중앙이 기준
        int imgw, imgh;
        imgw=img.getWidth(this);
        imgh=img.getHeight(this);
        x=x-(anc%3)*(imgw/2);
        y=y-(anc/3)*(imgh/2);
        gc.drawImage(img, x,y, this);
    }

    public void drawImageAnc(Image img, int x, int y, int wd,int ht,int imgX, int imgY){//sx,sy부터 wd,ht만큼 클리핑해서 그린다.
        if(wd<0||ht<0) return;
        int sx = wd * imgX;
        int sy = ht * imgY;
        x=x-wd/2;
        y=y-ht/2;
        gc.setClip(x, y, wd, ht);
        gc.drawImage(img, x-sx, y-sy, this);
        gc.setClip(0,0, this.getWidth()+10,this.getHeight()+30);
    }
}
