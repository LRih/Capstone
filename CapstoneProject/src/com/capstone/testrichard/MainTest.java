/***********************************************************************
 * Semester 1 2017 
 * COSC2408_1710 (Programming Project 1)
 * Full Name        : Kaizhi.Zhuang
 * Student Number   : s3535252
 * Course Code      : COSC2408
 * Create Date      : March 2017
 * 
 * This is provided by Kaizhi.Zhuang 
 **********************************************************************/
package com.capstone.testrichard;

import com.capstone.algorithms.KMeans;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainTest
{

	public static void main(String[] args)
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

}
