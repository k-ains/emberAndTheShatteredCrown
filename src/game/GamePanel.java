package src.game;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    public static final int WIDTH = 960;
    public static final int HEIGHT = 540;

    private Thread gameThread;
    private boolean running;

    private Player player;
    private SimpleLevel level;

    private int cameraY;

    private int bestPlatformCount;
    private int bestPlatformLineY;

    private int runMaxPlatformCount;
    private int runBestY;
    private int currentPlatformCount;

    private static final int MODE_TUTORIAL = 0;
    private static final int MODE_TOWN = 1;
    private static final int MODE_BEACH = 2;
    private static final int MODE_ICE = 3;
    private static final int MODE_CANDY = 4;

    private int gameMode;
    private boolean[] mapUnlocked;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);

        Assets.init();

        mapUnlocked = new boolean[5];
        int m = 0;
        while (m < 5) {
            mapUnlocked[m] = false;
            m = m + 1;
        }
        //for debugging only
        gameMode = MODE_BEACH;
        level = new Beach();
        // gameMode = MODE_TUTORIAL;
        // level = new Tutorial();

        // gameMode = MODE_ICE;
        // level = new Ice();

        int startX = level.getSpawnX();
        int startY = level.getSpawnY();
        player = new Player(startX, startY);

        cameraY = computeCameraY();
        bestPlatformCount = 0;
        bestPlatformLineY = player.getY();

        runMaxPlatformCount = 0;
        runBestY = player.getY();
        currentPlatformCount = 0;
        Sound.playMusic("/src/assets/sounds/background.wav");
    }

    public void startGameLoop() {
        if (running) {
            return;
        }
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        final int fps = 60;
        final double nsPerUpdate = 1_000_000_000.0 / fps;
        double delta = 0.0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta = delta + (now - lastTime) / nsPerUpdate;
            lastTime = now;

            while (delta >= 1.0) {
                updateGame();
                repaint();
                delta = delta - 1.0;
            }
        }
    }

    private void updateGame() {
        player.update(level);
        level.update();

        if (gameMode == MODE_TUTORIAL) {
            int currentPlatforms = level.getPlatformsReachedCount(player.getY());
            currentPlatformCount = currentPlatforms;

            if (currentPlatforms > runMaxPlatformCount) {
                runMaxPlatformCount = currentPlatforms;
                runBestY = player.getY();
            }
        } else {
            currentPlatformCount = 0;
        }

        player.updateMessageTimer();

        /// ----- CAMERA BEHAVIOR -----
        if (gameMode == MODE_TOWN) {
            cameraY = 0;
        } else {
            // TOWER RULE: camera only moves UP (never down)
            int desiredCameraY = player.getY() - HEIGHT / 2;

            if (desiredCameraY < cameraY) {
                cameraY = desiredCameraY;
            }
            if (cameraY < 0) {
                cameraY = 0;
            }

            int maxCam = level.getWorldHeight() - HEIGHT;
            if (maxCam < 0) {
                maxCam = 0;
            }
            if (cameraY > maxCam) {
                cameraY = maxCam;
            }
        }


        // FALLING DEATH:
        // For tower style levels only. Beach still uses it, but camera MUST be correct.
        if (gameMode != MODE_TOWN) {
            int visibleBottom = cameraY + HEIGHT + 40;
            if (player.getY() > visibleBottom) {
                player.markDead();
            }
        }

        if (player.isDead()) {
            if (gameMode == MODE_TUTORIAL) {
                if (runMaxPlatformCount > bestPlatformCount) {
                    bestPlatformCount = runMaxPlatformCount;
                    bestPlatformLineY = runBestY;
                }
                runMaxPlatformCount = 0;
                runBestY = player.getY();
            }

            player.onDeath(level);

            // IMPORTANT:
            // After respawn on tall maps (beach), camera must jump to player.
            if (gameMode == MODE_TOWN) {
                cameraY = 0;
            } else {
                cameraY = computeCameraY();
            }
        }
    }

    private int computeCameraY() {
        int desired = player.getY() - HEIGHT / 2;
        if (desired < 0) {
            desired = 0;
        }

        int maxCam = level.getWorldHeight() - HEIGHT;
        if (maxCam < 0) {
            maxCam = 0;
        }

        if (desired > maxCam) {
            desired = maxCam;
        }

        int result = desired;
        return result;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

       // background per mode
if (gameMode == MODE_TOWN && Assets.backgroundTown != null) {
    g.drawImage(Assets.backgroundTown, 0, 0, WIDTH, HEIGHT, null);
} else if (gameMode == MODE_BEACH && Assets.backgroundBeach != null) {
    g.drawImage(Assets.backgroundBeach, 0, 0, WIDTH, HEIGHT, null);
} else if (gameMode == MODE_ICE && Assets.backgroundIce != null) {
    g.drawImage(Assets.backgroundIce, 0, 0, WIDTH, HEIGHT, null);
} else {
    if (Assets.backgroundTower != null) {
        g.drawImage(Assets.backgroundTower, 0, 0, WIDTH, HEIGHT, null);
    } else {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }
}


        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        g2.translate(0, -cameraY);

        level.draw(g2);
        player.draw(g2);

        if (gameMode == MODE_TUTORIAL && bestPlatformCount > 0) {
            g2.setColor(Color.MAGENTA);
            g2.drawLine(0, bestPlatformLineY, WIDTH, bestPlatformLineY);
            g2.drawString("Best (" + bestPlatformCount + ")", 10, bestPlatformLineY - 4);
        }

        g2.translate(0, cameraY);

        drawHUD(g);
    }

    private void drawHUD(Graphics g) {
        int hearts = player.getHearts();
        int maxHearts = player.getMaxHearts();

        int heartSize = 24;
        int heartSpacing = 28;
        int heartXStart = 10;
        int heartY = 24;

        int i = 0;
        while (i < maxHearts) {
            int x = heartXStart + i * heartSpacing;
            int y = heartY - heartSize + 4;

            if (i < hearts && Assets.heart != null) {
                g.drawImage(Assets.heart, x, y, heartSize, heartSize, null);
            } else {
                if (i < hearts) {
                    g.setColor(java.awt.Color.RED);
                } else {
                    g.setColor(java.awt.Color.DARK_GRAY);
                }
                g.fillOval(x, y, heartSize, heartSize);
            }

            i = i + 1;
        }

        int hudStartY = heartY + 10;

        int coinIconSize = 20;
        int coinIconX = 10;
        int coinIconY = hudStartY + coinIconSize;

        if (Assets.coinGold != null && Assets.coinGold.length > 0) {
            long time = System.currentTimeMillis();
            int hudFrame = (int) ((time / 100) % Assets.coinGold.length);
            java.awt.image.BufferedImage hudImg = Assets.coinGold[hudFrame];
            g.drawImage(hudImg, coinIconX, coinIconY - coinIconSize, coinIconSize, coinIconSize, null);
        } else {
            g.setColor(java.awt.Color.YELLOW);
            g.fillOval(coinIconX, coinIconY - coinIconSize, coinIconSize, coinIconSize);
        }

        g.setColor(java.awt.Color.WHITE);
        g.drawString("x " + player.getCoins(), coinIconX + coinIconSize + 6, coinIconY - 5);

        // stars counter
        g.drawString("Stars: " + player.getStarsCollected() + " / 3", 10, coinIconY + 18);

        int textY = coinIconY + 40;

        if (gameMode == MODE_TUTORIAL) {
            g.drawString("Current Platforms: " + currentPlatformCount, 10, textY);
            g.drawString("Best Platforms: " + bestPlatformCount, 10, textY + 18);
        }

        String msg = player.getPopupMessage();
        if (player.getPopupTimer() > 0 && msg != null && msg.length() > 0) {
            g.setColor(java.awt.Color.WHITE);
            g.drawString(msg, 10, HEIGHT - 20);
        }
    }

 private void switchToTown() {
    level = new Town();
    gameMode = MODE_TOWN;

    player.respawnForNewLevel(level.getSpawnX(), level.getSpawnY());

    // refill hearts in town
    player.healToFull();

    cameraY = 0;
    player.showMessage("Back to town.");
}


    private void switchToBeach() {
        level = new Beach();
        gameMode = MODE_BEACH;
        player.respawnForNewLevel(level.getSpawnX(), level.getSpawnY());
        cameraY = computeCameraY();
    }

    private void switchToIce() {
        level = new Ice();
        gameMode = MODE_ICE;
        player.respawnForNewLevel(level.getSpawnX(), level.getSpawnY());
        cameraY = computeCameraY();
    }

    private void switchToCandy() {
        level = new Candy();
        gameMode = MODE_CANDY;
        player.respawnForNewLevel(level.getSpawnX(), level.getSpawnY());
        cameraY = computeCameraY();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // DEBUG HOTKEYS (so you don't redo tutorial):
        // 1 = Town, 2 = Beach, 3 = Tutorial
        if (code == KeyEvent.VK_1) {
            switchToTown();
        } else {
            if (code == KeyEvent.VK_2) {
                switchToBeach();
            } else {
                if (code == KeyEvent.VK_3) {
                    level = new Tutorial();
                    gameMode = MODE_TUTORIAL;
                    player.respawnForNewLevel(level.getSpawnX(), level.getSpawnY());
                    cameraY = computeCameraY();
                } else {
                    if (code == KeyEvent.VK_E) {
                        if (player.isTouchingDoor()) {
                            String target = player.getTouchingDoorTarget();

                            if ("TOWN".equals(target)) {
                                switchToTown();
                            } else if ("BEACH".equals(target)) {
                                switchToBeach();
                            } else if ("ICE".equals(target)) {
                                switchToIce();
                            } else if ("CANDY".equals(target)) {
                                switchToCandy();
                            } else {
                                player.showMessage("Unknown door: " + target);
                            }
                        }
                    } else {
                        player.keyPressed(e);
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) { }
}
