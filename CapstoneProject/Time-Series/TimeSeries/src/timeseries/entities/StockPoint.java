/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseries.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author daniel
 */
public class StockPoint {
    protected Date listedDate;
    protected String stockSymbol;
    protected double priceOpen;
    protected double priceClose;
    protected double priceHigh;
    protected double priceLow;
    protected double rateOfReturn;
    protected double volume;
    
    public StockPoint()
    {
        rateOfReturn = 0.0;
    }
    
    public StockPoint (String listedDate, String stockSymbol, double priceOpen, 
            double priceClose, double priceHigh, double priceLow, double volume)
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
        
        this.setListedDate(listedDate);
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
    
    public Date getListedDate ()
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
    
    public double getVolume()
    {
        /** 
         * @return the volume of stock sold.
         */
        return volume;
    }
    
    
    public boolean setListedDate (String strDate)
    {
        /**
         * @param   strDate the string date to be converted into a date-time format and saved.
         * @return  a boolean that states if the operation was successful.
         */
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
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
    
    public void setVolume(double volume)
    {
        /** 
         * @param volume the volume of stock sold.
         */
        this.volume = volume;
    }
}
