package model.ship;

import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Ship {
    protected String name;
    protected int shipLength;
    protected int shipLives;
    protected int posX;
    protected int posY;
    protected boolean isHorizontal;

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
        if(shipLives <= 1) {
            shipLives = 0;
            System.out.println("Deade");
        } else {
            shipLives--;
            System.out.println("No deade");
        }
    }

    public String getName() {
        return name;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.isHorizontal = horizontal;
    }

    @Override
    public String toString() {
        String nameString = "Ship";
        if(name.length() > 2) {
            nameString = name.substring(0, name.length() - 3);
        } else {
            nameString = "Patrol Boat";
        }

        return nameString;
    }
}
