package controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;
import model.board.Board;
import model.board.Tile;
import model.player.Player;
import model.ship.BattleShip;
import model.ship.CarrierShip;
import model.ship.Ship;
import view.GameView;

import java.util.Arrays;

public class Controller {
    private static Board usersBoard, opponentsBoard;
    private static Ship selectedShip;
    private static Tile selectedTile;

    public Controller(Board userBoard, Board opponentBoard) {


        usersBoard = userBoard;
        opponentsBoard = opponentBoard;
        selectedShip = null;
    }

    public static EventHandler<KeyEvent> keyEvent = new EventHandler<>() {

        @Override
        public void handle(KeyEvent keyEvent) {
            if(GameView.getGameState() == GameView.GameState.SETUP) {
                if(keyEvent.getCode() == KeyCode.DIGIT1) {
                    System.out.println("Damn thats fats");
                    if(selectedTile == null) {
                        selectedShip = new CarrierShip(0, 0);
                    } else {
                        selectedShip = new CarrierShip(selectedTile.getXPos(), selectedTile.getYPos());
                    }
                }

                if(keyEvent.getCode() == KeyCode.DIGIT2) {
                    if(selectedTile == null) {
                        selectedShip = new BattleShip(0, 0);
                    } else {
                        selectedShip = new BattleShip(selectedTile.getXPos(), selectedTile.getYPos());
                    }
                }

                GameView.setSelectedShip(selectedShip.getName());
            }
        }
    };

    public static EventHandler<MouseEvent> mouseEvent = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (GameView.getGameState() == GameView.GameState.SETUP) {
                // Left click on the mouse
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    placeShip(selectedTile, selectedShip);
                }

                // Right click on the mouse
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
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
        }
    };

    private static void placeShip(Tile tile, Ship ship) {
        Tile[] neighbourTiles = usersBoard.getTileNeighboursHorizontal(tile, selectedShip.getShipLength());
        boolean validPlacement = !Arrays.asList(neighbourTiles).contains(null);

        if(validPlacement) {
            for (Tile t : neighbourTiles) {
                t.setHasShip(true);
                t.setColor(Color.GRAY);
            }
        }
    }

    private static void tileClickedRightButton(Tile tile) {
        tile.setColor(Color.BLUE);
    }

    // Gets called when mouse hovers a tile
    public static void onHover(Tile tile) {
        selectedTile = tile;

        if(selectedShip != null) {
            Tile[] neighbourTiles = usersBoard.getTileNeighboursHorizontal(tile, selectedShip.getShipLength());
            boolean validPlacement = !Arrays.asList(neighbourTiles).contains(null);

            for (Tile t : usersBoard.getTiles()) {
                if (validPlacement && Arrays.asList(neighbourTiles).contains(t)) {
                    t.setColor(Color.GRAY);
                } else if (!t.hasShip()) {
                    t.setColor(Color.BLUE);
                }
            }
        }
    }
}
