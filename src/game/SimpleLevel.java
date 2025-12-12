package src.game;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleLevel {

    private List<Tile> tiles;
    private List<CheckpointFlag> checkpoints;
    private List<Spike> spikes;
    private List<Coin> coins;
    private List<MessageBox> messageBoxes;
    private List<WalkingEnemy> enemies;

    private int worldHeight;
    private Random random;

    public SimpleLevel() {
        tiles = new ArrayList<Tile>();
        checkpoints = new ArrayList<CheckpointFlag>();
        spikes = new ArrayList<Spike>();
        coins = new ArrayList<Coin>();
        messageBoxes = new ArrayList<MessageBox>();
        enemies = new ArrayList<WalkingEnemy>();
        random = new Random();

        int gap = 120;              // vertical distance between platforms
        int numberOfPlatforms = 25; // how high the tower goes
        int floorHeight = 60;

        // world is tall enough for all platforms + floor
        worldHeight = GamePanel.HEIGHT + numberOfPlatforms * gap + floorHeight;

        int floorY = worldHeight - floorHeight;

        // big floor at the very bottom
       

Tile floor = new Tile(
    0,
    floorY,
    GamePanel.WIDTH,
    floorHeight,
    true
);
tiles.add(floor);




        // ----- RANDOM PLATFORMS, BUT CONTROLLED -----

        int platformWidth = 260;
        int platformHeight = 24;

        int minXAllowed = 40;
        int maxXAllowed = GamePanel.WIDTH - platformWidth - 40;

        int maxHorizontalShift = 200;

        int previousX = (minXAllowed + maxXAllowed) / 2;
        int startY = floorY - 120;

        int i = 0;
        while (i < numberOfPlatforms) {
            int platformY = startY - gap * i;

            int platformX = previousX;
            boolean validPositionFound = false;

            while (!validPositionFound) {
                int fullRange = maxXAllowed - minXAllowed;
                int randomOffset = 0;

                if (fullRange > 0) {
                    randomOffset = random.nextInt(fullRange + 1);
                }

                int candidateX = minXAllowed + randomOffset;

                int diff = candidateX - previousX;
                if (diff < 0) {
                    diff = -diff;
                }

                if (diff <= maxHorizontalShift) {
                    platformX = candidateX;
                    validPositionFound = true;
                }
            }

          Tile platform = new Tile(
    platformX,
    platformY,
    platformWidth,
    platformHeight,
    true
);
tiles.add(platform);



boolean isCheckpointIndex = (i == 4 || i == 9 || i == 14);

if (isCheckpointIndex) {
    int flagWidth = 32;
    int flagHeight = 48;

    int flagX = platformX + platformWidth / 2 - flagWidth / 2;
    int flagY = platformY - flagHeight;

    CheckpointFlag flag = new CheckpointFlag(flagX, flagY);
    checkpoints.add(flag);
}


boolean hasSpike = false;

// spikes on some platforms, but never on checkpoint ones
if (!isCheckpointIndex && i % 3 == 2) {
    int spikeWidth = 32;
int spikeHeight = 28;
int spikeY = platformY - spikeHeight;

int margin = 40;

int laneLeft = platformX + margin;
int laneMiddle = platformX + platformWidth / 2 - spikeWidth / 2;
int laneRight = platformX + platformWidth - margin - spikeWidth;

    int[] laneXs = new int[3];
    laneXs[0] = laneLeft;
    laneXs[1] = laneMiddle;
    laneXs[2] = laneRight;

    boolean[] used = new boolean[3];

    int spikeCount = 1;

    int roll = random.nextInt(100);
    if (roll < 40) {
        spikeCount = 2;
    }

    int s = 0;
    while (s < spikeCount) {
        int laneIndex = random.nextInt(3);

        if (!used[laneIndex]) {
            int spikeX = laneXs[laneIndex];

            Spike spike = new Spike(spikeX, spikeY, spikeWidth, spikeHeight);
            spikes.add(spike);

            used[laneIndex] = true;
            s = s + 1;
        }
    }

    hasSpike = true;
}



// coins on safe platforms (no checkpoint and no spike)
if (!isCheckpointIndex && !hasSpike) {
   int coinSize = 32;
int coinX = platformX + platformWidth / 2 - coinSize / 2;
int coinY = platformY - coinSize - 4; // a little gap above platform
Coin coin = new Coin(coinX, coinY, 1);
coins.add(coin);

}

// message boxes ONLY on platforms without checkpoint and without spike
if (!isCheckpointIndex && !hasSpike) {

    // early movement tip (first safe platform above floor)
    if (i == 0) {
        int boxX = platformX + platformWidth / 2 - 12;
        int boxY = platformY - 70;
        MessageBox box = new MessageBox(
            boxX,
            boxY,
            false,
            "Use A/D or arrow keys to move."
        );
        messageBoxes.add(box);
    }

    // spike warning: BEFORE the first spike (first spike at i = 2)
    if (i == 1) {
        int boxX = platformX + platformWidth / 2 - 12;
        int boxY = platformY - 70;
        MessageBox box = new MessageBox(
            boxX,
            boxY,
            true,
            "Watch out for spikes above!"
        );
        messageBoxes.add(box);
    }

    // checkpoint explanation: BEFORE the first checkpoint (checkpoint at i = 4)
    if (i == 3) {
        int boxX = platformX + platformWidth / 2 - 12;
        int boxY = platformY - 70;
        MessageBox box = new MessageBox(
            boxX,
            boxY,
            true,
            "Flags ahead are checkpoints. Touch to save progress!"
        );
        messageBoxes.add(box);
    }
        // enemy warning: BEFORE the first walking enemy (first enemy at i = 13)
    if (i == 12) {
        int boxX = platformX + platformWidth / 2 - 12;
        int boxY = platformY - 70;  // same height you liked
        MessageBox box = new MessageBox(
            boxX,
            boxY,
            true,
            "Enemies ahead! Don't touch them."
        );
        messageBoxes.add(box);
    }

    // more messages insert here
}

    // enemies on some harder platforms: no checkpoint, no spike, a bit higher up
boolean canHaveEnemy = !isCheckpointIndex && !hasSpike && i >= 5 && i % 4 == 1;

if (canHaveEnemy) {
    int enemyWidth = 40;
int enemyHeight = 32;

int enemyX = platformX + platformWidth / 2 - enemyWidth / 2;
int enemyY = platformY - enemyHeight;

int margin = 30;
int enemyLeft = platformX + margin;
int enemyRight = platformX + platformWidth - margin;


    WalkingEnemy enemy = new WalkingEnemy(
        enemyX,
        enemyY,
        enemyWidth,
        enemyHeight,
        enemyLeft,
        enemyRight,
        2
    );
    enemies.add(enemy);
}



            previousX = platformX;
            i = i + 1;
        }
    }

    public void draw(Graphics g) {
    int i = 0;
    while (i < tiles.size()) {
        Tile t = tiles.get(i);
        t.draw(g);
        i = i + 1;
    }

    i = 0;
    while (i < checkpoints.size()) {
        CheckpointFlag flag = checkpoints.get(i);
        flag.draw(g);
        i = i + 1;
    }

    i = 0;
    while (i < spikes.size()) {
        Spike spike = spikes.get(i);
        spike.draw(g);
        i = i + 1;
    }

    i = 0;
    while (i < coins.size()) {
        Coin coin = coins.get(i);
        coin.draw(g);
        i = i + 1;
    }

    i = 0;
    while (i < messageBoxes.size()) {
        MessageBox box = messageBoxes.get(i);
        box.draw(g);
        i = i + 1;
    }

    i = 0;
    while (i < enemies.size()) {
        WalkingEnemy enemy = enemies.get(i);
        enemy.draw(g);
        i = i + 1;
    }
}


   public void update() {
    int i = 0;

    // update enemies
    while (i < enemies.size()) {
        WalkingEnemy enemy = enemies.get(i);
        enemy.update(this);
        i = i + 1;
    }

    // update coins (for spin animation)
    i = 0;
    while (i < coins.size()) {
        Coin coin = coins.get(i);
        coin.update(this);
        i = i + 1;
    }
}


    public List<Tile> getTiles() {
        List<Tile> result = tiles;
        return result;
    }

    public List<CheckpointFlag> getCheckpoints() {
        List<CheckpointFlag> result = checkpoints;
        return result;
    }

    public List<Spike> getSpikes() {
        List<Spike> result = spikes;
        return result;
    }

    public List<Coin> getCoins() {
        List<Coin> result = coins;
        return result;
    }

    public int getWorldHeight() {
        int value = worldHeight;
        return value;
    }
    public List<MessageBox> getMessageBoxes() {
    List<MessageBox> result = messageBoxes;
    return result;
}
public List<WalkingEnemy> getEnemies() {
    List<WalkingEnemy> result = enemies;
    return result;
}



    // used when you lose all hearts and we want a fresh run
    public void resetCheckpoints() {
        int i = 0;
        while (i < checkpoints.size()) {
            CheckpointFlag flag = checkpoints.get(i);
            flag.reset();
            i = i + 1;
        }
    }

    // how many platforms has the player reached at this Y?
    public int getPlatformsReachedCount(int playerY) {
        int count = 0;
        int i = 1; // index 0 is floor, real platforms start at 1

        while (i < tiles.size()) {
            Tile t = tiles.get(i);
            int platformY = t.getY();

            // smaller y = higher; if player is at or above this platform
            if (playerY <= platformY) {
                count = count + 1;
            }

            i = i + 1;
        }

        int result = count;
        return result;
    }
}
