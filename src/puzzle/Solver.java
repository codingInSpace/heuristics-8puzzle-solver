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
    private class State implements Comparable<State> {
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
        }

        private int getCost() {
            return this.cost;
        }

        private void updateCost() {
            cost = gCost + hCost;
        }

        private boolean isGoal(Board solution) {
            ArrayList<Tile> solutionTiles = solution.getTiles();
            ArrayList<Tile> currentTiles = board.getTiles();

            for (int i = 0; i < solutionTiles.size(); i++) {
                if (solutionTiles.get(i).getNumber() != currentTiles.get(i).getNumber())
                    return false;
            }

            return true;
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
        // Check if initial is solution
            // push to solutionBoards
            // return

        State state = new State(board, 0, 0, null);
        queue.add(state);

        int iterations = 0;

        while(true) {
            iterations++;
            System.out.println(iterations);

            state = queue.poll();

            if (state.isGoal(solutionBoard)) {
                // loop over previous states
                // push to solutionBoards
                // return
                break;
            }

            // Increment g moves
            state.gCost++;

            // Calculate h cost
            ArrayList<Tile> solutionTiles = solutionBoard.getTiles();
            ArrayList<Tile> currentTiles = board.getTiles();
            int h = 0;

            for (int i = 0; i < solutionTiles.size(); i++) {
                if (solutionTiles.get(i).getNumber() != currentTiles.get(i).getNumber()) {
                    h++;
                }
            }

            state.hCost = h;

            // Sum g and h
            state.updateCost();

            // Some base case
            if (iterations > 20) {
                break;
            }

        }
    }

}
