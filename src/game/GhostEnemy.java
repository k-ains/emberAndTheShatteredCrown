package src.game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GhostEnemy extends WalkingEnemy {

    private int leftBound;
    private int rightBound;
    private int speed;
    private int direction;

    private int baseY;
    private int floatTimer;

    private int animFrame;
    private int animTimer;
    private boolean facingRight;
    
    private BufferedImage[] sprites;

    public GhostEnemy(int x, int y, int w, int h,
                      int leftBound, int rightBound, int speed) {
        this(x, y, w, h, leftBound, rightBound, speed, Assets.ghostWalk);
    }
    
    public GhostEnemy(int x, int y, int w, int h,
                      int leftBound, int rightBound, int speed,
                      BufferedImage[] customSprites) {
        super(x, y, w, h, leftBound, rightBound, speed);

        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.speed = speed;
        this.direction = 1;

        this.baseY = y;
        this.floatTimer = 0;

        this.animFrame = 0;
        this.animTimer = 0;
        this.facingRight = true;
        
        this.sprites = customSprites != null ? customSprites : Assets.ghostWalk;
    }

    @Override
    public void update(SimpleLevel level) {
        // horizontal movement
        x += direction * speed;

        if (x < leftBound) {
            x = leftBound;
            direction = 1;
        } else if (x + width > rightBound) {
            x = rightBound - width;
            direction = -1;
        }

        facingRight = direction > 0;

        // vertical floating motion
        floatTimer++;
        y = baseY + (int)(Math.sin(floatTimer * 0.05) * 6);

        // animation
        animTimer++;
        if (animTimer >= 14) {
            animTimer = 0;
            animFrame++;

            if (sprites != null && animFrame >= sprites.length) {
                animFrame = 0;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = null;

        if (sprites != null && sprites.length > 0) {
            img = sprites[animFrame];
        }

        if (img != null) {
            Graphics2D g2 = (Graphics2D) g;
            if (facingRight) {
                g2.drawImage(img, x, y, width, height, null);
            } else {
                g2.drawImage(img, x + width, y, -width, height, null);
            }
        }
    }
}
