public class Board {
    private static final int[][][] lines = {{{0, 0}, {1, 0}, {2, 0}},
                                            {{0, 1}, {1, 1}, {2, 1}},
                                            {{0, 2}, {1, 2}, {2, 2}},
                                            {{0, 0}, {0, 1}, {0, 2}},
                                            {{1, 0}, {1, 1}, {1, 2}},
                                            {{2, 0}, {2, 1}, {2, 2}},
                                            {{0, 0}, {1, 1}, {2, 2}},
                                            {{0, 2}, {1, 1}, {2, 0}}};

    private static final int[][][][][][][][][] SCORES_MAP = initScoresMap();
    private final int[][] board = new int[3][3];
    private int score = 0;
    private int winner = 0;

    public int getScore() {
        return score;
    }

    public int getWinner() {
        return winner;
    }

    public void clearPlayer(int x, int y) {
        board[x][y] = 0;
        winner = 0;
    }

    public void setPlayer(int x, int y, int player) {
        board[x][y] = player;
        score = SCORES_MAP[board[0][0]][board[0][1]][board[0][2]][board[1][0]][board[1][1]][board[1][2]][board[2][0]][board[2][1]][board[2][2]];
        switch (score) {
            case 100 -> winner = 1;
            case -100 -> winner = 2;
            default -> winner = 0;
        }
    }

    /**
     * Méthode qui calcule tous les scores possibles pour un tic-tac-toe normal et les enregistre dans un tableau.
     * Ainsi, au lieu de calculer le score à chaque fois, il peut être récupéré directement.
     * Chaque case du tic-tac-toe correspond à un index ce qui évite de passer par une fonction de hashage.
     * @return un tableau contenant tous les scores possibles.
     */
    private static int[][][][][][][][][] initScoresMap() {
        int[][] board = new int[3][3];
        int[][][][][][][][][] scoresHashMap = new int[3][3][3][3][3][3][3][3][3];
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                for (int c = 0; c < 3; c++) {
                    for (int d = 0; d < 3; d++) {
                        for (int e = 0; e < 3; e++) {
                            for (int f = 0; f < 3; f++) {
                                for (int g = 0; g < 3; g++) {
                                    for (int h = 0; h < 3; h++) {
                                        for (int i = 0; i < 3; i++) {
                                            board[0][0] = a;
                                            board[0][1] = b;
                                            board[0][2] = c;
                                            board[1][0] = d;
                                            board[1][1] = e;
                                            board[1][2] = f;
                                            board[2][0] = g;
                                            board[2][1] = h;
                                            board[2][2] = i;
                                            scoresHashMap[a][b][c][d][e][f][g][h][i] = evaluateBoard(board);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return scoresHashMap;
    }

    private static int evaluateBoard(int[][] board) {
        int score = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (board[x][y] == 2) {
                    board[x][y] = -1;
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            int sum = board[lines[i][0][0]][lines[i][0][1]] + board[lines[i][1][0]][lines[i][1][1]] + board[lines[i][2][0]][lines[i][2][1]];
            int mul = board[lines[i][0][0]][lines[i][0][1]] * board[lines[i][1][0]][lines[i][1][1]] * board[lines[i][2][0]][lines[i][2][1]];

            switch (sum) {
                case -3:
                    return -100;
                case -2:
                    score -= 10;
                    break;
                case -1:
                    if (mul == 0) {
                        score -= 1;
                    }
                    break;
                case 0:
                    break;
                case 1:
                    if (mul == 0) {
                        score += 1;
                    }
                    break;
                case 2:
                    score += 10;
                    break;
                case 3:
                    return 100;
            }
        }
        return score;
    }
}
