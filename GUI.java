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

    // check if user clicks same square twice
    boolean clickedSameSquare(int pos) {
        if(getLastClicked() != pos)
            return false;

        setLastClicked(-1);
        setAvailableMoves(0L);
        resetBoard();
        return true;
    }

    // check if user chose a valid move
    boolean validMove(long mask, int pos) {
        return getBoard().getBitBoard().getBitValue(mask ,pos) == 1;
    }

    // attempt to move a piece
    boolean attemptMove(int pos) {
        if(getBoard().getCurrentPlayer().requestMove(getLastClicked(), pos) == -1)
            return false;

        // reset data
        setLastClicked(-1);
        setAvailableMoves(0L);

        // switch players
        getBoard().switchPlayers();

        // reset board
        resetBoard();

        return true;
    }

    // display available moves for a piece
    void displayMoves(int pos) {
        getBoard().getCurrentPlayer().updateMoves();
        setAvailableMoves(getBoard().getCurrentPlayer().getMovesAt(pos));

        Color m = new Color(134, 226, 116);
        Color a = new Color(181, 101, 104);

        if(getAvailableMoves() == 0L)
            return;

        long attacks = getBoard().getBitBoard().getAttacks(getAvailableMoves(), getBoard().getCurrentPlayer().getSide());

        for(int i = 0; i < AMOUNT; i++) {
            if(validMove(attacks, i))
                square[i].setBackground(a);
            else {
                if(validMove(getAvailableMoves(), i))
                    square[i].setBackground(m);
            }
        }
    }

    // action listener for the squares
    public void actionPerformed(ActionEvent e) {

        SquareButton s = (SquareButton)e.getSource();

        if(clickedSameSquare(s.getPosition()))
            return;

        if(validMove(getAvailableMoves(), s.getPosition())) {
            if(attemptMove(s.getPosition()))
                return;
        }

        // reset the board
        resetBoard();

        // highlight clicked square
        s.setBorder(new LineBorder(getBoard().getCurrentPlayer() == getBoard().getPlayerOne() ? whitePiece : blackPiece));

        // record last clicked
        setLastClicked(s.getPosition());

        // show available moves
        displayMoves(s.getPosition());
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
