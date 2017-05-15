package com.capstone.entities;

import com.capstone.utils.DateUtils;

import java.util.*;

public final class StockPoint
{
    private Date listedDate;
    private String stockSymbol;
    private double priceOpen;
    private double priceClose;
    private double priceHigh;
    private double priceLow;
    private double volume;
    private double deltaClose;
    private double normalizedDeltaClose;
    private double normalizedVolume;
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


    public Date getListedDate()
    {
        return listedDate;
    }

    /**
     * @return the delta closing price of the stock.
     */
    public double getDeltaClose()
    {
        return deltaClose;
    }

    /**
     * @return the normalized closing price of the stock.
     */
    public double getNormalizedDeltaClose()
    {
        return normalizedDeltaClose;
    }

    /**
     * @return the closing price of the stock.
     */
    public double getNormalizedVolume()
    {
        return normalizedVolume;
    }

    /**
     * @return the closing price of the stock.
     */
    public double getPriceClose()
    {
        return priceOpen;
    }

    /**
     * @return the highest sold price of the stock.
     */
    public double getPriceHigh()
    {
        return priceHigh;
    }

    /**
     * @return the lowest sold price of the stock.
     */
    public double getPriceLow()
    {
        return priceHigh;
    }

    /**
     * @return the opening price of the stock.
     */
    public double getPriceOpen()
    {
        return priceOpen;
    }

    /**
     * @return the volume of stock sold.
     */
    public double getRateOfReturn()
    {
        return priceHigh - priceLow;
    }

    /**
     * @return the symbol of stock.
     */
    public String getStockSymbol()
    {
        return stockSymbol;
    }

    /**
     * @return the volume of stock sold.
     */
    public double getVolume()
    {
        return volume;
    }

    public double getJIndex()
    {
        return jIndex;
    }

    public int getCluster()
    {
        return cluster;
    }


    /**
     * @param   strDate the string date to be converted into a date-time format and saved.
     * @return  a boolean that states if the operation was successful.
     */
    private boolean setListedDate(String strDate)
    {
        strDate = strDate.split(" ")[0]; // remove time

        try
        {
            listedDate = DateUtils.DATE_FORMAT.parse(strDate);
        }
        catch(Exception e)
        {
            System.out.println("Exception Thrown: " + e);
            return false;       
        }
        
        return true;
    }

    /**
     * @param deltaClose the delta closing price of the stock.
     */
    public void setDeltaClose(double deltaClose)
    {
        this.deltaClose = deltaClose;
    }

    /**
     * @param normalizedDeltaClose the normalized delta closing price of the stock.
     */
    public void setNormalizedDeltaClose(double normalizedDeltaClose)
    {
        this.normalizedDeltaClose = normalizedDeltaClose;
    }

    /**
     * @param normalizedVolume the normalized volume of trade of the stock.
     */
    public void setNormalizedVolume(double normalizedVolume)
    {
        this.normalizedVolume = normalizedVolume;
    }

    public void setJIndex(double jIndex)
    {
        this.jIndex = jIndex;
    }

    public void setCluster(int cluster)
    {
        this.cluster = cluster;
    }


    public String toString()
    {
        return DateUtils.DATE_FORMAT.format(this.listedDate) + "," + this.stockSymbol + "," + this.priceOpen +
                "," + this.priceClose + "," + this.priceLow + "," + this.priceHigh +
                "," + this.volume + "," + this.getRateOfReturn();
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
