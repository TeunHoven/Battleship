package view;

import Protocol.Exceptions.ServerUnavailableException;
import controller.Controller;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class BattleshipMainView extends Application{
    private static Stage mainStage;
    GameView game;
    boolean isOnline = false;
    String name, ip, port;

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;

        mainStage.setScene(setView());
        mainStage.setTitle("Battleship");
        mainStage.show();
    }

    private Scene setView() {
        BorderPane root = new BorderPane();

        Scene scene = new Scene(root);

        Label type = new Label("Offline/Online: ");
        ComboBox online = new ComboBox();
        online.getItems().addAll(
                "Offline",
                "Online"
        );

        Label nameLabel = new Label("Name: ");
        TextField nameField = new TextField("John Doe");
        HBox nameBox = new HBox(nameLabel, nameField);

        Label ipLabel = new Label("Ip Adress: ");
        TextField ipField = new TextField("127.0.0.1");
        HBox ipBox = new HBox(ipLabel, ipField);

        Label portLabel = new Label("Port: ");
        TextField portField = new TextField("5000");
        HBox portBox = new HBox(portLabel, portField);

        if(isOnline) {
            online.setValue("Online");
            online.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue ov, String t, String t1) {
                    isOnline = t1.equals("Online");
                    mainStage.setScene(setView());
                }
            });
            HBox top = new HBox(type, online);

            VBox info = new VBox(nameBox, ipBox, portBox);

            root.setTop(top);
            root.setCenter(info);

        } else {
            online.setValue("Offline");
            online.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue ov, String t, String t1) {
                    isOnline = t1.equals("Online");
                    mainStage.setScene(setView());
                }
            });
            HBox top = new HBox(type, online);

            root.setTop(top);
        }

        Button join = new Button("Create");
        join.setOnAction(e -> {
            name = nameField.getText();
            ip = ipField.getText();
            port = portField.getText();
            isOnline = online.getValue().equals("Online");
            System.out.println(online.getValue());
            Controller.createGame(isOnline, name, ip, port);
            try {
                this.game = new GameView();
            } catch (ServerUnavailableException serverUnavailableException) {
                serverUnavailableException.printStackTrace();
            }
            mainStage.setScene(game.getScene());
        });

        root.setBottom(join);
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
