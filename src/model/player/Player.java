package model.player;

import model.ship.Ship;

public class Player {
    String name;
    Ship[] ships;
    int points;
    boolean isReady;

    public Player(String name) {
        this.isReady = false;
        setName(name);

        resetPoints();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setShips(Ship[] ships) {
        this.ships = ships;
    }

    public Ship[] getShips() {
        return ships;
    }

    public void resetPoints() {
        points = 0;
    }

    public void addPoint() {
        points += 1;
    }

    public int getPoints() {
        return points;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady() {
        this.isReady = true;
    }
}
