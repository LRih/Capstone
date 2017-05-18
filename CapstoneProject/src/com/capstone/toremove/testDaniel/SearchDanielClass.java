/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.toremove.testDaniel;

import com.capstone.algorithms.JaccardIndex;
import com.capstone.data.SigmoidSigmoidPreprocessor;
import com.capstone.entities.Anomalies;
import com.capstone.entities.StockPoint;

import java.util.List;
import java.util.Map;
import java.util.Date;
/**
 *
 * @author Shadow
 */
public class SearchDanielClass {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        Map<Date, List<StockPoint>> _stocks;
        int k = 4;

        // Post-Process it so can be used directly.
        SigmoidSigmoidPreprocessor preprocessor = new SigmoidSigmoidPreprocessor();
        preprocessor.preprocess();
        _stocks = preprocessor.dateMap();
        
        for ( Date dateKey : _stocks.keySet() ) 
        {
            for (StockPoint stock : _stocks.get(dateKey))
            {
                //System.out.println(dateKey + " - GroupID: " + stock.getpGroup_number());
            }
        }
        
        /*for ( Date dateKey : _groups.keySet() ) 
        {
            for (ClusterGroup group : _groups.get(dateKey))
            {
                //System.out.println(dateKey + " - GroupID: " + group.getId());
            }
        }*/
        // Initialize the class
        //DataImport dataImport = new DataImport();
        
        // StockPoints of anomalies.
        // Needs a re-work to allow different stock items. ********************
        
        Anomalies _anomalies = new Anomalies();
        _anomalies.addAnomalyType(Anomalies.Type.Jaccard);
        //_anomalies.addAnomalyType(Anomalies.Type.ARMA);
        
        
        //define Jaccard service
        //Jaccard j = new Jaccard();
        JaccardIndex jaccard = new JaccardIndex(k, 8);
        jaccard.calculate(preprocessor);
        //Constructing data structure of dayGroupListMap before do anything
        //clusterDS.BuildDataStructure("2016-12-28", "2016-12-31");

        //Define a time Window string array
        //String[] timeWindows = new String[] {"2016-12-28","2016-12-29","2016-12-30"};

        //This is an example about how to get Jaccard Index coefficient:
        //Get a stock(Symbol is GHK) point in specific time window 
        /*StockPoint sp1 = clusterDS.getStockPointByDateAndSymbol("2016-12-28", "CHK");
        StockPoint sp2 = clusterDS.getStockPointByDateAndSymbol("2016-12-29", "CHK");
        StockPoint sp3 = clusterDS.getStockPointByDateAndSymbol("2016-12-30", "CHK");*/
        

        //using Jaccard Service to calculate Jaccard Index coefficient
        /*double d1 = j.coefficient(sp1, timeWindows);
        double d2 = j.coefficient(sp2, timeWindows);
        double d3 = j.coefficient(sp3, timeWindows);*/
        
        /*

        System.out.println("The Jaccard Index coefficient of the Stock CHK "
                        + "which is in timewindow(2016-12-28) between '2016-12-28' and '2016-12-30' "
                        + "is " + d1);
        /*System.out.println("The Jaccard Index coefficient of the Stock CHK "
                        + "which is in timewindow(2016-12-29) between '2016-12-28' and '2016-12-30' "
                        + "is " + d2);
        System.out.println("The Jaccard Index coefficient of the Stock CHK "
                        + "which is in timewindow(2016-12-30) between '2016-12-28' and '2016-12-30' "
                        + "is " + d3);*9*8*/
    }
}