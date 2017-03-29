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
        
        // TODO - Combine AR + MA to get ARMA
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
    
    private void modelMA()
    {
        /**
         * Works out the expected values at a given point in time. Takes all values
         * set within the class and returns them back to the class variables.
         */
        // B Value Calculation Unknown, assume 0.5
        double bValue = 0.5;
        double x = 0.0;
        double z = 0.0;
        
        MAUtVolume = new LinkedList<Double>();
        MAUtRateOfReturn = new LinkedList<Double>();
        
        
        
        // Set first value of Zt as 0
        // x(t) = sum (q, i = 0) (b(i) * x(t-i))
        MAUtVolume.add(0.0);
        
        for(int t = 1; t < stock.getStockElements(); t++)
        {
            z = modelMAZtVolume(bValue, t, 0);
            MAUtVolume.add(z);
            System.out.println(t + " - " + stock.getStockElement(t).getVolume() + "," + z);
            
            z = modelMAZtRateOfReturn(bValue, t, 0);
            MAUtVolume.add(z);
            System.out.println(t + " - " + stock.getStockElement(t).getVolume() + "," + z);
            
        }
        
    }
    
    private double modelMAZtVolume(double bValue, int t, int qValueOrder)
    {
        /**
         * @param   bValue      weighted value in the MA model formula.
         * @param   t           time point.
         * @param   qValueOrder Order of the recursion currently up to.
         * @return              Calculated Zt value.
         */
        
        // Make the order go up by 1.
        qValueOrder++;
        
        if (qValueOrder < qValue && (t - qValueOrder) > 0)
        {
            return (modelMAZtVolume(bValue, t, qValueOrder) + (Math.pow((1 - bValue), qValueOrder) 
                * stock.getStockElement(t - qValueOrder).getVolume()));
        }
        else
        {
            return (Math.pow((1 - bValue), qValueOrder) * stock.getStockElement(t 
                    - qValueOrder).getVolume());
        }
    }
    
    private double modelMAZtRateOfReturn(double bValue, int t, int qValueOrder)
    {
        /**
         * @param   bValue      weighted value in the MA model formula.
         * @param   t           time point.
         * @param   qValueOrder Order of the recursion currently up to.
         * @return              Calculated Zt value.
         */
        
        // Make the order go up by 1.
        qValueOrder++;
        
        if (qValueOrder < qValue && (t - qValueOrder) > 0)
        {
            return (modelMAZtRateOfReturn(bValue, t, qValueOrder) + (Math.pow((1 - bValue), qValueOrder) 
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
