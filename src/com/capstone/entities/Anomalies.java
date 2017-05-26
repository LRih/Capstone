package com.capstone.entities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Anomalies
{
    private Map<Type, List<StockPoint>> anomalies;

    /**
     * Creates the HashMap used for the class.
     */
    public Anomalies()
    {
        anomalies = new HashMap<Type, List<StockPoint>>();
    }

    /**
     * @param type Name of the key for the type of anomaly being used.
     */
    public void addAnomalyType(Type type)
    {
        anomalies.put(type, new LinkedList<StockPoint>());
    }

    /**
     * @param type       Name of the key for the type of anomaly being used.
     * @param stockPoint Data point that is an anomaly.
     */
    public void addAnomaly(Type type, StockPoint stockPoint)
    {
        anomalies.get(type).add(stockPoint);
    }

    public Set<Type> getKeySet()
    {
        return anomalies.keySet();
    }

    public List<StockPoint> getStockList(Type key)
    {
        return anomalies.get(key);
    }

    /**
     * @param filename filename to output anomalies to.
     */
    public void outputToFile(String filename)
    {
        // Output to file(s)
        BufferedWriter bufferedWriter = null;

        try
        {

            bufferedWriter = new BufferedWriter(new FileWriter(new File(filename)));

            bufferedWriter.write("date,symbol,priceOpen,priceClose,priceLow,priceHigh,volume,rateOfReturn,anomalytype");
            bufferedWriter.newLine();

            for (Type key : anomalies.keySet())
            {
                // Adds all stock that are relevant to stock class.
                for (StockPoint stockPoint : anomalies.get(key))
                {
                    bufferedWriter.write(stockPoint.toString() + "," + key);
                    bufferedWriter.newLine();
                }

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

    public enum Type
    {
        STDDEV, ARMA, Jaccard
    }
}
