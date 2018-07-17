package checkerclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 */
public class ClientLogin extends JFrame implements Runnable {

    JPanel p = new JPanel();
    JPanel logo = new JPanel();
    JButton login = new JButton("Login");
    JTextField username = new JTextField(20);
    JLabel name = new JLabel("Username");
    JLabel logoLabel = new JLabel();
    String enteredName = "";
    boolean nameEntered = false;

    ClientLogin() {

    }

    public String createLoginWindow() {
    setSize(400, 300);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        logoLabel.setBackground(new java.awt.Color(102, 102, 102));
        logoLabel.setFont(new java.awt.Font("Vrinda", 1, 30)); // NOI18N
        logoLabel.setForeground(new java.awt.Color(255, 255, 255));
        logoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoLabel.setText("King Me");
        logoLabel.setToolTipText("");
        logoLabel.setOpaque(true);

        login.setText("Login");

        name.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        name.setText("Enter Username");


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))

            .addGroup(layout.createSequentialGroup()
                .addGap(163, 163, 163)

                .addComponent(login)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(145, 145, 145)

                .addComponent(name)
                .addContainerGap(145, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(logoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)


                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(login)
                .addGap(34, 34, 34))

        );

        p.add(name);
        p.add(username);
        p.add(login);
        
        logo.add(logoLabel);
        
        add(logo);
        add(p);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processUsername();
            }
        });

        setVisible(true);

        this.run();

        return enteredName;
    }

    @Override
    public void run() {
        try {
            while (enteredName.equals("")&& !nameEntered) {
                Thread.sleep(5000);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(ClientLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void processUsername() {
        enteredName = username.getText();
        nameEntered = true;

        //close window
        this.dispose();

        //open lobby
        //Lobby l = new Lobby(enteredName); //MOVED TO CheckerClient so Connection object could be passed
    }

}
