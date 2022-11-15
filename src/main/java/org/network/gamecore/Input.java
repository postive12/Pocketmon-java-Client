package org.network.gamecore;

import java.util.Vector;

public class Input {
    private static final Vector<Integer> userInputs = new Vector<Integer>();
    private static final Vector<Integer> userInputsDown = new Vector<>();
    public static synchronized void SetPressed(int state){
        //System.out.println("Set Pressed");
        userInputsDown.add(state);
        if (!userInputs.contains(state)){
            userInputs.add(state);
        }
    }
    public static synchronized void UnsetPressed(int state){
        //System.out.println("Unset Pressed");
        if (!userInputs.contains(state))
            return;
        int i = 0;
        for(i = 0; i < userInputs.size();i++){
            if (userInputs.get(i) == state){
                break;
            }
        }
        userInputs.remove(i);
    }
    public static boolean GetKeyPressed(int state){
        return userInputs.contains(state);
    }
    public static boolean GetKeyDown(int state){
        return userInputsDown.contains(state);
    }
    public static void clearInput(){
        userInputs.clear();
    }
    public static void clearInputDown(){
        userInputsDown.clear();
    }
}
