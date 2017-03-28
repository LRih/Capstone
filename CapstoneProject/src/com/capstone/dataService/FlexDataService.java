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
package com.capstone.dataService;

import java.util.Iterator;
import java.util.List;

import com.capstone.algorithms.KMeans;
import com.capstone.entities.StockPoint;

import flex.messaging.io.ArrayCollection;
import flex.messaging.io.amf.ASObject;

/**
 * @author Jason.Zhuang
 * @studentId s3535252
 * Mar 25, 2017
 * FlexDataService.java
 * Describe: This class implements the data Service that supply Flex to display Graph
 */
public class FlexDataService
{
	KMeans k = new KMeans();

	/**
	 * Service for Flex to get all stock data points
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayCollection getPointsCollection()
	{
		ArrayCollection retCollection =	new ArrayCollection();
		k.init(3);
		k.calculate();
		List<StockPoint> points = KMeans.getPoints();
		for (Iterator<StockPoint> iterator = points.iterator(); iterator.hasNext();)
		{
			StockPoint stockPoint = (StockPoint) iterator.next();
			ASObject asObject = new ASObject();
			asObject.put("rr", stockPoint.getpX());
			asObject.put("vl", stockPoint.getpY());
			asObject.put("gp", stockPoint.getpGroup_number());
			retCollection.add(asObject);
		}

		return retCollection;
	}

	/**
	 * get the first group data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayCollection getKmeansPointsCollection1()
	{
		ArrayCollection retCollection =	new ArrayCollection();
		List<StockPoint> points = k.getGroups().get(0).getPoints();
		for (Iterator<StockPoint> iterator = points.iterator(); iterator.hasNext();)
		{
			StockPoint stockPoint = (StockPoint) iterator.next();
			ASObject asObject = new ASObject();

			asObject.put("rr", stockPoint.getpX());
			asObject.put("vl", stockPoint.getpY());
			asObject.put("gp", stockPoint.getpGroup_number());

			retCollection.add(asObject);
		}
		System.out.println("111111111111111111111111111111111111111111111");
		return retCollection;
	}

	/**
	 * get the second group data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayCollection getKmeansPointsCollection2()
	{
		ArrayCollection retCollection =	new ArrayCollection();
		List<StockPoint> points = k.getGroups().get(1).getPoints();
		for (Iterator<StockPoint> iterator = points.iterator(); iterator.hasNext();)
		{
			StockPoint stockPoint = (StockPoint) iterator.next();
			ASObject asObject = new ASObject();

			asObject.put("rr", stockPoint.getpX());
			asObject.put("vl", stockPoint.getpY());
			asObject.put("gp", stockPoint.getpGroup_number());
			retCollection.add(asObject);
		}
		System.out.println("22222222222222222222222222222222222222222222");
		return retCollection;
	}

	/**
	 * get the third group data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayCollection getKmeansPointsCollection3()
	{
		ArrayCollection retCollection =	new ArrayCollection();
		List<StockPoint> points = k.getGroups().get(2).getPoints();
		for (Iterator<StockPoint> iterator = points.iterator(); iterator.hasNext();)
		{
			StockPoint stockPoint = (StockPoint) iterator.next();
			ASObject asObject = new ASObject();

			asObject.put("rr", stockPoint.getpX());
			asObject.put("vl", stockPoint.getpY());
			asObject.put("gp", stockPoint.getpGroup_number());
			retCollection.add(asObject);
		}
		System.out.println("333333333333333333333333333333333333333333333");
		return retCollection;
	}
}
