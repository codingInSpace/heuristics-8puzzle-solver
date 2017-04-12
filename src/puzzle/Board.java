package puzzle;
import java.util.*;


public class Board {
    private ArrayList<Tile> tiles;
    private Deque<Integer> numbers;
    private final int N_TILES = 9;
    private final int N_COLS_ROWS = 3;

    public Board(boolean isSolution) {
        tiles = new ArrayList<Tile>();
        numbers = new LinkedList<Integer>();

        if (isSolution)
            setSolutionTiles();
        else
            setRandomTiles();

        initTiles();
    }

    public Board(ArrayList<Tile> tiles) {
        this.tiles = (ArrayList<Tile>)tiles.clone();
    }

    private void setRandomTiles() {
        //int[] RANDOM = new int[] { 1, 0, 2, 4, 5, 3, 7, 8, 6 }; // 3 moves
        int[] RANDOM = new int[] { 0, 1, 3, 4, 2, 5, 7, 8, 6 }; // 4 moves
        //int[] RANDOM = new int[] { 1, 3, 4, 8, 0, 5, 7, 2, 6 }; // not solvable

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

    private void initTiles() {
        for (int i = 0; i < N_COLS_ROWS; i++) {
            for (int j = 0; j < N_COLS_ROWS; j++) {
                Tile tile = new Tile(numbers.removeFirst(), i+1, j+1);
                tiles.add(tile);
            }
        }
    }

    public boolean isSolvable() {
        int inversions = 0;

        for(int i = 0; i < tiles.size() - 1; i++) {
            for(int j = i + 1; j < tiles.size(); j++) {
                if(tiles.get(i).getNumber() > tiles.get(j).getNumber())
                    inversions++;
            }

            if(tiles.get(i).getNumber() == 0 && i % 2 == 1)
                inversions++;
        }

        return (inversions % 2 == 0);
    }

    public void printBoardTiles() {
        for (int i = 0; i < N_TILES; i++) {
            tiles.get(i).printValues();
        }
    }

    public boolean equals(Object x) {
        if (x == this)
            return true;
        if (x == null)
            return false;
        if (x.getClass() != this.getClass())
            return false;

        Board that = (Board) x;

        ArrayList<Tile> thoseTiles = that.getTiles();

        if (thoseTiles.size() != tiles.size())
            return false;

        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getNumber() != thoseTiles.get(i).getNumber())
                return false;
        }

        return true;
    }

    public void swap(int index, int offset) {
        //System.out.println("Swapping " + offset);
        Tile temp = tiles.get(index);
        this.tiles.set(index, tiles.get(index + offset));
        this.tiles.set(index + offset, temp);
    }

    public Iterable<Board> getNeighbors() {
        Stack<Board> neighborBoards = new Stack<Board>();

        //System.out.println("Getting neighbors for");
        //this.printBoard();

        // The neighbors that are available
        boolean left = false;
        boolean right = false;
        boolean up = false;
        boolean down = false;

        int index = 0;
        ArrayList<Tile> originalTiles = (ArrayList<Tile>)tiles.clone();

        // Get index where tile number is 0
        for (int i = 0; i < originalTiles.size(); i++) {
            if (originalTiles.get(i).getNumber() == 0) {

                // Set which neighbors are available
                index = i+1;
                left = index % 3 != 1;
                right = index % 3 > 0;
                up = index > 3;
                down = index < 7;

                break;
            }
        }

        // Correct index at number 0 starting count from 0
        index = index - 1;

        Board board = new Board(originalTiles);

        if (left) {
            board.swap(index,  -1);
            neighborBoards.push(board);
            board = new Board(originalTiles);
        }

        if (down) {
            board.swap(index, 3);
            neighborBoards.push(board);
            board = new Board(originalTiles);
        }

        if (right) {
            board.swap(index, 1);
            neighborBoards.push(board);
            board = new Board(originalTiles);
        }

        if (up) {
            board.swap(index,  -3);
            neighborBoards.push(board);
        }

        return neighborBoards;
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
        System.out.println();
    }
}
