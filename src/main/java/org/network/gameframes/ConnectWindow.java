package org.network.gameframes;

import org.network.Main;
import org.network.UserSocket;

import javax.swing.*;

public class ConnectWindow extends JFrame {
    public ConnectWindow(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,100,340,130);
        setResizable(false);
        setLayout(null);


        JLabel ipText = new JLabel("IP");
        ipText.setBounds(10,10,30,30);
        add(ipText);
        JTextField serverIp = new JTextField("127.0.0.1");
        serverIp.setBounds(50,10,100,30);
        add(serverIp);

        JLabel portText = new JLabel("PORT");
        portText.setBounds(160,10,50,30);
        add(portText);

        JTextField port = new JTextField("30000");
        port.setBounds(200,10,100,30);
        add(port);

        JButton connectButton = new JButton("연결하기");
        connectButton.setBounds(10,50,300,30);
        connectButton.addActionListener(e->{
            Main.userSocket = new UserSocket(serverIp.getText(),port.getText());
            new LoginFrame();
            dispose();
        });
        add(connectButton);
        setVisible(true);
    }
}
