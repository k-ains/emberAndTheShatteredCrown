package src.game;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update(SimpleLevel level);
    public abstract void draw(Graphics g);

    public Rectangle getBounds() {
        Rectangle box = new Rectangle(x, y, width, height);
        return box;
    }

    // getters/setters (encapsulation)
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}
