package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.board.Board;

public class GameView {
    private Scene scene;
    private Board userBoard, enemyBoard;
    private Label userLabel, enemyLabel;
    private Label digitsCoor, letterCoor;
    private Font myFont;

    public GameView() {
        this.userBoard = new Board();
        this.enemyBoard = new Board();

        this.myFont = new Font(40);

        this.userLabel = new Label("User's Fleet");
        userLabel.setFont(myFont);
        this.enemyLabel = new Label("Enemy's Fleet");
        enemyLabel.setFont(myFont);

        setView();
    }

    private void setView() {
        BorderPane root = new BorderPane();
        root.setPrefSize(1280, 720);

        VBox userSide = new VBox(50, userLabel, userBoard);
        userSide.setAlignment(Pos.CENTER);

        VBox enemySide = new VBox(50, enemyLabel, enemyBoard);
        enemySide.setAlignment(Pos.CENTER);

        HBox boards = new HBox(100, userSide, enemySide);
        boards.setAlignment(Pos.CENTER);

        root.setCenter(boards);

        scene = new Scene(root);
    }

    public Scene getScene() {
        return scene;
    }
}
