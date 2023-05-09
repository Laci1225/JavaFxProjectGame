package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class OpeningScreenController {

    public void nextStage(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/game.fxml")));
            stage.setScene(new Scene(root));
            stage.setTitle("JavaFX Board Game Example");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
