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
    protected double priceOpen;
    protected double priceClose;
    protected double priceHigh;
    protected double priceLow;
    protected double rateOfReturn;
    protected long volume;
    
    public Stock ()
    {
        rateOfReturn = 0.0;
    }
    
    public Stock (String listedDate, String stockSymbol, double priceOpen, 
            double priceClose, double priceHigh, double priceLow, long volume)
    {
        /**
         * @param   strDate     the string date to be converted into a date-time format and saved.
         * @param   priceClose  the closing price of the stock.
         * @param   priceHigh   the highest sold price of the stock.
         * @param   priceLow    the lowest sold price of the stock.
         * @param   priceOpen   the opening price of the stock.
         * @param   stockSymbol the symbol of stock.
         * @param   volume      the volume of stock sold.
         */
        
        this.setDate(listedDate);
        this.stockSymbol = stockSymbol;
        this.priceOpen = priceOpen;
        this.priceClose = priceClose;
        this.priceHigh = priceHigh;
        this.priceLow = priceLow;
        this.volume = volume;
        
        rateOfReturn = priceHigh - priceLow;
    }
    
    public void calculateRateOfReturn()
    {
        rateOfReturn = priceHigh - priceLow;
    }
    
    public Date getDate ()
    {
        return listedDate;
    }
    
    public double getPriceClose()
    {
        /** 
         * @return the closing price of the stock.
         */
        return priceOpen;
    }
    
    public double getPriceHigh()
    {
        /**
         * @return the highest sold price of the stock.
         */
        return priceHigh;
    }
    
    public double getPriceLow()
    {
        /**
         * @return the lowest sold price of the stock.
         */
        return priceHigh;
    }
    
    public double getPriceOpen()
    {
        /** 
         * @return the opening price of the stock.
         */
        return priceOpen;
    }
    
    public String getSymbol()
    {
        /** 
         * @return the symbol of stock.
         */
        return stockSymbol;
    }
    
    public long getVolume()
    {
        /** 
         * @return the volume of stock sold.
         */
        return volume;
    }
    
    
    public boolean setDate (String strDate)
    {
        /**
         * @param   strDate the string date to be converted into a date-time format and saved.
         * @return  a boolean that states if the operation was successful.
         */
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
    
    public void setPriceClose(double priceClose)
    {
        /** 
         * @param priceClose the closing price of the stock.
         */
        this.priceClose = priceClose;
    }
    
    public void setPriceHigh(double priceHigh)
    {
        /**
         * @param priceHigh the highest sold price of the stock.
         */
        this.priceHigh = priceHigh;
    }
    
    public void setPriceLow(double priceLow)
    {
        /**
         * @param priceLow the lowest sold price of the stock.
         */
        this.priceLow = priceLow;
    }
    
    public void setPriceOpen(double priceOpen)
    {
        /** 
         * @param priceOpen the opening price of the stock.
         */
        this.priceOpen = priceOpen;
    }
    
    public void setSymbol(String stockSymbol)
    {
        /** 
         * @param stockSymbol the symbol of stock.
         */
        this.stockSymbol = stockSymbol;
    }
    
    public void setVolume(long volume)
    {
        /** 
         * @param volume the volume of stock sold.
         */
        this.volume = volume;
    }
}
