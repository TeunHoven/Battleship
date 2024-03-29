package model;

import Protocol.Client;
import Protocol.Exceptions.ServerUnavailableException;
import controller.Controller;
import controller.GameState;
import javafx.scene.paint.Color;
import model.board.Board;
import model.board.Tile;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import model.ship.*;
import view.GameView;

import java.util.Arrays;

public class GameManager {
    private static GameState gameState = GameState.JOINING;

    private static int round = 0;
    private static int turn = 0;
    private static boolean radarReadyUser = false;
    private static boolean radarReadyOpponent = false;

    private static int[] patrolBoatShips = {10, 0};
    private static int[] superPatrolShips = {8, 0};
    private static int[] destroyerShips = {5, 0};
    private static int[] battleshipShips = {3, 0};
    private static int[] carrierShips = {2, 0};

    private static int[] userKills ={0, 0, 0, 0 ,0};
    private static int[] opponentKills ={0, 0, 0, 0 ,0};

    private static Player user, opponent;
    private static Board userBoard, enemyBoard;
    private static String winner = "NONE";
    private static String opponentsName = "";

    private static boolean radarEnabled = true;
    private static boolean isOnline = false;

    private static Client client, computerClient;
    private static String name, ip, port;

    public static void setUp() throws ServerUnavailableException {
        user = new HumanPlayer(name);

        if(!isOnline) {
            opponent = new ComputerPlayer();
        } else {
            opponent = new ComputerPlayer();
        }

        userBoard = new Board(user);
        enemyBoard = new Board(opponent);

        if(isOnline) {
            client = new Client(name, ip, port);
            client.start();
            new Thread(client).start();

            computerClient = new Client("Computer200192", "127.0.0.1", "5000");
            computerClient.start();
            new Thread(computerClient).start();
        }

        if(opponent instanceof ComputerPlayer) {
            if(isOnline) {
                ((ComputerPlayer) opponent).setUp(computerClient);
            } else {
                ((ComputerPlayer) opponent).setUp();
            }
        }
    }

    /**
     * Checks the GameState to determine who's turn it is or whether the game has ended.
     */
    public static void checkRound() {
        switch(gameState) {
            case SETUP:
                if(userBoard.getPlayer().isReady() && enemyBoard.getPlayer().isReady()) {
                    userBoard.setColor(Color.BLUE);
                    setGameState(GameState.USERROUND);
                    round++;
                }
                GameView.setRound("Setup");
                break;
            case USERROUND:
                GameView.setRound("Your turn #" + round + "");
                GameView.setMessage("Click a tile!");

                if(!winner.equals("NONE")) {
                    setGameState(GameState.END);
                }
                break;
            case ENEMYROUND:
                GameView.setRound("Opponents turn #" + round + "");

                if(!winner.equals("NONE")) {
                    setGameState(GameState.END);
                }
                break;
            case END:
                break;
            default:
                break;
        }
    }

    /**
     * Gives the turn to the next player.
     */
    public static void nextTurn() {
        turn++;
        if(gameState == GameState.USERROUND) {
            gameState = GameState.ENEMYROUND;
        }else if(gameState == GameState.ENEMYROUND) {
            gameState = GameState.USERROUND;
        }

        if(turn%2 == 0) {
            nextRound();
            turn = 0;
        }
        GameView.setTopBox();
    }

    /**
     * Enables the radar every 4th round.
     */
    public static void nextRound() {
        round++;
        checkRound();
        GameView.setTopBox();

        if(round%4 == 0) {
            radarReadyUser = true;
            radarReadyOpponent = true;
        }
    }

    /**
     * Returns the current GameState.
     * @return The current GameState
     */
    public static GameState getGameState() {
        return gameState;
    }

    /**
     * Sets the current GameState.
     * @param gamestate - The GameState that needs to be set
     */
    public static void setGameState(GameState gamestate){
        gameState = gamestate;
    }

    /**
     * Checks whether the player can still place more Patrol boats or has reach its maximum.
     * @return True if the player can still place a Patrol Boat
     */
    public static boolean canAddPatrolBoat(){
        if(patrolBoatShips[1] < patrolBoatShips[0]) {
            patrolBoatShips[1]++;
            return true;
        }

        return false;
    }

    /**
     * Checks whether the player can still place more Super Patrol boats or has reach its maximum.
     * @return True if the player can still place a Super Patrol Boat
     */
    public static boolean canAddSuperPatrol(){
        if(superPatrolShips[1] < superPatrolShips[0]) {
            superPatrolShips[1]++;
            return true;
        }

        return false;
    }

    /**
     * Checks whether the player can still place more Destroyer Ships or has reach its maximum.
     * @return True if the player can still place a Destroyer ship
     */
    public static boolean canAddDestroyerShips(){
        if(destroyerShips[1] < destroyerShips[0]) {
            destroyerShips[1]++;
            return true;
        }

        return false;
    }

    /**
     * Checks whether the player can still place more Battle Ships or has reach its maximum.
     * @return True if the player can still place a Battle Ship
     */
    public static boolean canAddBattleshipShips(){
        if(battleshipShips[1] < battleshipShips[0]) {
            battleshipShips[1]++;
            return true;
        }

        return false;
    }

    /**
     * Checks whether the player can still place more Carrier Ships or has reach its maximum.
     * @return True if the player can still place a Carrier Ship
     */
    public static boolean canAddCarrierShips(){
        if(carrierShips[1] < carrierShips[0]) {
            carrierShips[1]++;
            return true;
        }

        return false;
    }

    // Checks for winner, if w == 0 -> No winner, if w == 1 -> User is winner, if w == 2 -> Opponent is winner.
    /**
     * Checks if there is a winner based on one player destroying all the other player's ships
     * @return 0 if there is no winner, return 1 if the User player is the winner, return 2 if the Opponent player is the winner.
     */
    public static int checkGameEnd(){
        int w = 0;
        if(user.getPoints() == 91) {
            w = 1;
            setWinner(user);
        } else if (opponent.getPoints() == 91) {
            w = 2;
            setWinner(opponent);
        }
        return w;
    }

    /**
     * Determines the outcome of the game when the time limit has been reached.
     */
    public static void setOutcome() {
        // Checks the outcome based on points
        if (user.getPoints() > opponent.getPoints()) {
            setWinner(user);
        } else if (user.getPoints() < opponent.getPoints()) {
            setWinner(opponent);
        } else {
            // Checks the outcome based on the amount of kills of inferior ships
            // Winner is initialized to DRAW so no need to set it again to draw
            if (getUserKills()[0] != getOpponentKills()[0]) {
                if (getUserKills()[0] > getOpponentKills()[0]) {
                    setWinner(user);
                }
                setWinner(opponent);
            } else if (getUserKills()[1] != getOpponentKills()[1]) {
                if (getUserKills()[1] > getOpponentKills()[1]) {
                    setWinner(user);
                }
                setWinner(opponent);
            } else if (getUserKills()[2] != getOpponentKills()[2]) {
                if (getUserKills()[2] > getOpponentKills()[2]) {
                    setWinner(user);
                }
                setWinner(opponent);
            } else if (getUserKills()[3] != getOpponentKills()[3]) {
                if (getUserKills()[3] > getOpponentKills()[3]) {
                    setWinner(user);
                }
                setWinner(opponent);
            } else if (getUserKills()[4] != getOpponentKills()[4]) {
                if (getUserKills()[4] > getOpponentKills()[4]) {
                    setWinner(user);
                }
                setWinner(opponent);
            }
        }

        if(winner.equals("NONE")) {
            winner = "DRAW";
        }
    }

    /**
     * Returns the total amount of PatrolBoatShips and the amount placed.
     * @return An int[] containing the total amount of ships and the amount already placed on the board.
     */
    public static int[] getPatrolBoatShips() {
        return patrolBoatShips;
    }

    /**
     * Returns the total amount of SuperPatrolShips and the amount placed.
     * @return An int[] containing the total amount of ships and the amount already placed on the board.
     */
    public static int[] getSuperPatrolShips() {
        return superPatrolShips;
    }

    /**
     * Returns the total amount of DestroyerShips and the amount placed.
     * @return An int[] containing the total amount of ships and the amount already placed on the board.
     */
    public static int[] getDestroyerShips() {
        return destroyerShips;
    }

    /**
     * Returns the total amount of BattleshipShips and the amount placed.
     * @return An int[] containing the total amount of ships and the amount already placed on the board.
     */
    public static int[] getBattleshipShips() {
        return battleshipShips;
    }

    /**
     * Returns the total amount of CarrierShips and the amount placed.
     * @return An int[] containing the total amount of ships and the amount already placed on the board.
     */
    public static int[] getCarrierShips() {
        return carrierShips;
    }

    /**
     * Returns the Board of the User player.
     * @return The Board assigned to the User player
     */
    public static Board getUserBoard() {
        return userBoard;
    }

    /**
     * Returns the Board of the Opponent player.
     * @return The Board assigned to the Opponent player
     */
    public static Board getOpponentBoard() {
        return enemyBoard;
    }

    /**
     * Returns the Player assigned to User.
     * @return The Player assigned to User
     */
    public static Player getUser() {
        return user;
    }

    /**
     * Returns the Player assigned to Opponent.
     * @return The Player assigned to Opponent
     */
    public static Player getOpponent() {
        return opponent;
    }

    /**
     * Assigns the winning Player's name to the winner variable.
     * @param player - The winning Player
     */
    public static void setWinner(Player player){
        winner = player.getName();
    }

    /**
     * Returns the winner.
     * @return A String containing the name of the winner
     */
    public static String getWinner(){
        return winner;
    }

    /**
     * Adds a kill to the kill list of the given player.
     * @param ship - The type of ship killed by the Player
     * @param player - The Player that killed the ship
     */
    public static void addKill(Ship ship, Player player){
        int[] kills = new int[5];
        if(player == user) {
            kills = userKills;
        } else if (player == opponent) {
            kills = opponentKills;
        }
        if(ship instanceof CarrierShip){
            kills[0]++;
        } else if(ship instanceof BattleShip){
            kills[1]++;
        } else if(ship instanceof DestroyerShip){
            kills[2]++;
        } else if(ship instanceof SuperPatrolShip){
            kills[3]++;
        } else if(ship instanceof PatrolBoatShip){
            kills[4]++;
        }
        if(player == user) {
            userKills = kills;
        } else if (player == opponent) {
            opponentKills = kills;
            System.out.println(opponentKills);
        }
    }

    /**
     * Returns the kill list of the User Player.
     * @return An int[] containing the amount of Ship kills the User Player has gotten.
     */
    public static int[] getUserKills(){
        return userKills;
    }

    /**
     * Returns the kill list of the Opponent Player.
     * @return An int[] containing the amount of Ship kills the Opponent Player has gotten.
     */
    public static int[] getOpponentKills(){
        return opponentKills;
    }

    /**
     * Sets the User radar variable to false after the radar being used
     */
    public static void radarUserUsed() {
        radarReadyUser = false;
    }

    /**
     * Returns whether or not the User Player is ready to use the Radar.
     * @return True if the User Player is ready to use the Radar.
     */
    public static boolean getRadarUserReady() {
        return radarReadyUser;
    }

    /**
     * Returns whether or not the Opponent Player is ready to use the Radar.
     * @return True if the Opponent Player is ready to use the Radar.
     */
    public static boolean getRadarOpponentReady() {
        return radarReadyOpponent;
    }

    public static String[][] getBoardAsString(Board board) {
        String[][] boardString = new String[Board.HEIGHT][Board.WIDTH];

        for(int y=0; y<Board.HEIGHT; y++) {
            for(int x=0; x<Board.WIDTH; x++) {
                Tile tile = board.getTile(x, y);

                if(tile.hasShip()) {
                    Ship ship = tile.getShip();
                    String number = "" + ship.getName().toCharArray()[ship.getName().toCharArray().length-1];

                    if(ship instanceof PatrolBoatShip) {
                        boardString[y][x] = "P" + number;
                    }

                    if(ship instanceof SuperPatrolShip) {
                        boardString[y][x] = "S" + number;
                    }

                    if(ship instanceof DestroyerShip) {
                        boardString[y][x] = "D" + number;
                    }

                    if(ship instanceof BattleShip) {
                        boardString[y][x] = "B" + number;
                    }

                    if(ship instanceof CarrierShip) {
                        boardString[y][x] = "C" + number;
                    }
                } else {
                    boardString[y][x] = "";
                }
            }
        }

        return boardString;
    }

    public static Client getUserClient() {
        return client;
    }

    public static Client getComputerClient() {
        return computerClient;
    }

    public static void setOpponent(String name) {
        opponentsName = name;
        GameView.setEnemyLabel();
    }

    public static void setRadarEnabled(boolean enabled) {
        radarEnabled = enabled;
    }

    public static Client getClient() {
        return client;
    }

    public static boolean isOnline() {
        return isOnline;
    }

    public static void setOnline(boolean online) {
        isOnline = online;
        System.out.println(isOnline);
    }

    public static void setName(String _name) {
        name = _name;
    }

    public static void setIP(String _ip) {
        ip = _ip;
    }

    public static void setPort(String _port) {
        port = _port;
    }
}
