/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * License: Code developed in this project has all right reserved.
 * Purpose: This code is developed to look for time-base anomalies in stock data.
 */
package timeseries;

import timeseries.entities.Stock;
import timeseries.entities.StockPoint;

import timeseries.utils.DataImport;
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
        StockPoint testStockPoint1 = new StockPoint();
        Stock testStock = new Stock();
        
        
        // Load data into system
        testStock = dataImport.importData("d:\\testdata1.csv");
        System.out.println("Elements: " + testStock.getStockElements());
        
        
        for (int i = 0; i < testStock.getStockElements(); i++)
        {
            System.out.println(testStock.getStockElement(i).getListedDate() + " " + 
                    testStock.getStockElement(i).getPriceHigh());
        }
        // Run analysis tools in sequence
        
        // Output to file(s)
    }
    
}
