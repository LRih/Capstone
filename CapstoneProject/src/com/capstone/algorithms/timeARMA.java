/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.algorithms;

import java.lang.Double;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.LinkedList;

import com.capstone.entities.Stock;
import com.capstone.entities.StockPoint;

/**
 *
 * @author daniel
 */
public class timeARMA extends timeAlgorithm{
    /**
     * Notes:
     * Time variable will be t which is a constant. This will rely on the fact that the X values 
     * (volume or rate of return) will become the main variable. All calculations will be able to 
     * duplicated for either value.
     * 
     * This will mean that parameters will need to be set either on launch or prior to running the 
     * findAnomalies() method.
     */
    
    protected double pValue;
    protected double qValue;
    
    
    LinkedList<Double> MAUtVolume;
    LinkedList<Double> MAUtRateOfReturn;
    LinkedList<Double> ARUtVolume;
    LinkedList<Double> ARUtRateOfReturn;
    
    LinkedList<Double> bestUtVolume;
    LinkedList<String> bestUtVolumeType;
    LinkedList<Double> bestUtRateOfReturn;
    LinkedList<String> bestUtRateOfReturnType;
    
    
    protected double stdRateOfReturn;
    protected double stdVolume;
    
    protected double anomalyCoeffientRateOfReturn;
    protected double anomalyCoeffientVolume;
    
    protected double anomalyThreasholdRateOfReturn;
    protected double anomalyThreasholdVolume;
    
    public timeARMA(Stock stock)
    {
        /**
         * @param   stock   Stock dataset that contains the data to be used.
         */
        super(stock);
        
        pValue = 1;
        qValue = 1;
        
        anomalyCoeffientRateOfReturn = 2.0;
        anomalyCoeffientVolume = 2.0;
        
    }
    
    public timeARMA()
    {
        super();
        
        pValue = 1;
        qValue = 1;
        
        anomalyCoeffientRateOfReturn = 2.0;
        anomalyCoeffientVolume = 2.0;
        
    }
    
    public double getPValue()
    {
        /**
         * @return  returns the currently stored P Value.
         */
        return pValue;
    }
    
    public double getQValue()
    {
        /**
         * @return  returns the currently stored Q Value.
         */
        return qValue;
    }
   
    public void setCoEfficents(double coefficent)
    {
        this.anomalyCoeffientRateOfReturn = coefficent;
        this.anomalyCoeffientVolume = coefficent;
    }
    
    public void setPValue(double pValue)
    {
        /**
         * @param   pValue  the P Value argument for the ARMA model.
         */
        this.pValue = pValue;
    }
    
    public LinkedList<StockPoint> findAnomalies()
    {
        /**
         * @return  returns all anomalies found in the calculations.
         */
        double XtVolume = 0.0;
        double XtRateOfReturn = 0.0;
        
        double varAR = 0.0;
        double varMA = 0.0;
        double varARMA = 0.0;
        
        // Creates new lists for data to be housed.
        bestUtVolume = new LinkedList<Double>();
        bestUtVolumeType = new LinkedList<String>();
        bestUtRateOfReturn = new LinkedList<Double>();
        bestUtRateOfReturnType = new LinkedList<String>();
        
        /**
         * Steps: 
         *  - Work out MA
         *  - Work out AM
         *  - Combine to work out ARMA
         *  - Determine best function to use
         *  - [Incomplete] Determine if anomaly.
         */
        
        // MA Modelling
        modelMA();
        
        // AR Modelling
        modelAR();
        
        // Work out best function to use by closest to actual value.
        for(int t = 0; t < stock.getStockElements(); t++)
        {
            // Volume
            varAR = Math.sqrt(Math.pow((stock.getStockElement(t).getVolume() - ARUtVolume.get(t)), 2.0));
            varMA = Math.sqrt(Math.pow((stock.getStockElement(t).getVolume() - MAUtVolume.get(t)), 2.0));
            varARMA = Math.sqrt(Math.pow((stock.getStockElement(t).getVolume() - (ARUtVolume.get(t) + MAUtVolume.get(t))), 2.0));
            
            if(varAR < varMA && varAR < varARMA)
            {
                bestUtVolume.add(varAR);
                bestUtVolumeType.add("AR");
                //System.out.println("Volume at time " + t + " varience is " + varAR + " with formula AR");
            }
            else if (varMA < varARMA)
            {
                bestUtVolume.add(varMA);
                bestUtVolumeType.add("MA");
                //System.out.println("Volume at time " + t + " varience is " + varAR + " with formula AR");
            }
            else
            {
                bestUtVolume.add(varARMA);
                bestUtVolumeType.add("ARMA");
                //System.out.println("Volume at time " + t + " varience is " + varARMA + " with formula ARMA");
            }
            
            /* Debug readouts*/
            /*System.out.println("Xt: " + stock.getStockElement(t).getVolume() + ", var(AR): " + varAR + 
                    ", var(MA): " + varMA + ", var(ARMA): " + varARMA);
            System.out.println("Xt: " + stock.getStockElement(t).getVolume() + ", AR: " + ARUtVolume.get(t) + 
                    ", MA: " + MAUtVolume.get(t) + ", ARMA: " + (ARUtVolume.get(t) + MAUtVolume.get(t)));
            */
            
            // Rate of Return
            varAR = Math.sqrt(Math.pow((stock.getStockElement(t).getRateOfReturn() - ARUtRateOfReturn.get(t)), 2.0));
            varMA = Math.sqrt(Math.pow((stock.getStockElement(t).getRateOfReturn() - MAUtRateOfReturn.get(t)), 2.0));
            varARMA = Math.sqrt(Math.pow((stock.getStockElement(t).getRateOfReturn() - (ARUtRateOfReturn.get(t) + MAUtRateOfReturn.get(t))), 2.0));

            if(varAR < varMA && varAR < varARMA)
            {
                bestUtRateOfReturn.add(varAR);
                bestUtRateOfReturnType.add("AR");
                //System.out.println("Rate of Return at time " + t + " varience is " + varAR + " with formula AR");
            }
            else if (varMA < varARMA)
            {
                bestUtRateOfReturn.add(varMA);
                bestUtRateOfReturnType.add("MA");
                //System.out.println("Rate of Return at time " + t + " varience is " + varAR + " with formula AR");
            }
            else
            {
                bestUtRateOfReturn.add(varARMA);
                bestUtRateOfReturnType.add("ARMA");
                //System.out.println("Rate of Return at time " + t + " varience is " + varARMA + " with formula ARMA");
            }
            /* Debug Readouts
            System.out.println("Xt: " + stock.getStockElement(t).getVolume() + ", var(AR): " + varAR + 
                    ", var(MA): " + varMA + ", var(ARMA): " + varARMA);
            System.out.println("Xt: " + stock.getStockElement(t).getVolume() + ", AR: " + ARUtRateOfReturn.get(t) + 
                    ", MA: " + MAUtRateOfReturn.get(t) + ", ARMA: " + (ARUtRateOfReturn.get(t) + MAUtRateOfReturn.get(t)));
            */
        }
        
        // Work out stdDev of movement
        
        /* Calc Means */
        double meanRateOfReturn = 0.0;
        double meanVolume = 0.0;
        double varSumVolume = 0.0;
        double varSumRateOfReturn = 0.0;
        double varRateOfReturn = 0.0;
        double varVolume = 0.0;
        
        for (int i = 0; i < bestUtRateOfReturn.size(); i++)
        {
            //stock.getStockElement(i).calculateRateOfReturn();
            meanVolume += bestUtVolume.get(i).doubleValue();
            meanRateOfReturn += bestUtRateOfReturn.get(i).doubleValue();
        }
        
        meanRateOfReturn = meanRateOfReturn / bestUtRateOfReturn.size();
        meanVolume = meanVolume / bestUtVolume.size();
        
        /* Get Variences */
        
        varSumRateOfReturn = 0.0;
        varSumVolume = 0.0;
        
        for (int i = 0; i < bestUtRateOfReturn.size(); i++)
        {
            varRateOfReturn = bestUtRateOfReturn.get(i) - meanRateOfReturn;
            varSumRateOfReturn += varRateOfReturn * varRateOfReturn;
            
            varVolume = bestUtVolume.get(i) - meanVolume;
            varSumVolume += varVolume * varVolume;
        }
        
        varSumRateOfReturn = Math.sqrt(varSumRateOfReturn);
        varSumVolume = Math.sqrt(varSumVolume);
        
        /* Standard Dev */
        
        stdRateOfReturn = Math.sqrt(Math.pow((varSumRateOfReturn - meanRateOfReturn), 2.0) / bestUtRateOfReturn.size());
        stdVolume = Math.sqrt(Math.pow((varSumVolume - meanVolume) , 2.0) / bestUtVolume.size());
        
       
        
        /* Calculate Threasholds */
        anomalyThreasholdRateOfReturn = anomalyCoeffientRateOfReturn * stdRateOfReturn;
        anomalyThreasholdVolume = anomalyCoeffientVolume * stdVolume;
        
        for (int i = 0; i < bestUtRateOfReturn.size(); i++)
        {
            if(bestUtVolume.get(i).doubleValue() > (meanVolume + anomalyThreasholdVolume) || 
                    bestUtVolume.get(i).doubleValue() < (meanVolume - anomalyThreasholdVolume))
            {
                //System.out.println("Anomaly (Volume): " + stock.getStockElement(i).getListedDate() + " - " + stock.getStockElement(i).getVolume());
                anomalies.add(stock.getStockElement(i));
                
            }
            else if(bestUtRateOfReturn.get(i).doubleValue() > (meanRateOfReturn + anomalyThreasholdRateOfReturn) || 
                    bestUtRateOfReturn.get(i).doubleValue() < (meanRateOfReturn - anomalyThreasholdRateOfReturn))
            {
                //System.out.println("Anomaly (RateOfReturn): " + stock.getStockElement(i).getListedDate() + " - " + stock.getStockElement(i).getRateOfReturn());
                anomalies.add(stock.getStockElement(i));
                
            }
        }
        
        // Returns the detected anomalies
        return anomalies;
    }
    
    
    public void setQValue(double qValue)
    {
        /**
         * @param   qValue  the Q Value argument for the ARMA model.
         */
        this.qValue = qValue;
    }
    
    private void modelAR()
    {
        /**
         * Works out the expected values at a given point in time. Takes all values
         * set within the class and returns them back to the class variables.
         */
        
        double aValue = 0.05;
        double ZtVolume = 0.0;
        double ZtRateOfReturn = 0.0;
        double Ut = 0.0;
        
        // Creates new linked lists for the data to be housed.
        ARUtVolume = new LinkedList<Double>();
        ARUtRateOfReturn = new LinkedList<Double>();
        
        // Initial Values are 0.0
        ARUtVolume.add(0.0);
        ARUtRateOfReturn.add(0.0);
                
        // Due to p is lag, the count must start at the 2nd element.
        for (int t = 1; t < stock.getStockElements(); t++)
        {
            // Work out Zt for current value
            ZtVolume = (aValue * stock.getStockElement(t).getVolume()) + ((1 - aValue) * 
                    stock.getStockElement(t - 1).getVolume());
            ZtRateOfReturn = (aValue * stock.getStockElement(t).getRateOfReturn()) 
                    + ((1 - aValue) * stock.getStockElement(t - 1).getRateOfReturn());
            
            // Ut Value Recursion Calculations
            Ut = ZtVolume + modelARUtVolume(aValue, t, 1);  
            ARUtVolume.add(Ut);
            
            Ut = ZtRateOfReturn + modelARUtRateOfReturn(aValue, t, 1);  
            ARUtRateOfReturn.add(Ut);
        }
    }
    
    private double modelARUtRateOfReturn(double aValue, int t, int pValueOrder)
    {
        /**
         * @param   aValue      lag coeffient value in the AR model formula.
         * @param   t           time point.
         * @param   pValueOrder Order of the recursion currently up to.
         * @return              Calculated Zt value.
         */
        
        /**
         * If there is another iteration to go, call the recursive function,
         * Else return the calculation
         */
        if (pValueOrder < pValue && (t - pValueOrder) > 1)
        {
            return (modelARUtRateOfReturn(aValue, t, ++pValueOrder) + (Math.pow((aValue), pValueOrder) 
                * stock.getStockElement(t - pValueOrder).getRateOfReturn()));
        }
        else
        {
            return (Math.pow((aValue), pValueOrder) * stock.getStockElement(t 
                    - pValueOrder).getRateOfReturn());
        }
    }
    
    private double modelARUtVolume(double aValue, int t, int pValueOrder)
    {
        /**
         * @param   aValue      lag coeffient value in the AR model formula.
         * @param   t           time point.
         * @param   pValueOrder Order of the recursion currently up to.
         * @return              Calculated Zt value.
         */
        
        /**
         * If there is another iteration to go, call the recursive function,
         * Else return the calculation
         */
        if (pValueOrder < pValue && (t - pValueOrder) > 1)
        {
            return (modelARUtVolume(aValue, t, ++pValueOrder) + (Math.pow((aValue), pValueOrder) 
                * stock.getStockElement(t - pValueOrder).getVolume()));
        }
        else
        {
            return (Math.pow((aValue), pValueOrder) * stock.getStockElement(t - pValueOrder).getVolume());
        }
    }
    
    private void modelMA()
    {
        /**
         * Works out the expected values at a given point in time. Takes all values
         * set within the class and returns them back to the class variables.
         */
        // B Value Calculation Unknown, assume 0.5
        double bValue = 0.5;
        double x = 0.0;
        double Ut = 0.0;
        
        // Creates a new LinkedList
        MAUtVolume = new LinkedList<Double>();
        MAUtRateOfReturn = new LinkedList<Double>();
        
        // Set first value of Zt as 0
        MAUtVolume.add(0.0);
        MAUtRateOfReturn.add(0.0);
        
        // Gets Ut value for each StockPoint.
        for(int t = 1; t < stock.getStockElements(); t++)
        {
            Ut = modelMAUtVolume(bValue, t, 0);
            MAUtVolume.add(Ut);
            
            Ut = modelMAUtRateOfReturn(bValue, t, 0);
            MAUtRateOfReturn.add(Ut);            
        }
        
    }
    
    private double modelMAUtVolume(double bValue, int t, int qValueOrder)
    {
        /**
         * @param   bValue      weighted value in the MA model formula.
         * @param   t           time point.
         * @param   qValueOrder Order of the recursion currently up to.
         * @return              Calculated Zt value.
         */
        
        /**
         * If there is another iteration to go, call the recursive function,
         * Else return the calculation
         */
        if (qValueOrder < qValue && (t - qValueOrder) > 0)
        {
            return (modelMAUtVolume(bValue, t, ++qValueOrder) + (Math.pow((1 - bValue), qValueOrder) 
                * stock.getStockElement(t - qValueOrder).getVolume()));
        }
        else
        {
            return (Math.pow((1 - bValue), qValueOrder) * stock.getStockElement(t 
                    - qValueOrder).getVolume());
        }
    }
    
    private double modelMAUtRateOfReturn(double bValue, int t, int qValueOrder)
    {
        /**
         * @param   bValue      weighted value in the MA model formula.
         * @param   t           time point.
         * @param   qValueOrder Order of the recursion currently up to.
         * @return              Calculated Zt value.
         */
        
        /**
         * If there is another iteration to go, call the recursive function,
         * Else return the calculation
         */
        if (qValueOrder < qValue && (t - ++qValueOrder) > 0)
        {
            return (modelMAUtRateOfReturn(bValue, t, qValueOrder) + (Math.pow((1 - bValue), qValueOrder) 
                * stock.getStockElement(t - qValueOrder).getRateOfReturn()));
        }
        else
        {
            return (Math.pow((1 - bValue), qValueOrder) * stock.getStockElement(t 
                    - qValueOrder).getRateOfReturn());
        }
        
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
            
            // Prints Header
            bufferedWriter.write("date,symbol,open,close,low,high,volume,rateofreturn,bestUTVolume,"
                    + "bestUtTypeVolume,ARUtVolume,MAUtVolume,ARMAUtVolume,bestUTRateOfReturn,"
                    + "bestUtTypeRateOfReturn,ARUtRateOfReturn,MAUtRateOfReturn,ARMAUtRateOfReturn");
            bufferedWriter.newLine();
            
            // Prints each line
            for(int i = 0; i < stock.getStockElements(); i++)
            {
                bufferedWriter.write(stock.getStockElement(i).toString() + "," + 
                        /*stock.getStockElement(i).getRateOfReturn() + "," +*/ bestUtVolume.get(i) + "," + 
                        bestUtVolumeType.get(i) + "," + ARUtVolume.get(i) + "," + MAUtVolume.get(i) + "," + 
                        (ARUtVolume.get(i) + MAUtVolume.get(i)) + "," + bestUtRateOfReturn.get(i) + "," + 
                        bestUtRateOfReturnType.get(i) + "," + ARUtRateOfReturn.get(i) + "," + MAUtRateOfReturn.get(i) + "," + 
                        (ARUtRateOfReturn.get(i) + MAUtRateOfReturn.get(i)));
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
