package chess;

import java.util.*;
import java.awt.*;
import javax.swing.*;

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


    // the below functions will generate masks of available moves for each piece

    // PAWN
    long getPawnMoves(int pos, boolean firstMove) {
        long openMoves = 0L;

        openMoves = masks[pos] >> 8;
        if(firstMove)
            openMoves |= openMoves >> 8;

        print(openMoves & ~currentState);
        return openMoves & ~currentState;
    }

    // ROOK
    long getRookMoves(int pos) {
        long openMoves = 0L;
        int i;

        // open moves to the right (white) or left (black)
        i = pos;
        while(i % 8 != 7) {
            i++;
            if(getBitValue(currentState, i) == 0)
                openMoves |= masks[i];
            else break;
        }

        // open moves to the left (white) or right (black)
        i = pos;
        while(i % 8 != 0) {
            i--;
            if(getBitValue(currentState, i) == 0)
                openMoves |= masks[i];
            else break;
        }

        // open moves up (white) or down (black)
        i = pos - 8;
        while(i >= 0) {
            if(getBitValue(currentState, i) == 0)
                openMoves |= masks[i];
            else break;
            i -= 8;
        }

        // open moves down (white) or up (black)
        i = pos + 8;
        while(i < 64) {
            if(getBitValue(currentState, i) == 0)
                openMoves |= masks[i];
            else break;
            i += 8;
        }
        print(openMoves);
        return openMoves;
    }

    // BISHOP
    long getBishopMoves(int pos) {
        long openMoves = 0L;
        int i;

        // open moves up-right (white) or down-left (black)
        i = pos;
        while(i % 8 != 7) {
            i -= 7;
            if(i >= 0) {
               if(getBitValue(currentState, i) == 0)
                   openMoves |= masks[i];
               else break;
            }
        }

        // open moves up-left (white) or down-right (black)
        i = pos;
        while(i % 8 != 0) {
            i -= 9;
            if(i >= 0) {
                if(getBitValue(currentState, i) == 0)
                    openMoves |= masks[i];
                else break;
            }
        }

        // open moves down-right (white) or up-left (black)
        i = pos;
        while(i % 8 != 7) {
            i += 9;
            if(i < 64) {
                if(getBitValue(currentState, i) == 0)
                    openMoves |= masks[i];
                else break;
            }
        }

        // open moves down-left (white) or up-right (black)
        i = pos;
        while(i % 8 != 0) {
            i += 7;
            if(i < 64) {
                if(getBitValue(currentState, i) == 0)
                    openMoves |= masks[i];
                else break;
            }
        }
        print(openMoves);
        return openMoves;
    }

    // KNIGHT
    long getKnightMoves(int pos) {
        long openMoves = 0L;
        int[] val = {6, 10, 15, 17}; // all positions knight can move (up or down from current pos)
        int loc;

        for(int j = 0; j < 4; j++) {
            if(j == 0 || j == 2) {
                loc = pos - val[j];
                if(loc % 8 > pos % 8 && loc >= 0)
                    openMoves |= masks[loc];

                loc = pos + val[j];
                if(loc % 8 < pos % 8 && loc < 64)
                    openMoves |= masks[loc];
            }
            else {
                loc = pos - val[j];
                if(loc % 8 < pos % 8 && loc >= 0)
                    openMoves |= masks[loc];

                loc = pos + val[j];
                if(loc % 8 > pos % 8 && loc < 64)
                    openMoves |= masks[loc];
            }
        }
        print(openMoves);
        return openMoves;
    }


    // main
    public static void main(String[] args) {
        BitBoard bb =  new BitBoard();
        //bb.getPawnMoves(50, true);
        //bb.getPawnMoves(43, false);
        //bb.getRookMoves(56);
        //bb.getRookMoves(34);
        //bb.getRookMoves(29);
        //bb.getBishopMoves(28);
        //bb.getBishopMoves(61);
        //bb.getBishopMoves(33);
        bb.getKnightMoves(35);
        bb.getKnightMoves(1);
        bb.getKnightMoves(23);
        
    }
}
