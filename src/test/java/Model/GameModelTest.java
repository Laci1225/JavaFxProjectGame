package Model;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    GameModel gameModel;
    Position position;

    @BeforeEach
    void init() {
        gameModel = new GameModel();
        position = new Position(0, 0);
    }

    @Test
    void testIsOnTable() {
        assertTrue(gameModel.isOnTable(position));
        position = new Position(-1, 3);
        assertFalse(gameModel.isOnTable(position));
        position = new Position(-1, -1);
        assertFalse(gameModel.isOnTable(position));
        position = new Position(5, 3);
        assertFalse(gameModel.isOnTable(position));
        position = new Position(4, 3);
        assertTrue(gameModel.isOnTable(position));
        position = new Position(3, 3);
        assertTrue(gameModel.isOnTable(position));
    }

    @Test
    void testIsNotOccupied() {
        Item item = new Item(ItemType.BLUE, position);
        assertFalse(gameModel.isNotOccupied(item.position()));
        item.moveTo(Direction.DOWN);
        assertTrue(gameModel.isNotOccupied(item.position()));
        item.moveTo(Direction.RIGHT);
        assertTrue(gameModel.isNotOccupied(item.position()));
        item.moveTo(Direction.UP);
        assertFalse(gameModel.isNotOccupied(item.position()));

        assertFalse(gameModel.isNotOccupied(new Position(4, 0)));
        assertFalse(gameModel.isNotOccupied(new Position(4, 3)));
        assertFalse(gameModel.isNotOccupied(new Position(0, 3)));
    }

    @Test
    void testPossibleMovements() {
        Item item = new Item(ItemType.BLUE, position);
        List<Position> possibleMovements = gameModel.possibleMovement(item.position());
        assertEquals(1, possibleMovements.size());
        assertTrue(possibleMovements.contains(new Position(1, 0)));
        assertFalse(possibleMovements.contains(new Position(0, 1)));
        assertFalse(possibleMovements.contains(new Position(-1, 0)));
        assertFalse(possibleMovements.contains(new Position(-1, -1)));

        //1,0
        item.moveTo(Direction.DOWN);
        possibleMovements = gameModel.possibleMovement(item.position());
        assertEquals(2, possibleMovements.size());
        assertTrue(possibleMovements.contains(new Position(1, 1)));
        assertTrue(possibleMovements.contains(new Position(2, 0)));
        assertFalse(possibleMovements.contains(new Position(1, 0)));
        assertFalse(possibleMovements.contains(new Position(-1, 0)));

        //1,1
        item.moveTo(Direction.RIGHT);
        possibleMovements = gameModel.possibleMovement(item.position());
        assertEquals(3, possibleMovements.size());
        assertTrue(possibleMovements.contains(new Position(1, 0)));
        assertTrue(possibleMovements.contains(new Position(2, 1)));
        assertTrue(possibleMovements.contains(new Position(2, 1)));
        assertFalse(possibleMovements.contains(new Position(1, 1)));
        assertFalse(possibleMovements.contains(new Position(0, 1)));

        //2,1
        item.moveTo(Direction.DOWN);
        possibleMovements = gameModel.possibleMovement(item.position());
        assertEquals(4, possibleMovements.size());
        assertTrue(possibleMovements.contains(new Position(1, 1)));
        assertTrue(possibleMovements.contains(new Position(2, 2)));
        assertTrue(possibleMovements.contains(new Position(3, 1)));
        assertTrue(possibleMovements.contains(new Position(2, 0)));
        assertFalse(possibleMovements.contains(new Position(2, 1)));
    }
    @Test
    void testToStringToTable() {
        String[][] grid = gameModel.toStringToTable();
        assertNotNull(grid);
        assertEquals(5, grid.length);
        assertEquals(4, grid[0].length);
        assertEquals("B", grid[0][0]);
        assertEquals("B", grid[4][3]);
    }

    @Test
    void testCheckTargetState(){
        Pair<ItemType, Boolean> state1 = gameModel.checkTargetState();
        assertNull(state1.getKey());
        assertFalse(state1.getValue());

    }
}