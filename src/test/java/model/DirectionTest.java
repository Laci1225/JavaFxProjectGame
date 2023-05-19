package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void testOf() {
        assertEquals(Direction.UP, Direction.of(-1, 0));
        assertEquals(Direction.RIGHT, Direction.of(0, 1));
        assertEquals(Direction.DOWN, Direction.of(1, 0));
        assertEquals(Direction.LEFT, Direction.of(0, -1));

        assertSame(Direction.UP, Direction.of(-1, 0));
        assertSame(Direction.RIGHT, Direction.of(0, 1));
        assertSame(Direction.DOWN, Direction.of(1, 0));
        assertSame(Direction.LEFT, Direction.of(0, -1));
    }

    @Test
    void testOf_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Direction.of(0, 0));
    }

}