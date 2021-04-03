import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;
import java.lang.*;
import java.util.Date;

public class GUI extends JFrame implements ActionListener, KeyListener, MouseListener
{
    ConnessioneAServer conn;
    
    String richiesta="";
    String risposta;
    String messaggio="";
    String clientNick;
    String selectedUser = " ";
    
    boolean inserimentoUsername = true;
    int centerY = 770;
    int rightY = 770;
    
    ArrayList<String> utenti = new ArrayList(); 
    ArrayList<Messaggio> messaggi = new ArrayList();
    
    Timer t_utenti_collegati = new Timer(5000, this);
    Timer t_messaggi_ricevuti = new Timer(2000, this);
    
    JPanel right = new JPanel();
    JPanel center = new JPanel();
    JPanel down = new JPanel();
    
    JScrollPane barraUtenti = new JScrollPane(right, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JScrollPane barraMessaggi = new JScrollPane(center, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    //Componenti per inserimento nickname
    JLabel inserisciNickname;
    JTextField textField;     
    JButton inviaNick;
    
    //Componenti per invio messaggi
    JButton inviaMex;
    JTextField textFieldMex;  
    
    //JButton per disconnettersi
    JButton quit;
    
    public GUI()
    {  
        //Connessione con il server
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader tastiera = new BufferedReader(in);
        conn=new ConnessioneAServer("127.0.0.1",2000);
        
        //Layout del JFrame
        setSize(700, 850);
        setLocation(50, 50);
        setResizable(true);
        setLayout(new BorderLayout());
        
        center.setPreferredSize(new Dimension(540, centerY));
        center.setLayout(new FlowLayout(FlowLayout.CENTER));
        center.setBackground(new Color(240, 240, 240));
        add(center, BorderLayout.CENTER);
        add(new JScrollPane(center, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        
        right.setPreferredSize(new Dimension(160, rightY)); 
        right.setBackground(new Color(157, 214, 233));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        
        down.setPreferredSize(new Dimension(850, 80));
        down.setBackground(new Color(229, 239, 248));
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
        
        //Pulsante per invio nickname
        inviaNick = new JButton("INVIA");
        center.add(inviaNick);
        inviaNick.addActionListener(this);
        
        //Pulsante per invio messaggio
        inviaMex = new JButton("INVIA");
        inviaMex.addActionListener(this);
        
        //Text field per scrivere il messaggio
        textFieldMex = new JTextField("", 50);
        textFieldMex.setPreferredSize(new Dimension(50, 24));
        
        //Pulsante per disconnettersi
        quit = new JButton("QUIT");
        quit.addActionListener(this);
        
        t_utenti_collegati.setActionCommand("TUC");
        t_messaggi_ricevuti.setActionCommand("TMR");
     
        setVisible(true);
    }
    
    public static void main(String args[])
    {
        new GUI();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        //USRS--> Riceve i nomi dei partecipanti alla chat
        if(e.getActionCommand() == "TUC")
        {    
            utenti.clear();
            risposta = conn.risposta("USRS");
            //Server: USERS[n1]§[n2]§[n3]...
            String[] users = risposta.split("§");
            utenti.add(users[0].substring(5));
                
            if(users.length>1)
            {
                for(int i = 1; i <= users.length-1; i++)
                {
                    utenti.add(users[i]);
                }
            }
            
            right.removeAll();
            aggiuntaBarraUtenti();
        }
        
        //RCVD--> Riceve nuovi messaggi
        if(e.getActionCommand() == "TMR")
        {
            Messaggio new_mex;
            messaggi.clear();
            risposta = conn.risposta("RCVD"+clientNick);
            
            if(!(risposta.compareTo("NO MEX")==0))
            {
                String[] new_messaggi = risposta.split("§");
                int num_mex = 1;
            
                //RF<NM1>§<TEXT1>§<DATE1>§<MODE1>§<NM2>§<TEXT2>§<DATE2>§<MODE2>§<NM1>§<TEXT1>§<DATE1>§<MODE1>
                for(int i = 0; i < new_messaggi.length/4; i++)
                {
                    String[] data = new_messaggi[2+i*4].split("/");
                    
                    Date data_mex = new Date(); 
                    data_mex.setHours(Integer.parseInt(data[0]));
                    data_mex.setMinutes(Integer.parseInt(data[1]));
                    data_mex.setSeconds(Integer.parseInt(data[2]));

                    if(new_messaggi[3].charAt(0) == 'B')
                    {
                        if(i == 0) new_mex = new Messaggio(new_messaggi[1], " ", new_messaggi[0].substring(2), data_mex, new_messaggi[3].charAt(0));
                        else new_mex = new Messaggio(new_messaggi[1+i*4], " ", new_messaggi[i*4], data_mex, new_messaggi[3+i*4].charAt(0));
                    }
                    
                    else 
                    {
                        if(i == 0) new_mex = new Messaggio(new_messaggi[1], clientNick, new_messaggi[0].substring(2), data_mex, new_messaggi[3].charAt(0));
                        else new_mex = new Messaggio(new_messaggi[1+i*4], clientNick, new_messaggi[i*4], data_mex, new_messaggi[3+i*4].charAt(0));
                    }
                    messaggi.add(new_mex);
                }
            }
            
            aggiuntaMessaggi();
        }
        
        if(e.getActionCommand() == "INVIA")
        {
            //NICK--> Inserimento Username
            if(inserimentoUsername) 
            {
                if(textField.getText().contains("§")) 
                {
                    inserisciNickname.setText("<html><font color='red'> Carattere § non valido!</font></html>");
                }
                else 
                {
                    risposta = conn.risposta("NICK"+textField.getText());
                    if(risposta.compareTo("ACCEPTED")==0)
                    {
                        inserimentoUsername = false;
                        clientNick = textField.getText();
                        
                        center.remove(textField);
                        center.remove(inviaNick);
                        center.remove(inserisciNickname);
                    
                        down.add(quit);
                        down.add(textFieldMex);
                        down.add(inviaMex);
                    
                        t_utenti_collegati.start();
                        t_messaggi_ricevuti.start();
                        aggiuntaBarraUtenti();
                    }
                
                    else if(risposta.compareTo("DECLINED")==0)
                    { 
                        inserisciNickname.setText("<html><font color='red'> Username non disponibile!</font></html>");
                    }
                }
                
                revalidate();
                repaint();
            }
            
            //TEXT--> Invia un messaggio
            else 
            {
                if(!textFieldMex.getText().contains("§"))
                {
                    //TEXT[Mode][Text]§[nM]§[nD]
                    Date data_mex = new Date(); 
                    
                    if(selectedUser.compareTo(" ") == 0) 
                    {
                        risposta = conn.risposta("TEXTB"+textFieldMex.getText()+"§"+clientNick+"§"+" ");    
                        //messaggi.add(new Messaggio(textFieldMex.getText(), " ", clientNick, data_mex, 'B'));
                    }
                    
                    else 
                    {
                        risposta = conn.risposta("TEXTU"+textFieldMex.getText()+"§"+clientNick+"§"+selectedUser);
                        messaggi.add(new Messaggio(textFieldMex.getText(), selectedUser, clientNick, data_mex, 'U'));
                    }    
                    selectedUser = " ";
                }
            }
            aggiuntaMessaggi();
            textFieldMex.setText("");
            textField.setText("");
        }
        
        if(e.getActionCommand() == "QUIT")
        {
            conn.risposta("QUIT"+clientNick);
            utenti.remove(clientNick);
            
            t_utenti_collegati.stop();
            t_messaggi_ricevuti.stop();
            
            inserimentoUsername = true;
            
            center.removeAll();
            down.removeAll();
            right.removeAll();
            
            center.setLayout(new FlowLayout(FlowLayout.CENTER));
            revalidate();
            repaint();
            
            inserisciNickname.setText("<html><font color='black'> Inserisci nickname</font></html>");

            center.add(inserisciNickname);
            center.add(textField);
            center.add(inviaNick);
       
            revalidate();
            repaint();
        }
    }
    
    public void aggiuntaBarraUtenti()
    {
        add(BorderLayout.EAST, barraUtenti);
        
        //Aggiunta label "Utenti"
        JLabel labelUtenti = new JLabel("Utenti");
        labelUtenti.setVisible(true);
        labelUtenti.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        labelUtenti.setAlignmentX(right.CENTER_ALIGNMENT);
        right.add(labelUtenti);
        
        JLabel me = new JLabel("Me: "+clientNick);
        me.setFont(new Font("Century Gothic", Font.PLAIN, 15));  
        me.setAlignmentX(right.CENTER_ALIGNMENT);
        me.setVisible(true);
        right.add(me);
        for(int i = 0; i < utenti.size(); i++)
        {
            if(utenti.get(i).compareTo(clientNick) != 0)
            {
                JLabel U = new JLabel(utenti.get(i));
                U.setFont(new Font("Century Gothic", Font.PLAIN, 15));  
                U.setAlignmentX(right.CENTER_ALIGNMENT);
                U.setVisible(true);
                U.addMouseListener(this);
                U.setName(utenti.get(i));
                
                if(utenti.get(i).compareTo(selectedUser)==0) U.setBorder(BorderFactory.createLineBorder(new Color(255, 92, 51), 2));
                
                right.add(U);
            } 
        }

        right.setVisible(true);
        revalidate();
        repaint();
    }
    
    public void aggiuntaMessaggi()
    {
        /*
         Per ogni messaggio nuovo aggiunge a center un JPanel contenente
         il messaggio, il mittente, il destinatario e la data
        */
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        for(int i = 0; i < messaggi.size(); i++)
        {
            //tutto:  700, 850
            //right:  160, 770  
            //down :  850,  80
            //center: 540, 770
            JPanel mex_panel = new JPanel();
            mex_panel.setPreferredSize(new Dimension(500, 160));
            mex_panel.setLayout(new BoxLayout(mex_panel, BoxLayout.Y_AXIS));
            
            
            JLabel nM_nD;
            if(messaggi.get(i).getMode() == 'B') 
            {
                nM_nD = new JLabel("da "+messaggi.get(i).getNM()+" a tutti ("+messaggi.get(i).getDate()+")");
                mex_panel.setBackground(new Color(154, 203, 250));
            }

            else if(messaggi.get(i).getNM().compareTo(clientNick)==0)
            {   
                nM_nD = new JLabel("a "+messaggi.get(i).getND()+" ("+messaggi.get(i).getDate()+")");
                mex_panel.setBackground(new Color(230, 242, 255));
            }
            
            else
            {
                nM_nD = new JLabel("da "+messaggi.get(i).getNM()+" a me ("+messaggi.get(i).getDate()+")");
                mex_panel.setBackground(new Color(204, 255, 204));
            }
            
            nM_nD.setFont(new Font("Century Gothic", Font.PLAIN, 17));  
            
            JLabel mex_text = new JLabel(messaggi.get(i).getText());
            mex_text.setFont(new Font("Century Gothic", Font.PLAIN, 22));  
            
            mex_panel.add(nM_nD);
            mex_panel.add(mex_text);

            center.add(mex_panel);
            //Distanziatore tra messaggi
            center.add(Box.createRigidArea(new Dimension(0, 8)));
            centerY += 168;
            center.setPreferredSize(new Dimension(540, centerY));
        }
        center.setVisible(true);
        revalidate();
        repaint();
    }
     
    public void mousePressed(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1 && e.getSource() instanceof JLabel)
        {
            JLabel source = (JLabel)e.getSource();
            
            if(source.getName().compareTo(selectedUser) == 0)
            {
                source.setBorder(BorderFactory.createLineBorder(new Color(255, 92, 51), 0));
                selectedUser = " ";
                t_utenti_collegati.stop();
                right.removeAll();
                aggiuntaBarraUtenti();
                t_utenti_collegati.start();
            }
            
            else 
            {
                selectedUser = source.getName();
            
                t_utenti_collegati.stop();
                right.removeAll();
                aggiuntaBarraUtenti();
                t_utenti_collegati.start();
            }
        }
    }
    
    public void mouseExited(MouseEvent e){}
    
    public void mouseEntered(MouseEvent e){}
    
    public void mouseReleased(MouseEvent e){}
    
    public void mouseClicked(MouseEvent e){}
    
    public void keyReleased(KeyEvent e){}
    
    public void keyTyped(KeyEvent e){}
    
    public void keyPressed(KeyEvent e){}
}
