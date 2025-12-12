package src.game;

import java.awt.Graphics;

public class Coin extends GameObject {

    private int value;
    private int animFrame;
    private int animTimer;

    public Coin(int x, int y, int value) {
        super(x, y, 32, 32);
        this.value = value;
        animFrame = 0;
        animTimer = 0;
    }

    @Override
public void update(SimpleLevel level) {
    animTimer = animTimer + 1;

    int frameSpeed = 2;   // was 6, now MUCH faster

    if (animTimer >= frameSpeed) {
        animTimer = 0;

        int count = 1;
        if (Assets.coinGold != null) {
            count = Assets.coinGold.length;
            if (count <= 0) {
                count = 1;
            }
        }

        animFrame = animFrame + 1;
        if (animFrame >= count) {
            animFrame = 0;
        }
    }
}


    @Override
    public void draw(Graphics g) {
        java.awt.image.BufferedImage img = null;

        if (Assets.coinGold != null && Assets.coinGold.length > 0) {
            int index = animFrame;
            if (index < 0) {
                index = 0;
            } else {
                if (index >= Assets.coinGold.length) {
                    index = Assets.coinGold.length - 1;
                }
            }
            img = Assets.coinGold[index];
        }

            if (img != null) {
        g.drawImage(img, x, y, width, height, null);
    } else {
        g.setColor(java.awt.Color.YELLOW);
        g.fillOval(x, y, width, height);
    }

}
    

    public int getValue() {
        int v = value;
        return v;
    }
        public void onPickup(Player player) {
        player.addCoins(value);
    }


}