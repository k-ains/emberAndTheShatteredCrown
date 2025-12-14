package src.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class House {

    private static final int BASE_TILE = 32;
    private static final int SCALE = 3;
    private static final int TILE = BASE_TILE * SCALE; // 96

    private int x;
    private int groundTopY;

    private BufferedImage roofLeft;
    private BufferedImage roofMid;
    private BufferedImage roofRight;

    private BufferedImage window;
    private BufferedImage doorTile;
    private BufferedImage wall;

    private Door door;
    private String target;

    public House(
        int x, int groundTopY,
        BufferedImage roofLeft, BufferedImage roofMid, BufferedImage roofRight,
        BufferedImage window, BufferedImage doorTile, BufferedImage wall,
        String target
    ) {
        this.x = x;
        this.groundTopY = groundTopY;

        this.roofLeft = roofLeft;
        this.roofMid = roofMid;
        this.roofRight = roofRight;

        this.window = window;
        this.doorTile = doorTile;
        this.wall = wall;

        this.target = target;

        int doorX = x + TILE;
        int doorY = groundTopY - TILE;

        // ✅ use the correct door sprite (beachDoor / iceDoor / candyDoor)
        door = new Door(doorX, doorY, TILE, TILE, target, doorTile);
    }

    public Door getDoor() {
        Door value = door;
        return value;
    }

    public void addTilesTo(List<Tile> tiles) {
        int roofY = groundTopY - TILE * 2;
        int bodyY = groundTopY - TILE;

        addTile(tiles, x + 0 * TILE, roofY, roofLeft);
        addTile(tiles, x + 1 * TILE, roofY, roofMid);
        addTile(tiles, x + 2 * TILE, roofY, roofRight);

        addTile(tiles, x + 0 * TILE, bodyY, window);
        addTile(tiles, x + 1 * TILE, bodyY, doorTile);
        addTile(tiles, x + 2 * TILE, bodyY, wall);
    }

    private void addTile(List<Tile> tiles, int tx, int ty, BufferedImage img) {
        Tile t = new Tile(tx, ty, TILE, TILE, false);
        t.setSprite(img);
        tiles.add(t);
    }

    public void draw(Graphics g) { }
}
