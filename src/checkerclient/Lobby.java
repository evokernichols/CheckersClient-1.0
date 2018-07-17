package checkerclient;

import checkerclient.Game.ObserveGame;
import checkerclient.Game.PlayGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 */
public class Lobby extends JFrame implements ActionListener {

    JPanel chatPanel = new JPanel();
    JPanel topImage = new JPanel();
    JPanel body = new JPanel();
    
    JLabel logoLabel = new JLabel();
    JLabel chatLabel = new JLabel();
    
    JButton chatSend = new JButton();
    JButton startGame = new JButton();
    JButton joinGame = new JButton();
    JButton observeGame = new JButton();
    
    JTextField chatField = new JTextField();                                    
    JTextField desiredTableField = new JTextField();

    JTextArea chatArea = new JTextArea("");

    JScrollPane jScrollPane1 = new JScrollPane();
    JScrollPane jScrollPane2 = new JScrollPane();
    JScrollPane jScrollPane3 = new JScrollPane();
    String username;
    JList observeList = new JList();
    JList joinList = new JList();
    
    Connection connection;
    ArrayList<Integer> tablesArray;

    /**
     * Send quit message and close program
     */
    private void exitProcedure() {
        connection.sendQuit();
        this.dispose();
        System.exit(0);
    }
    
    /**
     * Default Constructor
     */
    Lobby() {

    }  
    
    /**
     * Lobby Constructor
     * @param username Client's username
     * @param con The associated Connection object linking the client and server
     */
    Lobby(String username, Connection con) {
        setSize(1280, 720);
        setResizable(false);
        connection = con;
        connection.setLobby(this);
        //Send quit message before closing
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitProcedure();
            }           
        });
        
        this.username = username;
        chatSend.addActionListener(this);

        startGame.addActionListener(this);
        joinGame.addActionListener(this);
        observeGame.addActionListener(this);
        
        chatPanel.add(chatSend);
        chatPanel.add(chatField);

        chatArea.setEditable(false);
        chatArea.setFont(new Font("Serif", Font.PLAIN, 15));
        chatArea.setLineWrap(true);
        chatArea.setForeground(Color.BLACK);

        jScrollPane1.setViewportView(chatArea);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 102));

        chatSend.setText("Send");

        chatPanel.setBackground(new java.awt.Color(255, 255, 255));

        chatArea.setColumns(20);
        chatArea.setRows(5);
        jScrollPane1.setViewportView(chatArea);

        javax.swing.GroupLayout chatPanelLayout = new javax.swing.GroupLayout(chatPanel);
        chatPanel.setLayout(chatPanelLayout);
        chatPanelLayout.setHorizontalGroup(
            chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        chatPanelLayout.setVerticalGroup(
            chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                .addContainerGap())
        );

        chatLabel.setBackground(new java.awt.Color(102, 102, 102));
        chatLabel.setFont(new java.awt.Font("Vrinda", 1, 48)); // NOI18N
        chatLabel.setForeground(new java.awt.Color(255, 255, 255));
        chatLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chatLabel.setText("Chat");
        chatLabel.setOpaque(true);

        logoLabel.setBackground(new java.awt.Color(102, 102, 102));
        logoLabel.setFont(new java.awt.Font("Vrinda", 1, 60)); // NOI18N
        logoLabel.setForeground(new java.awt.Color(255, 255, 255));
        logoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoLabel.setText("King Me");
        logoLabel.setOpaque(true);

        startGame.setText("Start Game");

        jScrollPane3.setViewportView(observeList);
        jScrollPane2.setViewportView(joinList);
        joinGame.setText("Join Game");
        observeGame.setText("Observe Game");
        
        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
                bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(joinGame)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(observeGame)
                        .addGap(78, 78, 78))
                .addGroup(bodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]
        {
            jScrollPane2, jScrollPane3
        });

        bodyLayout.setVerticalGroup(
                bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(bodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                .addComponent(jScrollPane2))
                        .addGap(18, 18, 18)
                        .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(joinGame)
                                .addComponent(observeGame))
                        .addContainerGap(115, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(chatField)
                                        .addComponent(chatSend, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                                        .addComponent(chatPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(chatLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(logoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 861, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(27, 27, 27)
                                                        .addComponent(startGame)))
                                        .addContainerGap())
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(body, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(177, 177, 177))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(logoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(chatLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(body, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(55, 55, 55)))
                        .addComponent(chatField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chatSend, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                .addGroup(layout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(startGame)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        chatArea.append("Welcome to Chat\n");
        
        add(chatPanel);
        add(topImage);
        add(body);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == chatSend)
        {
            if (chatField.getText().replaceAll("\\s+","").length() < 1) //Don't allow white space only chat mesages
            {
                // do nothing
            } else
            {
                System.out.println(username + ": " + chatField.getText());
                chatArea.append(username + ": " + chatField.getText() + "\n");
                connection.sendChatAll(chatField.getText());
                chatField.setText("");                
            }
        }
        if(e.getSource()==startGame){
            //this.dispose(); Think the lobby is supposed to stay open even when in a game
            //Send messagte
            connection.sendMakeTable();
        }     
        if (e.getSource() == joinGame){
            //not sure if this is right
            connection.sendJoinTable(Integer.parseInt(chatField.getText()));    //VERY TEMPORARYY CHANGE THIS

            //this might be what we need, the index could correspond with the table number that needs sent to the server.
            //if thats the case we might need another another constructor to build these tables?
            //PlayGame game = new PlayGame(joinList.getSelectedIndex()); 			
        }
        if (e.getSource() == observeGame){			
            connection.sendObserveTable(Integer.parseInt(chatField.getText()));    //VERY TEMPORARYY CHANGE THIS
            //ObserveGame = new ObserveGame(joinList.getSelectedIndex()); similar to above.
        }      
    }      
    
    /**
     * Join table when table joined message is received
     */
    public void joinTable (int tableID) {
        PlayGame playGame = new PlayGame(connection, tableID);     
    }
    public void observeTable(int tableID) {
        ObserveGame observeGame = new ObserveGame(connection, tableID); 
    }
    
    /**
     * 
     * @param TIDS 
     */
    public void initTablesArray(int[] TIDS) {
        tablesArray = new ArrayList<Integer>();
        for (int tid: TIDS) {
            tablesArray.add(tid);
        }
        queryTables();
    }
    
    public void addTable(int tableID) {
        tablesArray.add(tableID);
        queryTables();
    }
    
    /**
     * Query the tables list
     */
    public void queryTables() {
        for (int table: tablesArray) {
            connection.sendAskTableStatus(table); //This will request status messages for each
        }
    }
    
    /**
     * Updates the views of the provided table
     * @param tableID
     * @param redPlayer
     * @param blackPlayer 
     */
    public void updateTableView(int tableID, String redPlayer, String blackPlayer) {        
        if ("-1".equals(redPlayer)) {   //Red slot open
            //UI CODE 
        }
        else {                          //Red slot taken
            //UI CODE 
        }
        if ("-1".equals(blackPlayer)) { //Black slot open
            //UI CODE
        }
        else {                          //Black slot taken
            //UI CODE
        }
    }
    
    /**
     * Updates the client side chat window with new messages
     * @param fromUser User from which the message originated
     * @param msgType 0 if all chat, 1 if private message
     * @param message Chat message contents
     */
    public void updateChat(String fromUser, int msgType, String message) {
        if (msgType == 0) { //All chat
            System.out.println(fromUser + ": " + message);
            chatArea.append(fromUser + ": " + message + "\n");
        }
        else{ //PM
            System.out.println("*PM* " + fromUser + ": " + message);
            chatArea.append("*PM* " + fromUser + ": " + message + "\n");
        }   
    }
}
