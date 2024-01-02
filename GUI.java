package chess;

import javax.swing.*;
import java.awt.*;

import javax.swing.border.LineBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
class BoardPanel extends JPanel implements ActionListener {

    private final int AMOUNT = 64;
    private int lastClicked;

    // squares on the board
    private SquareButton[] square = new SquareButton[AMOUNT];
    private GridLayout gl = new GridLayout(8, 8);

    // colors of the board
    static Color tan = new Color( 119,136,153 );
    static Color black = Color.WHITE;
    static Color grey = new Color( 57, 55, 55 );

    BoardPanel() {
        setLayout(gl);
        createBoard();
        setLastClicked(-1);
    }

    // setters/getters
    void setLastClicked(int pos) {
        lastClicked = pos;
    }

    int getLastClicked() {
        return lastClicked;
    }

    //functions to create/reset the board
    void createBoard() {
        LineBorder lb = new LineBorder(Color.WHITE, 0);
        int row = 0;

        for(int i = 0; i < AMOUNT; i++) {
            square[i] = new SquareButton(i);

            // add to panel
            add(square[i]);

            // add action listener
            square[i].addActionListener(this);
        }
        resetBoard();
    }
    
    void resetBoard() {
        LineBorder lb = new LineBorder(Color.WHITE, 0);
        int row = 0;

        for(int i = 0; i < AMOUNT; i++) {
            square[i].setBorder(lb);
            square[i].setOpaque(true);

            // new row
            if(i % 8 == 0)
                row++;

            // set color
            if(i % 2 == 0)
                square[i].setBackground(row % 2 != 0 ? black : tan);
            else
                square[i].setBackground(row % 2 != 0 ? tan : black);
        }
    }

    // action listener for the squares
    public void actionPerformed(ActionEvent e) {

        resetBoard();
        
        SquareButton s = (SquareButton)e.getSource();

        // check if we clicked same square twice
        if(getLastClicked() == s.getPosition()) {
            setLastClicked(-1);
            return;
        }

        // highlight square
        LineBorder lb = new LineBorder(Color.RED);
        s.setBorder(lb);

        setLastClicked(s.getPosition());
    }
}


// JButton representing a square on the board
class SquareButton extends JButton {
    private int position;

    SquareButton() {
        setPosition(-1);
    }

    SquareButton(int pos) {
        setPosition(pos);
    }

    // setters/getters
    void setPosition(int p) {
        position = p;
    }

    int getPosition() {
        return position;
    }
}
