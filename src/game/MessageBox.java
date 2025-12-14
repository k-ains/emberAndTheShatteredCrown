package src.game;

import java.awt.Graphics;

public class MessageBox extends GameObject {

    private String message;
    private boolean important;

    public MessageBox(int x, int y, boolean important, String message) {
        super(x, y, 32, 32);
        this.important = important;
        this.message = message;
    }

    @Override
    public void update(SimpleLevel level) { }

    @Override
    public void draw(Graphics g) {
        java.awt.image.BufferedImage img = null;

        if (important && Assets.boxExclaim != null) {
            img = Assets.boxExclaim;
        } else {
            if (!important && Assets.boxQuestion != null) {
                img = Assets.boxQuestion;
            } else {
                img = Assets.boxEmpty;
            }
        }

        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        } else {
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
}
