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
        StockPoint testStockPoint1 = new StockPoint();
        Stock testStock = new Stock();
        
        testStock.addStock(new StockPoint());
        testStock.getStockElement(0).setPriceHigh(1.2);
        
        // Load data into system
        testStockPoint1.setPriceHigh(1.1);
        
        System.out.println(testStock.getStockElement(0).getPriceHigh());
        System.out.println(testStockPoint1.getPriceHigh());
        // Run analysis tools in sequence
        
        // Output to file(s)
    }
    
}
