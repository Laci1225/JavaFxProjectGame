package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.Objects;

public class OpeningScreenController {
    @FXML
    private TextField player1;
    @FXML
    private TextField player2;

    public void nextStage(ActionEvent actionEvent) {
        if (isPlayersNameGiven()) {
            setGameStage(actionEvent);
        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Not all names are given");
            alert.showAndWait();
            Logger.warn("Not all names are given");
        }
    }

    private void setGameStage(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource("/game.fxml")));
            Parent root = fxmlLoader.load();
            fxmlLoader.<ProjectGameController>getController().setPlayer1Name(player1.getText());
            fxmlLoader.<ProjectGameController>getController().setPlayer2Name(player2.getText());
            stage.setScene(new Scene(root));
            stage.setTitle("JavaFX Board Game Example");
            stage.show();

            Logger.info("Player 1's name is set to {}", player1.getText());
            Logger.info("Player 2's name is set to {}", player2.getText());
            Logger.info("Loading game scene");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showLeaderboard(ActionEvent actionEvent) {
        LeaderboardController leaderboardController = new LeaderboardController();
        Logger.info("Loading leaderboard");
        leaderboardController.showLeaderboard(actionEvent);
    }

    public void quit(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        Logger.info("Quiting game");
    }

    private boolean isPlayersNameGiven() {
        return !(player1.getText().isEmpty() || player2.getText().isEmpty());
    }
}
