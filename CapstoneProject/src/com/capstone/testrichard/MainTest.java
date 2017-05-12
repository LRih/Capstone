package com.capstone.testrichard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.capstone.algorithms.JI;
import com.capstone.algorithms.KMeans;
import com.capstone.algorithms.KMeansAnomalizer;
import com.capstone.algorithms.ThreadedJI;
import com.capstone.dataService.SigmoidSigmoidPreprocessor;
import com.capstone.entities.StockPoint;

public class MainTest
{

	public static void main(String[] args)
	{
        testJaccard();
	}

	private static void testPreprocess()
	{
		SigmoidSigmoidPreprocessor preprocessor = new SigmoidSigmoidPreprocessor();
        //LinearSigmoidPreprocessor preprocessor = new LinearSigmoidPreprocessor();
        preprocessor.preprocess();
    }

    private static void testKmeansAnomalizer()
    {
        // right now take an arbitrary K = 10
        int k = 10;

        // get top x% of pts, adjusted as needed
        double topPercent = 0.001; // 0.1%

        // preprocess data
        SigmoidSigmoidPreprocessor preprocessor = new SigmoidSigmoidPreprocessor();
        preprocessor.preprocess();

        // pass normalized data into anomalizer
        KMeansAnomalizer anomalizer = new KMeansAnomalizer(k);
        List<StockPoint> pt = anomalizer.generateAnomalies(preprocessor.dateMap(), topPercent);

        System.out.println("Anomalies count: " + pt.size());
    }

    private static void testJaccard()
    {
        int k = 4;
        double stdDev = 3;

        // preprocess data
        final SigmoidSigmoidPreprocessor preprocessor = new SigmoidSigmoidPreprocessor();
        preprocessor.preprocess();

        // calculate jaccard index in specified date range
        final ThreadedJI index = new ThreadedJI(k, 8);
//        final JI index = new JI(k);
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
