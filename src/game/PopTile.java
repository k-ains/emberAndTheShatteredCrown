package src.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class PopTile extends Tile {
    
    private int popTimer;
    private int popDuration;
    private boolean isVisible;
    private int reappearDelay;
    private int reappearTimer;
    
    public PopTile(int x, int y, int w, int h) {
        super(x, y, w, h, true);
        popTimer = 0;
        popDuration = 60;
        reappearDelay = 90;
        reappearTimer = 0;
        isVisible = true;
    }
    
    @Override
    public void update(SimpleLevel level) {
        if (isVisible) {
            popTimer = popTimer + 1;
            if (popTimer >= popDuration) {
                isVisible = false;
                solid = false;
                popTimer = 0;
                reappearTimer = 0;
            }
        } else {
            reappearTimer = reappearTimer + 1;
            if (reappearTimer >= reappearDelay) {
                isVisible = true;
                solid = true;
                reappearTimer = 0;
            }
        }
    }
    
    @Override
    public void draw(Graphics g) {
        if (!isVisible) {
            return;
        }
        
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        }
    }
    
    public boolean getIsVisible() {
        return isVisible;
    }
}
