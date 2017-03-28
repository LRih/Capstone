/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseries.algothims;

import java.util.LinkedList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import timeseries.entities.Stock;
import timeseries.entities.StockPoint;

/**
 *
 * @author daniel
 */
public abstract class timeAlgorithm {
    /**
     * @param   stock       Is the full set of stock data to be processed.
     * @param   anomalies   Is the resulting dataset that is the stockpoints of anamoly data.
     */
    protected Stock stock;
    protected LinkedList<StockPoint> anomalies;
    
    public timeAlgorithm(Stock stock)
    {
        /**
         * @param   stock   Stock dataset that contains the data to be used.
         */
        this.stock = stock;
        anomalies = new LinkedList<StockPoint>();
    }
    
    public timeAlgorithm()
    {
        anomalies = new LinkedList<StockPoint>();
    }
    
    public LinkedList<StockPoint> findAnomalies()
    {
        /**
         * @return  returns all anomalies found in the calculations.
         */
        return anomalies;
    } 
    
    public void setStock (Stock stock)
    {
        /**
         * @param   stock   Stock dataset that contains the data to be used.
         */
        this.stock = stock;
    }
    
    public LinkedList<StockPoint> getAnomalies()
    {
        /**
         * @return  returns all anomalies found in the calculations.
         */
        return anomalies;
    }
    
    public void outputToFile(String filename)
    {
        /**
         * @param   filename    filename to output anomalies to.
         */
        BufferedWriter bufferedWriter = null;
        
        try
        {
           
            bufferedWriter = new BufferedWriter(new FileWriter(new File(filename))); 
            for(int i = 0; i < anomalies.size(); i++)
            {
                bufferedWriter.write(anomalies.get(i).toString());
                bufferedWriter.newLine();
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
