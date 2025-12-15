package test.game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import src.game.WalkingEnemy;

/**
 * Unit tests for WalkingEnemy class
 * Tests enemy movement, boundaries, and behavior
 */
public class WalkingEnemyTest {

    private WalkingEnemy enemy;

    @Before
    public void setUp() {
        // Create enemy at x=200, with left bound at 100, right bound at 400, speed 2
        enemy = new WalkingEnemy(200, 300, 32, 32, 100, 400, 2);
    }

    // ========== CREATION TESTS ==========
    @Test
    public void testEnemyCreation() {
        assertNotNull("Enemy should be created", enemy);
        assertEquals("Enemy X should be 200", 200, enemy.getX());
        assertEquals("Enemy Y should be 300", 300, enemy.getY());
    }

    @Test
    public void testEnemySize() {
        assertEquals("Enemy width should be 32", 32, enemy.getWidth());
        assertEquals("Enemy height should be 32", 32, enemy.getHeight());
    }

    // ========== MOVEMENT TESTS ==========
    @Test
    public void testEnemyMovesRight() {
        int initialX = enemy.getX();
        
        enemy.update(null);
        
        assertTrue("Enemy should move right initially", enemy.getX() > initialX);
    }

    @Test
    public void testEnemyMovementSpeed() {
        int initialX = enemy.getX();
        
        enemy.update(null);
        
        int expectedX = initialX + 2; // speed is 2
        assertEquals("Enemy should move by speed amount", expectedX, enemy.getX());
    }

    @Test
    public void testEnemyStaysWithinBounds() {
        // Update many times to force boundary collision
        for (int i = 0; i < 200; i++) {
            enemy.update(null);
        }
        
        assertTrue("Enemy X should be >= left bound", enemy.getX() >= 100);
        assertTrue("Enemy X should be <= right bound", enemy.getX() + enemy.getWidth() <= 400);
    }

    @Test
    public void testEnemyReversesAtRightBound() {
        // Move enemy near right bound
        enemy.setX(395);
        
        // Update several times
        int xBeforeBound = enemy.getX();
        for (int i = 0; i < 5; i++) {
            enemy.update(null);
        }
        
        // Should have hit bound and reversed
        assertTrue("Enemy should reverse and move left", enemy.getX() < xBeforeBound);
    }

    @Test
    public void testEnemyReversesAtLeftBound() {
        // Move enemy to left bound and facing left
        enemy.setX(100);
        
        // Force left movement by updating several times until it reverses
        for (int i = 0; i < 10; i++) {
            enemy.update(null);
        }
        
        // Should stay within bounds
        assertTrue("Enemy should not go past left bound", enemy.getX() >= 100);
    }

    // ========== POSITION TESTS ==========
    @Test
    public void testEnemyYPositionUnchanged() {
        int initialY = enemy.getY();
        
        for (int i = 0; i < 20; i++) {
            enemy.update(null);
        }
        
        assertEquals("Enemy Y position should not change", initialY, enemy.getY());
    }

    @Test
    public void testEnemyBounds() {
        assertNotNull("Enemy bounds should not be null", enemy.getBounds());
        assertEquals("Bounds should match enemy dimensions", 
                     enemy.getWidth(), enemy.getBounds().width);
        assertEquals("Bounds should match enemy dimensions", 
                     enemy.getHeight(), enemy.getBounds().height);
    }

    // ========== EDGE CASE TESTS ==========
    @Test
    public void testEnemyWithZeroSpeed() {
        WalkingEnemy slowEnemy = new WalkingEnemy(200, 300, 32, 32, 100, 400, 0);
        int initialX = slowEnemy.getX();
        
        slowEnemy.update(null);
        
        assertEquals("Enemy with 0 speed should not move", initialX, slowEnemy.getX());
    }

    @Test
    public void testEnemyWithHighSpeed() {
        WalkingEnemy fastEnemy = new WalkingEnemy(200, 300, 32, 32, 100, 400, 10);
        int initialX = fastEnemy.getX();
        
        fastEnemy.update(null);
        
        assertEquals("Fast enemy should move by speed amount", 
                     initialX + 10, fastEnemy.getX());
    }

    @Test
    public void testEnemyNarrowPatrolArea() {
        // Enemy with very narrow patrol area
        WalkingEnemy narrowEnemy = new WalkingEnemy(150, 200, 32, 32, 140, 180, 2);
        
        for (int i = 0; i < 50; i++) {
            narrowEnemy.update(null);
            assertTrue("Enemy should stay within narrow bounds", 
                       narrowEnemy.getX() >= 140 && narrowEnemy.getX() + 32 <= 180);
        }
    }
}
