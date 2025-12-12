package src.game;

import java.awt.Graphics;
import java.awt.Color;

public class MessageBox extends GameObject {

    private String message;
    private boolean important;

    public MessageBox(int x, int y, boolean important, String message) {
        super(x, y, 24, 24);
        this.important = important;
        this.message = message;
    }

    @Override
    public void update(SimpleLevel level) {
        // message boxes do not move
    }

   @Override
public void draw(Graphics g) {
    java.awt.image.BufferedImage img = null;

    // important = use exclaim box, otherwise use question box
    if (important && Assets.boxExclaim != null) {
        img = Assets.boxExclaim;
    } else {
        if (!important && Assets.boxQuestion != null) {
            img = Assets.boxQuestion;
        } else {
            if (Assets.boxPlain != null) {
                img = Assets.boxPlain;
            }
        }
    }

    if (img != null) {
        g.drawImage(img, x, y, width, height, null);
    } else {
        // fallback if images fail to load
        g.setColor(java.awt.Color.ORANGE);
        g.fillRect(x, y, width, height);
        g.setColor(java.awt.Color.WHITE);
        g.drawRect(x, y, width, height);
    }
}

    public String getMessage() {
        String value = message;
        return value;
    }

    public boolean isImportant() {
        boolean value = important;
        return value;
    }
}
