/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.entities;

import java.text.SimpleDateFormat;
import java.util.*;

public final class StockPoint
{
    protected Date   listedDate;
    protected String stockSymbol;
    protected double priceOpen;
    protected double priceClose;
    protected double priceHigh;
    protected double priceLow;
    protected double volume;
    protected double deltaClose;
    protected double normalizedDeltaClose;
    protected double normalizedVolume;
    private double jIndex = -1;

    private int cluster = 0;

    /**
     * @param   listedDate  the string date to be converted into a date-time format and saved.
     * @param   priceClose  the closing price of the stock.
     * @param   priceHigh   the highest sold price of the stock.
     * @param   priceLow    the lowest sold price of the stock.
     * @param   priceOpen   the opening price of the stock.
     * @param   stockSymbol the symbol of stock.
     * @param   volume      the volume of stock sold.
     */
    public StockPoint (String listedDate, String stockSymbol, double priceOpen, double priceClose, double priceHigh, double priceLow, double volume)
    {
        this.setListedDate(listedDate);
        this.stockSymbol = stockSymbol;
        this.priceOpen = priceOpen;
        this.priceClose = priceClose;
        this.priceHigh = priceHigh;
        this.priceLow = priceLow;
        this.volume = volume;
        this.jIndex = -1;
    }

    public double getX()
    {
        return getNormalizedDeltaClose();
    }
    public double getY()
    {
        return getNormalizedVolume();
    }


    public Date getListedDate ()
    {
        return listedDate;
    }

    public double getDeltaClose()
    {
        /** 
         * @return the delta closing price of the stock.
         */
        return deltaClose;
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
        return priceHigh - priceLow;
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

    public double getJIndex()
    {
        return jIndex;
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

    public void setJIndex(double jIndex)
    {
        this.jIndex = jIndex;
    }


    public String toString ()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        return df.format(this.listedDate) + "," + this.stockSymbol + "," + this.priceOpen + 
                "," + this.priceClose + "," + this.priceLow + "," + this.priceHigh +
                "," + this.volume + "," + this.getRateOfReturn();
    }
    
    public int getCluster()
	{
		return cluster;
	}

	public void setCluster(int cluster)
	{
		this.cluster = cluster;
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
		return Math.sqrt(Math.pow((p2.getX() - p1.getX()), 2) + Math.pow((p2.getY() - p1.getY()), 2));
	}


    public static final class JaccardIndexComparator implements Comparator<StockPoint>
    {
        public final int compare(StockPoint pt1, StockPoint pt2)
        {
            if (pt1.jIndex < pt2.jIndex) return -1;
            else if (pt1.jIndex > pt2.jIndex) return 1;
            return 0;
        }
    }
}
