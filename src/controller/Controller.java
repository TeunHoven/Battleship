package controller;

import Protocol.Exceptions.ServerUnavailableException;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;
import model.GameManager;
import model.board.Board;
import model.board.Tile;
import model.player.ComputerPlayer;
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

    private boolean radarUsed = false;
    private static final int[][] surroundedLocations = {{0, 0}, {1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}, {0, -1}, {1, -1}};

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
            switch (GameManager.getGameState()) {
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
            switch (GameManager.getGameState()) {
                case SETUP:
                    setUpMouse(mouseEvent);
                    break;

                case USERROUND:
                    try {
                        userRoundMouse(mouseEvent);
                    } catch (ServerUnavailableException e) {
                        e.printStackTrace();
                    }
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
        if (selectedShip != null) {
            selectedTiles = usersBoard.getTileNeighboursHorizontal(tile, selectedShip.getShipLength());
            boolean validPlacement = true;

            for (Tile t : selectedTiles) {
                if (t == null || t.hasShip()) {
                    validPlacement = false;
                }
            }

            if (validPlacement) {
                if (selectedShip instanceof PatrolBoatShip) {
                    if (GameManager.canAddPatrolBoat()) {
                        Ship ship = new PatrolBoatShip(tile.getXPos(), tile.getYPos(), true);
                        for (Tile t : selectedTiles) {
                            if (!t.hasShip()) {
                                t.setShip(ship);
                                t.setHasShip(true);
                                usersBoard.setShip(selectedTile, ship);
                            }
                        }
                    }
                }

                if (selectedShip instanceof SuperPatrolShip) {
                    if (GameManager.canAddSuperPatrol()) {
                        Ship ship = new SuperPatrolShip(tile.getXPos(), tile.getYPos(), usersBoard.isHorizontal());
                        for (Tile t : selectedTiles) {
                            if (!t.hasShip()) {
                                t.setShip(ship);
                                t.setHasShip(true);
                                usersBoard.setShip(selectedTile, ship);
                            }
                        }
                    }
                }

                if (selectedShip instanceof DestroyerShip) {
                    if (GameManager.canAddDestroyerShips()) {
                        Ship ship = new DestroyerShip(tile.getXPos(), tile.getYPos(), usersBoard.isHorizontal());
                        for (Tile t : selectedTiles) {
                            if (!t.hasShip()) {
                                t.setShip(ship);
                                t.setHasShip(true);
                                usersBoard.setShip(selectedTile, ship);
                            }
                        }
                    }
                }

                if (selectedShip instanceof BattleShip) {
                    if (GameManager.canAddBattleshipShips()) {
                        Ship ship = new BattleShip(tile.getXPos(), tile.getYPos(), usersBoard.isHorizontal());
                        for (Tile t : selectedTiles) {
                            if (!t.hasShip()) {
                                t.setShip(ship);
                                t.setHasShip(true);
                                usersBoard.setShip(selectedTile, ship);
                            }
                        }
                    }
                }

                if (selectedShip instanceof CarrierShip) {
                    if (GameManager.canAddCarrierShips()) {
                        Ship ship = new CarrierShip(tile.getXPos(), tile.getYPos(), usersBoard.isHorizontal());
                        for (Tile t : selectedTiles) {
                            if (t != null && !t.hasShip()) {
                                t.setShip(ship);
                                t.setHasShip(true);
                                usersBoard.setShip(selectedTile, ship);

                                selectedShip = new CarrierShip(tile.getXPos(), tile.getYPos(), usersBoard.isHorizontal());
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

        switch (GameManager.getGameState()) {
            case SETUP:
                setUpHover();
                break;

            case USERROUND:
                userRoundHover();
                break;

            case ENEMYROUND:
                if(!GameManager.isOnline()) {
                    computerSmartTurn();
                }
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

            if(isPlayerReady()) {
                GameView.readyButton.setDisable(false);
            }
        }

        // Right click on the mouse
        if (event.getButton() == MouseButton.SECONDARY) {
            // Does nothing yet
        }
    }

    // All mouse events necessary for the USERROUND game state
    private void userRoundMouse(MouseEvent event) throws ServerUnavailableException {
        if(!tileOnUsersBoard()) {
            if (GameManager.isOnline()) {
                GameManager.getClient().move(new Integer[]{selectedTile.getXPos(), selectedTile.getYPos()});
            } else {
                if (!GameManager.getRadarUserReady() || !radarUsed) {
                    if (selectedTile.hasShip() && !selectedTile.isShot()) {
                        shootTile(selectedTile, usersBoard);
                        // PLAYER GETS ANOTHER TURN
                        // RESET 30s TIMER
                    } else if (selectedTile.isShot()) {
                        // NOTIFY PLAYER THAT THE TILE IS ALREADY SHOT
                        // PLAYER GETS ANOTHER TURN
                        GameView.setMessage("Tile already shot!");
                    } else if (!selectedTile.hasShip()) {
                        // NOTIFY THE PLAYER OF A "MISS EVENT"
                        // END PLAYER TURN
                        selectedTile.setIsShot(true);
                        GameManager.nextTurn();
                        GameView.setMessage("Tile has no ship!");
                        opponentsBoard.setShot(selectedTile, false);
                    }
                } else {
                    Tile[] surroundedTiles = new Tile[9];
                    for (int i = 0; i < 9; i++) { // To get all the surrounded tiles
                        int x = selectedTile.getXPos() + surroundedLocations[i][0];
                        int y = selectedTile.getYPos() + surroundedLocations[i][1];

                        surroundedTiles[i] = opponentsBoard.getTile(x, y);
                    }
                    opponentsBoard.setRadar(surroundedTiles);
                    radarUsed = false;
                    updateView();
                    GameManager.radarUserUsed();
                }
            }
        }

        updateView();
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

        if (event.getCode() == KeyCode.E) {
            usersBoard.setHorizontal(!usersBoard.isHorizontal());
        }

        // When key 1 is pressed, set ship to Patrol Boat
        if (event.getCode() == KeyCode.DIGIT1) {
            if (selectedTile == null) {
                selectedShip = new PatrolBoatShip(0, 0, usersBoard.isHorizontal());
            } else {
                selectedShip = new PatrolBoatShip(selectedTile.getXPos(), selectedTile.getYPos(), usersBoard.isHorizontal());
            }
        }

        // When key 2 is pressed, set ship to Super Patrol
        if (event.getCode() == KeyCode.DIGIT2) {
            if (selectedTile == null) {
                selectedShip = new SuperPatrolShip(0, 0, usersBoard.isHorizontal());
            } else {
                selectedShip = new SuperPatrolShip(selectedTile.getXPos(), selectedTile.getYPos(), usersBoard.isHorizontal());
            }
        }

        // When key 3 is pressed, set ship to Destroyer
        if (event.getCode() == KeyCode.DIGIT3) {
            if (selectedTile == null) {
                selectedShip = new DestroyerShip(0, 0, usersBoard.isHorizontal());
            } else {
                selectedShip = new DestroyerShip(selectedTile.getXPos(), selectedTile.getYPos(), usersBoard.isHorizontal());
            }
        }

        // When key 4 is pressed, set ship to Battleship
        if (event.getCode() == KeyCode.DIGIT4) {
            if (selectedTile == null) {
                selectedShip = new BattleShip(0, 0, usersBoard.isHorizontal());
            } else {
                selectedShip = new BattleShip(selectedTile.getXPos(), selectedTile.getYPos(), usersBoard.isHorizontal());
            }
        }

        // When key 5 is pressed, set ship to Carrier
        if (event.getCode() == KeyCode.DIGIT5) {
            if (selectedTile == null) {
                selectedShip = new CarrierShip(0, 0, usersBoard.isHorizontal());
            } else {
                selectedShip = new CarrierShip(selectedTile.getXPos(), selectedTile.getYPos(), usersBoard.isHorizontal());
            }
        }

        if (selectedTile != null) {
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
        if (tileOnUsersBoard()) {
            if (selectedShip != null) {
                boolean validPlacement = isValidPlacement();

                for (Tile t : usersBoard.getTiles()) {
                    if (validPlacement && Arrays.asList(selectedTiles).contains(t)) {
                        t.setColor(Color.GRAY);
                    } else if (!t.hasShip()) {
                        t.setColor(Color.BLUE);
                    } else {
                        t.setColor(Color.BLUE);
                    }
                }
            }

            updateView();
        }
    }

    // All hover events necessary for the USERROUND game state
    private void userRoundHover() {
        if(!tileOnUsersBoard()) {
            if(!GameManager.getRadarUserReady() || !radarUsed) {
                for (Tile t : opponentsBoard.getTiles()) {
                    if (t == selectedTile) {
                        t.setColor(Color.RED);
                    } else {
                        t.setColor(Color.BLUE);
                    }
                }
            } else {
                for (Tile t : opponentsBoard.getTiles()) {
                    if (t == selectedTile) {
                        t.setColor(Color.GREEN);
                    } else {
                        t.setColor(Color.BLUE);
                    }
                }
            }
        }
        updateView();
    }

    // All hover events necessary for the ENEMYROUND game state
    private void enemyRoundHover() {

    }

    // All hover events necessary for the END game state
    private void endRoundHover() {

    }

    private boolean isValidPlacement() {
        selectedTiles = usersBoard.getTileNeighboursHorizontal(selectedTile, selectedShip.getShipLength());
        if (Arrays.asList(selectedTiles).contains(null)) {
            return false;
        }
        if (usersBoard.isHorizontal()) {
            for (Tile t : selectedTiles) {
                if (t.getYPos() != selectedTile.getYPos()) {
                    return false;
                }
            }
        }

        for (Tile t : selectedTiles) {
            if (t.hasShip()) {
                return false;
            }
        }
        return true;
    }

    private int isValidPlacement(int x, int y) {
        usersBoard.setHorizontal(true);

        boolean canHorizontal = isValidPlacement();

        usersBoard.setHorizontal(false);
        boolean canVertical = isValidPlacement();

        if (canHorizontal && canVertical) {
            return 0;
        } else if (canHorizontal) {
            return 1;
        } else if (canVertical) {
            return 2;
        } else {
            return 3;
        }
    }

    private int getShipsLeftOver() {
        int shipsLeftOver = 0;

        if (selectedShip instanceof CarrierShip) {
            shipsLeftOver = GameManager.getCarrierShips()[0] - GameManager.getCarrierShips()[1];
        }

        if (selectedShip instanceof BattleShip) {
            shipsLeftOver = GameManager.getBattleshipShips()[0] - GameManager.getBattleshipShips()[1];
        }

        if (selectedShip instanceof DestroyerShip) {
            shipsLeftOver = GameManager.getDestroyerShips()[0] - GameManager.getDestroyerShips()[1];
        }

        if (selectedShip instanceof SuperPatrolShip) {
            shipsLeftOver = GameManager.getSuperPatrolShips()[0] - GameManager.getSuperPatrolShips()[1];
        }

        if (selectedShip instanceof PatrolBoatShip) {
            shipsLeftOver = GameManager.getPatrolBoatShips()[0] - GameManager.getPatrolBoatShips()[1];
        }

        return shipsLeftOver;
    }

    /**
     * Updates the scene automatic
     */
    private void updateView() {
        GameManager.checkRound(); // Checks for every round if it's over!

        switch (GameManager.getGameState()) {
            case SETUP -> {
                if (selectedShip != null)
                    GameView.setSelectedShip(selectedShip.toString() + " " + getShipsLeftOver() + "x");

                if (selectedTiles != null) {
                    for (Tile t : usersBoard.getTiles()) {
                        if (Arrays.asList(selectedTiles).contains(t)) {
                            t.setColor(Color.GRAY);
                        } else {
                            t.setColor(Color.BLUE);
                        }
                    }
                }
            }

            case USERROUND -> {
                for (Tile t : usersBoard.getTiles()) {
                    t.setColor(Color.BLUE);
                }
            }

            case ENEMYROUND -> {
                for (Tile t : opponentsBoard.getTiles()) {
                    t.setColor(Color.BLUE);
                }
            }
        }

        if(usersBoard.getPlayer().isReady()) {
            GameView.setTopBox();
        }
        GameView.setUserPoints(GameManager.getUser().getPoints());
        GameView.setEnemyPoints(GameManager.getOpponent().getPoints());
    }

    /**
     * Checks if the selected tile is on the users board
     *
     * @return true if selected tile is on the users board
     */
    private boolean tileOnUsersBoard() {
        if (selectedTile.getBoard() == usersBoard)
            return true;
        else
            return false;
    }

    public void readyButtonClicked() throws ServerUnavailableException {
        usersBoard.getPlayer().setReady();
        GameView.setTopBox();
        GameManager.checkRound();
        String[][] grid = GameManager.getBoardAsString();
        updateView();
        GameManager.getClient().deploy(grid);
    }

    /**
     * When the randomize button is clicked it will randomize all ship placements
     */
    public void randomizeButtonClicked() {
        while (GameManager.getCarrierShips()[1] < GameManager.getCarrierShips()[0]) { // Carrier ships 2 max
            selectedShip = new CarrierShip(0, 0, true);
            randomizePlacement();
        }

        while (GameManager.getBattleshipShips()[1] < GameManager.getBattleshipShips()[0]) { // Battleships 3 max
            selectedShip = new BattleShip(0, 0, true);
            randomizePlacement();
        }

        while (GameManager.getDestroyerShips()[1] < GameManager.getDestroyerShips()[0]) { // Destroyer ships 5 max
            selectedShip = new DestroyerShip(0, 0, true);
            randomizePlacement();
        }

        while (GameManager.getSuperPatrolShips()[1] < GameManager.getSuperPatrolShips()[0]) { // Super Patrol 8 max
            selectedShip = new SuperPatrolShip(0, 0, true);
            randomizePlacement();
        }

        while (GameManager.getPatrolBoatShips()[1] < GameManager.getPatrolBoatShips()[0]) { // Patrol boats 10 max
            selectedShip = new PatrolBoatShip(0, 0, true);
            randomizePlacement();
        }

        GameView.readyButton.setDisable(false);
        updateView();
    }

    /**
     * The function that randomizeButtonClicked uses to randomize everything
     */
    private void randomizePlacement() {
        int x = (int) (14 * Math.random());
        int y = (int) (9 * Math.random());
        int horizontal = (int) (2 * Math.random()); // 0 horizontal, 1 vertical

        selectedTile = usersBoard.getTile(x, y);

        switch (isValidPlacement(x, y)) {
            case 0 -> {
                usersBoard.setHorizontal(horizontal == 0);
                placeShip(selectedTile);
            }
            case 1 -> {
                usersBoard.setHorizontal(true);
                placeShip(selectedTile);
            }
            case 2 -> {
                usersBoard.setHorizontal(false);
                placeShip(selectedTile);
            }
        }
    }

    /**
     * The basic AI implementation.
     * It only shoots at random tiles.
     */
    public static void computerPlayerTurn() {
        Tile tile = shootRandomTile();
        while (tile != null) {
            tile = shootRandomTile();
        }

        if(tile.hasShip()) {
            shootTile(tile, opponentsBoard);
        }
        GameManager.setGameState(GameState.USERROUND);
    }

    /**
     * A more advanced AI implementation.
     * It starts with shooting at random tiles,
     * but when he hits a ship it will try to sink this ship by shooting neighbouring tiles.
     */
    public void computerSmartTurn() {
        ComputerPlayer player = ((ComputerPlayer) opponentsBoard.getPlayer());
        Tile firstHitTile;
        boolean reset = false;

        if(player.getHitShip() == null) {
            firstHitTile = shootRandomTile();
            if(firstHitTile != null) {
                if(isKill(firstHitTile, player)) {
                    System.out.println("Reset first time");
                    reset = true;
                } else {
                    player.setHitShip(firstHitTile);
                }
                System.out.println("New First hit at Index X: " + firstHitTile.getXPos() + "; Index Y: " + firstHitTile.getYPos());
            } else {
                System.out.println("No hit");
            }
        } else {
            firstHitTile = player.getHitShip();
            System.out.println("First hit already at Index X: " + firstHitTile.getXPos() + "; Index Y: " + firstHitTile.getYPos());
        }

        if (firstHitTile != null) {
            while(true) {
                Tile neighbourTile;

                if(reset) {
                    System.out.println("Resetting in the while");
                    player.resetShooting();
                    firstHitTile = shootRandomTile();
                    if(firstHitTile == null)
                        break;
                    reset = false;
                    if(isKill(firstHitTile, player)) {
                        reset = true;
                    } else {
                        player.setHitShip(firstHitTile);
                    }
                }

                if (player.getVelocity()[0] == 0 &&
                        player.getVelocity()[1] == 0) {
                    neighbourTile = player.shootRandomNeighbour(firstHitTile);

                    while (neighbourTile != null && neighbourTile.isShot()) {
                        neighbourTile = player.shootRandomNeighbour(firstHitTile);
                    }

                    if(neighbourTile == null) {
                        reset = true;
                    }
                } else {
                    neighbourTile = player.shootNeighbour(firstHitTile);

                    while(neighbourTile != null && neighbourTile.isShot()) {
                        neighbourTile = player.shootNeighbour(neighbourTile);
                    }
                }

                if (neighbourTile != null && !neighbourTile.hasShip()) {
                    neighbourTile.setIsShot(true);
                    usersBoard.setShot(neighbourTile, false);
                    player.turnAround();
                    System.out.println("NO hit");
                    break;
                }

                if (neighbourTile != null && neighbourTile.hasShip()) {
                    System.out.println("Tile has ship!");
                    player.setVelocity(neighbourTile, firstHitTile);

                    firstHitTile = neighbourTile;

                    if(isKill(neighbourTile, player)) {
                        System.out.println("Resetting");
                        reset = true;
                    }
                }
            }
        }
        GameManager.nextTurn();
    }

    /**
     * Shoots at a random tile, if there is a ship on the randomly selected tile return this tile.
     * @return A Tile if the tile that was shot contains a Ship
     */
    public static Tile shootRandomTile() {
        int[] posTile = ((ComputerPlayer) opponentsBoard.getPlayer()).randomShot();
        Tile tile = usersBoard.getTile(posTile[0], posTile[1]);
        while (tile.isShot()) {
            posTile = ((ComputerPlayer) opponentsBoard.getPlayer()).randomShot();
            tile = usersBoard.getTile(posTile[0], posTile[1]);
        }
        System.out.println("Random tile shot Tile x: " + tile.getXPos() + " Tile y: " + tile.getYPos());
        if (!tile.hasShip()) {
            usersBoard.setShot(tile, false);
            return null;
            // miss event
        }
        return tile;
    }

    public static boolean isKill(Tile tile, Player player) {
        int pointsBeforeShot = player.getPoints();
        shootTile(tile, opponentsBoard);
        int pointsAfterShot = player.getPoints();

        return pointsAfterShot - pointsBeforeShot == 2;
    }

    /**
     * Shoots a Tile on a Board and adjusts shipLives and Player points accordingly.
     * @param tile - The Tile that to be shot
     * @param board - The board to which the shooting party belongs to
     */
    public static void shootTile(Tile tile, Board board) {
            Board otherBoard = board.getPlayer().getName().equals(usersBoard.getPlayer().getName()) ? opponentsBoard : usersBoard;
            board.getPlayer().addPoint();
            GameView.setMessage(board.getPlayer().getName() + " Hit a ship!");
            otherBoard.setShot(tile, true);
            System.out.println("Shot + Lives boat:" + tile.getShip().getShipLives());

            if (tile.getShip().getShipLives() <= 0) {
                GameManager.addKill(tile.getShip(), board.getPlayer());
                board.getPlayer().addPoint();
                GameView.setMessage(board.getPlayer().getName() + " Sunk a ship!");
                if (GameManager.checkGameEnd() == 1 || GameManager.checkGameEnd() == 2) {
                    GameView.setMessage(GameManager.getWinner() + " is the winner!");
                    GameManager.setGameState(GameState.END);
                }
            }
    }

    /**
     * ????????????????????
     */
    public void radarButtonClicked() {
        radarUsed = !radarUsed;
        if(radarUsed) {
            GameView.setMessage("Click on tile to use radar!");
        } else {
            GameManager.checkRound();
        }
    }

    /**
     * Checks whether the Player has placed all of his ships and thus is ready to play.
     * @return True if the player has placed all of his ships
     */
    private boolean isPlayerReady() {
        if(GameManager.getPatrolBoatShips()[0] == GameManager.getPatrolBoatShips()[1]
                && GameManager.getSuperPatrolShips()[0] == GameManager.getSuperPatrolShips()[1]
                && GameManager.getDestroyerShips()[0] == GameManager.getDestroyerShips()[1]
                && GameManager.getBattleshipShips()[0] == GameManager.getBattleshipShips()[1]
                && GameManager.getCarrierShips()[0] == GameManager.getCarrierShips()[1]) {
            return true;
        }

        return false;
    }
}
