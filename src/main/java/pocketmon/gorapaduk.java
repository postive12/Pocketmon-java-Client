package pocketmon;


import javax.swing.*;
import java.awt.*;

public class gorapaduk extends PocketMonster {
    public gorapaduk()
    {

        Image img1 = new ImageIcon(this.getClass().getResource("/gorapaduk.png")).getImage();
        Image img2 = new ImageIcon(this.getClass().getResource("/이상해풀.png")).getImage();
        Image img3 = new ImageIcon(this.getClass().getResource("/이상해꽃.png")).getImage();
        Image[] temp_img = new Image[3];
        temp_img[0]=img1;
        temp_img[1]=img2;
        temp_img[2]=img3;
        this.setImage_list(temp_img);

        Skill s1 = new Skill("마구할퀴기",30);
        Skill s2 = new Skill("물의파동",40);
        Skill s3 = new Skill("염동력",20);
        Skill s4 = new Skill("사슬묶기",40);

        Skill[] temp_skill = new Skill[4];
        temp_skill[0]=s1;
        temp_skill[1]=s2;
        temp_skill[2]=s3;
        temp_skill[3]=s4;
        this.setSkill_list(temp_skill);

        this.setName("고라파턱");
        this.setCurrent_HP(100);
        this.setMax_HP(100);
        //this.setType("grass");
        this.setAtk(10);
        this.setDef(10);
        this.setCondition("normal");
        this.setE_LV(0);
        this.setM_LV(2);
        this.setSpeed(10);
        this.setKill_score(0);

    }

}
