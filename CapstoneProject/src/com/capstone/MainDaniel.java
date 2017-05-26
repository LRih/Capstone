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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainDaniel
{
    public static void main(String[] args)
    {
        fullRun(4, 2);
    }

    private static void fullRun(int k, double stdDev)
    {
        Map<String, List<StockPoint>> _stocks;

        // Post-Process it so can be used directly.
        SigmoidSigmoidPreprocessor preprocessor = new SigmoidSigmoidPreprocessor();
        preprocessor.preprocess();

        // load stocks by means... optimize later
        _stocks = preprocessor.nameMap();

        // StockPoints of anomalies.
        List<StockPoint> anomalies;

        Anomalies _anomalies = new Anomalies();
        _anomalies.addAnomalyType(Anomalies.Type.STDDEV);
        _anomalies.addAnomalyType(Anomalies.Type.ARMA);
        _anomalies.addAnomalyType(Anomalies.Type.Jaccard);


        // Jaccard Index
        JaccardIndex jaccard = new JaccardIndex(k, 8);
        jaccard.calculate(preprocessor);
        jaccard.writeToSingleFile();

        // Runs each stock in a bubble for time-series detection.
        for (String key : _stocks.keySet())
        {

            // Creates a new stock unit for each time-series
            Stock unitStock = new Stock();

            // Adds all stock that are relevant to stock class.
            for (int i = 0; i < _stocks.get(key).size(); i++)
            {
                unitStock.addStock(_stocks.get(key).get(i));
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


        SearchDataCSV outputCSV = new SearchDataCSV(new File("output.searches.csv"), _stocks);
        outputCSV.outputToFile(searchStocks, _anomalies);
        //http://search.ebscohost.com.ezproxy.lib.rmit.edu.au/login.aspx?direct=true&db=bwh&db=bth&db=ufh&bquery=Willis+Towers+Watson&type=1&site=ehost-live&scope=site
    }

    private static void runJaccard(int k, double stdDev)
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
