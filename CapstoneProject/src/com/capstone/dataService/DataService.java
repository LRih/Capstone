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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.capstone.entities.StockPoint;
import com.capstone.utils.DataConstant;
import com.csvreader.CsvReader;

/**
 * @author Jason.Zhuang
 * @studentId s3535252
 * @date Mar 25, 2017
 * @filename DataService.java
 * @Describe: This class implements some methods which can provide data Service
 */
public class DataService
{
	
	public static ArrayList<StockPoint> stockList = new ArrayList<StockPoint>();
	
	/**
	 * load all stock data from prices.csv files
	 * 
	 * @return
	 */
	public static List<StockPoint> loadAllStockData()
	{
		//reference: https://sourceforge.net/projects/javacsv/files/
		File f = new File(DataService.class.getResource("/").getPath()); 
		//System.out.println(f.toString()); 
		String myClassPath = f.toString();
		stockList.clear();
		CsvReader r;
		SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormatter2 = new SimpleDateFormat("dd/MM/yyyy");
		Date stockDate = new Date();
		String strStockDate;
		try
		{
			r = new CsvReader(myClassPath + "\\prices.csv", ',', Charset.forName("GBK"));
			r.readHeaders();
			// Rate of Return (x value) = Highest Transaction - Lowest
			// Transaction
			// Volume (y value).
			while (r.readRecord())
			{
				StockPoint p = new StockPoint();
				//convert the stock date format to yyyy-MM-dd
				stockDate = dateFormatter2.parse(r.get("date"));
				strStockDate = dateFormatter1.format(stockDate);
				p.setListedDate(strStockDate);//the format is yyyy-MM-dd
				
				p.setStockSymbol(r.get("symbol"));
				p.setPriceOpen(Double.parseDouble(r.get("open")));
				p.setPriceClose(Double.parseDouble(r.get("close")));
				p.setPriceHigh(Double.parseDouble(r.get("high")));
				p.setPriceLow(Double.parseDouble(r.get("low")));
				//Rate of Return (x value) = Highest Transaction - Lowest
				p.calculateRateOfReturn();
			    //Volume (y value).
				p.setVolume(Double.parseDouble(r.get("volume")));
				stockList.add(p);
			}
			r.close();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stockList;
	}
	
	/**
	 * load a day data
	 * @param stringDate yyyy-MM-dd
	 */
	public static List<StockPoint> loadDataByDay(String argDate)
	{
		File f = new File(DataService.class.getResource("/").getPath()); 
		//System.out.println(f.toString()); 
		String myClassPath = f.toString();
		stockList.clear();
		CsvReader r;
		SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormatter2 = new SimpleDateFormat("dd/MM/yyyy");
		Date selectDate = new Date();
		Date stockDate = new Date();
		String strStockDate;
		try
		{
			selectDate = dateFormatter1.parse(argDate);
		} catch (ParseException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//the argument format yyyy-MM-dd
		String strSelectDate=dateFormatter1.format(selectDate);
		try
		{
			r = new CsvReader(myClassPath+"\\prices.csv", ',', Charset.forName("GBK"));
			r.readHeaders();
			// Rate of Return (x value) = Highest Transaction - Lowest
			// Transaction
			// Volume (y value).
			while (r.readRecord())
			{
				StockPoint p = new StockPoint();
				strStockDate = r.get("date");
				stockDate    = dateFormatter2.parse(strStockDate);
				strStockDate = dateFormatter1.format(stockDate);
				if (!strStockDate.equals(strSelectDate))
				{
					continue;
				}
				p.setListedDate(strStockDate+" 00:00:00");
				p.setStockSymbol(r.get("symbol"));
				p.setPriceOpen(Double.parseDouble(r.get("open")));
				p.setPriceClose(Double.parseDouble(r.get("close")));
				p.setPriceHigh(Double.parseDouble(r.get("high")));
				p.setPriceLow(Double.parseDouble(r.get("low")));
				//Rate of Return (x value) = Highest Transaction - Lowest
				p.calculateRateOfReturn();
			    //Volume (y value).
				p.setVolume(Double.parseDouble(r.get("volume")));
				stockList.add(p);
			}
			r.close();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("--------------------------");
		
		return stockList;
	}
	
	public static List<StockPoint> getStockDataTest()
	{
		return StockPoint.createRandomPoints(DataConstant.MIN_COORDINATE,
				DataConstant.MAX_COORDINATE, DataConstant.NUM_POINTS);
	}
	

}
