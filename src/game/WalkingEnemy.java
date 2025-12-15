package src.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class WalkingEnemy extends GameObject {

    private int speed;
    private int leftBound;
    private int rightBound;
    private int direction; // -1 = left, 1 = right
    private int animFrame;
    private int animTimer;
    private boolean facingRight;

   public WalkingEnemy(int x, int y, int width, int height, int leftBound, int rightBound, int speed) {
    super(x, y, width, height);

    this.leftBound = leftBound;
    this.rightBound = rightBound;
    this.speed = speed;
    this.direction = 1;

    this.animFrame = 0;
    this.animTimer = 0;
    this.facingRight = true;
}


   @Override
public void update(SimpleLevel level) {
    // movement
    x = x + direction * speed;

    if (x < leftBound) {
        x = leftBound;
        direction = 1;
    } else {
        if (x + width > rightBound) {
            x = rightBound - width;
            direction = -1;
        }
    }

    // facing
    if (direction > 0) {
        facingRight = true;
    } else {
        facingRight = false;
    }

    // animation
    animTimer = animTimer + 1;
    int frameSpeed = 15; // bigger = slower animation

    if (animTimer >= frameSpeed) {
        animTimer = 0;
        animFrame = animFrame + 1;

        int count = 1;
        if (Assets.enemyWalk != null) {
            count = Assets.enemyWalk.length;
            if (count <= 0) {
                count = 1;
            }
        }

        if (animFrame >= count) {
            animFrame = 0;
        }
    }
}

   @Override
public void draw(Graphics g) {
    BufferedImage img = null;

    if (Assets.enemyWalk != null && Assets.enemyWalk.length > 0) {
        int index = animFrame;
        if (index < 0) {
            index = 0;
        } else {
            if (index >= Assets.enemyWalk.length) {
                index = Assets.enemyWalk.length - 1;
            }
        }
        img = Assets.enemyWalk[index];
    }

    if (img == null) {
        img = Assets.enemyFallback;
    }

    if (img != null) {
        Graphics2D g2 = (Graphics2D) g;

        if (facingRight) {
            g2.drawImage(img, x, y, width, height, null);
        } else {
            // flip horizontally
            g2.drawImage(img, x + width, y, -width, height, null);
        }
    } else {
        g.setColor(java.awt.Color.CYAN);
        g.fillRect(x, y, width, height);
    }
}

}
