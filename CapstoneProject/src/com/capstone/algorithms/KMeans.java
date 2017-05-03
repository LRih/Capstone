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
 * Describe: This class implements the K-Means algrithim
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
        
        public KMeans (int k, HashMap<Date, List<StockPoint>> stocks)
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
        
        public void oldinit (int k, Date date, List<StockPoint> stocks)
        {
            /**
             * @param   k       k-value for k means calculation.
             * @param   date    Date of k-means calculation.
             * @param   stocks  All stocks for the date.
             */
            
            // Set K Value to class
            this.k = k;

            // Set stocks that are relevant to class
            this.points = stocks;
            
            // Pre-Process the data to normalize it.
            //new DataPreprocessService().preprocess();

            // Post-Process it so can be used directly.
            //LinearSigmoidPreprocessor preprocessor = new LinearSigmoidPreprocessor();
            /*SigmoidSigmoidPreprocessor preprocessor = new SigmoidSigmoidPreprocessor();
            preprocessor.preprocess();
            _stocks = preprocessor.dateMap();
            points = DataService.loadDataByDay(day);*/

            // init Cluster Group
            groups.clear();
            for (int i = 0; i < k; i++)
            {
                ClusterGroup group = new ClusterGroup(i);

                // initialise the central point for every group
                StockPoint centroid = StockPoint.createRandomPoint(DataConstant.MIN_COORDINATE,DataConstant.MAX_COORDINATE);

                centroid.setpGroup_number(i);

                group.setCentroid(centroid);

                group.setPoints(new ArrayList<StockPoint>());

                groups.add(group);

            }
        }
        
	/**
	 *  Initializes the process
         * Old and deprecate it
	 */
	public void oldinit(int k,String day)
	{
	    this.k = k;

		// init stock Points
		points.clear();
		
                
                Map<Date, List<StockPoint>> _stocks;

                // Pre-Process the data to normalize it.
                //new DataPreprocessService().preprocess();

                // Post-Process it so can be used directly.
                //LinearSigmoidPreprocessor preprocessor = new LinearSigmoidPreprocessor();
                SigmoidSigmoidPreprocessor preprocessor = new SigmoidSigmoidPreprocessor();
                preprocessor.preprocess();
                _stocks = preprocessor.dateMap();
		points = DataService.loadDataByDay(day);

		// init Cluster Group
		groups.clear();
		for (int i = 0; i < k; i++)
		{
			ClusterGroup group = new ClusterGroup(i);

			// initialise the central point for every group
			StockPoint centroid = StockPoint.createRandomPoint(DataConstant.MIN_COORDINATE,DataConstant.MAX_COORDINATE);
			
			centroid.setpGroup_number(i);
			
			group.setCentroid(centroid);

			group.setPoints(new ArrayList<StockPoint>());

			groups.add(group);
			
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
            
            for (Date dateKey : _groups.keySet()) 
            {
                groups = _groups.get(dateKey);
                //for (ClusterGroup groups : _groups.get(dateKey))
                {
                    ArrayList<StockPoint> oldCentroids = new ArrayList<StockPoint>();
                    int i_loop = 0;

                    while (!finish)
                    {
                        for (int i = 0; i < k; i++)
                        {
                                groups.get(i).getPoints().clear();
                        }


                        //Iterator all the points
                        for (Iterator<StockPoint> itP = points.iterator(); itP.hasNext();)
                        {
                            StockPoint p = (StockPoint) itP.next();
                            //calculate the distance between p and the first group center point
                            distance_mini = StockPoint.distance(p, groups.get(0).getCentroid());
                            p.setpGroup_number(0);

                            for (Iterator<ClusterGroup> itG = groups.iterator(); itG.hasNext();)
                            {
                                ClusterGroup group = itG.next();
                                distance_temp = StockPoint.distance(p, group.getCentroid());

                                if (distance_temp < distance_mini)
                                {
                                        p.setpGroup_number(group.getId());
                                        distance_mini = distance_temp;
                                }
                            }
                            groups.get(p.getpGroup_number()).addPoint(p);
                        }

                            // save old central point
                            oldCentroids.clear();
                            for (Iterator<ClusterGroup> itg = groups.iterator(); itg.hasNext();)
                            {
                                    ClusterGroup cg = itg.next();
                                    double x,y;
                                    x = cg.getCentroid().getRateOfReturn();
                                    y = cg.getCentroid().getVolume();
                                    StockPoint old_central = new StockPoint(x, y);
                                    oldCentroids.add(old_central);
                            }

                            // calculate new central points
                            groups = calculateNewCentroids(groups);

                            // compare all central point
                            finish = true;
                            for (int i = 0; i < k; i++)
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
	 * The process to calculate the K Means, with iterating method.
	 */
	public void oldcalculate()
	{
		boolean finish = false;
		double distance_mini = 0;
		double distance_temp = 0;
		ArrayList<StockPoint> oldCentroids = new ArrayList<StockPoint>();
		int i_loop = 0;
		while (!finish)
		{
			for (int i = 0; i < k; i++)
			{
				groups.get(i).getPoints().clear();
			}
			//Iterator all the points
			for (Iterator<StockPoint> itP = points.iterator(); itP.hasNext();)
			{
				StockPoint p = (StockPoint) itP.next();
				//calculate the distance between p and the first group center point
				distance_mini = StockPoint.distance(p, groups.get(0).getCentroid());
				p.setpGroup_number(0);

				for (Iterator<ClusterGroup> itG = groups.iterator(); itG.hasNext();)
				{
					ClusterGroup group = itG.next();
					distance_temp = StockPoint.distance(p, group.getCentroid());
					
					if (distance_temp < distance_mini)
					{
						p.setpGroup_number(group.getId());
						distance_mini = distance_temp;
					}
				}
				groups.get(p.getpGroup_number()).addPoint(p);
			}

			// save old central point
			oldCentroids.clear();
			for (Iterator<ClusterGroup> itg = groups.iterator(); itg.hasNext();)
			{
				ClusterGroup cg = itg.next();
				double x,y;
				x = cg.getCentroid().getRateOfReturn();
				y = cg.getCentroid().getVolume();
				StockPoint old_central = new StockPoint(x, y);
				oldCentroids.add(old_central);
			}

			// calculate new central points
			calculateNewCentroids();

			// compare all central point
			finish = true;
			for (int i = 0; i < k; i++)
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
			
			if (i_loop>=DataConstant.MAX_LOOP)
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
	}
        
        /**
	 * reCalculate a new Centroid
	 */
	private List<ClusterGroup> calculateNewCentroids(List<ClusterGroup> groups)
	{
		for (ClusterGroup g : groups)
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
