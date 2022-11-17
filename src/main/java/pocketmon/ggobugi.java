package pocketmon;

import javax.swing.*;
import java.awt.*;

public class ggobugi extends PocketMonster {
    public ggobugi()
    {
        //진화 단계별 이미지
        Image img1 = new ImageIcon(this.getClass().getResource("/꼬부기.png")).getImage();
        Image img2 = new ImageIcon(this.getClass().getResource("/어니부기.png")).getImage();
        Image img3 = new ImageIcon(this.getClass().getResource("/거북왕.png")).getImage();

        //진화 단계별 이미지 배열인 temp_img[]
        Image[] temp_img = new Image[3];
        temp_img[0]=img1;
        temp_img[1]=img2;
        temp_img[2]=img3;
        this.setImage_list(temp_img);

//		Skill s1 = new Skill("몸통박치기",50);
//		Skill s2 = new Skill("물대포",60);
//		Skill s3 = new Skill("깨물기",60);
//		Skill s4 = new Skill("파도타기",70);


        Skill s1 = new Skill("몸통박치기",30);
        Skill s2 = new Skill("물대포",40);
        Skill s3 = new Skill("깨물기",40);
        Skill s4 = new Skill("파도타기",50);

        //해당 포켓몬 기본 스킬 배열 : temp_list[]
        Skill[] temp_skill = new Skill[4];
        temp_skill[0]=s1;
        temp_skill[1]=s2;
        temp_skill[2]=s3;
        temp_skill[3]=s4;
        this.setSkill_list(temp_skill);

        this.setName("꼬부기");
        this.setCurrent_HP(100);
        this.setMax_HP(100);
        //this.setType("water");
        this.setAtk(10);
        this.setDef(10);
        this.setCondition("normal");
        this.setE_LV(0);
        this.setM_LV(2);
        this.setSpeed(10);
        this.setKill_score(0);

    }


}
