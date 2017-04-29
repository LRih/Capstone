/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.utils;

import com.capstone.entities.Anomalies;
import com.capstone.entities.SearchStocks;
import com.capstone.entities.SearchItem;
import com.capstone.entities.StockPoint;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

/**
 *
 * @author Shadow
 */
public class SearchDataCSV extends SearchDataOutput {
    public SearchDataCSV ()
    {
        super();
    }
    
    public SearchDataCSV(File file)
    {
        super();
        this.file = file;
    }
    
    public void outputToFile (SearchStocks search, Anomalies anomalies)
    {
        /*
         * @param   search      set of searched news articles for stocks.
         * @param   anomalies   set of anomalies detected.
        */
        outputToFile(search, anomalies, 'd');
    }
    
    public void outputToFile (SearchStocks search, Anomalies anomalies, char sortType)
    {
        // Output to file(s)
        /**
         * @param   search      set of searched news articles for stocks.
         * @param   anomalies   set of anomalies detected.
         * @param   sortType    the type of output required for the csv.
         */
        
        /**
         * Switchback notes:
         * t - output by anomaly detection type.
         * d - output by date.
         * s - output by stock.
         */
        
        
        HashMap <String, List<StockPoint>> _sortedAnomalies = new HashMap<String, List<StockPoint>>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        switch (sortType)
        {
            case 'd':
                // Resorts the data in date format, then proceeds with the writing of data.
                for ( Anomalies.Type key : anomalies.getKeySet())
                {
                    //_stocks = new HashMap<String, List<StockPoint>>();
                    for (StockPoint stock : anomalies.getStockList(key))
                    {
                        /*if (!_sortedAnomalies.containsKey(stock.getStockSymbol()))
                            _sortedAnomalies.put(stock.getStockSymbol(), new ArrayList<StockPoint>());

                            _sortedAnomalies.get(stock.getStockSymbol()).add(s);*/
                        String stockDate = df.format(stock.getListedDate());
                        if (!_sortedAnomalies.containsKey(stockDate))
                            
                            _sortedAnomalies.put(stockDate, new ArrayList<StockPoint>());

                        _sortedAnomalies.get(stockDate).add(stock);
                    }
                }

                
                writeOutput (search, _sortedAnomalies);
            break;

            // Output type: Anomaly detection method, also the default output type.
            case 't':
            default:
                
            for ( Anomalies.Type key : anomalies.getKeySet())
            {
                //_stocks = new HashMap<String, List<StockPoint>>();
                for (StockPoint stock : anomalies.getStockList(key))
                {
                    /*if (!_sortedAnomalies.containsKey(stock.getStockSymbol()))
                        _sortedAnomalies.put(stock.getStockSymbol(), new ArrayList<StockPoint>());

                        _sortedAnomalies.get(stock.getStockSymbol()).add(s);*/
                    String stockDate = df.format(stock.getListedDate());
                    if (!_sortedAnomalies.containsKey(stockDate))

                        _sortedAnomalies.put(stockDate, new ArrayList<StockPoint>());

                    _sortedAnomalies.get(stockDate).add(stock);
                }
            }    
            
        }
                writeOutput (search, _sortedAnomalies);
        
        
    }
    
    private void writeOutput (SearchStocks search, HashMap <String, List<StockPoint>> sortedAnomalies)
    {
        
        List<SearchItem> _searchResults; 
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        BufferedWriter bufferedWriter = null;
        
        try
        {
           
            bufferedWriter = new BufferedWriter(new FileWriter(file)); 
            
            bufferedWriter.write("date,symbol,priceOpen,priceClose,priceLow,priceHigh,volume," + 
                    "rateOfReturn,anomalytype,articleShortName,articleLongName,articleDate,newsSource,URL");
            bufferedWriter.newLine();
            
            
            for ( String key : sortedAnomalies.keySet()) 
            {
                // Adds all stock that are relevant to stock class.
                for (StockPoint stock : sortedAnomalies.get(key))
                { 
                    _searchResults = search.getAnomaliesSearchResults(stock.getStockSymbol(), stock.getListedDate());

                    if(_searchResults.isEmpty())
                    {
                        // Write the anomaly only and format for search results.
                        bufferedWriter.write(stock.toString() + "," + key + ",,,,,");

                        bufferedWriter.newLine();
                    }
                    else
                    {
                        // Writes anomaly stock and search data
                        for (SearchItem item : _searchResults)
                        {

                            bufferedWriter.write(stock.toString() + "," + item.getStockSymbol() + ",\"");
                            bufferedWriter.write(item.getShortName() + "\",\"" + item.getLongName() +
                                    "\"," + df.format(item.getDate()) + ",\"" + item.getNewsSource() + 
                                    "\",\"" + item.getURL() + "\"");
                            bufferedWriter.newLine();
                        }
                    }
                    //bufferedWriter.write(anomalies.get(key).get(i).toString() + "," + key);
                }
            }

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (bufferedWriter != null)
                {
                    bufferedWriter.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
