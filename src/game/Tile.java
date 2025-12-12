package src.game;

import java.awt.Color;
import java.awt.Graphics;

public class Tile extends GameObject {

    private boolean solid;

    public Tile(int x, int y, int width, int height, boolean solid) {
        super(x, y, width, height);
        this.solid = solid;
    }

    @Override
    public void update(SimpleLevel level) {
        // tiles do not move (for now)
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
    }

    public boolean isSolid() {
        boolean value = solid;
        return value;
    }
}
