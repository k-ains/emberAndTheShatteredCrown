package src.game;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Assets {

    public static BufferedImage background;
    public static BufferedImage spike;

    public static BufferedImage[] playerIdle;
    public static BufferedImage[] playerWalk;
    public static BufferedImage playerJump;   // will use player_hurt
    public static BufferedImage playerFall;

    public static BufferedImage[] enemyWalk;
    public static BufferedImage enemyFallback;


    public static BufferedImage playerFallback;

    public static void init() {
        // background + spikes
        background = tryLoad("assets/bg_tower.png", "src/assets/bg_tower.png");
        spike      = tryLoad("assets/spikes.png",    "src/assets/spikes.png");

        // idle frames
        playerIdle = new BufferedImage[2];
        playerIdle[0] = tryLoad("assets/player_idle_0.png", "src/assets/player_idle_0.png");
        playerIdle[1] = tryLoad("assets/player_idle_1.png", "src/assets/player_idle_1.png");

        // walk frames
        playerWalk = new BufferedImage[2];
        playerWalk[0] = tryLoad("assets/player_walk_0.png", "src/assets/player_walk_0.png");
        playerWalk[1] = tryLoad("assets/player_walk_1.png", "src/assets/player_walk_1.png");

        // jump uses the DUCK sprite
playerJump = tryLoad("assets/player_duck.png", "src/assets/player_duck.png");

// fall uses the FALL sprite
playerFall = tryLoad("assets/player_fall.png", "src/assets/player_fall.png");

        // fallback if anything fails
        if (playerIdle[0] != null) {
            playerFallback = playerIdle[0];
        } else {
            playerFallback = playerJump; // or null, then it will use orange box
        }


        // enemy walk frames (imp)
enemyWalk = new BufferedImage[2];
enemyWalk[0] = tryLoad("assets/imp_walk_0.png", "src/assets/imp_walk_0.png");
enemyWalk[1] = tryLoad("assets/imp_walk_1.png", "src/assets/imp_walk_1.png");

if (enemyWalk[0] != null) {
    enemyFallback = enemyWalk[0];
}

    }

    private static BufferedImage tryLoad(String path1, String path2) {
        BufferedImage img = loadImage(path1);
        if (img == null) {
            img = loadImage(path2);
        }
        return img;
    }

    private static BufferedImage loadImage(String path) {
        BufferedImage img = null;
        try {
            File file = new File(path);
            System.out.println("Trying: " + file.getAbsolutePath() + "  exists=" + file.exists());

            if (file.exists()) {
                img = ImageIO.read(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }
}
