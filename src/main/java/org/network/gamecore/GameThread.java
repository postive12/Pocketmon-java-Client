package org.network.gamecore;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class GameThread extends Thread{
    private final GameCanvas target;
    private final JLayeredPane parent;

    private static int cnt = 0;//루프 제어용 컨트롤 변수
    private final int delay = 17;//루프 딜레이. 1/1000초 단위.
    private long preTime;//루프 간격을 조절하기 위한 시간 체크값
    public GameThread(JLayeredPane parent, GameCanvas target){
        this.parent = parent;
        this.target = target;
    }

    @Override
    public void run() {
        while (true){
            preTime=System.currentTimeMillis();
            //parent.requestFocus();
            target.repaint();//화면 리페인트
            Update();//키 처리
            //프레임 처리
            if(System.currentTimeMillis()-preTime<delay) {
                try {
                    Thread.sleep(delay-System.currentTimeMillis()+preTime);
                } catch (InterruptedException e) {
                    //ignored
                }
            }
            cnt++;
        }
    }
    private void Update() {
        if (Input.GetKeyPressed(KeyEvent.VK_SPACE)){
            //System.out.println("Space pressed.");
            System.out.println(cnt);
        }
        GameObject.updateGameObjects();
    }
    public static int getCnt() {
        return cnt;
    }
}
