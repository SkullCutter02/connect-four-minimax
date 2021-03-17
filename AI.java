package Lab4.ConnectFour;

public class AI {
    private final int depth;

    public AI(int depth) {
        this.depth = depth;
    }

    private int[] minimax(Board board, int depth, int pos, boolean maximisingPlayer) {
        Piece player = maximisingPlayer ? Piece.AI : Piece.PLAYER;

        if(depth == 0) {
            Board tempBoard = new Board(board);
            return tempBoard.getScore(pos, player);
        }

        if(maximisingPlayer) {
            int column = pos;
            int maxEval = Integer.MIN_VALUE;

            for(int i = 0; i < 7; i++) {
                Board tempBoard = new Board(board);
                tempBoard.placePiece(i, player);
                int eval = minimax(tempBoard, depth - 1, i, false)[1];

                if(eval > maxEval) {
                    column = i;
                    maxEval = eval;
                }
            }

            return new int[]{column, maxEval};
        } else {
            int column = pos;
            int minEval = Integer.MAX_VALUE;

            for(int i = 0; i < 7; i++) {
                Board tempBoard = new Board(board);
                tempBoard.placePiece(i, player);
                int eval = minimax(tempBoard, depth - 1, i, true)[1];

                if(eval < minEval) {
                    column = i;
                    minEval = eval;
                }
            }

            return new int[]{column, minEval};
        }
    }

    public int[] getBestMovePos(Board board) {
        return minimax(board, depth, 0, true);
    }
}
