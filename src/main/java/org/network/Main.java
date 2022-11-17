package org.network;

import org.network.gameframes.Music;
import org.network.gameframes.LoginFrame;


public class Main {
    public static UserSocket userSocket;
    public static void main(String[] args)
    {
        userSocket = new UserSocket();
        new Music().play();
        new LoginFrame();
    }
}