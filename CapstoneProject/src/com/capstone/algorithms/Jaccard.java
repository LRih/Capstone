//package com.capstone.algorithms;
//
//import java.text.SimpleDateFormat;
//import java.util.List;
//
//import com.capstone.entities.StockPoint;
//import com.capstone.hiddenAnomalyBusiness.ClusterDataService;
//
///**
// * @author Jason.Zhuang
// * @studentId s3535252 24/04/2017 Jaccard.java
// * Describe: computing Jaccard Index value
// */
//public class Jaccard
//{
//
//	/**
//	 * calculate the similarityValue of two groups
//	 *
//	 * @param g1
//	 * @param g2
//	 * @return
//	 */
//	private double similarityValue(final ClusterGroup g1, final ClusterGroup g2)
//	{
//		int sameCount = 0;
//		int unioCount = 0;
//		double retValue = 0d;
//
//		if (g1 == null)
//		{
//			throw new NullPointerException("g1 must not be null");
//		}
//
//		if (g2 == null)
//		{
//			throw new NullPointerException("g2 must not be null");
//		}
//
//		if (g1.equals(g2))
//		{
//			return 1d;
//		}
//
//		List<StockPoint> listA = g1.points;
//		List<StockPoint> listB = g2.points;
//
//		for (int i = 0; i < listA.size(); i++)
//		{
//			StockPoint spA = listA.get(i);
//			for (int j = 0; j < listB.size(); j++)
//			{
//				StockPoint spB = listB.get(j);
//				if (spA.sameStock(spB))
//				{
//					sameCount = sameCount + 1;
//				}
//			}
//		}
//
//		unioCount = listA.size() + listB.size() - sameCount;
//
//		if (unioCount != 0)
//		{
//			retValue = (double)sameCount/(double)unioCount;
//		} else
//		{
//			retValue = 0d;
//		}
//
//		return retValue;
//	}
//
//	/**
//	 * calculate the Jaccard index coefficient of a StockPoint in different
//	 * timewindows
//	 *
//	 * @param arg_sp
//	 *            it is the stock point which is in time window j. It could be
//	 *            the same stock ,but the returnRate and volume are different
//	 *            between different time window.
//	 * @param timeWindows
//	 * @return Jaccard index coefficient
//	 */
//	public double coefficient(StockPoint arg_sp, String[] timeWindows)
//	{
//		// the G_i group is the partition to which Si belongs at time window.
//		// G_ij means S(i) in the Cluster Group to which S(i) belongs at
//		// TimeWindow(j)
//		double retValue = 0;
//		int w = timeWindows.length;
//		if (w == 0)
//		{
//			return 0;
//		}
//
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//
//		String stcokDate = df.format(arg_sp.getListedDate());
//		String stockSymbol = arg_sp.getStockSymbol();
//		int groupId = arg_sp.getCluster();
//
//		ClusterDataService clusterDS = new ClusterDataService();
//
//		// get the Group of Si
//		ClusterGroup G_i = clusterDS.getClusterGroupByDayandId(stcokDate,groupId);
//
//		double d = 0;
//		for (int i = 0; i < w; i++)
//		{
//
//			ClusterGroup G_ij = clusterDS.getClusterGroupByDateAndSymbol(timeWindows[i], stockSymbol);
//			double sim = similarityValue(G_i, G_ij);
//			d = d + sim;
//		}
//
//		if (w > 0)
//		{
//			retValue = 1.0 * (d / w);
//		} else
//		{
//			retValue = 0;
//		}
//
//		return retValue;
//	}
//}