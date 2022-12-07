package pocketmon;

public class pikachu extends PocketMonster {
    public pikachu()
    {
        setBackPath("pocketmon/pikachu-back.png");
        setFrontPath("pocketmon/pikachu-front.png");

        Skill s1 = new Skill("몸통박치기",30);
        Skill s2 = new Skill("백만볼트",50);
        Skill s3 = new Skill("전광석화",40);
        Skill s4 = new Skill("전기자석파",40);
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
