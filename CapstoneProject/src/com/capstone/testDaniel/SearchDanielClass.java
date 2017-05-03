/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.testDaniel;

import com.capstone.algorithms.Jaccard;
import com.capstone.algorithms.KMeans;
import com.capstone.dataService.SigmoidSigmoidPreprocessor;
import com.capstone.entities.Anomalies;
import com.capstone.entities.SearchStocks;
import com.capstone.entities.StockPoint;
import com.capstone.utils.SearchDataImport;
import java.util.LinkedList;
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
        
        
        // Post-Process it so can be used directly.
        SigmoidSigmoidPreprocessor preprocessor = new SigmoidSigmoidPreprocessor();
        preprocessor.preprocess();
        _stocks = preprocessor.dateMap();
        
        // Initialize the class
        //DataImport dataImport = new DataImport();
        
        // StockPoints of anomalies.
        // Needs a re-work to allow different stock items. ********************
        LinkedList<StockPoint> anomalies;
        
        Anomalies _anomalies = new Anomalies();
        _anomalies.addAnomalyType(Anomalies.Type.Jaccard);
        _anomalies.addAnomalyType(Anomalies.Type.ARMA);
        
        
        //define Jaccard service
        Jaccard j = new Jaccard();
        
        // Kmeans stuff
        //KMeans kMeans = new kMeans();
    }
}
