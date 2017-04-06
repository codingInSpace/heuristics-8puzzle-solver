package puzzle;

/**
 * A puzzle.Tile in the puzzle board
 */
public class Tile {
    private int number;
    private int row;
    private int col;

    public Tile(int number, int row, int col) {
        setNumber(number);
        setPosition(row, col);
    }

    public void setNumber(int number) { this.number = number; }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getNumber() { return number; }
    public int getRow() { return row; }
    public int getCol() { return col; }

    public void printValues() {
        System.out.println("Tile: " + number + ", row and col [" + row + ", " + col + "]");
    }
}

