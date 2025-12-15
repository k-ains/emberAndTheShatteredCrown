package src.game;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Assets {

    // ---------- BACKGROUNDS ----------
    public static BufferedImage backgroundTower;
    public static BufferedImage backgroundTown;
    public static BufferedImage backgroundBeach;
    public static BufferedImage backgroundIce;
    public static BufferedImage background;

    // ---------- COMMON ----------
    public static BufferedImage door;
    public static BufferedImage spike; // old spike (tower)
    public static BufferedImage[] enemyWalk;   // imp frames
    public static BufferedImage enemyFallback;

    // ---------- PLAYER ----------
    public static BufferedImage[] playerIdle;
    public static BufferedImage[] playerWalk;
    public static BufferedImage playerJump;
    public static BufferedImage playerFall;
    public static BufferedImage playerFallback;

    // ---------- COINS / HUD ----------
    public static BufferedImage[] coinGold;
    public static BufferedImage heart;

    // ---------- CHECKPOINT / BOXES ----------
    public static BufferedImage flagDown;
    public static BufferedImage flagRaised;
    public static BufferedImage boxExclaim;
    public static BufferedImage boxQuestion;
    public static BufferedImage boxEmpty;

    // NEW: heart box pickup
    public static BufferedImage boxHeart;

    // ---------- CASTLE / TOWER TILES ----------
    public static BufferedImage castlePlatform;
    public static BufferedImage castleFloor;
    public static BufferedImage castleTorch;

    public static BufferedImage platformLeft;
    public static BufferedImage platformMid;
    public static BufferedImage platformRight;

    // ---------- TOWN FLOOR TILES ----------
    public static BufferedImage townTopLeft;
    public static BufferedImage townTopMid;
    public static BufferedImage townTopRight;
    public static BufferedImage townWall;
    public static BufferedImage townBotLeft;
    public static BufferedImage townBotRight;

    // ---------- TOWN HOUSE SETS ----------
    // Beach house
    public static BufferedImage beachWall;
    public static BufferedImage beachDoor;
    public static BufferedImage beachWindow;
    public static BufferedImage beachRoofLeft;
    public static BufferedImage beachRoofMid;
    public static BufferedImage beachRoofRight;

    // Ice house
    public static BufferedImage iceWall;
    public static BufferedImage iceDoor;
    public static BufferedImage iceWindow;
    public static BufferedImage iceRoofLeft;
    public static BufferedImage iceRoofMid;
    public static BufferedImage iceRoofRight;

    // Candy house
    public static BufferedImage candyWall;
    public static BufferedImage candyDoor;
    public static BufferedImage candyWindow;
    public static BufferedImage candyRoofLeft;
    public static BufferedImage candyRoofMid;
    public static BufferedImage candyRoofRight;

    // ---------- BEACH LEVEL FLOOR / PLATFORMS ----------
    public static BufferedImage beachFloorTopLeft;
    public static BufferedImage beachFloorTopMid;
    public static BufferedImage beachFloorTopRight;

    public static BufferedImage beachFloor;
    public static BufferedImage beachFloorBottomLeft;
    public static BufferedImage beachFloorBottomRight;

    // ---------- BEACH SPECIAL PLATFORMS ----------
    public static BufferedImage umbrellaLeft;
    public static BufferedImage umbrellaMid;
    public static BufferedImage umbrellaRight;

    public static BufferedImage treeLeft;
    public static BufferedImage treeMid;
    public static BufferedImage treeRight;
    public static BufferedImage treeTrunk;
    public static BufferedImage treeGroundLeft;
    public static BufferedImage treeGroundMid;
    public static BufferedImage treeGroundRight;

    // ---------- BEACH ENEMIES ----------
    public static BufferedImage[] crabWalk;      // crab_walk_0..3
    public static BufferedImage crabFallback;

    public static BufferedImage plantIdle0;
    public static BufferedImage plantIdle1;
    public static BufferedImage plantIdle2;
    public static BufferedImage plantIdle3;

    // ---------- STARS ----------
    public static BufferedImage[] starFrames;    // star_0..9

    // ---------- BEACH SPIKES ----------
    public static BufferedImage coralSpike;
    public static BufferedImage seagrassSpike;



    // ---------- ICE LEVEL FLOOR / PLATFORMS ----------
    public static BufferedImage iceFloorTopLeft;
    public static BufferedImage iceFloorTopMid;
    public static BufferedImage iceFloorTopRight;
    public static BufferedImage iceSlopeRight;
    public static BufferedImage iceSlopeLeft;
    public static BufferedImage iceSpike;

    public static BufferedImage iceFloor;
    public static BufferedImage[] iceFloorBreakable;


    // ---------- ICE ENEMIES ----------
    public static BufferedImage[] ghostWalk;
    public static BufferedImage ghostFallback;


    public static void init() {

        // backgrounds
        backgroundTower = tryLoad("assets/bg_tower.png", "src/assets/bg_tower.png");
        backgroundTown  = tryLoad("assets/bg_town.png",  "src/assets/bg_town.png");
        backgroundBeach = tryLoad("assets/bg_beach.png", "src/assets/bg_beach.png");
        backgroundIce = tryLoad("assets/bg_ice.png", "src/assets/bg_ice.png");

        // default background
        background = backgroundTower;

        // common
        door  = tryLoad("assets/door.png", "src/assets/door.png");
        spike = tryLoad("assets/spikes.png", "src/assets/spikes.png");

        // player idle
        playerIdle = new BufferedImage[2];
        playerIdle[0] = tryLoad("assets/player_idle_0.png", "src/assets/player_idle_0.png");
        playerIdle[1] = tryLoad("assets/player_idle_1.png", "src/assets/player_idle_1.png");

        // player walk
        playerWalk = new BufferedImage[2];
        playerWalk[0] = tryLoad("assets/player_walk_0.png", "src/assets/player_walk_0.png");
        playerWalk[1] = tryLoad("assets/player_walk_1.png", "src/assets/player_walk_1.png");

        // player jump/fall
        playerJump = tryLoad("assets/player_duck.png", "src/assets/player_duck.png");
        playerFall = tryLoad("assets/player_fall.png", "src/assets/player_fall.png");

        // fallback
        if (playerIdle[0] != null) {
            playerFallback = playerIdle[0];
        } else {
            playerFallback = playerJump;
        }

        // ---------- IMP ENEMY (TOWER) ----------
        enemyWalk = new BufferedImage[2];
        enemyWalk[0] = tryLoad("assets/imp_walk_0.png", "src/assets/imp_walk_0.png");
        enemyWalk[1] = tryLoad("assets/imp_walk_1.png", "src/assets/imp_walk_1.png");

        if (enemyWalk[0] != null) {
            enemyFallback = enemyWalk[0];
}

        // coins
        coinGold = new BufferedImage[8];
        coinGold[0] = tryLoad("assets/coin_gold_0.png", "src/assets/coin_gold_0.png");
        coinGold[1] = tryLoad("assets/coin_gold_1.png", "src/assets/coin_gold_1.png");
        coinGold[2] = tryLoad("assets/coin_gold_2.png", "src/assets/coin_gold_2.png");
        coinGold[3] = tryLoad("assets/coin_gold_3.png", "src/assets/coin_gold_3.png");
        coinGold[4] = tryLoad("assets/coin_gold_4.png", "src/assets/coin_gold_4.png");
        coinGold[5] = tryLoad("assets/coin_gold_5.png", "src/assets/coin_gold_5.png");
        coinGold[6] = tryLoad("assets/coin_gold_6.png", "src/assets/coin_gold_6.png");
        coinGold[7] = tryLoad("assets/coin_gold_7.png", "src/assets/coin_gold_7.png");

        // hud
        heart = tryLoad("assets/heart_0.png", "src/assets/heart_0.png");

        // flags
        flagDown   = tryLoad("assets/flag_red_down.png", "src/assets/flag_red_down.png");
        flagRaised = tryLoad("assets/flag_green_raised.png", "src/assets/flag_green_raised.png");

        // boxes
        boxExclaim  = tryLoad("assets/box_exclaim.png",  "src/assets/box_exclaim.png");
        boxQuestion = tryLoad("assets/box_question.png", "src/assets/box_question.png");
        boxEmpty    = tryLoad("assets/box_empty.png",    "src/assets/box_empty.png");

        // heart box pickup
        boxHeart = tryLoad("assets/box_heart.png", "src/assets/box_heart.png");

        // castle tiles (optional, you already had these)
        castlePlatform = tryLoad("assets/castle tiles (43).png", "src/assets/castle tiles (43).png");
        castleFloor    = tryLoad("assets/castle tiles (42).png", "src/assets/castle tiles (42).png");
        castleTorch    = tryLoad("assets/castle tiles (35).png", "src/assets/castle tiles (35).png");

        platformLeft  = tryLoad("assets/castle tiles (61).png", "src/assets/castle tiles (61).png");
        platformMid   = tryLoad("assets/castle tiles (45).png", "src/assets/castle tiles (45).png");
        platformRight = tryLoad("assets/castle tiles (42).png", "src/assets/castle tiles (42).png");

        // town floor tiles
        townTopLeft  = tryLoad("assets/townTopLeft.png",  "src/assets/townTopLeft.png");
        townTopMid   = tryLoad("assets/townTopMid.png",   "src/assets/townTopMid.png");
        townTopRight = tryLoad("assets/townTopRight.png", "src/assets/townTopRight.png");
        townWall     = tryLoad("assets/townWall.png",     "src/assets/townWall.png");
        townBotLeft  = tryLoad("assets/townBotLeft.png",  "src/assets/townBotLeft.png");
        townBotRight = tryLoad("assets/townBotRight.png", "src/assets/townBotRight.png");

        // beach house tiles (TOWN menu houses)
        beachWall     = tryLoad("assets/beachWall.png",     "src/assets/beachWall.png");
        beachDoor     = tryLoad("assets/beachDoor.png",     "src/assets/beachDoor.png");
        beachWindow   = tryLoad("assets/beachWindow.png",   "src/assets/beachWindow.png");
        beachRoofLeft = tryLoad("assets/beachRoofLeft.png", "src/assets/beachRoofLeft.png");
        beachRoofMid  = tryLoad("assets/beachRoofMid.png",  "src/assets/beachRoofMid.png");
        beachRoofRight= tryLoad("assets/beachRoofRight.png","src/assets/beachRoofRight.png");

        // ice house tiles
        iceWall      = tryLoad("assets/iceWall.png",      "src/assets/iceWall.png");
        iceDoor      = tryLoad("assets/iceDoor.png",      "src/assets/iceDoor.png");
        iceWindow    = tryLoad("assets/iceWindow.png",    "src/assets/iceWindow.png");
        iceRoofLeft  = tryLoad("assets/iceRoofLeft.png",  "src/assets/iceRoofLeft.png");
        iceRoofMid   = tryLoad("assets/iceRoofMid.png",   "src/assets/iceRoofMid.png");
        iceRoofRight = tryLoad("assets/iceRoofRight.png", "src/assets/iceRoofRight.png");
        
        // candy house tiles
        candyWall      = tryLoad("assets/candyWall.png",      "src/assets/candyWall.png");
        candyDoor      = tryLoad("assets/candyDoor.png",      "src/assets/candyDoor.png");
        candyWindow    = tryLoad("assets/candyWindow.png",    "src/assets/candyWindow.png");
        candyRoofLeft  = tryLoad("assets/candyRoofLeft.png",  "src/assets/candyRoofLeft.png");
        candyRoofMid   = tryLoad("assets/candyRoofMid.png",   "src/assets/candyRoofMid.png");
        candyRoofRight = tryLoad("assets/candyRoofRight.png", "src/assets/candyRoofRight.png");

        // beach level floor/platform tiles (these are separate from house tiles)
        beachFloorTopLeft     = tryLoad("assets/beachFloorTopLeft.png",     "src/assets/beachFloorTopLeft.png");
        beachFloorTopMid      = tryLoad("assets/beachFloorTopMid.png",      "src/assets/beachFloorTopMid.png");
        beachFloorTopRight    = tryLoad("assets/beachFloorTopRight.png",    "src/assets/beachFloorTopRight.png");
        beachFloor            = tryLoad("assets/beachFloor.png",            "src/assets/beachFloor.png");
        beachFloorBottomLeft  = tryLoad("assets/beachFloorBottomLeft.png",  "src/assets/beachFloorBottomLeft.png");
        beachFloorBottomRight = tryLoad("assets/beachFloorBottomRight.png", "src/assets/beachFloorBottomRight.png");

        // ice level floor/platform tiles (these are separate from house tiles)
        iceFloorTopLeft     = tryLoad("assets/iceFloorTopLeft.png",     "src/assets/iceFloorTopLeft.png");
        iceFloorTopMid      = tryLoad("assets/iceFloorTopMid.png",     "src/assets/iceFloorTopMid.png");
        iceFloorTopRight    = tryLoad("assets/iceFloorTopRight.png",     "src/assets/iceFloorTopRight.png");
        iceFloor            = tryLoad("assets/iceFloor.png",            "src/assets/iceFloor.png");
        
        iceFloorBreakable = new BufferedImage[2];
        iceFloorBreakable[0]   = tryLoad("assets/iceFloorBreakable.png",    "src/assets/iceFloorBreakable.png");
        iceFloorBreakable[1]   = tryLoad("assets/iceFloorBreaking.png",     "src/assets/iceFloorBreaking.png");
        
        iceSlopeLeft        = tryLoad("assets/iceSlopeLeft.png",     "src/assets/iceSlopeLeft.png");
        iceSlopeRight       = tryLoad("assets/iceSlopeRight.png",     "src/assets/iceSlopeRight.png");
        iceSpike            = tryLoad("assets/iceSpike.png",     "src/assets/iceSpike.png");
        
        // umbrella platform tiles
        umbrellaLeft  = tryLoad("assets/umbrellaLeft.png",  "src/assets/umbrellaLeft.png");
        umbrellaMid   = tryLoad("assets/umbrellaMid.png",   "src/assets/umbrellaMid.png");
        umbrellaRight = tryLoad("assets/umbrellaRight.png", "src/assets/umbrellaRight.png");

        // tree tiles
        treeLeft       = tryLoad("assets/treeLeft.png",       "src/assets/treeLeft.png");
        treeMid        = tryLoad("assets/treeMid.png",        "src/assets/treeMid.png");
        treeRight      = tryLoad("assets/treeRight.png",      "src/assets/treeRight.png");
        treeTrunk      = tryLoad("assets/treeTrunk.png",      "src/assets/treeTrunk.png");
        treeGroundLeft = tryLoad("assets/treeGroundLeft.png", "src/assets/treeGroundLeft.png");
        treeGroundMid  = tryLoad("assets/treeGroundMid.png",  "src/assets/treeGroundMid.png");
        treeGroundRight= tryLoad("assets/treeGroundRight.png","src/assets/treeGroundRight.png");

        // crab enemy frames
        crabWalk = new BufferedImage[4];
        crabWalk[0] = tryLoad("assets/crab_walk_0.png", "src/assets/crab_walk_0.png");
        crabWalk[1] = tryLoad("assets/crab_walk_1.png", "src/assets/crab_walk_1.png");
        crabWalk[2] = tryLoad("assets/crab_walk_2.png", "src/assets/crab_walk_2.png");
        crabWalk[3] = tryLoad("assets/crab_walk_3.png", "src/assets/crab_walk_3.png");

        if (crabWalk[0] != null) {
            crabFallback = crabWalk[0];
        }

        // ghost enemy frames
        ghostWalk = new BufferedImage[2];
        ghostWalk[0] = tryLoad("assets/ghost_float_0.png", "src/assets/ghost_float_0.png");
        ghostWalk[1] = tryLoad("assets/ghost_float_1.png", "src/assets/ghost_float_1.png");
        
        if (crabWalk[0] != null) {
            ghostFallback = ghostWalk[0];
        }

        // plant frames
        plantIdle0 = tryLoad("assets/plant_idle_0.png", "src/assets/plant_idle_0.png");
        plantIdle1 = tryLoad("assets/plant_idle_1.png", "src/assets/plant_idle_1.png");
        plantIdle2 = tryLoad("assets/plant_idle_2.png", "src/assets/plant_idle_2.png");
        plantIdle3 = tryLoad("assets/plant_idle_3.png", "src/assets/plant_idle_3.png");

        // star animation frames
        starFrames = new BufferedImage[10];
        starFrames[0] = tryLoad("assets/star_0.png", "src/assets/star_0.png");
        starFrames[1] = tryLoad("assets/star_1.png", "src/assets/star_1.png");
        starFrames[2] = tryLoad("assets/star_2.png", "src/assets/star_2.png");
        starFrames[3] = tryLoad("assets/star_3.png", "src/assets/star_3.png");
        starFrames[4] = tryLoad("assets/star_4.png", "src/assets/star_4.png");
        starFrames[5] = tryLoad("assets/star_5.png", "src/assets/star_5.png");
        starFrames[6] = tryLoad("assets/star_6.png", "src/assets/star_6.png");
        starFrames[7] = tryLoad("assets/star_7.png", "src/assets/star_7.png");
        starFrames[8] = tryLoad("assets/star_8.png", "src/assets/star_8.png");
        starFrames[9] = tryLoad("assets/star_9.png", "src/assets/star_9.png");

        // beach spikes
        coralSpike    = tryLoad("assets/coralSpike.png",    "src/assets/coralSpike.png");
        seagrassSpike = tryLoad("assets/seagrassSpike.png", "src/assets/seagrassSpike.png");
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
