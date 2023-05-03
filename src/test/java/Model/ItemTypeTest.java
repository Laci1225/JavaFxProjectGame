package Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTypeTest {
    @Test
    void testSwitchColor() {
        ItemType blue = ItemType.BLUE;
        ItemType switchedToRed = blue.switchColor();
        assertEquals(ItemType.RED, switchedToRed);
        assertNotEquals(ItemType.RED, blue);

        ItemType red = ItemType.RED;
        ItemType switchedToBlue = red.switchColor();
        assertEquals(ItemType.BLUE, switchedToBlue);
        assertNotEquals(ItemType.BLUE, red);
    }
}