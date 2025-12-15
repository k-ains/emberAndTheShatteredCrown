package src.game;

import java.util.List;
import java.awt.image.BufferedImage;

public class Ice extends SimpleLevel {

    public Ice() {
        super();
        buildIce();
    }

    private void buildIce() {

        int gap = 120;
        int numberOfPlatforms = 40;
        int floorHeight = 60;

        worldHeight = GamePanel.HEIGHT + numberOfPlatforms * gap + floorHeight;

        int floorY = worldHeight - floorHeight;
        Tile floor = new Tile(0, floorY, GamePanel.WIDTH, floorHeight, true);
        tiles.add(floor);

        spawnX = 100;
        spawnY = worldHeight - 120;

        int platformWidth = 260;
        int platformHeight = 24;
        int TILE = 32;
        int tilesWide = platformWidth / TILE;

        int minXAllowed = 40;
        int maxXAllowed = GamePanel.WIDTH - platformWidth - 40;
        int maxHorizontalShift = 200;

        int previousX = (minXAllowed + maxXAllowed) / 2;
        int startY = floorY - 120;

        int lastPlatformX = previousX;
        int lastPlatformY = startY;

        int i = 0;
        while (i < numberOfPlatforms) {

            int platformY = startY - gap * i;

            int platformX = previousX;
            boolean valid = false;

            while (!valid) {
                int range = maxXAllowed - minXAllowed;
                int offset = 0;
                if (range > 0) {
                    offset = random.nextInt(range + 1);
                }

                int candidate = minXAllowed + offset;
                int diff = candidate - previousX;
                if (diff < 0) {
                    diff = -diff;
                }

                if (diff <= maxHorizontalShift) {
                    platformX = candidate;
                    valid = true;
                }
            }

            boolean isCheckpointIndex = (i == 4 || i == 9 || i == 14);
            boolean isLastPlatform = (i == numberOfPlatforms - 1);

            boolean makeSlope =
                !isCheckpointIndex &&
                !isLastPlatform &&
                i >= 3 &&
                i % 4 == 2;   // predictable tutorial spacing

            boolean hasSpike = false;

            // ===============================
            // PLATFORM / SLOPE
            // ===============================
            if (makeSlope) {
                boolean left = random.nextBoolean();
                int slopeX = platformX + platformWidth / 2 - 16;

                SlopeTile slope = new SlopeTile(slopeX, platformY, 32, 32, left);
                slope.setSprite(left ? Assets.iceSlopeLeft : Assets.iceSlopeRight);
                slope.makeleft(left);
                tiles.add(slope);
            } else {
                int t = 0;
                while (t < tilesWide) {

                    Tile tile;

                    if (!isCheckpointIndex && !hasSpike && random.nextInt(100) < 20) {
                        BufferedImage[] breakableSprites = new BufferedImage[] {
                            Assets.iceFloorBreakable[0],
                            Assets.iceFloorBreakable[1]
                        };
                        
                        tile = new BreakableTile(
                            platformX + t * TILE,
                            platformY,
                            TILE,
                            TILE,
                            breakableSprites
                        ); 
                    } else {
                    
                        tile = new Tile(
                            platformX + t * TILE,
                            platformY,
                            TILE,
                            TILE,
                            true
                        );

                        if (t == 0) {
                            tile.setSprite(Assets.iceFloorTopLeft);
                        } else if (t == tilesWide - 1) {
                            tile.setSprite(Assets.iceFloorTopRight);
                        } else {
                            tile.setSprite(Assets.iceFloorTopMid);
                        }

                        
                    }

                    tiles.add(tile);
                    t = t + 1;
                }
            }

            // ===============================
            // CHECKPOINT
            // ===============================
            if (isCheckpointIndex) {
                int flagW = 32;
                int flagH = 48;

                int flagX = platformX + platformWidth / 2 - flagW / 2;
                int flagY = platformY - flagH;

                checkpoints.add(new CheckpointFlag(flagX, flagY));
            }

            // ===============================
            // SPIKES (NORMAL PLATFORMS ONLY)
            // ===============================
            if (!makeSlope && !isCheckpointIndex && !isLastPlatform && i % 3 == 2) {

                int spikeW = 32;
                int spikeH = 28;
                int spikeY = platformY - spikeH;

                int margin = 40;

                int[] lanes = new int[] {
                    platformX + margin,
                    platformX + platformWidth / 2 - spikeW / 2,
                    platformX + platformWidth - margin - spikeW
                };

                boolean[] used = new boolean[3];

                int spikeCount = (random.nextInt(100) < 40) ? 2 : 1;

                int s = 0;
                while (s < spikeCount) {
                    int lane = random.nextInt(3);
                    if (!used[lane]) {
                        Spike spike = new Spike(lanes[lane], spikeY, spikeW, spikeH);
                        spike.setSprite(Assets.iceSpike);

                        spikes.add(spike);
                        used[lane] = true;
                        s = s + 1;
                    }
                }

                hasSpike = true;
            }

            // ===============================
            // COINS
            // ===============================
            if (!isCheckpointIndex && !hasSpike && !isLastPlatform) {
                int coinSize = 24;
                int cx = platformX + platformWidth / 2 - coinSize / 2;
                int cy = platformY - coinSize - 4;
                coins.add(new Coin(cx, cy, 1));
            }

            // ===============================
            // ENEMIES
            // ===============================
            boolean canHaveEnemy =
                !isCheckpointIndex &&
                !hasSpike &&
                !makeSlope &&
                !isLastPlatform &&
                i >= 5 &&
                i % 4 == 1;

            if (canHaveEnemy) {
                maybeAddGhostOnWide(platformX, platformY, platformWidth, i);
            }

            previousX = platformX;
            lastPlatformX = platformX;
            lastPlatformY = platformY;
            i = i + 1;
        }



        // ===============================
        // EXIT DOOR
        // ===============================
        int doorW = 40;
        int doorH = 56;

        doors.add(new Door(
            lastPlatformX + platformWidth / 2 - doorW / 2,
            lastPlatformY - doorH,
            doorW,
            doorH,
            "TOWN"
        ));
    }

    private void maybeAddGhostOnWide(int platformX, int platformY, int platformWidthPx, int index) {
        if (enemies == null) {
            return;
        }
        if (index < 2) {
            return;
        }


        int enemyW = 48;
        int enemyH = 32;

        int margin = 32;

        int leftBound = platformX + margin;
        int rightBound = platformX + platformWidthPx - margin - enemyW;

        if (rightBound <= leftBound) {
            return;
        }

        int lane = random.nextInt(3);
        int enemyX = leftBound;

        if (lane == 1) {
            enemyX = platformX + platformWidthPx / 2 - enemyW / 2;
        } else if (lane == 2) {
            enemyX = rightBound;
        }

        int enemyY = platformY - enemyH;
        enemies.add(new GhostEnemy(enemyX, enemyY, enemyW, enemyH, leftBound, rightBound, 2));
    }

}
