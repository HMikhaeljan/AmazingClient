/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Jeroen0606
 */
public final class Music {

    static Timer timer;
    static AudioStream audioStream;

    private Music() {

    }

    public static void startMusic(int nummer) throws IOException {
        timer = new Timer();
        switch (nummer) {
            case 1:
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        audioStream = null;
                        try {
                            InputStream in = null;
                            String gongFile = "Menu.WAV";
                            in = new FileInputStream(gongFile);
                            // create an audiostream from the inputstream
                            audioStream = new AudioStream(in);
                            // play the audio clip with the audioplayer class
                            AudioPlayer.player.start(audioStream);
                        } catch (IOException ex) {
                            System.out.println("Muziek Error!");
                        }
                    }
                 ;
            }, 0, 114500);
                break;
                
            case 2:
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        audioStream = null;
                        try {
                            InputStream in = null;
                            String gongFile = "TheMaze.WAV";
                            in = new FileInputStream(gongFile);
                            // create an audiostream from the inputstream
                            audioStream = new AudioStream(in);
                            // play the audio clip with the audioplayer class
                            AudioPlayer.player.start(audioStream);
                        } catch (IOException ex) {
                            System.out.println("Muziek Error!");
                        }
                    }
                 ;
            }, 0, 44250);
        }

    }

    public static void stopMusic() throws IOException {
        timer.cancel();
        audioStream.close();
    }
}
