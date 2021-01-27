package model.board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.ship.Ship;

public class Tile extends Rectangle {
    public static final int SIZE = 30;
    private int x, y;
    private Ship ship;
    private boolean hasShip;
    private boolean isShot;
    private Color tileColor;
    private Board board;

    public Tile(int x, int y, Board board) {
        super(SIZE, SIZE);
        this.x = x;
        this.y = y;
        this.ship = null;
        this.hasShip = false;
        this.isShot = false;
        this.tileColor = Color.BLUE;
        this.board = board;

        setUp();
    }

    /**
     * Set's up the tile with it's base colour.
     */
    private void setUp() {
        setFill(tileColor);
        setStroke(Color.BLACK);
    }

    /**
     * Assigns a certain ship on this tile.
     * @param ship - The ship to be placed on this tile
     */
    public void setShip(Ship ship) {
        this.ship = ship;
    }

    /**
     * Returns the ship currently located on the tile
     * @return an instance of Ship or null if no ship is located on the tile
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * Sets a variable to determine if this tile has a ship assigned to it.
     * @param hasShip - True if the tile has a ship placed on it
     */
    public void setHasShip(boolean hasShip) {
        this.hasShip = hasShip;
    }

    /**
     * Returns whether this tile has a ship assigned to it.
     * @return True if the tile has a ship placed on it
     */
    public boolean hasShip() {
        return hasShip;
    }

    /**
     * Fills the tile with a selected color.
     * @param color - The colour selected to fill the tile
     */
    public void setColor(Color color) {
        this.tileColor = color;
        setFill(tileColor);
    }

    /**
     * Returns the board this tile is located on.
     * @return - The board this tile is assigned to
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the X coordinate of this tile.
     * @return - The X coordinate of this tile
     */
    public int getXPos() {
        return x;
    }

    /**
     * Returns the Y coordinate of this tile.
     * @return - The Y coordinate of this tile
     */
    public int getYPos() {
        return y;
    }

    /**
     * Returns whether or not this tile has already been shot.
     * @return - True if the tile has already been shot
     */
    public boolean isShot(){
        return isShot;
    }

    /**
     * Sets the variable that determines whether or not the tile has already been shot.
     * @param isShot - True if the tile has been shot
     */
    public void setIsShot(boolean isShot){
        this.isShot = isShot;
        if(hasShip())
            ship.hitShip();
    }
}
