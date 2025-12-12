package src.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Decoration extends GameObject {

    private BufferedImage sprite;

    public Decoration(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height);
        this.sprite = sprite;
    }

    @Override
    public void update(SimpleLevel level) {
        // no logic, purely visual
    }

    @Override
    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        }
    }
}
