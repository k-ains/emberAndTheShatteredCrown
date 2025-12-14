package src.game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class Player extends GameObject {

    private static final int MOVE_SPEED = 3;

    // climbing
    private static final int CLIMB_SPEED = 2;

    // jump / gravity
    private static final int JUMP_STRENGTH = -20;
    private static final int GRAVITY = 1;
    private static final int MAX_FALL_SPEED = 9;

    private static final int STATE_IDLE = 0;
    private static final int STATE_WALK = 1;
    private static final int STATE_JUMP = 2;
    private static final int STATE_FALL = 3;

    private int velX;
    private int velY;

    private boolean leftPressed;
    private boolean rightPressed;

    private boolean jumpPressed; // SPACE
    private boolean upPressed;   // UP/W
    private boolean downPressed; // DOWN/S

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
    private boolean facingRight;

    private boolean reachedGoal;

    private boolean climbing;

    private boolean touchingDoor;
    private String touchingDoorTarget;

    private int starsCollected;

    public Player(int x, int y) {
        super(x, y, 32, 32);

        velX = 0;
        velY = 0;

        leftPressed = false;
        rightPressed = false;

        jumpPressed = false;
        upPressed = false;
        downPressed = false;

        onGround = false;

        startX = x;
        startY = y;

        respawnX = startX;
        respawnY = startY;

        maxHearts = 3;
        hearts = maxHearts;

        coins = 0;
        dead = false;

        popupMessage = "";
        popupTimer = 0;

        animState = STATE_IDLE;
        animFrame = 0;
        animTimer = 0;

        facingRight = true;
        reachedGoal = false;

        climbing = false;

        touchingDoor = false;
        touchingDoorTarget = "";

        starsCollected = 0;
    }

    @Override
    public void update(SimpleLevel level) {
        if (!dead) {

            // -------- HORIZONTAL INPUT --------
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

            Rectangle playerBounds = getBounds();

            // -------- CHECK IF TOUCHING CLIMB TILE --------
            boolean touchingClimb = false;

            java.util.List<Tile> allTilesForClimb = level.getTiles();
            int ci = 0;
            while (ci < allTilesForClimb.size()) {
                Tile t = allTilesForClimb.get(ci);

                if (t instanceof ClimbTile) {
                    if (playerBounds.intersects(t.getBounds())) {
                        touchingClimb = true;
                    }
                }

                ci = ci + 1;
            }

            // -------- RULES --------
            // UP jumps unless you're actively climbing on a trunk.
            boolean wantsClimb = false;
            if (touchingClimb) {
                if (upPressed || downPressed) {
                    wantsClimb = true;
                }
            }

            climbing = wantsClimb;

            boolean jumpInput = jumpPressed || (upPressed && !climbing);

            // -------- JUMP / CLIMB / GRAVITY --------
            if (!climbing) {
                if (jumpInput && onGround) {
                    velY = JUMP_STRENGTH;
                    onGround = false;
                }

                velY = velY + GRAVITY;
                if (velY > MAX_FALL_SPEED) {
                    velY = MAX_FALL_SPEED;
                }
            } else {
                velY = 0;

                if (upPressed) {
                    velY = -CLIMB_SPEED;
                } else {
                    if (downPressed) {
                        velY = CLIMB_SPEED;
                    }
                }
            }

            // -------- APPLY MOVEMENT (with climb-safe top landing) --------
            int nextX = x + velX;
            int nextY = y + velY;

            // clamp X
            if (nextX < 0) {
                nextX = 0;
            } else {
                int maxX = GamePanel.WIDTH - width;
                if (nextX > maxX) {
                    nextX = maxX;
                }
            }

            // If climbing, prevent getting stuck inside solid tiles.
            // If moving up into a solid tile (tree top), "finish" by placing you on top.
            if (climbing && velY < 0) {
                Rectangle nextBounds = new Rectangle(nextX, nextY, width, height);

                java.util.List<Tile> solids = level.getTiles();
                int si = 0;
                while (si < solids.size()) {
                    Tile st = solids.get(si);

                    if (st.isSolid()) {
                        Rectangle sb = st.getBounds();

                        if (nextBounds.intersects(sb)) {
                            // land on top of this tile (tiny correction, feels like reaching the ledge)
                            nextY = sb.y - height;
                            velY = 0;
                            climbing = false;
                            onGround = true;
                        }
                    }

                    si = si + 1;
                }
            }

            x = nextX;
            y = nextY;

            onGround = false;
            playerBounds = getBounds();

            // -------- COLLISION WITH SOLID TILES (normal landing + bounce) --------
            java.util.List<Tile> tileList = level.getTiles();
            int tIndex = 0;
            while (tIndex < tileList.size()) {
                Tile t = tileList.get(tIndex);

                if (t.isSolid()) {
                    Rectangle tileBounds = t.getBounds();

                    if (playerBounds.intersects(tileBounds)) {
                        boolean comingFromAbove =
                            velY >= 0 && playerBounds.y + height <= tileBounds.y + velY;

                        if (comingFromAbove) {
                            y = tileBounds.y - height;

                            if (t instanceof BounceTile) {
                                BounceTile bt = (BounceTile) t;
                                velY = bt.getBounceVel();
                                onGround = false;
                            } else {
                                velY = 0;
                                onGround = true;
                            }

                            playerBounds = getBounds();
                        }
                    }
                }

                tIndex = tIndex + 1;
            }

            // -------- PLANTS --------
            if (level.plants != null) {
                java.util.List<Plant> plantList = level.plants;
                int pIndex = 0;
                while (pIndex < plantList.size()) {
                    Plant p = plantList.get(pIndex);
                    Rectangle pb = p.getBounds();

                    if (playerBounds.intersects(pb)) {
                        boolean comingFromAbove = velY >= 0 && playerBounds.y + height <= pb.y + velY;

                        if (p.isHarmfulNow()) {
                            markDead();
                        } else {
                            if (p.isSolidNow() && comingFromAbove) {
                                y = pb.y - height;
                                velY = 0;
                                onGround = true;
                                playerBounds = getBounds();
                            }
                        }
                    }

                    pIndex = pIndex + 1;
                }
            }

            // -------- HEARTS / STARS --------
if (level.heartBoxes != null) {
    java.util.Iterator<HeartBox> hbIt = level.heartBoxes.iterator();
    while (hbIt.hasNext()) {
        HeartBox hb = hbIt.next();
        if (playerBounds.intersects(hb.getBounds())) {
            collectHeartFromBox();
            hbIt.remove();
        }
    }
}



            if (level.stars != null) {
                java.util.Iterator<Star> starIt = level.stars.iterator();
                while (starIt.hasNext()) {
                    Star s = starIt.next();
                    if (playerBounds.intersects(s.getBounds())) {
                        addStar();
                        starIt.remove();
                    }
                }
            }

            // -------- SPIKES --------
            java.util.List<Spike> spikeList = level.getSpikes();
            int sIndex = 0;
            while (sIndex < spikeList.size()) {
                Spike spike = spikeList.get(sIndex);
                if (playerBounds.intersects(spike.getBounds())) {
                    markDead();
                }
                sIndex = sIndex + 1;
            }

            // -------- ENEMIES --------
            java.util.List<WalkingEnemy> enemyList = level.getEnemies();
            int eIndex = 0;
            while (eIndex < enemyList.size()) {
                WalkingEnemy enemy = enemyList.get(eIndex);
                if (playerBounds.intersects(enemy.getBounds())) {
                    markDead();
                }
                eIndex = eIndex + 1;
            }

            // -------- COINS --------
            java.util.Iterator<Coin> coinIterator = level.getCoins().iterator();
            while (coinIterator.hasNext()) {
                Coin coin = coinIterator.next();
                if (playerBounds.intersects(coin.getBounds())) {
                    coin.onPickup(this);
                    coinIterator.remove();
                }
            }

            // -------- MESSAGE BOXES --------
            java.util.List<MessageBox> boxes = level.getMessageBoxes();
            int j = 0;
            while (j < boxes.size()) {
                MessageBox box = boxes.get(j);
                Rectangle boxBounds = box.getBounds();

                if (playerBounds.intersects(boxBounds)) {
                    int boxTop = boxBounds.y;
                    int boxBottom = boxBounds.y + boxBounds.height;
                    int playerTop = playerBounds.y;
                    int playerBottom = playerBounds.y + height;

                    if (velY >= 0 && playerBottom <= boxTop + velY) {
                        y = boxTop - height;
                        velY = 0;
                        onGround = true;
                        playerBounds = getBounds();
                    } else {
                        int boxCenterY = boxBounds.y + boxBounds.height / 2;
                        if (velY < 0 && playerTop > boxCenterY) {
                            y = boxBottom;
                            velY = 0;
                            showMessage(box.getMessage());
                            playerBounds = getBounds();
                        }
                    }
                }

                j = j + 1;
            }

            // -------- CHECKPOINT FLAGS --------
            java.util.List<CheckpointFlag> flags = level.getCheckpoints();
            int fIndex = 0;
            while (fIndex < flags.size()) {
                CheckpointFlag flag = flags.get(fIndex);
                if (playerBounds.intersects(flag.getBounds())) {
                    flag.activate(this);
                }
                fIndex = fIndex + 1;
            }

            // -------- DOORS --------
            touchingDoor = false;
            touchingDoorTarget = "";

            java.util.List<Door> doorList = level.getDoors();
            int dIndex = 0;
            while (dIndex < doorList.size()) {
                Door d = doorList.get(dIndex);
                if (playerBounds.intersects(d.getBounds())) {
                    touchingDoor = true;
                    touchingDoorTarget = d.getTarget();
                }
                dIndex = dIndex + 1;
            }

            // -------- ANIMATION --------
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

            int frameSpeed = 10;
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
                g2.drawImage(img, x + width, y, -width, height, null);
            }
        } else {
            g.setColor(java.awt.Color.ORANGE);
            g.fillRect(x, y, width, height);
        }
    }

    public void showMessage(String text) {
        popupMessage = text;
        popupTimer = 180;
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

    public int getCoins() {
        int value = coins;
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

    public boolean isDead() {
        boolean value = dead;
        return value;
    }

    public void markDead() {
        dead = true;
    }

    public void setCheckpoint(int x, int y) {
        respawnX = x;
        respawnY = y;
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
    public void healToFull() {
    hearts = maxHearts;
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

    public void respawnForNewLevel(int newStartX, int newStartY) {
        startX = newStartX;
        startY = newStartY;
        respawnX = newStartX;
        respawnY = newStartY;
        x = newStartX;
        y = newStartY;
        velX = 0;
        velY = 0;
        dead = false;
    }

    public boolean isClimbing() {
        boolean value = climbing;
        return value;
    }

    public boolean isTouchingDoor() {
        boolean value = touchingDoor;
        return value;
    }

    public String getTouchingDoorTarget() {
        String value = touchingDoorTarget;
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
                if (code == KeyEvent.VK_SPACE) {
                    jumpPressed = true;
                } else {
                    if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                        upPressed = true;
                    } else {
                        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                            downPressed = true;
                        }
                    }
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
                if (code == KeyEvent.VK_SPACE) {
                    jumpPressed = false;
                } else {
                    if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                        upPressed = false;
                    } else {
                        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                            downPressed = false;
                        }
                    }
                }
            }
        }
    }

    public void keyTyped(KeyEvent e) { }

    public void addCoins(int amount) {
        coins = coins + amount;
    }
    public void collectHeartFromBox() {
    // Rule: either heal 1 OR add a container, max 5 containers.
    // If not full health -> heal 1
    if (hearts < maxHearts) {
        hearts = hearts + 1;
    } else {
        // already full, increase max (up to 5) and heal into it
        if (maxHearts < 5) {
            maxHearts = maxHearts + 1;
            hearts = hearts + 1;
        }
    }

    if (hearts > maxHearts) {
        hearts = maxHearts;
    }

    showMessage("Heart!");
}

    public void addStar() {
        starsCollected = starsCollected + 1;
    }

    public int getStarsCollected() {
        int v = starsCollected;
        return v;
    }

    public void increaseHeartsToMax5() {
        if (maxHearts < 5) {
            maxHearts = maxHearts + 1;
        }

        if (hearts < maxHearts) {
            hearts = hearts + 1;
        } else {
            hearts = maxHearts;
        }
    }
}
