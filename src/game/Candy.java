package src.game;

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

            // ===============================
            // PLATFORM
            // ===============================
            int t = 0;
            while (t < tilesWide) {

                Tile tile = new Tile(
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
            // ENEMIES
            // ===============================
            boolean canHaveEnemy =
                !isCheckpointIndex &&
                !hasSpike &&
                !isLastPlatform &&
                i >= 5 &&
                i % 4 == 1;

            if (canHaveEnemy) {
                int ew = 40;
                int eh = 32;

                int ex = platformX + platformWidth / 2 - ew / 2;
                int ey = platformY - eh;

                int margin = 30;
                enemies.add(new WalkingEnemy(
                    ex, ey, ew, eh,
                    platformX + margin,
                    platformX + platformWidth - margin,
                    2
                ));
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
}
