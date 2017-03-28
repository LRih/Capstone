/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseries.algothims;

import java.util.LinkedList;

import java.lang.Double;

import timeseries.entities.Stock;
import timeseries.entities.StockPoint;

/**
 *
 * @author daniel
 */
public class timeStdDev extends timeAlgorithm {
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
    
    public timeStdDev(Stock stock)
    {
        /**
         * @param   stock   Stock dataset that contains the data to be used.
         */
        super(stock);
        
        anomalyCoeffientRateOfReturn = 2.0;
        anomalyCoeffientVolume = 2.0;
    }
    
    public timeStdDev()
    {
        super();
        
        anomalyCoeffientRateOfReturn = 2.0;
        anomalyCoeffientVolume = 2.0;
    }
    
    public LinkedList<StockPoint> findAnomalies()
    {
        /**
         * @return  returns all anomalies found in the calculations.
         */
        
        varRateOfReturn = new LinkedList<Double>();
        varVolume = new LinkedList<Double>();
        
        calculateMeans();
        calculateMeanRateOfReturn();
        calculateVariences();
        
        return anomalies;
    } 
    
    private double calculateMeanRateOfReturn ()
    {
        double mean = 0.0;
        
        for (int i = 0; i < stock.getStockElements(); i++)
        {
            stock.getStockElement(i).calculateRateOfReturn();
            mean += stock.getStockElement(i).getRateOfReturn();
        }
        
        return mean;
    }
    
    private void calculateMeans ()
    {
        meanRateOfReturn = 0.0;
        meanVolume = 0.0;
        
        for (int i = 0; i < stock.getStockElements(); i++)
        {
            stock.getStockElement(i).calculateRateOfReturn();
            meanVolume += stock.getStockElement(i).getRateOfReturn();
            meanRateOfReturn += stock.getStockElement(i).getVolume();
        }
        
        meanRateOfReturn = meanRateOfReturn / stock.getStockElements();
        meanVolume = meanVolume / stock.getStockElements();
    }
    
    private void calculateVariences ()
    {
        double varRateOfReturn;
        double varVolume;
        
        varSumRateOfReturn = 0.0;
        varSumVolume = 0.0;
        
        for (int i = 0; i < stock.getStockElements(); i++)
        {
            varRateOfReturn = stock.getStockElement(i).getRateOfReturn() - meanRateOfReturn;
            this.varRateOfReturn.add(varRateOfReturn);
            varSumRateOfReturn += varRateOfReturn;
            
            varVolume = stock.getStockElement(i).getVolume() - meanVolume;
            this.varVolume.add(varVolume);
            varSumVolume += varRateOfReturn;
        }
    }
}
