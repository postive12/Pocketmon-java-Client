package org.network.gamecore;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameInputKeyListener implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {
        //ignored
    }
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("Pressed");
        Input.SetPressed(e.getKeyCode());
    }
    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("Unpressed");
        Input.UnsetPressed(e.getKeyCode());
    }
}
