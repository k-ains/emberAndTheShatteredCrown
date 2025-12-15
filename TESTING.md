# Testing Guide for Ember and the Shattered Crown

## Overview
This document explains how to run the tests for your game project.

## Test Structure

### Unit Tests (`test/game/`)
Tests individual components in isolation:
- **PlayerTest.java** - Tests player mechanics (health, coins, respawn, stars)
- **GameObjectTest.java** - Tests base game object functionality
- **CoinTest.java** - Tests coin creation, value, and pickup
- **WalkingEnemyTest.java** - Tests enemy movement and boundaries

### Integration Tests (`test/integration/`)
Tests how components work together:
- **GameInteractionTest.java** - Tests player-coin-enemy interactions, collision, gameplay scenarios
- **PlayerProgressionTest.java** - Tests progression systems, checkpoint cycles, level completion

## Prerequisites

### 1. Install JUnit 4
Download JUnit JAR files:
- junit-4.13.2.jar
- hamcrest-core-1.3.jar

Place them in a `lib/` folder in your project root.

### 2. Project Structure
```
emberAndTheShatteredCrown/
├── src/
│   ├── game/
│   │   ├── Player.java
│   │   ├── Coin.java
│   │   ├── GameObject.java
│   │   └── ...
│   └── assets/
├── test/
│   ├── game/
│   │   ├── PlayerTest.java
│   │   ├── CoinTest.java
│   │   └── ...
│   ├── integration/
│   │   ├── GameInteractionTest.java
│   │   └── PlayerProgressionTest.java
│   └── AllTests.java
└── lib/
    ├── junit-4.13.2.jar
    └── hamcrest-core-1.3.jar
```

## Running Tests

### Option 1: Run All Tests (Command Line)

#### Compile Tests:
```powershell
javac -cp ".;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" -d bin src/game/*.java test/game/*.java test/integration/*.java test/AllTests.java
```

#### Run Test Suite:
```powershell
java -cp ".;bin;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.AllTests
```

### Option 2: Run Individual Test Classes

#### Run PlayerTest:
```powershell
javac -cp ".;lib/junit-4.13.2.jar" -d bin src/game/*.java test/game/PlayerTest.java
java -cp ".;bin;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.game.PlayerTest
```

#### Run GameInteractionTest:
```powershell
javac -cp ".;lib/junit-4.13.2.jar" -d bin src/game/*.java test/integration/GameInteractionTest.java
java -cp ".;bin;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.integration.GameInteractionTest
```

### Option 3: Using VS Code

1. Install "Test Runner for Java" extension
2. Open test file
3. Click "Run Test" button above test methods
4. View results in Test Explorer

### Option 4: Using IntelliJ IDEA

1. Right-click on `test` folder
2. Select "Run 'All Tests'"
3. View results in Run window

## Test Coverage

### What These Tests Cover:

#### Player Mechanics ✓
- Coin collection and accumulation
- Health/heart system (damage, healing, max hearts)
- Star collection
- Death and respawn mechanics
- Checkpoint system
- Position and movement

#### Game Objects ✓
- Bounding boxes and collision detection
- Position management
- Size properties

#### Coins ✓
- Creation and value
- Pickup interaction with player
- Animation updates

#### Enemies ✓
- Patrol movement
- Boundary detection and reversal
- Speed and direction

#### Integration Scenarios ✓
- Player-coin pickup interactions
- Player-enemy collision detection
- Health management cycles
- Checkpoint and respawn systems
- Complete level playthrough scenarios
- Progression across multiple deaths

## Expected Results

When all tests pass, you should see:
```
JUnit version 4.13.2
..................................
Time: 0.XXX

OK (XX tests)
```

If a test fails, you'll see:
```
JUnit version 4.13.2
.....F....
Time: 0.XXX
There was 1 failure:
1) testName(test.game.PlayerTest)
...
```

## Taking Screenshots

### For Execution Results Documentation:

1. **Run the game:**
   ```powershell
   javac -d bin src/game/*.java
   java -cp bin src.game.GameWindow
   ```

2. **Capture these screenshots:**
   - Game window opening
   - Player movement and jumping
   - Coin collection (before/after)
   - Enemy interactions
   - Health bar changes
   - Checkpoint activation
   - Death and respawn
   - Level completion

3. **Run tests and capture:**
   - Test execution in terminal
   - Test results (passed/failed)
   - Coverage reports

## Troubleshooting

### "Cannot find symbol" errors
- Ensure all source files are compiled first
- Check classpath includes JUnit JARs

### "Class not found" errors
- Verify package declarations match folder structure
- Ensure bin folder contains compiled .class files

### Tests fail with null pointer
- Some tests may fail if Assets are not loaded
- These are expected in unit test environment
- Focus on logic tests that don't require graphics

## Next Steps

1. Run the tests to verify game logic
2. Fix any failing tests
3. Capture screenshots of game execution
4. Document results
5. Add more tests for other game objects (Spike, Star, etc.)

## Test Metrics

- **Total Tests:** 60+
- **Unit Tests:** 40+
- **Integration Tests:** 20+
- **Code Coverage:** Core game mechanics (Player, GameObject, Collectibles, Enemies)
