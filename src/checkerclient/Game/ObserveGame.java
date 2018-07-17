package checkerclient.Game;

/**
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 */
import checkerclient.Connection;
import checkerclient.Connection;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ObserveGame extends JFrame implements ActionListener {

    int tableID;
    Connection connection;
    JPanel chatPanel = new JPanel();
    JPanel topImage = new JPanel();
    JPanel body = new JPanel();
    JPanel gamePanel = new JPanel();

    JLabel chatLabel = new JLabel();
    JLabel logoLabel = new JLabel();

    JButton chatButton = new JButton();
    JButton leaveObserve = new JButton();

    JTextField chatField = new JTextField();

    JTextArea chatArea = new JTextArea("");

    JScrollPane jScrollPane1 = new JScrollPane();

    JLabel red;
    JLabel black;
    JLabel blackKing;
    JLabel redKing;

    private int[][] tableState;

    
    private void exitProcedure(){
        connection.sendStopObserving(tableID);
        connection.removeObserver(this); //Disassociate this board on close
        this.dispose();
    }
    
    public ObserveGame(Connection con, int tID) {
        connection = con;
        tableID = tID;
        connection.addObserver(this);
        setSize(1280, 720);
        setResizable(false);
        
        //Send quit message before closing
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            exitProcedure();
        }
        });

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
            {2, 0, 2, 0, 2, 0, 2, 0}};

        gamePanel.setLayout(new GridLayout(8, 8));

        drawBoard();
    }

    private void drawVisual() {
        body.setBackground(new java.awt.Color(204, 204, 204));
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        setBackground(new java.awt.Color(102, 102, 102));

        body.setBackground(new java.awt.Color(204, 204, 204));

        leaveObserve.setText("Leave Observation");

        gamePanel.setBackground(new java.awt.Color(153, 153, 153));

        chatArea.setEditable(false);
        chatArea.setFont(new Font("Serif", Font.PLAIN, 15));
        chatArea.setLineWrap(true);
        chatArea.setForeground(Color.BLACK);

        jScrollPane1.setViewportView(chatArea);

        chatButton.addActionListener(this);
        leaveObserve.addActionListener(this);

        javax.swing.GroupLayout gamePanelLayout = new javax.swing.GroupLayout(gamePanel);

        gamePanel.setLayout(gamePanelLayout);

        gamePanelLayout.setHorizontalGroup(
                gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 400, Short.MAX_VALUE)
        );
        gamePanelLayout.setVerticalGroup(
                gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 400, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);

        body.setLayout(bodyLayout);

        bodyLayout.setHorizontalGroup(
                bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(bodyLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(leaveObserve)
                        .addGap(106, 106, 106)
                        .addComponent(gamePanel, 400, 400, 400)
                        .addContainerGap(213, Short.MAX_VALUE))
        );
        bodyLayout.setVerticalGroup(
                bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(bodyLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(leaveObserve)
                        .addGap(8, 8, 8)
                        .addComponent(gamePanel, 400, 400, 400)
                        .addContainerGap(156, Short.MAX_VALUE))
        );

        chatButton.setText("Send");

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
                        .addComponent(jScrollPane1)
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());

        getContentPane()
                .setLayout(layout);
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
                                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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

    private void createPieces() {
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

    }

    private void drawBoard() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                createPieces();

                if (j == 0) {
                    SquarePanel sp = new SquarePanel(1);
                    gamePanel.add(sp);

                    if (tableState[i][j] == 1) {

                        sp.add(red);
                    }
                    if (tableState[i][j] == 2) {

                        sp.add(black);
                    }
                    if (tableState[i][j] == 3) {

                        sp.add(redKing);
                    }
                    if (tableState[i][j] == 4) {

                        sp.add(blackKing);
                    }

                } else {
                    SquarePanel sp = new SquarePanel(2);
                    gamePanel.add(sp);

                    if (tableState[i][j] == 1) {

                        sp.add(red);
                    }
                    if (tableState[i][j] == 2) {

                        sp.add(black);
                    }
                    if (tableState[i][j] == 3) {

                        sp.add(redKing);
                    }
                    if (tableState[i][j] == 4) {

                        sp.add(blackKing);
                    }
                }
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chatButton) {
            if (chatField.getText().replaceAll("\\s+", "").length() < 1) //Don't allow white space only chat mesages
            {
                // do nothing
            } else {
                System.out.println(connection.getUserName() + ": " + chatField.getText());
                chatArea.append(connection.getUserName() + ": " + chatField.getText() + "\n");
                connection.sendChatAll(chatField.getText());
                chatField.setText("");
            }
        }
        if (e.getSource() == leaveObserve) {

            this.dispose();
        }
    }
}
