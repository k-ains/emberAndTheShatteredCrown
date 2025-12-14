package src.game;

public class Tutorial extends SimpleLevel {

    public Tutorial() {
        super();
        buildTower();
    }

    private void buildTower() {

        int gap = 120;
        int numberOfPlatforms = 25;
        int floorHeight = 60;

        worldHeight = GamePanel.HEIGHT + numberOfPlatforms * gap + floorHeight;

        int floorY = worldHeight - floorHeight;

        Tile floor = new Tile(0, floorY, GamePanel.WIDTH, floorHeight, true);
        tiles.add(floor);

        spawnX = 100;
        spawnY = worldHeight - 120;

        int platformWidth = 260;
        int platformHeight = 24;

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

            Tile platform = new Tile(platformX, platformY, platformWidth, platformHeight, true);
            tiles.add(platform);

            boolean isCheckpointIndex = (i == 4 || i == 9 || i == 14);
            boolean isLastPlatform = (i == numberOfPlatforms - 1);

            if (isCheckpointIndex) {
                int flagWidth = 32;
                int flagHeight = 48;

                int flagX = platformX + platformWidth / 2 - flagWidth / 2;
                int flagY = platformY - flagHeight;

                checkpoints.add(new CheckpointFlag(flagX, flagY));
            }

            boolean hasSpike = false;

            if (!isCheckpointIndex && !isLastPlatform && i % 3 == 2) {
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
                        spikes.add(new Spike(spikeX, spikeY, spikeWidth, spikeHeight));
                        used[laneIndex] = true;
                        s = s + 1;
                    }
                }

                hasSpike = true;
            }

            if (!isCheckpointIndex && !hasSpike && !isLastPlatform) {
                int coinSize = 24;
                int coinX = platformX + platformWidth / 2 - coinSize / 2;
                int coinY = platformY - coinSize - 4;
                coins.add(new Coin(coinX, coinY, 1));
            }

            if (!isCheckpointIndex && !hasSpike) {
                if (i == 0) {
                    messageBoxes.add(new MessageBox(
                        platformX + platformWidth / 2 - 12,
                        platformY - 70,
                        false,
                        "Use A/D or arrow keys to move."
                    ));
                }

                if (i == 1) {
                    messageBoxes.add(new MessageBox(
                        platformX + platformWidth / 2 - 12,
                        platformY - 70,
                        true,
                        "Watch out for spikes above!"
                    ));
                }

                if (i == 3) {
                    messageBoxes.add(new MessageBox(
                        platformX + platformWidth / 2 - 12,
                        platformY - 70,
                        true,
                        "Flags ahead are checkpoints. Touch to save progress!"
                    ));
                }

                if (i == 12) {
                    messageBoxes.add(new MessageBox(
                        platformX + platformWidth / 2 - 12,
                        platformY - 70,
                        true,
                        "Enemies ahead! Don't touch them."
                    ));
                }
            }

            boolean canHaveEnemy =
                !isCheckpointIndex && !hasSpike && !isLastPlatform && i >= 5 && i % 4 == 1;

            if (canHaveEnemy) {
                int enemyWidth = 40;
                int enemyHeight = 32;

                int enemyX = platformX + platformWidth / 2 - enemyWidth / 2;
                int enemyY = platformY - enemyHeight;

                int margin = 30;
                int enemyLeft = platformX + margin;
                int enemyRight = platformX + platformWidth - margin;

                enemies.add(new WalkingEnemy(enemyX, enemyY, enemyWidth, enemyHeight, enemyLeft, enemyRight, 2));
            }

            previousX = platformX;
            lastPlatformX = platformX;
            lastPlatformY = platformY;
            i = i + 1;
        }

        int doorWidth = 40;
        int doorHeight = 56;
        int doorX = lastPlatformX + platformWidth / 2 - doorWidth / 2;
        int doorY = lastPlatformY - doorHeight;

        doors.add(new Door(doorX, doorY, doorWidth, doorHeight, "TOWN"));
    }
}
