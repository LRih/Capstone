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
        // Pre-Process the data to normalize it.
        new DataPreprocessService().preprocess();
        
        
        // Initialize the class
        DataImport dataImport = new DataImport();
        
        Stock testStock = new Stock();
        LinkedList<StockPoint> anomalies;
        
        // Load data into system
        testStock = dataImport.importNormalizedData("normalized.csv");
        
        // Setups and runs algorithms
        System.out.printf("\n\n=========================\n\n");
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
        System.out.println("Completed Algorithm: ARMA");
        
        
        // For Debug of Data Only
        //algorithmStdDev.outputToDebugFile("d:\\debug.csv");
        
        
        /*for (int i = 0; i < testStock.getStockElements(); i++)
        {
            System.out.println(testStock.getStockElement(i).getListedDate() + " " + 
                    testStock.getStockElement(i).getPriceHigh());
        }*/
        // Run analysis tools in sequence
        
        // Output to file(s)
    }
    
}
