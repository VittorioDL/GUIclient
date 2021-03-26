import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;

public class GUI extends JFrame implements ActionListener, KeyListener, MouseListener
{
    ConnessioneAServer conn;
    
    String richiesta="";
    String risposta;
    String messaggio="";
    String clientNick;
    boolean inserimentoUsername = true;
    
    ArrayList<String> utenti = new ArrayList(); 
    ArrayList<Messaggio> messaggi = new ArrayList();
    
    Timer t_utenti_collegati = new Timer(5000, this);
    Timer t_messaggi_ricevuti = new Timer(2000, this);
    
    JPanel right = new JPanel(new GridBagLayout());
    JPanel center = new JPanel();
    JPanel down = new JPanel();
    
    JScrollPane barraUtenti;// = new JScrollPane(right, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JLabel inserisciNickname;
    JTextField textField;     //textfield per inserimento username
    
    JTextField textFieldMex;  //textfield per invio messaggi
    JButton inviaNick;
    JButton inviaMex;
    
    GridBagConstraints gbc = new GridBagConstraints();
    public GUI()
    {  
        //Connessione con il server
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader tastiera = new BufferedReader(in);
        conn=new ConnessioneAServer("127.0.0.1",2000);
        
        //Layout del JFrame
        setSize(700, 850);
        setLocation(50, 50);
        setResizable(false);
        setLayout(new BorderLayout());
 
        center.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(center, BorderLayout.CENTER);
        
        right.setPreferredSize(new Dimension(160, 770));
        right.setBackground(new Color(217, 217, 217));
        
        
        down.setPreferredSize(new Dimension(850, 80));
        add(down, BorderLayout.SOUTH);
        
        
        //JLabel inserimento nickname
        inserisciNickname = new JLabel("Inserisci nickname");
        inserisciNickname.setFont(new Font("Century Gothic", Font.PLAIN, 24));
        inserisciNickname.setVisible(true);
        center.add(inserisciNickname);
        
        //Text field per scrivere il nickname
        textField = new JTextField("", 50);
        textField.setPreferredSize(new Dimension(50, 24));
        center.add(textField);
        
        //Text field per scrivere il messaggio
        textFieldMex = new JTextField("", 50);
        textFieldMex.setPreferredSize(new Dimension(50, 24));
        
        
        //Pulsante per invio nickname
        inviaNick = new JButton("INVIA");
        center.add(inviaNick);
        inviaNick.addActionListener(this);
        
        //Pulsante per invio messaggio
        inviaMex = new JButton("INVIA");
        inviaMex.addActionListener(this);
        
        t_utenti_collegati.setActionCommand("TUC");
        t_messaggi_ricevuti.setActionCommand("TMR");
     
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        //USRS--> Riceve i nomi dei partecipanti alla chat
        if(e.getActionCommand() == "TUC")
        {    
            if(utenti.size()==0)
            {
                risposta = conn.risposta("USRS");
                //Server: USERS[n1]§[n2]§[n3]...
                String[] users = risposta.split("§");
                utenti.add(users[0].substring(5));
                
                if(users.length>1)
                {
                    for(int i = 1; i <= users.length-1; i++) utenti.add(users[i]);
                }
            }
            
            else 
            {
                risposta = conn.risposta("USRS");
                //Server: USERS[n1]§[n2]§[n3]...
                String[] users = risposta.split("§");
                utenti.set(0, users[0].substring(5));
                
                if(users.length>1)
                {
                    for(int i = 1; i <= users.length-1; i++)
                    {
                        if(i > utenti.size()-1) utenti.add(users[i]);
                        else utenti.set(i, users[i]);
                    }
                }
            }
            
            right.removeAll();
            aggiuntaBarraUtenti();
        }
        
        //RCVD--> Riceve nuovi messasggi
        if(e.getActionCommand() == "TMR")
        {
            //richiesta = "RCVD"+clientNick;
        }
        
        if(e.getActionCommand() == "INVIA")
        {
            //NICK--> Inserimento Username
            if(inserimentoUsername) 
            {
                risposta = conn.risposta("NICK"+textField.getText());
                System.out.println("Risposta server a richiesta NICK: "+risposta);
                if(risposta.compareTo("ACCEPTED")==0)
                {
                    inserimentoUsername = false;
                    center.remove(textField);
                    center.remove(inviaNick);
                    down.add(textFieldMex);
                    down.add(inviaMex);
                    textFieldMex.setVisible(true);
                    center.remove(inserisciNickname);
                    t_utenti_collegati.start();
                    t_messaggi_ricevuti.start();
                    aggiuntaBarraUtenti();
                }
                else if(risposta.compareTo("DECLINED")==0)
                { 
                    inserisciNickname.setText("Username già in uso!");
                }
                
                invalidate();
                validate();
                repaint();
            }
            
            //TEXT--> Invia un messaggio
            else 
            {
                
            }
            
            textFieldMex.setText("");
            textField.setText("");
        }
    }
    
    public void aggiuntaBarraUtenti()
    {
        add(right, BorderLayout.EAST);
        barraUtenti = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //barraUtenti.setPreferredSize(new Dimension(160, 770));
        right.add(barraUtenti);
        
        gbc.insets = new Insets(4,4,4,4);
                
        gbc.gridy = 10;
        gbc.gridx = 1;
        JLabel labelUtenti = new JLabel("Utenti");
        labelUtenti.setVisible(true);
        labelUtenti.setFont(new Font("Century Gothic", Font.PLAIN, 20));    
        right.add(labelUtenti, gbc);
        
        for(int i = 0; i < utenti.size(); i++)
        {
            gbc.gridy += 20;
            JLabel U = new JLabel(utenti.get(i));
            U.setFont(new Font("Century Gothic", Font.PLAIN, 15));  
            U.setVisible(true);
            right.add(U, gbc);
        }
        invalidate();
        validate();
        repaint();
    }
    
    public String inviaRichiesta()
    {
        String appo = richiesta;
        richiesta = "";
        return appo;
    }
    
    public void riceviRisposta(String r)
    {
        risposta = r;
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
