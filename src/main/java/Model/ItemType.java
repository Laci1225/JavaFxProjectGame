package Model;

public enum ItemType {
    BLUE, RED;

    public ItemType switchColor() {
        return (this == BLUE) ? RED : BLUE;
    }
}

