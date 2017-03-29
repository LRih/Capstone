/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * License: Code developed in this project has all right reserved.
 * Purpose: This code is developed to look for time-base anomalies in stock data.
 */
package timeseries;

import java.util.LinkedList;

import timeseries.entities.Stock;
import timeseries.entities.StockPoint;

import timeseries.utils.DataImport;

import timeseries.algothims.timeAlgorithm;
import timeseries.algothims.timeStdDev;
import timeseries.algothims.timeARMA;

/**
 *
 * @author Daniel Johnson, s3395210
 * 
 */
public class MainClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Initialize the class
        DataImport dataImport = new DataImport();
        Stock testStock = new Stock();
        LinkedList<StockPoint> anomalies;
        
        // Load data into system
        testStock = dataImport.importData("d:\\testdata1.csv");
        
        // Setups and runs algorithms
        System.out.println("Running Algorithm: Standard Deviation");
        timeStdDev algorithmStdDev = new timeStdDev(testStock);
        anomalies = algorithmStdDev.findAnomalies();
        algorithmStdDev.outputToFile("d:\\output.time.StdDev.csv");
        System.out.println("Completed Algorithm: Standard Deviation");
        
        System.out.println("Running Algorithm: ARMA");
        timeARMA algorithmARMA = new timeARMA(testStock);
        algorithmARMA.setPValue(2);
        algorithmARMA.setQValue(3);
        
        anomalies = algorithmARMA.findAnomalies();
        algorithmARMA.outputToFile("d:\\output.time.ARMA.csv");
        algorithmARMA.outputToDebugFile("d:\\output.time.ARMA.debug.csv");
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
