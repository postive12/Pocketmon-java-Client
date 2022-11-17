package org.network.gameframes;



import javazoom.jl.player.Player;
import org.network.Main;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class Music extends Thread{
    private AudioInputStream ais;
    private Boolean isLoop;
    private Player player;
    private BufferedInputStream bis;
    private File file;
    private FileInputStream fis;
   public Music(String path,boolean isLoop){
       try {
           this.isLoop=isLoop;
           file=new File(Main.class.getClassLoader().getResource(path).toURI());
           fis=new FileInputStream(file);
           bis=new BufferedInputStream(fis);
           player=new Player(bis);
       }
       catch (Exception e){

       }
    }
    public void close(){
       isLoop=false;
       player.close();
       this.interrupt();
    }
    public void run(){
       try{
           do{
               player.play();
           }while(isLoop);
       }catch (Exception e){

       }
    }

}
