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

//import com.capstone.dataService.DataService;
import com.capstone.dataService.SigmoidSigmoidPreprocessor;
import com.capstone.entities.ClusterGroup;
import com.capstone.entities.StockPoint;
import com.capstone.utils.DataConstant;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

/**
 * @author Jason.Zhuang
 * @studentId s3535252
 * Mar 25, 2017
 * KMeans.java
 * Describe: This class implements the K-Means algorithm
 */
public class KMeans
{
    /**
     * The number of clusters
     */
	private int k;
        
        // Deprecate once completed
	private List<StockPoint> points;
	private List<ClusterGroup> groups;
        
        private Map<Date, List<StockPoint>> _stocks;
        private Map<Date, List<ClusterGroup>> _groups;

        public KMeans ()
        {
            // Deprecate once completed
            this.points = new ArrayList<StockPoint>();
            this.groups = new ArrayList<ClusterGroup>();
                    
            this._stocks = new HashMap<Date, List<StockPoint>>();
            this._groups = new HashMap<Date, List<ClusterGroup>>();
            
            this.k = 1;
        }
        
        public KMeans (int k)
        {
            // Deprecate once completed
            this.points = new ArrayList<StockPoint>();
            this.groups = new ArrayList<ClusterGroup>();
            
            this._stocks = new HashMap<Date, List<StockPoint>>();
            this._groups = new HashMap<Date, List<ClusterGroup>>();
            this.k = k;
        }
        
        public KMeans (int k, Map<Date, List<StockPoint>> stocks)
        {
            // Deprecate once completed
            this.points = new ArrayList<StockPoint>();
            this.groups = new ArrayList<ClusterGroup>();
            
            this._stocks = stocks;
            this._groups = new HashMap<Date, List<ClusterGroup>>();
            this.k = k;
        }
        
        public void clusterGroups ()
        {
            /**
             * Clusters each day based off Kaizhi code. Re-written to work with the dataset by
             * Richard.
             */
            
            for (Date dateKey : _stocks.keySet()) 
            {
                for (StockPoint stock : _stocks.get(dateKey))
                {
                    if(!_groups.containsKey(dateKey))
                        _groups.put(dateKey, new ArrayList<ClusterGroup>());
                    
                    //groups = new ArrayList<ClusterGroup>();
                    
                    for (int i = 0; i < k; i++)
                    {
                        ClusterGroup group = new ClusterGroup(i);

                        // initialise the central point for every group
                        StockPoint centroid = StockPoint.createRandomPoint(DataConstant.MIN_COORDINATE,DataConstant.MAX_COORDINATE);

                        centroid.setpGroup_number(i);
                        group.setCentroid(centroid);
                        
                        group.setPoints(new ArrayList<StockPoint>());

                        _groups.get(dateKey).add(group);
                    }
                }
            }
        }

        public void calculate()
        {
            /**
             * The process to calculate the K Means, with iterating method.
             **/
            
            boolean finish = false;
            double distance_mini = 0;
            double distance_temp = 0;
            double x,y;
            
            ArrayList<StockPoint> oldCentroids = new ArrayList<StockPoint>();
            
            for (Date dateKey : _groups.keySet()) 
            {
                groups = _groups.get(dateKey);
                points = _stocks.get(dateKey);
                System.out.println(dateKey);
                //for (ClusterGroup groups : _groups.get(dateKey))
                {
                    finish = false;
                    distance_mini = 0;
                    distance_temp = 0;
            
                    //ArrayList<StockPoint> oldCentroids = new ArrayList<StockPoint>();
                    int i_loop = 0;

                    while (!finish)
                    {
                        for (int i = 0; i < k; i++)
                        {
                            _groups.get(dateKey).get(i).getPoints().clear();
                        }

                        for (StockPoint stock : _stocks.get(dateKey))
                        {
                            distance_mini = StockPoint.distance(stock, _groups.get(dateKey).get(0).getCentroid());
                            stock.setpGroup_number(0);
                            for (ClusterGroup group : _groups.get(dateKey))
                            {
                                distance_temp = StockPoint.distance(stock, group.getCentroid());

                                if (distance_temp < distance_mini)
                                {
                                        stock.setpGroup_number(group.getId());
                                        distance_mini = distance_temp;
                                }
                            }
                            _groups.get(dateKey).get(stock.getpGroup_number()).addPoint(stock);
                        }
                        
                        oldCentroids.clear();
                        for (ClusterGroup cg : _groups.get(dateKey))
                        {
                            x = cg.getCentroid().getRateOfReturn();
                            y = cg.getCentroid().getVolume();
                            StockPoint old_central = new StockPoint(x, y);
                            oldCentroids.add(old_central);
                            
                        }
                        

                        // calculate new central points
                        groups = calculateNewCentroids(_groups.get(dateKey));

                        // compare all central point
                        finish = true;
                        for (int i = 0; i < k; i++)
                        {
                                ClusterGroup cg = _groups.get(dateKey).get(i);
                                StockPoint point_new = cg.getCentroid();
                                StockPoint point_old = oldCentroids.get(i);
                                if (StockPoint.distance(point_new, point_old) != 0)
                                {
                                    finish = false;
                                    break;
                                }
                        }

                        if (i_loop>=DataConstant.MAX_LOOP)
                        {
                            finish = true;
                        }

                        i_loop ++;
                          
                    }
                }
                
            }
        }
        
        /**
	 * reCalculate a new Centroid
	 */
	private List<ClusterGroup> calculateNewCentroids(List<ClusterGroup> groups)
	{
		for(ClusterGroup g : groups)
		{
			double sumX = 0;
			double sumY = 0;
			List<StockPoint> list = g.getPoints();
			int num_points = list.size();

			for (StockPoint point : list)
			{
				sumX += point.getRateOfReturn();
				sumY += point.getVolume();
			}

			StockPoint centroid = g.getCentroid();
			if (num_points > 0)
			{
				double newX = sumX / num_points;
				double newY = sumY / num_points;
				BigDecimal bgX = new BigDecimal(newX).setScale(2, RoundingMode.UP);
				BigDecimal bgY = new BigDecimal(newY).setScale(2, RoundingMode.UP);
				centroid.setRateOfReturn(bgX.doubleValue());
				centroid.setVolume(bgY.doubleValue());
			}
		}
                
            return groups;
	}

	/**
	 * display the detail of a cluster group
	 * @param cg
	 */
	public void plotClusterGroup(ClusterGroup cg) 
	{
		System.out.print("[ClusterGroupNo: " + cg.getId()+"]");
		System.out.println("[Centroid: X=" + cg.getCentroid().getRateOfReturn()+",Y="+cg.getCentroid().getVolume() + "]");
	}
	
        // Old
	/*public List<StockPoint> getPoints()
	{
		return points;
	}

	public List<ClusterGroup> getGroups()
	{
		return groups;
	}*/
        
        public Map<Date, List<StockPoint>> getPoints()
	{
		return _stocks;
	}

	public Map<Date,List<ClusterGroup>> getGroups()
	{
		return _groups;
	}
}
