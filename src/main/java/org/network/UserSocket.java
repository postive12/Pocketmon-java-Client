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
import java.util.List;

public class UserSocket extends Thread{
    private static UserSocket current;
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
    private Socket socket; // 연결소켓
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Music intro;
    public UserSocket(){
        current = this;
        try {
            socket = new Socket("127.0.0.1", 30000);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            intro=new Music("music/login.mp3",true);
            intro.start();

            start();
        } catch (NumberFormatException | IOException e) {
            System.out.println("서버에 연결하지 못했습니다.");
            e.printStackTrace();
            System.exit(0);
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
                            if (loginPacket.password.equals("FirstIn")){
                                //이곳에 처음 진입시 활성화 코드 삽입
                                GameFrame.enableFirstPocketSelectPanel(true);
                            }
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
                    if(GameManager.current != null) GameManager.current.updateCharactersByUsername(userMoveListPacket);
                }
                if (obcm instanceof UserBattlePacket userBattlePacket){
                    if (userBattlePacket.commandType.equals("REQUEST")){
                        GameFrame.showOkNoPanel(userBattlePacket.username+"님이 배틀을 요청하셨습니다.<br>배틀을 수락하시겠습니까?",e -> {
                            UserBattlePacket result = new UserBattlePacket(
                                    UserData.id,
                                    UserData.username,
                                    "ACCEPT",
                                    userBattlePacket.username,
                                    null
                            );
                            sendObject(result);
                        });
                    }
                    else {
                        System.out.println(userBattlePacket.target + "/" + userBattlePacket.username);
                        GameManager.current.processBattlePacket(userBattlePacket);
                    }
                }
                if(obcm instanceof ChoosePocketPacket choosePocketPacket){
                   UserData.pocketMonList = choosePocketPacket.pocketMonList;
                   System.out.println(choosePocketPacket.pocketMonList);
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
                    System.exit(0);
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
