package GUI;


import Model.GameModel;
import Model.ItemType;
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
        if (node instanceof Rectangle)
            System.out.println("A rectangle is in (" + row + "," + col + ")");
        else if (node instanceof Circle)
            System.out.println("A circle is in (" + row + "," + col + ")");
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
