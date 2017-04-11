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
    private boolean isSolvable;
    public final HashSet<ArrayList<Tile>> handledTileSets;

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

    public void run() {
        State state = new State(board, 0, 0, null);

        // Start time count
        long timeCounter = System.currentTimeMillis();

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

            //System.out.println("Picked up new, " + state.gCost);
            //state.getBoard().printBoard();

            if (state.isGoal()) {

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
                System.out.println("Done in " + (System.currentTimeMillis() - timeCounter) + " ms.");

                break;
            }

            // Increment g moves
            state.gCost++;

            // Get all neighbors
            Iterable<Board> neighbors = state.board.getNeighbors();

            // Push the neighbors as new states in queue with g
            for (Board neighbor : neighbors) {

                // If neighbor board has already been a state, do nothing
                if (this.handledTileSets.contains((ArrayList<Tile>)neighbor.getTiles().clone())) {
                    continue;
                }

                // Calculate board h cost with h1
                int h = 0;
                ArrayList<Tile> currentTiles = neighbor.getTiles();
                for (int i = 0; i < currentTiles.size(); i++) {
                    int currentTilesNumber = currentTiles.get(i).getNumber();
                    if (currentTilesNumber == 0) continue;
                    if (currentTilesNumber != (i+1)) h++;
                }

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
