package src.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BreakableTile extends Tile {

    private int breakTimer;
    private boolean triggered;
    private boolean broken;

    private static final int BREAK_DELAY = 45; // frames before fully breaking

    private BufferedImage[] sprites; // [0] = intact, [1] = breaking

    public BreakableTile(int x, int y, int w, int h, BufferedImage[] sprites) {
        super(x, y, w, h, true);
        this.sprites = sprites;
        this.breakTimer = 0;
        this.triggered = false;
        this.broken = false;
        this.sprite = sprites[0]; // default intact
    }

    // Call this when player steps on it
    public void trigger() {
        if (!triggered) {
            triggered = true;
            breakTimer = 0;
            Sound.play("/src/assets/sounds/iceCracking.wav");
        }
    }

    @Override
    public void update(SimpleLevel level) {
        if (!triggered || broken) {
            return;
        }

        breakTimer++;

        // show "breaking" sprite halfway through
        if (breakTimer >= BREAK_DELAY / 2) {
            sprite = sprites[1]; // cracked/breaking
        }

        if (breakTimer >= BREAK_DELAY) {
            broken = true;
            solid = false; // no collision anymore
        }
    }

    @Override
    public void draw(Graphics g) {
        if (broken) return;

        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        }
    }

    public boolean isBroken() {
        return broken;
    }
}
