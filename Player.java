package chess;

import java.util.*;
import java.awt.*;

class Player {
    private final boolean WHITE = true, BLACK = false;

    // WHITE (true) or BLACK (false)
    private boolean side; 

    // constructor
    Player(boolean s) {
        setSide(s);
    }

    // setters/getters
    void setSide(boolean s) {
        side = s;
    }

    boolean getSide() {
        return side;
    }

    @Override public String toString() {
        return("Player: " + getSide());
    }
}
