package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a game logic to a simple board game.
 */
@Data
public class GameModel {

    public final int ROW_SIZE = 5;
    public final int COL_SIZE = 4;
    private final Item[] items;
    private int step = 0;
    private boolean selected;
    private ItemType turn = ItemType.BLUE;

    /**
     * Constructs a new {@link GameModel} object with default positions.
     */
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

    /**
     * Initialize {@code items} array's items.
     *
     * @param items The initial item positions on the game board
     */
    public GameModel(Item... items) {
        this.items = items;
    }


    /**
     * Set {@link ItemType} color to opposite.
     *
     * @param turn the color wanted to be switched to
     */
    public void setTurn(ItemType turn) {
        this.turn = turn;
    }


    /**
     * Moves an {@link Item} on the game board in the specified direction.
     *
     * @param position  coordinates of the {@link Item} to move
     * @param direction direction in which to move the {@link Item}
     * @throws IllegalStateException    If the item can not be found
     * @throws IllegalArgumentException If the movement is invalid,
     *                                  or if it's not the item's turn
     */
    public void moveItem(Position position, Direction direction) {
        step++;

        var item = Arrays.stream(items)
                .filter(x -> x.position().equals(position))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Item not found at the given position"));

        if (!possibleMovement(item.position()).contains(position.getPosition(direction))) {
            throw new IllegalArgumentException("Invalid movement: Not a valid step or item");
        }

        if (turn != item.type()) {
            throw new IllegalArgumentException("Invalid movement: Not the item's turn");
        }

        item.moveTo(direction);
        setTurn(turn.switchColor());
    }

    /**
     * Checks the possible winner positions of the game.
     *
     * @return A {@link TargetStateChecker} object that represents the outcome of the check
     */
    public TargetStateChecker checkTargetState() {
        Character[][] grid = makeBoard();
        return TargetStateChecker.checkTarget(grid);
    }

    /**
     * Creates a 2D grid representation of the game.
     * Writes {@link ItemType}'s first character, otherwise 0
     * It is a helper method for {@code checkTargetState}
     *
     * @return A 2D character array representing the game
     */
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

    /**
     * Returns a list of possible movements from the given {@link Position}.
     *
     * @param position The position to check
     * @return A list of possible movements
     */
    public List<Position> possibleMovement(Position position) {
        List<Position> possibleMovements = new ArrayList<>();
        if (isOnBoard(position.getUp()) && isNotOccupied(position.getUp()))
            possibleMovements.add(position.getUp());
        if (isOnBoard(position.getRight()) && isNotOccupied(position.getRight()))
            possibleMovements.add(position.getRight());
        if (isOnBoard(position.getDown()) && isNotOccupied(position.getDown()))
            possibleMovements.add(position.getDown());
        if (isOnBoard(position.getLeft()) && isNotOccupied(position.getLeft()))
            possibleMovements.add(position.getLeft());

        return possibleMovements;
    }

    /**
     * Returns if a given position is on the board.
     * @param position The position to check
     * @return whether it is on the board or not
     */
    public boolean isOnBoard(Position position) {
        return (-1 < position.row() && position.row() < ROW_SIZE &&
                -1 < position.col() && position.col() < COL_SIZE);
    }

    /**
     * Returns if a given position is inhabited.
     * @param position The position to check
     * @return whether it is occupied or not
     */
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