package com.capstone.algorithms;

import com.capstone.entities.ClusterGroup;
import com.capstone.entities.StockPoint;

/**
 * Evaluates the goodness of a resulting clustering via use of the
 * Davies-Bouldin index metric.
 *
 * See: https://en.wikipedia.org/wiki/Davies%E2%80%93Bouldin_index
 *
 * Author: Richard Liu (s3168087)
 */
public final class DBI
{
    private KMeans _clustering;

    public double calcDBI(KMeans clustering)
    {
        _clustering = clustering;

        double dbi = 0;

        for (ClusterGroup cluster : _clustering.getGroups())
            dbi += calcD(cluster);

        return dbi / _clustering.getGroups().size();
    }

    /**
     * Calculates D(i) for a specified cluster.
     */
    private double calcD(ClusterGroup cluster1)
    {
        boolean maxAssigned = false;
        double maxR = 0;

        for (ClusterGroup cluster2 : _clustering.getGroups())
        {
            if (cluster1 == cluster2)
                continue;

            double r = calcR(cluster1, cluster2);

            if (!maxAssigned || r > maxR)
            {
                maxAssigned = true;
                maxR = r;
            }
        }

        return maxR;
    }

    /**
     * Calculates R(i,j) for a pair of clusters.
     */
    private double calcR(ClusterGroup cluster1, ClusterGroup cluster2)
    {
        return (calcS(cluster1) + calcS(cluster2)) / calcM(cluster1, cluster2);
    }

    /**
     * Calculates S(i), the measure of scatter within a cluster.
     */
    private double calcS(ClusterGroup cluster)
    {
        double s = 0;

        for (StockPoint pt : cluster.getPoints())
        {
            double xDist = pt.getpX() - cluster.getCentroid().getpX();
            double yDist = pt.getpX() - cluster.getCentroid().getpY();
            s += Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
        }

        return s / cluster.getPoints().size();
    }

    /**
     * Calculates M(i,j), the measure of separation between two clusters.
     * I.e. the Euclidean distance between the cluster centers.
     */
    private double calcM(ClusterGroup cluster1, ClusterGroup cluster2)
    {
        double xDist = cluster1.getCentroid().getpX() - cluster2.getCentroid().getpY();
        double yDist = cluster1.getCentroid().getpY() - cluster2.getCentroid().getpY();

        return Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
    }
}
