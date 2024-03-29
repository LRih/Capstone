package com.capstone.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;

public class SearchStocks
{
    Map<String, List<SearchItem>> _searchItems;
    int searchDaysBack;
    int searchDaysForward;

    public SearchStocks()
    {
        _searchItems = new HashMap<String, List<SearchItem>>();
        this.searchDaysBack = 2;
        this.searchDaysForward = 2;
    }

    /**
     * @param   data    is the input data from a read of a file or a set of data.
     */
    public void addSearchItemsList(List<SearchItem> data)
    {
        for (SearchItem s : data)
        {
            if (!_searchItems.containsKey(s.getStockSymbol()))
                _searchItems.put(s.getStockSymbol(), new ArrayList<SearchItem>());

            _searchItems.get(s.getStockSymbol()).add(s);
        }
    }

    /**
     * @param   data    is the input data from a read of a file or a set of data.
     */
    public void addSearchItemsList(SearchItem data)
    {
        if (!_searchItems.containsKey(data.getStockSymbol()))
            _searchItems.put(data.getStockSymbol(), new ArrayList<SearchItem>());

        _searchItems.get(data.getStockSymbol()).add(data);
    }

    /**
     * @param   stockSymbol stock symbol to return results of.
     * @return list of search results.
     */
    public List<SearchItem> getSearchItemList(String stockSymbol)
    {
        return _searchItems.get(stockSymbol);
    }

    /**
     * @param   symbol  the symbol to get the search results for.
     * @param   date    the date to specify the search results for.
     * @return an arraylist of search items within the database.
     */
    public List<SearchItem> getAnomaliesSearchResults(String symbol, Date date)
    {
        ArrayList<SearchItem> _searchResults = new ArrayList<SearchItem>();

        // Adds all stock that are relevant to stock class.
        if (_searchItems.containsKey(symbol))
        {
            for (SearchItem item : _searchItems.get(symbol))
            {

                Date itemDate = item.getDate();
                if (itemDate.after(modifiedDate(date, searchDaysBack - 1)) &&
                    itemDate.before(modifiedDate(date, searchDaysForward + 1)))
                {

                    _searchResults.add(item);
                }
            }
        }

        return _searchResults;
    }

    private Date modifiedDate(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}
