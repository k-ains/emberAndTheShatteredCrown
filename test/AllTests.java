package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.game.*;
import test.integration.*;

/**
 * Test Suite Runner for Ember and the Shattered Crown
 * Runs all unit tests and integration tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    // Unit Tests
    PlayerTest.class,
    GameObjectTest.class,
    CoinTest.class,
    WalkingEnemyTest.class,
    
    // Integration Tests
    GameInteractionTest.class,
    PlayerProgressionTest.class
})
public class AllTests {
    // This class remains empty, used only as a holder for the above annotations
}
