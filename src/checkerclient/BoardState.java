package checkerclient;

import java.util.ArrayList;

/**
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 */
class BoardState {
    private int[][] boardState;
    private int redNormalCount;
    private int redKingCount;
    private int blackNormalCount;
    private int blackKingCount;
    private int emptySpaceCount;
    private ArrayList<Piece> redNormals;
    private ArrayList<Piece> blackNormals;
    private ArrayList<Piece> redKings;
    private ArrayList<Piece> blackKings;
    
    /**
     * Constructor for Board State
     * @param bs A 2D array representing the state of the game board
     */
    public BoardState(int[][] bs) {
        boardState = bs;
        redNormalCount = getPieceCount(2);
        redKingCount = getPieceCount(4);
        blackNormalCount = getPieceCount(1);
        blackKingCount = getPieceCount(3);
        emptySpaceCount = getPieceCount(0);
        redNormals = getPieces(2);
        blackNormals = getPieces(1);
        redKings = getPieces(4);
        blackKings = getPieces(3);
    }
    
    /**
     * Returns the count of the specified piece type on the current boardState
     * @param type Indicates piece type to be counted: 0 = empty, 1 = black,
     *             2 = red, 3 = black king, 4 = red king
     * @return The count of the specified piece type on the current boardState
     */
    private int getPieceCount(int type) {
        int count = 0;
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState[0].length; j++) {
                if (boardState[i][j] == type)
                    count++;
            }
        }
        return count;
    }
    
    /**
     * Returns an ArrayList of Piece objects for the provided type
     * @param type
     * @return 
     */
    private ArrayList<Piece> getPieces(int type) {
        ArrayList<Piece> pieces = new ArrayList<>();
                
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState[0].length; j++) {
                if (boardState[i][j] == type)
                    pieces.add(new Piece(i, j));                
            }
        }
        return pieces;
    }
    
    /**
     * Returns the count of normal red pieces
     * @return number of normal red pieces on the board
     */
    public int getRedNormalCount() {
        return redNormalCount;
    }
    
    /**
     * Returns the count of normal black pieces
     * @return number of normal black pieces on the board
     */
    public int getBlackNormalCount() {
        return blackNormalCount;
    }
    
    /**
     * Returns the count of red king pieces
     * @return number of red king pieces on the board
     */
    public int getRedKingCount() {
        return redKingCount;
    }
    
    /**
     * Returns the count of black king pieces
     * @return number of black king pieces on the board
     */
    public int getBlackKingCount() {
        return blackKingCount;
    }
    
    /**
     * Returns the count of empty spaces
     * @return number of empty spaces on the board
     */
    public int getEmptySpaceCount() {
        return emptySpaceCount;
    }
        
    /**
     * Returns the current board state
     * @return the current board state as represented by a 2D array
     */
    public int[][] getBoardState() {
        return boardState;
    }
    
    public ArrayList<Piece> getRedNormals() {
        return redNormals;
    }

    public ArrayList<Piece> getBlackNormals() {
        return blackNormals;
    }

    public ArrayList<Piece> getRedKings() {
        return redKings;
    }

    public ArrayList<Piece> getBlackKings() {
        return blackKings;
    }
    
    /**
     * Updates the board state to the provided value and automatically updates
     * piece counts
     * @param bs 
     */
    public void setBoardState(int[][] bs) {
        boardState = bs;
        redNormalCount = getPieceCount(2);
        redKingCount = getPieceCount(4);
        blackNormalCount = getPieceCount(1);
        blackKingCount = getPieceCount(3);   
        emptySpaceCount = getPieceCount(0);
        redNormals = getPieces(2);
        blackNormals = getPieces(1);
        redKings = getPieces(4);
        blackKings = getPieces(3);
    }
}
