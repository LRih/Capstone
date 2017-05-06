//package com.capstone.hiddenAnomalyBusiness;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//
//import com.capstone.entities.StockPoint;
//import com.capstone.utils.DataConstant;
//
///**
// * @author Jason.Zhuang
// * @studentId s3535252
// * 31/03/2017
// * ClusterDataService.java
// * Describe: this class provide methods that can get data which are clustered
// *
// */
//public class ClusterDataService
//{
//
//	/**
//	 * to store ClusterGroup List of every day
//	 * the key is date, and the value is the list of group
//	 */
//	private static HashMap<String, List<ClusterGroup>> dayGroupListMap
//	              = new HashMap<String, List<ClusterGroup>>();
//
//
//	public static HashMap<String, List<ClusterGroup>> getDayGroupListMap()
//	{
//		return dayGroupListMap;
//	}
//
//	/**
//	 * this is the main Entry of calculate Cluster Group
//	 * between Start Date and End Date
//	 * @param startDate yyyy-MM-dd
//	 * @param endDate yyyy-MM-dd
//	 */
//	public void BuildDataStructure(String startDate, String endDate)
//	{
//		Date sDate   = new Date();
//		Date eDate   = new Date();
//		Date temDate = new Date();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		try
//		{
//			sDate = df.parse(startDate);
//			eDate = df.parse(endDate);
//		}catch (ParseException e1)
//		{
//			e1.printStackTrace();
//		}
//		temDate = sDate;
//
//		while(temDate.before(eDate))
//		{
//			String day = df.format(temDate);
//			List<ClusterGroup> tempList = getGroupsByDay(day);
//			dayGroupListMap.put(day, tempList);
//			temDate.setTime(temDate.getTime()+ (24 * 3600 *1000));
//		};
//
//	}
//
//	/**
//	 * get cluster groups that every group contains the stock list
//	 * after using k-means algorithm
//	 * @param day yyyy-MM-dd
//	 * @return
//	 */
//	public List<ClusterGroup> getGroupsByDay(String day)
//	{
//		List<ClusterGroup> groupList = new ArrayList<ClusterGroup>();
//
//		KMeans kmeans = new KMeans();
//		//initialize Datalist of specific day
//		kmeans.init(DataConstant.NUM_GROUPS,day);
//		//calculate them to different group
//		kmeans.calculate();
//		//get groupList
//		groupList = kmeans.getGroups();
//
//		return groupList;
//	}
//
//	/**
//	 * get a Cluster Group by a Time window and group id
//	 * @param day
//	 * @param groupId
//	 * @return ClusterGroup
//	 */
//	public ClusterGroup getClusterGroupByDayandId(String day,int groupId)
//	{
//		ClusterGroup retGroup = null;
//
//		if (dayGroupListMap.size() == 0)
//		{
//			retGroup = null;
//		}
//
//		List<ClusterGroup> groupList = dayGroupListMap.get(day);
//		if (groupList == null || groupList.size()==0)
//		{
//			retGroup = null;
//		}
//
//		for (Iterator<ClusterGroup> iterator = groupList.iterator(); iterator.hasNext();)
//		{
//			ClusterGroup clusterGroup = (ClusterGroup) iterator.next();
//			if (clusterGroup.getId()== groupId)
//			{
//				retGroup = clusterGroup;
//				break;
//			}
//		}
//		return retGroup;
//	}
//
//	/**
//	 * get Cluster Group by Date and stock Symbol
//	 * @param day yyyy-MM-dd
//	 * @param stockSymbol
//	 * @return
//	 */
//	public ClusterGroup getClusterGroupByDateAndSymbol(String day,String stockSymbol)
//	{
//		ClusterGroup retGroup = null;
//
//		if (dayGroupListMap.size() == 0)
//		{
//			retGroup = null;
//		}
//
//		List<ClusterGroup> groupList = dayGroupListMap.get(day);
//
//		if (groupList == null || groupList.size()==0)
//		{
//			retGroup = null;
//		}
//
//		boolean findFlag = false;
//		for (Iterator<ClusterGroup> iterator = groupList.iterator(); iterator.hasNext();)
//		{
//			if (findFlag)
//			{
//				break;
//			}
//			ClusterGroup clusterGroup = (ClusterGroup) iterator.next();
//			List<StockPoint> stockList = clusterGroup.getPoints();
//			for (Iterator<StockPoint> it2 = stockList.iterator(); it2.hasNext();)
//			{
//				StockPoint object = (StockPoint) it2.next();
//				if (object.getStockSymbol().equals(stockSymbol))
//				{
//					retGroup = clusterGroup;
//					findFlag = true;
//					break;
//				}
//			}
//		}
//
//		return retGroup;
//	}
//
//	/**
//	 * get a Stock by its Symbol in certain day
//	 * @param arg_date
//	 * @param stockSymbol
//	 * @return StockPoint class
//	 */
//	public StockPoint getStockPointByDateAndSymbol(String arg_date,String stockSymbol)
//	{
//		StockPoint retValue = new StockPoint();
//
//		if (dayGroupListMap.size() == 0)
//		{
//			return null;
//		}
//
//		boolean findFlag = false;
//		List<ClusterGroup> groupList = dayGroupListMap.get(arg_date);
//		for (Iterator<ClusterGroup> it = groupList.iterator(); it.hasNext();)
//		{
//			if (findFlag)
//			{
//				break;
//			}
//			ClusterGroup clusterGroup = (ClusterGroup) it.next();
//			List<StockPoint> lst = clusterGroup.getPoints();
//			for (Iterator<StockPoint> it2 = lst.iterator(); it2.hasNext();)
//			{
//				StockPoint stockPoint = (StockPoint) it2.next();
//				if (stockPoint.getStockSymbol().equals(stockSymbol))
//				{
//					retValue = stockPoint;
//					findFlag = true;
//					break;
//				}
//			}
//		}
//
//		return retValue;
//
//	}
//
//}
