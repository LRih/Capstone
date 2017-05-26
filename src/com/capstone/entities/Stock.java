package com.capstone.entities;

import java.util.LinkedList;

import java.util.List;

public final class Stock
{
    private List<StockPoint> _stockPoints;
    private String _stockSymbol;

    public Stock()
    {
        _stockPoints = new LinkedList<StockPoint>();
    }

    public Stock(String stockSymbol)
    {
        _stockPoints = new LinkedList<StockPoint>();
        _stockSymbol = stockSymbol;
    }

    /**
     * @param   stockSymbol the symbol for this stock.
     */
    public void setStockSymbol(String stockSymbol)
    {
        _stockSymbol = stockSymbol;
    }

    /**
     * @param   stockPoint  adds the element provided to the linkedlist.
     */
    public void addStock(StockPoint stockPoint)
    {
        _stockPoints.add(stockPoint);
    }

    /**
     * @return amount of elements in stock.
     */
    public int getStockElements()
    {
        return _stockPoints.size();
    }

    /**
     * @param   element element Id of the stockPoints linkedlist.
     * @return stock at element id that was provided.
     */
    public StockPoint getStockElement(int element)
    {
        return _stockPoints.get(element);
    }
}
