package src.game;

import javax.sound.sampled.*;
import java.io.InputStream;

public class Sound {

    private static Clip musicClip;

    // one-shot sound effects
    public static void play(String path) {
        try {
            InputStream is = Sound.class.getResourceAsStream(path);
            if (is == null) return;

            AudioInputStream audio = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // looping background music
    public static void playMusic(String path) {
        try {
            if (musicClip != null && musicClip.isRunning()) {
                return; // already playing
            }

            InputStream is = Sound.class.getResourceAsStream(path);
            if (is == null) {
                System.err.println("Music not found: " + path);
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(is);
            musicClip = AudioSystem.getClip();
            musicClip.open(audio);

            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
        }
    }
}
