package src.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Spike extends GameObject {

    BufferedImage sprite;

    public Spike(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.sprite = Assets.spike;
    }

    @Override
    public void update(SimpleLevel level) {
        // spikes do not move (for now)
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    @Override
    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }
}