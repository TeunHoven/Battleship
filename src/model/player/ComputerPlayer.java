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
    private Ship currShip;
    private Board board;
    private boolean isPlaced;

    public ComputerPlayer() {
        super("Computer");
    }

    public void setUp() {
        this.board = GameManager.getOpponentBoard();
        this.isPlaced = false;

        setShips();
    }

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

            if(ships[0] == 10 && ships[1] == 8 && ships[2] == 5 && ships[3] == 3 && ships[4] == 2) {
                isPlaced = true;
            }
        }
    }

    private void placeShip(int x, int y, boolean horizontal) {
        Tile tile = board.getTile(x, y);
        board.setHorizontal(horizontal);
        currShip.setHorizontal(horizontal);
        Tile[] tiles = board.getTileNeighboursHorizontal(tile, currShip.getShipLength());

        for(Tile t: tiles) {
            t.setShip(currShip);
            t.setHasShip(true);
        }

        board.setShip(tile, currShip);
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

    private boolean hasShipOnTile(Tile[] tiles) {
        for(Tile t: tiles) {
            if(t.hasShip()) {
                return true;
            }
        }

        return false;
    }

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
}
