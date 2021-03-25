import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener, KeyListener, MouseListener
{
    int nSprite = (int)(Math.random()*7+3);
    
    ArrayList<Point> posizioni; 
    ArrayList<Point> destinazioni;
    
    Timer t = new Timer(10, this);
    public GUI()
    {  
        setSize(600, 800);
        setLocation(50, 50);
        setLayout(null); 
        setResizable(false);
        setVisible(true);
        
        t.setActionCommand("TIMER");
        
        //Creiamo gli sprites in modo random
        
        
        //Posizioniamo e visualizziamo gli sprites in modo random
        posizioni = new ArrayList<Point>(nSprite); 
        destinazioni = new ArrayList<Point>(nSprite); 
        
        for(int i = 0; i < nSprite; i++)
        {
            int x = (int)(Math.random()*(500))+50;
            int y = (int)(Math.random()*(700));
            
            int x1 = (int)(Math.random()*(500))+50;
            int y1 = (int)(Math.random()*(700));
            
            posizioni.add(new Point(x, y));
            destinazioni.add(new Point(x1, y1));
            
            
        }
        t.start();
    }
    
    public void spostamentoLabel(int currentX, int currentY, int x, int y, JLabel testo) 
    {   
        if(currentX<x)
        {
            currentX++;
            testo.setLocation(currentX, currentY);
        }
                
        else if(currentX>x)
        {
            currentX--;
            testo.setLocation(currentX, currentY);
        }
                
        if(currentY<y)
        {
            currentY++;
            testo.setLocation(currentX, currentY);
        }
                
        else if(currentY>y)
        {
            currentY--;
            testo.setLocation(currentX, currentY);
        }
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
