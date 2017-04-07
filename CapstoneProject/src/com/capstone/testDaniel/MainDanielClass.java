/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * License: Code developed in this project has all right reserved.
 * Purpose: This code is developed to look for time-base anomalies in stock data.
 */
package com.capstone.testDaniel;

import java.util.LinkedList;

import com.capstone.entities.Stock;
import com.capstone.entities.StockPoint;

import com.capstone.utils.DataImport;

import com.capstone.algorithms.timeAlgorithm;
import com.capstone.algorithms.timeStdDev;
import com.capstone.algorithms.timeARMA;
import com.capstone.dataService.DataPreprocessService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Daniel Johnson, s3395210
 * 
 */
public class MainDanielClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Map<String, List<StockPoint>> _stocks;
        Map<String, List<StockPoint>> _anomalies;
        
        // Pre-Process the data to normalize it.
        new DataPreprocessService().preprocess();
        
        // Post-Process it so can be used directly.
        _stocks = new DataPreprocessService().processPost();
        
        // Initialize the class
        DataImport dataImport = new DataImport();
        
        // StockPoints of anomalies.
        // Needs a re-work to allow different stock items. ********************
        Stock testStock = new Stock();
        LinkedList<StockPoint> anomalies;
        _anomalies = new HashMap<String, List<StockPoint>>();
        
        _anomalies.put("STDDEV", new LinkedList<StockPoint>());
        _anomalies.put("ARMA", new LinkedList<StockPoint>());
        
        // Load data into system
        testStock = dataImport.importNormalizedData("normalized.csv");
        
        
        /**
         * Runs each stock in a bubble for time-series detection.
         */
        for ( String key : _stocks.keySet() ) 
        {
            // Debug Keys
            System.out.println( key );
            
            // Creates a new stock unit for each time-series
            Stock unitStock = new Stock();
            
            // Adds all stock that are relevant to stock class.
            for (int i = 0; i < _stocks.get(key).size(); i++)
            {
                unitStock.addStock(_stocks.get(key).get(i));
            }
            
            // Runs each time-series algorithm to find anomalies.
            //. Time Series
            System.out.printf("\n\n=========================\n");
            System.out.println("Stock ID: " + key);
            System.out.printf("=========================\n\n");
            
            timeStdDev algorithmStdDev = new timeStdDev(unitStock);
            anomalies = algorithmStdDev.findAnomalies();
            for (StockPoint stockPoint : anomalies)
            {
                _anomalies.get("STDDEV").add(stockPoint);
            }
            System.out.println("Running Algorithm: Standard Deviation");
            //algorithmStdDev.outputToFile("output." + key + ".time.StdDev.csv");
            //algorithmStdDev.outputToDebugFile("output." + key + ".time.StdDev.debug.csv");
            System.out.println("Completed Algorithm: Standard Deviation");
        
            System.out.println("Running Algorithm: ARMA");
            timeARMA algorithmARMA = new timeARMA(unitStock);
            algorithmARMA.setPValue(2);
            algorithmARMA.setQValue(3);

            anomalies = algorithmARMA.findAnomalies();
            for (StockPoint stockPoint : anomalies)
            {
                _anomalies.get("ARMA").add(stockPoint);
            }
            //algorithmARMA.outputToFile("output." + key + ".time.ARMA.csv");
            //algorithmARMA.outputToDebugFile("output." + key + ".time.ARMA.debug.csv");
            System.out.println("Completed Algorithm: ARMA");
        }
        
        
        
        /**
         * Runs the test stock values being derived directly from standard stock.
         */
        // Setups and runs algorithms
        /*System.out.printf("\n\n=========================\n\n");
        System.out.println("Running Algorithm: Standard Deviation");
        timeStdDev algorithmStdDev = new timeStdDev(testStock);
        anomalies = algorithmStdDev.findAnomalies();
        algorithmStdDev.outputToFile("output.time.StdDev.csv");
        algorithmStdDev.outputToDebugFile("output.time.StdDev.debug.csv");
        System.out.println("Completed Algorithm: Standard Deviation");
        
        System.out.println("Running Algorithm: ARMA");
        timeARMA algorithmARMA = new timeARMA(testStock);
        algorithmARMA.setPValue(2);
        algorithmARMA.setQValue(3);
        
        anomalies = algorithmARMA.findAnomalies();
        algorithmARMA.outputToFile("output.time.ARMA.csv");
        algorithmARMA.outputToDebugFile("output.time.ARMA.debug.csv");
        System.out.println("Completed Algorithm: ARMA");*/
        
        
        // For Debug of Data Only
        //algorithmStdDev.outputToDebugFile("d:\\debug.csv");
        
        
        /*for (int i = 0; i < testStock.getStockElements(); i++)
        {
            System.out.println(testStock.getStockElement(i).getListedDate() + " " + 
                    testStock.getStockElement(i).getPriceHigh());
        }*/
        // Run analysis tools in sequence
        
        // Output to file(s)
        /**
         * @param   filename    filename to output anomalies to.
         */
        BufferedWriter bufferedWriter = null;
        String filename = "anomalies.csv";
        
        try
        {
           
            bufferedWriter = new BufferedWriter(new FileWriter(new File(filename))); 
            
            bufferedWriter.write("date,symbol,priceOpen,priceClose,priceLow,priceHigh,volume,rateOfReturn,anomalytype");
            bufferedWriter.newLine();
            
            for ( String key : _anomalies.keySet() ) 
            {
                // Adds all stock that are relevant to stock class.
                for (int i = 0; i < _anomalies.get(key).size(); i++)
                {
                    
                    bufferedWriter.write(_anomalies.get(key).get(i).toString() + "," + key);
                    bufferedWriter.newLine();
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
