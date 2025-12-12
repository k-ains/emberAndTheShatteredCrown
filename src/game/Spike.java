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
    if (Assets.spike != null) {
        g.drawImage(Assets.spike, x, y, width, height, null);
    } else {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }
}
}