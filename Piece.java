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

    private int position;   // each piece has a position 0-63
    private int numMoves;
    private Type type;
    private Orientation orientation;

    // constructor
    Piece() {
        setNumMoves(-1);
        setPosition(-1);
        setType(Type.NON);
        setOrientation(Orientation.NON);
    }
    
    // constructor
    Piece(int p, Orientation o, Type t) {
        setNumMoves(0);
        setPosition(p);
        setType(t);
        setOrientation(o);
    }
    
    int getPosition() {
        return position;
    }
    
    void setPosition(int p) {
        position = p;
    }   

    @Override public String toString() {
        return("\n" + getOrientation() + " " + getType() + ": " + getPosition());
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

    Orientation getOrientation() {
        return orientation;
    }

    void setOrientation(Orientation o) {
        orientation = o;
    }
    
    Type getType() {
        return type;
    }

    void setType(Type t) {
        type = t;
    }

    void updatePiece(int pos) {
        setPosition(pos);
        incrementNumMoves();
    };

    public static void main(String[] args) {
        Piece non = new Piece(-1, Orientation.NON, Type.NON);
        System.out.println(non.toString());

        Piece pawn = new Pawn(0, Orientation.WHITE);
        System.out.println(pawn.toString());

        Piece rook = new Rook(1, Orientation.BLACK);
        System.out.println(rook.toString());

        Piece bishop = new Bishop(2, Orientation.BLACK);
        System.out.println(bishop.toString());

        Piece knight = new Knight(3, Orientation.WHITE);
        System.out.println(knight.toString());

        Piece queen = new Queen(4, Orientation.BLACK);
        System.out.println(queen.toString());

        Piece king = new King(5, Orientation.WHITE);
        System.out.println(king.toString());
    }
}

class Pawn extends Piece
{
//    @Override
//    String toString() {
//        //return ( color() == 0 ? "\u265f" : "\u2659" );
//        //return "\u2659";
//        return("
//    }

    Pawn() {
        super();
    }
        
    Pawn(int p, Orientation o) {
        super(p,o,Type.PAWN);
    }
    
}



class Rook extends Piece
{
//    @Override
//    String toString() {
//        //return ( color() == 0 ? "\u265c" : "\u2656" );
//        return "\u2656";
//    }
        
    Rook() {
        super();
    }
        
    Rook(int p, Orientation o) {
        super(p,o,Type.ROOK);
    }
}


class Bishop extends Piece
{
//    @Override
//    String toString() {
//        //return ( color() == 0 ? "\u265d" : "\u2657" );
//        return "\u2657";
//    }
        
    Bishop() {
           super();
    }
        
    Bishop(int p, Orientation o) {
        super(p,o,Type.BISHOP);
    }
    
}


class Knight extends Piece
{
//    @Override
//    String toString() {
//        // return ( color() == 0 ? "\u265e" : "\u2658" );
//        return "\u2658";
//    }
        
    Knight() {
        super();
    }
        
    Knight(int p, Orientation o) {
        super(p,o,Type.KNIGHT);
    }
    
}



class Queen extends Piece
{
//    @Override
//    String toString() {
//        // return ( color() == 0 ? "\u265b" : "\u2655" );
//        return "\u2655";
//    }
        
    Queen() {
        super();
    }
        
    Queen(int p, Orientation o) {
        super(p,o,Type.QUEEN);
    }
    
}


class King extends Piece
{
//    @Override
//    String toString() {
//        //return ( color() == 0 ? "\u265a" : "\u2654" );
//        return "\u2654"; 
//    }

    King() {
        super();
    }
        
    King(int p, Orientation o) {
        super(p,o,Type.KING);
    }    
}
