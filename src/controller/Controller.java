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
    public static Controller sharedInstance = new Controller();

    private static Board usersBoard, opponentsBoard;
    private static Ship selectedShip;
    private static Tile selectedTile;
    private static Tile[] selectedTiles;

    private Controller() {

    }

    public static void setUp() {
        usersBoard = GameManager.getUserBoard();
        opponentsBoard = GameManager.getOpponentBoard();
        selectedShip = null;
    }

    public EventHandler<KeyEvent> keyEvent = new EventHandler<>() {

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
            updateView();
        }
    };

    public EventHandler<MouseEvent> mouseEvent = new EventHandler<>() {
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
            updateView();
        }
    };

    private void placeShip(Tile tile) {
        if(selectedShip != null) {
            selectedTiles = usersBoard.getTileNeighboursHorizontal(tile, selectedShip.getShipLength());
            boolean validPlacement = !Arrays.asList(selectedTiles).contains(null);

            if (validPlacement) {
                if (selectedShip instanceof PatrolBoatShip) {
                    if (GameManager.canAddPatrolBoat()) {
                        for (Tile t : selectedTiles) {
                            if(!t.hasShip()) {
                                t.setHasShip(true);
                                t.setColor(Color.GRAY);
                            }
                        }
                    }
                }

                if (selectedShip instanceof SuperPatrolShip) {
                    if (GameManager.canAddSuperPatrol()) {
                        for (Tile t : selectedTiles) {
                            if(!t.hasShip()) {
                                SuperPatrolShip sps = new SuperPatrolShip(selectedTile.getXPos(), selectedTile.getYPos(), usersBoard.isHorizontal());
                                t.setShip(sps);
                                t.setHasShip(true);
                                t.setColor(Color.GRAY);
                            }
                        }
                    }
                }

                if (selectedShip instanceof DestroyerShip) {
                    if (GameManager.canAddDestroyerShips()) {
                        for (Tile t : selectedTiles) {
                            if(!t.hasShip()) {
                                t.setHasShip(true);
                                t.setColor(Color.GRAY);
                            }
                        }
                    }
                }

                if (selectedShip instanceof BattleShip) {
                    if (GameManager.canAddBattleshipShips()) {
                        for (Tile t : selectedTiles) {
                            if(!t.hasShip()) {
                                t.setHasShip(true);
                                t.setColor(Color.GRAY);
                            }
                        }
                    }
                }

                if (selectedShip instanceof CarrierShip) {
                    if (GameManager.canAddCarrierShips()) {
                        for (Tile t : selectedTiles) {
                            if (!t.hasShip()) {
                                t.setHasShip(true);
                                t.setColor(Color.GRAY);
                            }
                        }
                    }
                }
            }
        }
    }

    private void tileClickedRightButton(Tile tile) {
        tile.setColor(Color.BLUE);
    }

    // Gets called when mouse hovers a tile
    public void onHover(Tile tile) {
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
    private void setUpMouse(MouseEvent event) {
        // Left click on the mouse
        if (event.getButton() == MouseButton.PRIMARY) {
            placeShip(selectedTile);
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
    private void userRoundMouse(MouseEvent event) {
        if(selectedTile.hasShip() && !selectedTile.isShot()) {
            selectedTile.getShip().hitShip();
            usersBoard.getPlayer().addPoint();
            selectedTile.setIsShot(true);
            if(selectedTile.getShip().getShipLives() == 0) {
                usersBoard.getPlayer().addPoint();
            }
            // PLAYER GETS ANOTHER TURN
            // RESET 30s TIMER
        } else if (selectedTile.isShot()) {
            // NOTIFY PLAYER THAT THE TILE IS ALREADY SHOT
            // PLAYER GETS ANOTHER TURN
        } else if (!selectedTile.hasShip()) {
            // NOTIFY THE PLAYER OF A "MISS EVENT"
            // END PLAYER TURN
            selectedTile.setIsShot(true);
            GameManager.setGameState(GameState.ENEMYROUND);
        }
    }

    // All mouse events necessary for the ENEMYROUND game state
    private void enemyRoundMouse(MouseEvent event) {
        
    }

    // All mouse events necessary for the END game state
    private void endRoundMouse(MouseEvent event) {

    }

    ////////////////////////////////////////////////////////
    ///////////////// KEY CONTROLLER //////////////////////
    //////////////////////////////////////////////////////

    // All keys events necessary for the SETUP game state
    private void setUpKeys(KeyEvent event) {
        int shipsLeftover = 0;

        if(event.getCode() == KeyCode.E) {
            usersBoard.setHorizontal(!usersBoard.isHorizontal());
        }

        // When key 1 is pressed, set ship to Patrol Boat
        if(event.getCode() == KeyCode.DIGIT1) {
            if(selectedTile == null) {
                selectedShip = new PatrolBoatShip(0, 0, usersBoard.isHorizontal());
            } else {
                selectedShip = new PatrolBoatShip(selectedTile.getXPos(), selectedTile.getYPos(), usersBoard.isHorizontal());
            }
        }

        // When key 2 is pressed, set ship to Super Patrol
        if(event.getCode() == KeyCode.DIGIT2) {
            if(selectedTile == null) {
                selectedShip = new SuperPatrolShip(0, 0, usersBoard.isHorizontal());
            } else {
                selectedShip = new SuperPatrolShip(selectedTile.getXPos(), selectedTile.getYPos(), usersBoard.isHorizontal());
            }
        }

        // When key 3 is pressed, set ship to Destroyer
        if(event.getCode() == KeyCode.DIGIT3) {
            if(selectedTile == null) {
                selectedShip = new DestroyerShip(0, 0, usersBoard.isHorizontal());
            } else {
                selectedShip = new DestroyerShip(selectedTile.getXPos(), selectedTile.getYPos(), usersBoard.isHorizontal());
            }
        }

        // When key 4 is pressed, set ship to Battleship
        if(event.getCode() == KeyCode.DIGIT4) {
            if(selectedTile == null) {
                selectedShip = new BattleShip(0, 0, usersBoard.isHorizontal());
            } else {
                selectedShip = new BattleShip(selectedTile.getXPos(), selectedTile.getYPos(), usersBoard.isHorizontal());
            }
        }

        // When key 5 is pressed, set ship to Carrier
        if(event.getCode() == KeyCode.DIGIT5) {
            if(selectedTile == null) {
                selectedShip = new CarrierShip(0, 0, usersBoard.isHorizontal());
            } else {
                selectedShip = new CarrierShip(selectedTile.getXPos(), selectedTile.getYPos(), usersBoard.isHorizontal());
            }
        }

        if(selectedTile != null) {
            selectedTiles = usersBoard.getTileNeighboursHorizontal(selectedTile, selectedShip.getShipLength());
        }
        updateView();
    }

    // All keys events necessary for the USERROUND game state
    private void userRoundKeys(KeyEvent event) {

    }

    // All keys events necessary for the ENEMYROUND game state
    private void enemyRoundKeys(KeyEvent event) {

    }

    // All keys events necessary for the END game state
    private void endRoundKeys(KeyEvent event) {

    }

    ////////////////////////////////////////////////////////
    ///////////////// HOVER CONTROLLER ////////////////////
    //////////////////////////////////////////////////////

    // All hover events necessary for the SETUP game state
    private void setUpHover() {
        if(selectedShip != null) {
            selectedTiles = usersBoard.getTileNeighboursHorizontal(selectedTile, selectedShip.getShipLength());
            boolean validPlacement = !Arrays.asList(selectedTiles).contains(null);

            for (Tile t : usersBoard.getTiles()) {
                if (validPlacement && Arrays.asList(selectedTiles).contains(t)) {
                    t.setColor(Color.GRAY);
                } else if (!t.hasShip()) {
                    t.setColor(Color.BLUE);
                }
            }
        }

        updateView();
    }

    // All hover events necessary for the USERROUND game state
    private void userRoundHover() {
        for (Tile t: opponentsBoard.getTiles()) {
            if(t == selectedTile) {
                t.setColor(Color.RED);
            } else {
                t.setColor(Color.BLUE);
            }
        }
    }

    // All hover events necessary for the ENEMYROUND game state
    private void enemyRoundHover() {

    }

    // All hover events necessary for the END game state
    private void endRoundHover() {

    }

    private int getShipsLeftOver() {
        int shipsLeftOver = 0;

        if(selectedShip instanceof CarrierShip) {
            shipsLeftOver = GameManager.getCarrierShips()[0] - GameManager.getCarrierShips()[1];
        }

        if(selectedShip instanceof BattleShip) {
            shipsLeftOver = GameManager.getBattleshipShips()[0] - GameManager.getBattleshipShips()[1];
        }

        if(selectedShip instanceof DestroyerShip) {
            shipsLeftOver = GameManager.getDestroyerShips()[0] - GameManager.getDestroyerShips()[1];
        }

        if(selectedShip instanceof SuperPatrolShip) {
            shipsLeftOver = GameManager.getSuperPatrolShips()[0] - GameManager.getSuperPatrolShips()[1];
        }

        if(selectedShip instanceof PatrolBoatShip) {
            shipsLeftOver = GameManager.getPatrolBoatShips()[0] - GameManager.getPatrolBoatShips()[1];
        }

        return shipsLeftOver;
    }

    private void updateView() {
        if(selectedShip != null)
            GameView.setSelectedShip(selectedShip.getName() + " " + getShipsLeftOver() + "x");

        if(selectedTiles != null) {
            for (Tile t : usersBoard.getTiles()) {
                if(Arrays.asList(selectedTiles).contains(t)) {
                    t.setColor(Color.GRAY);
                } else if(t.hasShip()) {
                    t.setColor(Color.GRAY);
                } else {
                    t.setColor(Color.BLUE);
                }
            }
        }
    }
}
