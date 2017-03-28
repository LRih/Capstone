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
        StockPoint testStock = new StockPoint();
        
        // Load data into system
        testStock.setPriceHigh(1.1);
        
        System.out.println(testStock.getPriceHigh());
        // Run analysis tools in sequence
        
        // Output to file(s)
    }
    
}
