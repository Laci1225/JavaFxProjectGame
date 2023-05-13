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
import leaderboard.IdAndLeaderboardList;
import leaderboard.Leaderboard;
import lombok.Setter;
import org.tinylog.Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProjectGameController {

    private final LocalDateTime startTime = LocalDateTime.now();
    @FXML
    private GridPane gameBoard;
    private final GameModel gameModel = new GameModel();

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
            /*var circle = gameBoard.getChildren().stream().filter(child -> (child instanceof Circle &&
                    GridPane.getRowIndex(child).equals(row) &&
                    GridPane.getColumnIndex(child).equals(col))).findFirst();
            if (circle.isPresent())
                node = circle.get();*/
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

                gameModel.selectFrom(selected);
                Logger.debug("From: ({},{})", selected.col(), selected.row());

            } else
                Logger.warn("Not its turn hexValue: {} Circle's hexValue: {}",
                        gameModel.getTurn().hexValue(), ((Circle) node).getFill().toString());
        } else if (gameModel.isSelectFrom() && node instanceof Rectangle) {
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
        if (gameModel.checkTargetState().getValue()) {
            Logger.info("{} has won the game", gameModel.checkTargetState().getKey());

            String winner = (gameModel.checkTargetState().getKey().equals(ItemType.BLUE) ? player1Name : player2Name);
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

    private final static String leaderboardFileName = "leaderboard.json";

    private void writeWinnerToJson(String winner, int winnerStep) {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
                .registerModule(new JavaTimeModule());
        LocalDateTime finnishTime = LocalDateTime.now();
        try {
            var idAndList = initIdAndList(objectMapper);
            long id = idAndList.getId();
            List<Leaderboard> leaderboardList = idAndList.getLeaderboard();
            var leaderboard = leaderboardBuilder(id, startTime, winner, winnerStep, finnishTime);
            addLeaderboardToList(leaderboardList, leaderboard, winner, winnerStep);
            var orderedList = orderLeaderboardListByStepThenByDuration(leaderboardList);
            objectMapper.writeValue(new FileWriter(leaderboardFileName), orderedList);
            Logger.info("Winner has written to {}", leaderboardFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Leaderboard> orderLeaderboardListByStepThenByDuration(List<Leaderboard> leaderboardList) {
        return leaderboardList.stream().sorted(Comparator.comparing(
                Leaderboard::getStep).thenComparing(Leaderboard::getDuration)).toList();
    }

    private void addLeaderboardToList(List<Leaderboard> leaderboardList, Leaderboard leaderboard, String winner, int winnerStep) {
        var sameName = leaderboardList.stream().
                filter(leaderboard1 -> leaderboard1.getWinner().equals(winner)).findFirst();
        if (sameName.isPresent()) {
            if (sameName.get().getStep() > winnerStep) {
                leaderboardList.remove(sameName.get());
                leaderboardList.add(leaderboard);
            }
        } else
            leaderboardList.add(leaderboard);
    }

    private IdAndLeaderboardList initIdAndList(ObjectMapper objectMapper) throws IOException {
        long id;
        List<Leaderboard> leaderboardList;
        File file = new File(leaderboardFileName);
        if (file.length() == 0) {
            leaderboardList = new ArrayList<>();
            id = 0L;
        } else {
            leaderboardList = objectMapper.readValue(new FileReader(leaderboardFileName),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Leaderboard.class));
            var maxId = leaderboardList.stream().map(Leaderboard::getId).max(Long::compareTo);
            if (maxId.isPresent()) {
                id = maxId.get() + 1;
            } else throw new IllegalArgumentException("Id not found"); //never happens
        }
        return new IdAndLeaderboardList(id, leaderboardList);
    }

    private Leaderboard leaderboardBuilder(Long id, LocalDateTime startTime, String winner, int winnerStep, LocalDateTime finnishTime) {
        return Leaderboard.builder()
                .id(id)
                .start(startTime.getYear() + " " + startTime.getMonthValue() + " " +
                        startTime.getDayOfMonth() + " " + startTime.getHour() + " " +
                        startTime.getMinute() + " " + startTime.getSecond())
                .winner(winner)
                .step(winnerStep)
                .end(finnishTime.getYear() + " " + finnishTime.getMonthValue() + " " +
                        startTime.getDayOfMonth() + " " + finnishTime.getHour() + " " +
                        finnishTime.getMinute() + " " + finnishTime.getSecond())
                .duration((finnishTime.getHour() * 3600 + finnishTime.getMinute() * 60 + finnishTime.getSecond())
                        - (startTime.getHour() * 3600 + startTime.getMinute() * 60 + startTime.getSecond()))
                .build();
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
        return circle;
    }
}
