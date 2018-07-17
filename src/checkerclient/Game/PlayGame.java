package checkerclient.Game;

import checkerclient.AI;
import checkerclient.Connection;
import checkerclient.Connection;
import checkerclient.Move;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 */
public class PlayGame extends JFrame implements ActionListener {

    Connection connection;
    JPanel chatPanel = new JPanel();
    JPanel topImage = new JPanel();
    JPanel body = new JPanel();
    JPanel gamePanel = new JPanel();

    JLabel fromXLabel = new JLabel();
    JLabel fromYLabel = new JLabel();
    JLabel toXLabel = new JLabel();
    JLabel toYLabel = new JLabel();
    JLabel chatLabel = new JLabel();
    JLabel logoLabel = new JLabel();

    JButton chatButton = new JButton();
    JButton sendMove = new JButton();
    JButton recommendMove = new JButton();

    JTextField chatField = new JTextField();
    JTextField fromXText = new JTextField();
    JTextField fromYText = new JTextField();
    JTextField toXText = new JTextField();
    JTextField toYText = new JTextField();

    JLabel red;
    JLabel black;
    JLabel blackKing;
    JLabel redKing;

    private int turn; //1 is black 2 is red
    private int playingAs; //1 is back 2 is red    
    private int tableID;
    private boolean gameStarted = false;
    private AI ai;
    
    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
        if (playingAs == turn) {
            sendMove.setText("Send Move");
        }
        else {
            sendMove.setText("Not your turn");
        }
    }
    
    public void setGameStarted(boolean started) {
        this.gameStarted = started;
    }
    
    public boolean getGameStarted() {
        return gameStarted;
    }
    public int getPlayingAs() {
        return playingAs;
    }

    public void setPlayingAs(int playingAs) {
        this.playingAs = playingAs;
    }
    

    private class Coords {
        int x;
        int y;
        Coords () {
            
        }
        Coords (int x, int y) {
            this.x = x;
            this.y = y;
        }        
    }

    private int[][] tableState;
    private SquarePanel[][] spArray;

    /**
     * Send leave table message and close window
     */
    private void exitProcedure() {
        connection.sendLeaveTable();
        connection.setGameBoard(null); //Disassociate this board on close
        this.dispose();
    }

    public PlayGame(Connection con, int tid) {
        tableID = tid;
        setTitle("Table: " + tableID);
        connection = con;
        connection.setGameBoard(this);        
        setSize(1280, 720);
        setResizable(false);
        //Send quit message before closing
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitProcedure();
            }

        });
        turn = 0;                                                               //Maybe

        drawVisual();

        //This is a temp array for creating a table status.
        tableState = new int[][]{
            {0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 2, 0, 2, 0, 2, 0},
            {0, 2, 0, 2, 0, 2, 0, 2},
            {2, 0, 2, 0, 2, 0, 2, 0}
        };
        ai = new AI(tableState, playingAs, 3); //AI that looks ahead 3 moves
        spArray = new SquarePanel[8][8];

        gamePanel.setLayout(new GridLayout(8, 8));

        drawBoard();

    }

    private void drawVisual() {
        body.setBackground(new java.awt.Color(204, 204, 204));

        fromXLabel.setText("From X");

        fromYLabel.setText("From Y");

        toXLabel.setText("To X");

        toYLabel.setText("To Y");

        sendMove.setText("Send Move");
        sendMove.addActionListener(this);

        recommendMove.setText("Recommend Move");

        gamePanel.setBackground(new java.awt.Color(153, 153, 153));

        /*javax.swing.GroupLayout gamePanelLayout = new javax.swing.GroupLayout(gamePanel);
         gamePanel.setLayout(gamePanelLayout);
         gamePanelLayout.setHorizontalGroup(
         gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 400, Short.MAX_VALUE)
         );
         gamePanelLayout.setVerticalGroup(
         gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 400, Short.MAX_VALUE)
         );*/
        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
                bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(bodyLayout.createSequentialGroup()                         
                        .addGap(26, 26, 26)
                        .addComponent(recommendMove)  
                        .addGap(143, 143, 143)
                        .addComponent(gamePanel, 400, 400, 400)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bodyLayout.createSequentialGroup()
                        .addContainerGap(192, Short.MAX_VALUE)
                        .addComponent(fromXLabel)
                        .addGap(2, 2, 2)
                        .addComponent(fromXText, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fromYLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromYText, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(toXLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toXText, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(toYLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toYText, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(sendMove)
                        .addGap(184, 184, 184))
        );
        bodyLayout.setVerticalGroup(
                bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(bodyLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(recommendMove)
                        .addGap(8, 8, 8)
                        .addComponent(gamePanel, 400, 400, 400)
                        .addGap(40, 40, 40)
                        .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fromXLabel)
                                .addComponent(fromXText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fromYLabel)
                                .addComponent(fromYText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(toXLabel)
                                .addComponent(toXText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(toYLabel)
                                .addComponent(toYText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(sendMove))
                        .addContainerGap(99, Short.MAX_VALUE))
        );

        chatButton.setText("Send");
        chatButton.addActionListener(this);

        chatPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout chatPanelLayout = new javax.swing.GroupLayout(chatPanel);
        chatPanel.setLayout(chatPanelLayout);
        chatPanelLayout.setHorizontalGroup(
                chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
        );
        chatPanelLayout.setVerticalGroup(
                chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(chatField)
                                        .addComponent(chatButton, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                                        .addComponent(chatPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(chatLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(logoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(logoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(chatLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(chatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chatField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
        );

        add(gamePanel);
        add(chatPanel);
        add(topImage);
        add(body);

        setVisible(true);
    }

    /*private void createPieces() {
         red = new JLabel();
         black = new JLabel();
         blackKing = new JLabel();
         redKing = new JLabel();

         ImageIcon ImageIcon1 = new ImageIcon("Red.png");
         Image Image1 = ImageIcon1.getImage();
         red.setIcon(ImageIcon1);

         ImageIcon ImageIcon2 = new ImageIcon("Black.png");
         Image Image2 = ImageIcon2.getImage();
         black.setIcon(ImageIcon2);

         ImageIcon ImageIcon3 = new ImageIcon("BlackKing.png");
         Image Image3 = ImageIcon3.getImage();
         blackKing.setIcon(ImageIcon3);

         ImageIcon ImageIcon4 = new ImageIcon("RedKing.png");
         Image Image4 = ImageIcon4.getImage();
         redKing.setIcon(ImageIcon4);

     }*/
    private void drawBoard() {
        
        gamePanel.setLayout(new GridLayout(8, 8));
        int[][] tableGrid = new int[][]{
            {2, 1, 2, 1, 2, 1, 2, 1},
            {1, 2, 1, 2, 1, 2, 1, 2},
            {2, 1, 2, 1, 2, 1, 2, 1},
            {1, 2, 1, 2, 1, 2, 1, 2},
            {2, 1, 2, 1, 2, 1, 2, 1},
            {1, 2, 1, 2, 1, 2, 1, 2},
            {2, 1, 2, 1, 2, 1, 2, 1},
            {1, 2, 1, 2, 1, 2, 1, 2}};
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                SquarePanel sp = new SquarePanel(tableGrid[i][j]);
                spArray[i][j] = sp;
                gamePanel.add(spArray[i][j]); 
            }
        }
        addPieces();        
    }

    public void addPieces() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (tableState[i][j] == 1) {

                    JLabel black = new JLabel();

                    ImageIcon ImageIcon2 = new ImageIcon("Black.png");
                    black.setIcon(ImageIcon2);
                    spArray[i][j].add(black);
                }
                if (tableState[i][j] == 2) {
                    JLabel red = new JLabel();

                    ImageIcon ImageIcon1 = new ImageIcon("Red.png");
                    red.setIcon(ImageIcon1);
                    spArray[i][j].add(red);
                }
                if (tableState[i][j] == 3) {

                    JLabel blackKing = new JLabel();
                    ImageIcon ImageIcon3 = new ImageIcon("BlackKing.png");
                    blackKing.setIcon(ImageIcon3);
                    spArray[i][j].add(blackKing);
                }
                if (tableState[i][j] == 4) {

                    JLabel redKing = new JLabel();

                    ImageIcon ImageIcon4 = new ImageIcon("RedKing.png");
                    redKing.setIcon(ImageIcon4);
                    spArray[i][j].add(redKing);
                }
            }
        }
    }

    public void update(int[][] tableState) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                spArray[i][j].removeAll();
            }
        }
        this.tableState = tableState;
        addPieces();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chatButton) {
            if(chatField.getText().equalsIgnoreCase("ready")) {
                connection.sendReady();
            }
        }
        
        if (e.getSource() == recommendMove && playingAs == turn) {
            Move move = ai.recommendMove(tableState);
            fromXText.setText("" + move.getFromX());
            fromYText.setText("" + move.getFromY());
            toXText.setText("" + move.getToX());
            toYText.setText("" + move.getToY());            
        }

        if (e.getSource() == sendMove && playingAs == turn) {
            Coords from = new Coords();
            Coords to = new Coords();

            //TODO: temporary needs to break or something instead
            boolean fail = false;
            if (TryParseInt(fromXText.getText())) {
                from.x = Integer.parseInt(fromXText.getText());
            } else {
                fail = true;
            }

            if (TryParseInt(fromYText.getText())) {
                from.y = Integer.parseInt(fromYText.getText());
            } else {
                fail = true;
            }

            if (TryParseInt(toXText.getText())) {
                to.x = Integer.parseInt(toXText.getText());
            } else {
                fail = true;
            }

            if (TryParseInt(toYText.getText())) {
                to.y = Integer.parseInt(toYText.getText());
            } else {
                fail = true;
            }

            //movePiece(from, to);
            connection.sendMove(from.x, from.y, to.x, to.y);
            //Change turn
            if (turn == 1) {
                turn = 2;
            }
            else {
                turn = 1;
            }
        }
    }

    private boolean TryParseInt(String val) {
        boolean parsable = true;
        try {
            Integer.parseInt(val);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        return parsable;
    }

    /**
     * Called from Connection
     * @param move 
     */
    public void externalMovePiece(int fX, int fY, int tX, int tY) {
        Coords from = new Coords(fX, fY);
        Coords to = new Coords(tX, tY);
        movePiece(from, to);
    }
    private void movePiece(Coords from, Coords to) {
        //gets color and status
        int value = tableState[from.y][from.x];

        //if not moved do nothing & don't change turn if that's controlled here
        if (!isLocationSame(from, to) && !pieceCollision(from, to)) {
            //make location empty
            tableState[from.y][from.x] = 0;
            tableState[to.y][to.x] = value;
            //piece moved successfully. 
           
            drawBoard();
        }
    }

    private boolean isLocationSame(Coords from, Coords to) {
        boolean sameLoc = false;

        if (from.x == to.x && from.y == to.y) {
            sameLoc = true;

        }

        return sameLoc;
    }

    private boolean pieceCollision(Coords from, Coords to) {

        //piece here?
        boolean collision = tableState[to.y][to.x] != 0;

        return collision;
    }

//    @Override
//    public void mousePressed(MouseEvent me){
//        int x = me.getX();
//    }
}
