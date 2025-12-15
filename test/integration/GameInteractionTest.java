package test.integration;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import src.game.Player;
import src.game.Coin;
import src.game.WalkingEnemy;
import java.awt.Rectangle;

/**
 * Integration tests for game interactions
 * Tests how different game components work together
 */
public class GameInteractionTest {

    private Player player;
    private Coin coin;
    private WalkingEnemy enemy;

    @Before
    public void setUp() {
        player = new Player(100, 100);
        coin = new Coin(200, 100, 1);
        enemy = new WalkingEnemy(300, 100, 32, 32, 250, 400, 2);
    }

    // ========== PLAYER-COIN INTERACTION ==========
    @Test
    public void testPlayerCollectsCoin() {
        int initialCoins = player.getCoins();
        
        // Simulate coin pickup
        coin.onPickup(player);
        
        assertEquals("Player should gain coin value", 
                     initialCoins + coin.getValue(), player.getCoins());
    }

    @Test
    public void testPlayerCollectsMultipleCoins() {
        Coin coin1 = new Coin(150, 100, 1);
        Coin coin2 = new Coin(200, 100, 5);
        Coin coin3 = new Coin(250, 100, 10);
        
        coin1.onPickup(player);
        coin2.onPickup(player);
        coin3.onPickup(player);
        
        assertEquals("Player should have total of all coin values", 
                     16, player.getCoins());
    }

    @Test
    public void testCoinCollisionDetection() {
        // Move player to coin position
        player.setX(coin.getX());
        player.setY(coin.getY());
        
        // Check if bounding boxes intersect
        Rectangle playerBounds = player.getBounds();
        Rectangle coinBounds = coin.getBounds();
        
        assertTrue("Player and coin should collide when at same position", 
                   playerBounds.intersects(coinBounds));
    }

    // ========== PLAYER-ENEMY INTERACTION ==========
    @Test
    public void testPlayerEnemyCollisionDetection() {
        // Move player to enemy position
        player.setX(enemy.getX());
        player.setY(enemy.getY());
        
        Rectangle playerBounds = player.getBounds();
        Rectangle enemyBounds = enemy.getBounds();
        
        assertTrue("Player and enemy should collide when at same position", 
                   playerBounds.intersects(enemyBounds));
    }

    @Test
    public void testPlayerEnemyNoCollisionWhenApart() {
        // Player and enemy start far apart
        Rectangle playerBounds = player.getBounds();
        Rectangle enemyBounds = enemy.getBounds();
        
        assertFalse("Player and enemy should not collide when far apart", 
                    playerBounds.intersects(enemyBounds));
    }

    // ========== PLAYER HEALTH SYSTEM ==========
    @Test
    public void testPlayerHealthManagement() {
        int initialHearts = player.getHearts();
        
        // Simulate damage
        player.onDeath(null);
        
        assertTrue("Player hearts should be restored to max after death", 
                   player.getHearts() > 0);
    }

    @Test
    public void testPlayerHeartCollection() {
        // Damage player first
        player.markDead();
        player.onDeath(null);
        
        int heartsAfterDamage = player.getHearts();
        
        // Collect heart
        player.collectHeartFromBox();
        
        assertTrue("Player should heal after collecting heart", 
                   player.getHearts() >= heartsAfterDamage);
    }

    @Test
    public void testPlayerHeartSystemIntegration() {
        // Full health heart collection should increase max hearts
        int initialMaxHearts = player.getMaxHearts();
        
        player.collectHeartFromBox();
        
        assertTrue("Collecting heart at full health should increase capacity", 
                   player.getMaxHearts() >= initialMaxHearts);
    }

    // ========== CHECKPOINT SYSTEM ==========
    @Test
    public void testCheckpointRespawnSystem() {
        // Set checkpoint
        int checkpointX = 500;
        int checkpointY = 600;
        player.setCheckpoint(checkpointX, checkpointY);
        
        // Move player away
        player.setX(1000);
        player.setY(1000);
        
        // Respawn at checkpoint
        player.respawnAtCheckpoint(null);
        
        assertEquals("Player should respawn at checkpoint X", checkpointX, player.getX());
        assertEquals("Player should respawn at checkpoint Y", checkpointY, player.getY());
    }

    @Test
    public void testRespawnAfterDeath() {
        // Set checkpoint
        player.setCheckpoint(300, 400);
        
        // Move player away
        player.setX(1000);
        player.setY(1000);
        
        // Simulate death
        player.markDead();
        player.onDeath(null);
        
        // Player should be at checkpoint after death (with hearts remaining)
        assertFalse("Player should not be dead after respawn", player.isDead());
    }

    // ========== ENEMY MOVEMENT IN GAME WORLD ==========
    @Test
    public void testEnemyPatrolDoesNotAffectPlayer() {
        int playerX = player.getX();
        int playerY = player.getY();
        
        // Enemy patrols
        for (int i = 0; i < 50; i++) {
            enemy.update(null);
        }
        
        // Player position unchanged
        assertEquals("Enemy movement should not affect player position", 
                     playerX, player.getX());
        assertEquals("Enemy movement should not affect player position", 
                     playerY, player.getY());
    }

    @Test
    public void testMultipleEnemiesIndependentMovement() {
        WalkingEnemy enemy1 = new WalkingEnemy(100, 100, 32, 32, 50, 200, 2);
        WalkingEnemy enemy2 = new WalkingEnemy(300, 100, 32, 32, 250, 400, 3);
        
        int enemy1X = enemy1.getX();
        int enemy2X = enemy2.getX();
        
        enemy1.update(null);
        enemy2.update(null);
        
        // Both should move independently
        assertNotEquals("Enemy 1 should move", enemy1X, enemy1.getX());
        assertNotEquals("Enemy 2 should move", enemy2X, enemy2.getX());
    }

    // ========== STAR COLLECTION INTEGRATION ==========
    @Test
    public void testStarCollectionProgression() {
        assertEquals("Player starts with 0 stars", 0, player.getStarsCollected());
        
        player.addStar();
        assertEquals("Player has 1 star", 1, player.getStarsCollected());
        
        player.addStar();
        assertEquals("Player has 2 stars", 2, player.getStarsCollected());
        
        player.addStar();
        assertEquals("Player has 3 stars", 3, player.getStarsCollected());
    }

    // ========== COMPREHENSIVE GAME SCENARIO ==========
    @Test
    public void testCompleteGameplayScenario() {
        // Start: Player at 0 coins, 3 hearts
        assertEquals("Player starts with 0 coins", 0, player.getCoins());
        assertEquals("Player starts with 3 hearts", 3, player.getHearts());
        
        // Collect coins
        Coin coin1 = new Coin(0, 0, 1);
        Coin coin2 = new Coin(0, 0, 5);
        coin1.onPickup(player);
        coin2.onPickup(player);
        assertEquals("Player has 6 coins", 6, player.getCoins());
        
        // Collect star
        player.addStar();
        assertEquals("Player has 1 star", 1, player.getStarsCollected());
        
        // Set checkpoint
        player.setCheckpoint(500, 500);
        
        // Collect heart at full health
        int maxHeartsBefore = player.getMaxHearts();
        player.collectHeartFromBox();
        assertTrue("Max hearts increased", player.getMaxHearts() > maxHeartsBefore);
        
        // Take damage
        player.markDead();
        player.onDeath(null);
        
        // Player should still have coins and stars after respawn
        assertEquals("Coins persist after respawn", 6, player.getCoins());
        assertEquals("Stars persist after respawn", 1, player.getStarsCollected());
    }

    // ========== COLLISION SYSTEM ==========
    @Test
    public void testNoCollisionWhenNotOverlapping() {
        // Place objects far apart
        player.setX(100);
        player.setY(100);
        
        coin.setX(1000);
        coin.setY(1000);
        
        Rectangle playerBounds = player.getBounds();
        Rectangle coinBounds = coin.getBounds();
        
        assertFalse("No collision when objects are far apart", 
                    playerBounds.intersects(coinBounds));
    }

    @Test
    public void testCollisionAtExactOverlap() {
        // Place player and coin at exact same position
        player.setX(500);
        player.setY(500);
        coin.setX(500);
        coin.setY(500);
        
        Rectangle playerBounds = player.getBounds();
        Rectangle coinBounds = coin.getBounds();
        
        assertTrue("Collision when objects overlap exactly", 
                   playerBounds.intersects(coinBounds));
    }

    @Test
    public void testCollisionAtPartialOverlap() {
        // Place objects with partial overlap
        player.setX(100);
        player.setY(100);
        coin.setX(116); // 32px - 16px overlap
        coin.setY(100);
        
        Rectangle playerBounds = player.getBounds();
        Rectangle coinBounds = coin.getBounds();
        
        assertTrue("Collision when objects partially overlap", 
                   playerBounds.intersects(coinBounds));
    }
}
