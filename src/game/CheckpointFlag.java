package src.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class CheckpointFlag extends GameObject {

    private boolean activated;

    public CheckpointFlag(int x, int y) {
        super(x, y, 32, 48);
        activated = false;
    }

public void activate(Player p) {
    if (!activated) {
        activated = true;

        // platform surface is the bottom of the flag
        int platformY = y + height;

        // stand on top of the platform
        int respawnY = platformY - p.getHeight();

        // center player on the flag so you don't spawn at an edge
        int respawnX = x + width / 2 - p.getWidth() / 2;

        p.setCheckpoint(respawnX, respawnY);
        p.showMessage("Checkpoint!");
    }
}

    public void reset() {
        activated = false;
    }

    public Rectangle getBounds() {
        Rectangle r = new Rectangle(x, y, width, height);
        return r;
    }

    @Override
    public void update(SimpleLevel level) { }

    @Override
    public void draw(Graphics g) {
        if (activated) {
            if (Assets.flagRaised != null) {
                g.drawImage(Assets.flagRaised, x, y, width, height, null);
            } else {
                g.setColor(Color.GREEN);
                g.fillRect(x, y, width, height);
            }
        } else {
            if (Assets.flagDown != null) {
                g.drawImage(Assets.flagDown, x, y, width, height, null);
            } else {
                g.setColor(Color.WHITE);
                g.fillRect(x, y, width, height);
            }
        }
    }
}
