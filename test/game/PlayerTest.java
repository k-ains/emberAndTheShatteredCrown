package test.game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import src.game.Player;

/**
 * Unit tests for Player class
 * Tests individual player methods and behaviors
 */
public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        // Initialize player before each test
        player = new Player(100, 100);
    }

    // ========== COIN TESTS ==========
    @Test
    public void testInitialCoinsIsZero() {
        assertEquals("Player should start with 0 coins", 0, player.getCoins());
    }

    @Test
    public void testAddCoins() {
        player.addCoins(5);
        assertEquals("Player should have 5 coins after adding 5", 5, player.getCoins());
    }

    @Test
    public void testAddMultipleCoins() {
        player.addCoins(3);
        player.addCoins(7);
        player.addCoins(2);
        assertEquals("Player should have 12 coins after adding 3+7+2", 12, player.getCoins());
    }

    @Test
    public void testAddZeroCoins() {
        player.addCoins(0);
        assertEquals("Player should still have 0 coins after adding 0", 0, player.getCoins());
    }

    // ========== HEALTH/HEARTS TESTS ==========
    @Test
    public void testInitialHearts() {
        assertEquals("Player should start with 3 hearts", 3, player.getHearts());
    }

    @Test
    public void testMaxHearts() {
        assertEquals("Player should start with max 3 hearts", 3, player.getMaxHearts());
    }

    @Test
    public void testCollectHeartWhenDamaged() {
        // Damage player first
        player.onDeath(null);
        int heartsBeforeHeal = player.getHearts();
        
        // Collect heart
        player.collectHeartFromBox();
        
        assertTrue("Hearts should increase after collecting heart box", 
                   player.getHearts() > heartsBeforeHeal);
    }

    @Test
    public void testCollectHeartAtFullHealth() {
        // Player starts at full health (3/3)
        int maxHeartsBeforeCollect = player.getMaxHearts();
        
        player.collectHeartFromBox();
        
        assertEquals("Max hearts should increase when collecting at full health", 
                     maxHeartsBeforeCollect + 1, player.getMaxHearts());
    }

    @Test
    public void testMaxHeartsLimit() {
        // Try to increase max hearts beyond 5
        for (int i = 0; i < 10; i++) {
            player.collectHeartFromBox();
        }
        
        assertTrue("Max hearts should not exceed 5", player.getMaxHearts() <= 5);
    }

    @Test
    public void testHealToFull() {
        // Damage player
        player.onDeath(null);
        
        // Heal to full
        player.healToFull();
        
        assertEquals("Hearts should equal max hearts after healing to full", 
                     player.getMaxHearts(), player.getHearts());
    }

    // ========== DEATH TESTS ==========
    @Test
    public void testPlayerNotDeadInitially() {
        assertFalse("Player should not be dead initially", player.isDead());
    }

    @Test
    public void testMarkDead() {
        player.markDead();
        assertTrue("Player should be dead after marking dead", player.isDead());
    }

    // ========== POSITION TESTS ==========
    @Test
    public void testInitialPosition() {
        assertEquals("Player X should be 100", 100, player.getX());
        assertEquals("Player Y should be 100", 100, player.getY());
    }

    @Test
    public void testPlayerSize() {
        assertEquals("Player width should be 32", 32, player.getWidth());
        assertEquals("Player height should be 32", 32, player.getHeight());
    }

    @Test
    public void testSetPosition() {
        player.setX(200);
        player.setY(300);
        
        assertEquals("Player X should be 200", 200, player.getX());
        assertEquals("Player Y should be 300", 300, player.getY());
    }

    // ========== CHECKPOINT TESTS ==========
    @Test
    public void testSetCheckpoint() {
        player.setCheckpoint(500, 600);
        player.respawnAtCheckpoint(null);
        
        assertEquals("Player should respawn at checkpoint X", 500, player.getX());
        assertEquals("Player should respawn at checkpoint Y", 600, player.getY());
    }

    @Test
    public void testRespawnAtStart() {
        // Move player away from start
        player.setX(500);
        player.setY(600);
        
        // Respawn at start
        player.respawnAtStart(null);
        
        assertEquals("Player should respawn at start X", 100, player.getX());
        assertEquals("Player should respawn at start Y", 100, player.getY());
    }

    // ========== STAR COLLECTION TESTS ==========
    @Test
    public void testInitialStarsCollected() {
        assertEquals("Player should start with 0 stars", 0, player.getStarsCollected());
    }

    @Test
    public void testAddStar() {
        player.addStar();
        assertEquals("Player should have 1 star after collecting", 1, player.getStarsCollected());
    }

    @Test
    public void testAddMultipleStars() {
        player.addStar();
        player.addStar();
        player.addStar();
        assertEquals("Player should have 3 stars after collecting 3", 3, player.getStarsCollected());
    }

    // ========== BOUNDARY TESTS ==========
    @Test
    public void testGetBounds() {
        assertNotNull("Player bounds should not be null", player.getBounds());
        assertEquals("Bounds width should match player width", 
                     player.getWidth(), player.getBounds().width);
        assertEquals("Bounds height should match player height", 
                     player.getHeight(), player.getBounds().height);
    }
}
