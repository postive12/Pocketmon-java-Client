package org.network.gamecompnent;

import org.network.UserData;
import org.network.UserSocket;
import org.network.data.UserMoveData;
import org.network.gamecore.GameConfig;
import org.network.gamecore.GameObject;
import org.network.gamecore.Input;
import org.network.packet.UserMoveListPacket;
import org.network.packet.UserMovePacket;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameManager extends GameObject {
    public static GameManager current;
    private Point localPlayerDirection = new Point(0,0);
    public GameManager(){
        current = this;
    }
    @Override
    public void update() {
        GameObject localPlayer = findGameObjectByIdentificationId(UserData.username);
        if (localPlayer == null){
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
            if (g != null){
                System.out.println(g.getIdentificationId());
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
        for (var target : GameObject.userGameObjects){
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
