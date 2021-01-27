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

    /**
     * Returns the ships length.
     * @return An int depicting the length of the ship
     */
    public int getShipLength(){
        return shipLength;
    }

    /**
     * Returns the amount of live the ship has left.
     * @return The amount of lives the ship has left
     */
    public int getShipLives() {
        return shipLives;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    /**
     * Reduces the amount of lives of a ship.
     * Also checks whether the ship is sunk.
     */
    public void hitShip() {
        if(shipLives == 0) {
            System.out.println("Ship is already dead");
        } else if(shipLives == 1) {
            shipLives = 0;
            System.out.println("Dead");
        } else {
            shipLives--;
            System.out.println("No dead");
        }
    }

    /**
     * Returns the name of this ship
     * @return The name of the current ship
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the variable that depicts whether the ship is placed horizontally or vertically.
     * @return True if the Ship is placed horizontally
     */
    public boolean isHorizontal() {
        return isHorizontal;
    }

    /**
     * Sets the variable that shows whether the ship is placed horizontally or vertically.
     * @param horizontal True if the Ship is placed horizontally
     */
    public void setHorizontal(boolean horizontal) {
        this.isHorizontal = horizontal;
    }

    /**
     * Returns the current information of the Ship in a String format.
     * @return A string containing the information of the Ship
     */
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
