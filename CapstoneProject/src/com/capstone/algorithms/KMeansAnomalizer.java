package com.capstone.algorithms;


import com.capstone.entities.StockPoint;
import com.capstone.utils.MathUtils;

import java.util.*;

public class KMeansAnomalizer
{
    private final int _k;

    private Map<Date, List<StockPoint>> _data;
    private List<Cluster> _clusters = new ArrayList<Cluster>();
    private List<ClusterPoint> _clusterPts = new ArrayList<ClusterPoint>();


    public KMeansAnomalizer(int k)
    {
        if (k <= 0)
            throw new RuntimeException("k must be greater than 0");

        _k = k;
    }


    public final List<StockPoint> generateAnomalies(Map<Date, List<StockPoint>> data, double topPercent)
    {
        List<StockPoint> anomalies = new ArrayList<StockPoint>();

        _data = data;
        _clusterPts.clear();

        initClusters();

        System.out.println("Generating clustering anomalies ... ");

        for (Date date : _data.keySet())
        {
            // perform k-means clustering
            do
            {
                for (Cluster cluster : _clusters)
                    cluster.data.clear();

                for (StockPoint pt : _data.get(date))
                    assignPointToNearestCluster(pt);

            } while (updateClusters()); // centers changed

            // calculate distances to center for each stock point
            for (StockPoint pt : _data.get(date))
            {
                int cluster = pt.getCluster();
                double dist = Math.sqrt(
                    Math.pow(pt.getX() - _clusters.get(cluster).centerX, 2) +
                    Math.pow(pt.getY() - _clusters.get(cluster).centerY, 2)
                );

                _clusterPts.add(new ClusterPoint(pt, dist));
            }
        }

        Collections.sort(_clusterPts, new ClusterPointComparator());
        int max = (int)(_clusterPts.size() * topPercent);

        for (int i = 0; i < max; i++)
        {
//            System.out.println(_clusterPts.get(i).pt.getStockSymbol() + ", " + _clusterPts.get(i).pt.getListedDate() + ", " + _clusterPts.get(i).dist);
            anomalies.add(_clusterPts.get(i).pt);
        }

        Collections.sort(anomalies, new DateSymbolComparator());

        return anomalies;
    }

    private void initClusters()
    {
        _clusters.clear();

        for (int i = 0; i < _k; i++)
        {
            Cluster cluster = new Cluster(MathUtils.rand(0, 1), MathUtils.rand(0, 1));
            _clusters.add(cluster);
        }
    }

    private void assignPointToNearestCluster(StockPoint pt)
    {
        int closestCenterIndex = getClosestClusterIndex(pt.getX(), pt.getY());

        pt.setCluster(closestCenterIndex);
        _clusters.get(closestCenterIndex).data.put(pt.getStockSymbol(), pt);
    }

    private boolean updateClusters()
    {
        boolean centersChanged = false;

        for (Cluster cluster : _clusters)
            if (cluster.updateCenter())
                centersChanged = true;

        return centersChanged;
    }

    private int getClosestClusterIndex(double x, double y)
    {
        double closestDist = 0;
        int closestCenterIndex = -1;

        for (int i = 0; i < _clusters.size(); i++)
        {
            double dist = Math.pow(x - _clusters.get(i).centerX, 2) + Math.pow(y - _clusters.get(i).centerY, 2);

            // assign first checked center as closest
            // otherwise check if new center is closer, update best distance if so
            if (closestCenterIndex == -1 || dist < closestDist)
            {
                closestDist = dist;
                closestCenterIndex = i;
            }
        }

        return closestCenterIndex;
    }


    private double calcStdDev()
    {
        double stdDev = Math.sqrt(calcSampleVariance());

        System.out.println("Distance stdDev: " + stdDev);
        return stdDev;
    }
    private double calcSampleVariance()
    {
        double mean = calcMean();
        double variance = 0;

        for (ClusterPoint s : _clusterPts)
            variance += Math.pow(s.dist - mean, 2);

        return variance / (_clusterPts.size() - 1);
    }
    private double calcMean()
    {
        double mean = 0;

        for (ClusterPoint s : _clusterPts)
            mean += s.dist;

        mean /= _clusterPts.size();

        System.out.println("Distance mean: " + mean);
        return mean;
    }


    private final class Cluster
    {
        public double centerX;
        public double centerY;

        public Map<String, StockPoint> data = new HashMap<String, StockPoint>();


        public Cluster(double centerX, double centerY)
        {
            this.centerX = centerX;
            this.centerY = centerY;
        }


        public final boolean updateCenter()
        {
            // no data in cluster
            if (data.size() == 0)
                return false;

            double oldX = centerX;
            double oldY = centerY;

            centerX = 0;
            centerY = 0;

            for (StockPoint pt : data.values())
            {
                centerX += pt.getX();
                centerY += pt.getY();
            }

            centerX = centerX / data.size();
            centerY = centerY / data.size();

            return centerX != oldX || centerY != oldY;
        }
    }
    private final class ClusterPoint
    {
        public final StockPoint pt;
        public final double dist;

        public ClusterPoint(StockPoint pt, double dist)
        {
            this.pt = pt;
            this.dist = dist;
        }
    }

    private static final class DateSymbolComparator implements Comparator<StockPoint>
    {
        public final int compare(StockPoint pt1, StockPoint pt2)
        {
            int dateCmp = pt1.getListedDate().compareTo(pt2.getListedDate());
            if (dateCmp != 0)
                return dateCmp;

            return pt1.getStockSymbol().compareTo(pt2.getStockSymbol());
        }
    }
    private static final class ClusterPointComparator implements Comparator<ClusterPoint>
    {
        public final int compare(ClusterPoint pt1, ClusterPoint pt2)
        {
            if (pt1.dist < pt2.dist) return 1;
            else if (pt1.dist > pt2.dist) return -1;
            return 0;
        }
    }
}
