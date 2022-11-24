package pocketmon;
import javax.swing.ImageIcon;
import java.awt.Image;
public class pikachu extends PocketMonster {
    public pikachu(){

        Image img1 = new ImageIcon(this.getClass().getResource("/피카츄.png")).getImage();
        Image img2 = new ImageIcon(this.getClass().getResource("/라이츄.png")).getImage();
        Image[] temp_img = new Image[2];
        temp_img[0]=img1;
        temp_img[1]=img2;

        Skill s1 = new Skill("몸통박치기",30);
        Skill s2 = new Skill("백만볼트",50);
        Skill s3 = new Skill("전광석화",40);
        Skill s4 = new Skill("전기자석파",0);
        Skill[] temp_skill = new Skill[4];
        temp_skill[0]=s1;
        temp_skill[1]=s2;
        temp_skill[2]=s3;
        temp_skill[3]=s4;
        this.setSkill_list(temp_skill);

        this.setName("피카츄");
        this.setCurrent_HP(100);
        this.setMax_HP(100);
        //this.setType("electric");
        this.setAtk(10);
        this.setCondition("normal");

    }



}
