package puzzle;

/**
 * A puzzle.Tile in the puzzle board
 */
public class Tile {
    private int number;

    public Tile(int number) {
        setNumber(number);
    }

    public void setNumber(int number) { this.number = number; }
    public int getNumber() { return number; }
}

