/*
 * Purpose: Stock data storage container.
 */
package com.capstone.entities;

import java.util.LinkedList;

import java.util.List;

public final class Stock
{
    private List<StockPoint> stockPoints;
    protected String stockSymbol;

    public Stock()
    {
        stockPoints = new LinkedList<StockPoint>();
    }

    public Stock(String stockSymbol)
    {
        stockPoints = new LinkedList<StockPoint>();
        this.stockSymbol = stockSymbol;
    }

    /**
     * @param   stockSymbol the symbol for this stock.
     */
    public void setStockSymbol(String stockSymbol)
    {
        this.stockSymbol = stockSymbol;
    }

    /**
     * @param   stockPoint  adds the element provided to the linkedlist.
     */
    public void addStock(StockPoint stockPoint)
    {
        stockPoints.add(stockPoint);
    }

    /**
     * @return amount of elements in stock.
     */
    public int getStockElements()
    {
        return stockPoints.size();
    }

    /**
     * @param   element element Id of the stockPoints linkedlist.
     * @return stock at element id that was provided.
     */
    public StockPoint getStockElement(int element)
    {
        return stockPoints.get(element);
    }
}
