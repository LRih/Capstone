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
        // Output to file(s)
        /**
         * @param   filename    filename to output anomalies to.
         */
        List<SearchItem> _searchResults; 
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        BufferedWriter bufferedWriter = null;
        
        try
        {
           
            bufferedWriter = new BufferedWriter(new FileWriter(file)); 
            
            bufferedWriter.write("date,symbol,priceOpen,priceClose,priceLow,priceHigh,volume," + 
                    "rateOfReturn,anomalytype,articleShortName,articleLongName,articleDate,newsSource,URL");
            bufferedWriter.newLine();
            
            for ( String key : anomalies.getKeySet()) 
            {
                // Adds all stock that are relevant to stock class.
                for (StockPoint stock : anomalies.getStockList(key))
                {
                    //List<StockPoint> list = anomalies.getStockList(key); 
                    //List<SearchItem> _searchResults; 
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
                            
                            bufferedWriter.write(stock.toString() + "," + key + ",\"");
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
