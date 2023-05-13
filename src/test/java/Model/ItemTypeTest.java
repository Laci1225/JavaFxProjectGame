package Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTypeTest {
    @Test
    void testSwitchColor() {
        ItemType blue = ItemType.BLUE;
        ItemType switchedToRed = blue.switchColor();
        ItemType red = ItemType.RED;
        ItemType switchedToBlue = red.switchColor();

        assertEquals(ItemType.RED, switchedToRed);
        assertNotEquals(ItemType.RED, blue);
        assertEquals(ItemType.BLUE, switchedToBlue);
        assertNotEquals(ItemType.BLUE, red);
    }
    @Test
    public void testHexValue() {
        ItemType blueItem = ItemType.BLUE;
        ItemType redItem = ItemType.RED;

        assertEquals(blueItem.hexValue(), "0x0000ffff");
        assertEquals(redItem.hexValue(), "0xff0000ff");
    }
}