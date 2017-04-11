package puzzle;

import java.util.*;

/**
 * Handles solving logic
 */
public class Solver {
    private Board board;
    private Board solutionBoard;
    private final Stack<Board> solutionPath;
    private PriorityQueue<State> queue;
    private boolean h1;
    private boolean isSolvable;

    /**
     * Inner class State to keep track of board configuration
     */
    class State implements Comparable<State> {
        private Board board;
        private State previous;
        private int cost;
        private int gCost;
        private int hCost;

        State(Board board, int gCost, int hCost, State prev) {
            this.board = board;
            this.gCost = gCost; //?
            this.hCost = hCost; //?
            this.cost = gCost + hCost;
            this.previous = prev;

            //System.out.println("New state");
            //this.board.printBoard();

        }

        private int getCost() {
            return this.cost;
        }

        private void updateCost() {
            cost = gCost + hCost;
        }

        public boolean isGoal(Board solution) {
            ArrayList<Tile> currentTiles = board.getTiles();
            for (int i = 0; i < currentTiles.size(); i++) {
                int currentTilesNumber = currentTiles.get(i).getNumber();

                if (currentTilesNumber == 0) {
                    if (i != currentTiles.size() - 1)
                        return false;
                }

                if (currentTilesNumber != (i+1)) return false;
            }

            return true;
        }

        public Board getBoard() {
            return board;
        }

        @Override
        public int compareTo(State that) {
            if (this.getCost() < that.getCost())
                return -1;
            if (this.getCost() > that.getCost())
                return +1;
            return 0;
        }
    }

    public Solver(Board initialBoard, boolean heuristicOne) {
        this.board = initialBoard;
        this.solutionBoard = new Board(true);
        this.solutionPath = new Stack<Board>();
        this.queue = new PriorityQueue<>();
        this.isSolvable = true;
        this.h1 = heuristicOne; // false: h2

        run();
    }

    private void run() {
        State state = new State(board, 0, 0, null);
        queue.add(state);

        int iterations = 0;

        while(true) {
            iterations++;
            System.out.println("iteration " + iterations + ", queue: " + queue.size());

            if (queue.size() == 0) {
                System.out.println("Final board was");
                state.board.printBoard();
                break;
            }

            state = queue.poll();

            System.out.println("Picked up new");
            state.getBoard().printBoard();

            if (state.isGoal(solutionBoard)) {

                board.printBoard();
                // loop over previous states
                solutionPath.push(board);
                while (state.previous != null) {
                    state = state.previous;

                    // push to solutionBoards
                    solutionPath.push(state.board);
                }

                System.out.println("Found solution");
                break;
            }

            // Increment g moves
            state.gCost++;


            // Get all neighbors
            Iterable<Board> neighbors = board.getNeighbors();

            // Push the neighbors as new states in queue with g
            for (Board neighbor : neighbors) {
                if (state.previous != null && neighbor.equals(state.previous.board)) {
                    //System.out.println("Ending neighbor iteration");
                    //neighbor.printBoard();
                    continue;
                }

                // Calculate board h cost with h1
                ArrayList<Tile> currentTiles = neighbor.getTiles();
                int h = 0;

                for (int i = 0; i < currentTiles.size(); i++) {
                    int currentTilesNumber = currentTiles.get(i).getNumber();

                    if (currentTilesNumber == 0) continue;
                    if (currentTilesNumber != (i+1)) h++;
                }

                int boardCost = state.gCost + h;

                State newState = new State(neighbor, state.gCost, boardCost, state);
                queue.add(newState);
            }

            // Some base case
            if (iterations > 1000000) {
                System.out.println("exceeded max iterations");
                break;
            }
        }
    }

}
