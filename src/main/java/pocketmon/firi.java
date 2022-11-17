package pocketmon;

import javax.swing.*;
import java.awt.*;

public class firi extends PocketMonster{
    public firi()
    {
        Image img1 = new ImageIcon(this.getClass().getResource("/파이리.png")).getImage();
        Image img2 = new ImageIcon(this.getClass().getResource("/리자드.png")).getImage();
        Image img3 = new ImageIcon(this.getClass().getResource("/리자몽.png")).getImage();
        Image[] temp_img = new Image[3];
        temp_img[0]=img1;
        temp_img[1]=img2;
        temp_img[2]=img3;
        this.setImage_list(temp_img);

        Skill s1 = new Skill("몸통박치기",30,"normal");
        Skill s2 = new Skill("화염방사",50,"fire");
        Skill s3 = new Skill("깨물기",40,"normal");
        Skill s4 = new Skill("불대문자",50,"fire");
        Skill[] temp_skill = new Skill[4];
        temp_skill[0]=s1;
        temp_skill[1]=s2;
        temp_skill[2]=s3;
        temp_skill[3]=s4;
        this.setSkill_list(temp_skill);

        this.setName("파이리");
        this.setCurrent_HP(100);
        this.setMax_HP(100);
        this.setType("fire");
        this.setAtk(10);
        this.setDef(10);
        this.setCondition("normal");
        this.setE_LV(0);
        this.setM_LV(2);
        this.setSpeed(10);
        this.setKill_score(0);

    }

    @Override
    public String evolution() {
        return null;
    }
}
