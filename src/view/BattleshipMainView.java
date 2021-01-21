package view;

import javafx.application.Application;
import javafx.stage.Stage;

public class BattleshipMainView extends Application{
    private static Stage mainStage;
    GameView game;

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;

        this.game = new GameView();

        mainStage.setScene(game.getScene());
        mainStage.setTitle("Battleship");
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
