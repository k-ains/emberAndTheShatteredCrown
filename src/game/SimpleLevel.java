package src.game;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class SimpleLevel {

    protected List<Tile> tiles;
    protected List<CheckpointFlag> checkpoints;
    protected List<Spike> spikes;
    protected List<Coin> coins;
    protected List<MessageBox> messageBoxes;
    protected List<WalkingEnemy> enemies;
    protected List<Door> doors;
    protected java.util.List<House> houses;

    protected int worldHeight;
    protected Random random;

    protected int spawnX;
    protected int spawnY;
    protected java.util.List<Plant> plants;
    protected java.util.List<Star> stars;
    protected java.util.List<HeartBox> heartBoxes;
    protected java.util.List<SpinningHazard> spinningHazards;

    

    protected SimpleLevel() {
        tiles = new ArrayList<Tile>();
        checkpoints = new ArrayList<CheckpointFlag>();
        spikes = new ArrayList<Spike>();
        coins = new ArrayList<Coin>();
        messageBoxes = new ArrayList<MessageBox>();
        enemies = new ArrayList<WalkingEnemy>();
        doors = new ArrayList<Door>();
        random = new Random();
        houses = new java.util.ArrayList<House>();

        plants = new java.util.ArrayList<Plant>();
        stars = new java.util.ArrayList<Star>();
        heartBoxes = new java.util.ArrayList<HeartBox>();
        spinningHazards = new java.util.ArrayList<SpinningHazard>();

        spawnX = 100;
        spawnY = GamePanel.HEIGHT - 120;
        worldHeight = GamePanel.HEIGHT;
    }

    // -------- update + draw --------

    public void draw(Graphics g) {
        int i = 0;

        while (i < tiles.size()) {
            Tile t = tiles.get(i);
            t.draw(g);
            i = i + 1;
        }

        i = 0;
        while (i < checkpoints.size()) {
            CheckpointFlag flag = checkpoints.get(i);
            flag.draw(g);
            i = i + 1;
        }

        i = 0;
        while (i < spikes.size()) {
            Spike spike = spikes.get(i);
            spike.draw(g);
            i = i + 1;
        }

        i = 0;
        while (i < coins.size()) {
            Coin coin = coins.get(i);
            coin.draw(g);
            i = i + 1;
        }

        i = 0;
        while (i < messageBoxes.size()) {
            MessageBox box = messageBoxes.get(i);
            box.draw(g);
            i = i + 1;
        }

        i = 0;
        while (i < enemies.size()) {
            WalkingEnemy enemy = enemies.get(i);
            enemy.draw(g);
            i = i + 1;
        }

        i = 0;
        while (i < doors.size()) {
            Door door = doors.get(i);
            door.draw(g);
            i = i + 1;
        }
        i = 0;
        while (i < houses.size()) {
            House h = houses.get(i);
            h.draw(g);
            i = i + 1;
        }

        i = 0;
        while (i < heartBoxes.size()) {
            heartBoxes.get(i).draw(g);
            i = i + 1;
        }

        i = 0;
        while (i < stars.size()) {
            stars.get(i).draw(g);
            i = i + 1;
        }

        i = 0;
        while (i < plants.size()) {
            plants.get(i).draw(g);
            i = i + 1;
        }

        i = 0;
        while (i < spinningHazards.size()) {
            spinningHazards.get(i).draw(g);
            i = i + 1;
        }

    }

    public void update() {
        int i = 0;

        while (i < enemies.size()) {
            WalkingEnemy enemy = enemies.get(i);
            enemy.update(this);
            i = i + 1;
        }

        i = 0;
        while (i < coins.size()) {
            Coin coin = coins.get(i);
            coin.update(this);
            i = i + 1;
        }
         i = 0;
        while (i < plants.size()) {
            plants.get(i).update(this);
            i = i + 1;
        }

        i = 0;
        while (i < stars.size()) {
            stars.get(i).update(this);
            i = i + 1;
        }

        i = 0;
        while (i < tiles.size()) {
            Tile t = tiles.get(i);
            t.update(this);
            i = i + 1;
        }

        i = 0;
        while (i < spinningHazards.size()) {
            spinningHazards.get(i).update(this);
            i = i + 1;
        }

    }
      public void updateWithPlayer(Player player) {
        int i = 0;
        while (i < enemies.size()) {
            WalkingEnemy enemy = enemies.get(i);
            if (enemy instanceof GhostEnemy) {
                GhostEnemy ghost = (GhostEnemy) enemy;
                ghost.setPlayerPosition(player.getX(), player.getY());
            } else if (enemy instanceof Wasp) {
                Wasp wasp = (Wasp) enemy;
                wasp.setPlayerPosition(player.getX(), player.getY());
            }
            enemy.update(this);
            i = i + 1;
        }

        i = 0;
        while (i < coins.size()) {
            Coin coin = coins.get(i);
            coin.update(this);
            i = i + 1;
        }
        
        i = 0;
        while (i < plants.size()) {
            plants.get(i).update(this);
            i = i + 1;
        }

        i = 0;
        while (i < stars.size()) {
            stars.get(i).update(this);
            i = i + 1;
        }

        i = 0;
        while (i < tiles.size()) {
            Tile t = tiles.get(i);
            t.update(this);
            i = i + 1;
        }

        i = 0;
        while (i < spinningHazards.size()) {
            spinningHazards.get(i).update(this);
            i = i + 1;
        }
    }

    // -------- getters --------

    public List<Tile> getTiles() {
        List<Tile> result = tiles;
        return result;
    }

    public List<CheckpointFlag> getCheckpoints() {
        List<CheckpointFlag> result = checkpoints;
        return result;
    }

    public List<Spike> getSpikes() {
        List<Spike> result = spikes;
        return result;
    }

    public List<Coin> getCoins() {
        List<Coin> result = coins;
        return result;
    }

    public List<MessageBox> getMessageBoxes() {
        List<MessageBox> result = messageBoxes;
        return result;
    }

    public List<WalkingEnemy> getEnemies() {
        List<WalkingEnemy> result = enemies;
        return result;
    }

    public List<Door> getDoors() {
        List<Door> result = doors;
        return result;
    }

    public List<SpinningHazard> getSpinningHazards() {
        List<SpinningHazard> result = spinningHazards;
        return result;
    }

    public int getWorldHeight() {
        int value = worldHeight;
        return value;
    }

    public int getSpawnX() {
        int value = spawnX;
        return value;
    }

    public int getSpawnY() {
        int value = spawnY;
        return value;
    }

    public void resetCheckpoints() {
        int i = 0;
        while (i < checkpoints.size()) {
            CheckpointFlag flag = checkpoints.get(i);
            flag.reset();
            i = i + 1;
        }
    }

    public int getPlatformsReachedCount(int playerY) {
        int count = 0;
        int i = 1;

        while (i < tiles.size()) {
            Tile t = tiles.get(i);
            int platformY = t.getY();

            if (playerY <= platformY) {
                count = count + 1;
            }

            i = i + 1;
        }

        int result = count;
        return result;
    }
}
