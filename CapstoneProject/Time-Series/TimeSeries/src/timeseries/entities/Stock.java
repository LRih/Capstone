/*
 * Purpose: Stock data storage container.
 */
package timeseries.entities;

/* 
 * Package Imports 
 */
import java.util.Date;
import java.util.LinkedList;
import java.text.SimpleDateFormat;

import timeseries.entities.StockPoint;

/**
 *
 * @author daniel
 */
public class Stock {

    LinkedList<StockPoint> stockPoints;
    protected String stockSymbol;
            
    public Stock ()
    {
        stockPoints = new LinkedList<StockPoint>();
    }    
    
    public Stock (String stockSymbol)
    {
        stockPoints = new LinkedList<StockPoint>();
        this.stockSymbol = stockSymbol;
    } 
    
    public void setStockSymbol (String stockSymbol)
    {
        /**
         * @param   stockSymbol the symbol for this stock.
         */
        this.stockSymbol = stockSymbol;
    }
    
    public void addStock (StockPoint stockPoint)
    {
        /**
         * @param   stockpoint  adds the element provided to the linkedlist.
         */
        stockPoints.add(stockPoint);
    }
    
    public int getStockElements ()
    {
        /**
         * @return  amount of elements in stock.
         */
        return stockPoints.size();
    }
    
    public StockPoint getStockElement(int element)
    {
        /**
         * @param   element element Id of the stockPoints linkedlist.
         * @return          stock at element id that was provided.
         */
        return stockPoints.get(element);
    }
}
