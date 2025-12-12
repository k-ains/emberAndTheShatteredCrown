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

    // best height tracking
    private int bestPlatformCount;
    private int bestPlatformLineY;

    // current run tracking
    private int runMaxPlatformCount;
    private int runBestY;
    private int currentPlatformCount;


    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);

        Assets.init();

        level = new SimpleLevel();

        int startY = level.getWorldHeight() - 120;
        player = new Player(100, startY);

        cameraY = player.getY() - HEIGHT / 2;
        if (cameraY < 0) {
            cameraY = 0;
        }

        bestPlatformCount = 0;
        bestPlatformLineY = player.getY();

        runMaxPlatformCount = 0;
        runBestY = player.getY();
        currentPlatformCount = 0;

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
   
int currentPlatforms = level.getPlatformsReachedCount(player.getY());
currentPlatformCount = currentPlatforms;

if (currentPlatforms > runMaxPlatformCount) {
    runMaxPlatformCount = currentPlatforms;
    runBestY = player.getY();
}

player.updateMessageTimer();


        int desiredCameraY = player.getY() - HEIGHT / 2;

        if (desiredCameraY < cameraY) {
            cameraY = desiredCameraY;
        }

        if (cameraY < 0) {
            cameraY = 0;
        }

        int visibleBottom = cameraY + HEIGHT + 40;

        if (player.getY() > visibleBottom) {
            player.markDead();
        }

        if (player.isDead()) {
            if (runMaxPlatformCount > bestPlatformCount) {
                bestPlatformCount = runMaxPlatformCount;
                bestPlatformLineY = runBestY;
            }

            runMaxPlatformCount = 0;
            runBestY = player.getY();

            player.onDeath(level);

            cameraY = player.getY() - HEIGHT / 2;
            if (cameraY < 0) {
                cameraY = 0;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (Assets.background != null) {
    g.drawImage(Assets.background, 0, 0, WIDTH, HEIGHT, null);
} else {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, WIDTH, HEIGHT);
}


        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        g2.translate(0, -cameraY);

        level.draw(g2);
        player.draw(g2);

        if (bestPlatformCount > 0) {
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

    // -------- HEARTS ROW (top-left) --------
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

    // vertical starting point for text/coins below hearts
    int hudStartY = heartY + 10;  // a bit below the hearts row

    // -------- SPINNING COIN ICON + COUNT --------
    int coinIconSize = 20;
    int coinIconX = 10;
    int coinIconY = hudStartY + coinIconSize; // drawImage uses top-left

    // pick a frame based on time so it spins
    if (Assets.coinGold != null && Assets.coinGold.length > 0) {
        long time = System.currentTimeMillis();
        int hudFrame = (int) ((time / 100) % Assets.coinGold.length); // change 100 for speed
        java.awt.image.BufferedImage hudImg = Assets.coinGold[hudFrame];
        g.drawImage(hudImg, coinIconX, coinIconY - coinIconSize, coinIconSize, coinIconSize, null);
    } else {
        g.setColor(java.awt.Color.YELLOW);
        g.fillOval(coinIconX, coinIconY - coinIconSize, coinIconSize, coinIconSize);
    }

    g.setColor(java.awt.Color.WHITE);
    g.drawString("x " + player.getCoins(), coinIconX + coinIconSize + 6, coinIconY - 5);

    // -------- PLATFORM TEXT (still below everything) --------
    int textY = coinIconY + 12;

    g.setColor(java.awt.Color.WHITE);
    g.drawString("Current Platforms: " + currentPlatformCount, 10, textY);
    g.drawString("Best Platforms: " + bestPlatformCount, 10, textY + 18);

    // -------- POPUP MESSAGE AT BOTTOM --------
    String msg = player.getPopupMessage();
    if (player.getPopupTimer() > 0 && msg != null && msg.length() > 0) {
        g.setColor(java.awt.Color.WHITE);
        g.drawString(msg, 10, HEIGHT - 20);
    }
}




    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }
}
