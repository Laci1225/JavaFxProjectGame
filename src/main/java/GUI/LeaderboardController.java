package GUI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import leaderboard.Leaderboard;
import leaderboard.LeaderboardHelper;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class LeaderboardController {
    private static final String LEADERBOARD_FILE_NAME = ProjectGameController.getLEADERBOARD_FILE_NAME();
    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(new JavaTimeModule());
    @FXML
    private TableView<Leaderboard> leaderboard;
    @FXML
    private TableColumn<Leaderboard, String> winner;
    @FXML
    private TableColumn<Leaderboard, Integer> step;
    @FXML
    private TableColumn<Leaderboard, String> start;
    @FXML
    private TableColumn<Leaderboard, Integer> duration;

    @FXML
    private void initialize() {
        winner.setCellValueFactory(new PropertyValueFactory<>("winner"));
        step.setCellValueFactory(new PropertyValueFactory<>("step"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        duration.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer duration, boolean empty) {
                super.updateItem(duration, empty);
                if (empty || duration == null) {
                    setText(null);
                } else {
                    setText(getFormattedDuration(duration));
                }
            }
        });
        setLeaderboard();
    }

    private void setLeaderboard() {
        try {
            LeaderboardHelper leaderboardHelper = new LeaderboardHelper();
            leaderboardHelper.initIdAndList(objectMapper, LEADERBOARD_FILE_NAME);
            List<Leaderboard> leaderboardList = leaderboardHelper.getLeaderboardList()
                    .subList(0, Math.min(leaderboardHelper.getLeaderboardList().size(), 10));
            ObservableList<Leaderboard> observableResult = FXCollections.observableArrayList();
            observableResult.addAll(leaderboardList);
            leaderboard.setItems(observableResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showLeaderboard(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource("/leaderboard.fxml")));
            Parent root = fxmlLoader.load();

            stage.setScene(new Scene(root));
            stage.setTitle("Leaderboard");
            stage.show();
            Logger.info("Showing Leaderboard");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void quit(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void showOpeningScene(ActionEvent actionEvent) throws IOException {
        OpeningScreenApplication o = new OpeningScreenApplication();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        o.start(stage);
    }

    public void clearData() throws IOException {
        LeaderboardHelper leaderboardHelper = new LeaderboardHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        leaderboardHelper.clearJson(objectMapper, LEADERBOARD_FILE_NAME);
        initialize();
    }

    private String getFormattedDuration(long duration) {
        int hours = (int) (duration / 3600);
        int minutes = (int) ((duration % 3600) / 60);
        int seconds = (int) (duration % 60);
        return hours + "h " + minutes + "m " + seconds + "s";
    }
}
