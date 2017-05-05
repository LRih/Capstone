package com.capstone.algorithms;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.Double;
import java.lang.Math;

import com.capstone.entities.ClusterGroup;
import com.capstone.entities.StockPoint;
import com.capstone.hiddenAnomalyBusiness.ClusterDataService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import java.util.Date;
import java.util.LinkedList;

/**
 * @author Jason.Zhuang
 * @studentId s3535252 24/04/2017 Jaccard.java 
 * Describe: computing Jaccard Index value
 */
public class Jaccard
{
    Map<Date, List<StockPoint>> _stocks;
    Map<Date, List<ClusterGroup>> _groups;
    int days;
    
    //Anomaly Detection
    
    protected double meanJaccard;
    protected double varSumJaccard;
    protected double stdJaccard;
    protected double anomalyCoeffientJaccard;
    protected double anomalyThreasholdJaccard;
    protected LinkedList<Double> varJaccard;

    public Jaccard ()
    {
        this._stocks = new HashMap<Date, List<StockPoint>>();
        this._groups = new HashMap<Date, List<ClusterGroup>>();
        this.days = 3;
    }

    public Jaccard (Map<Date, List<StockPoint>> stocks, Map<Date, List<ClusterGroup>> groups)
    {
        this._stocks = stocks;
        this._groups = groups;
        this.days = 3;
    }

    public Jaccard (Map<Date, List<StockPoint>> stocks, Map<Date, List<ClusterGroup>> groups, int days)
    {
        this._stocks = stocks;
        this._groups = groups;
        this.days = days;
    }

    public void setDays(int days)
    {
        /**
         * @param   days    days the jaccard index runs for.
         */
        this.days = days;
    }

    private double similarityValue(final ClusterGroup g1, final ClusterGroup g2)
    {
        /**
        * calculate the similarityValue of two groups
        * 
        * @param g1
        * @param g2
        * @return
        */
        int sameCount = 0;
        int unioCount = 0;
        double retValue = 0d;

        if (g1 == null)
        {
                throw new NullPointerException("g1 must not be null");
        }

        if (g2 == null)
        {
                throw new NullPointerException("g2 must not be null");
        }

        if (g1.equals(g2))
        {
                return 1d;
        }

        List<StockPoint> listA = g1.points;
        List<StockPoint> listB = g2.points;

        for (int i = 0; i < listA.size(); i++)
        {
                StockPoint spA = listA.get(i);
                for (int j = 0; j < listB.size(); j++)
                {
                        StockPoint spB = listB.get(j);
                        if (spA.sameStock(spB))
                        {
                                sameCount = sameCount + 1;
                        }
                }
        }

        unioCount = listA.size() + listB.size() - sameCount;

        if (unioCount != 0)
        {
                retValue = (double)sameCount/(double)unioCount;
        } else
        {
                retValue = 0d;
        }

        return retValue;
    }

    public void setStockData (HashMap<Date, List<StockPoint>> stocks, HashMap<Date, List<ClusterGroup>> groups)
    {
        this._stocks = stocks;
        this._groups = groups;
    }
        
    public void calculate()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int dayComplete = 0;
        
        // ensure that days is above 0
        if(days > 0)
        {
            // Loops for all data known
            for (Date dateKey : _stocks.keySet()) 
            {
                    System.out.println(dateKey + "(" + dayComplete++ + "/" + _stocks.size() + ")");
                //groups = _groups.get(dateKey);
                for (StockPoint stock : _stocks.get(dateKey))
                {
                    ClusterGroup G_i = _groups.get(dateKey).get(stock.getpGroup_number());

                    // Jaccard stuff
                    double d = 0;
                    for (int i = 0; i < days; i++)
                    {
                        Date dateWindow = modifiedDate(dateKey, i);
                        if(_groups.containsKey(dateWindow))
                        {
                            ClusterGroup G_ij = _groups.get(dateWindow).get(stock.getpGroup_number());
                            d += similarityValue(G_i, G_ij);
                        }
                    }

                    stock.setJIndex(1.0 * (d / days));
                    //System.out.println(stock.getJIndex());
                }
            } 


        }
    }
        
    public LinkedList<StockPoint> findAnomalies()
    {
        /**
         * @return  returns all anomalies found in the calculations.
         */
        LinkedList<StockPoint> anomalies = new LinkedList<StockPoint>();
        varJaccard = new LinkedList<Double>();
        //varRateOfReturn = new LinkedList<Double>();
        //varVolume = new LinkedList<Double>();
         
        anomalyCoeffientJaccard = 2.0;
        
        calculateMeans();
        calculateVariences();
        
        calculateStandardDeviations();
        calculateAnomalyThreasholds();
        
        
        
        System.out.println("Mean: " + meanJaccard);
        
        System.out.printf("Varience: %.4f \n", varSumJaccard);
        System.out.printf("StdDev: %.4f \n", stdJaccard);
        System.out.println("Jaccard Anomaly Levels: " + anomalyCoeffientJaccard + "," + anomalyThreasholdJaccard);
        //System.out.println("Rate of Return Anomaly Levels: " + anomalyCoeffientRateOfReturn + "," + anomalyThreasholdRateOfReturn);
        System.out.println((meanJaccard + anomalyThreasholdJaccard) + " " + (meanJaccard - anomalyThreasholdJaccard));
        for (Date dateKey : _stocks.keySet())
        {
            for (StockPoint stock : _stocks.get(dateKey))
            {
                
                //double stockJVar = (stock.getJIndex() - meanJaccard);
                if((stock.getJIndex() > (meanJaccard + anomalyThreasholdJaccard)) || 
                    (stock.getJIndex() < (meanJaccard - anomalyThreasholdJaccard)))
                {
                    //System.out.println("Anomaly (RateOfReturn): " + stock.getStockElement(i).getListedDate() + " - " + stock.getStockElement(i).getRateOfReturn());
                    anomalies.add(stock);
                    //System.out.println("Anomaly (" + dateKey + ")(" + stock.getStockSymbol() + ")" + stock.getJIndex());
                }
            }
        }
        System.out.println("Anomalies (Jaccard): " + anomalies.size());
        /*
        
        
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
        }*/
        
        return anomalies;
    } 
    
    /*private void calculateStandardDeviations ()
    {
        int elements = 0;
        for(Date dateKey : _stocks.keySet())
        {
            for(StockPoint stock : _stocks.get(dateKey))
            {
                elements++;
            }
            //elements += _stocks.get(dateKey).size();
        }
        //this.stdJaccard = Math.sqrt(Math.pow((varSumJaccard - meanJaccard), 2.0) / elements);
        
    }*/
     
    
    private void calculateAnomalyThreasholds ()
    {
        this.anomalyThreasholdJaccard = anomalyCoeffientJaccard * stdJaccard;
        
        
    }
    
    private void calculateMeans ()
    {
        meanJaccard = 0.0;
        int elements = 0;
        
        for (Date dateKey : _stocks.keySet())
        {
            //elements += _stocks.get(dateKey).size();
            for (StockPoint stock : _stocks.get(dateKey))
            {
                elements++;
                meanJaccard = meanJaccard + stock.getJIndex();
            }
        }
        
        meanJaccard = meanJaccard / elements;
    }
    
    /*private void calculateVariences ()
    {
        double jaccard;
        int elements = 0;
        
        varSumJaccard = 0.0;
        
        for (Date dateKey : _stocks.keySet())
        {
            for (StockPoint stock : _stocks.get(dateKey))
            {
                elements++;
                varSumJaccard += stock.getJIndex();
                
                // varience
                jaccard = (stock.getJIndex()- meanJaccard);
                this.varJaccard.add(jaccard);
                this.varSumJaccard += jaccard * jaccard;    
            }
        }
        
        varSumJaccard = Math.sqrt(varSumJaccard) / elements;
        this.stdJaccard = Math.sqrt(Math.pow((varSumJaccard - meanJaccard), 2.0) / elements);
        //this.stdJaccard = Math.sqrt(varSumJaccard / elements);
    }*/
    
    private void calculateStandardDeviations()
    {
        stdJaccard = Math.sqrt(varSumJaccard);

        
    }
    
    private void calculateVariences()
    {
        double variance = 0;
        int elements = 0;
        
        for (Date dateKey : _stocks.keySet())
        {
            for (StockPoint stock : _stocks.get(dateKey))
            {
                elements++;
                variance += Math.pow(stock.getJIndex() - meanJaccard, 2);
            }
        }

        varSumJaccard =  variance / (elements - 1);
    }  
    
    public double coefficient(StockPoint arg_sp, String[] timeWindows)
    {

        /**
        * calculate the Jaccard index coefficient of a StockPoint in different
        * timewindows
        * 
        * @param arg_sp
        *            it is the stock point which is in time window j. It could be
        *            the same stock ,but the returnRate and volume are different
        *            between different time window.
        * @param timeWindows
        * @return Jaccard index coefficient
        */

        // the G_i group is the partition to which Si belongs at time window.
        // G_ij means S(i) in the Cluster Group to which S(i) belongs at
        // TimeWindow(j)
        double retValue = 0;
        int w = timeWindows.length;
        if (w == 0)
        {
                return 0;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String stcokDate = df.format(arg_sp.getListedDate());
        String stockSymbol = arg_sp.getStockSymbol();
        int groupId = arg_sp.getpGroup_number();

        ClusterDataService clusterDS = new ClusterDataService();

        // get the Group of Si
        ClusterGroup G_i = clusterDS.getClusterGroupByDayandId(stcokDate,groupId);

        double d = 0;
        for (int i = 0; i < w; i++)
        {

                ClusterGroup G_ij = clusterDS.getClusterGroupByDateAndSymbol(timeWindows[i], stockSymbol);
                double sim = similarityValue(G_i, G_ij);
                d = d + sim;
        }

        if (w > 0)
        {
                retValue = 1.0 * (d / w);
        } else
        {
                retValue = 0;
        }

        return retValue;
    }
        
    public Map<Date, List<StockPoint>> getStocks ()
    {
        /**
         * @return  returns stock hashmap.
         */
        return _stocks;
    }
        
    private Date modifiedDate (Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
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
            bufferedWriter.write("date,symbol,priceOpen,priceClose,priceLow,priceHigh,volume,rateOfReturn,jaccardIndex,clusterID");
            bufferedWriter.newLine();
            for (Date dateKey : _stocks.keySet())
            {
                for (StockPoint stock : _stocks.get(dateKey))
                {
                    bufferedWriter.write(stock.toString() + "," + stock.getJIndex() + "," + stock.getpGroup_number());
                        //varJaccard.get(i) + "," /*+ stock.getStockElement(i).getRateOfReturn()*/);
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