/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Daniel
 * StockPoint.java
 * Describe: This is a Stock class entity
 * @modify Jason.Zhuang
 * 30/03/2017
 */
public class StockPoint 
{
    protected Date   listedDate;
    protected String stockSymbol;
    protected double priceOpen;
    protected double priceClose;
    protected double priceHigh;
    protected double priceLow;
    //Rate of Return (x value) = Highest Transaction - Lowest
    protected double rateOfReturn;
    //Volume (y value).
    protected double volume;
    
    protected double deltaClose;
    protected double normalizedDeltaClose;
    protected double normalizedVolume;
    private double jIndex;
    
    private int    pGroup_number = 0;
    private int    flag_anomaly=0;
    
    public StockPoint()
    {
        rateOfReturn = 0.0;
        pGroup_number = 0;
        flag_anomaly = 0;
    }
    
    public StockPoint (String listedDate, String stockSymbol, double priceOpen, 
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
        
        this.setListedDate(listedDate);
        this.stockSymbol = stockSymbol;
        this.priceOpen = priceOpen;
        this.priceClose = priceClose;
        this.priceHigh = priceHigh;
        this.priceLow = priceLow;
        this.rateOfReturn = priceHigh - priceLow;
        this.volume = volume;
        this.jIndex = 0.0;
    }
    
    public StockPoint (String listedDate, String stockSymbol, double rateOfReturn, 
            double volume)
    {
        /**
         * @param   strDate         the string date to be converted into a date-time format and saved.
         * @param   rateOfReturn    the rate of return pre-calculated.
         * @param   stockSymbol     the symbol of stock.
         * @param   volume          the volume of stock sold.
         */
        
        this.setListedDate(listedDate);
        this.stockSymbol = stockSymbol;
        this.rateOfReturn = rateOfReturn;
        this.volume = volume;
    }
    
    /**
     * this is for test,which just create random points
     * @param rateOfReturn
     * @param volume
     */
    public StockPoint (double rateOfReturn, double volume)
    {
    	 this.rateOfReturn = rateOfReturn;
         this.volume = volume;
    }
    
    public void calculateRateOfReturn()
    {
        this.rateOfReturn = priceHigh - priceLow;
        //System.out.println("asdas");
    }
    
    public double getDeltaClose()
    {
        /** 
         * @return the delta closing price of the stock.
         */
        return deltaClose;
    }
    
    public double getJIndex ()
    {
        /**
         * @return  the jaccard index of the stock that has been calculated;
         */
        return jIndex;
    }
    
    public double getNormalizedDeltaClose()
    {
        /** 
         * @return the normalized closing price of the stock.
         */
        return normalizedDeltaClose;
    }
    
    public double getNormalizedVolume()
    {
        /** 
         * @return the closing price of the stock.
         */
        return normalizedVolume;
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
    
    public double getRateOfReturn()
    {
        /** 
         * @return the volume of stock sold.
         */
        return rateOfReturn;
    }
    
    public String getStockSymbol()
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
        strDate = strDate.split(" ")[0]; // remove time

        /**
         * @param   strDate the string date to be converted into a date-time format and saved.
         * @return  a boolean that states if the operation was successful.
         */
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
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
    
    public void setDeltaClose(double deltaClose)
    {
        /** 
         * @param deltaClose the delta closing price of the stock.
         */
        this.deltaClose = deltaClose;
    }
    
    
    public void setJIndex (double jIndex)
    {
        /**
         * @param   jIndex  sets the Jaccard index
         */
        this.jIndex = jIndex;
    }
    
    
    public void setNormalizedDeltaClose(double normalizedDeltaClose)
    {
        /** 
         * @param normalizedDeltaClose the normalized delta closing price of the stock.
         */
        this.normalizedDeltaClose = normalizedDeltaClose;
    }
    
    public void setNormalizedVolume (double normalizedVolume)
    {
        /** 
         * @param normalizedVolume the normalized volume of trade of the stock.
         */
        this.normalizedVolume = normalizedVolume;
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
    
    public void setRateofReturn(double rateOfReturn)
    {
        /** 
         * @param priceOpen the opening price of the stock.
         */
        this.rateOfReturn = rateOfReturn;
    }
    
    public void setStockSymbol(String stockSymbol)
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
    
    public String toString ()
    {
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        return df.format(this.listedDate) + "," + this.stockSymbol + "," + this.priceOpen + 
                "," + this.priceClose + "," + this.priceLow + "," + this.priceHigh +
                "," + this.volume + "," + this.rateOfReturn;                
    }
    
    //added by Jason
    public int getpGroup_number()
	{
		return pGroup_number;
	}

	public void setpGroup_number(int pGroup_number)
	{
		this.pGroup_number = pGroup_number;
	}

	public int getFlag_anomaly()
	{
		return flag_anomaly;
	}

	public void setFlag_anomaly(int flag_anomaly)
	{
		this.flag_anomaly = flag_anomaly;
	}

	public void setRateOfReturn(double rateOfReturn)
	{
		this.rateOfReturn = rateOfReturn;
	}

	public Date getListedDate ()
    {
        return listedDate;
    }
    
    /**
	 * Calculates the distance between two points.
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double distance(StockPoint p1, StockPoint p2)
	{
		//double retValue = Math.sqrt(Math.pow((p2.getVolume() - p1.getVolume()), 2)
		//		+ Math.pow((p2.getRateOfReturn() - p1.getRateOfReturn()), 2));
		//BigDecimal bg = new BigDecimal(retValue).setScale(2, RoundingMode.UP);
		//return bg.doubleValue();
                
                return (double) Math.sqrt(Math.pow((p2.getVolume() - p1.getVolume()), 2) + Math.pow((p2.getRateOfReturn() - p1.getRateOfReturn()), 2));
	}

	/**
	 * Creates random StockPoint
	 * @param min
	 * @param max
	 * @return
	 */
	public static StockPoint createRandomPoint(int min, int max)
	{
		Random r = new Random();
		//double x = min + (max - min) * r.nextDouble();
		//double y = min + (max - min) * r.nextDouble();
		//BigDecimal bgx = new BigDecimal(x).setScale(2, RoundingMode.UP);
		//BigDecimal bgy = new BigDecimal(y).setScale(2, RoundingMode.UP);
		//return new StockPoint(bgx.doubleValue(), bgy.doubleValue());
                return new StockPoint(min + (max - min) * r.nextDouble(), min + (max - min) * r.nextDouble());
	}

	/**
	 * @param min
	 * @param max
	 * @param number how many records I want to create
	 * @return
	 */
	public static List<StockPoint> createRandomPoints(int min, int max,
			int number)
	{
		List<StockPoint> points = new ArrayList<StockPoint>(number);
		for (int i = 0; i < number; i++)
		{
			points.add(createRandomPoint(min, max));
		}
		return points;
	}

	
	public boolean sameStock(StockPoint arg)
	{
		if ( this.stockSymbol.equals(arg.stockSymbol))
		{
			return true;
		}
		
		return false;
	}
}
