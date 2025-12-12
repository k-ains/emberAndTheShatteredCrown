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

    public static java.awt.image.BufferedImage[] coinGold;
    public static java.awt.image.BufferedImage heart;

    public static java.awt.image.BufferedImage flagDown;
    public static java.awt.image.BufferedImage flagRaised;
    public static java.awt.image.BufferedImage boxExclaim;
    public static java.awt.image.BufferedImage boxQuestion;
    public static java.awt.image.BufferedImage boxPlain;
    public static java.awt.image.BufferedImage castlePlatform;
public static java.awt.image.BufferedImage castleFloor;
public static java.awt.image.BufferedImage castleTorch; // optional

public static java.awt.image.BufferedImage platformLeft;
public static java.awt.image.BufferedImage platformMid;
public static java.awt.image.BufferedImage platformRight;



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
        // gold coin animation frames
        coinGold = new java.awt.image.BufferedImage[8];
        coinGold[0] = tryLoad("assets/coin_gold_0.png", "src/assets/coin_gold_0.png");
        coinGold[1] = tryLoad("assets/coin_gold_1.png", "src/assets/coin_gold_1.png");
        coinGold[2] = tryLoad("assets/coin_gold_2.png", "src/assets/coin_gold_2.png");
        coinGold[3] = tryLoad("assets/coin_gold_3.png", "src/assets/coin_gold_3.png");
        coinGold[4] = tryLoad("assets/coin_gold_4.png", "src/assets/coin_gold_4.png");
        coinGold[5] = tryLoad("assets/coin_gold_5.png", "src/assets/coin_gold_5.png");
        coinGold[6] = tryLoad("assets/coin_gold_6.png", "src/assets/coin_gold_6.png");
        coinGold[7] = tryLoad("assets/coin_gold_7.png", "src/assets/coin_gold_7.png");

        // heart icon (for HUD)
        heart = tryLoad("assets/heart_0.png", "src/assets/heart_0.png");

        // checkpoint flag down / raised
        flagDown   = tryLoad("assets/flag_red_down.png",   "src/assets/flag_red_down.png");
        flagRaised = tryLoad("assets/flag_green_raised.png", "src/assets/flag_green_raised.png");
        // message / item boxes
        boxExclaim = tryLoad("assets/box_exclaim.png", "src/assets/box_exclaim.png");
        boxQuestion = tryLoad("assets/box_question.png", "src/assets/box_question.png");
        boxPlain   = tryLoad("assets/box_plain.png",   "src/assets/box_plain.png");
       // castle tiles
castlePlatform = tryLoad(
    "assets/castle tiles (43).png",
    "src/assets/castle tiles (43).png"
);

castleFloor = tryLoad(
    "assets/castle tiles (42).png",
    "src/assets/castle tiles (42).png"
);

// optional decoration (torch)
castleTorch = tryLoad(
    "assets/castle tiles (35).png",
    "src/assets/castle tiles (35).png"
);
// segmented castle platforms (left, middle, right)
platformLeft = tryLoad(
    "assets/castle tiles (61).png",
    "src/assets/castle tiles (61).png"
);

platformMid = tryLoad(
    "assets/castle tiles (45).png",
    "src/assets/castle tiles (45).png"
);

platformRight = tryLoad(
    "assets/castle tiles (42).png",
    "src/assets/castle tiles (42).png"
);


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
