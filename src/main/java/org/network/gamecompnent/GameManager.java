package org.network.gamecompnent;

import org.network.UserData;
import org.network.UserSocket;
import org.network.WindowConfig;
import org.network.data.PocketMonData;
import org.network.data.UserMoveData;
import org.network.gamecore.GameConfig;
import org.network.gamecore.GameObject;
import org.network.gamecore.Input;
import org.network.gameframes.GameFrame;
import org.network.packet.UserBattlePacket;
import org.network.packet.UserMoveListPacket;
import org.network.packet.UserMovePacket;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameManager extends GameObject {
    private static GameManager current;
    public boolean isLocalPlayerMovable = true;
    private Point localPlayerDirection = new Point(0,0);
    public GameManager(){
        current = this;
        
        //맵 구조물 추가
        GameObject structure01 = new GameObject();
        structure01.setIdentificationId("STRUCTURE_GROUP");
        structure01.setTransform(new Point(166,128));
        structure01.setSize(new Point(332,256));
        Initiate(structure01);

        GameObject wallLeft = new GameObject();
        wallLeft.setIdentificationId("STRUCTURE_GROUP");
        wallLeft.setTransform(new Point(-50,WindowConfig.HEIGHT/2));
        wallLeft.setSize(new Point(100, WindowConfig.HEIGHT));
        Initiate(wallLeft);

        GameObject wallRight = new GameObject();
        wallRight.setIdentificationId("STRUCTURE_GROUP");
        wallRight.setTransform(new Point(WindowConfig.WIDTH * 2 / 3 + 50,WindowConfig.HEIGHT/2));
        wallRight.setSize(new Point(100, WindowConfig.HEIGHT));
        Initiate(wallRight);

        GameObject wallTop = new GameObject();
        wallTop.setIdentificationId("STRUCTURE_GROUP");
        wallTop.setTransform(new Point(WindowConfig.WIDTH/2,-50));
        wallTop.setSize(new Point(WindowConfig.WIDTH, 100));
        Initiate(wallTop);

        GameObject wallBottom = new GameObject();
        wallBottom.setIdentificationId("STRUCTURE_GROUP");
        wallBottom.setTransform(new Point(WindowConfig.WIDTH/2,WindowConfig.HEIGHT + 50));
        wallBottom.setSize(new Point(WindowConfig.WIDTH,100));
        Initiate(wallBottom);
    }
    public static GameManager getInstance(){
        return current;
    }
    @Override
    public void update() {
        GameObject localPlayer = findGameObjectByIdentificationId(UserData.username);
        if (localPlayer == null){
            return;
        }
        handleEvent();
        if(!isLocalPlayerMovable){
            return;
        }
        if (Input.GetKeyDown(KeyEvent.VK_SPACE)){
            int currentState = localPlayer.getCurrentImgLine();
            Point transform = new Point(localPlayer.getTransform());
            switch (currentState) {
                case 0 -> transform.y -= localPlayer.getHeight();
                case 1 -> transform.x += localPlayer.getWidth();
                case 2 -> transform.y += localPlayer.getHeight();
                case 3 -> transform.x -= localPlayer.getWidth();
            }
            GameObject g = PhysicsManager.getObjectFromPos(transform);
            if (g != null && !g.getIdentificationId().equals("STRUCTURE_GROUP")){
                UserBattlePacket userBattlePacket = new UserBattlePacket(
                        UserData.id,
                        UserData.username,
                        "REQUEST",
                        g.getIdentificationId(),
                        null
                );
                UserSocket.getInstance().sendObject(userBattlePacket);
            }
        }
        if (Input.GetKeyPressed(KeyEvent.VK_A)){
            localPlayerDirection.x -= 1;
        }
        if (Input.GetKeyPressed(KeyEvent.VK_D)){
            localPlayerDirection.x += 1;
        }
        if (Input.GetKeyPressed(KeyEvent.VK_W) && localPlayerDirection.x == 0){
            localPlayerDirection.y -= 1;
        }
        if (Input.GetKeyPressed(KeyEvent.VK_S)&& localPlayerDirection.x == 0){
            localPlayerDirection.y += 1;
        }
        if (localPlayerDirection.x == 0 && localPlayerDirection.y == 0){
            return;
        }
        int ratio = PhysicsManager.getMoveRatioByGameObjectAndSpeed(findGameObjectByIdentificationId(UserData.username),localPlayerDirection, GameConfig.UserSpeed,true);
        sendPlayerMovePacket(ratio);
        localPlayerDirection.y = 0;
        localPlayerDirection.x = 0;
    }
    private void handleEvent() {
        if (Input.GetKeyPressed(KeyEvent.VK_H)){
            List<Integer> args = new ArrayList<>();
            args.add(0);
            args.add(1);
            UserBattlePacket battlePacket = new UserBattlePacket(
                    UserData.id,
                    UserData.username,
                    "ATTACK",
                    "OPPONENT",
                    args
            );
            UserSocket.getInstance().sendObject(battlePacket);
        }
        if (Input.GetKeyDown(KeyEvent.VK_J)){
        }
        if (Input.GetKeyDown(KeyEvent.VK_K)){

        }
        if (Input.GetKeyDown(KeyEvent.VK_L)){
        }
    }
    public void processBattlePacket(UserBattlePacket battlePacket){
        if (battlePacket.commandType.equals("ACCEPT")){
            GameFrame.getInstance().enableBattleWindow(true);
            GameFrame.getInstance().enableChoosePocketForBattlePanel(true);
        }
        if (battlePacket.commandType.equals("HEALTH")){
            List<Integer> data = battlePacket.args;
            GameFrame.getInstance().setPlayerHealth(false,data.get(0),data.get(1));
            GameFrame.getInstance().setPlayerHealth(true,data.get(2),data.get(3));
        }
        if (battlePacket.commandType.equals("CHANGE")){
            boolean isOpponent = !battlePacket.target.equals(UserData.username);
            //System.out.println( "battle packet : "+ battlePacket.target + " / " + UserData.username +"/" +isLocalPlayer);
            GameFrame.getInstance().setPlayerImage(
                    isOpponent,
                    isOpponent ? PocketMonData.monsterInfo.get(battlePacket.args.get(0)).getFrontPath() : PocketMonData.monsterInfo.get(battlePacket.args.get(0)).getBackPath()
            );
        }
        if (battlePacket.commandType.equals("CHANGE_REQUEST")){
            System.out.println("Get Change Request");
            GameFrame.getInstance().setBattleButtonChangeState();
        }
        if(battlePacket.commandType.equals("EXIT")){
            GameFrame.getInstance().enableBattleWindow(false);
        }
    }
    public void sendPlayerMovePacket(int ratio){
        //System.out.println(ratio);
        UserMovePacket userMovePacket = new UserMovePacket(
                UserData.id,
                UserData.username,
                new Point(
                        localPlayerDirection.x * ratio,
                        localPlayerDirection.y * ratio
                )
        );
        UserSocket.getInstance().sendObject(userMovePacket);
    }
    public void updateCharactersByUsername(UserMoveListPacket userMoveListPacket){
        List<String> userList = new ArrayList<>();
        for (UserMoveData userMoveData : userMoveListPacket.userMoveList){
            userList.add(userMoveData.username);
        }
        checkCharactersByUsername(userList);
        for (UserMoveData userMoveData : userMoveListPacket.userMoveList){
            //System.out.println("Username : "+ userMoveData.username + " / Current post : " + userMoveData.currentPos.x +" : " + userMoveData.currentPos.y);
            GameObject g = findGameObjectByIdentificationId(userMoveData.username);
            if (g == null){
                continue;
            }
            Point currentPos = g.getTransform();
            //System.out.println(currentPos.toString()+"/"+userMoveData.currentPos.toString());
            if (currentPos.x == userMoveData.currentPos.x&&currentPos.y == userMoveData.currentPos.y){
                g.setIsAnimationStop(true);
            }
            else {
                g.setIsAnimationStop(false);
            }

            g.setTransform(userMoveData.currentPos);
            g.setCurrentImgLine(userMoveData.seeDirection);
        }
    }
    public void checkCharactersByUsername(List<String> usernameList){
        List<GameObject> destroyTarget = new ArrayList<>();
        for (var target : GameObject.gameObjects){
            if (target.getIdentificationId().equals("STRUCTURE_GROUP")){
                continue;
            }
            if (!usernameList.contains(target.getIdentificationId())){
                destroyTarget.add(target);
            }
        }
        for (GameObject target : destroyTarget){
            Destroy(target);
        }
        for (String username : usernameList){
            if (findGameObjectByIdentificationId(username)==null){
                Initiate(new Character(username));
            }
        }
    }
}
