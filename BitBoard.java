package chess;

import java.util.*;
import java.awt.*;
import java.lang.Math;

class BitBoard {

    private long[] masks = null;

    // long availableSpots;
    private long board;
    private long white;
    private long black;

    final long[] WHITEPIECES = {
        0x1000000000000000L, // KING
        0x0800000000000000L, // QUEEN
        0x8100000000000000L, // ROOK
        0x2400000000000000L, // BISHOP
        0x4200000000000000L, // KNIGHT
        0x00FF000000000000L, // PAWN
    };
    
    final long[] BLACKPIECES = {
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
        System.out.println("\nWhite state: \n");
        print(white);

        initializeBlackBits();
        System.out.println("\nBlack state: \n");
        print(black);

        board = black | white;
        System.out.println("\nBoard state: \n");
        print(board);
        System.out.print("\n");
    }

    void initializeWhiteBits() {
        for(int i = 0; i < WHITEPIECES.length; i++)
            white |= WHITEPIECES[i];
    }

    void initializeBlackBits() {
        for(int i = 0; i < BLACKPIECES.length; i++)
            black |= BLACKPIECES[i];
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

    long getBoard() {
        return board;
    }

    void setWhite(long val) {
        white = val;
    }

    long getWhite() {
        return white;
    }

    void setBlack(long val) {
        black = val;
    }

    long getBlack() {
        return black;
    }

    // play
    int playForBlack(long moves, int from, int to) {
        long newBoard = moveBit(board, from, to, moves);
        long newBlack = moveBit(black, from, to, moves);

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
        return 0;
    }

    int playForWhite(long moves, int from, int to) {
        long newBoard = moveBit(board, from, to, moves);
        long newWhite = moveBit(white, from, to, moves);

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
        return 0;
    }


    // the below functions will generate masks of available moves for each piece

    // PAWN
    long getPawnMoves(int pos, Orientation or, boolean firstMove) {
        long openMoves = 0L;
        long opponent = (or == Orientation.WHITE ? black : white);
        int loc;

        if(or == Orientation.WHITE) {
            loc = pos - 8;
            if(getBitValue(board, loc) == 0) {
                openMoves = masks[loc];

                // can move two positions on first move
                if(firstMove && getBitValue(board, loc - 8) == 0)
                    openMoves |= masks[loc - 8];
            }
            // add attacks if they exist
            loc = pos - 7;
            if(pos % 8 != 7 && getBitValue(opponent, loc) == 1)
                openMoves |= masks[loc];

            loc = pos - 9;
            if(pos % 8 != 0 && getBitValue(opponent, loc) == 1)
                openMoves |= masks[loc];
        }
        else {
            loc = pos + 8;
            if(getBitValue(board, loc) == 0) {
                openMoves = masks[loc];

                // can move two positions on first move
                if(firstMove && getBitValue(board, loc + 8) == 0)
                    openMoves |= masks[loc + 8];
            }
            // add attacks if they exist
            loc = pos + 7;
            if(pos % 8 != 0 && getBitValue(opponent, loc) == 1)
                openMoves |= masks[loc];

            loc = pos + 9;
            if(pos % 8 != 7 && getBitValue(opponent, loc) == 1)
                openMoves |= masks[loc];
        }
        return openMoves;
    }

    // ROOK
    long getRookMoves(int pos, Orientation or) {
        long openMoves = 0L;
        long opponent = (or == Orientation.WHITE ? black : white);
        long me = (or == Orientation.WHITE ? white : black);
        int i;

        // open moves to the right (white) or left (black)
        i = pos;
        while(i % 8 != 7) {
            i++;
            if(getBitValue(me, i) == 0) {
                if(getBitValue(opponent, i) == 1) {
                    openMoves |= masks[i];
                    break;
                }
                openMoves |= masks[i];
            }
            else break;
        }

        // open moves to the left (white) or right (black)
        i = pos;
        while(i % 8 != 0) {
            i--;
            if(getBitValue(me, i) == 0) {
                if(getBitValue(opponent, i) == 1) {
                    openMoves |= masks[i];
                    break;
                }
                openMoves |= masks[i];
            }
            else break;
        }

        // open moves up (white) or down (black)
        i = pos - 8;
        while(i >= 0) {
            if(getBitValue(me, i) == 0) {
                if(getBitValue(opponent, i) == 1) {
                    openMoves |= masks[i];
                    break;
                }
                openMoves |= masks[i];
            }
            else break;
            i -= 8;
        }

        // open moves down (white) or up (black)
        i = pos + 8;
        while(i < 64) {
            if(getBitValue(me, i) == 0) {
                if(getBitValue(opponent, i) == 1) {
                    openMoves |= masks[i];
                    break;
                }
                openMoves |= masks[i];
            }
            else break;
            i += 8;
        }
        return openMoves;
    }

    // BISHOP
    long getBishopMoves(int pos, Orientation or) {
        long openMoves = 0L;
        long opponent = (or == Orientation.WHITE ? black : white);
        long me = (or == Orientation.WHITE ? white : black);
        int i;

        // open moves up-right (white) or down-left (black)
        i = pos;
        while(i % 8 != 7) {
            i -= 7;
            if(i >= 0) {
                if(getBitValue(me, i) == 0) {
                    if(getBitValue(opponent, i) == 1) {
                        openMoves |= masks[i];
                        break;
                    }
                    openMoves |= masks[i];
                }
                else break;
            }
        }

        // open moves up-left (white) or down-right (black)
        i = pos;
        while(i % 8 != 0) {
            i -= 9;
            if(i >= 0) {
                if(getBitValue(me, i) == 0) {
                    if(getBitValue(opponent, i) == 1) {
                        openMoves |= masks[i];
                        break;
                    }
                    openMoves |= masks[i];
                }
                else break;
            }
        }

        // open moves down-right (white) or up-left (black)
        i = pos;
        while(i % 8 != 7) {
            i += 9;
            if(i < 64) {
                if(getBitValue(me, i) == 0) {
                    if(getBitValue(opponent, i) == 1) {
                        openMoves |= masks[i];
                        break;
                    }
                    openMoves |= masks[i];
                }
                else break;
            }
        }

        // open moves down-left (white) or up-right (black)
        i = pos;
        while(i % 8 != 0) {
            i += 7;
            if(i < 64) {
                if(getBitValue(me, i) == 0) {
                    if(getBitValue(opponent, i) == 1) {
                        openMoves |= masks[i];
                        break;
                    }
                    openMoves |= masks[i];
                }
                else break;
            }
        }
        return openMoves;
    }

    // KNIGHT
    long getKnightMoves(int pos, Orientation or) {
        long openMoves = 0L;
        int[] val = {6, 10, 15, 17}; // all positions knight can move (up or down from current pos)
        int loc;

        for(int v : val) {
            if(v == 6 || v == 15) {
                loc = pos - v;
                if(loc % 8 > pos % 8 && loc >= 0)
                    openMoves |= masks[loc];

                loc = pos + v;
                if(loc % 8 < pos % 8 && loc < 64)
                    openMoves |= masks[loc];
            }
            else {
                loc = pos - v;
                if(loc % 8 < pos % 8 && loc >= 0)
                    openMoves |= masks[loc];

                loc = pos + v;
                if(loc % 8 > pos % 8 && loc < 64)
                    openMoves |= masks[loc];
            }
        }

        if(or == Orientation.WHITE)
            return openMoves & ~white;

        return openMoves & ~black;
    }

    // QUEEN
    long getQueenMoves(int pos, Orientation or) {
        long openMoves = 0L;

        openMoves |= getRookMoves(pos, or);
        openMoves |= getBishopMoves(pos, or);

        return openMoves;
    }

    // KING
    long getKingMoves(int pos, Orientation or) {
        long openMoves = 0L;
        long opponent = (or == Orientation.WHITE ? black : white);
        long me = (or == Orientation.WHITE ? white : black);
        int[] val = {1, 7, 8, 9};
        int loc, offset;

        for(int v : val) {
            loc = pos - v;
            offset = Math.abs((loc % 8) - (pos % 8));
            if(offset == 0 || offset == 1) {
                if((getBitValue(me, loc) == 0 || getBitValue(opponent, loc) == 1) && loc >= 0)
                    openMoves |= masks[loc];
            }

            loc = pos + v;
            offset = Math.abs((loc % 8) - (pos % 8));
            if(offset == 0 || offset == 1) {
                if((getBitValue(me, loc) == 0 || getBitValue(opponent, loc) == 1) && loc < 64)
                    openMoves |= masks[loc];
            }
        } 
        return openMoves;
    }

    // generate available attacks for a piece
    long getAttacks(long moves, Orientation or) {
        long opponent = (or == Orientation.WHITE ? black : white);
        return moves & opponent;
    }



    // main
    public static void main(String[] args) {
        BitBoard bb =  new BitBoard();

        // play for black
        long validMoves = bb.getPawnMoves(8, Orientation.BLACK, true);
        bb.playForBlack(validMoves, 8, 24);
        bb.printBitBoard();
        bb.print(bb.getBlack());

        // play for white
        validMoves = bb.getPawnMoves(52, Orientation.WHITE, true);
        bb.playForWhite(validMoves, 52, 36);
        bb.printBitBoard();
        bb.print(bb.getWhite());
    }
}
