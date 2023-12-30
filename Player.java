package chess;

import java.util.*;
import java.awt.*;

class Player {

    private Orientation orientation;
    private Piece piece[] = new Piece[16];


    // default constructor 
    Player() {
        // do nothing, idk if this is a good idea
    }

    // constructor
    Player(Orientation o) {
        setOrientation(o);
        setPlayerPieces();
    }

    /*--------------------------------------*/
    /* setters/getters for class attributes */
    /*--------------------------------------*/

    Orientation getOrientation() {
        return this.orientation;
    }

    void setOrientation(Orientation o) {
        this.orientation = o;
    }

    void setPlayerPieces() {
        int i, j;

        if(this.getOrientation() == Orientation.WHITE)
            i = 48;
        else
            i = 8;

        // place pawns on second row
        for(j = 0; j < 8; j++, i++)
            piece[j] = new Pawn(i, this.getOrientation());

        // if black, non-pawn pieces get placed behind
        if(this.getOrientation() == Orientation.BLACK)
            i = 0;

        // place non-pawn pieces
        piece[j++] = new Rook(i++, this.getOrientation());
        piece[j++] = new Knight(i++, this.getOrientation());
        piece[j++] = new Bishop(i++, this.getOrientation());
        piece[j++] = new Queen(i++, this.getOrientation());
        piece[j++] = new King(i++, this.getOrientation());
        piece[j++] = new Bishop(i++, this.getOrientation());
        piece[j++] = new Knight(i++, this.getOrientation());
        piece[j++] = new Rook(i++, this.getOrientation());
    }

    @Override public String toString() {
        return("Player: " + this.getOrientation());
    }

    void printPlayerData() {
        System.out.println(this.toString());
        for(Piece p : this.piece)
            System.out.print(p.toString());
        System.out.println("");
    }

    /*--------------------------------------*/
    /* methods to mimic player capabilities */
    /*--------------------------------------*/

    int requestMove(Piece p, int loc) {
        // maybe generate an attack mask for this piece?
        // also generate an open move mask
        // validate player can make move based on piece type
        // make move or return error (-1)
        return -1;
    }

    public static void main(String[] args) {

        Player p1 = new Player(Orientation.WHITE);
        Player p2 = new Player(Orientation.BLACK);

        p1.printPlayerData();
        System.out.println("\n\n");
        p2.printPlayerData();
    }
}
