
package src.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tile extends GameObject {

    private boolean solid;
    private BufferedImage sprite;

    // simple flags so we know how to draw
    private boolean townTheme;   // true = use town tiles instead of castle
    private boolean townFloor;   // true = big bottom wall, false = slim platform

    public Tile(int x, int y, int width, int height, boolean solid) {
        super(x, y, width, height);
        this.solid = solid;
        sprite = null;
        townTheme = false;
        townFloor = false;
    }

    public void setSprite(BufferedImage img) {
        sprite = img;
    }

    public void setTownTheme(boolean value) {
        townTheme = value;
    }

    public void setTownFloor(boolean value) {
        townFloor = value;
    }

    public boolean isTownTheme() {
        boolean value = townTheme;
        return value;
    }

    @Override
    public void update(SimpleLevel level) {
        // static
    }

    @Override
    public void draw(Graphics g) {
        if (townTheme) {
            drawTown(g);
        } else {
            drawCastle(g);
        }
    }

    // ================= CASTLE / TOWER DRAW =================

    private void drawCastle(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        } else {
            if (Assets.platformLeft != null &&
                Assets.platformMid  != null &&
                Assets.platformRight != null) {

                drawSegmentedCastlePlatform(g);
            } else {
                g.setColor(Color.GRAY);
                g.fillRect(x, y, width, height);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, width, height);
            }
        }
    }

    private void drawSegmentedCastlePlatform(Graphics g) {
        int totalWidth = width;
        int h = height;

        int baseSegmentW = 40;
        int segW = baseSegmentW;

        if (segW * 3 > totalWidth) {
            segW = totalWidth / 3;
            if (segW <= 0) {
                g.drawImage(Assets.platformMid, x, y, totalWidth, h, null);
                return;
            }
        }

        int leftW  = segW;
        int rightW = segW;

        int leftX  = x;
        int rightX = x + totalWidth - rightW;

        int middleStartX = leftX + leftW;
        int middleEndX   = rightX;

        // left
        g.drawImage(Assets.platformLeft, leftX, y, leftW, h, null);

        // middle pieces
        int curX = middleStartX;
        while (curX < middleEndX) {
            int drawW = segW;
            if (curX + drawW > middleEndX) {
                drawW = middleEndX - curX;
            }
            if (drawW <= 0) {
                curX = middleEndX;
            } else {
                g.drawImage(Assets.platformMid, curX, y, drawW, h, null);
                curX = curX + drawW;
            }
        }

        // right
        g.drawImage(Assets.platformRight, rightX, y, rightW, h, null);
    }

    // ================= TOWN DRAW =================

    private void drawTown(Graphics g) {
        // if town art is missing, fall back so it still works
        if (Assets.townTopMid == null && Assets.townWall == null) {
            drawCastle(g);
            return;
        }

        int tileW = 32;
        int tileH = 32;

        if (Assets.townTopMid != null) {
            tileW = Assets.townTopMid.getWidth();
            tileH = Assets.townTopMid.getHeight();
        } else {
            if (Assets.townWall != null) {
                tileW = Assets.townWall.getWidth();
                tileH = Assets.townWall.getHeight();
            }
        }

        if (townFloor) {
            // big chunky wall at the bottom (with bottom corners)
            drawTownWall(g, tileW, tileH);
        } else {
            // slim platform like tower platforms: left + mid + mid + mid + mid + right
            drawTownPlatformSmall(g, tileW, tileH);
        }
    }

    // big wall for the floor: repeats wall tiles vertically
    private void drawTownWall(Graphics g, int tileW, int tileH) {
        int cols = width / tileW;
        if (width % tileW != 0) {
            cols = cols + 1;
        }

        int rows = height / tileH;
        if (height % tileH != 0) {
            rows = rows + 1;
        }

        int row = 0;
        while (row < rows) {
            int col = 0;
            while (col < cols) {
                BufferedImage img = null;

                // top edge (row 0): 26 / 41 / 49
                if (row == 0) {
                    if (col == 0 && Assets.townTopLeft != null) {
                        img = Assets.townTopLeft;
                    } else {
                        if (col == cols - 1 && Assets.townTopRight != null) {
                            img = Assets.townTopRight;
                        } else {
                            if (Assets.townTopMid != null) {
                                img = Assets.townTopMid;
                            }
                        }
                    }
                } else {
                    // wall body
                    if (Assets.townWall != null) {
                        img = Assets.townWall;
                    }
                }

                int drawX = x + col * tileW;
                int drawY = y + row * tileH;

                // last row – use bottom left/right if you have them
                if (row == rows - 1) {
                    if (col == 0 && Assets.townBotLeft != null) {
                        img = Assets.townBotLeft;
                    } else {
                        if (col == cols - 1 && Assets.townBotRight != null) {
                            img = Assets.townBotRight;
                        }
                    }
                }

                if (img != null) {
                    g.drawImage(img, drawX, drawY, tileW, tileH, null);
                } else {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(drawX, drawY, tileW, tileH);
                }

                col = col + 1;
            }
            row = row + 1;
        }
    }

    // slim town platform: same *feel* as tower platform
    private void drawTownPlatformSmall(Graphics g, int tileW, int tileH) {
        BufferedImage imgLeft  = Assets.townTopLeft  != null ? Assets.townTopLeft  : Assets.townTopMid;
        BufferedImage imgMid   = Assets.townTopMid   != null ? Assets.townTopMid   : Assets.townWall;
        BufferedImage imgRight = Assets.townTopRight != null ? Assets.townTopRight : Assets.townTopMid;

        int segments = 6; // left + 4 mid + right
        int patternWidth = tileW * segments;

        int startX = x;
        if (width > patternWidth) {
            startX = x + (width - patternWidth) / 2;
        }

        int drawY = y + height - tileH; // sit on top of the collision box

        int segIndex = 0;
        while (segIndex < segments) {
            BufferedImage use = imgMid;
            if (segIndex == 0) {
                use = imgLeft;
            } else {
                if (segIndex == segments - 1) {
                    use = imgRight;
                }
            }

            int drawX = startX + segIndex * tileW;
            g.drawImage(use, drawX, drawY, tileW, tileH, null);

            segIndex = segIndex + 1;
        }
    }

    // ================= COLLISION HELPERS =================

    public boolean isSolid() {
        boolean value = solid;
        return value;
    }

    public Rectangle getBounds() {
        Rectangle r = new Rectangle(x, y, width, height);
        return r;
    }
}



