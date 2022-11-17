package org.network.gamecore;

import org.network.gamecompnent.GameManager;

import java.awt.*;
import java.util.Vector;

public class GameObject
{
    public static Vector<GameObject> gameObjects = new Vector<>();
    private boolean isEnabled = true;
    private boolean isAnimationStop = false;
    private int defaultImagePos = 0;
    private int id;
    private String identificationId = "";
    private Image image;

    private Point size = new Point(0,0);
    private Point transform = new Point(0,0);
    private int imgSwapRatio = 1;
    private int imageTick = 10;
    private int currentImgLine = 0;
    private Point imageCount = new Point(1,1);
    public static void Initiate(GameObject gameObject){
        gameObjects.add(gameObject);
    }
    public static void Destroy(GameObject gameObject){
        gameObjects.remove(gameObject);
    }
    public static GameObject findGameObjectByIdentificationId(String identificationId){
        for (GameObject g : gameObjects){
            if (g.identificationId.equals(identificationId)){
                return g;
            }
        }
        return null;
    }
    public static void updateGameObjects(){
        GameManager.current.update();
        for (GameObject g : gameObjects){
            if (g.isEnabled){
                g.update();
            }
        }
    }
    public String getIdentificationId() {
        return identificationId;
    }

    public void update(){
        //Override and put code to here
    }
    public void confineInWindow(){

    }
    public void setIdentificationId(String identificationId) {
        this.identificationId = identificationId;
    }
    public int calCurrentImgX(int cnt){
        int imgX = isAnimationStop ?
                defaultImagePos :
                (cnt%(60/imgSwapRatio))/(imageTick/imgSwapRatio);
        return Math.min(imgX, imageCount.x - 1);


    }
    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public Point getTransform() {
        return transform;
    }
    public void setTransform(Point transform) {
        this.transform = transform;
    }

    public int getImgSwapRatio() {
        return imgSwapRatio;
    }

    public void setImgSwapRatio(int imgSwapRatio) {
        this.imgSwapRatio = imgSwapRatio>0?imgSwapRatio:1;
    }
    public void setImageTick(int sizeX) {
        imageTick = 60/sizeX;
    }

    public Point getImageCount() {
        return imageCount;
    }
    public boolean getIsAnimationStop() {
        return isAnimationStop;
    }

    public void setIsAnimationStop(boolean animationStop) {
        isAnimationStop = animationStop;

    }

    public int getDefaultImagePos() {
        return defaultImagePos;
    }

    public void setDefaultImagePos(int defaultImagePos) {
        this.defaultImagePos = defaultImagePos;
    }
    public void setImageCount(Point imageCount) {
        this.imageCount = imageCount;
    }
    public int getXSize(Canvas canvas){
        if (image==null){
            return 0;
        }
        return image.getWidth(canvas)/imageCount.x;
    }
    public int getYSize(Canvas canvas){
        if (image==null){
            return 0;
        }
        return image.getHeight(canvas)/imageCount.y;
    }
    public int getCurrentImgLine() {
        return currentImgLine;
    }
    public void setCurrentImgLine(int currentImgLine) {
        this.currentImgLine = currentImgLine;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
    public void setSize(Point size) {
        this.size = size;
    }
    public int getWidth(){
        return size.x;
    }
    public int getHeight(){
        return size.y;
    }
}
