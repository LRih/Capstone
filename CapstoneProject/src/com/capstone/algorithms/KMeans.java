/***********************************************************************
 * Semester 1 2017 
 * COSC2408_1710 (Programming Project 1)
 * Full Name        : Kaizhi.Zhuang
 * Student Number   : s3535252
 * Course Code      : COSC2408
 * Create Date      : March 2017
 * This class implements the K-Means algrithim
 * This is provided by Kaizhi.Zhuang 
 **********************************************************************/

package com.capstone.algorithms;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.capstone.dataService.DataService;
import com.capstone.entities.ClusterGroup;
import com.capstone.entities.StockPoint;
import com.capstone.utils.DataConstant;

/**
 * @author Jason.Zhuang
 * @studentId s3535252
 * Mar 25, 2017
 * KMeans.java
 * Describe: This class implements the K-Means algrithim
 */
public class KMeans
{
	/**
	 *  all stock points
	 */
	private static List<StockPoint> points = new ArrayList<StockPoint>();

	/**
	 * cluster groups
	 */
	private static List<ClusterGroup> groups = new ArrayList<ClusterGroup>();;

	/**
	 *  Initializes the process
	 */
	public void init()
	{
		System.out.println("!!! initialise points and Group n !!!");
		// init stock Points
		points.clear();
		points = DataService.getStockData();

		// init Cluster Group
		groups.clear();
		for (int i = 0; i < DataConstant.NUM_GROUPS; i++)
		{
			ClusterGroup group = new ClusterGroup(i);

			// initialise the central point
			StockPoint centroid = StockPoint.createRandomPoint(DataConstant.MIN_COORDINATE,DataConstant.MAX_COORDINATE);
			
			centroid.setpGroup_number(i);
			
			group.setCentroid(centroid);

			group.setPoints(new ArrayList<StockPoint>());

			groups.add(group);
			
			//display Group information
			plotClusterGroup(group);
		}
		
		System.out.println("===============================================");

	}

	/**
	 * The process to calculate the K Means, with iterating method.
	 */
	public void calculate()
	{
		boolean finish = false;
		double distance_mini = 0;
		double distance_temp = 0;
		ArrayList<StockPoint> oldCentroids = new ArrayList<StockPoint>();
		int i_loop = 0;
		while (!finish)
		{
			for (int i = 0; i < DataConstant.NUM_GROUPS; i++)
			{
				groups.get(i).getPoints().clear();;
			}
			
			for (Iterator<StockPoint> itP = points.iterator(); itP.hasNext();)
			{
				StockPoint p = (StockPoint) itP.next();
				distance_mini = StockPoint.distance(p, groups.get(0).getCentroid());
				p.setpGroup_number(0);

				for (Iterator<ClusterGroup> itG = groups.iterator(); itG.hasNext();)
				{
					ClusterGroup group = (ClusterGroup) itG.next();
					distance_temp = StockPoint.distance(p, group.getCentroid());
					
					if (distance_temp < distance_mini)
					{
						p.setpGroup_number(group.getId());
						distance_mini = distance_temp;
					}
				}
				ClusterGroup g = groups.get(p.getpGroup_number());
				g.addPoint(p);
			}

			// save old central point
			oldCentroids.clear();
			for (Iterator<ClusterGroup> itg = groups.iterator(); itg.hasNext();)
			{
				ClusterGroup cg = (ClusterGroup) itg.next();
				double x,y;
				x = cg.getCentroid().getpX();
				y = cg.getCentroid().getpY();
				StockPoint old_central = new StockPoint(x, y);
				oldCentroids.add(old_central);
			}

			// calculate new central points
			calculateNewCentroids();

			// compare all central point
			finish = true;
			for (int i = 0; i < DataConstant.NUM_GROUPS; i++)
			{
				ClusterGroup cg = groups.get(i);
				StockPoint point_new = cg.getCentroid();
				StockPoint point_old = oldCentroids.get(i);
				if (StockPoint.distance(point_new, point_old) != 0)
				{
					finish = false;
					break;
				}
			}
			
			System.out.println("this is :"+i_loop+" loop:");
			for (int i = 0; i < DataConstant.NUM_GROUPS; i++)
			{
				ClusterGroup cg = groups.get(i);
				plotClusterGroup(cg);
			}
			
			if (i_loop>=5000)
			{
				finish = true;
			}
			
			i_loop ++;
			
			
		}
	}

	/**
	 * reCalculate a new Centroid
	 */
	private void calculateNewCentroids()
	{
		for (ClusterGroup g : groups)
		{
			double sumX = 0;
			double sumY = 0;
			List<StockPoint> list = g.getPoints();
			int num_points = list.size();

			for (StockPoint point : list)
			{
				sumX += point.getpX();
				sumY += point.getpY();
			}

			StockPoint centroid = g.getCentroid();
			if (num_points > 0)
			{
				double newX = sumX / num_points;
				double newY = sumY / num_points;
				BigDecimal bgX = new BigDecimal(newX).setScale(2, RoundingMode.UP);
				BigDecimal bgY = new BigDecimal(newY).setScale(2, RoundingMode.UP);
				centroid.setpX(bgX.doubleValue());
				centroid.setpY(bgY.doubleValue());
			}
		}
	}

	/**
	 * display the detail of a cluster group
	 * @param cg
	 */
	public void plotClusterGroup(ClusterGroup cg) 
	{
		System.out.print("[ClusterGroupNo: " + cg.getId()+"]");
		System.out.println("[Centroid: X=" + cg.getCentroid().getpX()+",Y="+cg.getCentroid().getpY() + "]");
	}
	
	public static List<StockPoint> getPoints()
	{
		return points;
	}

	public static void setPoints(List<StockPoint> points)
	{
		KMeans.points = points;
	}

	public static void setGroups(List<ClusterGroup> groups)
	{
		KMeans.groups = groups;
	}

	public List<ClusterGroup> getGroups()
	{
		return groups;
	}
}