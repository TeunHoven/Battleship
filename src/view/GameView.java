package view;

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
    public enum GameState {
        SETUP,
        USERROUND,
        ENEMYROUND,
        END
    }

    private Scene scene;
    private Board userBoard, enemyBoard;
    private Label userLabel, enemyLabel, header;
    private Font myFont;
    private Player user, opponent;

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

        setView();

        this.gameState = GameState.SETUP;
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

        HBox headerBox = new HBox(0, header);
        headerBox.setAlignment(Pos.CENTER);

        root.setTop(headerBox);
        root.setCenter(boards);

        scene = new Scene(root);
    }

    public Scene getScene() {
        return scene;
    }

    public static GameState getGameState() {
        return gameState;
    }
}