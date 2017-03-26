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
package com.capstone.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Jason.Zhuang
 * @studentId s3535252
 * Mar 25, 2017
 * ClusterGroup.java
 * Describe: This is a Stock class entity
 */
public class StockPoint
{
	private double pX = 0.0;
	
	private double pY = 0.0;
	
	private int    pGroup_number = 0;

  int flag_anomaly=0;
  
  
	public StockPoint(double pX, double pY) {
		super();
		this.pX = pX;
		this.pY = pY;
	}

	public StockPoint(double pX, double pY, int pGroup_number) {
		super();
		this.pX = pX;
		this.pY = pY;
		this.pGroup_number = pGroup_number;
	}

	public double getpX()
	{
		return pX;
	}

	public void setpX(double pX)
	{
		this.pX = pX;
	}

	public double getpY()
	{
		return pY;
	}

	public void setpY(double pY)
	{
		this.pY = pY;
	}

	public int getpGroup_number()
	{
		return pGroup_number;
	}

	public void setpGroup_number(int pGroup_number)
	{
		this.pGroup_number = pGroup_number;
	}
	
	/**
	 * Calculates the distance between two points.
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double distance(StockPoint p1, StockPoint p2)
	{
		double retValue = Math.sqrt(Math.pow((p2.getpY() - p1.getpY()), 2)
				+ Math.pow((p2.getpX() - p1.getpX()), 2));
		
		BigDecimal bg = new BigDecimal(retValue).setScale(2, RoundingMode.UP);
		
		return bg.doubleValue();
	}
	
	

	/**
	 * Creates random StockPoint
	 * @param min
	 * @param max
	 * @return
	 */
	public static StockPoint createRandomPoint(int min, int max)
	{
		Random r = new Random();
		double x = min + (max - min) * r.nextDouble();
		double y = min + (max - min) * r.nextDouble();
		BigDecimal bgx = new BigDecimal(x).setScale(2, RoundingMode.UP);
		BigDecimal bgy = new BigDecimal(y).setScale(2, RoundingMode.UP);
		return new StockPoint(bgx.doubleValue(), bgy.doubleValue());
	}

	/**
	 * @param min 
	 * @param max
	 * @param number how many records I want to create
	 * @return
	 */
	public static List<StockPoint> createRandomPoints(int min, int max, int number) 
	{
    	List<StockPoint> points = new ArrayList<StockPoint>(number);
    	for(int i = 0; i< number; i++) 
    	{
    		points.add(createRandomPoint(min,max));
    	}
    	return points;
    }

}
