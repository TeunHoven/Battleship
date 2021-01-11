package model.board;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Board extends Parent {
    private int width, height;

    private ArrayList<Tile> tile;
    private VBox rows;

    // Creates a board 15 wide, 10 height
    public Board () {
        this.width = 15;
        this.height = 10;

        this.rows = new VBox();
        for(int y=0; y < height; y++) {
            HBox row = new HBox();
            for(int x=0; x < width; x++) {
                Tile tile = new Tile(x, y);
                row.getChildren().add(tile);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }
}
