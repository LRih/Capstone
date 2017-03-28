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

import timeseries.algothims.timeStdDev;
import timeseries.algothims.timeAlgorithm;
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
        System.out.println("Elements: " + testStock.getStockElements());
        
        // Setups and runs algorithms
        timeStdDev algorithmStdDev = new timeStdDev(testStock);
        
        anomalies = algorithmStdDev.findAnomalies();
        algorithmStdDev.outputToFile("d:\\output.csv");
        algorithmStdDev.outputToDebugFile("d:\\output.csv");
        
        
        /*for (int i = 0; i < testStock.getStockElements(); i++)
        {
            System.out.println(testStock.getStockElement(i).getListedDate() + " " + 
                    testStock.getStockElement(i).getPriceHigh());
        }*/
        // Run analysis tools in sequence
        
        // Output to file(s)
    }
    
}
