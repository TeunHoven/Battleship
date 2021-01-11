package model.board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.ship.Ship;

public class Tile extends Rectangle {
    private final int size = 30;
    private int x, y;
    private Ship ship;
    private boolean hasShip;
    private Color tileColor;

    public Tile(int x, int y) {
        super(30, 30);
        this.x = x;
        this.y = y;
        this.ship = null;
        this.hasShip = false;
        this.tileColor = Color.BLUE;

        setUp();
    }

    private void setUp() {
        setFill(tileColor);
        setStroke(Color.BLACK);
    }

    public int getSize() {
        return size;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Ship getShip() {
        return ship;
    }

    public void setHasShip(boolean hasShip) {
        this.hasShip = hasShip;
    }

    public boolean hasShip() {
        return hasShip;
    }

    public void setColor(Color color) {
        this.tileColor = color;
    }
}
