package chess;

import java.util.*;
import java.awt.*;

class Board {
    private final boolean WHITE = true, BLACK = false;
    // the idea is for Board to be the 2nd tier encompassing class
    //      - A board will have an underlying bitboard, shared by 2 players, each player has 16 pieces, etc.
    //      - a GUI will have an underlying board that it sends requests to and portrays on the screen
    private Player p1, p2, currentPlayer;
    private BitBoard bb;

    // default constructor
    Board() {
        bb = new BitBoard();
        setPlayerOne(new Player(WHITE, bb));
        setPlayerTwo(new Player(BLACK, bb));
        setCurrentPlayer(getPlayerOne());
    }

    // constructor
    Board(Player p1, Player p2, BitBoard bb) {
        setPlayerOne(p1);
        setPlayerTwo(p2);
        setCurrentPlayer(p1);
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

    void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    Player getPlayerOne() {
        return p1;
    }

    Player getPlayerTwo() {
        return p2;
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }

    BitBoard getBitBoard() {
        return bb;
    }

    void setBitBoard(BitBoard b) {
        bb = b;
    }
    
    void switchPlayers() {
        if(getCurrentPlayer() == getPlayerOne())
            setCurrentPlayer(getPlayerTwo());
        else
            setCurrentPlayer(getPlayerOne());
    }
}
