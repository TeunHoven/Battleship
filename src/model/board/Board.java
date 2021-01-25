package model.board;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import model.player.ComputerPlayer;
import model.player.Player;
import model.ship.Ship;

import java.util.ArrayList;
import java.util.Arrays;

public class Board extends Parent {
    private Player player;

    private int width, height;

    private ArrayList<Tile> tiles;
    private VBox rows;

    private String letterCoords = "ABCDEFGHIJKLMNO";

    private boolean isHorizontal;

    public Board (Player player) {
        this.player = player;

        this.width = 15;
        this.height = 10;

        this.tiles = new ArrayList<>();

        this.rows = new VBox();

        this.isHorizontal = true;

        setUpBoard();
    }

    // Creates a board 15 wide, 10 high
    private void setUpBoard() {
        HBox letterCoordBox = new HBox(); // Horizontal Coordinates Box

        for(int y=0; y < height; y++) {
            HBox row = new HBox();

            // Puts a digit in front of the row, vertical coordinates (1 ... 10)
            Label digit = new Label("" + (y+1) + " ");
            digit.setFont(new Font(Tile.SIZE-10));
            digit.setAlignment(Pos.CENTER);
            digit.setPrefWidth(Tile.SIZE);
            row.getChildren().add(digit);

            for(int x=0; x < width; x++) {
                // Puts a letter above each row, Horizontal coordinates (A ... O)
                if(y == 0) {
                    Label letter = new Label("" + letterCoords.toCharArray()[x]);
                    letter.setFont(new Font(Tile.SIZE-10));
                    letter.setTranslateX(Tile.SIZE);
                    letter.setAlignment(Pos.CENTER);
                    letter.setPrefWidth(Tile.SIZE+1);
                    letterCoordBox.getChildren().add(letter);
                }

                // Creates all the tiles in the board
                Tile tile = new Tile(x, y, this);

                // Creates the mouse events
                tile.addEventFilter(MouseEvent.MOUSE_CLICKED, Controller.sharedInstance.mouseEvent);
                tile.hoverProperty().addListener((observable, oldValue, newValue) -> Controller.sharedInstance.onHover(tile));
                tiles.add(tile);
                row.getChildren().add(tile);
            }
            if(y == 0) {
                rows.getChildren().add(letterCoordBox);
            }
            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }

    // Gets tile from x, y coordinates on the board
    public Tile getTile(int x, int y) {
        int index = ((y)*width)+(x);

        if(index < 0 || index > 149)
            return null;

        return tiles.get(index);
    }

    // Gets neigbour tiles from a specific size
    public Tile[] getTileNeighboursHorizontal(Tile tile, int size) {
        Tile[] neighbours = new Tile[size];

        for(int i=0; i < size; i++) {
            if(isHorizontal()) {
                if(tile.getXPos()+i < 15) {
                    neighbours[i] = getTile(tile.getXPos() + i, tile.getYPos());
                } else {
                    neighbours[i] = null;
                }
            } else {
                neighbours[i] = getTile(tile.getXPos(), tile.getYPos() + i);
            }
        }

        return neighbours;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public Player getPlayer(){
        return player;
    }

    public void setShip(Tile tile, Ship ship) {
        double width = (30*ship.getShipLength())-10;
        double labelCentre = (width/2)-(3*ship.getName().toCharArray().length);

        int x = 5+(tile.getXPos()+1)*(Tile.SIZE+1);
        int y = 5+(tile.getYPos()+1)*(Tile.SIZE+1);

        Label label = new Label(ship.getName());
        Rectangle rect = new Rectangle(20, 20);
        label.setFont(new Font(13));

        rect.setFill(Color.BLACK);
        label.setTextFill(Color.WHITE);

        if(!ship.isHorizontal()) { // Vertical
            // Ship rectangle
            rect.setX(x);
            rect.setY(y);
            rect.setHeight(width);

            Rotate rotation = new Rotate();

            rotation.setPivotX(label.getTranslateX());
            rotation.setPivotY(label.getTranslateY());

            label.getTransforms().add(rotation);
            rotation.setAngle(90);

            label.setTranslateX(25+(tile.getXPos()+1)*(Tile.SIZE+1));
            label.setTranslateY(rect.getY()+labelCentre);
        } else { // Horizontal
            // Ship rectangle
            rect.setX(x);
            rect.setY(y);
            rect.setWidth(width);

            label.setTranslateX(rect.getX()+labelCentre);
            label.setTranslateY(5+(tile.getYPos()+1)*(Tile.SIZE+1));
        }

        getChildren().add(rect);
        getChildren().add(label);
    }

    public void setShot(Tile tile, boolean hit) {
        int x = (tile.getXPos()+1)*(Tile.SIZE+1);
        int y = (tile.getYPos()+1)*(Tile.SIZE+1);

        Rectangle rect = new Rectangle(20, 20);
        rect.setFill(Color.GRAY);

        rect.setTranslateX(x+5);
        rect.setTranslateY(y+5);

        if(hit) {
            rect.setFill(Color.RED);
        }

        getChildren().add(rect);
    }

    public void setColor(Color c) {
        for(Tile t: tiles) {
            t.setFill(c);
        }
    }
}
