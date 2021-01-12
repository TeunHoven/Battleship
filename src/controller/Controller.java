package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;
import model.board.Tile;
import view.GameView;

public class Controller {
    public static EventHandler<MouseEvent> mouseEvent = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            // Left click on the mouse
            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (mouseEvent.getSource() instanceof Tile) { // When a tile is clicked
                    Tile tile = (Tile) mouseEvent.getSource();
                    tileClickedLeftButton(tile);
                }
            }

            // Right click on the mouse
            if(mouseEvent.getButton() == MouseButton.SECONDARY) {
                if (mouseEvent.getSource() instanceof Tile) { // When a tile is clicked
                    Tile tile = (Tile) mouseEvent.getSource();
                    tileClickedRightButton(tile);
                }
            }

//            if(GameView.getGameState() == GameView.GameState.SETUP) {
//                if(mouseEvent.getTarget() instanceof Tile) {
//                    Tile tile = (Tile) mouseEvent.getSource();
//                    Tile[] tiles = tile.getBoard().getTileNeighboursHorizontal(tile, 5);
//
//                    for(Tile t: tiles) {
//                        t.setColor(Color.GRAY);
//                    }
//                }
//            }
        }
    };

    private static void tileClickedLeftButton(Tile tile) {
        tile.setColor(Color.GRAY);
    }

    private static void tileClickedRightButton(Tile tile) {
        tile.setColor(Color.BLUE);
    }
}
