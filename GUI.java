package chess;

import javax.swing.*;
import java.awt.*;

// a GUI will be the tier 1 encompassing class of the other objects in this package
// it will contain the following:
//      a single board, and within the board is:
//          2 players with their pieces, and a bitboard (shared by both players)
class GUI {
    private Board board;

    GUI() {
        board = new Board();

        // create the frame
        JFrame frame = new JFrame("Chess");

        // create the components of the frame
        MessagePanel mp = new MessagePanel();
        BoardPanel bp = new BoardPanel();

        // set frame attributes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // add components to the frame
        frame.getContentPane().add(BorderLayout.NORTH, mp);
        frame.getContentPane().add(BorderLayout.CENTER, bp);

        // display GUI
        frame.setVisible(true);
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

// JPanel representing an area to display system messages
//
// remember panels are not visible, they are simply containers for other
// visible components
//
// so you must add visible components to the panel in these classes
// that will display on the screen
class MessagePanel extends JPanel {

    MessagePanel() {
        System.out.println("In MessagePanel() constructor");
    }
}


// JPanel representing the board
class BoardPanel extends JPanel {
    private final int ROWS = 8, COLS = 8;

    BoardPanel() {
        System.out.println("In BoardPanel() constructor");
    }
}


// JButton representing a square on the board
class SquareButton extends JButton {
    private int position;

    SquareButton() {
        setPosition(-1);
        System.out.println("In SquareButton() constructor");
    }

    // setters/getters
    void setPosition(int p) {
        position = p;
    }

    int getPosition() {
        return position;
    }
}
