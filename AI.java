package Lab4.ConnectFour;

import java.util.*;

public class AI {
    private final int depth;

    public AI(int depth) {
        this.depth = depth;
    }

    private int[] minimax(Board board, int depth, int pos, int alpha, int beta, boolean maximisingPlayer) {
        Piece player = maximisingPlayer ? Piece.AI : Piece.PLAYER;
        int[] validLocations = board.getValidLocations();

        if(depth == 0) {
            if(board.areFourConnected(Piece.AI) || board.areFourConnected(Piece.PLAYER) || board.getValidLocations().length == 0) {
                if(board.areFourConnected(Piece.AI)) return new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE};
                else if(board.areFourConnected(Piece.PLAYER)) return new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE};
                else if(board.getValidLocations().length == 0) return new int[]{Integer.MIN_VALUE, 0};
            } else {
                return board.getScore(pos, Piece.AI);
            }
        }

        if(maximisingPlayer) {
            int column = validLocations[new Random().nextInt(validLocations.length)];
            int maxEval = Integer.MIN_VALUE;

            for(int i : validLocations) {
                Board tempBoard = new Board(board);
                tempBoard.placePiece(i, player);
                int eval = minimax(tempBoard, depth - 1, i, alpha, beta, false)[1];

                if(eval > maxEval) {
                    column = i;
                    maxEval = eval;
                }

                alpha = Math.max(alpha, maxEval);
                if(alpha >= beta) break;
            }

            return new int[]{column, maxEval};
        } else {
            int column = validLocations[new Random().nextInt(validLocations.length)];
            int minEval = Integer.MAX_VALUE;

            for(int i : validLocations) {
                Board tempBoard = new Board(board);
                tempBoard.placePiece(i, player);
                int eval = minimax(tempBoard, depth - 1, i, alpha, beta, true)[1];

                if(eval < minEval) {
                    column = i;
                    minEval = eval;
                }

                beta = Math.min(beta, minEval);
                if(alpha >= beta) break;
            }

            return new int[]{column, minEval};
        }
    }

    public int[] getBestMovePos(Board board) {
        return minimax(board, depth, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
    }
}
