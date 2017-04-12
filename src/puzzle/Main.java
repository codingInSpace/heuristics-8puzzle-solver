package puzzle;

public class Main {
    public static void main (String[] args) {
        Board board = new Board(false);
        board.printBoard();

        Solver solver = new Solver(board, false);
        solver.run();
    }
}
