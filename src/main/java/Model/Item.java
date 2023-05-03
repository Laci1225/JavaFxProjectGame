package Model;

public record Item(ItemType type, Position position) {


    public void moveTo(Direction direction) {
        position.setTo(direction);
    }

    @Override
    public String toString() {
        return type.toString() + position.toString();
    }

    public static void main(String[] args) {
        Item item = new Item(ItemType.BLUE, new Position(0, 0));
        System.out.println(item);
    }

}

