package src.game;

import java.awt.Graphics;
import java.awt.Color;

public class CheckpointFlag extends GameObject {

    private boolean active;

    public CheckpointFlag(int x, int y) {
        super(x, y, 24, 40);
        active = false;
    }

    @Override
    public void update(SimpleLevel level) { }

    @Override
    public void draw(Graphics g) {
        Color poleColor;
        Color flagColor;

        if (active) {
            poleColor = Color.WHITE;
            flagColor = Color.GREEN;
        } else {
            poleColor = Color.DARK_GRAY;
            flagColor = Color.RED;
        }

        g.setColor(poleColor);
        g.fillRect(x + width / 2 - 2, y + 5, 4, height - 10);

        int flagTopX = x + width / 2;
        int flagTopY = y + 5;

        int[] xs = new int[3];
        int[] ys = new int[3];

        xs[0] = flagTopX;
        ys[0] = flagTopY;

        xs[1] = flagTopX + 16;
        ys[1] = flagTopY + 8;

        xs[2] = flagTopX;
        ys[2] = flagTopY + 16;

        g.setColor(flagColor);
        g.fillPolygon(xs, ys, 3);
    }

    public void activate(Player player) {
        if (!active) {
            active = true;

            int spawnX = x + width / 2 - player.getWidth() / 2;
            int spawnY = y - player.getHeight();

            player.setCheckpoint(spawnX, spawnY);
        }
    }

    public void reset() {
        active = false;
    }

    public boolean isActive() {
        boolean value = active;
        return value;
    }
}
