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
import model.GameManager;
import model.board.Board;
import model.player.HumanPlayer;
import model.player.Player;

public class GameView {
    private static Scene scene;
    private static BorderPane root;
    private static Label userLabel, enemyLabel, header, currShip, userPoints, enemyPoints;
    private Font myFont;
    private Controller controller;

    public GameView() {
        GameManager.setUp();
        Controller.setUp();

        this.controller = Controller.sharedInstance;

        setView();
    }

    private void setView() {
        root = new BorderPane();
        root.setPrefSize(1280, 720);

        this.myFont = new Font(40);

        userLabel = new Label(GameManager.getUser().getName() + "'s Fleet");
        userLabel.setFont(myFont);
        enemyLabel = new Label(GameManager.getOpponent().getName() + "'s Fleet");
        enemyLabel.setFont(myFont);

        header = new Label("Battleship");
        header.setFont(new Font(50));
        currShip = new Label("Ship: none");
        currShip.setFont(new Font(20));

        VBox userSide = new VBox(20, userLabel, GameManager.getUserBoard());
        userSide.getPrefWidth();
        userSide.setAlignment(Pos.CENTER);

        VBox enemySide = new VBox(20, enemyLabel, GameManager.getOpponentBoard());
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
        scene.setOnKeyPressed(controller.keyEvent);
    }

    public Scene getScene() {
        return scene;
    }

    public static void setSelectedShip(String name) {
        currShip.setText("Ship: " + name);
    }

    public static void setPoints(int points) {

    }
}