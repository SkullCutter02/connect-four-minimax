package Lab4.ConnectFour;

import java.util.*;

public class ConnectFour {
    private boolean isGameFinished = false;

    private Piece turn = Piece.PLAYER;

    public void main() {
        Scanner scanner = new Scanner(System.in);

        Board board = new Board(7, 6);
        AI ai = new AI(5);

        while(!isGameFinished) {
            if(turn == Piece.PLAYER) board.displayBoard();
            System.out.println(turn == Piece.PLAYER ? "Your turn!" : "AI's turn");

            while(true) {
                if(turn == Piece.PLAYER) {
                    int pos = scanner.nextInt();

                    if(board.canPlace(pos)) {
                        board.placePiece(pos, turn);
                        break;
                    } else
                        System.out.println("Invalid Piece Placement! Try again!");
                } else {
                    int[] bestMove = ai.getBestMovePos(board);
                    System.out.println("AI placed piece at: " + bestMove[0]);
                    System.out.println("The score for this position is: " + bestMove[1]);
                    board.placePiece(bestMove[0], turn);
                    break;
                }
            }

            if(board.areFourConnected(turn)) isGameFinished = true;
            else turn = turn == Piece.AI ? Piece.PLAYER : Piece.AI;
        }

        System.out.println("The winner is: " + turn);
    }
}
