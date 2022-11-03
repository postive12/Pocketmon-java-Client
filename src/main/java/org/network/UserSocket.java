package org.network;

import org.network.packet.LoginPacket;
import org.network.packet.LoginPacketType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserSocket extends Thread{
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
    private Socket socket; // 연결소켓
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public UserSocket(){
        try {
            socket = new Socket("127.0.0.1", 30000);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());
            start();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }
    public void Login(String username,String password){
        LoginPacket loginPacket = new LoginPacket(
                -1,
                LoginPacketType.LOGIN,
                username,
                password
        );
        sendObject(loginPacket);
    }
    public void SignIn(String username,String password){
        LoginPacket loginPacket = new LoginPacket(
                -1,
                LoginPacketType.SIGN_IN,
                username,
                password
        );
        sendObject(loginPacket);
    }
    public void run() {
        while (true) {
            try {
                Object obcm = null;
                String msg = null;
                try {
                    obcm = ois.readObject();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    break;
                }
                if (obcm == null)
                    break;
            } catch (IOException e) {
                try {
                    ois.close();
                    oos.close();
                    socket.close();
                    break;
                } catch (Exception ee) {
                    break;
                } // catch문 끝
            } // 바깥 catch문끝

        }
    }

    public void sendObject(Object ob) { // 서버로 메세지를 보내는 메소드
        try {
            oos.writeObject(ob);
        } catch (IOException e) {
            //AppendText("SendObject Error");
        }
    }

}
