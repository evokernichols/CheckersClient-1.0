package checkerclient;

import java.util.ArrayList;

/**
 * Makes a recommended move based off the current game state
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 */
public class AI {
    private BoardState currentState;
    private int playingFor; //1 for black, 2 for red
    private int lookAhead;  // Number of moves to look ahead
    
    /**
     * Constructor for AI Object
     * @param boardArray The current state of the game, represented as a 2D array
     * @param playingFor An int indicating which side the AI is to recommend a 
     *                   move for: 1 = black, 2 = red.
     * @param lookAhead Number of moves to look ahead
     */
    public AI (int[][] boardArray, int playingFor, int lookAhead){
        currentState = new BoardState(boardArray);
        this.playingFor = playingFor;  
        this.lookAhead = lookAhead;
    }
    /*Getters and Setters*/
    /**
     * 
     * @param boardArray 
     */
    public void updateBoardState (int[][] boardArray) {
        currentState = new BoardState(boardArray);        
    }
    
    /***
     * 
     * @return 
     */
    public int getPlayingFor() {
        return playingFor;
    }

    /**
     * 
     * @param playingFor 
     */
    public void setPlayingFor(int playingFor) {
        this.playingFor = playingFor;
    }
    
    public int getLookAhead() {
        return lookAhead;
    }

    public void setLookAhead(int lookAhead) {
        this.lookAhead = lookAhead;
    }

    /**
     * 
     * @return 
     */
    public BoardState getCurrentState() {
        return currentState;
    }

    
    
    /*Move recommendation methods*/
    /**
     * Returns a move recommendation based on the current board state. It is up
     * to the PlayGame class to decide when/if to make the recommended move.
     * @param currentState current state of the board represented as a 2D array
     * @return a recommended move
     */
    public Move recommendMove(int[][] currentState) {
        this.currentState = new BoardState(currentState);
        Move move;
        move = calculateMove();
        return move;
    }
    
    /**
     * Calculates the recommended move. Returns a move with all -1 values if 
     * there is an error.
     * @return 
     */
    private Move calculateMove() {
        Move move = new Move(-1,-1,-1,-1); //Init to invalid move
        ArrayList<Piece> normalPieces;
        ArrayList<Piece> kingPieces;
        ArrayList<Move> tempMoves;
        ArrayList<Move> moves = new ArrayList<>();
        
        if (playingFor == 1) { //Playing for black
            normalPieces = currentState.getBlackNormals();
            kingPieces = currentState.getBlackKings();
        }
        else if (playingFor == 2) { //Playing for red
            normalPieces = currentState.getBlackNormals();
            kingPieces = currentState.getBlackKings();
        }
        else {
            System.out.println("AI ERROR: Set playingFor to 1 (black) or 2 (red)");
            return (new Move(-1,-1,-1,-1)); //Return an invalid move
        }
        
        for (Piece p: normalPieces) { //Get all moves that our normal pieces can make
            tempMoves = getLegalMoves(p, playingFor); //playingFor should be 1 for black normal, 2 for red normal
            for (Move m: tempMoves) {
                moves.add(m);
            }
        }
        for (Piece p: normalPieces) { //Get all moves that our kings can make
            tempMoves = getLegalMoves(p, 2 + playingFor); //2 + playingFor should be 3 for black kings, 4 for red kings
            for (Move m: tempMoves) {
                moves.add(m);
            }
        }
        
        if (moves.isEmpty()) {  //If there are no possible moves
            System.out.println("No moves possible");
            return (new Move(-1,-1,-1,-1)); //Return an invalid move
        }
        
        move = getBestMove(moves);
        return move;
    }
    
    /**
     * Get legal moves for the given piece
     * @param piece Piece for which legal moves are to be found
     * @param type 1 = black normal, 2 = red normal, 3 = black king, 4 = red king
     * @return A list of legal moves for the provided piece
     */
    private ArrayList<Move> getLegalMoves(Piece piece, int type) {
        ArrayList<Move> moves = new ArrayList<>(); 
        int x = piece.getX();
        int y = piece.getY();
        int[][] board = currentState.getBoardState();
        
        /*Calc possible moves*/   
        //Check non jumping moves
        if(y - 1 >= 0 && type != 1) { //Can move UP
            if (x + 1 <= 7 && board[x+1][y-1] == 0) { //Can move UP RIGHT by 1
                moves.add(new Move(x,y,x+1,y-1));
            }
            if (x - 1 >= 0 && board[x-1][y-1] == 0) { //Can move UP LEFT by 1
                moves.add(new Move(x,y,x-1,y-1));
            }  
        } 
        if(y + 1 <= 7 && type != 2) { //Can move DOWN            
            if (x + 1 <= 7 && board[x+1][y+1] == 0) { //Can move DOWN RIGHT by 1
                moves.add(new Move(x,y,x+1,y+1));
            }
            if (x - 1 >= 0 && board[x-1][y+1] == 0) { //Can move DOWN LEFT by 1
                moves.add(new Move(x,y,x-1,y+1));
            }  
        }  
        //Check jumping moves
        if(y - 2 >= 0 && type != 1) { //Can move UP 2
            if (x + 2 <= 7 && board[x+2][y-2] == 0 && (board[x+1][y-1] != 0 || 
                                                       board[x+1][y-1] != playingFor || 
                                                       board[x+1][y-1] != playingFor + 2)) { //Can jump UP RIGHT
                moves.add(new Move(x,y,x+2,y-2));
            }
            if (x - 2 >= 0 && board[x-2][y-2] == 0 && (board[x-1][y-1] != 0 || 
                                                       board[x-1][y-1] != playingFor || 
                                                       board[x-1][y-1] != playingFor + 2)) { //Can jump UP LEFT
                moves.add(new Move(x,y,x-2,y-2));
            }  
        } 
        if(y + 2 >= 7 && type != 2) { //Can move UP 2
            if (x + 2 <= 7 && board[x+2][y+2] == 0 && (board[x+1][y+1] != 0 || 
                                                       board[x+1][y+1] != playingFor || 
                                                       board[x+1][y+1] != playingFor + 2)) { //Can jump DOWN RIGHT
                moves.add(new Move(x,y,x+2,y+2));
            }
            if (x - 2 >= 0 && board[x-2][y+2] == 0 && (board[x-1][y+1] != 0 || 
                                                       board[x-1][y+1] != playingFor || 
                                                       board[x-1][y+1] != playingFor + 2)) { //Can jump DOWN LEFT
                moves.add(new Move(x,y,x-2,y+2));
            }  
        }            
        return moves;
    }
    
    /**
     * Determines the best move in the provided list of moves
     * @param moves
     * @return 
     */
    private Move getBestMove(ArrayList<Move> moves) {
        Move bestMove = new Move(-1,-1,-1,-1);
        for (Move m: moves) {
            
        }
        
        
        return bestMove;
    }
    
    /**
     * Returns a new BoardState in which the provided move has been applied
     * @param move
     * @return 
     */
    private BoardState applyMove(Move move) {
        int pieceType;
        BoardState newState;
        int[][] newStateArray = currentState.getBoardState();        
        pieceType = currentState.getBoardState()[move.getFromX()][move.getFromY()]; //Get the type of piece being moved
        newStateArray[move.getFromX()][move.getFromY()] = 0;    //Remove from the from pos
        newStateArray[move.getToX()][move.getToY()] = pieceType;//Place in the to pos
        newState = new BoardState(newStateArray);
        return newState;
    }
    
    /**
     * Returns the piece count advantage of the current state
     * @return 
     */
    private int getPieceAdvantage() {
        int redNorm = currentState.getRedNormalCount();
        int redKing = currentState.getRedKingCount();
        int redTotal = redNorm + redKing;
        int blackNorm = currentState.getBlackNormalCount();
        int blackKing = currentState.getBlackKingCount();
        int blackTotal = blackNorm + blackKing;
        int advantage;
        
        if(playingFor == 1) { //Playing black
            advantage = blackTotal - redTotal;
        }
        else { // playingFor == 2
            advantage = redTotal - blackTotal;
        }
        return advantage;
    }
}