package model.ship;

public class Ship {
    int shipLength;
    int shipLives;
    int posX;
    int posY;

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

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void hitShip() {
        shipLives = shipLives - 1;
    }
}
