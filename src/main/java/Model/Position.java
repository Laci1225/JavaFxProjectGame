package Model;

import java.util.Objects;

/**
 * Represents a 2D position.
 */
public class Position implements Cloneable {

    private int row;
    private int col;

    /**
     * Creates a {@code Position} object.
     *
     * @param row the row coordinate of the position
     * @param col the column coordinate of the position
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * {@return the row coordinate of the position}
     */
    public int row() {
        return row;
    }

    /**
     * {@return the column coordinate of the position}
     */
    public int col() {
        return col;
    }

    /**
     * {@return the position whose vertical and horizontal distances from this
     * position are equal to the coordinate changes of the direction given}
     *
     * @param direction a direction that specifies a change in the coordinates
     */
    public Position getPosition(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    /**
     * Convenience method that is equivalent to {@code getPosition(Direction.UP)}.
     *
     * @return the position above this position
     */
    public Position getUp() {
        return getPosition(Direction.UP);
    }

    /**
     * Convenience method that is equivalent to {@code getPosition(Direction.RIGHT)}.
     *
     * @return the position to the right of this position
     */
    public Position getRight() {
        return getPosition(Direction.RIGHT);
    }

    /**
     * Convenience method that is equivalent to {@code getPosition(Direction.DOWN)}.
     *
     * @return the position below this position
     */
    public Position getDown() {
        return getPosition(Direction.DOWN);
    }

    /**
     * Convenience method that is equivalent to {@code getPosition(Direction.LEFT)}.
     *
     * @return the position to the left of this position
     */
    public Position getLeft() {
        return getPosition(Direction.LEFT);
    }

    /**
     * Changes the position by the coordinate changes of the direction given.
     *
     * @param direction a direction that specifies a change in the coordinates
     */
    public void setTo(Direction direction) {
        row += direction.getRowChange();
        col += direction.getColChange();
    }

    /**
     * Convenience method that is equivalent to {@code setTo(Direction.UP)}.
     */
    public void setUp() {
        setTo(Direction.UP);
    }

    /**
     * Convenience method that is equivalent to {@code setTo(Direction.RIGHT)}.
     */
    public void setRight() {
        setTo(Direction.RIGHT);
    }

    /**
     * Convenience method that is equivalent to {@code setTo(Direction.DOWN)}.
     */
    public void setDown() {
        setTo(Direction.DOWN);
    }

    /**
     * Convenience method that is equivalent to {@code setTo(Direction.LEFT)}.
     */
    public void setLeft() {
        setTo(Direction.LEFT);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof Position other) && row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public Position clone() {
        Position copy;
        try {
            copy = (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        return copy;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

}
