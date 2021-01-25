package model;

import controller.GameState;
import javafx.scene.paint.Color;
import model.board.Board;
import model.player.ComputerPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import model.ship.*;
import view.GameView;

public class GameManager {
    private static GameState gameState = GameState.SETUP;

    private static int[] patrolBoatShips = {10, 0};
    private static int[] superPatrolShips = {8, 0};
    private static int[] destroyerShips = {5, 0};
    private static int[] battleshipShips = {3, 0};
    private static int[] carrierShips = {2, 0};

    private static int[] userKills ={0, 0, 0, 0 ,0};
    private static int[] opponentKills ={0, 0, 0, 0 ,0};

    private static Player user, opponent;
    private static Board userBoard, enemyBoard;
    private static String winner = "DRAW";

    public static void setUp() {
        user = new HumanPlayer("Teun");
        opponent = new ComputerPlayer();

        userBoard = new Board(user);
        enemyBoard = new Board(opponent);

        if(opponent instanceof ComputerPlayer) {
            ((ComputerPlayer) opponent).setUp();
        }
    }

    public static void checkRound() {
        switch(gameState) {
            case SETUP:
                if(userBoard.getPlayer().isReady() && enemyBoard.getPlayer().isReady()) {
                    userBoard.setColor(Color.BLUE);
                    setGameState(GameState.USERROUND);
                }
                GameView.setRound("Setup");
                break;
            case USERROUND:
                GameView.setRound("Your round");
                GameView.setMessage("Click a tile!");
                break;
            case ENEMYROUND:
                GameView.setRound("Opponents round");
                break;
            case END:
                break;
            default:
                break;
        }
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState gamestate){
        gameState = gamestate;
    }

    public static boolean canAddPatrolBoat(){
        if(patrolBoatShips[1] < patrolBoatShips[0]) {
            patrolBoatShips[1]++;
            return true;
        }

        return false;
    }

    public static boolean canAddSuperPatrol(){
        if(superPatrolShips[1] < superPatrolShips[0]) {
            superPatrolShips[1]++;
            return true;
        }

        return false;
    }

    public static boolean canAddDestroyerShips(){
        if(destroyerShips[1] < destroyerShips[0]) {
            destroyerShips[1]++;
            return true;
        }

        return false;
    }

    public static boolean canAddBattleshipShips(){
        if(battleshipShips[1] < battleshipShips[0]) {
            battleshipShips[1]++;
            return true;
        }

        return false;
    }

    public static boolean canAddCarrierShips(){
        if(carrierShips[1] < carrierShips[0]) {
            carrierShips[1]++;
            return true;
        }

        return false;
    }

    // Checks for winner, if w == 0 -> No winner, if w == 1 -> User is winner, if w == 2 -> Opponent is winner.
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

    public static void setOutcome() {
        if(user.getPoints() > opponent.getPoints()) {
            setWinner(user);
        } else if (user.getPoints() < opponent.getPoints()){
            setWinner(opponent);
        } else {
            if(getUserKills()[0] != getOpponentKills()[0]){
                if(getUserKills()[0] > getOpponentKills()[0]){
                    setWinner(user);
                }
                setWinner(opponent);
            } else if(getUserKills()[1] != getOpponentKills()[1]){
                if(getUserKills()[1] > getOpponentKills()[1]){
                    setWinner(user);
                }
                setWinner(opponent);
            } else if(getUserKills()[2] != getOpponentKills()[2]){
                if(getUserKills()[2] > getOpponentKills()[2]){
                    setWinner(user);
                }
                setWinner(opponent);
            } else if(getUserKills()[3] != getOpponentKills()[3]){
                if(getUserKills()[3] > getOpponentKills()[3]){
                    setWinner(user);
                }
                setWinner(opponent);
            } else if(getUserKills()[4] != getOpponentKills()[4]) {
                if (getUserKills()[4] > getOpponentKills()[4]) {
                    setWinner(user);
                }
                setWinner(opponent);
            }
        }

    }

    public static int[] getPatrolBoatShips() {
        return patrolBoatShips;
    }

    public static int[] getSuperPatrolShips() {
        return superPatrolShips;
    }

    public static int[] getDestroyerShips() {
        return destroyerShips;
    }

    public static int[] getBattleshipShips() {
        return battleshipShips;
    }

    public static int[] getCarrierShips() {
        return carrierShips;
    }

    public static Board getUserBoard() {
        return userBoard;
    }

    public static Board getOpponentBoard() {
        return enemyBoard;
    }

    public static Player getUser() {
        return user;
    }

    public static Player getOpponent() {
        return opponent;
    }

    public static void setWinner(Player player){
        winner = player.getName();
    }

    public static String getWinner(){
        return winner;
    }

    // adds a kill to the kill list of the player
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
        }
    }

    public static int[] getUserKills(){
        return userKills;
    }

    public static int[] getOpponentKills(){
        return opponentKills;
    }
}
