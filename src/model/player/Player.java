package model.player;

import model.ship.Ship;

public class Player {
    private String name;
    private Ship[] ships;
    private int points;
    private boolean isReady;

    public Player(String name) {
        this.isReady = false;
        setName(name);

        resetPoints();
    }

    /**
     * Sets the players name
     * @param name - The players name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this player.
     * @return The name of the player
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the ships of this player.
     * @param ships - An array of ships to be assigned to the player
     */
    public void setShips(Ship[] ships) {
        this.ships = ships;
    }

    /**
     * Returns the ships of the player.
     * @return An array of Ships that belong to the player
     */
    public Ship[] getShips() {
        return ships;
    }

    /**
     * Resets the player's points.
     */
    public void resetPoints() {
        points = 0;
    }

    /**
     * Adds a point to the player's total points.
     */
    public void addPoint() {
        points += 1;
    }

    /**
     * Returns the current points of the player.
     * @return The current amount of points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns whether the player is ready to play.
     * @return True if the player is ready to play
     */
    public boolean isReady() {
        return isReady;
    }

    /**
     * Sets the player to ready.
     */
    public void setReady() {
        this.isReady = true;
    }
}
