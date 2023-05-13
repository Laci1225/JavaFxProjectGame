package Model;

/**
 * Represents a 2D item.
 */
public record Item(ItemType type, Position position) {

    /**
     * Moves the item in the specified direction.
     *
     * @param direction a direction that specifies a change in the coordinates
     */
    public void moveTo(Direction direction) {
        position.setTo(direction);
    }

    @Override
    public String toString() {
        return type.toString() + position.toString();
    }
}

