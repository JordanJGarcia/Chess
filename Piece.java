package chess;

import java.util.*;
import java.awt.*;

enum Type {
    NON,
    PAWN,
    ROOK,
    BISHOP,
    KNIGHT,
    QUEEN,
    KING
}

class Piece {
    protected final boolean WHITE = true, BLACK = false;

    private int position;   // each piece has a position 0-63
    private int numMoves;
    private Type type;
    boolean side;           // white (true) or black (false)
    private BitBoard bb;    // each piece will have a copy of the BitBoard to access

    // constructor
    Piece(int p, Type t, boolean s, BitBoard b) {
        setNumMoves(0);
        setPosition(p);
        setType(t);
        setSide(s);
        setBitBoard(b);
    }
    
    int getPosition() {
        return position;
    }
    
    void setPosition(int p) {
        position = p;
    }   

    int getNumMoves() {
        return numMoves;
    }
    
    void incrementNumMoves() {
           numMoves++; 
    }

    void setNumMoves(int n) {
        numMoves = n;
    }

    Type getType() {
        return type;
    }

    void setType(Type t) {
        type = t;
    }

    long getMoves() {
        return 0L;
    }

    void setSide(boolean b) {
        side = b;
    }
    
    boolean getSide() {
        return side;
    }

    void setBitBoard(BitBoard b) {
        bb = b;
    }

    BitBoard getBitBoard() {
        return bb;
    }

    void updatePiece(int pos) {
        setPosition(pos);
        incrementNumMoves();
    };

    public static void main(String[] args) {

    }
}

class Pawn extends Piece
{
    @Override
    public String toString() {
        return getSide() == WHITE ? "\u2659" : "\u265f";
    }

    Pawn(int p, boolean s, BitBoard b) {
        super(p,Type.PAWN, s, b);
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
        super(p,Type.ROOK, s, b);
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
        super(p,Type.BISHOP, s, b);
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
        super(p,Type.KNIGHT, s, b);
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
        super(p,Type.QUEEN, s, b);
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
        super(p,Type.KING, s, b);
    }    

    long getMoves() {
        return getBitBoard().getKingMoves(getPosition(), getSide());
    }
}
