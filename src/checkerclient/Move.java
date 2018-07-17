package checkerclient;

/**
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 */
public class Move {
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;
    
    public Move(int fX, int fY, int tX, int tY) {
        fromX = fX;
        fromY = fY;
        toX = tX;
        toY = tY;
    }
    
     public void setFromX(int fromX) {
        this.fromX = fromX;
    }

    public void setFromY(int fromY) {
        this.fromY = fromY;
    }

    public void setToX(int toX) {
        this.toX = toX;
    }

    public void setToY(int toY) {
        this.toY = toY;
    }

    public int getFromX() {
        return fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public int getToX() {
        return toX;
    }

    public int getToY() {
        return toY;
    }
    
}
