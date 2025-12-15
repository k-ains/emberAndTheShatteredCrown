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
        int startY = floorY - 120;        int lastPlatformX = previousX;
        int lastPlatformY = startY;
        
        int starsPlaced = 0;
        int heartsPlaced = 0;

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
            }            boolean isCheckpointIndex = (i == 4 || i == 9 || i == 14);
            boolean isLastPlatform = (i == numberOfPlatforms - 1);

            // Special handling for tutorial platforms (0-5)
            boolean makeSlope = false;
            boolean forceBreakable = false;
            boolean preventBreakable = false;
            boolean preventSpike = false;
            boolean preventEnemy = false;
            boolean forceEnemy = false;
            boolean addMessageBox = false;
            String messageText = "";
            
            if (i == 1) {
                // Platform 1: Safe with message box
                preventBreakable = true;
                preventSpike = true;
                preventEnemy = true;
                addMessageBox = true;
                messageText = "Careful! Some ice tiles break when you land on them!";
            } else if (i == 2) {
                // Platform 2: Has breakable tile
                forceBreakable = true;
                preventSpike = true;
                preventEnemy = true;
            } else if (i == 3) {
                // Platform 3: Safe with message box
                preventBreakable = true;
                preventSpike = true;
                preventEnemy = true;
                addMessageBox = true;
                messageText = "Next: Slopes launch you! Hold direction to control.";
            } else if (i == 4) {
                // Platform 4: Slope (override checkpoint)
                makeSlope = true;
                preventBreakable = true;
                preventSpike = true;
                preventEnemy = true;            } else if (i == 5) {
                // Platform 5: Safe platform with message box warning about ghost
                preventBreakable = true;
                preventSpike = true;
                preventEnemy = true;
                addMessageBox = true;
                messageText = "Warning: Ghosts ahead! They follow you!";
            } else if (i == 6) {
                // Platform 6: Ghost enemy
                preventBreakable = true;
                preventSpike = true;
                forceEnemy = true;
            } else if (i > 6) {
                // Random pattern for platforms after 6
                makeSlope = !isCheckpointIndex &&
                           !isLastPlatform &&
                           i >= 7 &&
                           i % 4 == 2;
            }

            boolean hasSpike = false;
            boolean hasBreakable = false;

            // ===============================
            // PLATFORM / SLOPE
            // ===============================
            if (makeSlope) {
                boolean left = random.nextBoolean();
                int slopeX = platformX + platformWidth / 2 - 16;

                SlopeTile slope = new SlopeTile(slopeX, platformY, 32, 32, left);
                slope.setSprite(left ? Assets.iceSlopeLeft : Assets.iceSlopeRight);
                slope.makeleft(left);
                tiles.add(slope);            } else {
                int t = 0;
                while (t < tilesWide) {

                    Tile tile;

                    // Check if this platform should have breakable tiles
                    boolean shouldBreak = false;
                    if (forceBreakable && t == tilesWide / 2) {
                        // Force breakable on specific platform (middle tile)
                        shouldBreak = true;
                    } else if (!preventBreakable && !isCheckpointIndex && !hasSpike && i > 6 && random.nextInt(100) < 20) {
                        // Random breakable for platforms after tutorial
                        shouldBreak = true;
                    }

                    if (shouldBreak) {
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
                        hasBreakable = true;
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
            }            // ===============================
            // CHECKPOINT
            // ===============================
            if (isCheckpointIndex && i != 4) {
                int flagW = 32;
                int flagH = 48;

                int flagX = platformX + platformWidth / 2 - flagW / 2;
                int flagY = platformY - flagH;

                checkpoints.add(new CheckpointFlag(flagX, flagY));
            }            // ===============================
            // SPIKES (NORMAL PLATFORMS ONLY)
            // ===============================
            if (!preventSpike && !makeSlope && !isCheckpointIndex && !isLastPlatform && i > 6 && i % 3 == 2) {

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
            // STARS (place 3 throughout level)
            // ===============================
            if (stars != null && starsPlaced < 3) {
                // Place stars on safe platforms spaced throughout the level
                boolean placeStar = false;
                if (i == 10 && starsPlaced == 0) placeStar = true;
                if (i == 20 && starsPlaced == 1) placeStar = true;
                if (i == 32 && starsPlaced == 2) placeStar = true;
                
                if (placeStar && !hasSpike && !isCheckpointIndex && !makeSlope) {
                    stars.add(new Star(platformX + platformWidth / 2 - 14, platformY - 150));
                    starsPlaced++;
                }
            }
            
            // ===============================
            // HEARTS (place 2 throughout level)
            // ===============================
            if (heartBoxes != null && heartsPlaced < 2) {
                boolean placeHeart = false;
                if (i == 12 && heartsPlaced == 0) placeHeart = true;
                if (i == 25 && heartsPlaced == 1) placeHeart = true;
                
                if (placeHeart && !hasSpike && !isCheckpointIndex) {
                    heartBoxes.add(new HeartBox(platformX + platformWidth / 2 - 16, platformY - 70));
                    heartsPlaced++;
                }
            }            // ===============================
            // ENEMIES
            // ===============================
            boolean canHaveEnemy = false;
            
            if (forceEnemy) {
                // Force enemy on platform 6
                canHaveEnemy = true;
            } else if (!preventEnemy && i > 6) {
                // Random enemies after tutorial
                canHaveEnemy = !isCheckpointIndex &&
                              !hasSpike &&
                              !makeSlope &&
                              !isLastPlatform &&
                              i >= 7 &&
                              i % 4 == 1;
            }

            if (canHaveEnemy) {
                maybeAddGhostOnWide(platformX, platformY, platformWidth, i);
            }
            
            // ===============================
            // MESSAGE BOXES
            // ===============================
            if (addMessageBox && messageText.length() > 0) {
                messageBoxes.add(new MessageBox(platformX + 4 * TILE, platformY - 3 * TILE, true, messageText));
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
        }        int enemyY = platformY - enemyH;
        GhostEnemy ghost = new GhostEnemy(enemyX, enemyY, enemyW, enemyH, leftBound, rightBound, 2);
        ghost.setFollowPlayer(true); // Enable player following
        enemies.add(ghost);
    }

}
