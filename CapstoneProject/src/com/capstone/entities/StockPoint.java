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
        return this.listedDate + "," + this.stockSymbol + "," + this.priceOpen + 
                "," + this.priceClose + "," + this.priceLow + "," + this.priceHigh +
                "," + this.volume;                
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
		double retValue = Math.sqrt(Math.pow((p2.getVolume() - p1.getVolume()), 2)
				+ Math.pow((p2.getRateOfReturn() - p1.getRateOfReturn()), 2));
		BigDecimal bg = new BigDecimal(retValue).setScale(2, RoundingMode.UP);
		return bg.doubleValue();
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
		double x = min + (max - min) * r.nextDouble();
		double y = min + (max - min) * r.nextDouble();
		BigDecimal bgx = new BigDecimal(x).setScale(2, RoundingMode.UP);
		BigDecimal bgy = new BigDecimal(y).setScale(2, RoundingMode.UP);
		return new StockPoint(bgx.doubleValue(), bgy.doubleValue());
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

}
