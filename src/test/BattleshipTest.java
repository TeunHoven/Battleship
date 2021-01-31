package test;

import Protocol.Exceptions.ServerUnavailableException;
import controller.Controller;
import model.GameManager;
import model.board.Board;
import model.board.Tile;
import model.player.ComputerPlayer;
import model.player.Player;
import model.ship.CarrierShip;
import model.ship.SuperPatrolShip;
import org.junit.jupiter.api.*;
import view.BattleshipMainView;

import static org.junit.jupiter.api.Assertions.*;


public class BattleshipTest {
    private static BattleshipMainView bmv;
    private static final GameManager manager = new GameManager();
    private static final Controller controller = Controller.sharedInstance;
    private static Board userBoard;
    private static Board opponentBoard;

    private static final int[] positionTile = {5, 5};

    @BeforeAll
    public static void setUp() throws ServerUnavailableException {
        bmv = new BattleshipMainView();
        GameManager.setUp();
        userBoard = GameManager.getUserBoard();
        opponentBoard = GameManager.getOpponentBoard();
    }

    @Test
    public void setShipTest() {
        Tile tile = userBoard.getTile(positionTile[0], positionTile[1]);
        assertNull(tile.getShip());
        userBoard.setShip(tile, new SuperPatrolShip(positionTile[0], positionTile[1], false));
        assertNotNull(tile.getShip());
    }
}
