package src.game;

import java.awt.image.BufferedImage;

public class Candy extends SimpleLevel {

    public Candy() {
        super();
        buildCandy();
    }

    private void buildCandy() {

        int gap = 120;
        int numberOfPlatforms = 25;
        int floorHeight = 60;
        int TILE = 32;

        worldHeight = GamePanel.HEIGHT + numberOfPlatforms * gap + floorHeight;

        int floorY = worldHeight - floorHeight;
        buildCandyFloor(floorY);

        spawnX = 100;
        spawnY = worldHeight - 120;

        int platformWidth = 260;
        int platformHeight = 24;
        int tilesWide = platformWidth / TILE;

        int minXAllowed = 40;
        int maxXAllowed = GamePanel.WIDTH - platformWidth - 40;
        int maxHorizontalShift = 200;

        int previousX = (minXAllowed + maxXAllowed) / 2;
        int startY = floorY - 120;

        int lastPlatformX = previousX;
        int lastPlatformY = startY;

        // =========================================================
        // INTRO SEQUENCE (tutorial messages for candy level)
        // =========================================================
        int p0X = previousX;
        addNormalPlatformWide(p0X, startY, tilesWide);
        messageBoxes.add(new MessageBox(p0X + 4 * TILE, startY - 70, true, "Candy Land: Watch for floating ghosts!"));
        
        int p1Y = startY - gap;
        int p1X = pickPlatformX(p0X, minXAllowed, maxXAllowed);
        addNormalPlatformWide(p1X, p1Y, tilesWide);
        messageBoxes.add(new MessageBox(p1X + 4 * TILE, p1Y - 70, true, "Ghosts phase through platforms!"));

        previousX = p1X;
        int currentY = p1Y;

        int i = 2;
        while (i < numberOfPlatforms) {
            int platformY = currentY - gap;

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

            // ===============================
            // PLATFORM (with random breakable tiles)
            // ===============================
            boolean makeBreakable = !isCheckpointIndex && !isLastPlatform && i >= 2;
            int breakableCount = makeBreakable ? 2 : 0; // always 2 breakable tiles
            
            int t = 0;
            while (t < tilesWide) {
                boolean isBreakable = false;
                
                if (makeBreakable && breakableCount > 0 && t >= 2 && t < tilesWide - 2) {
                    if (random.nextInt(100) < 60) {
                        isBreakable = true;
                        breakableCount = breakableCount - 1;
                    }
                }
                
                Tile tile;
                
                if (isBreakable) {
                    BufferedImage[] breakSprites = new BufferedImage[2];
                    breakSprites[0] = Assets.candyBreakable;
                    breakSprites[1] = Assets.candyBreakable; // same for now
                    tile = new BreakableTile(
                        platformX + t * TILE,
                        platformY,
                        TILE,
                        TILE,
                        breakSprites
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
                        tile.setSprite(Assets.candyRoofLeft);
                    } else if (t == tilesWide - 1) {
                        tile.setSprite(Assets.candyRoofRight);
                    } else {
                        tile.setSprite(Assets.candyRoofMid);
                    }
                }

                tiles.add(tile);
                t = t + 1;
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
            boolean hasSpike = false;

            if (!isCheckpointIndex && !isLastPlatform && i % 3 == 2) {

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
                        spike.setSprite(Assets.spike);

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
            // SPINNING HAZARDS
            // ===============================
            boolean canHaveSpin = !isCheckpointIndex && !hasSpike && !isLastPlatform && i % 5 == 3;
            if (canHaveSpin) {
                int spinSize = 40;
                int spinX = platformX + platformWidth / 2 - spinSize / 2;
                int spinY = platformY - spinSize - 20;
                
                SpinningHazard spin = new SpinningHazard(spinX, spinY, spinSize, spinSize);
                spin.setSprite(Assets.candySpin);
                spinningHazards.add(spin);
            }

            // ===============================
            // POP TILES (some platforms disappear/reappear)
            // ===============================
            boolean makePopTile = !isCheckpointIndex && !hasSpike && !isLastPlatform && i % 6 == 4;
            
            // ===============================
            // ENEMIES
            // ===============================
            boolean canHaveEnemy =
                !isCheckpointIndex &&
                !hasSpike &&
                !isLastPlatform &&
                !canHaveSpin &&
                i >= 3;

            if (canHaveEnemy) {
                maybeAddGhostOnWide(platformX, platformY, platformWidth, i);
            }

            previousX = platformX;
            lastPlatformX = platformX;
            lastPlatformY = platformY;
            currentY = platformY;
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

    private void buildCandyFloor(int floorY) {
        int TILE_SIZE = 64;
        int cols = GamePanel.WIDTH / TILE_SIZE;
        if (GamePanel.WIDTH % TILE_SIZE != 0) {
            cols = cols + 1;
        }

        int rows = 1;
        int r = 0;
        while (r < rows) {
            int c = 0;
            while (c < cols) {
                int x = c * TILE_SIZE;
                int y = floorY + r * TILE_SIZE;

                Tile tile = new Tile(x, y, TILE_SIZE, TILE_SIZE, true);
                tile.setSprite(Assets.candyWall);
                tiles.add(tile);

                c = c + 1;
            }
            r = r + 1;
        }
    }

    private int pickPlatformX(int previousX, int minXAllowed, int maxXAllowed) {
        int platformX = previousX;
        int maxShift = 200;

        boolean valid = false;
        while (!valid) {
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

            if (diff <= maxShift) {
                platformX = candidateX;
                valid = true;
            }
        }

        int result = platformX;
        return result;
    }

    private void addNormalPlatformWide(int x, int y, int tilesWide) {
        int TILE = 32;
        int i = 0;
        while (i < tilesWide) {
            Tile t = new Tile(x + i * TILE, y, TILE, TILE, true);

            if (i == 0) {
                t.setSprite(Assets.candyRoofLeft);
            } else if (i == tilesWide - 1) {
                t.setSprite(Assets.candyRoofRight);
            } else {
                t.setSprite(Assets.candyRoofMid);
            }

            tiles.add(t);
            i = i + 1;
        }
    }

    private void maybeAddGhostOnWide(int platformX, int platformY, int platformWidthPx, int index) {
        if (enemies == null) {
            return;
        }
        if (index < 3) {
            return;
        }

        int roll = random.nextInt(100);
        if (roll >= 50) {
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
        enemies.add(new GhostEnemy(enemyX, enemyY, enemyW, enemyH, leftBound, rightBound, 2, Assets.waspIdle));
    }
}
