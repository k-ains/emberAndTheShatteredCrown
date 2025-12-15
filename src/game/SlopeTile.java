package src.game;

import java.awt.Rectangle;

public class SlopeTile extends Tile {

    private boolean left;

    public SlopeTile(int x, int y, int w, int h, boolean left) {
        super(x, y, w, h, true);
        this.left = left;
    }

    public boolean isLeft() {
        return left;
    }

    public void makeleft( boolean x){
        left = x;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
