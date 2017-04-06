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

import com.capstone.hiddenAnomalyBusiness.ClusterDataService;

public class MainRun
{

	public static void main(String[] args)
	{
//		KMeans kmeans = new KMeans();
//		kmeans.init(3);
//		kmeans.calculate();
		
		//DataService ds = new DataService();
		//ds.loadAllStockData();
		//ds.loadDataByDay("29/12/2016");
		
		ClusterDataService clusterDS = new ClusterDataService();
		clusterDS.getGroupsByDay("2016-12-30");
		
//		MainRun r = new MainRun();
//		r.test();
		

	}
	
	public void test()
	{
		File f = new File(this.getClass().getResource("/").getPath()); 
		System.out.println(f.toString()); 
		
		File f1 = new File(this.getClass().getResource("").getPath()); 
		System.out.println(f1); 
		
		File directory = new File("");//参数为空 
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
