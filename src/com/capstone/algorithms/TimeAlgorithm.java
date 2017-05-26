package com.capstone.algorithms;

import java.util.LinkedList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import com.capstone.entities.Stock;
import com.capstone.entities.StockPoint;

public abstract class TimeAlgorithm
{
    /**
     * @param stock       Is the full set of stock data to be processed.
     * @param anomalies   Is the resulting dataset that is the stockpoints of anamoly data.
     */
    protected Stock stock;
    protected LinkedList<StockPoint> anomalies;

    /**
     * @param   stock   Stock dataset that contains the data to be used.
     */
    public TimeAlgorithm(Stock stock)
    {
        this.stock = stock;
        anomalies = new LinkedList<StockPoint>();
    }

    public TimeAlgorithm()
    {
        anomalies = new LinkedList<StockPoint>();
    }

    /**
     * @return returns all anomalies found in the calculations.
     */
    public LinkedList<StockPoint> findAnomalies()
    {
        return anomalies;
    }

    /**
     * @return returns all anomalies found in the calculations.
     */
    public LinkedList<StockPoint> getAnomalies()
    {
        return anomalies;
    }

    /**
     * @param   filename    filename to output anomalies to.
     */
    public void outputToFile(String filename)
    {
        BufferedWriter bufferedWriter = null;

        try
        {

            bufferedWriter = new BufferedWriter(new FileWriter(new File(filename)));

            bufferedWriter.write("date,symbol,priceOpen,priceClose,priceLow,priceHigh,volume,rateOfReturn");
            bufferedWriter.newLine();
            for (int i = 0; i < anomalies.size(); i++)
            {
                bufferedWriter.write(anomalies.get(i).toString());
                bufferedWriter.newLine();
            }
        }
        catch (IOException e)
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
