package src.game;

import java.awt.Graphics;

public abstract class Collectible extends GameObject {

    public Collectible(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public abstract void onPickup(Player player);

    @Override
    public void update(SimpleLevel level) {
        // default: no movement
    }

    @Override
    public abstract void draw(Graphics g);
}
