package org.network.gamecompnent;

import org.network.gamecore.GameConfig;
import org.network.gamecore.GameObject;

import java.awt.*;

public class PhysicsManager {
    public static boolean checkPhysicsByPoint(Point target, Point checkPos, int width, int height){
        return
                target.x - width/2 <= checkPos.x &&
                        checkPos.x <= target.x + width/2 &&
                        target.y - height/2 <= checkPos.y &&
                        checkPos.y <= target.y + height/2;
    }
    public static int getMoveRatioByGameObjectAndSpeed(GameObject gameObject, Point direction,int speed){
        int lastAvailableNumber = 0;
        for (int i = 1; i <= speed; i++){
            Point curPos = new Point(gameObject.getTransform());
            curPos.x += direction.x * i;
            curPos.y += direction.y * i;
            boolean isAvailable = true;
            for (GameObject g: GameObject.gameObjects){
                if (g == gameObject){
                    continue;
                }
                Point leftTop = new Point(curPos.x - gameObject.getWidth()/2,curPos.y - gameObject.getHeight()/2);
                Point leftBottom = new Point(curPos.x- gameObject.getWidth()/2,curPos.y + gameObject.getHeight()/2);
                Point rightTop = new Point(curPos.x + gameObject.getWidth()/2,curPos.y - gameObject.getHeight()/2);
                Point rightBottom = new Point(curPos.x + gameObject.getWidth()/2,curPos.y + gameObject.getHeight()/2);

                if (checkPhysicsByPoint(g.getTransform(), leftTop,g.getWidth(),g.getHeight()) || checkPhysicsByPoint(g.getTransform(), leftBottom,g.getWidth(),g.getHeight()) ||
                    checkPhysicsByPoint(g.getTransform(), rightTop,g.getWidth(),g.getHeight()) || checkPhysicsByPoint(g.getTransform(), rightBottom,g.getWidth(),g.getHeight()))
                {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable){
                lastAvailableNumber = i;
            }
            else {
                break;
            }
        }
        return lastAvailableNumber;
    }
    public static GameObject getObjectFromPos(Point pos){
        for (GameObject g : GameObject.gameObjects){
            if (checkPhysicsByPoint(g.getTransform(), pos,g.getWidth(),g.getHeight())){
                return g;
            }
        }
        return null;
    }
}
