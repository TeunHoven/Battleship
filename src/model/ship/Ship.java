package model.ship;

public class Ship {
    int shipLength;
    int shipLives;
    int posX;
    int posY;

    public Ship(int shipLength, int shipLives, int posX, int posY) {
        this.shipLength = shipLength;
        this.shipLives = shipLives;
        this.posX = posX;
        this.posY = posY;
    }

    public int getShipLength(){
        return shipLength;
    }

    public int getShipLives() {
        return shipLives;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void hitShip() {
        shipLives = shipLives - 1;
    }
}
