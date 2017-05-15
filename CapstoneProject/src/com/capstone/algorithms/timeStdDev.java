/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.algorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import java.lang.Double;
import java.lang.Math;

import com.capstone.entities.Stock;
import com.capstone.entities.StockPoint;

/**
 *
 * @author daniel
 */
public class TimeStdDev extends TimeAlgorithm
{
    protected double meanRateOfReturn;
    protected double meanVolume;
    
    protected double varSumRateOfReturn;
    protected double varSumVolume;
    
    protected double stdRateOfReturn;
    protected double stdVolume;
    
    protected double anomalyCoeffientRateOfReturn;
    protected double anomalyCoeffientVolume;
    
    protected double anomalyThreasholdRateOfReturn;
    protected double anomalyThreasholdVolume;
    
    protected LinkedList<Double> varRateOfReturn;
    protected LinkedList<Double> varVolume;
    
    public TimeStdDev(Stock stock)
    {
        /**
         * @param   stock   Stock dataset that contains the data to be used.
         */
        super(stock);
        
        anomalyCoeffientRateOfReturn = 2.0;
        anomalyCoeffientVolume = 2.0;
    }
    
    public TimeStdDev()
    {
        super();
        
        anomalyCoeffientRateOfReturn = 2.0;
        anomalyCoeffientVolume = 2.0;
    }
    
    public void setCoEfficents(double coefficent)
    {
        this.anomalyCoeffientRateOfReturn = coefficent;
        this.anomalyCoeffientVolume = coefficent;
    }
    
    public LinkedList<StockPoint> findAnomalies()
    {
        /**
         * @return  returns all anomalies found in the calculations.
         */
        
        varRateOfReturn = new LinkedList<Double>();
        varVolume = new LinkedList<Double>();
        
        calculateMeans();
        calculateVariences();
        
        calculateStandardDeviations();
        calculateAnomalyThreasholds();
        
        //System.out.println("Mean: " + meanRateOfReturn + "," + meanVolume);
        
        //System.out.printf("Varience: %.4f, %.4f \n", varSumRateOfReturn, varSumVolume);
        //System.out.printf("StdDev: %.4f, %.4f \n", stdRateOfReturn, stdVolume);
        //System.out.println("Volume Anomaly Levels: " + anomalyCoeffientVolume + "," + anomalyThreasholdVolume);
        //System.out.println("Rate of Return Anomaly Levels: " + anomalyCoeffientRateOfReturn + "," + anomalyThreasholdRateOfReturn);
        
        
        for (int i = 0; i < stock.getStockElements(); i++)
        {
            if(stock.getStockElement(i).getVolume() > (meanVolume + anomalyThreasholdVolume) || 
                    stock.getStockElement(i).getVolume() < (meanVolume - anomalyThreasholdVolume))
            {
                //System.out.println("Anomaly (Volume): " + stock.getStockElement(i).getListedDate() + " - " + stock.getStockElement(i).getVolume());
                anomalies.add(stock.getStockElement(i));
                
            }
            else if(stock.getStockElement(i).getRateOfReturn() > (meanRateOfReturn + anomalyThreasholdRateOfReturn) || 
                    stock.getStockElement(i).getRateOfReturn() < (meanRateOfReturn - anomalyThreasholdRateOfReturn))
            {
                //System.out.println("Anomaly (RateOfReturn): " + stock.getStockElement(i).getListedDate() + " - " + stock.getStockElement(i).getRateOfReturn());
                anomalies.add(stock.getStockElement(i));
                
            }
        }
        
        return anomalies;
    } 
    
    private void calculateStandardDeviations ()
    {
        stdRateOfReturn = Math.sqrt(Math.pow((varSumRateOfReturn - meanRateOfReturn), 2.0) / stock.getStockElements());
        stdVolume = Math.sqrt(Math.pow((varSumVolume - meanVolume) , 2.0) / stock.getStockElements());
        
    }
     
    
    private void calculateAnomalyThreasholds ()
    {
        anomalyThreasholdRateOfReturn = anomalyCoeffientRateOfReturn * stdRateOfReturn;
        anomalyThreasholdVolume = anomalyCoeffientVolume * stdVolume;
        
    }
    
    private void calculateMeans ()
    {
        meanRateOfReturn = 0.0;
        meanVolume = 0.0;
        
        for (int i = 0; i < stock.getStockElements(); i++)
        {
            //stock.getStockElement(i).calculateRateOfReturn();
            meanVolume += stock.getStockElement(i).getVolume();
            meanRateOfReturn += stock.getStockElement(i).getRateOfReturn();
        }
        
        meanRateOfReturn = meanRateOfReturn / stock.getStockElements();
        meanVolume = meanVolume / stock.getStockElements();
    }
    
    private void calculateVariences ()
    {
        double rateOfReturn;
        double volume;
        
        varSumRateOfReturn = 0.0;
        varSumVolume = 0.0;
        
        for (int i = 0; i < stock.getStockElements(); i++)
        {
            rateOfReturn = (stock.getStockElement(i).getRateOfReturn() - meanRateOfReturn);
            this.varRateOfReturn.add(rateOfReturn);
            this.varSumRateOfReturn += rateOfReturn * rateOfReturn;
            varSumRateOfReturn += rateOfReturn * rateOfReturn;
            
            volume = stock.getStockElement(i).getVolume() - meanVolume;
            this.varVolume.add(volume);
            varSumVolume += volume * volume;
        }
        
        varSumRateOfReturn = Math.sqrt(varSumRateOfReturn);
        varSumVolume = Math.sqrt(varSumVolume);
    }
    
    public void outputToDebugFile(String filename)
    {
        /**
         * @param   filename    filename to output anomalies to.
         */
        BufferedWriter bufferedWriter = null;
        
        try
        {
           
            bufferedWriter = new BufferedWriter(new FileWriter(new File(filename))); 
            bufferedWriter.write("date,symbol,priceOpen,priceClose,priceLow,priceHigh,volume,rateOfReturn,varienceVolume,varienceRateOfReturn");
            bufferedWriter.newLine();
            for(int i = 0; i < stock.getStockElements(); i++)
            {
                bufferedWriter.write(stock.getStockElement(i).toString() + "," + varVolume.get(i) + "," + 
                        varRateOfReturn.get(i) + "," /*+ stock.getStockElement(i).getRateOfReturn()*/);
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
