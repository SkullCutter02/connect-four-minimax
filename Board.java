package Lab4.ConnectFour;

import java.util.*;

public class Board {
    private final Piece[][] board;

    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_BLUE = "\u001B[34m";
    private final String ANSI_RESET = "\u001B[0m";

    public Board(int x, int y) {
        this.board = new Piece[x][y];

        for (Piece[] pieces : this.board) {
            Arrays.fill(pieces, Piece.NONE);
        }
    }

    public Board(Board board) {
        this.board = cloneBoard(board.board);
    }

    private Piece[][] cloneBoard(Piece[][] board) {
        Piece[][] temp = board.clone();

        for(int i = 0; i < temp.length; i++) {
            temp[i] = temp[i].clone();
        }

        return temp;
    }

    public void displayBoard() {
        for (int i = board.length - 2; i >= 0; i--) {
            for (Piece[] pieces : board) {
                if (pieces[i] == Piece.PLAYER) {
                    System.out.print(ANSI_BLUE + "P " + ANSI_BLUE);
                } else if (pieces[i] == Piece.AI) {
                    System.out.print(ANSI_RED + "A " + ANSI_RED);
                } else {
                    System.out.print(ANSI_RESET + "X " + ANSI_RESET);
                }
            }

            System.out.println(ANSI_RESET);
        }

        System.out.println("0 1 2 3 4 5 6");
    }

    public boolean canPlace(int pos) {
        if (pos > board.length - 1) return false;

        for (int i = 0; i < board[pos].length; i++) {
            if (board[pos][i] == Piece.NONE) {
                return true;
            }
        }

        return false;
    }

    public void placePiece(int pos, Piece player) {
        for (int i = 0; i < board[pos].length; i++) {
            if (board[pos][i] == Piece.NONE) {
                board[pos][i] = player;
                break;
            }
        }
    }

    private int findPos(int pos) {
        for (int i = 0; i < board[pos].length; i++) {
            if (board[pos][i] == Piece.NONE) {
                return i;
            }
        }

        return board[0].length;
    }

    private boolean isFilledAt(int row, int col, Piece player) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return false;
        }

        return board[row][col] == player;
    }

    public boolean areFourConnected(Piece player) {
        // check vertical
        int pieces = 4;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int num = 0;

                for (int k = 0; k < pieces; k++) {
                    if (isFilledAt(i + k, j, player)) {
                        num += 1;
                    }
                }

                if (num == pieces) return true;
            }
        }

        // check horizontal
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int num = 0;

                for (int k = 0; k < pieces; k++) {
                    if (isFilledAt(i, j + k, player)) {
                        num += 1;
                    }
                }

                if (num == pieces) return true;
            }
        }

        // check ascending diagonal
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int num = 0;

                for (int k = 0; k < pieces; k++) {
                    if (isFilledAt(i - k, j + k, player)) {
                        num += 1;
                    }
                }

                if (num == pieces) return true;
            }
        }

        // check descending diagonal
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int num = 0;

                for (int k = 0; k < pieces; k++) {
                    if (isFilledAt(i - k, j - k, player)) {
                        num += 1;
                    }
                }

                if (num == pieces) return true;
            }
        }

        return false;
    }

    public int[] getScore(int pos, Piece player) {
        Piece opponent = player == Piece.PLAYER ? Piece.AI : Piece.PLAYER;
        int score = 0;

        // center column: +4 points
        if (pos == 3 && canPlace(pos)) score += 4;

        // lines of three: +5 points
        score += checkLinesOfThree(pos, player) * 5;
        // lines of two: +2 points
        score += checkLinesOfTwo(pos, player) * 2;

        if (areFourConnected(player)) score += 1000;

        // opp. lines of three: -100 points
        score -= checkLinesOfThree(pos, opponent) * 100;
        // opp. lines of two: -2 points
        score -= checkLinesOfTwo(pos, opponent) * 2;

        if(areFourConnected(opponent)) score -= 1000;

        return new int[]{pos, score};
    }

    private int checkLinesOfTwo(int pos, Piece player) {
        Piece opponent = player == Piece.PLAYER ? Piece.AI : Piece.PLAYER;
        int score = 0;

        // check down
        if (findPos(pos) - 2 >= 0 &&
                board[pos][findPos(pos) - 2] == player &&
                board[pos][findPos(pos) - 1] == player)
            score += 1;

        // check left
        boolean hasLeftOpponent = false;

        for (int i = pos - 1; i >= Math.max(0, pos - 3); i--) {
            if (board[i][findPos(pos) - 1] == opponent) {
                hasLeftOpponent = true;
            } else if (board[i][findPos(pos) - 1] == player) {
                if (!hasLeftOpponent) {
                    score += 1;
                }
            }
        }

        // check right
        boolean hasRightOpponent = false;

        for (int i = pos + 1; i < Math.min(board.length, pos + 3); i++) {
            if (board[i][findPos(pos) - 1] == opponent) {
                hasRightOpponent = true;
            } else if (board[i][findPos(pos) - 1] == player) {
                if (!hasRightOpponent) {
                    score += 1;
                }
            }
        }

        return score;
    }

    private int checkLinesOfThree(int pos, Piece player) {
        Piece opponent = player == Piece.PLAYER ? Piece.AI : Piece.PLAYER;
        int score = 0;

        // check down
        if (findPos(pos) - 3 >= 0 &&
                board[pos][findPos(pos) - 3] == player &&
                board[pos][findPos(pos) - 2] == player &&
                board[pos][findPos(pos) - 1] == player)
            score += 1;

        // check left
        boolean hasLeftOpponent = false;
        int leftCounter = 1;

        for (int i = pos - 1; i >= Math.max(0, pos - 3); i--) {
            if (board[i][findPos(pos) - 1] == opponent) {
                hasLeftOpponent = true;
            } else if (board[i][findPos(pos) - 1] == player) {
                if (!hasLeftOpponent) {
                    leftCounter += 1;
                }
            }
        }

        if(leftCounter >= 3) score += 1;

        // check right
        boolean hasRightOpponent = false;
        int rightCounter = 1;

        for (int i = pos + 1; i < Math.min(board.length, pos + 3); i++) {
            if (board[i][findPos(pos) - 1] == opponent) {
                hasRightOpponent = true;
            } else if (board[i][findPos(pos) - 1] == player) {
                if (!hasRightOpponent) {
                    rightCounter += 1;
                }
            }
        }

        if(rightCounter >= 3) score += 1;

        return score;
    }

    public int[] getValidLocations() {
        List<Integer> validLocations = new ArrayList<>();

        for(int i = 0; i < board.length; i++) {
            if(canPlace(i)) {
                validLocations.add(i);
            }
        }

        return validLocations.stream().mapToInt(i -> i).toArray();
    }
}
