package org.network.gamecompnent;

import org.network.gamecore.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PhysicsManager {
    public static boolean checkPhysicsByPoint(Point target, Point checkPos, int width, int height){
        return
                target.x - width/2 <= checkPos.x &&
                        checkPos.x <= target.x + width/2 &&
                        target.y - height/2 <= checkPos.y &&
                        checkPos.y <= target.y + height/2;
    }
    public static int getMoveRatioByGameObjectAndSpeed(GameObject gameObject, Point direction,int speed,boolean isConstraintWindow){
        int lastAvailableNumber = 0;
        for (int i = 1; i <= speed; i++){
            Point curPos = new Point(gameObject.getTransform());
            curPos.x += direction.x * i;
            curPos.y += direction.y * i;
            boolean isAvailable = true;
            //구조물 오브젝트 검증

            //게임 오브젝트 검증
            for (GameObject g: GameObject.gameObjects){
                if (g == gameObject){
                    continue;
                }
                if (!g.getIdentificationId().equals("STRUCTURE_GROUP")){
                    continue;
                }
                List<Point> checkList = new ArrayList<>();

                Point leftTop = new Point(curPos.x - gameObject.getWidth()/2,curPos.y - gameObject.getHeight()/2);
                Point leftBottom = new Point(curPos.x- gameObject.getWidth()/2,curPos.y + gameObject.getHeight()/2);
                Point rightTop = new Point(curPos.x + gameObject.getWidth()/2,curPos.y - gameObject.getHeight()/2);
                Point rightBottom = new Point(curPos.x + gameObject.getWidth()/2,curPos.y + gameObject.getHeight()/2);
                if (direction.x == 1){
                    checkList.add(rightTop);
                    checkList.add(rightBottom);
                }
                if (direction.x == -1){
                    checkList.add(leftTop);
                    checkList.add(leftBottom);
                }
                if (direction.y == -1){
                    checkList.add(leftTop);
                    checkList.add(rightTop);
                }
                if (direction.y == 1){
                    checkList.add(leftBottom);
                    checkList.add(rightBottom);
                }
                boolean flag = true;
                for (Point p : checkList){
                    if (checkPhysicsByPoint(g.getTransform(), p,g.getWidth(),g.getHeight())){
                        flag = false;
                        break;
                    }
                }
                if (!flag){
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
