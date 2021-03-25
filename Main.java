//
//  ESEMPIO DI STRUTTURA DI UN CLIENT
//
import java.io.*;
import java.awt.event.*;

public class Main
{
    // VARIABILI GLOBALI CHE SERVONO PER RICHIEDERE
    // SERVIZI AL SERVER
    String richiesta="";
    
    // CREAZIONE DI UN OGGETTO PER LA CONNESSIONE AL SERVER
    // SPECIFICANDO INDIRIZZO E PORTA
    // (Vedi classe definita in seguito)
    
    public static void main(String args[]) throws Exception
    {
       InputStreamReader in = new InputStreamReader(System.in);
       BufferedReader tastiera = new BufferedReader(in);
       ConnessioneAServer conn=new ConnessioneAServer("127.0.0.1",2000);
    
       while(true)
       {
           String richiesta=tastiera.readLine();
           String risposta=conn.risposta(richiesta);
           System.out.println(risposta);
       }
    }
    
}