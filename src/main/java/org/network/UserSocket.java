package org.network;

import org.network.gameframes.GameFrame;
import org.network.gameframes.LoginFrame;
import org.network.packet.LoginPacket;
import org.network.packet.LoginPacketType;
import org.network.packet.UserChatPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserSocket extends Thread{
    private static UserSocket current;
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
    private Socket socket; // 연결소켓
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public UserSocket(){
        current = this;
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
    public static UserSocket getInstance(){
        return current;
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
            System.out.println("Alive");
            try {
                Object obcm = null;
                //String msg = null;
                try {
                    obcm = ois.readObject();
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not founded");
                    e.printStackTrace();
                    break;
                }
                if (obcm == null) {
                    System.out.println("Null object received");
                    break;
                }
                System.out.println("packet received");
                //로그인 패킷 처리
                if (obcm instanceof LoginPacket loginPacket){
                    System.out.println("Login packet received");
                    if (loginPacket.loginPacketType==LoginPacketType.LOGIN_ACCEPT){
                        try{
                            LoginFrame.current.close();
                            UserData.username = loginPacket.username;
                            UserData.id = loginPacket.id;
                            new GameFrame();
                        }
                        catch (Exception e){
                            System.out.println("Error occur but alive");
                        }
                    }
                    else {
                        LoginFrame.current.setLoginLog(loginPacket);
                    }
                }
                //유저 채팅 패킷 처리
                if (obcm instanceof UserChatPacket chatPacket){
                    if (!chatPacket.username.equals(UserData.username)){
                        GameFrame.AppendText(chatPacket.chat);
                    }
                }

            } catch (IOException e) {
                System.out.println("Error Client exited");
                try {
                    ois.close();
                    oos.close();
                    socket.close();
                    break;
                } catch (Exception ee) {
                    System.out.println("Error Client exited");
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
