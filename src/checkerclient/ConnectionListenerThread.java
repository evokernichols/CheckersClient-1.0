package checkerclient;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 */
public class ConnectionListenerThread extends Thread{
    private Connection connection;
    private DataInputStream streamIn;
    private boolean running;
    
    /**
     * Constructor for listener thread
     * @param con The Connection object which is instantiating this thread
     * @param stream The DataInputStream from the connection established in Connection
     */
    public ConnectionListenerThread (Connection con, DataInputStream stream) {
        connection = con;
        streamIn = stream;
        start();
        running = true;
    }
    /**
     * Run method for the thread
     */
    public void run() {
        while(running) {
            if (listenToServer() == false) {
                System.out.println("Connection lost");
                break;  
            }
        }
    }
    
    /**
     * Listens to the input stream from the server. If messages are encountered,
     * they are processed via the receivedMessage method of the associated 
     * Connection object
     * @return Stream still active
     */
    public boolean listenToServer() {
        try {
            byte[] msg = new byte[2000];
            int ct = streamIn.read(msg);
            if (!msg.equals("")) {
                String[] messages = getMessagesFromBytes(ct, msg);
                for (String message : messages) {
                    if(message.equals("ping")){ //See isConnected() in server's GameClient.cs
                        System.out.println("Ping");
                    }
                    else { //Processable message
                        System.out.println("RECEIVED MESSAGE: " + message);
                        connection.receivedMessage(message);
                    }
                }
            }
            return true;
        }
        catch (IOException e) {
            System.out.println(e); //<----java.lang.ArrayIndexOutOfBoundsException: 0
            return false;
        }
    }
    
    /**
     * Converts a byte sting of length ct to an array of Strings
     * @param ct Byte string length
     * @param msg Byte string
     * @return Array of message Strings
     */
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
        
}
