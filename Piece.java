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

    // position in the board index
    private int position;

    // each piece has moved x amt of times
    private int numMoves;

    // each piece has a type
    private Type type;

    // each piece belongs to a player
    boolean side;           

    // each piece has a copy of the bitboard?
    private BitBoard bb;

    // constructor
    Piece(Type t, int p, boolean s, BitBoard b) {
        setType(t);
        setPosition(p);
        setSide(s);
        setBitBoard(b);
        setNumMoves(0);
    }

    // non-piece constructor
    Piece(Type t, int p) {
        setType(t);
        setPosition(p);
        setSide(false);
        setBitBoard(null);
        setNumMoves(-1);
    }
    
    void setPosition(int p) {
        position = p;
    }   

    void setNumMoves(int n) {
        numMoves = n;
    }

    void setType(Type t) {
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
    
    Type getType() {
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
        super(Type.NON, p);
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
        super(Type.PAWN, p, s, b);
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
        super(Type.ROOK, p, s, b);
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
        super(Type.BISHOP, p, s, b);
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
        super(Type.KNIGHT, p, s, b);
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
        super(Type.QUEEN, p, s, b);
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
        super(Type.KING, p, s, b);
    }    

    long getMoves() {
        return getBitBoard().getKingMoves(getPosition(), getSide());
    }
}
