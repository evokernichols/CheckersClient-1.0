package checkerclient;

import checkerclient.Game.ObserveGame;
import checkerclient.Game.ObserveGame;
import checkerclient.Game.PlayGame;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 */
public class Connection {
    /*************
     * CONSTANTS *
     ************/
    /*Message types sent by client*/
    private static final int MSG_ALL = 101;        //client <1> sends message <2> to everyone in lobby
    private static final int MSG_C = 102;          //client <1> sends message <3> to client <2>
    private static final int MAKE_TBL = 103;       //client <1> wants to make and sit at a table
    private static final int JOIN_TBL = 104;       //client <1> wants to join table id <2>
    private static final int READY = 105;          //client <1> is ready for game to start
    private static final int MOVE = 106;           //client <1> moves from <2> to <3>. <2> and <3> are 0-based.
    private static final int LEAVE_TBL = 107;      //client <1> leaves the table
    private static final int QUIT = 108;           //client <1> leaves the server
    private static final int ASK_TBL_STATUS = 109; //client <1> is asking for the status of table <2>        
    private static final int OBSERVE_TBL = 110;    //client <1> wants to observer table <2>
    private static final int STOP_OBSERVING = 115; //client <1> wants to stop observing table <2>;
    /*In the server code but apparently unused*/
    //private static final int REGISTER = 111;        //client <1> is registering, with password <2>
    //private static final int LOGIN = 112;           //client <1> is logging in, using password <2>
    //private static final int UPDATE_PROFILE = 113;  //client <1> is updating profile. 
    //private static final int GET_PROFILE = 114;     //client <1> wants to get the profile for client <2>
    
    /*Message types sent by server*/
    private static final int CONN_OK = 200;        //connection to server establised
    private static final int IN_LOBBY = 218;       //client has joined the lobby
    private static final int OUT_LOBBY = 213;      //client has left the lobby
    private static final int MSG = 201;            //client received message <3> from <1>. If <2> = 1 then it is private.
    private static final int NEW_TBL = 202;        //a new table has been created with id <1> 
    private static final int GAME_START = 203;     //The game at the table has begun
    private static final int COLOR_BLACK = 204;    //client is playing as black 
    private static final int COLOR_RED = 205;      //client is playing as red
    private static final int OPP_MOVE = 206;       //opponent has moved from <1> to <2>
    private static final int BOARD_STATE = 207;    //the board state on table <1> is <2>
    private static final int GAME_WIN = 208;       //client has won their game
    private static final int GAME_LOSE = 209;      //client has lost their game
    private static final int TBL_JOINED = 210;     //client has joined table <1>
    private static final int TBL_LEFT = 222;       //client has left table <1>.
    private static final int WHO_IN_LOBBY = 212;   //the clients <1> <2> ... <n> are in the server
    private static final int NOW_IN_LOBBY = 214;   //client <1> is now in the lobby
    private static final int WHO_ON_TBL = 219;     //the clients <2> <3> are on table with tid <1>. <2> is black. <3> is red. If either is -1, the seat is open.   
    private static final int TBL_LIST = 216;       //the current tables are <1> <2> ... <n>. Clients will have to request status.
    private static final int NOW_LEFT_LOBBY = 217; //the client <1> has left the lobby.
    private static final int OPP_LEFT_TABLE = 220; //your opponent has left the game.
    private static final int YOUR_TURN = 221;      //it is now your turn.        
    private static final int NOW_OBSERVING = 230;  //you are now observing table <1>. 
    private static final int STOPPED_OBSERVING = 235;   //you stopped observing table <1>.
    //private static final int REGISTER_OK = 231;    //your registration is complete.
    //private static final int LOGIN_OK = 232;       //you have logged in successfully.
    //private static final int PROFILE_UPDATED = 233;//your profile has been updated.
    //private static final int USER_PROFILE = 234;   //the profile for <1>. <4> is its description, <2> is wins, <3> is losses.
    
    /* Error messages*/
    private static final int NET_EXCEPTION = 400;       //network exception
    private static final int NAME_IN_USE = 401;         //client name already in use
    private static final int BAD_NAME = 408;            //the user name requested is bad.
    private static final int ILLEGAL = 402;             //illegal move
    private static final int TBL_FULL = 403;            //table you tried to join is full
    private static final int NOT_IN_LOBBY = 404;        //If you are not in the lobby, you can't join a table.
    private static final int BAD_MESSAGE = 405;         //some part of a message the server got from a client is bad.
    private static final int ERR_IN_LOBBY = 406;        //you cannot perform the requested operation because you are in the lobby.
    private static final int PLAYERS_NOT_READY = 409;   //you made a move, but your opponent is not ready to play yet
    private static final int NOT_YOUR_TURN = 410;       //you made a move, but its not your turn.
    private static final int TBL_NOT_EXIST = 411;       //table queried does not exist.
    private static final int GAME_NOT_CREATED = 412;    //called if you say you are ready on a table with no current game.
    //private static final int ALREADY_REGISTERED = 413;  //this name is already registered.
    private static final int LOGIN_FAIL = 414;          //authentication failed.        
    private static final int NOT_OBSERVING = 415;       //client is not observing a table.
    
    
    /********************
     * CONNECTION STUFF *
     *******************/
    private Socket socket;
    private DataOutputStream streamOut;
    private DataInputStream streamIn;
    private String userName;
    private boolean quit = false;
    private boolean gotMSG = false;
    private ConnectionListenerThread listenerThread;
    
    /**
     * Constructor for server connection
     * @param IPAddress The game server's IP address
     * @param userConnecting Desired user name for the connection 
     */
    public Connection(String IPAddress, String userConnecting) {
        try {
            socket = new Socket(IPAddress, 45322);
            streamIn = new DataInputStream(socket.getInputStream());
            streamOut = new DataOutputStream(socket.getOutputStream());
            userName = userConnecting;
                       
            establishConnection();
            
        } catch (Exception e) {
            System.out.println("Error creating connection");
            //Error message, etc
        }  
    }    
    
    private void establishConnection() {         
        try {
            serverConnect();
            streamOut.write(userName.getBytes());
            //original connection response. Everything else should happen somewhere else
            //serverConnect();
            listenerThread = new ConnectionListenerThread(this, streamIn);
        } 
        catch (IOException ex) {
            System.out.println("Error establishing connection");
        }
    }
    
    private void serverConnect() {
        while (!quit && !gotMSG) {
            quit = serverMsg();
        }
        quit = false;
        gotMSG = false;
    }

    private String[] getMessagesFromBytes(int ct, byte[] msg) {
        String[] collection;
        String text = "";
        try {
            //append bytes to string
            for (int n = 0; n < ct; n++) {
                text += (char) msg[n];
            }
            //split by message
            collection = text.split("<EOM>");
        } catch (Exception ex) {
            System.out.println(ex);
            collection = new String[0];
        }
        return collection;
    }

    private boolean serverMsg() {
        boolean hasMsg = false;
        try {
            byte[] msg = new byte[2000];
            //read in message 
            int ct = streamIn.read(msg);
            if (!msg.equals("")) {
                String[] collection = getMessagesFromBytes(ct, msg);
                for (int i = 0; i < collection.length; i++) {
                    System.out.println("Message: " + collection[i] + "\n");
                    hasMsg = true;
                    gotMSG = true;
                }
            } else if (gotMSG) {
                quit = true;
            }
        } catch (Exception ex) {
            hasMsg = false;
        }
        return hasMsg;
    }
    
    /*****************
     * CLASS LINKING *
     ****************/
    Lobby lobby = null;
    PlayGame gameBoard = null;
    ArrayList<ObserveGame> observers= new ArrayList<ObserveGame>();
    
    /**
     * Associates a Lobby object with this Connection. Should be called when a 
     * new Lobby window is instantiated
     * @param lob The Lobby to be associated with
     */
    public void setLobby (Lobby lob) {
        lobby = lob;
    }
    /**
     * Returns the Lobby object associated with this Connection
     * @return The Lobby associated with this Connection
     */
    public Lobby getLobby() {
        return lobby;
    }
    /**
     * Associates a PlayGame object with this Connection. Should be called when a 
     * new PlayGame window is instantiated
     * @param game The PlayGame to be associated with
     */
    public void setGameBoard (PlayGame game) {
        gameBoard = game;
    }
    /**
     * Returns the PlayGame object associated with this Connection
     * @return The PlayGame object associated with this Connection
     */
    public PlayGame getGameBoard() {
        return gameBoard;
    }  
    /**
     * 
     * @param obs 
     */
    public void addObserver(ObserveGame obs) {
        observers.add(obs);
    }
    /**
     * 
     * @param obs 
     */
    public void removeObserver(ObserveGame obs) {
        observers.remove(obs);
    }
    
    /**
     * 
     * @return 
     */
    public String getUserName() {
        return userName;
    }
    /*****************
     * SEND MESSAGES *
     ****************/
    /**
     * This method sends pre-constructed strings. Called only by public methods
     * in this class
     * @param messageString 
     */
    private void sendMessage(String messageString) {        
        try {
            streamOut.write(messageString.getBytes());
            System.out.println("Sent: " + messageString); //DEBUG
        } catch (IOException ex) {
        }
    }
    
    /**
     * Sends a chat message directed to all users
     * @param chatMessage The chat message
     */
    public void sendChatAll(String chatMessage) {
        String message = MSG_ALL + " " + userName + " " + chatMessage + " <EOM>";
        sendMessage(message);
    }
    
    /**
     * Sends a chat message directed to a specified user
     * @param toUser The username of the client to which the message is being sent
     * @param chatMessage The chat message
     */
    public void sendChatPrivate(String toUser, String chatMessage) {
        String message = MSG_C + " " + userName + " " + toUser + " " + chatMessage + " <EOM>";
        sendMessage(message);
    }
    
    /**
     * Sends a request to make a new table
     */
    public void sendMakeTable() {
        String message = MAKE_TBL + " " + userName + " <EOM>";
        sendMessage(message);
    }
    
    /**
     * Sends a request to join specified a table
     * @param tableID The table that the user intends to join
     */
    public void sendJoinTable(int tableID) {
        String message = JOIN_TBL + " " + userName + " " + tableID + " <EOM>";
        sendMessage(message);
    }
    
    /**
     * Sends notification of ready state
     */
    public void sendReady() {
        String message = READY + " " + userName + " <EOM>";
        sendMessage(message);
    }
    
    /**
     * Sends message indicating a from (fromX, fromY) to (toX, toY)
     * @param fromX The horizontal starting position of the piece to be moved
     * @param fromY The vertical starting position of the piece to be moved
     * @param toX The horizontal ending position of the piece to be moved
     * @param toY The vertical ending position of the piece to be moved
     */
    public void sendMove(int fromX, int fromY, int toX, int toY) {
        String message = MOVE + " " + userName + " " + fromX + "," + fromY + " " + toX + "," + toY + " <EOM>";
        sendMessage(message);
    }
    
    /**
     * Sends request to leave the current table
     */
    public void sendLeaveTable() {
        String message = LEAVE_TBL + " " + userName + " <EOM>";
        sendMessage(message);
    }
    
    /**
     * Sends notification of intent to disconnect from server
     */
    public void sendQuit() {
        String message = QUIT + " " + userName + " <EOM>";
        sendMessage(message);
    }
    
    /**
     * Sends request for the status of a specified table
     * @param tableID The table for which a status update is being requested
     */
    public void sendAskTableStatus(int tableID) {
        String message = ASK_TBL_STATUS + " " + userName + " " + tableID + " <EOM>";
        sendMessage(message);
    }
    
    /**
     * Sends request to observe a specified table
     * @param tableID The table that the user intends to observe
     */
    public void sendObserveTable(int tableID) {
        String message = OBSERVE_TBL + " " + userName + " " + tableID + " <EOM>";
        sendMessage(message);
    }
    
    /**
     * Sends request to stop observing the specified table
     * @param tableID The table that the user intends to stop observing
     */
    public void sendStopObserving(int tableID) {
        String message = STOP_OBSERVING + " " + userName + " " + tableID + " <EOM>";
        sendMessage(message);
    }
    
    
    /*********************************************
     * DECONSTRUCT RECEIVED MESSAGES *
     ********************************************/
    /**
     * Deconstructs a message and calls the appropriate helping method to process it
     * @param message The full message received from the server, including code and <EOF>
     */
    public void receivedMessage(String message) {        
        String [] args = message.split(" ");
        switch (Integer.parseInt(args[0])) {
            /*Standard Messages*/
            case CONN_OK: 
                receivedConnectionOK(); 
                break;
            case IN_LOBBY: 
                receivedInLobby();      
                break;
            case OUT_LOBBY: 
                receivedOutLobby();     
                break;
            case MSG: 
                String chatMessage = "";
                for (int i = 3; i < args.length; i++)                       //ASSUMES no <EOF> is present. Add - 1 if not.
                    chatMessage += args[i] + " "; //Recombine last arguments into chatMessage
                receivedChatMessage(args[1], Integer.parseInt(args[2]), chatMessage);
                break;
            case NEW_TBL: 
                receivedNewTable(Integer.parseInt(args[1]));
                break; 
            case GAME_START: 
                receivedGameStart();    
                break;
            case COLOR_BLACK: 
                receivedColorBlack();   
                break;
            case COLOR_RED:
                receivedColorRed();
                break;
            case OPP_MOVE: 
                int fromX = Integer.parseInt(args[1].substring(0,1));
                int fromY = Integer.parseInt(args[1].substring(2,3));  
                int toX = Integer.parseInt(args[2].substring(0,1));
                int toY = Integer.parseInt(args[2].substring(2,3)); 
                receivedOpponentMove(fromX, fromY, toX, toY);
                break;
            case BOARD_STATE: 
                receivedBoardState(Integer.parseInt(args[1]), args[2]);
                break;
            case GAME_WIN: 
                receivedGameWin();
                break;
            case GAME_LOSE:
                receivedGameLose();
                break;
            case TBL_JOINED: 
                receivedTableJoined(Integer.parseInt(args[1]));
                break;
            case TBL_LEFT: 
                receivedTableLeft(Integer.parseInt(args[1]));
                break;
            case WHO_IN_LOBBY: 
                String[] users = new String[args.length - 1];                   //ASSUMES no <EOF> is present. Change -1 to -2 if not.
                for (int i = 1; i < args.length; i++)                           //ASSUMES no <EOF> is present. Add -1 if not.
                    users[i-1] = args[i];
                receivedWhoInLobby(users);
                break;
            case NOW_IN_LOBBY: 
                receivedNowInLobby(args[1]);
                break;
            case WHO_ON_TBL: 
                receivedWhoOnTable(Integer.parseInt(args[1]), args[3], args[2]);
                break;
            case TBL_LIST:
                int[] tables = new int[args.length - 1];                        //ASSUMES no <EOF> is present. Change -1 to -2 if not.
                for (int i = 1; i < args.length; i++)                           //ASSUMES no <EOF> is present. Add -1 if not.
                    tables[i-1] = Integer.parseInt(args[i]);
                receivedTableList(tables);
                break;
            case NOW_LEFT_LOBBY: 
                receivedNowLeftLobby(args[1]);
                break;    
            case OPP_LEFT_TABLE:
                receivedOpponentLeftTable();
                break;
            case YOUR_TURN:
                receivedYourTurn();
                break;
            case NOW_OBSERVING:
                receivedNowObserving(Integer.parseInt(args[1]));
                break;
            case STOPPED_OBSERVING:
                receivedStoppedObserving(Integer.parseInt(args[1]));
                break;
            /*Errors*/    
            case NET_EXCEPTION:
                receivedNetException();
                break;
            case NAME_IN_USE:
                receivedNameInUse();
                break;
            case BAD_NAME:
                receivedBadName();
                break;
            case ILLEGAL:
                receivedIllegalMove();
                break;
            case TBL_FULL:
                receivedTableFull();
                break;
            case NOT_IN_LOBBY:
                receivedNotInLobby();
                break;
            case BAD_MESSAGE:
                receivedBadMessage();
                break;
            case ERR_IN_LOBBY:
                receivedErrorInLobby();
                break;
            case PLAYERS_NOT_READY:
                receivedPlayersNotReady();
                break;
            case NOT_YOUR_TURN:
                receivedNotYourTurn();
                break;
            case TBL_NOT_EXIST:
                receivedTableDoesNotExist();
                break;
            case GAME_NOT_CREATED:
                receivedGameNotCreated();
                break;
            case LOGIN_FAIL:
                receivedLoginFailed();
                break;
            case NOT_OBSERVING:
                receivedNotObserving();
                break;    
            default:
        }     
    }
    
    /*****************************
     * PROCESS RECEIVED MESSAGES *
     ****************************/
    /**
     * Display successful connection 
     */
    private void receivedConnectionOK() {
        System.out.println("Received Connection OK message");
        //Can't find any part of the server code that actually sends this; may never be called.
    }
    
    /**
     * Enter lobby
     */
    private void receivedInLobby() {
        if (lobby == null) {
            System.out.println("Entering Lobby...");
            CheckerClient.enterLobby();
        }
    }
    
    /**
     * Leave Lobby
     */
    private void receivedOutLobby() {
        System.out.println("Exiting Lobby...");
    }
    
    /**
     * Displays a received chat message
     * @param fromUser Name of user from which message originated
     * @param messageType 0 if broadcast message, 1 if private message
     * @param chatMessage The chat message received
     */
    private void receivedChatMessage(String fromUser, int messageType, String chatMessage) {
        if (!fromUser.equals(userName)) {
            System.out.println(fromUser + " (" + messageType + "): " + chatMessage);
            lobby.updateChat(fromUser, messageType, chatMessage);
        }
    }
    
    /**
     * Alert user that a new table has been created
     * @param tableID The ID of the newly created table
     */
    private void receivedNewTable(int tableID) {
        System.out.println("Recieved new table: " + tableID);
        lobby.addTable(tableID);
    }
    
    /**
     * Begin checkers game
     */
    private void receivedGameStart() {
        System.out.println("Game has started");
        if(gameBoard != null) {
            gameBoard.setGameStarted(true);
        }
    }
    
    /**
     * Processes user is playing the black pieces.
     */
    private void receivedColorBlack() {
        System.out.println("You are playing black");
        if(gameBoard != null) {
            gameBoard.setPlayingAs(1);
        }
    }
    
    /**
     * Processes user is playing the red pieces.
     */
    private void receivedColorRed() {
        System.out.println("You are playing red");
        if(gameBoard != null) {
            gameBoard.setPlayingAs(2);
        }
    }
    
    /**
     * Processes move made by opponent
     * @param fromX The starting X position of the piece to be moved
     * @param fromY The starting Y position of the piece to be moved
     * @param toX The ending X position of the piece to be moved
     * @param toY The ending Y position of the piece to be moved
     */
    private void receivedOpponentMove(int fromX, int fromY, int toX, int toY) {
        System.out.println("Opponent moved from " + fromX + "," + fromY + "to " + toX + "," + toY);
        if(gameBoard != null) {
            gameBoard.externalMovePiece(fromX,fromY,toX,toY);
        }
    }
    
    /**
     * Processes the game state of the specified table
     * @param tableID The ID of the table to be processed
     * @param gameState A String representation of a 2D array representing the game board
     */
    private void receivedBoardState(int tableID, String gameState) { //For gameState's encoding see encodeState() in CheckersGame.cs in server implementation
        System.out.println("Received game state for table " + tableID + ": " + gameState);
        int[][] newState = new int[8][8];
        int strPos = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newState[i][j] = gameState.charAt(strPos);
                strPos++;
            }
        }
        if(gameBoard != null) {
            gameBoard.update(newState);
        }
    }
    
    /**
     * Processes game win
     */
    private void receivedGameWin() {
        System.out.println("You have won");
        //TODO
    }
    
    /**
     * Processes game loss
     */
    private void receivedGameLose() {
        System.out.println("You have lost");
        //TODO
    }
    
    /**
     * Processes client joined a specified table
     * @param tableID The table that has been joined
     */
    private void receivedTableJoined(int tableID) {
        System.out.println("Join table " + tableID);
        lobby.joinTable(tableID);
    }
    
    /**
     * Processes client left a specified table
     * @param tableID The table that has been joined
     */
    private void receivedTableLeft(int tableID) {
        System.out.println("Left table " + tableID);
        //TODO
    }
    
    /**
     * Processes users in lobby list
     * @param usersList List of users in the lobby
     */
    private void receivedWhoInLobby(String[] usersList) {
        System.out.println("Lobby user list obtained");
        for (String user : usersList) {
            System.out.print(user + " ");
        }
        //TODO
    }
    
    /**
     * Display that user has joined lobby
     * @param user The user that has joined the lobby
     */
    private void receivedNowInLobby(String user) {
        System.out.println(user + " has joined the lobby");
        //TODO
    }
    
    /**
     * Displays who is on table or if there are open slots
     * @int tableID The ID of the table
     * @param redPlayer The name of the red player, -1 if empty
     * @param blackPlayer  The name of the black player, -1 if empty
     */
    private void receivedWhoOnTable(int tableID, String redPlayer, String blackPlayer) {
        System.out.println("Table " + tableID + ", Red: " + redPlayer + ", Black: " + blackPlayer);
        lobby.updateTableView(tableID, redPlayer, blackPlayer);        
    }
    
    /**
     * Processes list of tables
     * @param tables An array of table IDs
     */
    private void receivedTableList(int[] tables) {
        System.out.println("Lobby user list obtained");
        for (int table : tables) {
            System.out.print(table + " ");
        }
        System.out.print("\n");
        lobby.initTablesArray(tables);
    }
    
    /**
     * Processes user left lobby
     * @param user The user that left the lobby
     */
    private void receivedNowLeftLobby(String user) {
        System.out.println(user + " has left the lobby");
        //TODO: remove from chatters list/valid pm-able user
    }
    
    /**
     * Processes opponent leaving the table
     */
    private void receivedOpponentLeftTable() {
        System.out.println("The opponent has left the table");
        //TODO
    }
    
    /**
     * Processes when it is the user's turn
     */
    private void receivedYourTurn() {
        System.out.println("It is your turn");
        if(gameBoard != null) {
            gameBoard.setTurn(gameBoard.getPlayingAs());
        }
    }
    
    /**
     * Begins observing table
     * @param tableID The table that is to be observed
     */
    private void receivedNowObserving(int tableID) {
        System.out.println("Now observing table " + tableID);
        lobby.observeTable(tableID);
    }
    
    /**
     * Stops observing table
     * @param tableID The table that is to stop being observed
     */
    private void receivedStoppedObserving(int tableID) {
        System.out.println("Stopped observing table " + tableID);
        //TODO
    }
    
    /***************************
     * PROCESS RECEIVED ERRORS *
     **************************/
    /**
     * Processes net exception
     */
    private void receivedNetException() {
        System.out.println("Net Exception received from server");
        //Doesn't appear to be sent in server code
    }
    
    /**
     * Display name in use error
     */
    private void receivedNameInUse() {
        System.out.println("Name in user error receieved from server");
        //TODO: Display name in use error on GUI
    }
    
    /**
     * Display bad name error
     */
    private void receivedBadName() {
        System.out.println("Bad name error receieved from server");
        //TODO: Display bad name error on GUI (name contains space)
    }
    
    /**
     * Processes illegal move
     */
    private void receivedIllegalMove() {
        System.out.println("Illegal move attempted");
        if(gameBoard != null) {
            gameBoard.setTurn(gameBoard.getPlayingAs());
        }
        //TODO: prevent local update of this move and prompt for another
    }
    
    /**
     * Prevent joining a full table
     */
    private void receivedTableFull() {
        System.out.println("Table is full");
        //TODO: prevent table join
    }
    
    /**
     * Processes not in lobby error
     */
    private void receivedNotInLobby() {
        System.out.println("Not in lobby error receieved from server");
        //TODO: not able to join table because user is not in lobby 
    }
    
    /**
     * Processes bad message error
     */
    private void receivedBadMessage() {
        System.out.println("Bad message error received from server");        
        //TODO: display/handle bad message
    }
    
    /**
     * Processes error: not in lobby
     */
    private void receivedErrorInLobby() {
        System.out.println("Error in lobby receieved from server");
        //TODO: not able to perform action as client is not in lobby
    }
    
    /**
     * Processes players not ready error: unable to make move
     */
    private void receivedPlayersNotReady() {
        System.out.println("Players not ready error receieved from server");
       //TODO: Prevent move if this is received 
    }
    
    /**
     * Processes not your turn error: unable to make move
     */
    private void receivedNotYourTurn() {
        System.out.println("Not your turn error received from server");
       //TODO: Prevent move if this is received 
    }
    
    /**
     * Processes table does not exist error
     */
    private void receivedTableDoesNotExist() {
        System.out.println("Table does not exist error received from server");
        //TODO: handle table does not exist
    }
    
    /**
     * Processes game not created error
     */
    private void receivedGameNotCreated() {
        System.out.println("Table does not exist error received from server");
        //TODO: handle game not create
    }
    
    /**
     * Processes login failure
     */
    private void receivedLoginFailed() {
        System.out.println("Login failed");
        //TODO handle login failure
    }
    
    /**
     * Processes not observing
     */
    private void receivedNotObserving() {
        System.out.println("Not observing error received from server");
        //TODO not observing
    }
}