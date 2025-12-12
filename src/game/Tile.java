package src.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tile extends GameObject {

    private boolean solid;
    private BufferedImage sprite;   // optional single sprite

    public Tile(int x, int y, int width, int height, boolean solid) {
        super(x, y, width, height);
        this.solid = solid;
        sprite = null;
    }

    // still keep this in case you want specific sprites later
    public Tile(int x, int y, int width, int height, boolean solid, BufferedImage sprite) {
        super(x, y, width, height);
        this.solid = solid;
        this.sprite = sprite;
    }

    @Override
    public void update(SimpleLevel level) { }

    @Override
    public void draw(Graphics g) {
        if (sprite != null) {
            // normal single image (not used for your platforms right now)
            g.drawImage(sprite, x, y, width, height, null);
        } else {
            // if we have our 3 castle pieces, draw segmented
            if (Assets.platformLeft != null &&
                Assets.platformMid != null &&
                Assets.platformRight != null) {

                drawSegmentedPlatform(g);
            } else {
                // fallback: plain gray block
                g.setColor(Color.GRAY);
                g.fillRect(x, y, width, height);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, width, height);
            }
        }
    }

    private void drawSegmentedPlatform(Graphics g) {
        int totalWidth = width;
        int h = height;

        // base width for each piece before repeating middle
        int baseSegmentW = 40; // you can tweak this
        int segW = baseSegmentW;

        // make sure 3 segments can fit; if not, shrink them
        if (segW * 3 > totalWidth) {
            segW = totalWidth / 3;
            if (segW <= 0) {
                // just draw a middle sprite stretched
                g.drawImage(Assets.platformMid, x, y, totalWidth, h, null);
                return;
            }
        }

        int leftW = segW;
        int rightW = segW;

        int leftX = x;
        int rightX = x + totalWidth - rightW;

        int middleStartX = leftX + leftW;
        int middleEndX = rightX;

        // left piece
        g.drawImage(Assets.platformLeft, leftX, y, leftW, h, null);

        // repeated middle pieces
        int curX = middleStartX;
        while (curX < middleEndX) {
            int drawW = segW;
            if (curX + drawW > middleEndX) {
                drawW = middleEndX - curX; // last piece may be smaller
            }
            if (drawW <= 0) {
                break;
            }
            g.drawImage(Assets.platformMid, curX, y, drawW, h, null);
            curX = curX + drawW;
        }

        // right piece
        g.drawImage(Assets.platformRight, rightX, y, rightW, h, null);
    }

    public boolean isSolid() {
        boolean value = solid;
        return value;
    }

    public Rectangle getBounds() {
        Rectangle r = new Rectangle(x, y, width, height);
        return r;
    }
}
