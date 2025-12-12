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

    private int worldHeight;
    private Random random;

    public SimpleLevel() {
        tiles = new ArrayList<Tile>();
        checkpoints = new ArrayList<CheckpointFlag>();
        spikes = new ArrayList<Spike>();
        coins = new ArrayList<Coin>();
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

        int platformWidth = 180;
        int platformHeight = 20;

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
                int flagX = platformX + platformWidth / 2 - 12;
                int flagY = platformY - 40;
                CheckpointFlag flag = new CheckpointFlag(flagX, flagY);
                checkpoints.add(flag);
            }

            // spikes on some platforms, but never on checkpoint ones
            if (!isCheckpointIndex && i % 3 == 2) {
                int spikeWidth = 24;
                int spikeHeight = 20;
                int spikeX = platformX + platformWidth / 2 - spikeWidth / 2;
                int spikeY = platformY - spikeHeight;
                Spike spike = new Spike(spikeX, spikeY, spikeWidth, spikeHeight);
                spikes.add(spike);
            }

            // coins on safe platforms (no checkpoint and no spike pattern)
            if (!isCheckpointIndex && i % 3 != 2) {
                int coinX = platformX + platformWidth / 2 - 8;
                int coinY = platformY - 20;
                Coin coin = new Coin(coinX, coinY, 1);
                coins.add(coin);
            }

            previousX = platformX;
            i = i + 1;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

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
