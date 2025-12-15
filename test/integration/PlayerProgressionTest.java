package test.integration;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import src.game.Player;

/**
 * Integration tests for player progression and game state
 * Tests complex player interactions over time
 */
public class PlayerProgressionTest {

    private Player player;

    @Before
    public void setUp() {
        player = new Player(100, 100);
    }

    // ========== PROGRESSION SYSTEM ==========
    @Test
    public void testCoinAccumulation() {
        // Simulate collecting many coins
        for (int i = 0; i < 100; i++) {
            player.addCoins(1);
        }
        
        assertEquals("Player should accumulate 100 coins", 100, player.getCoins());
    }

    @Test
    public void testHeartSystemProgression() {
        // Start with 3 hearts, max 3
        assertEquals(3, player.getHearts());
        assertEquals(3, player.getMaxHearts());
        
        // Collect heart at full health - should increase max to 4
        player.collectHeartFromBox();
        assertEquals(4, player.getHearts());
        assertEquals(4, player.getMaxHearts());
        
        // Collect again - max to 5
        player.collectHeartFromBox();
        assertEquals(5, player.getHearts());
        assertEquals(5, player.getMaxHearts());
        
        // Max hearts capped at 5
        player.collectHeartFromBox();
        assertTrue("Hearts should not exceed 5", player.getMaxHearts() <= 5);
        assertTrue("Hearts should not exceed max", player.getHearts() <= player.getMaxHearts());
    }

    @Test
    public void testStarCollectionProgression() {
        // Collect stars over time
        for (int i = 1; i <= 10; i++) {
            player.addStar();
            assertEquals("Star count should increase", i, player.getStarsCollected());
        }
    }

    // ========== DEATH AND RESPAWN CYCLE ==========
    @Test
    public void testMultipleDeathsAndRespawns() {
        int maxHearts = player.getMaxHearts();
        
        // Mark dead and respawn manually (avoiding null level)
        for (int i = 0; i < 3; i++) {
            player.markDead();
            player.respawnAtCheckpoint(null);
            assertFalse("Player should respawn", player.isDead());
        }
        
        // Hearts should remain at max
        assertEquals("Hearts remain at max", maxHearts, player.getHearts());
    }

    @Test
    public void testCheckpointProgressionSystem() {
        // Simulate level progression with checkpoints
        int[] checkpointX = {100, 300, 500, 700, 900};
        int[] checkpointY = {100, 150, 200, 250, 300};
        
        for (int i = 0; i < checkpointX.length; i++) {
            player.setCheckpoint(checkpointX[i], checkpointY[i]);
            player.respawnAtCheckpoint(null);
            
            assertEquals("Player at checkpoint " + i + " X", 
                         checkpointX[i], player.getX());
            assertEquals("Player at checkpoint " + i + " Y", 
                         checkpointY[i], player.getY());
        }
    }

    // ========== COMPLETE LEVEL SCENARIO ==========
    @Test
    public void testCompleteLevelPlaythrough() {
        // Starting state
        assertEquals(0, player.getCoins());
        assertEquals(0, player.getStarsCollected());
        assertEquals(3, player.getHearts());
        
        // Checkpoint 1
        player.setCheckpoint(200, 100);
        player.addCoins(10);
        
        // Checkpoint 2
        player.setCheckpoint(400, 100);
        player.addCoins(15);
        player.addStar();
        
        // Take damage (use respawn instead of onDeath)
        player.markDead();
        player.respawnAtCheckpoint(null);
        
        // Should respawn at checkpoint 2 with progress intact
        assertEquals("Coins preserved", 25, player.getCoins());
        assertEquals("Stars preserved", 1, player.getStarsCollected());
        
        // Checkpoint 3
        player.setCheckpoint(600, 100);
        int maxHeartsBefore = player.getMaxHearts();
        player.collectHeartFromBox(); // Increase max hearts
        assertTrue("Max hearts increased", player.getMaxHearts() >= maxHeartsBefore);
        player.addCoins(20);
        player.addStar();
        
        // Final state
        assertEquals("Total coins collected", 45, player.getCoins());
        assertEquals("Total stars collected", 2, player.getStarsCollected());
        assertTrue("Max hearts increased", player.getMaxHearts() > 3);
    }

    // ========== HEALTH MANAGEMENT OVER TIME ==========
    @Test
    public void testHealthManagementCycle() {
        // Full health
        assertEquals(3, player.getHearts());
        
        // Increase max hearts
        player.collectHeartFromBox();
        assertEquals(4, player.getHearts());
        
        // Take damage
        player.markDead();
        player.onDeath(null);
        
        // Heal to full
        player.healToFull();
        assertEquals("Healed to max", player.getMaxHearts(), player.getHearts());
    }

    // ========== POSITION RESET SCENARIOS ==========
    @Test
    public void testRespawnAtStartAfterAllDeaths() {
        // Set checkpoint
        player.setCheckpoint(500, 500);
        
        // Move to checkpoint
        player.respawnAtCheckpoint(null);
        assertEquals(500, player.getX());
        
        // Respawn at start (game over scenario)
        player.respawnAtStart(null);
        assertEquals("Back to start X", 100, player.getX());
        assertEquals("Back to start Y", 100, player.getY());
    }

    @Test
    public void testNewLevelRespawn() {
        // Current level stats
        player.addCoins(50);
        player.addStar();
        
        // Move to new level with new spawn point
        player.respawnForNewLevel(1000, 200);
        
        // Stats should be preserved
        assertEquals("Coins preserved across levels", 50, player.getCoins());
        assertEquals("Stars preserved across levels", 1, player.getStarsCollected());
    }

    // ========== EDGE CASE SCENARIOS ==========
    @Test
    public void testRapidCoinCollection() {
        // Simulate rapid coin collection
        for (int i = 0; i < 1000; i++) {
            player.addCoins(1);
        }
        
        assertEquals("Should handle large coin counts", 1000, player.getCoins());
    }

    @Test
    public void testMaxHeartLimit() {
        // Try to exceed max heart limit
        for (int i = 0; i < 20; i++) {
            player.collectHeartFromBox();
        }
        
        assertTrue("Max hearts capped at 5", player.getMaxHearts() <= 5);
        assertTrue("Current hearts <= max hearts", 
                   player.getHearts() <= player.getMaxHearts());
    }

    @Test
    public void testRepeatedRespawnSameCheckpoint() {
        player.setCheckpoint(300, 400);
        
        // Respawn multiple times
        for (int i = 0; i < 10; i++) {
            player.markDead();
            player.respawnAtCheckpoint(null);
            
            assertEquals("Should respawn at same checkpoint", 300, player.getX());
            assertEquals("Should respawn at same checkpoint", 400, player.getY());
        }
    }
}
