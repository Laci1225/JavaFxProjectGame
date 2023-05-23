package model;

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
    void testMoveItem() {
        Item item = gameModel.getItems()[0];

        gameModel.moveItem(item.position(), Direction.DOWN);
        assertEquals(new Position(1, 0), item.position());

        gameModel.setTurn(gameModel.getTurn().switchColor());

        gameModel.moveItem(item.position(), Direction.RIGHT);
        assertEquals(new Position(1, 1), item.position());


    }

    @Test
    void testMoveItem_shouldThrowIllegalStateOrArgumentException() {
        assertThrows(IllegalStateException.class, () -> gameModel
                .moveItem(new Item(ItemType.BLUE, new Position(3, 3))
                        .position(), Direction.RIGHT));
        assertThrows(IllegalArgumentException.class, () -> gameModel
                .moveItem(new Item(ItemType.BLUE, new Position(4, 3))
                        .position(), Direction.DOWN));
        assertThrows(IllegalArgumentException.class, () -> {
            Item item = gameModel.getItems()[0];
            gameModel.moveItem(item.position(), Direction.DOWN);
            gameModel.moveItem(item.position(), Direction.RIGHT);
        });
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
        assertFalse(possibleMovements.contains(new Position(4, 5)));
    }

    @Test
    void testMakeBoard() {
        Character[][] expectedBoard = {
                {'B', 'R', 'B', 'R'},
                {'0', '0', '0', '0'},
                {'0', '0', '0', '0'},
                {'0', '0', '0', '0'},
                {'R', 'B', 'R', 'B'}};

        var actualBoard = gameModel.makeBoard();

        assertArrayEquals(actualBoard, expectedBoard);
    }

    @Test
    void testTargetStateChecker() {
        TargetStateCheckerTest test = new TargetStateCheckerTest();
        test.testCheckTargetState();
    }

    @Test
    void testIsOnTable() {
        assertTrue(gameModel.isOnBoard(position));

        position = new Position(-1, 3);
        assertFalse(gameModel.isOnBoard(position));

        position = new Position(-1, -1);
        assertFalse(gameModel.isOnBoard(position));

        position = new Position(5, 3);
        assertFalse(gameModel.isOnBoard(position));

        position = new Position(4, 3);
        assertTrue(gameModel.isOnBoard(position));

        position = new Position(3, 3);
        assertTrue(gameModel.isOnBoard(position));
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
    void testToString() {
        String test = "[B, R, B, R]\n[0, 0, 0, 0]\n[0, 0, 0, 0]\n[0, 0, 0, 0]\n[R, B, R, B]";

        assertEquals(test, gameModel.toString());
    }
}