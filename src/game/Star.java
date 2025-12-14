package src.game;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Star extends GameObject {

    private int frame;
    private int timer;

    public Star(int x, int y) {
        super(x, y, 64, 64);
        frame = 0;
        timer = 0;
    }

    public void update(SimpleLevel level) {
        timer = timer + 1;
        if (timer >= 6) {
            timer = 0;
            frame = frame + 1;
            if (frame >= 10) {
                frame = 0;
            }
        }
    }

    public Rectangle getBounds() {
        Rectangle r = new Rectangle(x, y, width, height);
        return r;
    }

    public void draw(Graphics g) {
        java.awt.image.BufferedImage img = Assets.starFrames[frame];
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        }
    }
}
