package checkerclient.Game;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * @author Ryan Nichols, Sydney Pratt, Andrew Stein, Jan Cabonce
 */
public class SquarePanel extends JPanel{
    Color color = Color.RED;

    public SquarePanel(int c){ //1 black 2 red
        if (c == 1){
            color = Color.BLACK;
        } else {
            color = Color.RED;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}