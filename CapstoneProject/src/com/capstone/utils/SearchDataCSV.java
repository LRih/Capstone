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
        //oldOutputToFile(search, anomalies, 'd');
        outputToFile(search, anomalies, 'd');
    }
    
    public void outputToFile (SearchStocks search, Anomalies anomalies, char parameterSwitch)
    {
        /**
         * @param   search          set of searched news articles for stocks.
         * @param   anomalies       set of anomalies detected.
         * @param   parameterSwitch the type of output required for the csv.
         */
        
        /**
         * Switchback notes:
         * a - Article information outputted(implies -p in addition)
         * d - Debug Mode, all information outputted
         * p - Stock Price information outputted
         * s - Statistics outputted .
         */
        
        // Stock Date, Stock Symbol, <Anomaly Types>, Article Details
        AnomalySorted anomaliesSorted = new AnomalySorted();
        
        List<SearchItem> _searchResults; 
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        BufferedWriter bufferedWriter = null;
        
        // Statistics to gather
        int anomalyTypeHitsSize = anomalies.getKeySet().size();
        long anomalyTypeHits[] = new long[anomalyTypeHitsSize + 1];
        long anomalyArticles = 0;
        
        // Get list of anomaly Types
        for (Anomalies.Type key : anomalies.getKeySet())
        {
            anomaliesSorted.anomalyTypes.add(key.toString());
        }
        
        // Puts it into easy to use hashmaps
        for ( Anomalies.Type key : anomalies.getKeySet())
        {
            for (StockPoint stock : anomalies.getStockList(key))
            {
                anomaliesSorted.addAnomaly(stock, key.toString());
            }
        }
        
        
        try
        {
           
            bufferedWriter = new BufferedWriter(new FileWriter(file)); 
            bufferedWriter.write("date,symbol,");
            
             // Stock Details
            if(parameterSwitch == 'd' || parameterSwitch == 'a' || parameterSwitch == 'p')
            {
                bufferedWriter.write("priceOpen,priceClose,priceLow,priceHigh,volume,rateOfReturn,");
            }
            
            // For each type, make a new column
            for(String aTypes : anomaliesSorted.anomalyTypes)
                bufferedWriter.write(aTypes + ",");
            bufferedWriter.write("anomalyHits,");
            
            // Article Details if required
            if(parameterSwitch == 'd' || parameterSwitch == 'a')
            {
                bufferedWriter.write("articleShortName,articleLongName,articleDate,newsSource,URL");
            }
            bufferedWriter.newLine();
            
            // for each symbol and date combo output the anomalies and any articles.
            for (String anomalySymbol : anomaliesSorted.stockAnomalies.keySet())
            {
                for (Date anomalyDate : anomaliesSorted.stockAnomalies.get(anomalySymbol).keySet())
                {
                    StockPoint stockPoint = anomaliesSorted.stockPoints.get(anomalySymbol).get(anomalyDate);
                    System.out.println(stockPoint.toString());
                    ArrayList<String> anomalyTypes = anomaliesSorted.stockAnomalies.get(anomalySymbol).get(anomalyDate);
                    String toOutput;
                    
                    // Default identifiers (forced inclusions)
                    toOutput = anomalySymbol + "," + df.format(anomalyDate) + ",";
                    
                     // Stock Details
                    if(parameterSwitch == 'd' || parameterSwitch == 'a' || parameterSwitch == 'p')
                    {
                        toOutput = toOutput + stockPoint.getPriceOpen() + "," + 
                                stockPoint.getPriceClose() + "," + 
                                stockPoint.getPriceLow() + "," + 
                                stockPoint.getPriceHigh() + "," + 
                                stockPoint.getVolume() + "," +
                                stockPoint.getRateOfReturn() + ",";
                    }
                    
                    int aHits = 0;
                    for(String knownAType : anomaliesSorted.anomalyTypes)
                    {
                        if(anomalyTypes.contains(knownAType))
                        {
                            toOutput = toOutput + "true,";
                            aHits++;
                        }
                        else
                        {
                            toOutput = toOutput + "false,";
                        }
                        
                    }
                    // Anomaly type hit counter.
                    toOutput = toOutput + aHits + ",";
                    anomalyTypeHits[aHits] = anomalyTypeHits[aHits] + 1;
                    
                    // retrived search results
                    
                    _searchResults = search.getAnomaliesSearchResults(anomalySymbol, anomalyDate);
                    
                    if(parameterSwitch == 'd' || parameterSwitch == 'a')
                    {
                        
                        if(_searchResults.isEmpty())
                        {
                            // Write the anomaly only and format for search results.
                            bufferedWriter.write(toOutput + ",,,,");
                            bufferedWriter.newLine();
                        }
                        else
                        {
                            anomalyArticles++;
                            // Writes anomaly stock and search data
                            for (SearchItem item : _searchResults)
                            {

                                bufferedWriter.write(toOutput + "\"");
                                bufferedWriter.write(item.getShortName() + "\",\"" + item.getLongName() +
                                        "\"," + df.format(item.getDate()) + ",\"" + item.getNewsSource() + 
                                        "\",\"" + item.getURL() + "\"");
                                bufferedWriter.newLine();
                            }
                        }
                    }
                    else
                    {
                        bufferedWriter.write(toOutput);
                        bufferedWriter.newLine();
                    }
                    
                }
            }
            
            for (long hits : anomalyTypeHits)
            {
                System.out.println(hits);
            }
            System.out.println("Articles: " + anomalyArticles);

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
    
    
    
    private class AnomalySorted
    {
        public ArrayList<String> anomalyTypes;
        public HashMap<String, HashMap<Date, StockPoint>> stockPoints;
        public HashMap<String, HashMap<Date, ArrayList<String>>> stockAnomalies;
        
        public AnomalySorted()
        {
            anomalyTypes = new ArrayList<String>();
            stockPoints = new HashMap<>();
            stockAnomalies = new HashMap<>();
                    
        }
        
        public void addAnomaly (StockPoint stock, String type)
        {
            if(!stockPoints.containsKey(stock.getStockSymbol()))
            {
                stockPoints.put(stock.getStockSymbol(), new HashMap<Date, StockPoint>());
                stockAnomalies.put(stock.getStockSymbol(), new HashMap<Date, ArrayList<String>>());
            }
            
            if(!stockPoints.get(stock.getStockSymbol()).containsKey(stock.getListedDate()))
            {
                stockPoints.get(stock.getStockSymbol()).put(stock.getListedDate(), stock);
                stockAnomalies.get(stock.getStockSymbol()).put(stock.getListedDate(), new ArrayList<String>());
            }
            
            if(!stockAnomalies.get(stock.getStockSymbol()).get(stock.getListedDate()).contains(type))
            {
                stockAnomalies.get(stock.getStockSymbol()).get(stock.getListedDate()).add(type);
            }
        }
    }
}

