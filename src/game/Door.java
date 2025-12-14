package src.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Door extends GameObject {

    private String target;
    private BufferedImage sprite;

    public Door(int x, int y, int width, int height, String target) {
        this(x, y, width, height, target, (BufferedImage) null);
    }

    // keeps your old "unused boolean" constructor working
    public Door(int x, int y, int width, int height, String target, boolean unused) {
        this(x, y, width, height, target, (BufferedImage) null);
    }

    // NEW: door with custom sprite (town houses use this)
    public Door(int x, int y, int width, int height, String target, BufferedImage sprite) {
        super(x, y, width, height);
        this.target = target;
        this.sprite = sprite;
    }

    public String getTarget() {
        String value = target;
        return value;
    }

    @Override
    public void update(SimpleLevel level) { }

    @Override
    public void draw(Graphics g) {
        BufferedImage img = sprite;

        if (img == null) {
            img = Assets.door;
        }

        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        } else {
            g.setColor(new Color(150, 80, 40));
            g.fillRect(x, y, width, height);
        }
    }
}
