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

/**
 *
 * @author Shadow
 */
public class SearchStocks {
    Map<String, List<SearchItem>> _searchItems;
    
    public SearchStocks ()
    {
        _searchItems = new HashMap<String, List<SearchItem>>();
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
}
