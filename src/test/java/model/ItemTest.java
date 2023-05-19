package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    Item item;

    @BeforeEach
    void init(){
        item = new Item(ItemType.BLUE, new Position(1, 1));
    }
    @Test
    void testMoveTo(){
        item.moveTo(Direction.UP);
        assertEquals(new Position(0,1), item.position());

        item.moveTo(Direction.DOWN);
        assertNotEquals(new Position(0,1), item.position());
        assertEquals(new Position(1,1), item.position());

        item.moveTo(Direction.LEFT);
        assertEquals(new Position(1, 0),item.position());

        item.moveTo(Direction.RIGHT);
        assertEquals(new Position(1,1), item.position());
    }
    @Test
    void testToString() {
        assertEquals("BLUE(1,1)", item.toString());
        assertNotEquals("BLUE(1,2)", item.toString());
    }

}