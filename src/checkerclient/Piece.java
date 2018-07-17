package checkerclient;

import java.util.ArrayList;

/**
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 */
public class Piece {
    private int x;
    private int y;
    
    public Piece (int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /*Getters and setters*/
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
