package org.network.gameframes;



import org.network.Main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.InputStream;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class Music extends Thread{
    private AudioInputStream ais;
   public void play(String path){
       try {

           ais = AudioSystem.getAudioInputStream(
                   new File(Main.class.getClassLoader().getResource(path).toURI()));
           start();
       }
       catch (Exception e){

       }


   }
    @Override
    public void run() {
        while (true){
            try{
                Clip clip=AudioSystem.getClip();

                clip.open(ais);

                //소리 설정
                //FloatControl gainControl=(FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                //불륨조절
                //gainControl.setValue(-30.0f);

                clip.start();
                Thread.sleep(clip.getMicrosecondLength()/1000);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}