package checkerclient;

/**
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 * This class contains the main method and is used to initialize the client
 * program.
 */
public class CheckerClient {

    public static ClientObject User;
    private static Connection connection;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initClientServices();
    }

    private static void initClientServices() {

       
        //Create client
        User = new ClientObject();
        //Request login
        RequestClientLogin();
        
         //Connect to server using username
        try {
            CreateServerConnection();
        } catch (Exception e) {
            System.out.println(e);
        }

        
        //System.out.println("");
        //System.out.println("Done. Sending Quit.");
        
        //connection.sendQuit();
        //we don't care to listen cuz we left
        //System.out.println("Successful Disconnect");

    }

    private static void RequestClientLogin() {
        ClientLogin login = new ClientLogin();
        User.setUsername(login.createLoginWindow());
    }

    private static void CreateServerConnection() throws Exception {
        connection = new Connection("127.0.0.1", User.getUsername());           //Change to accept IP from UI. Autofill loopback?
        System.out.println("Connecting to Server");
        
        
    }
    
    /**
     * 
     */
    public static void enterLobby() {
        Lobby l = new Lobby(User.getUsername(), connection);
    }
            
   

}
