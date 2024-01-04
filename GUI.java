package chess;

import javax.swing.*;
import java.awt.*;

import javax.swing.border.LineBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// class representing the GUI
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
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI gui = new GUI();
            }
        });
    }
}

// JPanel representing an area to display system messages
class MessagePanel extends JPanel {

    MessagePanel() {
        System.out.println("In MessagePanel() constructor");
    }
}


// JPanel representing the board
class BoardPanel extends JPanel implements ActionListener {
    private final boolean WHITE = true, BLACK = false;
    private final int AMOUNT = 64;

    // last clicked square
    private int last;

    // moves and attacks for a piece
    private long moves, attacks;
    
    // underlying board
    private Board board;

    // squares on the board
    private SquareButton[] square = new SquareButton[AMOUNT];

    // colors of the board
    static Color black = new Color(119,136,153);
    static Color white = Color.WHITE;
    static Color blackPiece = Color.BLACK;
    static Color whitePiece = new Color(255,200,113);

    // layout of the BoardPanel
    private GridLayout gl = new GridLayout(8, 8);

    BoardPanel() {
        setLayout(gl);

        setBoard(new Board());
        createBoard();

        setLastClicked(-1);
        setMoves(0L);
        setAttacks(0L);
    }

    // setters/getters
    void setLastClicked(int pos) {
        last = pos;
    }

    void setMoves(long l) {
        moves = l;
    }

    void setAttacks(long l) {
        attacks = l;
    }

    void setBoard(Board b) {
        board = b;
    }

    int getLastClicked() {
        return last;
    }

    long getMoves() {
        return moves;
    }

    long getAttacks() {
        return attacks;
    }

    Board getBoard() {
        return board;
    }

    //functions to create/reset the board
    void createBoard() {
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
        Piece[] boardIndex = getBoard().getIndex();

        for(int i = 0; i < AMOUNT; i++) {
            square[i].setOpaque(true);
            square[i].setText(boardIndex[i].toString());

            if(boardIndex[i].getType() != -1)
                square[i].setForeground(boardIndex[i].getSide() == WHITE ? whitePiece : blackPiece);

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
    }

    // check if user clicks same square twice
    boolean clickedSameSquare(int pos) {
        if(getLastClicked() != pos)
            return false;

        setLastClicked(-1);
        setMoves(0L);
        setAttacks(0L);
        resetBoard();
        return true;
    }

    // check if user chose a valid move
    boolean bitIsSet(long mask, int pos) {
        return getBoard().getBitBoard().getBitValue(mask ,pos) == 1;
    }

    // attempt to move a piece
    boolean attemptMove(int pos) {
        // verify moves have been initialized
        if(getMoves() == 0L)
            return false;

        // request to make move to pos
        if(getBoard().requestMove(getLastClicked(), pos) == -1)
            return false;

        // reset data
        setLastClicked(-1);
        setMoves(0L);
        setAttacks(0L);

        // switch players
        getBoard().switchPlayers();

        // reset board
        resetBoard();

        return true;
    }

    // display available moves for a piece
    void displayMoves(int pos) {
        setMoves(getBoard().getIndexAt(pos).getMoves());
        setAttacks(getBoard().getBitBoard().getAttacks(getMoves(), getBoard().getCurrentPlayer().getSide()));

        if(getMoves() == 0L)
            return;

        Color m = new Color(134, 226, 116);
        Color a = new Color(181, 101, 104);

        for(int i = 0; i < AMOUNT; i++) {
            if(bitIsSet(getAttacks(), i))
                square[i].setBackground(a);
            else {
                if(bitIsSet(getMoves(), i))
                    square[i].setBackground(m);
            }
        }
    }

    // action listener for the squares
    public void actionPerformed(ActionEvent e) {
        SquareButton s = (SquareButton)e.getSource();

        if(clickedSameSquare(s.getPosition()))
            return;

        if(attemptMove(s.getPosition()))
            return;

        // reset the board
        resetBoard();

        // highlight clicked square
        s.setBorder(new LineBorder(getBoard().getCurrentPlayer() == getBoard().getPlayerOne() ? whitePiece : blackPiece));

        // record last clicked
        setLastClicked(s.getPosition());

        // show available moves
        if(getBoard().getIndexAt(s.getPosition()).getSide() == getBoard().getCurrentPlayer().getSide())
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
