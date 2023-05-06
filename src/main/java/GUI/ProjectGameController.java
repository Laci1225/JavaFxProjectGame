package GUI;


import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class ProjectGameController {

    @FXML
    Rectangle r00;
    @FXML
    Rectangle r01;
    @FXML
    Rectangle r02;
    @FXML
    Rectangle r03;
    @FXML
    Rectangle r10;
    @FXML
    Rectangle r11;
    @FXML
    Rectangle r12;
    @FXML
    Rectangle r13;
    @FXML
    Rectangle r20;
    @FXML
    Rectangle r21;
    @FXML
    Rectangle r22;
    @FXML
    Rectangle r23;
    @FXML
    Rectangle r30;
    @FXML
    Rectangle r31;
    @FXML
    Rectangle r32;
    @FXML
    Rectangle r33;
    @FXML
    Rectangle r40;
    @FXML
    Rectangle r41;
    @FXML
    Rectangle r42;
    @FXML
    Rectangle r43;


    @FXML
    GridPane gameBoard;
    @FXML
    Circle blue1;
    @FXML
    Circle blue2;
    @FXML
    Circle blue3;
    @FXML
    Circle blue4;
    @FXML
    Circle red1;
    @FXML
    Circle red2;
    @FXML
    Circle red3;
    @FXML
    Circle red4;
    @FXML
    private void initialize() {
        initCircle();
        initSquares();
    }

    private void handleMouseClick(MouseEvent mouseEvent) {
    }

    private void initSquares() {
        Rectangle[] rectangles = new Rectangle[]{r00, r01, r02, r03, r10, r11, r12, r13, r20, r21, r22, r23, r30, r31, r32, r33, r40, r41, r42, r43};
        for (Rectangle rectangle : rectangles) {
            rectangle.setOnMouseClicked(this::handleMouseClick);
        }
    }

    private void initCircle() {

        Circle[] circles = new Circle[]{blue1, blue2, blue3, blue4, red1, red2, red3, red4};
        for (Circle circle : circles) {
            circle.setOnMouseClicked(this::handleMouseClick);
        }
    }

}
