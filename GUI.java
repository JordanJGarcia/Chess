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

    GUI() {
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
        frame.setSize( 1100, 800 );
        frame.setVisible(true);
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
    private long availableMoves;
    
    private Board board;

    // squares on the board
    private SquareButton[] square = new SquareButton[AMOUNT];
    private GridLayout gl = new GridLayout(8, 8);

    // colors of the board
    static Color black = new Color( 119,136,153 );
    static Color white = Color.WHITE;
    static Color whitePiece = new Color( 255,200,113 );
    static Color blackPiece = Color.BLACK;

    BoardPanel() {
        board = new Board();
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

    void setAvailableMoves(long l) {
        availableMoves = l;
    }

    long getAvailableMoves() {
        return availableMoves;
    }

    void setBoard(Board b) {
        board = b;
    }

    Board getBoard() {
        return board;
    }

    //functions to create/reset the board
    void createBoard() {
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
        int row = 0;

        for(int i = 0; i < AMOUNT; i++) {
            square[i].setOpaque(true);
            square[i].setText("");

            // new row
            if(i % 8 == 0)
                row++;

            // set color
            if(i % 2 == 0) {
                square[i].setBackground(row % 2 != 0 ? white : black);
                square[i].setBorder(new LineBorder(row % 2 != 0 ? white : black));
            }
            else {
                square[i].setBackground(row % 2 != 0 ? black : white);
                square[i].setBorder(new LineBorder(row % 2 != 0 ? black : white));
            }
        }
        
        // set pieces
        for(Piece p : getBoard().getPlayerOne().getPieces()) {
            square[p.getPosition()].setText(p.toString());
            square[p.getPosition()].setForeground(whitePiece);
        }

        for(Piece p : getBoard().getPlayerTwo().getPieces()) {
            square[p.getPosition()].setText(p.toString());
            square[p.getPosition()].setForeground(blackPiece);
        }
    }

    // action listener for the squares
    public void actionPerformed(ActionEvent e) {

        SquareButton s = (SquareButton)e.getSource();

        // check if we clicked same square twice
        if(getLastClicked() == s.getPosition()) {
            setLastClicked(-1);
            setAvailableMoves(0L);
            resetBoard();
            return;
        }

        // if we clicked an available move, request to move there
        if(getBoard().getBitBoard().getBitValue(getAvailableMoves(), s.getPosition()) == 1) {
            // if move is successful
            if(getBoard().getCurrentPlayer().requestMove(getLastClicked(), s.getPosition()) != -1) {

                // reset meta data
                setLastClicked(-1);
                setAvailableMoves(0L);

                // switch players
                getBoard().switchPlayers();

                // reset board
                resetBoard();
                return;
            }
        }

        // reset the board
        resetBoard();

        // square highlights
        LineBorder selected = new LineBorder(getBoard().getCurrentPlayer() == getBoard().getPlayerOne() ? whitePiece : blackPiece);
        Color option = new Color(134, 226, 116);

        // hightlight selected
        s.setBorder(selected);

        // record last clicked
        setLastClicked(s.getPosition());

        // get available moves for piece
        setAvailableMoves(getBoard().getCurrentPlayer().getMovesAt(s.getPosition()));

        // if no moves, do nothing
        if(getAvailableMoves() == 0L)
            return;

        // show available moves
        for(int i = 0; i < AMOUNT; i++) {
            if(getBoard().getBitBoard().getBitValue(getAvailableMoves(), i) == 1)
                square[i].setBackground(option);
        }
    }
}


// JButton representing a square on the board
class SquareButton extends JButton {
    private int position;

    SquareButton() {
        setPosition(-1);
        setFont(new Font("Serif Plain", Font.BOLD, 50));
    }

    SquareButton(int pos) {
        setPosition(pos);
        setFont(new Font("Serif Plain", Font.BOLD, 50));
    }

    // setters/getters
    void setPosition(int p) {
        position = p;
    }

    int getPosition() {
        return position;
    }
}
