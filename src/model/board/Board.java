package model.board;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class Board extends Parent {
    private int width, height;

    private ArrayList<Tile> tile;
    private VBox rows;

    private String letterCoords = "ABCDEFGHIJKLMNO";

    public Board () {
        this.width = 15;
        this.height = 10;

        this.rows = new VBox();

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
                Tile tile = new Tile(x, y);
                row.getChildren().add(tile);
            }
            if(y == 0) {
                rows.getChildren().add(letterCoordBox);
            }
            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }
}
