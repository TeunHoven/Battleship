package view;

import controller.Controller;
import controller.GameState;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.board.Board;
import model.player.HumanPlayer;
import model.player.Player;

public class GameView {
    private Scene scene;
    private Board userBoard, enemyBoard;
    private static Label userLabel, enemyLabel, header, currShip;
    private Font myFont;
    private Player user, opponent;
    private Controller controller;

    private static GameState gameState;

    public GameView() {
        this.user = new HumanPlayer("Teun");
        this.opponent = new HumanPlayer("Tim");

        this.userBoard = new Board(user);
        this.enemyBoard = new Board(opponent);

        this.myFont = new Font(40);

        this.userLabel = new Label(user.getName() + "'s Fleet");
        userLabel.setFont(myFont);
        this.enemyLabel = new Label(opponent.getName() + "'s Fleet");
        enemyLabel.setFont(myFont);

        this.header = new Label("Battleship");
        header.setFont(new Font(50));
        this.currShip = new Label("Ship: none");
        currShip.setFont(new Font(20));

        setView();

        gameState = GameState.SETUP;

        controller = new Controller(userBoard, enemyBoard);
    }

    private void setView() {
        BorderPane root = new BorderPane();
        root.setPrefSize(1280, 720);

        VBox userSide = new VBox(20, userLabel, userBoard);
        userSide.getPrefWidth();
        userSide.setAlignment(Pos.CENTER);

        VBox enemySide = new VBox(20, enemyLabel, enemyBoard);
        enemySide.setAlignment(Pos.CENTER);

        HBox boards = new HBox(100, userSide, enemySide);
        boards.setAlignment(Pos.CENTER);

        HBox ship = new HBox(0, currShip);
        ship.setAlignment(Pos.TOP_LEFT);
        HBox headerBox = new HBox(0, header);
        headerBox.setAlignment(Pos.CENTER);

        VBox topBox = new VBox(10, headerBox, ship);
        topBox.setAlignment(Pos.CENTER);

        root.setTop(topBox);
        root.setCenter(boards);

        scene = new Scene(root);
        scene.setOnKeyPressed(Controller.keyEvent);
    }

    public Scene getScene() {
        return scene;
    }

    public static void setSelectedShip(String name) {
        currShip.setText("Ship: " + name);
    }
}