package chess;

import java.util.*;
import java.awt.*;

class Board {
    // the idea is for Board to be the 2nd tier encompassing class
    //      - A board will have an underlying bitboard, shared by 2 players, each player has 16 pieces, etc.
    //      - a GUI will have an underlying board that it sends requests to and portrays on the screen
    private Player p1, p2;
    private BitBoard bb;

    // default constructor
    Board() {
        this.p1 = new Player(Color.WHITE);
        this.p2 = new Player(Color.BLACK);
        this.bb = new BitBoard();
    }

    // constructor
    Board(Player p1, Player p2, BitBoard bb) {
        this.setPlayerOne(p1);
        this.setPlayerTwo(p2);
        this.setBitBoard(bb);
    }

    // setters/getters

    void printPlayerData() {
        p1.printPlayerData();
        System.out.println("\n");
        p2.printPlayerData();
    }

    void setPlayerOne(Player p) {
        this.p1 = p;
    }

    void setPlayerTwo(Player p) {
        this.p2 = p;
    }

    void getBitBoard() {
        this.bb.printBitBoard();
    }

    void setBitBoard(BitBoard b) {
        this.bb = b;
    }
    
    public static void main(String[] args) {
        Board board = new Board();
    }
}
