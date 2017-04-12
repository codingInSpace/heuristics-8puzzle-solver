package puzzle;

import java.util.*;

/**
 * Handles solving logic
 */
public class Solver {
    private Board board;
    private Board solutionBoard;
    private Stack<Board> solutionPath;
    private PriorityQueue<State> queue;
    private boolean h1;
    public final HashSet<ArrayList<Tile>> handledTileSets;
    private final int N_COLS_ROWS = 3;

    /**
     * Inner class State to keep track of board configuration
     */
    private class State {
        private Board board;
        private State previous;
        private int cost;
        private int gCost;
        private int hCost;

        State(Board board, int gCost, int hCost, State prev) {
            this.board = board;
            this.gCost = gCost;
            this.hCost = hCost;
            this.cost = this.gCost + this.hCost;
            this.previous = prev;
        }

        private int getCost() {
            return this.cost;
        }

        public boolean isGoal() {
            ArrayList<Tile> currentTiles = board.getTiles();

            if (currentTiles.size() == 0) return false;

            for (int i = 0; i < currentTiles.size(); i++) {
                int currentTilesNumber = currentTiles.get(i).getNumber();

                if (currentTilesNumber == 0) {
                    if (i != currentTiles.size() - 1)
                        return false;
                }

                if (currentTilesNumber != 0 && currentTilesNumber != (i+1))
                    return false;
            }

            return true;
        }
    }

    public Solver(Board initialBoard, boolean heuristicOne) {
        this.board = initialBoard;
        this.solutionBoard = new Board(true);
        this.solutionPath = new Stack<Board>();
        handledTileSets = new HashSet<ArrayList<Tile>>();

        this.queue = new PriorityQueue<State>(10, new Comparator<State>() {

            @Override
            public int compare(State x, State y) {
                return x.getCost() - y.getCost();
            }
        });

        this.h1 = heuristicOne; // false: h2
    }

    public int getHamming(ArrayList<Tile> tiles) {
        int counter =  0;
        for (int i = 0; i < tiles.size(); i++) {
            int currentTilesNumber = tiles.get(i).getNumber();
            if (currentTilesNumber == 0) continue;
            if (currentTilesNumber != (i+1)) counter++;
        }
        return counter;
    }

    public int getManhattan(ArrayList<Tile> tiles) {
        int[][] newTiles = new int[N_COLS_ROWS][N_COLS_ROWS];
        int distance = 0;

        // Reformat tiles array
        for (int i = 0; i < N_COLS_ROWS; i++) {
            for (int j = 0; j < N_COLS_ROWS; j++) {
                newTiles[i][j] = tiles.get(0).getNumber();
                tiles.remove(0);
            }
        }

        // Calculate manhattan
        for (int i = 0; i < N_COLS_ROWS; i++) {
            for (int j = 0; j < N_COLS_ROWS; j++) {
                int value = newTiles[i][j];
                if (value != 0) {                            // don't compute MD for element 0
                    int targetX = (value - 1) / N_COLS_ROWS; // expected x-coordinate (row)
                    int targetY = (value - 1) % N_COLS_ROWS; // expected y-coordinate (col)
                    int dx = i - targetX;                   // x-distance
                    int dy = j - targetY;                   // y-distance
                    distance += Math.abs(dx) + Math.abs(dy);
                }
            }
        }

        return distance;
    }

    public void run() {
        System.out.println("Running solver with " + (this.h1 ? "Hamming" : "Manhattan") + " heuristic");
        State state = new State(board, 0, 0, null);

        // Start time count
        long timeCountStart = System.nanoTime();

        if (!state.board.isSolvable()) {
            System.out.println("Board is not solvable");
            return;
        }

        queue.add(state);

        int iterations = 0;

        while(true) {
            iterations++;

            if (queue.size() == 0) {
                System.out.println("Error, queue empty");
                state.board.printBoard();
                return;
            }

            // Get first state in queue, the state with lowest cost estimate
            state = queue.poll();
            handledTileSets.add((ArrayList<Tile>)state.board.getTiles().clone());

            if (state.isGoal()) {

                long timeCountStop = System.nanoTime();
                double timeDiff = (timeCountStop - timeCountStart)/1e6;

                // loop over previous states and push back
                solutionPath.push(state.board);
                while (state.previous != null) {
                    state = state.previous;
                    solutionPath.push(state.board);
                }

                System.out.println("Found solution as follows:");
                while (!solutionPath.isEmpty()) {
                    solutionPath.pop().printBoard();
                }
                System.out.println("Done in " + timeDiff + " ms.");

                break;
            }

            // Increment g moves
            state.gCost++;

            // Get all neighbors
            Iterable<Board> neighbors = state.board.getNeighbors();

            // Push the neighbors as new states in queue with g
            for (Board neighbor : neighbors) {

                // If neighbor board has already been a state, do nothing
                if (this.handledTileSets.contains((ArrayList<Tile>)neighbor.getTiles().clone()))
                    continue;

                // Calculate board h cost with h1
                int h = 0;
                if (this.h1)
                    h = getHamming(neighbor.getTiles());
                else
                    h = getManhattan((ArrayList<Tile>)neighbor.getTiles().clone()); //clone tiles because altering them in func

                // Create a state out of board and sort it in queue by cost
                State newState = new State(neighbor, state.gCost, h, state);
                queue.add(newState);
            }

            // Some base case
            if (iterations > 50000) {
                System.out.println("exceeded max iterations");
                break;
            }
        }
    }
}
