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
        int[] RANDOM = new int[] { 7, 5, 2, 4, 1, 6, 3, 8, 0 };

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

    public void printBoard() {
        ArrayList<Integer> tilesFirstRow = new ArrayList<>();
        ArrayList<Integer> tilesSecondRow = new ArrayList<>();
        ArrayList<Integer> tilesThirdRow = new ArrayList<>();

        for (int i = 0; i < N_TILES; i++) {
            if (tiles.get(i).getRow() == 1)
                tilesFirstRow.add(tiles.get(i).getNumber());
            else if (tiles.get(i).getRow() == 2)
                tilesSecondRow.add(tiles.get(i).getNumber());
            else if (tiles.get(i).getRow() == 3)
                tilesThirdRow.add(tiles.get(i).getNumber());
        }

        System.out.println("Board");
        System.out.println(tilesFirstRow.get(0) + " " + tilesFirstRow.get(1) + " " + tilesFirstRow.get(2));
        System.out.println(tilesSecondRow.get(0) + " " + tilesSecondRow.get(1) + " " + tilesSecondRow.get(2));
        System.out.println(tilesThirdRow.get(0) + " " + tilesThirdRow.get(1) + " " + tilesThirdRow.get(2));
        System.out.println(" ");
    }
}
