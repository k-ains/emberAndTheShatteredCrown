package src.game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class CrabEnemy extends WalkingEnemy {

    private int animFrame;
    private int animTimer;

    // --- Crab circling platform ---
    private static final int STATE_TOP = 0;
    private static final int STATE_DROP_RIGHT = 1;
    private static final int STATE_BOTTOM = 2;
    private static final int STATE_CLIMB_LEFT = 3;
    private static final int STATE_DROP_LEFT = 4;
    private static final int STATE_CLIMB_RIGHT = 5;
    private int state = STATE_TOP;
    private int platformTopY;
    private int platformBottomY;
    private int platformLeft;
    private int platformRight;
    private int climbSpeed = 2;
    private int dropSpeed = 3;
    private boolean circlePlatform = true;
    private boolean facingRight = true;

    public void setCirclePlatform(boolean v) { circlePlatform = v; }

    public CrabEnemy(int x, int y, int w, int h, int leftBound, int rightBound, int speed) {
        super(x, y, w, h, leftBound, rightBound, speed);
        animFrame = 0;
        animTimer = 0;
        // For circling, set platform bounds
        this.platformLeft = leftBound;
        this.platformRight = rightBound + w;
        this.platformTopY = y;
        this.platformBottomY = y + 32; // assumes platform is 32px thick
    }    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;
        if (Assets.crabWalk != null && Assets.crabWalk.length > 0) {
            animTimer = animTimer + 1;
            if (animTimer >= 10) {
                animTimer = 0;
                animFrame = animFrame + 1;
                if (animFrame >= Assets.crabWalk.length) {
                    animFrame = 0;
                }
            }
            img = Assets.crabWalk[animFrame];
        }
        if (img != null) {
            // Flip sprite based on direction and state (upside down when on bottom/dropping/climbing)
            boolean flipVertical = (state == STATE_BOTTOM || state == STATE_DROP_RIGHT || state == STATE_CLIMB_LEFT);
            
            if (flipVertical) {
                // Draw upside down below the platform (and flip horizontal based on facing direction)
                // Offset by full height so crab appears below the platform edge
                int offsetY = y + height + height; // double height to position below platform
                if (facingRight) {
                    g.drawImage(img, x, offsetY, width, -height, null);
                } else {
                    g.drawImage(img, x + width, offsetY, -width, -height, null);
                }
            } else {
                // Draw normal (just flip horizontal based on facing direction)
                if (facingRight) {
                    g.drawImage(img, x, y, width, height, null);
                } else {
                    g.drawImage(img, x + width, y, -width, height, null);
                }
            }
        } else {
            g.fillRect(x, y, width, height);
        }
    }

    @Override
    public void update(SimpleLevel level) {
        if (!circlePlatform) {
            super.update(level);
            return;
        }
        switch (state) {
            case STATE_TOP:
                x += 2;
                facingRight = true;
                if (x + width >= platformRight) {
                    x = platformRight - width;
                    state = STATE_DROP_RIGHT;
                }
                break;
            case STATE_DROP_RIGHT:
                y += dropSpeed;
                // keep facing right while dropping
                if (y >= platformBottomY) {
                    y = platformBottomY;
                    state = STATE_BOTTOM;
                }
                break;
            case STATE_BOTTOM:
                x -= 2;
                facingRight = false; // flip when walking left at bottom
                if (x <= platformLeft) {
                    x = platformLeft;
                    state = STATE_CLIMB_LEFT;
                }
                break;
            case STATE_CLIMB_LEFT:
                y -= climbSpeed;
                // keep facing left while climbing
                if (y <= platformTopY) {
                    y = platformTopY;
                    state = STATE_TOP;
                }
                break;
        }
    }

    @Override
    public Rectangle getBounds() {
        Rectangle r = new Rectangle(x, y, width, height);
        return r;
    }
}
