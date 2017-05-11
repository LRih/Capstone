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

import java.io.File;
import java.io.IOException;
import java.net.URL;

//import com.capstone.algorithms.Jaccard;
import com.capstone.entities.StockPoint;
//import com.capstone.hiddenAnomalyBusiness.ClusterDataService;

/*public class MainRun
{

	public static void main(String[] args)
	{
		//define Data Service of Clustering operation
		ClusterDataService clusterDS = new ClusterDataService();
		//define Jaccard service
		Jaccard j = new Jaccard();
		
		//Constructing data structure of dayGroupListMap before do anything
		clusterDS.BuildDataStructure("2016-12-28", "2016-12-31");
		
		//Define a time Window string array
		String[] timeWindows = new String[] {"2016-12-28","2016-12-29","2016-12-30"};
		
		//This is an example about how to get Jaccard Index coefficient:
		//Get a stock(Symbol is GHK) point in specific time window 
		StockPoint sp1 = clusterDS.getStockPointByDateAndSymbol("2016-12-28", "CHK");
		StockPoint sp2 = clusterDS.getStockPointByDateAndSymbol("2016-12-29", "CHK");
		StockPoint sp3 = clusterDS.getStockPointByDateAndSymbol("2016-12-30", "CHK");
		
		//using Jaccard Service to calculate Jaccard Index coefficient
		double d1 = j.coefficient(sp1, timeWindows);
		double d2 = j.coefficient(sp2, timeWindows);
		double d3 = j.coefficient(sp3, timeWindows);
		
		System.out.println("The Jaccard Index coefficient of the Stock CHK "
				+ "which is in timewindow(2016-12-28) between '2016-12-28' and '2016-12-30' "
				+ "is " + d1);
		System.out.println("The Jaccard Index coefficient of the Stock CHK "
				+ "which is in timewindow(2016-12-29) between '2016-12-28' and '2016-12-30' "
				+ "is " + d2);
		System.out.println("The Jaccard Index coefficient of the Stock CHK "
				+ "which is in timewindow(2016-12-30) between '2016-12-28' and '2016-12-30' "
				+ "is " + d3);
		
		System.exit(0);
		
	}
	
	public void test()
	{
		File f = new File(this.getClass().getResource("/").getPath()); 
		System.out.println(f.toString()); 
		
		File f1 = new File(this.getClass().getResource("").getPath()); 
		System.out.println(f1); 
		
		File directory = new File("");//argument is null
		String courseFile = "";
		try
		{
			courseFile = directory.getCanonicalPath();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println(courseFile); 
		
		
		URL xmlpath = this.getClass().getClassLoader().getResource("prices.csv"); 
		System.out.println(xmlpath); 
		
		System.out.println(System.getProperty("user.dir")); 
		
		System.out.println( System.getProperty("java.class.path")); 
		
	}

}
*/