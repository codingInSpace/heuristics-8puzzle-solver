package puzzle;
import java.util.*;


public class Board {
    private ArrayList<Tile> tiles;
    private Deque<Integer> numbers;
    private final int N_TILES = 9;
    private final int N_COLS_ROWS = 3;

    public Board(boolean solution) {
        tiles = new ArrayList<Tile>();
        numbers = new LinkedList<Integer>();

        if (solution)
            setSolutionTiles();
        else
            setRandomTiles();

        init();
    }

    private void setRandomTiles() {
        int[] RANDOM = new int[] { 7, 5, 2, 4, 0, 6, 3, 8, 1 };

        for (int i = 0; i < N_TILES; i++) {
            numbers.addLast(RANDOM[i]);
        }
    }

    private void setSolutionTiles() {
        final int[] GOAL = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 0 };

        for (int i = 0; i < N_TILES; i++) {
           numbers.addLast(GOAL[i]);
        }
    }

    private void init() {
        for (int i = 0; i < N_COLS_ROWS; i++) {
            for (int j = 0; j < N_COLS_ROWS; j++) {
                Tile tile = new Tile(numbers.removeFirst(), i+1, j+1);
                tiles.add(tile);
            }
        }
    }

    public void printBoardTiles() {
        for (int i = 0; i < N_TILES; i++) {
            tiles.get(i).printValues();
        }
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void printBoard() {
        System.out.println("Board:");

        for (int i = 0; i < tiles.size(); i++) {
            if (i != 0 && i % 3 == 0)
                System.out.println();

            System.out.print(tiles.get(i).getNumber() + " ");
        }
        
        System.out.println();
    }
}
