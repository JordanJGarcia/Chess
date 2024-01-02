package chess;

import java.util.*;
import java.awt.*;

class Board {
    private final boolean WHITE = true, BLACK = false;
    // the idea is for Board to be the 2nd tier encompassing class
    //      - A board will have an underlying bitboard, shared by 2 players, each player has 16 pieces, etc.
    //      - a GUI will have an underlying board that it sends requests to and portrays on the screen
    private Player p1, p2;
    private BitBoard bb;

    // default constructor
    Board() {
        bb = new BitBoard();
        p1 = new Player(WHITE, bb);
        p2 = new Player(BLACK, bb);
    }

    // constructor
    Board(Player p1, Player p2, BitBoard bb) {
        setPlayerOne(p1);
        setPlayerTwo(p2);
        setBitBoard(bb);
    }

    // setters/getters

    void printPlayerData() {
        p1.printPlayerData();
        System.out.println("\n");
        p2.printPlayerData();
    }

    void setPlayerOne(Player p) {
        p1 = p;
    }

    void setPlayerTwo(Player p) {
        p2 = p;
    }

    Player getPlayerOne() {
        return p1;
    }

    Player getPlayerTwo() {
        return p2;
    }

    void getBitBoard() {
        bb.printBitBoard();
    }

    void setBitBoard(BitBoard b) {
        bb = b;
    }
    
    public static void main(String[] args) {
        Board board = new Board();
        board.printPlayerData();
        System.out.println("request status (WHITE): " + board.getPlayerOne().requestMove(48, 32));
        board.printPlayerData();
    }
}
