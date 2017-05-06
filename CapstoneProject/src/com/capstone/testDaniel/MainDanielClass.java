/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * License: Code developed in this project has all right reserved.
 * Purpose: This code is developed to look for time-base anomalies in stock data.
 */
package com.capstone.testDaniel;

import com.capstone.algorithms.JI;

import com.capstone.entities.Stock;
import com.capstone.entities.StockPoint;

import com.capstone.algorithms.timeStdDev;
import com.capstone.algorithms.timeARMA;
import com.capstone.dataService.SigmoidSigmoidPreprocessor;

import com.capstone.entities.Anomalies;
import com.capstone.entities.SearchStocks;

import com.capstone.utils.SearchDataCSV;
import com.capstone.utils.SearchDataImport;

import java.io.File;

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
        int k = 4;
        double stdDev = 2;

        // Pre-Process the data to normalize it.
        //new DataPreprocessService().preprocess();
        
        // Post-Process it so can be used directly.
        //LinearSigmoidPreprocessor preprocessor = new LinearSigmoidPreprocessor();
        SigmoidSigmoidPreprocessor preprocessor = new SigmoidSigmoidPreprocessor();
        preprocessor.preprocess();
        
        // load stocks by means... optimize later
        _stocks = preprocessor.nameMap();

        // Initialize the class
        //DataImport dataImport = new DataImport();
        
        // StockPoints of anomalies.
        // Needs a re-work to allow different stock items. ********************
        List<StockPoint> anomalies;
        
        Anomalies _anomalies = new Anomalies();
        _anomalies.addAnomalyType(Anomalies.Type.STDDEV);
        _anomalies.addAnomalyType(Anomalies.Type.ARMA);
        _anomalies.addAnomalyType(Anomalies.Type.Jaccard);
        
        
        // Jaccard Index
        JI jaccard = new JI(k);
        jaccard.calculate(preprocessor);
            //algorithmStdDev.outputToDebugFile("output." + key + ".time.StdDev.debug.csv");
        // Load data into system
        //testStock = dataImport.importNormalizedData("normalized.csv");
        
        
        /**
         * Runs each stock in a bubble for time-series detection.
         */
        for ( String key : _stocks.keySet() ) 
        {
            // Debug Keys
            //System.out.println( key );
            
            // Creates a new stock unit for each time-series
            Stock unitStock = new Stock();
            
            // Adds all stock that are relevant to stock class.
            for (int i = 0; i < _stocks.get(key).size(); i++)
            {
                unitStock.addStock(_stocks.get(key).get(i));
            }
            
            // Runs each time-series algorithm to find anomalies.
            //. Time Series
            //System.out.printf("\n\n=========================\n");
            //System.out.println("Stock ID: " + key);
            //System.out.printf("=========================\n\n");
            
            timeStdDev algorithmStdDev = new timeStdDev(unitStock);
            anomalies = algorithmStdDev.findAnomalies();
            for (StockPoint stockPoint : anomalies)
            {
                _anomalies.addAnomaly(Anomalies.Type.STDDEV, stockPoint);
            }
            //System.out.println("Running Algorithm: Standard Deviation");
            //algorithmStdDev.outputToFile("output." + key + ".time.StdDev.csv");
            //algorithmStdDev.outputToDebugFile("output." + key + ".time.StdDev.debug.csv");
            //System.out.println("Completed Algorithm: Standard Deviation");
        
            //System.out.println("Running Algorithm: ARMA");
            timeARMA algorithmARMA = new timeARMA(unitStock);
            algorithmARMA.setPValue(2);
            algorithmARMA.setQValue(3);

            anomalies = algorithmARMA.findAnomalies();
            for (StockPoint stockPoint : anomalies)
            {
                //_anomalies.get("STDDEV").add(stockPoint);
                _anomalies.addAnomaly(Anomalies.Type.ARMA, stockPoint);
            }
            //algorithmARMA.outputToFile("output." + key + ".time.ARMA.csv");
            //algorithmARMA.outputToDebugFile("output." + key + ".time.ARMA.debug.csv");
            //System.out.println("Completed Algorithm: ARMA");
            
            
        }
        
        // jaccard index anomalies
        anomalies = jaccard.getAnomalies(stdDev);
        for (StockPoint stockPoint : anomalies)
            _anomalies.addAnomaly(Anomalies.Type.Jaccard, stockPoint);

        
        
        // Outputs to file
        _anomalies.outputToFile("anomalies.csv");
        
        // Search Functionality
        SearchDataImport data = new SearchDataImport();
        data.setSearchFolder(".\\searchData");
        SearchStocks searchStocks = data.importData();
        
        
        SearchDataCSV outputCSV = new SearchDataCSV(new File("output.searches.csv"), _stocks);
        outputCSV.outputToFile(searchStocks, _anomalies);
        //http://search.ebscohost.com.ezproxy.lib.rmit.edu.au/login.aspx?direct=true&db=bwh&db=bth&db=ufh&bquery=Willis+Towers+Watson&type=1&site=ehost-live&scope=site
    }
    
    
}
