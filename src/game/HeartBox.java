package src.game;

import java.awt.Graphics;
import java.awt.Rectangle;

public class HeartBox extends GameObject {

    public HeartBox(int x, int y) {
        super(x, y, 32, 32);
    }

    public Rectangle getBounds() {
        Rectangle r = new Rectangle(x, y, width, height);
        return r;
    }

    @Override
    public void update(SimpleLevel level) { }

    @Override
    public void draw(Graphics g) {
        if (Assets.boxHeart != null) {
            g.drawImage(Assets.boxHeart, x, y, width, height, null);
        }
    }
}
