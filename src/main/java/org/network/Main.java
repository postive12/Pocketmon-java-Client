package org.network;

import org.network.packet.startFrame;


public class Main {
    public static void main(String[] args)
    {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setUserSocket(new UserSocket());
    }
}