package chess;

import java.util.*;
import java.awt.*;

class Player {

    private Orientation or;
    private Piece piece[] = new Piece[16];
    private BitBoard bb; // each player has a copy of the bb that gets updated


    // default constructor 
    Player() {
        // do nothing, idk if this is a good idea
    }

    // constructor
    Player(Orientation o, BitBoard b) {
        setOrientation(o);
        setBitBoard(b);
        setPlayerPieces();
    }

    /*--------------------------------------*/
    /* setters/getters for class attributes */
    /*--------------------------------------*/

    Orientation getOrientation() {
        return or;
    }

    void setOrientation(Orientation o) {
        or = o;
    }

    BitBoard getBitBoard() {
        return bb;
    }

    void setBitBoard(BitBoard b) {
        bb = b;
    }

    void setPlayerPieces() {
        int i, j;

        if(getOrientation() == Orientation.WHITE)
            i = 48;
        else
            i = 8;

        // place pawns on second row
        for(j = 0; j < 8; j++, i++)
            piece[j] = new Pawn(i, getOrientation());

        // if black, non-pawn pieces get placed behind
        if(getOrientation() == Orientation.BLACK)
            i = 0;

        // place non-pawn pieces
        piece[j++] = new Rook(i++, getOrientation());
        piece[j++] = new Knight(i++, getOrientation());
        piece[j++] = new Bishop(i++, getOrientation());
        piece[j++] = new Queen(i++, getOrientation());
        piece[j++] = new King(i++, getOrientation());
        piece[j++] = new Bishop(i++, getOrientation());
        piece[j++] = new Knight(i++, getOrientation());
        piece[j++] = new Rook(i++, getOrientation());
    }

    Piece[] getPieces() {
        return piece;
    }

    @Override public String toString() {
        return("Player: " + getOrientation());
    }

    void printPlayerData() {
        System.out.println(toString());
        for(Piece p : piece)
            System.out.print(p.toString());

        System.out.println("");
        System.out.println("player " + or + " bitboard: ");
        bb.printBitBoard();
    }

    /*--------------------------------------*/
    /* methods to mimic player capabilities */
    /*--------------------------------------*/

    int requestMove(int from, int to) {
        long validMoves = 0L;
        Piece p = null;

        for(Piece t : piece) {
            if(t.getPosition() == from)
                p = t;
        }

        if(p == null)
            return -1;

        switch(p.getType()) {
            case PAWN:
                validMoves = bb.getPawnMoves(from, or, p.getNumMoves() == 0);
                break;
            case ROOK:
                validMoves = bb.getRookMoves(from, or);
                break;
            case BISHOP:
                validMoves = bb.getBishopMoves(from, or);
                break;
            case KNIGHT:
                validMoves = bb.getKnightMoves(from, or);
                break;
            case QUEEN:
                validMoves = bb.getQueenMoves(from, or);
                break;
            case KING:
                validMoves = bb.getKingMoves(from, or);
                break;
            default:
                break;
        }

       if(or == Orientation.BLACK) {
           if(bb.playForBlack(validMoves, from, to) == -1)
               return -1;
       } 

       if(bb.playForWhite(validMoves, from, to) == -1)
           return -1;

       p.updatePiece(to);
       return 0;
    }



    public static void main(String[] args) {
        System.out.println("in main() for Player.java");
    }
}
