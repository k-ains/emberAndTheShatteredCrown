package src.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;


public class Player extends GameObject {

    private static final int MOVE_SPEED = 3;
    private static final int JUMP_STRENGTH = -20;  
    private static final int GRAVITY = 1;
    private static final int MAX_FALL_SPEED = 9;

    //for player animation
    private static final int STATE_IDLE = 0;
    private static final int STATE_WALK = 1;
    private static final int STATE_JUMP = 2;
    private static final int STATE_FALL = 3;


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

    private String popupMessage;
    private int popupTimer;

        private int animState;
    private int animFrame;
    private int animTimer;
    private boolean facingRight; //because sprite only has 1 direction


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

        facingRight = true;


        popupMessage = "";
        popupTimer = 0;

                animState = STATE_IDLE;
        animFrame = 0;
        animTimer = 0;


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
            if (velX < 0) {
    facingRight = false;
} else {
    if (velX > 0) {
        facingRight = true;
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
            // collision with enemies
                java.util.List<WalkingEnemy> enemyList = level.getEnemies();
                int enemyIndex = 0;
                while (enemyIndex < enemyList.size()) {
                    WalkingEnemy enemy = enemyList.get(enemyIndex);
                    Rectangle enemyBounds = enemy.getBounds();

                    if (playerBounds.intersects(enemyBounds)) {
                        markDead();
                    }

                    enemyIndex = enemyIndex + 1;
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
            // collision with message boxes (bonk from below)
            java.util.List<MessageBox> boxes = level.getMessageBoxes();
            int j = 0;
            while (j < boxes.size()) {
                MessageBox box = boxes.get(j);
                Rectangle boxBounds = box.getBounds();

                if (playerBounds.intersects(boxBounds) && velY < 0) {
                    int playerTop = playerBounds.y;
                    int boxCenterY = boxBounds.y + boxBounds.height / 2;

                    if (playerTop > boxCenterY) {
                        showMessage(box.getMessage());
                    }
                }

                        j = j + 1;
                    }


            // collision with checkpoint flags
            for (CheckpointFlag flag : level.getCheckpoints()) {
                Rectangle flagBounds = flag.getBounds();
                if (playerBounds.intersects(flagBounds)) {
                    flag.activate(this);
                }
            }
                   // ----- ANIMATION STATE -----
            int newState = animState;

            if (!onGround) {
                if (velY < 0) {
                    newState = STATE_JUMP;
                } else {
                    newState = STATE_FALL;
                }
            } else {
                if (velX != 0) {
                    newState = STATE_WALK;
                } else {
                    newState = STATE_IDLE;
                }
            }

            if (newState != animState) {
                animState = newState;
                animFrame = 0;
                animTimer = 0;
            }

            // ----- ANIMATION FRAME -----
            int frameSpeed = 10; // higher = slower animation
            int framesCount = 1;

            if (animState == STATE_IDLE && Assets.playerIdle != null) {
                framesCount = Assets.playerIdle.length;
            } else {
                if (animState == STATE_WALK && Assets.playerWalk != null) {
                    framesCount = Assets.playerWalk.length;
                } else {
                    framesCount = 1;
                }
            }

            if (framesCount <= 0) {
                framesCount = 1;
            }

            animTimer = animTimer + 1;

            if (animTimer >= frameSpeed) {
                animTimer = 0;
                animFrame = animFrame + 1;
                if (animFrame >= framesCount) {
                    animFrame = 0;
                }
            }

        }
    }

    public void showMessage(String text) {
    popupMessage = text;
    popupTimer = 180; // about 3 seconds at 60 FPS
}

public void updateMessageTimer() {
    if (popupTimer > 0) {
        popupTimer = popupTimer - 1;
        if (popupTimer == 0) {
            popupMessage = "";
        }
    }
}

public String getPopupMessage() {
    String value = popupMessage;
    return value;
}

public int getPopupTimer() {
    int value = popupTimer;
    return value;
}


   @Override
public void draw(Graphics g) {
    BufferedImage img = null;

    if (animState == STATE_IDLE && Assets.playerIdle != null) {
        if (Assets.playerIdle.length > 0) {
            int index = animFrame;
            if (index < 0) {
                index = 0;
            } else {
                if (index >= Assets.playerIdle.length) {
                index = Assets.playerIdle.length - 1;
                }
            }
            img = Assets.playerIdle[index];
        }
    } else {
        if (animState == STATE_WALK && Assets.playerWalk != null) {
            if (Assets.playerWalk.length > 0) {
                int index = animFrame;
                if (index < 0) {
                    index = 0;
                } else {
                    if (index >= Assets.playerWalk.length) {
                        index = Assets.playerWalk.length - 1;
                    }
                }
                img = Assets.playerWalk[index];
            }
        } else {
            if (animState == STATE_JUMP && Assets.playerJump != null) {
                img = Assets.playerJump;
            } else {
                if (animState == STATE_FALL && Assets.playerFall != null) {
                    img = Assets.playerFall;
                }
            }
        }
    }

    if (img == null) {
        img = Assets.playerFallback;
    }

    if (img != null) {
        Graphics2D g2 = (Graphics2D) g;

        if (facingRight) {
            g2.drawImage(img, x, y, width, height, null);
        } else {
            // flip horizontally by drawing with negative width
            g2.drawImage(img, x + width, y, -width, height, null);
        }
    } else {
        g.setColor(java.awt.Color.ORANGE);
        g.fillRect(x, y, width, height);
    }
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
