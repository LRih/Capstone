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

import java.util.List;

import com.capstone.entities.StockPoint;
import com.capstone.utils.DataConstant;

/**
 * @author Jason.Zhuang
 * @studentId s3535252
 * Mar 25, 2017
 * DataService.java
 * Describe: This class implements some methods which can provide data Service
 */
public class DataService
{

	/**
	 * load stock data
	 * @return
	 */
	public static List<StockPoint> getStockData()
	{
		return StockPoint.createRandomPoints(DataConstant.MIN_COORDINATE,
				DataConstant.MAX_COORDINATE, DataConstant.NUM_POINTS);
	}

}
