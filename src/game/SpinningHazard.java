package src.game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class SpinningHazard extends GameObject {
    
    private BufferedImage sprite;
    private float rotation;
    private float rotationSpeed;
    
    public SpinningHazard(int x, int y, int w, int h) {
        super(x, y, w, h);
        rotation = 0;
        rotationSpeed = 2.0f;
        sprite = null;
    }
    
    public void setSprite(BufferedImage img) {
        sprite = img;
    }
    
    @Override
    public void update(SimpleLevel level) {
        rotation = rotation + rotationSpeed;
        if (rotation >= 360) {
            rotation = rotation - 360;
        }
    }
    
    @Override
    public void draw(Graphics g) {
        if (sprite == null) {
            return;
        }
        
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform original = g2.getTransform();
        
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int halfW = width / 2;
        int halfH = height / 2;
        
        // Apply main rotation
        g2.rotate(Math.toRadians(rotation), centerX, centerY);
        
        // Quarter 1: Top-left (flip vertical - was bottom-left)
        g2.drawImage(sprite, x, y, x + halfW, y + halfH,
                     0, sprite.getHeight(), sprite.getWidth(), 0, null);
        
        // Quarter 2: Top-right (flip both - was bottom-right)
        g2.drawImage(sprite, x + halfW, y, x + width, y + halfH,
                     sprite.getWidth(), sprite.getHeight(), 0, 0, null);
        
        // Quarter 3: Bottom-right (flip horizontal - was top-right)
        g2.drawImage(sprite, x + halfW, y + halfH, x + width, y + height,
                     sprite.getWidth(), 0, 0, sprite.getHeight(), null);
        
        // Quarter 4: Bottom-left (normal - was top-left)
        g2.drawImage(sprite, x, y + halfH, x + halfW, y + height,
                     0, 0, sprite.getWidth(), sprite.getHeight(), null);
        
        g2.setTransform(original);
    }
    
    public boolean collidesWithPlayer(int px, int py, int pw, int ph) {
        int left = x;
        int right = x + width;
        int top = y;
        int bottom = y + height;
        
        int pleft = px;
        int pright = px + pw;
        int ptop = py;
        int pbottom = py + ph;
        
        boolean collision = !(pright < left || pleft > right || pbottom < top || ptop > bottom);
        return collision;
    }
}
