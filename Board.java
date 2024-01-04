package chess;

import java.util.*;
import java.awt.*;

class Board {
    private final boolean WHITE = true, BLACK = false;

    private Player p1, p2, currentPlayer;
    private BitBoard bb;

    // a board will have an index of all pieces
    private Piece index[] = new Piece[64];

    // a board will record eaten pieces
    ArrayList<Piece> eaten = new ArrayList<Piece>(32);

    // constructor
    Board() {
        setBitBoard(new BitBoard());
        setPieces();
        setPlayerOne(new Player(WHITE));
        setPlayerTwo(new Player(BLACK));
        setCurrentPlayer(getPlayerOne());
    }

    // setters/getters
    void setPlayerOne(Player p) {
        p1 = p;
    }

    void setPlayerTwo(Player p) {
        p2 = p;
    }

    void setCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    void setBitBoard(BitBoard b) {
        bb = b;
    }

    void setPieces() {
        int i = 0;

        // black non-pawns
        index[i] = new Rook(i++, BLACK, getBitBoard());
        index[i] = new Knight(i++, BLACK, getBitBoard());
        index[i] = new Bishop(i++, BLACK, getBitBoard());
        index[i] = new Queen(i++, BLACK, getBitBoard());
        index[i] = new King(i++, BLACK, getBitBoard());
        index[i] = new Bishop(i++, BLACK, getBitBoard());
        index[i] = new Knight(i++, BLACK, getBitBoard());
        index[i] = new Rook(i++, BLACK, getBitBoard());

        // black pawns
        for(; i < 16; i++)
            index[i] = new Pawn(i, BLACK, getBitBoard());

        // non-pieces
        for(; i < 48; i++)
            index[i] = new Non(i);

        // white pawns
        for(; i < 56; i++)
            index[i] = new Pawn(i, WHITE, getBitBoard());

        // white non-pawns
        index[i] = new Rook(i++, WHITE, getBitBoard());
        index[i] = new Knight(i++, WHITE, getBitBoard());
        index[i] = new Bishop(i++, WHITE, getBitBoard());
        index[i] = new Queen(i++, WHITE, getBitBoard());
        index[i] = new King(i++, WHITE, getBitBoard());
        index[i] = new Bishop(i++, WHITE, getBitBoard());
        index[i] = new Knight(i++, WHITE, getBitBoard());
        index[i] = new Rook(i++, WHITE, getBitBoard());
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

    Player getOpponent() {
        return getCurrentPlayer() == getPlayerOne() ? getPlayerTwo() : getPlayerOne();
    }

    BitBoard getBitBoard() {
        return bb;
    }

    Piece[] getIndex() {
        return index;
    }

    Piece getPieceAt(int pos) {
        if(pos < 0 || pos > 63)
            return null;

        return index[pos];
    }

    void showEaten() {
        System.out.println("Eaten pieces: " + eaten);
    }

    // game functionality
    void movePiece(int from, int to) {
        // update piece data
        index[from].movePiece(to);

        // update piece index
        index[to] = index[from];
        index[from] = new Non(from);
        return;
    }

    int requestMove(int f, int t) {
        Piece from = getPieceAt(f);
        Piece to = getPieceAt(t);

        // ensure we are moving proper piece
        if(from.getSide() != getCurrentPlayer().getSide())
            return -1;
        
        // request move from bitboard
        if(getBitBoard().requestMove(from, to) == -1)
            return -1;

        // check if we ate an opponent piece
        if(to.getType() != -1 && to.getSide() == getOpponent().getSide())
            eaten.add(to);

        movePiece(f, t);
        return 0;
    }

    void switchPlayers() {
        setCurrentPlayer(getOpponent());
    }
}
