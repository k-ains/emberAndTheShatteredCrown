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
    }    public void draw(Graphics g) {
        java.awt.image.BufferedImage img = null;

        if (frame == 0) { img = Assets.plantIdle0; }
        if (frame == 1) { img = Assets.plantIdle1; }
        if (frame == 2) { img = Assets.plantIdle2; }
        if (frame == 3) { img = Assets.plantIdle3; }

        if (img != null) {
            // Scale up the plant based on frame (frame 3 = normal size, frame 0 = biggest)
            int growthFactor = (3 - frame); // 3, 2, 1, 0 -> grows from frame 3 to frame 0
            int scaleW = width + (growthFactor * 8); // grows 8px wider per stage
            int scaleH = height + (growthFactor * 8); // grows 8px taller per stage
            int offsetX = (scaleW - width) / 2; // center horizontally
            int offsetY = scaleH - height; // grow upward from base
            g.drawImage(img, x - offsetX, y - offsetY, scaleW, scaleH, null);
        }
    }
}
