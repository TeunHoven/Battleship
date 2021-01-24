package model.ship;

import model.GameManager;

public class PatrolBoatShip extends Ship {

    public PatrolBoatShip(int posX, int posY, boolean isHorizontal) {
        super(1,1, posX ,posY, "" + (GameManager.getPatrolBoatShips()[1]), isHorizontal);
    }
}
