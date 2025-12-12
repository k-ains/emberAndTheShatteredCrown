package src.game;

import java.awt.Graphics;
import java.awt.Color;

public class WalkingEnemy extends GameObject {

    private int speed;
    private int leftBound;
    private int rightBound;
    private int direction; // -1 = left, 1 = right

    public WalkingEnemy(int x, int y, int width, int height, int leftBound, int rightBound, int speed) {
        super(x, y, width, height);

        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.speed = speed;
        this.direction = 1;
    }

    @Override
    public void update(SimpleLevel level) {
        x = x + direction * speed;

        if (x < leftBound) {
            x = leftBound;
            direction = 1;
        } else {
            if (x + width > rightBound) {
                x = rightBound - width;
                direction = -1;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(x, y, width, height);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }
}
