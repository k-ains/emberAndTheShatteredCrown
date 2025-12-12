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

   player.update(level);

int currentPlatforms = level.getPlatformsReachedCount(player.getY());
currentPlatformCount = currentPlatforms;

if (currentPlatforms > runMaxPlatformCount) {
    runMaxPlatformCount = currentPlatforms;
    runBestY = player.getY();
}


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

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

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

        int i = 0;
        while (i < maxHearts) {
            int x = 10 + i * 18;
            int y = 10;

            if (i < hearts) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.DARK_GRAY);
            }

            g.fillOval(x, y, 14, 14);

            i = i + 1;
        }

       g.setColor(Color.YELLOW);
g.drawString("Coins: " + player.getCoins(), 10, 40);

g.setColor(Color.WHITE);
g.drawString("Current Platforms: " + currentPlatformCount, 10, 60);
g.drawString("Best Platforms: " + bestPlatformCount, 10, 80);

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
