package view;

import Protocol.Exceptions.ServerUnavailableException;
import controller.Controller;
import controller.GameState;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.GameManager;
import model.board.Board;
import model.player.HumanPlayer;
import model.player.Player;

public class GameView {
    private static Scene scene;
    private static BorderPane root;
    private static Label userLabel, enemyLabel, header, currShip, userPoints, enemyPoints, messageLabel, roundLabel;
    public static Button randomizeButton, readyButton;
    private static VBox topBox;
    private Font myFont;
    private static Controller controller;

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

        userPoints = new Label("Points: " + GameManager.getUser().getPoints());
        userPoints.setFont(new Font(20));
        enemyPoints = new Label("Points: " + GameManager.getOpponent().getPoints());
        enemyPoints.setFont(new Font(20));

        header = new Label("Battleship");
        header.setFont(new Font(50));

        messageLabel = new Label("Press 1-5 to select a ship and click on a tile to place it!");
        messageLabel.setFont(new Font(20));
        messageLabel.setTextFill(Color.RED);

        roundLabel = new Label("Setup");
        roundLabel.setFont(new Font(15));

        VBox userSide = new VBox(20, userLabel, userPoints, GameManager.getUserBoard());
        userSide.getPrefWidth();
        userSide.setAlignment(Pos.CENTER);

        VBox enemySide = new VBox(20, enemyLabel, enemyPoints, GameManager.getOpponentBoard());
        enemySide.setAlignment(Pos.CENTER);

        HBox boards = new HBox(100, userSide, enemySide);
        boards.setAlignment(Pos.CENTER);

        VBox headerBox = new VBox(0, header, messageLabel, roundLabel);
        headerBox.setAlignment(Pos.CENTER);

        roundLabel = new Label("Setup");
        roundLabel.setFont(new Font(15));

        currShip = new Label("Ship: none");
        currShip.setFont(new Font(20));

        randomizeButton = new Button("Randomize placement!");
        randomizeButton.setFont(new Font(12));
        randomizeButton.setOnAction(e -> controller.randomizeButtonClicked());

        readyButton = new Button("Ready");
        readyButton.setFont(new Font(12));
        readyButton.setOnAction(e -> {
            try {
                controller.readyButtonClicked();
            } catch (ServerUnavailableException serverUnavailableException) {
                serverUnavailableException.printStackTrace();
            }
        });
        readyButton.setDisable(true);

        VBox userInfo = new VBox(0, currShip, randomizeButton, readyButton);
        userInfo.setAlignment(Pos.TOP_LEFT);

        topBox = new VBox(10, headerBox, userInfo);
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

    public static void setUserPoints(int points) {
        userPoints.setText("Points: " + points);
    }

    public static void setEnemyPoints(int points) {
        enemyPoints.setText("Points: " + points);
    }

    public static void setMessage(String message) {
        messageLabel.setText(message);
    }

    public static void setRound(String round) {
        roundLabel.setText(round);
    }

    public static void setEnemyLabel() {
        enemyLabel.setText(GameManager.getOpponent().getName() + "'s Fleet");
    }

    public static void setTopBox() {
        Button radar = new Button("Radar");
        radar.setOnAction(e -> controller.radarButtonClicked());
        radar.setAlignment(Pos.CENTER);
        radar.setDisable(true);

        if(GameManager.getRadarUserReady())
            radar.setDisable(false);

        VBox headerBox = new VBox(0, header, messageLabel, roundLabel, radar);
        headerBox.setAlignment(Pos.CENTER);

        topBox = new VBox(10, headerBox);
        root.setTop(topBox);
    }
}