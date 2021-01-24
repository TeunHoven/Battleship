package model.ship;

import model.GameManager;

public class BattleShip extends Ship {

    public BattleShip(int posX, int posY, boolean isHorizontal) {
        super(4,4, posX ,posY, "Battleship #" + (GameManager.getBattleshipShips()[1]), isHorizontal);
    }
}
