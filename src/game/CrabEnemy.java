package src.game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class CrabEnemy extends WalkingEnemy {

    private int animFrame;
    private int animTimer;

    public CrabEnemy(int x, int y, int w, int h, int leftBound, int rightBound, int speed) {
        super(x, y, w, h, leftBound, rightBound, speed);
        animFrame = 0;
        animTimer = 0;
    }

    @Override
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
            g.drawImage(img, x, y, width, height, null);
        } else {
            g.fillRect(x, y, width, height);
        }
    }

    @Override
    public Rectangle getBounds() {
        Rectangle r = new Rectangle(x, y, width, height);
        return r;
    }
}
