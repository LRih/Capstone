/*
 * Purpose: Stock data storage container.
 */
package timeseries.entities;

/* 
 * Package Imports 
 */
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author daniel
 */
public class Stock {
    protected Date listedDate;
    protected String stockSymbol;
    protected float priceOpen;
    protected float priceClose;
    protected float priceHigh;
    protected float priceLow;
    protected float volume;
    
    public Stock ()
    {
        
    }
    
    public boolean setDate (String strDate)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-mm-dd");
        
        try
        {
            listedDate = dateFormatter.parse(strDate);
        }
        catch(Exception e)
        {
            System.out.println("Exception Thrown: " + e);
            return false;       
        }
        
        return true;
    }
}
