package model.ship;

public class Ship {
    String name;
    int shipLength;
    int shipLives;
    int posX;
    int posY;
    boolean isHorizontal;

    public Ship(int shipLength, int shipLives, int posX, int posY, String name, boolean isHorizontal) {
        this.shipLength = shipLength;
        this.shipLives = shipLives;
        this.posX = posX;
        this.posY = posY;
        this.name = name;
        this.isHorizontal = isHorizontal;
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
