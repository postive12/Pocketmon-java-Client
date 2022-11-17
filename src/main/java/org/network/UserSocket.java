package org.network;

import org.network.gamecompnent.GameManager;
import org.network.gameframes.GameFrame;
import org.network.gameframes.LoginFrame;
import org.network.gameframes.Music;
import org.network.packet.*;

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
        Music intro=new Music("music/login.mp3",true);
        intro.start();
        while (true) {
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
                //로그인 패킷 처리
                if (obcm instanceof LoginPacket loginPacket){
                    System.out.println("Login packet received");
                    if (loginPacket.loginPacketType==LoginPacketType.LOGIN_ACCEPT){
                        try{
                            LoginFrame.current.close();
                            UserData.username = loginPacket.username;
                            UserData.id = loginPacket.id;
                            new GameFrame();
                            intro.close();
                            Music lobby=new Music("music/lobby.mp3",true);
                            lobby.start();
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
                if (obcm instanceof UserListPacket userListPacket){
                    GameFrame.updateUserList(userListPacket);
                }
                if (obcm instanceof UserMoveListPacket userMoveListPacket){
                    //System.out.println("Receive userMoveList");
                    GameManager.current.updateCharactersByUsername(userMoveListPacket);
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
