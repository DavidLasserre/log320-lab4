import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import static java.lang.System.currentTimeMillis;

public class MainBoard {
    private final Board mainBoard = new Board();
    private final SmallBoard[][] smallBoards = new SmallBoard[3][3];
    private final ArrayList<Move> moveHistory = new ArrayList<>();
    private int currentPlayer;
    private long startTime = 0;

    public MainBoard(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        for (int X = 0; X < 3; X++) {
            for (int Y = 0; Y < 3; Y++) {
                smallBoards[X][Y] = new SmallBoard(X, Y);
            }
        }
    }

    private ArrayList<Move> getPossibleMoves() {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        if (mainBoard.getWinner() != 0) {
            return possibleMoves;
        }
        Move lastMove = getLastMove();
        if (lastMove == null) {
            possibleMoves.addAll(getAllPossibleMoves());
        }
        else {
            possibleMoves.addAll(smallBoards[lastMove.x][lastMove.y].getPossibleMoves());
            if (possibleMoves.size() == 0) {
                possibleMoves.addAll(getAllPossibleMoves());
            }
        }
        return possibleMoves;
    }

    private ArrayList<Move> getAllPossibleMoves() {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (int X = 0; X < 3; X++) {
            for (int Y = 0; Y < 3; Y++) {
                possibleMoves.addAll(this.smallBoards[X][Y].getPossibleMoves());
            }
        }
        return possibleMoves;
    }

    public void playMove(Move move) {
        smallBoards[move.X][move.Y].playMove(move, currentPlayer);
        mainBoard.setPlayer(move.X, move.Y, smallBoards[move.X][move.Y].getWinner());
        moveHistory.add(move);
        updateCurrentPlayer();
    }

    public void undoLastMove() {
        Move move = getLastMove();
        assert move != null;
        smallBoards[move.X][move.Y].undoMove(move);
        mainBoard.clearPlayer(move.X, move.Y);
        moveHistory.remove(move);
        updateCurrentPlayer();
    }

    private Move getLastMove() {
        if (moveHistory.size() == 0) {
            return null;
        }
        return moveHistory.get(moveHistory.size() - 1);
    }

    private void updateCurrentPlayer() {
        if (currentPlayer == 1) {
            currentPlayer = 2;
        }
        else {
            currentPlayer = 1;
        }
    }

    public Move playBestMove() {
        startTime = currentTimeMillis();
        int moveHistorySize = moveHistory.size();
        ArrayList<Move> moves = getPossibleMoves();
        Move bestMove = null;

        try {
            for (int depth = 0; depth < 20; depth++) {
                Move bestMoveForDepth = moves.get(0);
                int bestScoreForDepth = Integer.MIN_VALUE;
                for (Move move : moves) {
                    playMove(move);
                    int score = minimax(depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (bestScoreForDepth < score) {
                        bestScoreForDepth = score;
                        bestMoveForDepth = move;
                    }
                    undoLastMove();
                }
                bestMove = bestMoveForDepth;
            }
        }
        catch (TimeoutException e) {
            while (moveHistory.size() != moveHistorySize) {
                undoLastMove();
            }
        }
        assert bestMove != null;
        playMove(bestMove);
        return bestMove;
    }

    private int minimax(int depth, int alpha, int beta) throws TimeoutException {
        if (currentTimeMillis() - startTime > 3000) {
            throw new TimeoutException();
        }
        ArrayList<Move> moves = getPossibleMoves();
        if (depth == 0 || moves.size() == 0) {
            return evaluateBoard(depth);
        }
        if (currentPlayer == 1) {
            int max = Integer.MIN_VALUE;
            moves.sort(Move.maxScoreComparator.reversed());

            for (Move move : moves) {
                playMove(move);
                int score = minimax(depth - 1, alpha, beta);
                move.maxScore = score;
                if (max < score) {
                    max = score;
                }
                if (alpha < score) {
                    alpha = score;
                }
                undoLastMove();
                if (beta <= alpha) {
                    break;
                }
            }
            return max;
        }
        else {
            int min = Integer.MAX_VALUE;
            moves.sort(Move.minScoreComparator);
            for (Move move : moves) {
                playMove(move);
                int score = minimax(depth - 1, alpha, beta);
                move.minScore = score;
                if (min > score) {
                    min = score;
                }
                if (beta > score) {
                    beta = score;
                }
                undoLastMove();
                if (beta <= alpha) {
                    break;
                }
            }
            return min;
        }
    }

    private int evaluateBoard(int depth) {
        return switch (mainBoard.getWinner()) {
            case 0 -> (mainBoard.getScore() * 1000)
                    + smallBoards[0][0].getScore() + smallBoards[0][1].getScore() + smallBoards[0][2].getScore()
                    + smallBoards[1][0].getScore() + smallBoards[1][1].getScore() + smallBoards[1][2].getScore()
                    + smallBoards[2][0].getScore() + smallBoards[2][1].getScore() + smallBoards[2][2].getScore();
            case 1 -> 1000000 + depth;
            case 2 -> -1000000 - depth;
            default -> throw new IllegalStateException("Unexpected value: " + mainBoard.getWinner());
        };
    }
}
