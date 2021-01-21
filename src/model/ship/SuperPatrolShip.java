package model.ship;

import model.GameManager;

public class SuperPatrolShip extends Ship {

    public SuperPatrolShip(int posX, int posY, boolean isHorizontal) {
        super(2,2, posX ,posY, "SP #" + (GameManager.getSuperPatrolShips()[1]+1), isHorizontal);
    }
}
