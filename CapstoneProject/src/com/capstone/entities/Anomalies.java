/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.entities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shadow
 */
public class Anomalies {
    private Map<String, List<StockPoint>> anomalies;
    
    public Anomalies ()
    {
        /**
         *  Creates the HashMap used for the class.
         */
        anomalies = new HashMap<String, List<StockPoint>>();
    }
    
    public void addAnomalyType (String type)
    {
        /**
         * @param   type    Name of the key for the type of anomaly being used.
         */
        anomalies.put(type, new LinkedList<StockPoint>());
    }
    
    public void addAnomaly(String type, StockPoint stockPoint)
    {
        /**
         * @param   type        Name of the key for the type of anomaly being used.
         * @param   stockPoint  Data point that is an anomaly.
         */
        anomalies.get(type).add(stockPoint);
    }
    
    public void outputToFile(String filename)
    {
        // Output to file(s)
        /**
         * @param   filename    filename to output anomalies to.
         */
        BufferedWriter bufferedWriter = null;
        
        try
        {
           
            bufferedWriter = new BufferedWriter(new FileWriter(new File(filename))); 
            
            bufferedWriter.write("date,symbol,priceOpen,priceClose,priceLow,priceHigh,volume,rateOfReturn,anomalytype");
            bufferedWriter.newLine();
            
            for ( String key : anomalies.keySet() ) 
            {
                // Adds all stock that are relevant to stock class.
                for (StockPoint stockPoint : anomalies.get(key))
                //for (int i = 0; i < anomalies.get(key).size(); i++)
                {
                    
                    //bufferedWriter.write(anomalies.get(key).get(i).toString() + "," + key);
                    bufferedWriter.write(stockPoint.toString() + "," + key);
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
