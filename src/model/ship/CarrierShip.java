package model.ship;

import model.GameManager;

public class CarrierShip extends Ship {

    public CarrierShip(int posX, int posY, boolean isHorizontal) {
        super(5,5, posX ,posY, "Carrier #" + (GameManager.getCarrierShips()[1]), isHorizontal);
    }
}
