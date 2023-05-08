package GUI;


import Model.Direction;
import Model.GameModel;
import Model.ItemType;
import Model.Position;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;


public class ProjectGameController {

    @FXML
    GridPane gameBoard;
    GameModel gameModel = new GameModel();

    @FXML
    private void initialize() {
        initRectangle();
        initCircle();
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        Node node = (Node) event.getSource();
        var row = GridPane.getRowIndex(node);
        var col = GridPane.getColumnIndex(node);
        var selectedPosition = new Position(row, col);
        if (node instanceof Rectangle)
            System.out.println("A rectangle is in (" + row + "," + col + ")");
        else if (node instanceof Circle)
            System.out.println("A circle is in (" + row + "," + col + ")");
        handleClickOnNode(selectedPosition, node);
    }

    Position selected;
    Circle selectedCircle;

    private void handleClickOnNode(Position position, Node node) {

        if (node instanceof Circle) {
            if (gameModel.turn.hexValue().equals(((Circle) node).getFill().toString())) {
                selected = position;
                selectedCircle = (Circle) node;

                selectedCircle.setStroke(Color.BLACK);
                setStrokeWidth(selectedCircle);

                gameModel.selectFrom(selected);
            } else
                System.out.println("Not its turn hV:" + gameModel.turn.hexValue() + "C:" + ((Circle) node).getFill().toString());
        } else if (gameModel.selectFrom && node instanceof Rectangle) {
            if (gameModel.possibleMovement(selected).contains(position)) {
                resetAllStrokeWidthToDefault();
                var direct = Direction.of(position.row() - selected.row(),
                        position.col() - selected.col());

                gameBoard.getChildren().remove(selectedCircle);
                gameBoard.add(selectedCircle, position.col(), position.row());
                gameModel.moveItem(selected, direct);

                if (gameModel.checkTargetState().getValue())
                    System.out.println("Won: " + gameModel.checkTargetState().getKey());
            } else {
                System.out.println("Illegal step");
            }
        }

        //handleGameOver();
    }

    private void resetAllStrokeWidthToDefault() {
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Circle) {
                ((Circle) node).setStrokeWidth(0);
            }
        }
    }

    private void setStrokeWidth(Node node) {
        for (Node child : gameBoard.getChildren()) {
            if (child instanceof Circle) {
                if (((Circle) child).getStrokeWidth() != 0) {
                    resetAllStrokeWidthToDefault();
                    break;
                }
            }
        }
        ((Circle) node).setStrokeWidth(2);
    }


    private void initRectangle() {
        for (int i = 0; i < gameModel.ROW_SIZE; i++) {
            for (int j = 0; j < gameModel.COL_SIZE; j++) {
                Rectangle r = createRectangle();
                r.setWidth(gameBoard.getRowConstraints().get(i).getPrefHeight());
                r.setHeight(gameBoard.getColumnConstraints().get(j).getPrefWidth());
                gameBoard.add(r, i, j);
                GridPane.setRowIndex(r, i);
                GridPane.setColumnIndex(r, j);
            }
        }
    }

    private Rectangle createRectangle() {
        Rectangle rectangle = new Rectangle();
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setFill(Color.WHITE);
        rectangle.setOnMouseClicked(this::handleMouseClick);
        return rectangle;
    }

    private void initCircle() {
        for (int i = 0; i < (gameModel.ROW_SIZE - 1) * 2; i++) {
            Circle circle = createCircle(gameModel.getItems()[i].type());
            var row = gameModel.getItems()[i].position().row();
            var col = gameModel.getItems()[i].position().col();
            gameBoard.add(circle, row, col);

            GridPane.setRowIndex(circle, row);
            GridPane.setColumnIndex(circle, col);
            GridPane.setHalignment(circle, HPos.CENTER);
            GridPane.setValignment(circle, VPos.CENTER);
        }
    }

    private Circle createCircle(ItemType color) {
        Circle circle = new Circle();
        switch (color) {
            case BLUE -> circle.setFill(Color.BLUE);
            case RED -> circle.setFill(Color.RED);
        }
        circle.setRadius(45);
        circle.setOnMouseClicked(this::handleMouseClick);
        //circle.setMouseTransparent(true);
        return circle;
    }

}
