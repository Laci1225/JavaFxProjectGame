package GUI;


import lombok.Getter;
import model.Direction;
import model.GameModel;
import model.ItemType;
import model.Position;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import leaderboard.LeaderboardHelper;
import leaderboard.Leaderboard;
import lombok.Setter;
import org.tinylog.Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

public class ProjectGameController {

    private final LocalDateTime startTime = LocalDateTime.now();
    @FXML
    private GridPane gameBoard;
    private final GameModel gameModel = new GameModel();

    @Getter
    private final static String LEADERBOARD_FILE_NAME = "leaderboard.json";

    private Position selected;

    private Circle selectedCircle;

    @Setter
    private String player1Name;
    @Setter
    private String player2Name;

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
        if (node instanceof Rectangle) {
            for (var circle : gameBoard.getChildren()) {
                if (circle instanceof Circle &&
                        GridPane.getRowIndex(circle).equals(row) &&
                        GridPane.getColumnIndex(circle).equals(col)) {
                    node = circle;
                    break;
                }
            }
        }
        handleClickOnNode(selectedPosition, node);
    }

    private void handleClickOnNode(Position position, Node node) {

        if (node instanceof Circle) {
            if (gameModel.getTurn().hexValue().equals(((Circle) node).getFill().toString())) {
                selected = position;
                selectedCircle = (Circle) node;

                selectedCircle.setStroke(Color.BLACK);
                setStrokeWidth(selectedCircle);
                setStrokeForTargetRectangle(gameModel.possibleMovement(position));
                gameModel.setSelected(true);
                //gameModel.selectFrom(selected);
                Logger.debug("From: ({},{})", selected.col(), selected.row());

            } else
                Logger.warn("Not its turn hexValue: {} Circle's hexValue: {}",
                        gameModel.getTurn().hexValue(), ((Circle) node).getFill().toString());
        } else if (gameModel.isSelected() && node instanceof Rectangle) {
            if (gameModel.possibleMovement(selected).contains(position)) {
                resetAllStrokeWidthToDefault();
                var direct = Direction.of(position.row() - selected.row(),
                        position.col() - selected.col());

                gameBoard.getChildren().remove(selectedCircle);
                gameBoard.add(selectedCircle, position.col(), position.row());

                gameModel.moveItem(selected, direct);
                Logger.debug("To: ({},{})", position.col(), position.row());

            } else {
                Logger.error("Illegal step");
            }
        }
        handleGameOver();
    }

    private void handleGameOver() {
        if (gameModel.checkTargetState().isTargetState()) {
            Logger.info("{} has won the game", gameModel.checkTargetState().getItemType());

            String winner = (gameModel.checkTargetState().getItemType().equals(ItemType.BLUE) ? player1Name : player2Name);
            int winnerStep = ((gameModel.getStep() % 2 == 0) ? gameModel.getStep() / 2 : gameModel.getStep() / 2 + 1);
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(winner + " won the game under " + winnerStep + " step");
            alert.showAndWait();
            Stage stage = (Stage) (gameBoard.getScene().getWindow());
            stage.close();
            writeWinnerToJson(winner, winnerStep);
            restartGame();
        }
    }


    private void writeWinnerToJson(String winner, int winnerStep) {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
                .registerModule(new JavaTimeModule());
        LocalDateTime finnishTime = LocalDateTime.now();
        try {
            LeaderboardHelper leaderboardHelper = new LeaderboardHelper();
            leaderboardHelper.initIdAndList(objectMapper, LEADERBOARD_FILE_NAME);
            Leaderboard leaderboard = leaderboardHelper.leaderboardBuilder(startTime, winner, winnerStep, finnishTime);
            leaderboardHelper.addLeaderboardToList(leaderboard);

            var orderedList = leaderboardHelper.orderLeaderboardListByStepThenByDuration();
            objectMapper.writeValue(new FileWriter(LEADERBOARD_FILE_NAME), orderedList);

            Logger.info("Winner has written to {}", LEADERBOARD_FILE_NAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void restartGame() {
        OpeningScreenApplication openingScreenApplication = new OpeningScreenApplication();
        try {
            openingScreenApplication.start(new Stage());
            Logger.info("New game started");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setStrokeForTargetRectangle(List<Position> possibleRectangles) {
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Rectangle && possibleRectangles
                    .contains(new Position(GridPane.getRowIndex(node), GridPane.getColumnIndex(node)))) {
                ((Rectangle) node).setStrokeWidth(2);
            }
        }
    }

    private void resetAllStrokeWidthToDefault() {
        for (Node node : gameBoard.getChildren()) {
            if (node instanceof Circle) {
                ((Circle) node).setStrokeWidth(0);
            }
            if (node instanceof Rectangle)
                ((Rectangle) node).setStrokeWidth(1);
        }
    }

    private void setStrokeWidth(Node node) {
        for (Node child : gameBoard.getChildren()) {
            if (child instanceof Circle) {
                resetAllStrokeWidthToDefault();
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
        return circle;
    }
}
