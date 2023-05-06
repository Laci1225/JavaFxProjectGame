package Model;

public enum ItemType {
    BLUE, RED;

    public ItemType switchColor() {
        return (this == BLUE) ? RED : BLUE;
    }

    public String hexValue() {
        return (this == BLUE) ? "0x0000ffff": "0xff0000ff";
    }
}

