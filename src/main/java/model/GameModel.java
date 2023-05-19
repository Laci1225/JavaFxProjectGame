package model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class GameModel {

    public final int ROW_SIZE = 5;
    public final int COL_SIZE = 4;

    private final Item[] items;
    private int step = 0;

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
        this.items = items;
    }

    private ItemType turn = ItemType.BLUE;

    public void setTurn(ItemType turn) {
        this.turn = turn;
    }

    private boolean selectFrom;

    public Position selectFrom(Position p) {
        if (isOnTable(p) && !isNotOccupied(p)) {
            selectFrom = true;
            return p;
        } else throw new IllegalArgumentException("Not an Item");
    }

    public Position selectTo(Position p) {
        if (isOnTable(p) && isNotOccupied(p)) {
            turn = turn.switchColor();
            selectFrom = false;
            return p;
        } else throw new IllegalArgumentException("Not an empty Square");
    }


    public void moveItem(Position position, Direction direction) {
        step++;

        var item = Arrays.stream(items)
                .filter(x -> x.position().equals(position))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Item not found at the given position"));

        Position newPosition = selectFrom(position).getPosition(direction);
        if (!possibleMovement(item.position()).contains(newPosition)) {
            throw new IllegalArgumentException("Invalid movement: Not a valid step or item");
        }

        if (turn != item.type()) {
            throw new IllegalArgumentException("Invalid movement: Not the item's turn");
        }

        if (!possibleMovement(item.position()).contains(selectTo(newPosition))) {
            throw new IllegalArgumentException("Invalid movement");
        }

        item.moveTo(direction);
    }

    public TargetStateChecker checkTargetState() {
        Character[][] grid = makeBoard();
        return TargetStateChecker.checkTarget(grid);
    }

    public Character[][] makeBoard() {
        Character[][] board = new Character[ROW_SIZE][COL_SIZE];
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COL_SIZE; j++) {
                boolean gotColor = false;
                for (Item item : items) {
                    if (item.position().equals(new Position(i, j))) {
                        board[i][j] = item.toString().charAt(0);
                        gotColor = true;
                    }
                }
                if (!gotColor)
                    board[i][j] = '0';
            }
        }
        return board;
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
            sb.append(Arrays.toString(makeBoard()[i]));
            if (i < ROW_SIZE - 1)
                sb.append("\n");
        }
        return sb.toString();
    }
}