package checkerclient;

/**
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 */
public class ClientObject {
    private
    String username;
    int xCoor;
    int yCoor;
    int nextXCoor;
    int nextYCoor;

    public ClientObject() {
        xCoor = 0;
        yCoor = 0;
        nextXCoor = 0;
        nextYCoor = 0;
    }

    public String getUsername() {
        return username;

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getxCoor() {
        return xCoor;
    }

    public void setxCoor(int xCoor) {
        this.xCoor = xCoor;
    }

    public int getyCoor() {
        return yCoor;
    }

    public void setyCoor(int yCoor) {
        this.yCoor = yCoor;
    }

    public int getNextXCoor() {
        return nextXCoor;
    }

    public void setNextXCoor(int nextXCoor) {
        this.nextXCoor = nextXCoor;
    }

    public int getNextYCoor() {
        return nextYCoor;
    }

    public void setNextYCoor(int nextYCoor) {
        this.nextYCoor = nextYCoor;
    }

}
