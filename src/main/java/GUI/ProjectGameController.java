package GUI;


import Model.Direction;
import Model.GameModel;
import Model.ItemType;
import Model.Position;
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
import leaderboard.Leaderboard;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


public class ProjectGameController {

    LocalDateTime startTime = LocalDateTime.now();
    @FXML
    GridPane gameBoard;
    GameModel gameModel = new GameModel();


    private String player1;
    private String player2;

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
            for (var circle : gameBoard.getChildren()) {
                if (circle instanceof Circle &&
                        GridPane.getRowIndex(circle).equals(row) &&
                        GridPane.getColumnIndex(circle).equals(col))
                    node = circle;
            }
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

        handleGameOver();
    }

    private void handleGameOver() {
        if (gameModel.checkTargetState().getValue()) {
            String winner = (gameModel.checkTargetState().getKey().equals(ItemType.BLUE) ? player1 : player2);
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
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        try {
            LocalDateTime finnishTime = LocalDateTime.now();

            var leaderboard = Leaderboard.builder()
                    .start(objectMapper.writeValueAsString(
                            startTime.getYear() + " " + startTime.getMonthValue() + " " +
                                    startTime.getDayOfMonth() + " " + startTime.getHour() + " " +
                                    startTime.getMinute() + " " + startTime.getSecond()))
                    .winner(winner)
                    .step(winnerStep)
                    .end(objectMapper.writeValueAsString(
                            startTime.getYear() + " " + startTime.getMonthValue() + " " +
                                    startTime.getDayOfMonth() + " " + startTime.getHour() + " " +
                                    startTime.getMinute() + " " + startTime.getSecond()))
                    .duration((finnishTime.getHour() * 3600 + finnishTime.getMinute() * 60 + finnishTime.getSecond())
                            - (startTime.getHour() * 3600 + startTime.getMinute() * 60 + startTime.getSecond()))
                    .build();

            List<Leaderboard> a = objectMapper.readValue(new FileReader("src/leaderboard.json"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Leaderboard.class));
            a.add(leaderboard);
            objectMapper.writeValue(new FileWriter("src/leaderboard.json"), a);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void restartGame() {
        OpeningScreenApplication openingScreenApplication = new OpeningScreenApplication();
        try {
            openingScreenApplication.start(new Stage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public void setPlayer1Name(String text) {
        player1 = text;
    }

    public void setPlayer2Name(String text) {
        player2 = text;
    }
}
