import java.util.Date;
//Date(int year, int month, int date, int hrs, int min, int sec)
public class Messaggio
{
    private String text;
    private String nD;
    private String nM;
    private char mode;
    private Date date;
    private boolean sent = false;
    
    public Messaggio() {}
    
    public Messaggio(String text, String nD, String nM, Date date, char mode)
    {
        this.text = text;
        this.nD = nD;
        this.nM = nM;
        this.date = date;
        this.mode = mode;
    }

    public String getText()
    {
        return text;
    }
    
    public String getND()
    {
        return nD;
    }
    
    public String getNM()
    {
        return nM;
    }
    
    public Date getDate()
    {
        return date;
    }
    
    public char getMode()
    {
        return mode;
    }
    
    public void inviato()
    {
        sent = true;
    }
    
    public boolean getSent()
    {
        return sent;
    }
}
