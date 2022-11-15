package org.network.gamecompnent;

import org.network.Main;
import org.network.WindowConfig;
import org.network.gamecore.GameObject;
import org.network.gamecore.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Character extends GameObject {
    private Point direction = new Point(0,0);
    private int speed = 4;
    public Character(String identificationId){
        setImage(new ImageIcon(Main.class.getClassLoader().getResource("characters/players/1.png")).getImage());
        setTransform(new Point(WindowConfig.WIDTH/3,WindowConfig.HEIGHT/2));
        setImageTick(4);
        setImageCount(new Point(4,4));
        setSize(new Point(64,80));
        setIdentificationId(identificationId);
        setImgSwapRatio(2);
        setDefaultImagePos(2);
    }
    @Override
    public void update() {
//        if (Input.GetKeyPressed(KeyEvent.VK_A)){
//            direction.x -= 1;
//            setCurrentImgLine(3);
//        }
//        if (Input.GetKeyPressed(KeyEvent.VK_D)){
//            setCurrentImgLine(1);
//            direction.x += 1;
//        }
//        if (Input.GetKeyPressed(KeyEvent.VK_W) && direction.x == 0){
//            setCurrentImgLine(0);
//            direction.y -= 1;
//        }
//        if (Input.GetKeyPressed(KeyEvent.VK_S)&& direction.x == 0){
//            setCurrentImgLine(2);
//            direction.y += 1;
//        }
//        if (direction.x == 0 && direction.y == 0){
//            setAnimationStop(true);
//            return;
//        }
//        setAnimationStop(false);
//        setTransform(new Point(getTransform().x + direction.x * speed,getTransform().y + direction.y * speed));
//        direction.y = 0;
//        direction.x = 0;
    }
}
