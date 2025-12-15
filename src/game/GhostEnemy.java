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
      // Player following behavior
    private boolean followPlayer = false;
    private static final int MAX_FOLLOW_DISTANCE = 360; // 3 platforms * ~120px gap
    private int playerX = -1;
    private int playerY = -1;
    
    public void setPlayerPosition(int px, int py) {
        this.playerX = px;
        this.playerY = py;
    }

    public GhostEnemy(int x, int y, int w, int h,
                      int leftBound, int rightBound, int speed) {
        this(x, y, w, h, leftBound, rightBound, speed, Assets.ghostWalk);
    }
    
    public void setFollowPlayer(boolean follow) {
        this.followPlayer = follow;
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
        // Player following behavior
        if (followPlayer && playerX >= 0 && playerY >= 0) {
            // Calculate distance to player
            int playerCenterX = playerX + 16; // assuming player is 32px wide
            int playerCenterY = playerY + 16; // assuming player is 32px tall
            int ghostCenterX = x + width / 2;
            int ghostCenterY = y + height / 2;
            
            int dx = playerCenterX - ghostCenterX;
            int dy = playerCenterY - ghostCenterY;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            // Follow player if within 3 platforms distance
            if (distance < MAX_FOLLOW_DISTANCE && distance > 10) {
                // Normalize and move towards player
                double moveX = (dx / distance) * speed;
                double moveY = (dy / distance) * speed * 0.5; // slower vertical movement
                
                x += (int) moveX;
                y += (int) moveY;
                
                facingRight = dx > 0;
            } else {
                // Too far or too close, revert to patrol behavior
                patrolBehavior();
            }
        } else {
            patrolBehavior();
        }

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
    
    private void patrolBehavior() {
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
