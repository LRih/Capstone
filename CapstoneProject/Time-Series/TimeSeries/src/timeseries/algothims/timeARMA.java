/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseries.algothims;

import java.lang.Double;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.LinkedList;

import timeseries.entities.Stock;
import timeseries.entities.StockPoint;

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
    
    public timeARMA(Stock stock)
    {
        /**
         * @param   stock   Stock dataset that contains the data to be used.
         */
        super(stock);
        
        pValue = 1;
        qValue = 1;
        
    }
    
    public timeARMA()
    {
        super();
        
        pValue = 1;
        qValue = 1;
        
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
        
        /**
         * Steps: 
         *  - Work out MA
         *  - Work out AM
         *  - Combine to work out ARMA
         *  - Determine best function to use
         *  - Determine if anomaly.
         */
        
        // TODO - MA Modelling
        modelMA();
        
        // TODO - AR Modelling
        modelAR();
        
        // TODO - Combine AR + MA to get ARMA
        // Work out best function to use by closest to actual value.
        for(int t = 0; t < stock.getStockElements(); t++)
        {
            varAR = Math.sqrt(Math.pow((stock.getStockElement(t).getVolume() - ARUtVolume.get(t)), 2.0));
            varMA = Math.sqrt(Math.pow((stock.getStockElement(t).getVolume() - MAUtVolume.get(t)), 2.0));
            varARMA = Math.sqrt(Math.pow((stock.getStockElement(t).getVolume() - (ARUtVolume.get(t) + MAUtVolume.get(t))), 2.0));
            
            if(varAR < varMA && varAR < varARMA)
            {
                System.out.println("Volume at time " + t + " varience is " + varAR + " with formula AR");
            }
            else if (varMA < varARMA)
            {
                System.out.println("Volume at time " + t + " varience is " + varAR + " with formula AR");
            }
            else
            {
                System.out.println("Volume at time " + t + " varience is " + varARMA + " with formula ARMA");
            }
            System.out.println("Xt: " + stock.getStockElement(t).getVolume() + ", var(AR): " + varAR + 
                    ", var(MA): " + varMA + ", var(ARMA): " + varARMA);
            System.out.println("Xt: " + stock.getStockElement(t).getVolume() + ", AR: " + ARUtVolume.get(t) + 
                    ", MA: " + MAUtVolume.get(t) + ", ARMA: " + (ARUtVolume.get(t) + MAUtVolume.get(t)));
            
        }
        
        System.out.println(stock.getStockElements());
        
        
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
        
        
        ARUtVolume = new LinkedList<Double>();
        ARUtRateOfReturn = new LinkedList<Double>();
        
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
            System.out.println(t + " - " + stock.getStockElement(t).getVolume() + "," + Ut);
            
            Ut = ZtRateOfReturn + modelARUtRateOfReturn(aValue, t, 1);  
            ARUtVolume.add(Ut);
            System.out.println(t + " - " + stock.getStockElement(t).getVolume() + "," + Ut);
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
        
        MAUtVolume = new LinkedList<Double>();
        MAUtRateOfReturn = new LinkedList<Double>();
        
        
        
        // Set first value of Zt as 0
        // x(t) = sum (q, i = 0) (b(i) * x(t-i))
        MAUtVolume.add(0.0);
        MAUtRateOfReturn.add(0.0);
        
        for(int t = 1; t < stock.getStockElements(); t++)
        {
            Ut = modelMAUtVolume(bValue, t, 0);
            MAUtVolume.add(Ut);
            //System.out.println(t + " - " + stock.getStockElement(t).getVolume() + "," + ut);
            
            Ut = modelMAUtRateOfReturn(bValue, t, 0);
            MAUtRateOfReturn.add(Ut);
            //System.out.println(t + " - " + stock.getStockElement(t).getRateOfReturn() + "," + ut);
            
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
        
        // Make the order go up by 1.
        
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
        
        // Make the order go up by 1.
        //qValueOrder++;
        
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
            for(int i = 0; i < stock.getStockElements(); i++)
            {
                bufferedWriter.write(stock.getStockElement(i).toString() /*+ "," + varVolume.get(i) + "," + 
                        varRateOfReturn.get(i) + "," + stock.getStockElement(i).getRateOfReturn()*/);
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
