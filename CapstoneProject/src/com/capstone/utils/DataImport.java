/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.utils;

import java.util.LinkedList;
import java.lang.Double;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import com.capstone.entities.Stock;
import com.capstone.entities.StockPoint;

/**
 *
 * @author daniel
 */
public class DataImport {
    
    public DataImport ()
    {
        
    }
    
    public Stock importData (String fileLocation)
    {
        // http://crunchify.com/how-to-read-convert-csv-comma-separated-values-file-to-arraylist-in-java-using-split-operation/
        //LinkedList<Stock> stock = new LinkedList<Stock>();
        Stock stock = new Stock();
        
        BufferedReader bufferedReader = null;
        String readLine = null;
        
        try 
        {
            bufferedReader = new BufferedReader(new FileReader(new File(fileLocation)));
            //bufferedReader = new BufferedReader(fileLocation);
            // Firstline are headers
            bufferedReader.readLine();
            
            while ((readLine = bufferedReader.readLine()) != null)
            {
                String[] splitLine = readLine.split("\\s*,\\s*");
                
                /*for (int i = 0; i < splitLine.length; i++)
                {
                    
                    /*if(!(splitData[i] == null) || !(splitData[i].length()) == 0)) 
                    {
                        
                    }
                } */
                
                // String listedDate, String stockSymbol, double priceOpen, double priceClose, double priceHigh, double priceLow, long volume)
                StockPoint stockPoint = new StockPoint();
                
                // Sets variables
                stockPoint.setListedDate(splitLine[0]);
                stockPoint.setStockSymbol(splitLine[1]);

                stockPoint.setPriceOpen(Double.parseDouble(splitLine[2]));
                stockPoint.setPriceClose(Double.parseDouble(splitLine[3]));
                stockPoint.setPriceLow(Double.parseDouble(splitLine[4]));
                stockPoint.setPriceHigh(Double.parseDouble(splitLine[5]));
                stockPoint.setVolume(Double.parseDouble(splitLine[6]));
                
                stockPoint.calculateRateOfReturn();
                
                stock.addStock(stockPoint);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                if(bufferedReader != null) 
                {
                    bufferedReader.close();
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
                
        return stock;
    }
}
