package model.ship;

public class Ship {
    String name;
    int shipLength;
    int shipLives;
    int posX;
    int posY;

    public Ship(int shipLength, int shipLives, int posX, int posY, String name) {
        this.shipLength = shipLength;
        this.shipLives = shipLives;
        this.posX = posX;
        this.posY = posY;
        this.name = name;
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

    public String getName() {
        return name;
    }
}
