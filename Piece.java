package chess;

import java.util.*;
import java.awt.*;

class Piece {
    protected final boolean WHITE = true, BLACK = false;

    // position in the board index
    private int position;

    // each piece has moved x amt of times
    private int numMoves;

    // each piece has a type
    private int type;

    // each piece belongs to a player
    boolean side;           

    // each piece has a copy of the bitboard?
    private BitBoard bb;

    // constructor
    Piece(int t, int p, boolean s, BitBoard b) {
        setType(t);
        setPosition(p);
        setSide(s);
        setBitBoard(b);
        setNumMoves(0);
    }

    // non-piece constructor
    Piece(int t, int p) {
        setType(t);
        setPosition(p);
        setSide(false);
        setBitBoard(null);
        setNumMoves(-1);
    }
    
    // setters/getters
    void setPosition(int p) {
        position = p;
    }   

    void setNumMoves(int n) {
        numMoves = n;
    }

    void setType(int t) {
        type = t;
    }

    void setSide(boolean b) {
        side = b;
    }
    
    void setBitBoard(BitBoard b) {
        bb = b;
    }

    int getPosition() {
        return position;
    }
    
    int getNumMoves() {
        return numMoves;
    }
    
    int getType() {
        return type;
    }

    boolean getSide() {
        return side;
    }

    BitBoard getBitBoard() {
        return bb;
    }

    long getMoves() {
        return 0L;
    }

    // move a piece on the board
    void movePiece(int p) {
        setPosition(p);
        setNumMoves(getNumMoves() + 1);
    };
}


// represents no piece
class Non extends Piece
{
    @Override
    public String toString() {
        return "";
    }

    Non(int p) {
        super(-1, p);
    }

    long getMoves() {
        return 0L;
    }
}


class Pawn extends Piece
{
    @Override
    public String toString() {
        return getSide() == WHITE ? "\u2659" : "\u265f";
    }

    Pawn(int p, boolean s, BitBoard b) {
        super(5, p, s, b);
    }
    
    long getMoves() {
        return getBitBoard().getPawnMoves(getPosition(), getSide(), getNumMoves() == 0);
    }
}



class Rook extends Piece
{
    @Override
    public String toString() {
        return getSide() == WHITE ? "\u2656" : "\u265c";
    }
        
    Rook(int p, boolean s, BitBoard b) {
        super(2, p, s, b);
    }
    
    long getMoves() {
        return getBitBoard().getRookMoves(getPosition(), getSide());
    }
}


class Bishop extends Piece
{
    @Override
    public String toString() {
        return getSide() == WHITE ? "\u2657" : "\u265d";
    }
        
    Bishop(int p, boolean s, BitBoard b) {
        super(3, p, s, b);
    }
    
    long getMoves() {
        return getBitBoard().getBishopMoves(getPosition(), getSide());
    }
}


class Knight extends Piece
{
    @Override
    public String toString() {
        return getSide() == WHITE ? "\u2658" : "\u265e";
    }
        
    Knight(int p, boolean s, BitBoard b) {
        super(4, p, s, b);
    }
    
    long getMoves() {
        return getBitBoard().getKnightMoves(getPosition(), getSide());
    }
}



class Queen extends Piece
{
    @Override
    public String toString() {
        return getSide() == WHITE ? "\u2655" : "\u265b";
    }
        
    Queen(int p, boolean s, BitBoard b) {
        super(1, p, s, b);
    }
    
    long getMoves() {
        return getBitBoard().getQueenMoves(getPosition(), getSide());
    }
}


class King extends Piece
{
    @Override
    public String toString() {
        return getSide() == WHITE ? "\u2654" : "\u265a";
    }

    King(int p, boolean s, BitBoard b) {
        super(0, p, s, b);
    }    

    long getMoves() {
        return getBitBoard().getKingMoves(getPosition(), getSide());
    }
}
