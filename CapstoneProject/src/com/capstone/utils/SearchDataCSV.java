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
import com.capstone.entities.Stock;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Shadow
 */
public class SearchDataCSV extends SearchDataOutput {
    protected Map<String, List<StockPoint>> _stocks;
    protected File statsFile;
    
    public SearchDataCSV ()
    {
        super();
        this.statsFile = new File("stats.txt");
    }
    
    public SearchDataCSV(File file)
    {
        super();
        this.file = file;
        this.statsFile = new File("stats.txt");
    }
    
    public SearchDataCSV(File file, Map<String, List<StockPoint>> stocks)
    {
        super();
        this.file = file;
        this._stocks = stocks;
        this.statsFile = new File("stats.txt");
    }
    
    public void setStatsFile(File file)
    {
        this.statsFile = file;
    }
    
    public void outputToFile (SearchStocks search, Anomalies anomalies)
    {
        /*
         * @param   search      set of searched news articles for stocks.
         * @param   anomalies   set of anomalies detected.
        */
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
        long anomalyTypeHits[] = new long[anomalyTypeHitsSize + 2];
        long anomalyTypesCount[] = new long[anomalyTypeHitsSize + 2];
        long anomalyTypesSearches[] = new long[anomalyTypeHitsSize + 2];
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
                bufferedWriter.write("priceOpen,priceClose,priceLow,priceHigh,volume,rateOfReturn,jaccard,");
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
                                stockPoint.getRateOfReturn() + "," +
                                stockPoint.getJIndex() + ",";
                    }
                    
                    int aHits = 0;
                    if(anomalyTypes.contains("Jaccard") == true && anomalyTypes.size() == 1)
                    {
                        anomalyTypesCount[anomalyTypeHitsSize + 1]++;
                    }
                    for(String knownAType : anomaliesSorted.anomalyTypes)
                    {
                        
                        
                        if(anomalyTypes.contains(knownAType))
                        {
                            toOutput = toOutput + "true,";
                            anomalyTypesCount[anomaliesSorted.anomalyTypes.indexOf(knownAType)]++;
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
                            
                            // Article Counter
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
                            
                            if(anomalyTypes.contains("Jaccard") == true && anomalyTypes.size() == 1)
                            {
                                anomalyTypesSearches[anomalyTypeHitsSize + 1]++;
                            }
                            
                            //Adds search results counters
                            for(String knownAType : anomaliesSorted.anomalyTypes)
                            {
                                if(anomalyTypes.contains(knownAType))
                                    anomalyTypesSearches[anomaliesSorted.anomalyTypes.indexOf(knownAType)]++;
                            }
                        }
                    }
                    else
                    {
                        // This gathers stats only but no output
                        if (parameterSwitch == 's')
                        {
                            if(_searchResults.isEmpty())
                            {

                            }
                            else
                            {
                                anomalyArticles++;
                            }
                        }
                        bufferedWriter.write(toOutput);
                        bufferedWriter.newLine();
                    }
                    
                }
            }
            bufferedWriter.close();
            
            
            // Data Statistics output
            if (parameterSwitch == 's' || parameterSwitch == 'd')
            {
                bufferedWriter = new BufferedWriter(new FileWriter(statsFile)); 
                /**
                 * This section outputs the stats from the run into a seperate file. 
                 * 
                 */
                
                
                // Stock Information
                long stockPointCount = 0;
                long stockSymbolCount = 0;
                long stockDateCount = 0;
                ArrayList<Date> stockDates = new ArrayList<Date>();
                
                for (String key : _stocks.keySet()) 
                {
                    for (StockPoint stocks : _stocks.get(key))
                    {
                        // Collects all known dates for the run.
                        if(!stockDates.contains(stocks.getListedDate()))
                        {
                            stockDates.add(stocks.getListedDate());
                            stockDateCount++;
                        }
                        
                        // Counts stock points
                        stockPointCount++;
                    }
                }
                
                // Outputs Stock Stats
                bufferedWriter.write("Stock Information:");
                bufferedWriter.newLine();
                bufferedWriter.write("Stock Points: " + stockPointCount);
                bufferedWriter.newLine();
                bufferedWriter.write("Stock Types: " + _stocks.size());
                bufferedWriter.newLine();
                bufferedWriter.write("Stock Dates: " + stockDateCount);
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                
                // Anomaly Data
                anomalyTypeHits[0] = stockPointCount;
                for (int hits = 1; hits <= anomalyTypeHitsSize; hits++)
                {
                    anomalyTypeHits[0] -= anomalyTypeHits[hits];
                }
                
                // Summary of amount of hits
                bufferedWriter.write("Anomaly Hits:");
                bufferedWriter.newLine();
                for (int hits = 0; hits <= anomalyTypeHitsSize; hits++)
                {
                    bufferedWriter.write("StockPoints with " + hits + " hit(s): " + anomalyTypeHits[hits]);
                    bufferedWriter.newLine();
                }
                
                bufferedWriter.write("Anomalies Total: " + (stockPointCount - anomalyTypeHits[0]));
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                
                
                for (String anomalyType : anomaliesSorted.anomalyTypes)
                {
                    
                    bufferedWriter.write("Anomaly Type - " + anomalyType);
                    
                    bufferedWriter.newLine();
                    bufferedWriter.write(" - Total Anomalies: " + 
                            anomalyTypesCount[anomaliesSorted.anomalyTypes.indexOf(anomalyType)]);
                    
                    bufferedWriter.newLine();
                    bufferedWriter.write(" - Anomalies with search result(s): " +
                            anomalyTypesSearches[anomaliesSorted.anomalyTypes.indexOf(anomalyType)]);
                    bufferedWriter.newLine();
                    bufferedWriter.write(" - Percent with search results(s): ");
                    long typeSearches = anomalyTypesSearches[anomaliesSorted.anomalyTypes.indexOf(anomalyType)];
                    long typeTotal = anomalyTypesCount[anomaliesSorted.anomalyTypes.indexOf(anomalyType)];
                    double searchPercentage = ( (double)typeSearches / (double)typeTotal * 100);
                    bufferedWriter.write(String.format("%.2f",searchPercentage) + "%");
                    bufferedWriter.newLine();
                }
                
                bufferedWriter.write("Anomaly Type - Jaccard Unique");
                
                // Unique Jaccards
                bufferedWriter.newLine();
                bufferedWriter.write(" - Total Anomalies: " + 
                        anomalyTypesCount[anomalyTypeHitsSize + 1]);

                bufferedWriter.newLine();
                bufferedWriter.write(" - Anomalies with search result(s): " +
                        anomalyTypesSearches[anomalyTypeHitsSize + 1]);
                bufferedWriter.newLine();
                bufferedWriter.write(" - Percent with search results(s): ");
                long typeSearches = anomalyTypesSearches[anomalyTypeHitsSize + 1];
                long typeTotal = anomalyTypesCount[anomalyTypeHitsSize + 1];
                double searchPercentage = ( (double)typeSearches / (double)typeTotal * 100);
                bufferedWriter.write(String.format("%.2f",searchPercentage) + "%");
                bufferedWriter.newLine();
                    
                    
                bufferedWriter.newLine();
                
                // Summary Data
                bufferedWriter.write("Dates with Anomalies: " + anomaliesSorted.anomalyDates.size() );
                bufferedWriter.newLine();
                bufferedWriter.write("Stocks with Anomalies: " + anomaliesSorted.stockPoints.size() );
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                
                // Search Article Data
                bufferedWriter.write("Anomalies with Articles: " + anomalyArticles);
                
                bufferedWriter.newLine();
                bufferedWriter.close();
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
    
    
    
    private class AnomalySorted
    {
        public ArrayList<String> anomalyTypes;
        public ArrayList<Date> anomalyDates;
        public HashMap<String, HashMap<Date, StockPoint>> stockPoints;
        public HashMap<String, HashMap<Date, ArrayList<String>>> stockAnomalies;
        
        public AnomalySorted()
        {
            anomalyTypes = new ArrayList<String>();
            anomalyDates = new ArrayList<Date>();
            stockPoints = new HashMap<String, HashMap<Date, StockPoint>>();
            stockAnomalies = new HashMap<String, HashMap<Date, ArrayList<String>>>();
                    
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
                if(!anomalyDates.contains(stock.getListedDate()))
                    anomalyDates.add(stock.getListedDate());
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

