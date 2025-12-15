package test.game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import src.game.Coin;
import src.game.Player;

/**
 * Unit tests for Coin class
 * Tests coin creation, value, and interaction with player
 */
public class CoinTest {

    private Coin coin;
    private Player player;

    @Before
    public void setUp() {
        coin = new Coin(100, 100, 1);
        player = new Player(50, 50);
    }

    // ========== CREATION TESTS ==========
    @Test
    public void testCoinCreation() {
        assertNotNull("Coin should be created", coin);
        assertEquals("Coin X position should be 100", 100, coin.getX());
        assertEquals("Coin Y position should be 100", 100, coin.getY());
    }

    @Test
    public void testCoinSize() {
        assertEquals("Coin width should be 32", 32, coin.getWidth());
        assertEquals("Coin height should be 32", 32, coin.getHeight());
    }

    // ========== VALUE TESTS ==========
    @Test
    public void testCoinValue() {
        assertEquals("Coin value should be 1", 1, coin.getValue());
    }

    @Test
    public void testCoinValueFive() {
        Coin bigCoin = new Coin(200, 200, 5);
        assertEquals("Big coin value should be 5", 5, bigCoin.getValue());
    }

    @Test
    public void testCoinValueTen() {
        Coin megaCoin = new Coin(300, 300, 10);
        assertEquals("Mega coin value should be 10", 10, megaCoin.getValue());
    }

    // ========== PICKUP TESTS ==========
    @Test
    public void testCoinPickup() {
        int initialCoins = player.getCoins();
        
        coin.onPickup(player);
        
        assertEquals("Player should have 1 more coin after pickup", 
                     initialCoins + 1, player.getCoins());
    }

    @Test
    public void testMultipleCoinPickups() {
        Coin coin1 = new Coin(0, 0, 1);
        Coin coin2 = new Coin(0, 0, 1);
        Coin coin3 = new Coin(0, 0, 1);
        
        coin1.onPickup(player);
        coin2.onPickup(player);
        coin3.onPickup(player);
        
        assertEquals("Player should have 3 coins after picking up 3", 
                     3, player.getCoins());
    }

    @Test
    public void testBigCoinPickup() {
        Coin bigCoin = new Coin(0, 0, 5);
        
        bigCoin.onPickup(player);
        
        assertEquals("Player should have 5 coins after picking up big coin", 
                     5, player.getCoins());
    }

    // ========== UPDATE TESTS ==========
    @Test
    public void testCoinUpdateDoesNotMove() {
        int initialX = coin.getX();
        int initialY = coin.getY();
        
        coin.update(null);
        
        assertEquals("Coin X should not change after update", initialX, coin.getX());
        assertEquals("Coin Y should not change after update", initialY, coin.getY());
    }

    @Test
    public void testCoinUpdateMultipleTimes() {
        int initialX = coin.getX();
        int initialY = coin.getY();
        
        for (int i = 0; i < 10; i++) {
            coin.update(null);
        }
        
        assertEquals("Coin should remain stationary", initialX, coin.getX());
        assertEquals("Coin should remain stationary", initialY, coin.getY());
    }
}
