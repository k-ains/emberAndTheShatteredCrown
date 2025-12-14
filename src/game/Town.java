package src.game;

public class Town extends SimpleLevel {

    public Town() {
        super();
        buildTown();
    }

   private void buildTown() {
    worldHeight = GamePanel.HEIGHT;

    int floorHeight = 180;
    int floorTopY = worldHeight - floorHeight;

    Tile ground = new Tile(0, floorTopY, GamePanel.WIDTH, floorHeight, true);
    tiles.add(ground);
    ground.setTownTheme(true);   // use town tiles
ground.setTownFloor(true);   // draw the big wall (with bottom corners)
    spawnX = 80;
    spawnY = floorTopY - 32;

    // house width is 288 when SCALE=3
    int houseW = 288;
    int gap = 24;

    int x1 = gap;                     // 24
    int x2 = x1 + houseW + gap;       // 336
    int x3 = x2 + houseW + gap;       // 648

    addHouseBeach(x1, floorTopY);
    addHouseIce(x2, floorTopY);
    addHouseCandy(x3, floorTopY);

    messageBoxes.add(new MessageBox(spawnX + 40, spawnY - 60, true, "Press E at a door"));
}


    private void addHouseBeach(int x, int groundTopY) {
        House h = new House(
            x, groundTopY,
            Assets.beachRoofLeft, Assets.beachRoofMid, Assets.beachRoofRight,
            Assets.beachWindow, Assets.beachDoor, Assets.beachWall,
            "BEACH"
        );
        h.addTilesTo(tiles);
        houses.add(h);
        doors.add(h.getDoor());
    }

    private void addHouseIce(int x, int groundTopY) {
        House h = new House(
            x, groundTopY,
            Assets.iceRoofLeft, Assets.iceRoofMid, Assets.iceRoofRight,
            Assets.iceWindow, Assets.iceDoor, Assets.iceWall,
            "ICE"
        );
        h.addTilesTo(tiles);
        houses.add(h);
        doors.add(h.getDoor());
    }

    private void addHouseCandy(int x, int groundTopY) {
        House h = new House(
            x, groundTopY,
            Assets.candyRoofLeft, Assets.candyRoofMid, Assets.candyRoofRight,
            Assets.candyWindow, Assets.candyDoor, Assets.candyWall,
            "CANDY"
        );
        h.addTilesTo(tiles);
        houses.add(h);
        doors.add(h.getDoor());
    }
}
