package chess;

import javax.swing.*;

// a GUI will be the tier 1 encompassing class of the other objects in this package
// it will contain the following:
//      a single board, and within the board is:
//          2 players with their pieces, and a bitboard (shared by both players)
class GUI {
    private Board board;

    GUI() {
        board = new Board();
    }

    GUI(Board b) {
        setBoard(b);
    }

    void setBoard(Board b) {
        board = b;
    }

    Board getBoard() {
        return board;
    }


    public static void main(String[] args) {
        System.out.println("In GUI class");
        GUI gui = new GUI();
    }
}
