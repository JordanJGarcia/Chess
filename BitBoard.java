package chess;

import java.util.*;
import java.awt.*;
import java.lang.Math;

// should we record each set of pieces as they move?
// black pawns/white pawns
// b knights/w knights
// etc ?
// ... food for thought

enum pc {
    K, // king
    Q, // queen
    R, // rook
    N, // knight
    P  // pawn
}

class BitBoard {

    private long[] masks = null;

    // these will hold the states of the entire board
    // and of all player pieces together
    private long board;
    private long white;
    private long black;

    // these will hold the states of each players individual pieces
    private final long[] WHITE = {
        0x1000000000000000L, // KING
        0x0800000000000000L, // QUEEN
        0x8100000000000000L, // ROOK
        0x2400000000000000L, // BISHOP
        0x4200000000000000L, // KNIGHT
        0x00FF000000000000L, // PAWN
    };
    
    private final long[] BLACK = {
        0x0000000000000010L, // KING   
        0x0000000000000008L, // QUEEN  
        0x0000000000000081L, // ROOK   
        0x0000000000000024L, // BISHOP 
        0x0000000000000042L, // KNIGHT 
        0x000000000000FF00L, // PAWN  
    };

    // default constructor
    BitBoard() {
        masks = new long[64];

        // generate masks for each bit position
        for(int i = 0; i < masks.length; i++)
            masks[i] = 1L << i;    
        
        createBoard();
    }

    // creates the initial board to play on
    void createBoard() {
        initializeWhiteBits();
        initializeBlackBits();
        board = black | white;

        System.out.println("\nBoard state: \n");
        print(board);
    }

    void initializeWhiteBits() {
        for(int i = 0; i < WHITE.length; i++)
            white |= WHITE[i];
    }

    void initializeBlackBits() {
        for(int i = 0; i < BLACK.length; i++)
            black |= BLACK[i];
    }    

    // prints state of a long (board)
    void print(long l) {
        for(int p = 0; p < 64; p++) {
            if((p+1)%8 == 0 && p != 0)
                System.out.println(getBitValue(l, p) + " ");
            else
                System.out.print(getBitValue(l, p) + " ");
        }
        System.out.println("");
    }

    void printBitBoard() {
        System.out.println("Board state: ");
        print(board);
    }

    // returns value at certain position in mask (0 or 1)
    long getBitValue(long mask, int pos) {
        return ((mask >> pos) & 1);
    }

    // moves a bit in board b from position o to position n,
    // only if it exists in board g as well
    long moveBit(long b, int o, int n, long g) {

        if((masks[n] & g) != masks[n])
            return 0L;

        long r = b | masks[n];
        r &= ~(1L << o);
        return r;
    }

    // setters/getters
    void setBoard(long val) {
        board = val;
    }

    void setWhite(long val) {
        white = val;
    }

    void setBlack(long val) {
        black = val;
    }

    long getBoard() {
        return board;
    }

    long getWhite() {
        return white;
    }

    long getBlack() {
        return black;
    }

    // play
    int playForBlack(long moves, int from, int to) {
        long newBoard = moveBit(board, from, to, moves);
        long newBlack = moveBit(black, from, to, moves);
        long tempWhite = white | masks[to];

        if(newBoard == 0L) {
            System.err.println("error: invalid move");
            return -1;
        }

        if(newBlack == 0L) {
            System.err.println("error: could not set black bitboard");
            return -1;
        }
        setBoard(newBoard);
        setBlack(newBlack);

        // check if eating white piece
        if(tempWhite == white)
            setWhite(white & ~masks[to]);

        return 0;
    }

    int playForWhite(long moves, int from, int to) {
        long newBoard = moveBit(board, from, to, moves);
        long newWhite = moveBit(white, from, to, moves);
        long tempBlack = black | masks[to];

        if(newBoard == 0L) {
            System.err.println("error: invalid move");
            return -1;
        }

        if(newWhite == 0L) {
            System.err.println("error: could not set white bitboard");
            return -1;
        }
        setBoard(newBoard);
        setWhite(newWhite);

        // check if eating black piece
        if(tempBlack == black)
            setBlack(black & ~masks[to]);

        return 0;
    }


    // the below functions will generate masks of available moves for each piece

    // PAWN
    long getPawnMoves(int pos, boolean s, boolean firstMove) {
        long moves = 0L;
        long attacks = 0L;
        long opponent = (s ? black : white);

        // get all possible moves and attacks
        if(s == true) {
            moves |= masks[pos - 8];

            if(firstMove)
                moves |= masks[pos - 16];

            attacks |= masks[pos - 7];
            attacks |= masks[pos - 9];
        }
        else {
            moves |= masks[pos + 8];

            if(firstMove)
                moves |= masks[pos + 16];

            attacks |= masks[pos + 7];
            attacks |= masks[pos + 9];
        }

        // moves should NOT have opponent bit set
        moves &= ~opponent;
        
        // attacks should have the opponent bit set
        attacks &= opponent;

        // combine
        return moves |= attacks;
    }

    // ROOK
    long getRookMoves(int pos, boolean s) {
        long moves = 0L;
        long opponent = (s ? black : white);
        long me = (s ? white : black);

        // open moves to the right (white) or left (black)
        for(int i = pos + 1; i % 8 != 0 && i < 64 && i >= 0;  i++) {
            if((masks[i] & me) == 0L)
                moves |= masks[i];
            else break;

            if((masks[i] & opponent) == masks[i])
                break;
        }

        // open moves to the left (white) or right (black)
        for(int i = pos - 1; i % 8 != 7 && i < 64 && i >= 0; i--) {
            if((masks[i] & me) == 0L)
                moves |= masks[i];
            else break;

            if((masks[i] & opponent) == masks[i])
                break;
        }

        // open moves up (white) or down (black)
        for(int i = pos - 8; i >= 0 && i < 64; i -= 8) {
            if((masks[i] & me) == 0L)
                moves |= masks[i];
            else break;

            if((masks[i] & opponent) == masks[i])
                break;
        }

        // open moves down (white) or up (black)
        for(int i = pos + 8; i >= 0 && i < 64; i += 8) {
            if((masks[i] & me) == 0L)
                moves |= masks[i];
            else break;

            if((masks[i] & opponent) == masks[i])
                break;
        }
        return moves;
    }

    // BISHOP
    long getBishopMoves(int pos, boolean s) {
        long moves = 0L;
        long opponent = (s ? black : white);
        long me = (s ? white : black);

        // open moves up-right (white) or down-left (black)
        for(int i = pos - 7; i % 8 != 0 && i >= 0 && i < 64; i -= 7) {
            if((masks[i] & me) == 0L)
                moves |= masks[i];
            else break;

            if((masks[i] & opponent) == masks[i])
                break;
        }

        // open moves up-left (white) or down-right (black)
        for(int i = pos - 9; i % 8 != 7 && i >= 0 && i < 64; i -= 9) {
            if((masks[i] & me) == 0L)
                moves |= masks[i];
            else break;

            if((masks[i] & opponent) == masks[i])
                break;
        }

        // open moves down-right (white) or up-left (black)
        for(int i = pos + 9; i % 8 != 0 && i >= 0 && i < 64; i += 9) {
            if((masks[i] & me) == 0L)
                moves |= masks[i];
            else break;

            if((masks[i] & opponent) == masks[i])
                break;
        }

        // open moves down-left (white) or up-right (black)
        for(int i = pos + 7; i % 8 != 7 && i >= 0 && i < 64; i += 7) {
            if((masks[i] & me) == 0L)
                moves |= masks[i];
            else break;

            if((masks[i] & opponent) == masks[i])
                break;
        }
        return moves;
    }

    // KNIGHT
    long getKnightMoves(int pos, boolean s) {
        long moves = 0L;
        long me = (s ? white : black);

        int[] val = {6, 10, 15, 17}; // all positions knight can move (up or down from current pos)
        int loc;

        for(int v : val) {
            if(v == 6 || v == 15) {
                loc = pos - v;
                if(loc % 8 > pos % 8 && loc >= 0)
                    moves |= masks[loc];

                loc = pos + v;
                if(loc % 8 < pos % 8 && loc < 64)
                    moves |= masks[loc];
            }
            else {
                loc = pos - v;
                if(loc % 8 < pos % 8 && loc >= 0)
                    moves |= masks[loc];

                loc = pos + v;
                if(loc % 8 > pos % 8 && loc < 64)
                    moves |= masks[loc];
            }
        }
        return moves & ~me;
    }

    // QUEEN
    long getQueenMoves(int pos, boolean s) {
        long moves = 0L;

        moves |= getRookMoves(pos, s);
        moves |= getBishopMoves(pos, s);

        return moves;
    }

    // KING
    long getKingMoves(int pos, boolean s) {
        long moves = 0L;
        long opponent = (s ? black : white);
        long me = (s ? white : black);

        int[] val = {1, 7, 8, 9};
        int loc, offset;

        for(int v : val) {
            loc = pos - v;
            offset = Math.abs((loc % 8) - (pos % 8));
            if(loc >= 0 && offset == 0 || offset == 1) {
                if((masks[loc] & me) == 0L || (masks[loc] & opponent) == masks[loc])
                    moves |= masks[loc];
            }

            loc = pos + v;
            offset = Math.abs((loc % 8) - (pos % 8));
            if(loc < 64 && offset == 0 || offset == 1) {
                if((masks[loc] & me) == 0L || (masks[loc] & opponent) == masks[loc])
                    moves |= masks[loc];
            }
        } 
        return moves;
    }

    // generate available attacks for a piece
    long getAttacks(long moves, boolean s) {
        return moves & (s ? black : white);
    }
}
