package Model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameModel {

    public final int ROW_SIZE = 5;
    public final int COL_SIZE = 4;

    protected static Item[] items;

    public Item[] getItems() {
        return items;
    }

    public GameModel() {
        this(
                new Item(ItemType.BLUE, new Position(0, 0)),
                new Item(ItemType.BLUE, new Position(0, 2)),
                new Item(ItemType.BLUE, new Position(4, 1)),
                new Item(ItemType.BLUE, new Position(4, 3)),
                new Item(ItemType.RED, new Position(0, 1)),
                new Item(ItemType.RED, new Position(0, 3)),
                new Item(ItemType.RED, new Position(4, 0)),
                new Item(ItemType.RED, new Position(4, 2))
        );
    }

    public GameModel(Item... items) {
        GameModel.items = items;
    }

    ItemType turn = ItemType.BLUE;

    private Position selectFrom(Position p) {
        if (isOnTable(p) && !isNotOccupied(p)) {
            return p;
        } else throw new IllegalArgumentException("Not an Item");
    }

    private Position selectTo(Position p) {
        if (isOnTable(p) && isNotOccupied(p)) {
            turn = turn.switchColor();
            return p;
        } else throw new IllegalArgumentException("Not an empty Square");
    }


    public void moveItem(Position position, Direction direction) {
        //System.out.println(position);
        var item = Arrays.stream(items).filter(x -> x.position().equals(position)).findFirst();

        if (item.isPresent() && possibleMovement(item.get().position()).contains(position.getPosition(direction))) {
            if (!(selectFrom(position) == item.get().position() && turn == item.get().type()))
                throw new IllegalArgumentException();

            System.out.println("From: " + item.get());

            if (!(possibleMovement(item.get().position()).contains(selectTo(position.getPosition(direction)))))
                throw new IllegalArgumentException();

            System.out.println("To:" + item.get());

            item.get().moveTo(direction);
        } else throw new IllegalArgumentException("Not a step/Not an item");
    }

    public Pair<ItemType, Boolean> checkTargetState() {
        String[][] grid = toStringToTable();
        String blue = ItemType.BLUE.toString().substring(0, 1);
        String red = ItemType.RED.toString().substring(0, 1);
        // Check rows
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                if (grid[i][j].equals(grid[i][j + 1]) && grid[i][j].equals(grid[i][j + 2])) {
                    if (grid[i][j].equals(blue)
                            && grid[i][j + 1].equals(blue)
                            && grid[i][j + 2].equals(blue))
                        return new Pair<>(ItemType.BLUE, true);

                    else if (grid[i][j].equals(grid[i][j + 1]) && grid[i][j].equals(grid[i][j + 2]))
                        if (grid[i][j].equals(red)
                                && grid[i][j + 1].equals(red)
                                && grid[i][j + 2].equals(red))
                            return new Pair<>(ItemType.RED, true);
                }
            }
        }
        // Check columns
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (grid[i][j].equals(grid[i + 1][j]) && grid[i][j].equals(grid[i + 2][j])) {
                    if (grid[i][j].equals(blue) && grid[i + 1][j].equals(blue) && grid[i + 2][j].equals(blue))
                        return new Pair<>(ItemType.BLUE, true);
                    else if (grid[i][j].equals(red) && grid[i + 1][j].equals(red) && grid[i + 2][j].equals(red))
                        return new Pair<>(ItemType.RED, true);
                }
            }
        }
        // Check diagonals
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (grid[i][j].equals(grid[i + 1][j + 1]) && grid[i][j].equals(grid[i + 2][j + 2])) {
                    if (grid[i][j].equals(blue) && grid[i + 1][j + 1].equals(blue) && grid[i + 2][j + 2].equals(blue))
                        return new Pair<>(ItemType.BLUE, true);
                    else if (grid[i][j].equals(red) && grid[i + 1][j + 1].equals(red) && grid[i + 2][j + 2].equals(red))
                        return new Pair<>(ItemType.RED, true);

                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 2; j < 4; j++) {
                if (grid[i][j].equals(grid[i + 1][j - 1]) && grid[i][j].equals(grid[i + 2][j - 2])) {
                    if (grid[i][j].equals(blue) && grid[i + 1][j - 1].equals(blue) && grid[i + 2][j - 2].equals(blue))
                        return new Pair<>(ItemType.BLUE, true);
                    else if (grid[i][j].equals(red) && grid[i + 1][j - 1].equals(red) && grid[i + 2][j - 2].equals(red))
                        return new Pair<>(ItemType.RED, true);
                }
            }
        }
        return new Pair<>(null, false);
    }

    public String[][] toStringToTable() {
        String[] rows = toString().split("\n");
        String[][] stringArray = new String[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            stringArray[i] = rows[i].split(" ");
        }
        return stringArray;
    }

    public List<Position> possibleMovement(Position position) {
        List<Position> possibleMovements = new ArrayList<>();
        if (isOnTable(position.getUp()) && isNotOccupied(position.getUp()))
            possibleMovements.add(position.getUp());
        if (isOnTable(position.getRight()) && isNotOccupied(position.getRight()))
            possibleMovements.add(position.getRight());
        if (isOnTable(position.getDown()) && isNotOccupied(position.getDown()))
            possibleMovements.add(position.getDown());
        if (isOnTable(position.getLeft()) && isNotOccupied(position.getLeft()))
            possibleMovements.add(position.getLeft());

        return possibleMovements;
    }

    public boolean isOnTable(Position position) {
        return (-1 < position.row() && position.row() < ROW_SIZE &&
                -1 < position.col() && position.col() < COL_SIZE);
    }

    public boolean isNotOccupied(Position position) {
        for (var piece : items) {
            if (position.equals(piece.position()))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COL_SIZE; j++) {
                boolean gotColor = false;
                for (Item item : items) {
                    if (item.position().equals(new Position(i, j))) {
                        sb.append(item.type().toString().charAt(0)).append(" ");
                        gotColor = true;
                    }
                }
                if (!gotColor)
                    sb.append("0 ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
