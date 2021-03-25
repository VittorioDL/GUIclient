import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener, KeyListener, MouseListener
{
    Timer t = new Timer(10, this);
    public GUI()
    {  
        setSize(600, 800);
        setLocation(50, 50);
        setLayout(null); 
        setResizable(false);
        setVisible(true);
        
        t.setActionCommand("TIMER");
        
       
        
       
        t.start();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        
        if(e.getActionCommand() == "TIMER")
        {
            
        }
    }
    
    public void keyReleased(KeyEvent e)
    {
        
    }
    
    public void keyTyped(KeyEvent e)
    {
        
    }
    
    public void keyPressed(KeyEvent e)
    {
       
    }
    
    public void mouseExited(MouseEvent e)
    {
        
    }
    
    public void mouseEntered(MouseEvent e)
    {
        
    }
    
    public void mouseReleased(MouseEvent e)
    {
        
    }
    
    public void mousePressed(MouseEvent e)
    {
      
    }
    
    public void mouseClicked(MouseEvent e)
    {
           
    }
    
}
