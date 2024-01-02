package chess;

import java.util.*;
import java.awt.*;

class Player {
    private final boolean WHITE = true, BLACK = false;

    private boolean side; // WHITE (true) or BLACK (false)

    private Piece piece[] = new Piece[16];
    private BitBoard bb; // each player has a copy of the bb that gets updated

    // hold masks for available moves for each position
    // sort of a hash table that will be updated as pieces move
    private long[] moves;

    // constructor
    Player(boolean s, BitBoard b) {
        moves = new long[64];

        // initialize moves hash map
        for(int i = 0; i < moves.length; i++)
            moves[i] = 0L;

        setSide(s);
        setBitBoard(b);
        setPlayerPieces();
    }

    /*--------------------------------------*/
    /* setters/getters for class attributes */
    /*--------------------------------------*/

    long getMovesAt(int pos) {
        if(pos < 0 || pos > 63)
            return 0L;

        return moves[pos];
    }

    void updateMoves() {
        for(Piece p : piece)
            moves[p.getPosition()] = p.getMoves();
    }

    boolean getSide() {
        return side;
    }

    void setSide(boolean s) {
        side = s;
    }

    BitBoard getBitBoard() {
        return bb;
    }

    void setBitBoard(BitBoard b) {
        bb = b;
    }

    void setPlayerPieces() {
        int i, j;

        if(getSide() == WHITE)
            i = 48;
        else
            i = 8;

        // place pawns on second row
        for(j = 0; j < 8; j++, i++) {
            piece[j] = new Pawn(i, getSide(), getBitBoard());
            moves[i] = piece[j].getMoves();
        }

        // if BLACK, non-pawn pieces get placed behind
        if(getSide() == BLACK)
            i = 0;

        // place non-pawn pieces
        piece[j] = new Rook(i, getSide(), getBitBoard());
        moves[i++] = piece[j++].getMoves();

        piece[j] = new Knight(i, getSide(), getBitBoard());
        moves[i++] = piece[j++].getMoves();

        piece[j] = new Bishop(i, getSide(), getBitBoard());
        moves[i++] = piece[j++].getMoves();

        piece[j] = new Queen(i, getSide(), getBitBoard());
        moves[i++] = piece[j++].getMoves();

        piece[j] = new King(i, getSide(), getBitBoard());
        moves[i++] = piece[j++].getMoves();

        piece[j] = new Bishop(i, getSide(), getBitBoard());
        moves[i++] = piece[j++].getMoves();

        piece[j] = new Knight(i, getSide(), getBitBoard());
        moves[i++] = piece[j++].getMoves();

        piece[j] = new Rook(i, getSide(), getBitBoard());
        moves[i++] = piece[j++].getMoves();

    }

    Piece[] getPieces() {
        return piece;
    }

    @Override public String toString() {
        return("Player: " + getSide());
    }

    void printPlayerData() {
        System.out.println(toString());
        System.out.println("");
        System.out.println("board: ");
        bb.printBitBoard();
    }

    /*--------------------------------------*/
    /* methods to mimic player capabilities */
    /*--------------------------------------*/

    int requestMove(int from, int to) {

        if(getSide() == BLACK) {
            if(getBitBoard().playForBlack(moves[from], from, to) == -1)
                return -1;
        } 
        else {
            if(getBitBoard().playForWhite(moves[from], from, to) == -1)
                return -1;
        }

        // clear hash entry
        moves[from] = 0L;
            
        // update piece
        for(Piece p : piece) {
            if(p.getPosition() == from)
                p.updatePiece(to);
        }

        return 0;
    }
}
