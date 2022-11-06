package org.network.panel;

import org.network.Main;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Image;
public class BackgroundPanel extends JLabel {
    private String path;
    private Image image;
    public BackgroundPanel(String path){
        this.path = path;
        setVisible(true);
        try{
            image = new ImageIcon(Main.class.getClassLoader().getResource(path)).getImage();
        }
        catch (Exception e){
            System.out.println("Failed to import image.");
            e.printStackTrace();
        }
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image,0,0,getWidth(),getHeight(),null);
    }
}

