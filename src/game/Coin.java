package src.game;

import java.awt.Graphics;
import java.awt.Color;

public class Coin extends Collectible {

    private int value;

    public Coin(int x, int y, int value) {
        super(x, y, 16, 16);
        this.value = value;
    }

    @Override
    public void onPickup(Player player) {
        player.addCoins(value);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, width, height);

        g.setColor(Color.ORANGE);
        g.drawOval(x, y, width, height);
    }
}
