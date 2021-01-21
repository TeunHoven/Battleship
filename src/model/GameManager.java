package model;

import controller.GameState;
import model.board.Board;
import model.player.HumanPlayer;
import model.player.Player;

public class GameManager {
    private static GameState gameState = GameState.SETUP;

    private static int[] patrolBoatShips = {10, 0};
    private static int[] superPatrolShips = {8, 0};
    private static int[] destroyerShips = {5, 0};
    private static int[] battleshipShips = {3, 0};
    private static int[] carrierShips = {2, 0};

    private static Player user, opponent;
    private static Board userBoard, enemyBoard;

    public static void setUp() {
        user = new HumanPlayer("Teun");
        opponent = new HumanPlayer("Tim");

        userBoard = new Board(user);
        enemyBoard = new Board(opponent);
    }

    public static GameState getGameState() {
        return gameState;
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
}
