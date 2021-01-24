package model.ship;

import model.GameManager;

public class DestroyerShip extends Ship {

    public DestroyerShip(int posX, int posY, boolean isHorizontal) {
        super(3,3, posX ,posY, "Destroyer #" + (GameManager.getDestroyerShips()[1]), isHorizontal);
    }
}
