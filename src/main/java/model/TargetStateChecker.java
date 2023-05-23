package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Responsible for checking the target state in a grid of characters.
 * It stores an {@link ItemType} if it is a winner step
 */
@Getter
@AllArgsConstructor
public class TargetStateChecker {
    private ItemType itemType;
    private boolean targetState;

    /**
     * Checks the target state in a grid of characters.
     *
     * @param grid the grid of characters to check
     * @return a new {@link TargetStateChecker} object with the result of the check
     */
    public static TargetStateChecker checkTarget(Character[][] grid) {
        Character blue = ItemType.BLUE.toString().charAt(0);
        Character red = ItemType.RED.toString().charAt(0);
        // Check rows
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                if (grid[i][j].equals(grid[i][j + 1]) && grid[i][j].equals(grid[i][j + 2])) {
                    if (grid[i][j].equals(blue) && grid[i][j + 1].equals(blue) && grid[i][j + 2].equals(blue))
                        return new TargetStateChecker(ItemType.BLUE, true);
                    if (grid[i][j].equals(red) && grid[i][j + 1].equals(red) && grid[i][j + 2].equals(red))
                        return new TargetStateChecker(ItemType.RED, true);
                }
            }
        }
        // Check columns
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (grid[i][j].equals(grid[i + 1][j]) && grid[i][j].equals(grid[i + 2][j])) {
                    if (grid[i][j].equals(blue) && grid[i + 1][j].equals(blue) && grid[i + 2][j].equals(blue))
                        return new TargetStateChecker(ItemType.BLUE, true);
                    else if (grid[i][j].equals(red) && grid[i + 1][j].equals(red) && grid[i + 2][j].equals(red))
                        return new TargetStateChecker(ItemType.RED, true);
                }
            }
        }
        // Check diagonals
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (grid[i][j].equals(grid[i + 1][j + 1]) && grid[i][j].equals(grid[i + 2][j + 2])) {
                    if (grid[i][j].equals(blue) && grid[i + 1][j + 1].equals(blue) && grid[i + 2][j + 2].equals(blue))
                        return new TargetStateChecker(ItemType.BLUE, true);
                    else if (grid[i][j].equals(red) && grid[i + 1][j + 1].equals(red) && grid[i + 2][j + 2].equals(red))
                        return new TargetStateChecker(ItemType.RED, true);
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 2; j < 4; j++) {
                if (grid[i][j].equals(grid[i + 1][j - 1]) && grid[i][j].equals(grid[i + 2][j - 2])) {
                    if (grid[i][j].equals(blue) && grid[i + 1][j - 1].equals(blue) && grid[i + 2][j - 2].equals(blue))
                        return new TargetStateChecker(ItemType.BLUE, true);
                    else if (grid[i][j].equals(red) && grid[i + 1][j - 1].equals(red) && grid[i + 2][j - 2].equals(red))
                        return new TargetStateChecker(ItemType.RED, true);
                }
            }
        }
        return new TargetStateChecker(null, false);
    }
}
