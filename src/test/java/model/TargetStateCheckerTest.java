package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TargetStateCheckerTest {
    @Test
    void testCheckTargetState() {
        GameModel blueCol = new GameModel(
                new Item(ItemType.BLUE, new Position(2, 3)),
                new Item(ItemType.BLUE, new Position(3, 3)),
                new Item(ItemType.BLUE, new Position(4, 1)),
                new Item(ItemType.BLUE, new Position(4, 3)),
                new Item(ItemType.RED, new Position(0, 1)),
                new Item(ItemType.RED, new Position(0, 3)),
                new Item(ItemType.RED, new Position(4, 0)),
                new Item(ItemType.RED, new Position(4, 2)));
        GameModel blueDiag = new GameModel(
                new Item(ItemType.BLUE, new Position(1, 1)),
                new Item(ItemType.BLUE, new Position(2, 2)),
                new Item(ItemType.BLUE, new Position(3, 3)),
                new Item(ItemType.BLUE, new Position(4, 3)),
                new Item(ItemType.RED, new Position(0, 1)),
                new Item(ItemType.RED, new Position(0, 3)),
                new Item(ItemType.RED, new Position(4, 0)),
                new Item(ItemType.RED, new Position(4, 2)));
        GameModel blueReverseDiag = new GameModel(
                new Item(ItemType.BLUE, new Position(1, 3)),
                new Item(ItemType.BLUE, new Position(2, 2)),
                new Item(ItemType.BLUE, new Position(3, 1)),
                new Item(ItemType.BLUE, new Position(4, 3)),
                new Item(ItemType.RED, new Position(0, 1)),
                new Item(ItemType.RED, new Position(0, 3)),
                new Item(ItemType.RED, new Position(4, 0)),
                new Item(ItemType.RED, new Position(4, 2)));
        GameModel blueRow = new GameModel(
                new Item(ItemType.BLUE, new Position(3, 2)),
                new Item(ItemType.BLUE, new Position(3, 3)),
                new Item(ItemType.BLUE, new Position(3, 1)),
                new Item(ItemType.BLUE, new Position(4, 3)),
                new Item(ItemType.RED, new Position(0, 1)),
                new Item(ItemType.RED, new Position(0, 3)),
                new Item(ItemType.RED, new Position(4, 0)),
                new Item(ItemType.RED, new Position(4, 2)));
        GameModel redCol = new GameModel(
                new Item(ItemType.BLUE, new Position(0, 1)),
                new Item(ItemType.BLUE, new Position(0, 3)),
                new Item(ItemType.BLUE, new Position(4, 0)),
                new Item(ItemType.BLUE, new Position(4, 2)),
                new Item(ItemType.RED, new Position(2, 3)),
                new Item(ItemType.RED, new Position(3, 3)),
                new Item(ItemType.RED, new Position(1, 1)),
                new Item(ItemType.RED, new Position(4, 3)));
        GameModel redDiag = new GameModel(
                new Item(ItemType.BLUE, new Position(0, 1)),
                new Item(ItemType.BLUE, new Position(0, 3)),
                new Item(ItemType.BLUE, new Position(4, 0)),
                new Item(ItemType.BLUE, new Position(4, 2)),
                new Item(ItemType.RED, new Position(2, 2)),
                new Item(ItemType.RED, new Position(3, 3)),
                new Item(ItemType.RED, new Position(1, 1)),
                new Item(ItemType.RED, new Position(4, 3)));
        GameModel redReverseDiag = new GameModel(
                new Item(ItemType.BLUE, new Position(0, 1)),
                new Item(ItemType.BLUE, new Position(0, 3)),
                new Item(ItemType.BLUE, new Position(4, 0)),
                new Item(ItemType.BLUE, new Position(4, 2)),
                new Item(ItemType.RED, new Position(2, 2)),
                new Item(ItemType.RED, new Position(3, 1)),
                new Item(ItemType.RED, new Position(1, 3)),
                new Item(ItemType.RED, new Position(4, 3)));
        GameModel redRow = new GameModel(
                new Item(ItemType.BLUE, new Position(0, 1)),
                new Item(ItemType.BLUE, new Position(0, 3)),
                new Item(ItemType.BLUE, new Position(4, 0)),
                new Item(ItemType.BLUE, new Position(4, 2)),
                new Item(ItemType.RED, new Position(3, 2)),
                new Item(ItemType.RED, new Position(3, 3)),
                new Item(ItemType.RED, new Position(3, 1)),
                new Item(ItemType.RED, new Position(4, 3)));
        GameModel notATarget = new GameModel(
                new Item(ItemType.BLUE, new Position(0, 1)),
                new Item(ItemType.BLUE, new Position(0, 3)),
                new Item(ItemType.BLUE, new Position(4, 0)),
                new Item(ItemType.BLUE, new Position(4, 2)),
                new Item(ItemType.RED, new Position(3, 2)),
                new Item(ItemType.RED, new Position(2, 3)),
                new Item(ItemType.RED, new Position(3, 1)),
                new Item(ItemType.RED, new Position(4, 3)));
        TargetStateChecker blueColTarget = blueCol.checkTargetState();
        TargetStateChecker blueDiagTarget = blueDiag.checkTargetState();
        TargetStateChecker blueReverseDiagTarget = blueReverseDiag.checkTargetState();
        TargetStateChecker blueRowTarget = blueRow.checkTargetState();
        TargetStateChecker redColTarget = redCol.checkTargetState();
        TargetStateChecker redDiagTarget = redDiag.checkTargetState();
        TargetStateChecker redReverseDiagTarget = redReverseDiag.checkTargetState();
        TargetStateChecker redRowTarget = redRow.checkTargetState();
        TargetStateChecker notATargetCheck = notATarget.checkTargetState();

        assertTrue(blueColTarget.isTargetState());
        assertNotNull(blueColTarget.getItemType());
        assertTrue(blueDiagTarget.isTargetState());
        assertNotNull(blueDiagTarget.getItemType());
        assertTrue(blueReverseDiagTarget.isTargetState());
        assertNotNull(blueReverseDiagTarget.getItemType());
        assertTrue(blueRowTarget.isTargetState());
        assertNotNull(blueRowTarget.getItemType());
        assertTrue(redColTarget.isTargetState());
        assertNotNull(redColTarget.getItemType());
        assertTrue(redDiagTarget.isTargetState());
        assertNotNull(redDiagTarget.getItemType());
        assertTrue(redReverseDiagTarget.isTargetState());
        assertNotNull(redReverseDiagTarget.getItemType());
        assertTrue(redRowTarget.isTargetState());
        assertNotNull(redRowTarget.getItemType());

        assertNull(notATargetCheck.getItemType());
        assertFalse(notATargetCheck.isTargetState());
    }
}