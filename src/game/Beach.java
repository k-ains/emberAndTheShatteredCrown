package src.game;

public class Beach extends SimpleLevel {

    private static final int TILE = 32;

    private static final int BASE_GAP = 140;
    private static final int PLATFORM_TILES = 9;
    private static final int MAX_SHIFT = 200;

    private static final int UMBRELLA_NEXT_BONUS = 190;
    private static final int UMBRELLA_BOUNCE_VEL = -30;
    private static final int STAR_ABOVE_UMBRELLA = 220;

    private static final int TREE_MIN_SEGMENTS = 10;
    private static final int TREE_EXTRA_SEGMENTS = 5;

    public Beach() {
        super();
        buildBeach();
    }

    private void buildBeach() {
        int cols = GamePanel.WIDTH / TILE;
        if (GamePanel.WIDTH % TILE != 0) {
            cols = cols + 1;
        }

        int platformWidthPx = PLATFORM_TILES * TILE;

        int platformCount = 26;
        int worstStep = BASE_GAP + UMBRELLA_NEXT_BONUS;
        worldHeight = GamePanel.HEIGHT + platformCount * worstStep + 700;

        int floorTopY = worldHeight - (TILE * 2);
        buildBeachFloor(cols, floorTopY);

        spawnX = 80;
        spawnY = floorTopY - 32;

        int minXAllowed = 40;
        int maxXAllowed = GamePanel.WIDTH - platformWidthPx - 40;
        if (maxXAllowed < minXAllowed) {
            maxXAllowed = minXAllowed;
        }

        int previousX = (minXAllowed + maxXAllowed) / 2;
        int currentY = floorTopY - 120;

        int starsPlaced = 0;
        int heartsPlaced = 0;

        // =========================================================
        // INTRO SEQUENCE (guaranteed readable):
        // NORMAL (jump info) -> NORMAL (umbrella info) -> UMBRELLA
        // -> NORMAL (tree info) -> TREE
        // =========================================================

        int p0X = previousX;
        addNormalPlatformWide(p0X, currentY, PLATFORM_TILES, true, 30);
        messageBoxes.add(new MessageBox(p0X + 4 * TILE, currentY - 3 * TILE, true, "Beach: UP/W or SPACE jumps."));
        currentY = currentY - BASE_GAP;

        int p1X = pickPlatformX(p0X, minXAllowed, maxXAllowed);
        addNormalPlatformWide(p1X, currentY, PLATFORM_TILES, true, 30);
        messageBoxes.add(new MessageBox(p1X + 4 * TILE, currentY - 3 * TILE, true, "Next: Umbrellas bounce higher."));
        currentY = currentY - BASE_GAP;

        int p2X = pickPlatformX(p1X, minXAllowed, maxXAllowed);
        addUmbrellaPlatformWide(p2X, currentY, PLATFORM_TILES);

        if (stars != null && starsPlaced < 3) {
            stars.add(new Star(p2X + platformWidthPx / 2 - 14, currentY - STAR_ABOVE_UMBRELLA));
            starsPlaced = starsPlaced + 1;
        }

        // plants ONLY on umbrellas
        if (plants != null) {
            if (random.nextInt(100) < 55) {
                plants.add(new Plant(p2X + platformWidthPx / 2 - TILE / 2, currentY - TILE));
            }
        }

        addCoinOnWide(p2X, currentY, platformWidthPx);

        currentY = currentY - (BASE_GAP + UMBRELLA_NEXT_BONUS);

        int p3X = pickPlatformX(p2X, minXAllowed, maxXAllowed);
        addNormalPlatformWide(p3X, currentY, PLATFORM_TILES, true, 30);
        messageBoxes.add(new MessageBox(p3X + 4 * TILE, currentY - 3 * TILE, true, "Next: Trees must be climbed (UP/W, DOWN/S)."));
        currentY = currentY - BASE_GAP;

        int p4X = pickPlatformX(p3X, minXAllowed, maxXAllowed);
        addNormalPlatformWide(p4X, currentY, PLATFORM_TILES, false, 0); // tree, no spikes

        int treeBaseX = p4X + platformWidthPx / 2 - (3 * TILE) / 2;
        int topY = addTree(treeBaseX, currentY + TILE);

        addCoinOn3Wide(treeBaseX, topY);

        currentY = topY - BASE_GAP;
        previousX = p4X;

        // =========================================================
        // REST OF LEVEL (NO RANDOM MESSAGE BOX SPAM)
        // =========================================================
        // =========================================================
        // REST OF LEVEL (NO RANDOM MESSAGE BOX SPAM)
        // Checkpoints: NEVER on umbrellas, NEVER with enemies
        // =========================================================
        int i = 5;
        while (i < platformCount) {
            int platformX = pickPlatformX(previousX, minXAllowed, maxXAllowed);

            boolean wantsCheckpoint = (i == 9 || i == 16 || i == 23); // checkpoints only on NORMAL platforms

            boolean makeTree = (i % 7 == 4);
            boolean makeUmbrella = (i % 5 == 2);

            // If this index is a checkpoint, force it to be a NORMAL platform
            if (wantsCheckpoint) {
                makeTree = false;
                makeUmbrella = false;
            }

            if (makeTree) {
                makeUmbrella = false;
            }

            if (makeTree) {
                int baseX = platformX + platformWidthPx / 2 - (3 * TILE) / 2;
                int tTopY = addTree(baseX, currentY + TILE);

                // NO checkpoints here (keeps checkpoints predictable + avoids tree issues)
                // NO enemies on trees already

                addCoinOn3Wide(baseX, tTopY);

                currentY = tTopY - BASE_GAP;
            } else if (makeUmbrella) {
                addUmbrellaPlatformWide(platformX, currentY, PLATFORM_TILES);

                // plants ONLY on umbrellas
                if (plants != null) {
                    if (random.nextInt(100) < 55) {
                        plants.add(new Plant(platformX + platformWidthPx / 2 - TILE / 2, currentY - TILE));
                    }
                }

                if (stars != null && starsPlaced < 3) {
                    stars.add(new Star(platformX + platformWidthPx / 2 - 14, currentY - STAR_ABOVE_UMBRELLA));
                    starsPlaced = starsPlaced + 1;
                }

                addCoinOnWide(platformX, currentY, platformWidthPx);

                // ✅ umbrellas never get checkpoints
                // ✅ enemies are allowed here
                maybeAddCrabOnWide(platformX, currentY, platformWidthPx, i);

                currentY = currentY - (BASE_GAP + UMBRELLA_NEXT_BONUS);

                // After umbrella, next platform gets fewer spikes
                if (i + 1 < platformCount) {
                    int nextX = pickPlatformX(platformX, minXAllowed, maxXAllowed);
                    addNormalPlatformWide(nextX, currentY, PLATFORM_TILES, true, 10);
                    previousX = nextX;
                    i++;
                    continue;
                }
            } else {
                // NORMAL platform
                boolean allowSpikes = !wantsCheckpoint;
                int spikeChance = 30;
                // If previous was tree or umbrella, lower spike chance
                if (i > 0 && (platformX == previousX)) {
                    spikeChance = 10;
                }
                addNormalPlatformWide(platformX, currentY, PLATFORM_TILES, allowSpikes, spikeChance);

                if (heartBoxes != null) {
                    if (i >= 10 && heartsPlaced < 2) {
                        if (random.nextInt(100) < 30) {
                            heartBoxes.add(new HeartBox(platformX + platformWidthPx / 2 - 16, currentY - 70));
                            heartsPlaced = heartsPlaced + 1;
                        }
                    }
                }

                if (wantsCheckpoint) {
                    addCheckpointOnWide(platformX, currentY, platformWidthPx);
                    // ✅ no enemies on checkpoint platforms
                } else {
                    maybeAddCrabOnWide(platformX, currentY, platformWidthPx, i);
                }

                addCoinOnWide(platformX, currentY, platformWidthPx);

                currentY = currentY - BASE_GAP;
            }

            previousX = platformX;
            i = i + 1;
        }


        // ensure 3 stars
        if (stars != null) {
            while (starsPlaced < 3) {
                int starX = 260 + starsPlaced * 80;
                int starY = floorTopY - 360 - starsPlaced * 260;
                stars.add(new Star(starX, starY));
                starsPlaced = starsPlaced + 1;
            }
        }

        // END PLATFORM + DOOR BACK TO TOWN (readable on a normal platform)
        addNormalPlatformWide(previousX, currentY, PLATFORM_TILES, true, 30);

        int doorW = 40;
        int doorH = 56;
        int doorX = previousX + platformWidthPx / 2 - doorW / 2;
        int doorY = currentY - doorH;

        doors.add(new Door(doorX, doorY, doorW, doorH, "TOWN"));
        messageBoxes.add(new MessageBox(previousX + 4 * TILE, currentY - 3 * TILE, true, "Press E to return to town."));
    }

    private int pickPlatformX(int previousX, int minXAllowed, int maxXAllowed) {
        int platformX = previousX;

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

            if (diff <= MAX_SHIFT) {
                platformX = candidateX;
                valid = true;
            }
        }

        int result = platformX;
        return result;
    }

    private void buildBeachFloor(int cols, int floorTopY) {
        int c = 0;
        while (c < cols) {
            int x = c * TILE;

            Tile top = new Tile(x, floorTopY, TILE, TILE, true);
            if (c == 0) {
                top.setSprite(Assets.beachFloorTopLeft);
            } else if (c == cols - 1) {
                top.setSprite(Assets.beachFloorTopRight);
            } else {
                top.setSprite(Assets.beachFloorTopMid);
            }
            tiles.add(top);

            Tile bot = new Tile(x, floorTopY + TILE, TILE, TILE, true);
            if (c == 0) {
                bot.setSprite(Assets.beachFloorBottomLeft);
            } else if (c == cols - 1) {
                bot.setSprite(Assets.beachFloorBottomRight);
            } else {
                bot.setSprite(Assets.beachFloor);
            }
            tiles.add(bot);

            c = c + 1;
        }
    }

    private void addNormalPlatformWide(int x, int y, int tilesWide, boolean allowSpikes, int spikeChancePercent) {
        int i = 0;
        int[] spikeSlots = new int[tilesWide];
        if (allowSpikes) {
            java.util.List<Integer> slots = new java.util.ArrayList<>();
            for (int s = 0; s < tilesWide; s++) slots.add(s);
            java.util.Collections.shuffle(slots, random);
            for (int s = 0; s < 2 && s < slots.size(); s++) spikeSlots[slots.get(s)] = 1;
        }
        while (i < tilesWide) {
            Tile t = new Tile(x + i * TILE, y, TILE, TILE, true);
            if (i == 0) {
                t.setSprite(Assets.beachFloorTopLeft);
            } else if (i == tilesWide - 1) {
                t.setSprite(Assets.beachFloorTopRight);
            } else {
                t.setSprite(Assets.beachFloorTopMid);
            }
            tiles.add(t);
            // --- Add coralSpikes and seaGrass spikes, max 2 per platform, only if allowed and not under message boxes ---
            if (allowSpikes && spikes != null && spikeSlots[i] == 1) {
                int spikeX = x + i * TILE;
                int spikeY = y - 24;
                boolean overlapsMsg = false;
                if (messageBoxes != null) {
                    for (MessageBox box : messageBoxes) {
                        // Check if the spike would overlap the message box horizontally and vertically
                        int boxX = box.getX();
                        int boxY = box.getY();
                        int boxW = box.getWidth();
                        int boxH = box.getHeight();
                        if (spikeX + TILE > boxX && spikeX < boxX + boxW && spikeY + 24 > boxY && spikeY < boxY + boxH) {
                            overlapsMsg = true;
                            break;
                        }
                    }
                }
                if (!overlapsMsg && random.nextInt(100) < spikeChancePercent) {
                    int spikeRoll = random.nextInt(100);
                    Spike s = new Spike(spikeX, spikeY, TILE, 24);
                    if (spikeRoll < 50) {
                        s.setSprite(Assets.coralSpike);
                    } else {
                        s.setSprite(Assets.seagrassSpike);
                    }
                    spikes.add(s);
                }
            }
            i = i + 1;
        }
    }

    private void addUmbrellaPlatformWide(int x, int y, int tilesWide) {
        int i = 0;
        while (i < tilesWide) {
            BounceTile t = new BounceTile(x + i * TILE, y, TILE, TILE, true, UMBRELLA_BOUNCE_VEL);

            if (i == 0) {
                t.setSprite(Assets.umbrellaLeft);
            } else if (i == tilesWide - 1) {
                t.setSprite(Assets.umbrellaRight);
            } else {
                t.setSprite(Assets.umbrellaMid);
            }

            tiles.add(t);
            i = i + 1;
        }
    }

    private int addTree(int x, int groundTopY) {
        int groundY = groundTopY - TILE;

        Tile gL = new Tile(x + 0 * TILE, groundY, TILE, TILE, true);
        gL.setSprite(Assets.treeGroundLeft);
        tiles.add(gL);

        Tile gM = new Tile(x + 1 * TILE, groundY, TILE, TILE, true);
        gM.setSprite(Assets.treeGroundMid);
        tiles.add(gM);

        Tile gR = new Tile(x + 2 * TILE, groundY, TILE, TILE, true);
        gR.setSprite(Assets.treeGroundRight);
        tiles.add(gR);

        int trunkX = x + TILE;

        int segments = TREE_MIN_SEGMENTS + random.nextInt(TREE_EXTRA_SEGMENTS);
        int s = 0;
        while (s < segments) {
            ClimbTile trunk = new ClimbTile(trunkX, groundY - TILE - (TILE * s), TILE, TILE);
            trunk.setSprite(Assets.treeTrunk);
            tiles.add(trunk);
            s = s + 1;
        }

        int topY = groundY - TILE - (TILE * segments);

        Tile tL = new Tile(trunkX - TILE, topY, TILE, TILE, true);
        tL.setSprite(Assets.treeLeft);
        tiles.add(tL);

        Tile tM = new Tile(trunkX, topY, TILE, TILE, true);
        tM.setSprite(Assets.treeMid);
        tiles.add(tM);

        Tile tR = new Tile(trunkX + TILE, topY, TILE, TILE, true);
        tR.setSprite(Assets.treeRight);
        tiles.add(tR);

        int result = topY;
        return result;
    }

    private void addCheckpointOnWide(int platformX, int platformY, int platformWidthPx) {
        int flagW = 32;
        int flagH = 48;

        int flagX = platformX + platformWidthPx / 2 - flagW / 2;
        int flagY = platformY - flagH;

        checkpoints.add(new CheckpointFlag(flagX, flagY));
    }

    private void addCheckpointOn3Wide(int treeX, int topY) {
        int flagW = 32;
        int flagH = 48;

        int flagX = treeX + (3 * TILE) / 2 - flagW / 2;
        int flagY = topY - flagH;

        checkpoints.add(new CheckpointFlag(flagX, flagY));
    }

    private void addCoinOnWide(int platformX, int platformY, int platformWidthPx) {
        if (coins == null) {
            return;
        }

        int coinSize = 24;
        int lane = random.nextInt(3);

        int cx = platformX + 40;
        if (lane == 1) {
            cx = platformX + platformWidthPx / 2 - coinSize / 2;
        } else if (lane == 2) {
            cx = platformX + platformWidthPx - 40 - coinSize;
        }

        int cy = platformY - coinSize - 6;
        coins.add(new Coin(cx, cy, 1));
    }

    private void addCoinOn3Wide(int treeX, int topY) {
        if (coins == null) {
            return;
        }
        int coinSize = 24;
        int cx = treeX + (3 * TILE) / 2 - coinSize / 2;
        int cy = topY - coinSize - 6;
        coins.add(new Coin(cx, cy, 1));
    }

    private void maybeAddCrabOnWide(int platformX, int platformY, int platformWidthPx, int index) {
        if (enemies == null) {
            return;
        }
        if (index < 6) {
            return;
        }

        int roll = random.nextInt(100);
        if (roll >= 45) {
            return;
        }

        int enemyW = 48;
        int enemyH = 32;

        int margin = 32;

        int leftBound = platformX; // walk to true left edge
        int rightBound = platformX + platformWidthPx - enemyW; // walk to true right edge

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
        CrabEnemy crab = new CrabEnemy(enemyX, enemyY, enemyW, enemyH, leftBound, rightBound, 2);
        crab.setCirclePlatform(true); // Enable circling
        enemies.add(crab);
    }
}
