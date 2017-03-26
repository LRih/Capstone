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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jason.Zhuang
 * @studentId s3535252
 * Mar 25, 2017
 * ClusterGroup.java
 * Describe: This is a Cluster Group Entity
 */
public class ClusterGroup
{
	/**
	 * the group id
	 */
	public int id;
	
	/**
	 * the central point coordinate
	 */
	public StockPoint centroid;
	
	/**
	 * the Stock Point List in this Group
	 */
	public List<StockPoint> points;
	

	/**
	 *  Creates a new ClusterGroup
	 * @param id
	 */
	public ClusterGroup(int id)
	{
		this.id = id;
		this.points = new ArrayList<StockPoint>();
		this.centroid = null;
	}
	
	public int getId()
	{
		return id;
	}
	
	public List<StockPoint> getPoints()
	{
		return points;
	}

	public void setPoints(List<StockPoint> points)
	{
		this.points = points;
	}
	
	public void addPoint(StockPoint point)
	{
		points.add(point);
	}
	
	public void delPoint(StockPoint point)
	{
		points.remove(point);
	}
	
	public StockPoint getCentroid()
	{
		return centroid;
	}

	public void setCentroid(StockPoint centroid)
	{
		this.centroid = centroid;
	}

	public void clear()
	{
		points.clear();
	}

	
}
