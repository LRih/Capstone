/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;

/**
 *
 * @author Shadow
 */
public class SearchStocks {
    Map<String, List<SearchItem>> _searchItems;
    int searchDaysBack;
    int searchDaysForward;
    
    public SearchStocks ()
    {
        _searchItems = new HashMap<String, List<SearchItem>>();
        this.searchDaysBack = 2;
        this.searchDaysForward = 2;
    }
    
    public void addSearchItemsList (List<SearchItem> data)
    {
        /**
         * @param   data    is the input data from a read of a file or a set of data.
         */
        for (SearchItem s : data)
        {
            if (!_searchItems.containsKey(s.getStockSymbol()))
                _searchItems.put(s.getStockSymbol(), new ArrayList<SearchItem>());

            _searchItems.get(s.getStockSymbol()).add(s);
        }
    }
    
    public void addSearchItemsList (SearchItem data)
    {
        /**
         * @param   data    is the input data from a read of a file or a set of data.
         */
        if (!_searchItems.containsKey(data.getStockSymbol()))
            _searchItems.put(data.getStockSymbol(), new ArrayList<SearchItem>());

        _searchItems.get(data.getStockSymbol()).add(data);
    }
    
    public List<SearchItem> getSearchItemList (String stockSymbol)
    {
        /**
         * @param   stockSymbol stock symbol to return results of.
         * @return              list of search results.
         */
        
        return _searchItems.get(stockSymbol);
    }
    
    public List<SearchItem> getAnomaliesSearchResults (String symbol, Date date)
    {
        ArrayList<SearchItem> _searchResults = new ArrayList<SearchItem>();
        
        for ( String key : _searchItems.keySet() ) 
        {
            // Adds all stock that are relevant to stock class.
            for (SearchItem item : _searchItems.get(key))
            //for (int i = 0; i < anomalies.get(key).size(); i++)
            {
                Date itemDate = item.getDate();
                if(itemDate.after(modifiedDate(itemDate, searchDaysBack - 1)) && 
                        itemDate.before(modifiedDate(itemDate, searchDaysForward + 1)))
                {
                    System.out.println(item.getLongName());
                }
            }

        }

        return _searchResults;
    }
    
    private Date modifiedDate (Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}
