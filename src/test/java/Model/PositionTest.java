package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    Position position;

    void assertPosition(int expectedRow, int expectedCol, Position position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.row()),
                () -> assertEquals(expectedCol, position.col())
        );
    }

    @BeforeEach
    void init() {
        position = new Position(1, 1);
    }

    @Test
    void testConstructor() {
        assertEquals(1, position.row());
        assertEquals(1, position.col());
    }

    @Test
    void testGetPosition() {
        assertPosition(0, 1, position.getPosition(Direction.UP));
        assertPosition(1, 2, position.getPosition(Direction.RIGHT));
        assertPosition(2, 1, position.getPosition(Direction.DOWN));
        assertPosition(1, 0, position.getPosition(Direction.LEFT));
    }

    @Test
    void testGetUp() {
        assertPosition(0, 1, position.getUp());
    }

    @Test
    void testGetRight() {
        assertPosition(1, 2, position.getRight());
    }

    @Test
    void testGetDown() {
        assertPosition(2, 1, position.getDown());
    }

    @Test
    void testGetLeft() {
        assertPosition(1, 0, position.getLeft());
    }

    /*@Test
    void testSetTo() {
    }*/
    @Test
    void testSetToUp() {
        position.setTo(Direction.UP);
        assertPosition(0, 1, position);
    }

    @Test
    void testSetToRight() {
        position.setTo(Direction.RIGHT);
        assertPosition(1, 2, position);
    }

    @Test
    void testSetToDown() {
        position.setTo(Direction.DOWN);
        assertPosition(2, 1, position);
    }

    @Test
    void testSetToLeft() {
        position.setTo(Direction.LEFT);
        assertPosition(1, 0, position);
    }

    @Test
    void testSetUp() {
        position.setUp();
        assertPosition(0, 1, position);
    }

    @Test
    void testSetRight() {
        position.setRight();
        assertPosition(1, 2, position);
    }

    @Test
    void testSetDown() {
        position.setDown();
        assertPosition(2, 1, position);
    }

    @Test
    void testSetLeft() {
        position.setLeft();
        assertPosition(1, 0, position);
    }

    @Test
    void testEquals() {
        assertEquals(position, new Position(1, 1));
        assertNotEquals(position, new Position(3, 3));
        assertNotEquals(position, null);
        assertNotEquals(position, new Position(-1, -1));
    }

    @Test
    void testHashCode() {
        assertEquals(position.hashCode(), position.hashCode());
        assertNotEquals(position.hashCode(), new Position(1, 2).hashCode());
    }

    @Test
    void testClone() {
        assertNotSame(position, position.clone());
        assertEquals(position, position.clone());
        assertNotEquals(position,position.getDown().clone());
    }

    @Test
    void testToString() {
        assertEquals("(1,1)", "(" + position.row() + "," + position.col() + ")");
        assertNotEquals("(0,1)", "(" + position.row() + "," + position.col() + ")");
    }

}