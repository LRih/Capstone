package com.capstone.hiddenAnomalyBusiness;

import java.util.ArrayList;
import java.util.List;

import com.capstone.algorithms.KMeans;
import com.capstone.entities.ClusterGroup;
import com.capstone.utils.DataConstant;

/**
 * @author Jason.Zhuang
 * @studentId s3535252
 * 31/03/2017
 * ClusterDataService.java
 * Describe: this class provide methods that can get data which are clustered
 * 
 */
public class ClusterDataService
{
	
	/**
	 * get cluster groups that every group contains the stock list
	 * after using k-means algorithm
	 * @param day yyyy-MM-dd
	 * @return
	 */
	public List<ClusterGroup> getGroupsByDay(String day)
	{
		List<ClusterGroup> groupList = new ArrayList<ClusterGroup>();
		KMeans kmeans = new KMeans();
		kmeans.init(DataConstant.NUM_GROUPS,day);
		kmeans.calculate();
		groupList = kmeans.getGroups();
		
//		for (Iterator<ClusterGroup> iterator = groupList.iterator(); iterator.hasNext();)
//		{
//			ArrayList<StockPoint> sl = new ArrayList<StockPoint>();
//			ClusterGroup g = (ClusterGroup) iterator.next();
//			System.out.println(g.id);
//			sl=(ArrayList<StockPoint>) g.getPoints();
//			System.out.println(sl.size());
//			
//		}
//		System.out.println("===========================");

		return groupList;
	}
	

}
