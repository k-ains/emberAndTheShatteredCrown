package src.game;

import java.awt.image.BufferedImage;

public class BounceTile extends Tile {
    private int bounceVel;

    public BounceTile(int x, int y, int w, int h, boolean solid, int bounceVel) {
        super(x, y, w, h, solid);
        this.bounceVel = bounceVel;
    }

    public int getBounceVel() {
        int v = bounceVel;
        return v;
    }
}
