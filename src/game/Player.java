package src.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Player extends GameObject {

    private static final int MOVE_SPEED = 2;
    private static final int JUMP_STRENGTH = -20;  
    private static final int GRAVITY = 1;
    private static final int MAX_FALL_SPEED = 5;

    private int velX;
    private int velY;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean jumpPressed;
    private boolean onGround;

    private int respawnX;
    private int respawnY;

    private int startX;
    private int startY;

    private int maxHearts;
    private int hearts;

    private int coins;
    private boolean dead;

    public Player(int x, int y) {
        super(x, y, 32, 32);

        velX = 0;
        velY = 0;
        leftPressed = false;
        rightPressed = false;
        jumpPressed = false;
        onGround = false;

        startX = x;
        startY = y;

        respawnX = startX;
        respawnY = startY;

        maxHearts = 3;
        hearts = maxHearts;
        coins = 0;

        dead = false;
    }

    @Override
    public void update(SimpleLevel level) {
        if (!dead) {
            if (leftPressed) {
                velX = -MOVE_SPEED;
            } else {
                if (rightPressed) {
                    velX = MOVE_SPEED;
                } else {
                    velX = 0;
                }
            }

            if (jumpPressed && onGround) {
                velY = JUMP_STRENGTH;
                onGround = false;
            }

            velY = velY + GRAVITY;
            if (velY > MAX_FALL_SPEED) {
                velY = MAX_FALL_SPEED;
            }

            x = x + velX;
            y = y + velY;

            onGround = false;

            Rectangle playerBounds = getBounds();

            // collision with tiles (floor + platforms)
            for (Tile t : level.getTiles()) {
                if (t.isSolid()) {
                    Rectangle tileBounds = t.getBounds();

                    if (playerBounds.intersects(tileBounds)) {
                        boolean comingFromAbove = velY >= 0 && playerBounds.y + height <= tileBounds.y + velY;

                        if (comingFromAbove) {
                            y = tileBounds.y - height;
                            velY = 0;
                            onGround = true;
                            playerBounds = getBounds();
                        }
                    }
                }
            }

            // collision with spikes
            for (Spike spike : level.getSpikes()) {
                Rectangle spikeBounds = spike.getBounds();
                if (playerBounds.intersects(spikeBounds)) {
                    markDead();
                }
            }
            // collision with coins
java.util.Iterator<Coin> coinIterator = level.getCoins().iterator();
while (coinIterator.hasNext()) {
    Coin coin = coinIterator.next();
    Rectangle coinBounds = coin.getBounds();

    if (playerBounds.intersects(coinBounds)) {
        coin.onPickup(this);
        coinIterator.remove();
    }
}


            // collision with checkpoint flags
            for (CheckpointFlag flag : level.getCheckpoints()) {
                Rectangle flagBounds = flag.getBounds();
                if (playerBounds.intersects(flagBounds)) {
                    flag.activate(this);
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, width, height);
    }

    public void setCheckpoint(int x, int y) {
        respawnX = x;
        respawnY = y;
    }

    public void respawnAtCheckpoint(SimpleLevel level) {
        x = respawnX;
        y = respawnY;
        velX = 0;
        velY = 0;
        dead = false;
    }

    public void respawnAtStart(SimpleLevel level) {
        respawnX = startX;
        respawnY = startY;
        respawnAtCheckpoint(level);
    }

public void onDeath(SimpleLevel level) {
    hearts = hearts - 1;

    if (hearts <= 0) {
        hearts = maxHearts;
        respawnAtStart(level);
        level.resetCheckpoints();
    } else {
        respawnAtCheckpoint(level);
    }

    dead = false;
}


    public boolean isDead() {
        boolean value = dead;
        return value;
    }

    public void markDead() {
        dead = true;
    }

    public void addCoins(int amount) {
    coins = coins + amount;
}

public int getCoins() {
    int value = coins;
    return value;
}

    public int getY() {
        int value = y;
        return value;
    }

    public int getWidth() {
        int value = width;
        return value;
    }

    public int getHeight() {
        int value = height;
        return value;
    }

    public int getHearts() {
        int value = hearts;
        return value;
    }

    public int getMaxHearts() {
        int value = maxHearts;
        return value;
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else {
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            } else {
                if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    jumpPressed = true;
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        } else {
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            } else {
                if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    jumpPressed = false;
                }
            }
        }
    }

    public void keyTyped(KeyEvent e) {
        // not used
    }
}
