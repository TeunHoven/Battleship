package controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;
import model.GameManager;
import model.board.Board;
import model.board.Tile;
import model.player.Player;
import model.ship.*;
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
            switch(GameManager.getGameState()) {
                case SETUP:
                    setUpKeys(keyEvent);
                    break;

                case USERROUND:
                    userRoundKeys(keyEvent);
                    break;

                case ENEMYROUND:
                    enemyRoundKeys(keyEvent);
                    break;

                case END:
                    endRoundKeys(keyEvent);
                    break;

                default:
                    break;
            }
        }
    };

    public static EventHandler<MouseEvent> mouseEvent = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            switch(GameManager.getGameState()) {
                case SETUP:
                    setUpMouse(mouseEvent);
                    break;

                case USERROUND:
                    userRoundMouse(mouseEvent);
                    break;

                case ENEMYROUND:
                    enemyRoundMouse(mouseEvent);
                    break;

                case END:
                    endRoundMouse(mouseEvent);
                    break;

                default:
                    break;
            }
        }
    };

    private static void placeShip(Tile tile, Ship ship) {
        Tile[] neighbourTiles = usersBoard.getTileNeighboursHorizontal(tile, selectedShip.getShipLength());
        boolean validPlacement = !Arrays.asList(neighbourTiles).contains(null);

        if(validPlacement) {
            if(selectedShip instanceof PatrolBoatShip) {
                if(GameManager.canAddPatrolBoat()) {
                    for (Tile t : neighbourTiles) {
                        t.setHasShip(true);
                        t.setColor(Color.GRAY);
                    }
                }
            }

            if(selectedShip instanceof SuperPatrolShip) {
                if(GameManager.canAddSuperPatrol()) {
                    for (Tile t : neighbourTiles) {
                        t.setHasShip(true);
                        t.setColor(Color.GRAY);
                    }
                }
            }

            if(selectedShip instanceof DestroyerShip) {
                if(GameManager.canAddDestroyerShips()) {
                    for (Tile t : neighbourTiles) {
                        t.setHasShip(true);
                        t.setColor(Color.GRAY);
                    }
                }
            }

            if(selectedShip instanceof BattleShip) {
                if(GameManager.canAddBattleshipShips()) {
                    for (Tile t : neighbourTiles) {
                        t.setHasShip(true);
                        t.setColor(Color.GRAY);
                    }
                }
            }

            if(selectedShip instanceof CarrierShip) {
                if(GameManager.canAddCarrierShips()) {
                    for (Tile t : neighbourTiles) {
                        t.setHasShip(true);
                        t.setColor(Color.GRAY);
                    }
                }
            }

            if(selectedShip instanceof PatrolBoatShip) {
                if(GameManager.canAddPatrolBoat()) {
                    for (Tile t : neighbourTiles) {
                        t.setHasShip(true);
                        t.setColor(Color.GRAY);
                    }
                }
            }
        }
    }

    private static void tileClickedRightButton(Tile tile) {
        tile.setColor(Color.BLUE);
    }

    // Gets called when mouse hovers a tile
    public static void onHover(Tile tile) {
        selectedTile = tile;

        switch(GameManager.getGameState()) {
            case SETUP:
                setUpHover();
                break;

            case USERROUND:
                userRoundHover();
                break;

            case ENEMYROUND:
                enemyRoundHover();
                break;

            case END:
                endRoundHover();
                break;

            default:
                break;
        }
    }

    ////////////////////////////////////////////////////////
    /////////////// MOUSE CONTROLLER //////////////////////
    //////////////////////////////////////////////////////

    // All mouse events necessary for the SETUP game state
    private static void setUpMouse(MouseEvent event) {
        // Left click on the mouse
        if (event.getButton() == MouseButton.PRIMARY) {
            placeShip(selectedTile, selectedShip);
        }

        // Right click on the mouse
        if (event.getButton() == MouseButton.SECONDARY) {
            if (event.getSource() instanceof Tile) { // When a tile is clicked
                Tile tile = (Tile) event.getSource();
                tileClickedRightButton(tile);
            }
        }
    }

    // All mouse events necessary for the USERROUND game state
    private static void userRoundMouse(MouseEvent event) {

    }

    // All mouse events necessary for the ENEMYROUND game state
    private static void enemyRoundMouse(MouseEvent event) {

    }

    // All mouse events necessary for the END game state
    private static void endRoundMouse(MouseEvent event) {

    }

    ////////////////////////////////////////////////////////
    ///////////////// KEY CONTROLLER //////////////////////
    //////////////////////////////////////////////////////

    // All keys events necessary for the SETUP game state
    private static void setUpKeys(KeyEvent event) {
        if(event.getCode() == KeyCode.E) {
            usersBoard.setHorizontal(!usersBoard.isHorizontal());
        }

        // When key 1 is pressed, set ship to Patrol Boat
        if(event.getCode() == KeyCode.DIGIT1) {
            if(selectedTile == null) {
                selectedShip = new PatrolBoatShip(0, 0);
            } else {
                selectedShip = new PatrolBoatShip(selectedTile.getXPos(), selectedTile.getYPos());
            }
            int shipsLeftover = GameManager.getPatrolBoatShips()[0] - GameManager.getPatrolBoatShips()[1];

            GameView.setSelectedShip(selectedShip.getName() + " " + shipsLeftover + "x");
        }

        // When key 2 is pressed, set ship to Super Patrol
        if(event.getCode() == KeyCode.DIGIT2) {
            if(selectedTile == null) {
                selectedShip = new SuperPatrolShip(0, 0);
            } else {
                selectedShip = new SuperPatrolShip(selectedTile.getXPos(), selectedTile.getYPos());
            }
            int shipsLeftover = GameManager.getSuperPatrolShips()[0] - GameManager.getSuperPatrolShips()[1];

            GameView.setSelectedShip(selectedShip.getName() + " " + shipsLeftover + "x");
        }

        // When key 3 is pressed, set ship to Destroyer
        if(event.getCode() == KeyCode.DIGIT3) {
            if(selectedTile == null) {
                selectedShip = new DestroyerShip(0, 0);
            } else {
                selectedShip = new DestroyerShip(selectedTile.getXPos(), selectedTile.getYPos());
            }
            int shipsLeftover = GameManager.getDestroyerShips()[0] - GameManager.getDestroyerShips()[1];

            GameView.setSelectedShip(selectedShip.getName() + " " + shipsLeftover + "x");
        }

        // When key 4 is pressed, set ship to Battleship
        if(event.getCode() == KeyCode.DIGIT4) {
            if(selectedTile == null) {
                selectedShip = new BattleShip(0, 0);
            } else {
                selectedShip = new BattleShip(selectedTile.getXPos(), selectedTile.getYPos());
            }
            int shipsLeftover = GameManager.getBattleshipShips()[0] - GameManager.getBattleshipShips()[1];

            GameView.setSelectedShip(selectedShip.getName() + " " + shipsLeftover + "x");
        }

        // When key 5 is pressed, set ship to Carrier
        if(event.getCode() == KeyCode.DIGIT5) {
            if(selectedTile == null) {
                selectedShip = new CarrierShip(0, 0);
            } else {
                selectedShip = new CarrierShip(selectedTile.getXPos(), selectedTile.getYPos());
            }
            int shipsLeftover = GameManager.getCarrierShips()[0] - GameManager.getCarrierShips()[1];

            GameView.setSelectedShip(selectedShip.getName() + " " + shipsLeftover + "x");
        }
    }

    // All keys events necessary for the USERROUND game state
    private static void userRoundKeys(KeyEvent event) {

    }

    // All keys events necessary for the ENEMYROUND game state
    private static void enemyRoundKeys(KeyEvent event) {

    }

    // All keys events necessary for the END game state
    private static void endRoundKeys(KeyEvent event) {

    }

    ////////////////////////////////////////////////////////
    ///////////////// HOVER CONTROLLER ////////////////////
    //////////////////////////////////////////////////////

    // All hover events necessary for the SETUP game state
    private static void setUpHover() {
        if(selectedShip != null) {
            Tile[] neighbourTiles = usersBoard.getTileNeighboursHorizontal(selectedTile, selectedShip.getShipLength());
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

    // All hover events necessary for the USERROUND game state
    private static void userRoundHover() {
        for (Tile t: opponentsBoard.getTiles()) {
            if(t == selectedTile) {
                t.setColor(Color.RED);
            } else {
                t.setColor(Color.BLUE);
            }
        }
    }

    // All hover events necessary for the ENEMYROUND game state
    private static void enemyRoundHover() {

    }

    // All hover events necessary for the END game state
    private static void endRoundHover() {

    }
}
