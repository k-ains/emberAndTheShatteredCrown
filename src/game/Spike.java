package src.game;

import java.awt.Color;
import java.awt.Graphics;

public class Spike extends GameObject {

    public Spike(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update(SimpleLevel level) {
        // spikes do not move (for now)
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);

        int[] xs = new int[3];
        int[] ys = new int[3];

        xs[0] = x;
        ys[0] = y + height;

        xs[1] = x + width / 2;
        ys[1] = y;

        xs[2] = x + width;
        ys[2] = y + height;

        g.fillPolygon(xs, ys, 3);
    }
}
