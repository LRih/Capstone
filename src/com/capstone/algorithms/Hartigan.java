package com.capstone.algorithms;

import com.capstone.entities.ClusterGroup;
import com.capstone.entities.StockPoint;

import java.util.List;

/**
 * Uses Hartigan's theory to obtain an initial value for the number of
 * clusters.
 */
public final class Hartigan
{
    private Hartigan()
    {
        throw new AssertionError();
    }

    public static int calcK(List<StockPoint> pts)
    {
        int k = 1;
        double essSum = calcESSSum(pts, k);

        while (true)
        {
            double nextEssSum = calcESSSum(pts, k + 1);

            double val = (essSum / nextEssSum - 1) * (pts.size() - k - 1);

            if (!(val > 10))
                return k;

            essSum = nextEssSum;
            k++;
        }
    }

    /**
     * Performs k-means and returns the ESS sum of the resulting clustering.
     */
    private static double calcESSSum(List<StockPoint> pts, int k)
    {
        KMeans clustering = new KMeans(k);
        clustering.clusterize(pts);

        return calcESSSum(clustering);
    }

    /**
     * Calculates and adds together the ESS of all clusters.
     */
    private static double calcESSSum(KMeans clustering)
    {
        double s = 0;

        for (ClusterGroup cluster : clustering.clusters())
            s += calcESS(cluster);

        return s;
    }

    /**
     * Calculates the within sum of squares for a cluster.
     */
    private static double calcESS(ClusterGroup cluster)
    {
        double s = 0;

        for (StockPoint pt : cluster.data.values())
        {
            double xDist = pt.getX() - cluster.centerX;
            double yDist = pt.getY() - cluster.centerY;
            s += Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
        }

        return s;
    }
}
