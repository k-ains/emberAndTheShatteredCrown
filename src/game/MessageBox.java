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
        if (important) {
            g.setColor(Color.ORANGE);
        } else {
            g.setColor(Color.YELLOW);
        }
        g.fillRect(x, y, width, height);

        g.setColor(Color.BLACK);
        String symbol;

        if (important) {
            symbol = "!";
        } else {
            symbol = "?";
        }

        g.drawString(symbol, x + 8, y + 16);
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
