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
package com.capstone;

import com.capstone.algorithms.KMeans;

public class MainRun
{

	public static void main(String[] args)
	{
		KMeans kmeans = new KMeans();
		kmeans.init(3);
		kmeans.calculate();

	}

}
