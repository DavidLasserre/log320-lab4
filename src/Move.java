import java.util.Comparator;

public class Move {
    public final String move;
    public final int X, Y, x, y;
    public int maxScore = Integer.MIN_VALUE;
    public int minScore = Integer.MAX_VALUE;

    public Move(String move) {
        this.move = move;
        this.X = (move.charAt(0) - 'A') / 3;
        this.Y = (move.charAt(1) - '1') / 3;
        this.x = (move.charAt(0) - 'A') % 3;
        this.y = (move.charAt(1) - '1') % 3;
    }

    public Move(int X, int Y, int x, int y) {
        this.X = X;
        this.Y = Y;
        this.x = x;
        this.y = y;
        this.move = new String(new char[]{(char) (X * 3 + x + 'A'), (char) (Y * 3 + y + '1')});
    }

    public static Comparator<Move> maxScoreComparator = Comparator.comparingInt(move -> move.maxScore);
    public static Comparator<Move> minScoreComparator = Comparator.comparingInt(move -> move.minScore);

    @Override
    public String toString() {
        return this.move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move m)) return false;
        return this.move.equals(m.move);
    }
}
