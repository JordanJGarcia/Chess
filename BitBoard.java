package chess;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.lang.Math;

class BitBoard {

    private long[] masks = null;

    // long availableSpots;
    private long currentState;
    private long currentWhiteState;
    private long currentBlackState;

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
        print(currentWhiteState);

        initializeBlackBits();
        System.out.println("\nBlack state: \n");
        print(currentBlackState);

        currentState = currentBlackState | currentWhiteState;
        System.out.println("\nBoard state: \n");
        print(currentState);
        System.out.print("\n");
    }

    // prints state of a long (board or portions of board) 
    void print(long board) {
        for(int k = 0; k < 64; k++) {
            if((k+1)%8 == 0 && k != 0)
                System.out.println(getBitValue(board, k) + " ");
            else
                System.out.print(getBitValue(board, k) + " ");
        }
        System.out.println("");
    }

    void printBitBoard() {
        System.out.println("Board state: ");
        print(currentState);
    }

    void initializeWhiteBits() {
        for(int i = 0; i < WHITEPIECES.length; i++)
            currentWhiteState = currentWhiteState | WHITEPIECES[i];
    }


    void initializeBlackBits() {
        for(int i = 0; i < BLACKPIECES.length; i++)
            currentBlackState = currentBlackState | BLACKPIECES[i];
    }    

    // returns value at certain position in mask (0 or 1)
    long getBitValue(long mask, int pos) {
        return ((mask >> pos) & 1);
    }

    // moves a bit in board from o position to n
    long moveBit(long board, int o, int n) {
        board |= masks[n];
        board &= ~(1L << o);
        return board;
    }


    // the below functions will generate masks of available moves for each piece

    // PAWN
    long getPawnMoves(int pos, Orientation or, boolean firstMove) {
        long openMoves = 0L;
        long opponent = (or == Orientation.WHITE ? currentBlackState : currentWhiteState);
        int loc;

        if(or == Orientation.WHITE) {
            loc = pos - 8;
            if(getBitValue(currentState, loc) == 0) {
                openMoves = masks[loc];

                // can move two positions on first move
                if(firstMove && getBitValue(currentState, loc - 8) == 0)
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
            if(getBitValue(currentState, loc) == 0) {
                openMoves = masks[loc];

                // can move two positions on first move
                if(firstMove && getBitValue(currentState, loc + 8) == 0)
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
        long opponent = (or == Orientation.WHITE ? currentBlackState : currentWhiteState);
        long me = (or == Orientation.WHITE ? currentWhiteState : currentBlackState);
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
        long opponent = (or == Orientation.WHITE ? currentBlackState : currentWhiteState);
        long me = (or == Orientation.WHITE ? currentWhiteState : currentBlackState);
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
            return openMoves & ~currentWhiteState;

        return openMoves & ~currentBlackState;
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
        long opponent = (or == Orientation.WHITE ? currentBlackState : currentWhiteState);
        long me = (or == Orientation.WHITE ? currentWhiteState : currentBlackState);
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
        long opponent = (or == Orientation.WHITE ? currentBlackState : currentWhiteState);
        return moves & opponent;
    }



    // main
    public static void main(String[] args) {
        BitBoard bb =  new BitBoard();
        bb.currentState = bb.moveBit(bb.currentState, 8, 24);
        bb.currentBlackState = bb.moveBit(bb.currentBlackState, 8, 24);
        bb.printBitBoard();

        bb.currentState = bb.moveBit(bb.currentState, 15, 23);
        bb.currentBlackState = bb.moveBit(bb.currentBlackState, 15, 23);
        bb.printBitBoard();

        bb.print(bb.getKingMoves(33, Orientation.WHITE));
        bb.print(bb.getKingMoves(24, Orientation.BLACK));
        bb.print(bb.getKingMoves(21, Orientation.WHITE));
        bb.print(bb.getAttacks(bb.getKingMoves(21, Orientation.WHITE), Orientation.WHITE));

        bb.print(bb.getRookMoves(40, Orientation.WHITE));
        bb.print(bb.getAttacks(bb.getRookMoves(40, Orientation.WHITE), Orientation.WHITE));

        bb.print(bb.getQueenMoves(28, Orientation.BLACK));
        bb.print(bb.getAttacks(bb.getQueenMoves(28, Orientation.BLACK), Orientation.BLACK));

    }
}
