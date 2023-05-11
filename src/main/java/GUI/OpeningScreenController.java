package GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class OpeningScreenController {
    @FXML
    private TextField player1;
    @FXML
    private TextField player2;

    public TextField getPlayer1() {
        return player1;
    }

    public TextField getPlayer2() {
        return player2;
    }

    public void nextStage(ActionEvent actionEvent) {
        if (isPlayersNameGiven()) {
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Not all names are given");
            alert.showAndWait();
        }
    }

    public boolean isPlayersNameGiven() {
        //System.out.println(player1.getText());
        //System.out.println(player2.getText());
        return !(player1.getText().isEmpty() && player2.getText().isEmpty());
    }
}
