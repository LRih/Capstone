package com.capstone.testrichard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.capstone.algorithms.KMeans;
import com.capstone.algorithms.KMeansAnomalizer;
import com.capstone.dataService.SigmoidSigmoidPreprocessor;
import com.capstone.entities.StockPoint;

public class MainTest
{

	public static void main(String[] args)
	{
        testKmeansAnomalizer();
	}

	private static void testClustering()
	{
        // panels
        final CPanel cPanel = new CPanel();

        JButton btn = new JButton("New");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                KMeans kmeans = new KMeans();
                kmeans.init(4,"2016-12-30");
                kmeans.calculate();

                cPanel.setClusterer(kmeans);
            }
        });


        // window
        JFrame frame = new JFrame("Clustering");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(cPanel);
        frame.add(btn, BorderLayout.SOUTH);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
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
}
