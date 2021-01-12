package model.board;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;

public class Board extends Parent {
    private int width, height;

    private ArrayList<Tile> tiles;
    private VBox rows;

    private String letterCoords = "ABCDEFGHIJKLMNO";

    private boolean setHorizontal;

    public Board () {
        this.width = 15;
        this.height = 10;

        this.tiles = new ArrayList<>();

        this.rows = new VBox();

        this.setHorizontal = true;

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
                tile.addEventFilter(MouseEvent.MOUSE_CLICKED, Controller.mouseEvent);
                tile.hoverProperty().addListener((observable, oldValue, newValue) -> onHover(tile));
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

    public Tile getTile(int x, int y) {
        int index = ((y)*15)+(x);

        if(index < 0 || index > 149)
            return null;

        return tiles.get(index);
    }

    public Tile[] getTileNeighboursHorizontal(Tile tile, int size) {
        Tile[] neighbours = new Tile[size+1];

        for(int i=0; i <= size; i++) {
            if(setHorizontal) {
                neighbours[i] = getTile(tile.getXPos() + i, tile.getYPos());
            } else {
                neighbours[i] = getTile(tile.getXPos(), tile.getYPos() + i);
            }
        }

        return neighbours;
    }

    private void onHover(Tile tile) {
        Tile[] neighbourTiles = tile.getBoard().getTileNeighboursHorizontal(tile, 5);
        boolean validPlacement = !Arrays.asList(neighbourTiles).contains(null);

        for (Tile t : tiles) {
            if(validPlacement && Arrays.asList(neighbourTiles).contains(t)) {
                t.setColor(Color.GRAY);
            } else {
                t.setColor(Color.BLUE);
            }
        }
    }
}
