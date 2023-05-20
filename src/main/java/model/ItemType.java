package model;

/**
 * Represents the color of an item in a game.
 */
public enum ItemType {
    BLUE, RED;

    /**
     * Switches the color of the item to the other color.
     *
     * @return the opposite color of this item
     */
    public ItemType switchColor() {
        return (this == BLUE) ? RED : BLUE;
    }

    /**
     * @return the hexadecimal value of this item's color.
     */
    public String hexValue() {
        return (this == BLUE) ? "0x0000ffff" : "0xff0000ff";
    }
}

