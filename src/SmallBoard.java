import java.util.ArrayList;

public class SmallBoard {
    private final Board board = new Board();
    private final ArrayList<Move> possibleMoves = new ArrayList<>();

    public int getScore() {
        return board.getScore();
    }

    public int getWinner() {
        return board.getWinner();
    }

    public SmallBoard(int X, int Y) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                possibleMoves.add(new Move(X, Y, x, y));
            }
        }
    }

    public void playMove(Move move, int player) {
        board.setPlayer(move.x, move.y, player);
        possibleMoves.remove(move);
    }

    public void undoMove(Move move) {
        board.clearPlayer(move.x, move.y);
        possibleMoves.add(move);
    }

    public ArrayList<Move> getPossibleMoves() {
        if (board.getWinner() != 0) {
            return new ArrayList<>();
        }
        return possibleMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SmallBoard otherBoard)) return false;
        return board.getWinner() == otherBoard.board.getWinner();
    }
}
