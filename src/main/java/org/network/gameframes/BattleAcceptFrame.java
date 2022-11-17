package org.network.gameframes;

import org.network.WindowConfig;

import javax.swing.*;

public class BattleAcceptFrame extends JFrame {
    public BattleAcceptFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);
        setResizable(false);
        //setContentPane(gameLayer);
    }
}
