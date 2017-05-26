package com.capstone;

import com.capstone.algorithms.JaccardIndex;
import com.capstone.algorithms.TimeARMA;
import com.capstone.algorithms.TimeStdDev;
import com.capstone.data.SigmoidSigmoidPreprocessor;
import com.capstone.entities.Anomalies;
import com.capstone.entities.SearchStocks;
import com.capstone.entities.Stock;
import com.capstone.entities.StockPoint;
import com.capstone.utils.CPanel;
import com.capstone.utils.SearchDataCSV;
import com.capstone.utils.SearchDataImport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * The main entry point into the app. The main function calls fullRun which performs
 * the following processes:
 *
 *   1. Loads all raw data and performs preprocessing as described in the
 *      accompanying report.
 *   2. Resulting data is clustered using K-means with specified k.
 *   3. Jaccard index is computed for each stock and outputted into the files:
 *        - jaccard.csv:           jaccard index for all data
 *        - jaccard.anomalies.csv: list of anomalies obtained from jaccard index
 *        - jaccard/*.csv:         jaccard index organized into stocks
 *   4. Anomalies based ARMA, StdDev and Jaccard are collected using a
 *      specified standard deviation as the threshold.
 *   5. Anomalies are outputted into the file anomalies.csv.
 */
public class Main
{
    public static void main(String[] args)
    {
        try
        {
            fullRun(4, 3);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void fullRun(int k, double stdDev) throws Exception
    {
        // Perform data preprocessing
        SigmoidSigmoidPreprocessor preprocessor = new SigmoidSigmoidPreprocessor();
        preprocessor.preprocess();

        // StockPoints of anomalies.
        List<StockPoint> anomalies;

        Anomalies _anomalies = new Anomalies();
        _anomalies.addAnomalyType(Anomalies.Type.STDDEV);
        _anomalies.addAnomalyType(Anomalies.Type.ARMA);
        _anomalies.addAnomalyType(Anomalies.Type.Jaccard);


        // Jaccard Index
        JaccardIndex jaccard = new JaccardIndex(k, 8);
        jaccard.calculate(preprocessor);

        jaccard.writeToSingleFile(); // write values to single file sorted by jaccard index
        jaccard.writeToIndividualFiles(); // write values to one file per stock sorted by date
        jaccard.writeAnomaliesFile(stdDev); // write anomalies to file calcuated per stock

        // Runs each stock in a bubble for time-series detection.
        for (String key : preprocessor.nameMap().keySet())
        {
            // Creates a new stock unit for each time-series
            Stock unitStock = new Stock();

            // Adds all stock that are relevant to stock class.
            for (int i = 0; i < preprocessor.nameMap().get(key).size(); i++)
            {
                unitStock.addStock(preprocessor.nameMap().get(key).get(i));
            }

            // Runs each time-series algorithm to find anomalies.

            TimeStdDev algorithmStdDev = new TimeStdDev(unitStock);
            algorithmStdDev.setCoEfficents(stdDev);
            anomalies = algorithmStdDev.findAnomalies();
            for (StockPoint stockPoint : anomalies)
            {
                _anomalies.addAnomaly(Anomalies.Type.STDDEV, stockPoint);
            }
            
            TimeARMA algorithmARMA = new TimeARMA(unitStock);
            algorithmARMA.setPValue(2);
            algorithmARMA.setQValue(3);
            algorithmARMA.setCoEfficents(stdDev);

            anomalies = algorithmARMA.findAnomalies();
            for (StockPoint stockPoint : anomalies)
            {
                _anomalies.addAnomaly(Anomalies.Type.ARMA, stockPoint);
            }
        }

        // jaccard index anomalies
        anomalies = jaccard.getAnomalies(stdDev);
        for (StockPoint stockPoint : anomalies)
            _anomalies.addAnomaly(Anomalies.Type.Jaccard, stockPoint);


        // Outputs to file
        _anomalies.outputToFile("anomalies.csv");

        // Search Functionality
        SearchDataImport data = new SearchDataImport();
        data.setSearchFolder(".\\searchData");
        SearchStocks searchStocks = data.importData();


        SearchDataCSV outputCSV = new SearchDataCSV(new File("output.searches.csv"), preprocessor.nameMap());
        outputCSV.outputToFile(searchStocks, _anomalies);
        //http://search.ebscohost.com.ezproxy.lib.rmit.edu.au/login.aspx?direct=true&db=bwh&db=bth&db=ufh&bquery=Willis+Towers+Watson&type=1&site=ehost-live&scope=site
    }

    private static void runJaccard(int k, double stdDev) throws IOException
    {
        // preprocess data
        final SigmoidSigmoidPreprocessor preprocessor = new SigmoidSigmoidPreprocessor();
        preprocessor.preprocess();

        // calculate jaccard index in specified date range
        final JaccardIndex index = new JaccardIndex(k, 8);
        index.calculate(preprocessor, "2010-01-05", "2011-01-01");

        index.writeToSingleFile(); // write values to single file sorted by jaccard index
        index.writeToIndividualFiles(); // write values to one file per stock sorted by date
        index.writeAnomaliesFile(stdDev); // write anomalies to file calcuated per stock

        final Iterator<Date> it = preprocessor.dateMap().keySet().iterator();

        final CPanel cPanel = new CPanel();

        final JButton btn = new JButton("New");
        btn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Date date = it.next();

                cPanel.setClusterer(index.getClustering(date));
                btn.setText(date.toString());
            }
        });

        cPanel.setLayout(new BorderLayout());
        cPanel.add(btn, BorderLayout.SOUTH);


        // window
        JFrame frame = new JFrame("Clustering");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(cPanel);
        frame.add(btn, BorderLayout.SOUTH);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
