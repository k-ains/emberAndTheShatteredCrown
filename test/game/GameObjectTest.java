package test.game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import src.game.GameObject;
import src.game.Coin;
import java.awt.Rectangle;

/**
 * Unit tests for GameObject abstract class
 * Tests basic game object functionality through Coin implementation
 */
public class GameObjectTest {

    private GameObject gameObject;

    @Before
    public void setUp() {
        // Use Coin as concrete implementation of GameObject
        gameObject = new Coin(50, 75, 1);
    }

    @Test
    public void testGetPosition() {
        assertEquals("X position should be 50", 50, gameObject.getX());
        assertEquals("Y position should be 75", 75, gameObject.getY());
    }

    @Test
    public void testSetPosition() {
        gameObject.setX(150);
        gameObject.setY(200);
        
        assertEquals("X should be updated to 150", 150, gameObject.getX());
        assertEquals("Y should be updated to 200", 200, gameObject.getY());
    }

    @Test
    public void testGetSize() {
        assertEquals("Width should be 32", 32, gameObject.getWidth());
        assertEquals("Height should be 32", 32, gameObject.getHeight());
    }

    @Test
    public void testGetBounds() {
        Rectangle bounds = gameObject.getBounds();
        
        assertNotNull("Bounds should not be null", bounds);
        assertEquals("Bounds X should match object X", gameObject.getX(), bounds.x);
        assertEquals("Bounds Y should match object Y", gameObject.getY(), bounds.y);
        assertEquals("Bounds width should match object width", gameObject.getWidth(), bounds.width);
        assertEquals("Bounds height should match object height", gameObject.getHeight(), bounds.height);
    }

    @Test
    public void testBoundsAfterPositionChange() {
        gameObject.setX(300);
        gameObject.setY(400);
        
        Rectangle bounds = gameObject.getBounds();
        
        assertEquals("Bounds should update with position", 300, bounds.x);
        assertEquals("Bounds should update with position", 400, bounds.y);
    }

    @Test
    public void testNegativePosition() {
        gameObject.setX(-50);
        gameObject.setY(-100);
        
        assertEquals("GameObject should handle negative X", -50, gameObject.getX());
        assertEquals("GameObject should handle negative Y", -100, gameObject.getY());
    }
}
