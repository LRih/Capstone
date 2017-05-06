package com.capstone.algorithms;

import com.capstone.entities.ClusterGroup;
import com.capstone.entities.StockPoint;
import com.capstone.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class KMeans
{
    private final int _k;

    private List<StockPoint> _data;
    private List<ClusterGroup> _clusters = new ArrayList<ClusterGroup>();


    public KMeans(int k)
    {
        if (k <= 0)
            throw new RuntimeException("k must be greater than 0");

        _k = k;
    }


    public final void clusterize(List<StockPoint> data)
    {
        clusterize(data, false);
    }
    public final void clusterize(List<StockPoint> data, boolean keepOldCenters)
    {
        _data = data;

        if (!keepOldCenters || _clusters.isEmpty())
            initClusters();

        do
        {
            for (ClusterGroup cluster : _clusters)
                cluster.data.clear();

            for (StockPoint pt : _data)
                assignPointToNearestCluster(pt);

        } while (updateClusters()); // centers changed
    }
    public final void clusterize(List<StockPoint> data, List<ClusterGroup> centers)
    {
        _data = data;

        if (centers != null)
            _clusters = centers;
        else
            initClusters();

        do
        {
            for (ClusterGroup cluster : _clusters)
                cluster.data.clear();

            for (StockPoint pt : _data)
                assignPointToNearestCluster(pt);

        } while (updateClusters()); // centers changed
    }

    private void initClusters()
    {
        _clusters.clear();

        for (int i = 0; i < _k; i++)
        {
            ClusterGroup cluster = new ClusterGroup(MathUtils.rand(0, 1), MathUtils.rand(0, 1));
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

        for (ClusterGroup cluster : _clusters)
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


    public final int k()
    {
        return _k;
    }

    public final List<StockPoint> data()
    {
        return _data;
    }
    public final List<ClusterGroup> clusters()
    {
        return _clusters;
    }
}
