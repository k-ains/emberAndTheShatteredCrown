package src.game;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Plant extends GameObject {

    private int frame;
    private int timer;

    public Plant(int x, int y) {
        super(x, y, 32, 32);
        frame = 0;
        timer = 0;
    }

    public void update(SimpleLevel level) {
        timer = timer + 1;
        if (timer >= 20) { // speed
            timer = 0;
            frame = frame + 1;
            if (frame > 3) {
                frame = 0;
            }
        }
    }

    public boolean isSolidNow() {
        boolean value = (frame == 2 || frame == 3);
        return value;
    }

    public boolean isHarmfulNow() {
        boolean value = (frame == 0 || frame == 1);
        return value;
    }

    public Rectangle getBounds() {
        Rectangle r = new Rectangle(x, y, width, height);
        return r;
    }

    public void draw(Graphics g) {
        java.awt.image.BufferedImage img = null;

        if (frame == 0) { img = Assets.plantIdle0; }
        if (frame == 1) { img = Assets.plantIdle1; }
        if (frame == 2) { img = Assets.plantIdle2; }
        if (frame == 3) { img = Assets.plantIdle3; }

        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        }
    }
}
