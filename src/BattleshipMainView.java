import javafx.application.Application;
import javafx.stage.Stage;

public class BattleshipMainView extends Application{
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
