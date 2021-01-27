package model.player;

import javafx.scene.paint.Color;
import model.GameManager;
import model.board.Board;
import model.board.Tile;
import model.ship.*;

import java.util.Arrays;
import java.util.Random;

public class ComputerPlayer extends Player {
    private int[] ships = {0, 0, 0, 0, 0}; // PatrolBoat, SuperPatrol, Destroyer, Battleship, Carrier
    private int[] direction = {1, -1, 1, -1};
    private static int[] velocity = {0 , 0};
    private Tile hitShip;
    private Ship currShip;
    private Board board;
    private Board userBoard;
    private boolean isPlaced;
    private boolean hasTriedSameDirection = false;

    public ComputerPlayer() {
        super("Computer");
    }

    public void setUp() {
        this.board = GameManager.getOpponentBoard();
        this.userBoard = GameManager.getUserBoard();
        this.isPlaced = false;

        setShips();
    }

    /**
     * Randomly places the ships on the board for the ComputerPlayer.
     */
    private void setShips() {
        while(!isPlaced) {
            while(ships[4] < 2) { // Carrier ships 2 max
                int x = (int) (14*Math.random());
                int y = (int) (9*Math.random());
                int horizontal = (int) (2*Math.random()); // 0 horizontal, 1 vertical
                boolean hasPlaced = false;

                currShip = new CarrierShip(x, y, true);
                switch (canPlaceShip(x, y)) {
                    case 0 -> {
                        placeShip(x, y, horizontal == 0);
                        hasPlaced = true;
                    }
                    case 1 -> {
                        placeShip(x, y, true);
                        hasPlaced = true;
                    }
                    case 2 -> {
                        placeShip(x, y, false);
                        hasPlaced = true;
                    }
                }

                if(hasPlaced) {
                    ships[4]++;
                }
            }

            while(ships[3] < 3) { // Battleships 3 max
                int x = (int) (14*Math.random());
                int y = (int) (9*Math.random());
                int horizontal = (int) (2*Math.random()); // 0 horizontal, 1 vertical
                boolean hasPlaced = false;

                currShip = new BattleShip(x, y, true);
                switch (canPlaceShip(x, y)) {
                    case 0 -> {
                        placeShip(x, y, horizontal == 0);
                        hasPlaced = true;
                    }
                    case 1 -> {
                        placeShip(x, y, true);
                        hasPlaced = true;
                    }
                    case 2 -> {
                        placeShip(x, y, false);
                        hasPlaced = true;
                    }
                }

                if(hasPlaced) {
                    ships[3]++;
                }
            }

            while(ships[2] < 5) { // Destroyer ships 5 max
                int x = (int) (14*Math.random());
                int y = (int) (9*Math.random());
                int horizontal = (int) (2*Math.random()); // 0 horizontal, 1 vertical
                boolean hasPlaced = false;

                currShip = new DestroyerShip(x, y, true);
                switch (canPlaceShip(x, y)) {
                    case 0 -> {
                        placeShip(x, y, horizontal == 0);
                        hasPlaced = true;
                    }
                    case 1 -> {
                        placeShip(x, y, true);
                        hasPlaced = true;
                    }
                    case 2 -> {
                        placeShip(x, y, false);
                        hasPlaced = true;
                    }
                }

                if(hasPlaced) {
                    ships[2]++;
                }
            }

            while(ships[1] < 8) { // Super Patrol 8 max
                int x = (int) (14*Math.random());
                int y = (int) (9*Math.random());
                int horizontal = (int) (2*Math.random()); // 0 horizontal, 1 vertical
                boolean hasPlaced = false;

                currShip = new SuperPatrolShip(x, y, true);
                switch (canPlaceShip(x, y)) {
                    case 0 -> {
                        placeShip(x, y, horizontal == 0);
                        hasPlaced = true;
                    }
                    case 1 -> {
                        placeShip(x, y, true);
                        hasPlaced = true;
                    }
                    case 2 -> {
                        placeShip(x, y, false);
                        hasPlaced = true;
                    }
                }

                if(hasPlaced) {
                    ships[1]++;
                }
            }

            while(ships[0] < 10) { // Patrol boats 10 max
                int x = (int) (14*Math.random());
                int y = (int) (9*Math.random());
                boolean hasPlaced = false;

                currShip = new PatrolBoatShip(x, y, true);
                if(canPlaceShip(x, y) == 0 || canPlaceShip(x, y) == 1) {
                    placeShip(x, y, true);
                    hasPlaced = true;
                }

                if(hasPlaced) {
                    ships[0]++;
                }
            }

            //Final check to ensure all ships are placed
            if(ships[0] == 10 && ships[1] == 8 && ships[2] == 5 && ships[3] == 3 && ships[4] == 2) {
                isPlaced = true;
                setReady();
            }
        }
    }

    /**
     * Assigns a certain ship to a Tile with the given coordinates.
     * @param x - The X coordinate of the selected Tile
     * @param y - THe Y coordinate of the selected Tile
     * @param horizontal - True if the ship is to be place horizontally, false if the ship is to be placed vertically
     */
    private void placeShip(int x, int y, boolean horizontal) {
        Tile tile = board.getTile(x, y);
        board.setHorizontal(horizontal);
        currShip.setHorizontal(horizontal);
        Tile[] tiles = board.getTileNeighboursHorizontal(tile, currShip.getShipLength());

        for(Tile t: tiles) {
            t.setShip(currShip);
            t.setHasShip(true);
        }
    }

    /**
     * 0 when ship both horizontal && vertical can be placed, 1 only horizontal, 2 only vertical, 3 not
     * @param x Horizontal placement
     * @param y Vertical placement
     * @return int of the ship can be placed
     */
    private int canPlaceShip(int x, int y) {
        Tile tile = board.getTile(x, y);
        board.setHorizontal(true);
        Tile[] tiles = board.getTileNeighboursHorizontal(tile, currShip.getShipLength());

        boolean canHorizontal = isValidPlacement(tile);
        if(canHorizontal) {
            boolean hasShipOnOneTile = hasShipOnTile(tiles);

            if (hasShipOnOneTile) {
                canHorizontal = false;
            }
        }

        board.setHorizontal(false);
        tiles = board.getTileNeighboursHorizontal(tile, currShip.getShipLength());
        boolean canVertical = isValidPlacement(tile);

        if(canVertical) {
            boolean hasShipOnOneTile = hasShipOnTile(tiles);

            if (hasShipOnOneTile) {
                canVertical = false;
            }
        }

        if(canHorizontal && canVertical) {
            return 0;
        } else if(canHorizontal) {
            return 1;
        } else if(canVertical) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * Checks whether all the Tiles given have a ship assigned to them.
     * @param tiles - The Tiles that need to be checked
     * @return True if all Tiles have a ship on them
     */
    private boolean hasShipOnTile(Tile[] tiles) {
        for(Tile t: tiles) {
            if(t.hasShip()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether the selected placement is valid.
     * @param tile - The tile selected
     * @return True if all the neighbouring tiles are inbounds and are in a straight line
     */
    private boolean isValidPlacement(Tile tile) {
        Tile[] tiles = board.getTileNeighboursHorizontal(tile, currShip.getShipLength());

        if(Arrays.asList(tiles).contains(null)) {
            return false;
        }

        if(board.isHorizontal()) {
            for(Tile t: tiles) {
                if(t.getYPos() != tile.getYPos()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Produces a random X, Y coordinate to fire a shot at.
     * @return An int[] with X and Y coordinates
     */
    public int[] randomShot(){
        int x = (int) (Board.WIDTH*Math.random());
        int y = (int) (Board.HEIGHT*Math.random());
        System.out.println("Random Random Random shot, X: " + x + "; Y: " + y);
        int[] shot = {x, y};
        return shot;
    }

    /**
     * Returns a random neighbouring tile.
     * @param tile - The selected tile
     * @return A neighbouring Tile
     */
    public Tile shootRandomNeighbour(Tile tile){
        int[] posXY = {tile.getXPos(), tile.getYPos()};
        int i = (int) (Math.random()*4);

        if(Arrays.equals(direction, new int[]{0, 0, 0, 0})) {
            return null;
        }

        while(direction[i] == 0) {
            i = (int) (Math.random() * 4);
        }
        if (i < 2) {
            posXY[0] = posXY[0] + direction[i];
        } else {
            posXY[1] = posXY[1] + direction[i];
        }

        System.out.println("Shoot Random Index X: " + posXY[0] + "; Index Y: " + posXY[1]);
        System.out.println("Velocity X: " + velocity[0] + "; Velocity Y: " + velocity[1]);
        direction[i] = 0;
        return userBoard.getTile(posXY[0], posXY[1]);
    }

    /**
     * Returns a neighbouring tile based on the velocity produced by previously hitting a ship.
     * @param tile - The selected tile
     * @return A neighbouring tile
     */
    public Tile shootNeighbour(Tile tile) {
        int[] posXY = {tile.getXPos(), tile.getYPos()};

        posXY[0] = posXY[0] + velocity[0];
        posXY[1] = posXY[1] + velocity[1];

        if(posXY[0] <= 0 || posXY[1] <= 0 || posXY[0] > Board.WIDTH-1 || posXY[1] > Board.HEIGHT-1) {
            turnAround();
            posXY[0] = posXY[0] + velocity[0];
            posXY[1] = posXY[1] + velocity[1];
        }

        System.out.println("Shoot Neighbour Index X: " + posXY[0] + "; Index Y: " + posXY[1]);
        System.out.println("Velocity X: " + velocity[0] + "; Velocity Y: " + velocity[1]);
        return userBoard.getTile(posXY[0], posXY[1]);
    }

    /**
     * Sets the velocity variable based on the given tiles
     * @param tile - The first tile to be compared
     * @param tile2 - The second tile to be compared
     */
    public void setVelocity(Tile tile, Tile tile2) {
        velocity = new int[]{tile.getXPos() - tile2.getXPos(), tile.getYPos() - tile2.getYPos()};
        if(velocity[0] < 0)
            velocity[0] = -1;
        else if (velocity[0] > 0)
            velocity[0] = 1;

        if(velocity[1] < 0)
            velocity[1] = -1;
        else if (velocity[1] > 0)
            velocity[1] = 1;

        System.out.println("Velocity X: " + velocity[0] + "; Velocity Y: " + velocity[1]);
    }

    /**
     * Changes the velocity in the opposite direction.
     */
    public void turnAround() {
        if(hasTriedSameDirection) {
            velocity[0] = 0;
            velocity[1] = 0;
            hasTriedSameDirection = false;
        } else {
            velocity[0] *= -1;
            velocity[1] *= -1;
            hasTriedSameDirection = true;
        }
        System.out.println("Velocity X: " + velocity[0] + "; Velocity Y: " + velocity[1]);
    }

    /**
     * Returns the current velocity with which the computer is targeting tiles.
     * @return An int[] with the current velocity
     */
    public int[] getVelocity(){
        return velocity;
    }

    /**
     * Resets the velocity.
     */
    public void resetVelocity(){
        velocity[0] = 0;
        velocity[1] = 0;
    }

    /**
     * Resets the targeting mode, computer player goes back to hunting mode.
     */
    public void resetShooting() {
        resetVelocity();
        hitShip = null;
        direction = new int[]{1, -1, 1, -1};
    }

    /**
     * Sets the variable that stores the tile of the last ship ComputerPlayer hit.
     * @param tile - The tile that was hit
     */
    public void setHitShip(Tile tile){
        this.hitShip = tile;
    }

    /**
     * Returns the tile of the last ship that ComputerPlayer hit.
     * @return The tile that was last hit
     */
    public Tile getHitShip(){
        return hitShip;
    }
}
