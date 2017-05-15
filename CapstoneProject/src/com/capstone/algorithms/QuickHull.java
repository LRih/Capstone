package com.capstone.algorithms;

import com.capstone.entities.StockPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a convex hull for a set of given points using Quickhull with
 * average case complexity: O(n log n).
 * <p>
 * See: https://en.wikipedia.org/wiki/Quickhull
 */
public final class QuickHull
{
    private List<StockPoint> _hull = new ArrayList<StockPoint>();

    public List<StockPoint> getHull(List<StockPoint> pts)
    {
        _hull.clear();

        // hull requires at least two points
        if (pts.size() < 3)
            return pts;

        // get the leftmost and rightmost points and add them to hull
        StockPoint pt1 = getMinX(pts);
        StockPoint pt2 = getMaxX(pts);

        _hull.add(pt1);
        _hull.add(pt2);

        // the two points subdivide remaining points into two groups
        List<StockPoint> leftPts = new ArrayList<StockPoint>();
        List<StockPoint> rightPts = new ArrayList<StockPoint>();

        for (StockPoint pt : pts)
        {
            if (pt == pt1 || pt == pt2)
                continue;

            if (isLeftSide(pt1, pt2, pt))
                leftPts.add(pt);
            else
                rightPts.add(pt);
        }

        findHull(leftPts, pt1, pt2);
        findHull(rightPts, pt2, pt1);

        return _hull;
    }

    /**
     * Recursive function in QuickHull for generating a convex hull.
     */
    private void findHull(List<StockPoint> pts, StockPoint pt1, StockPoint pt2)
    {
        int insertPos = _hull.indexOf(pt2);

        // no more points to consider
        if (pts.size() == 0)
            return;

        // only one point to consider, must be on hull
        if (pts.size() == 1)
        {
            _hull.add(insertPos, pts.get(0));
            return;
        }

        StockPoint newPt = getFurthest(pt1, pt2, pts);
        pts.remove(newPt);
        _hull.add(insertPos, newPt);

        // Determine left points of pt1-newPt
        List<StockPoint> leftPts1 = new ArrayList<StockPoint>();
        for (int i = pts.size() - 1; i >= 0; i--)
        {
            StockPoint pt = pts.get(i);
            if (isLeftSide(pt1, newPt, pt))
            {
                pts.remove(i);
                leftPts1.add(pt);
            }
        }

        // Determine left points of newPt-pt2
        List<StockPoint> leftPts2 = new ArrayList<StockPoint>();
        for (int i = pts.size() - 1; i >= 0; i--)
        {
            StockPoint pt = pts.get(i);
            if (isLeftSide(newPt, pt2, pt))
            {
                pts.remove(i);
                leftPts2.add(pt);
            }
        }

        findHull(leftPts1, pt1, newPt);
        findHull(leftPts2, newPt, pt2);
    }


    private static StockPoint getMinX(List<StockPoint> pts)
    {
        StockPoint min = null;

        for (StockPoint pt : pts)
            if (min == null || pt.getX() < min.getX())
                min = pt;

        return min;
    }

    private static StockPoint getMaxX(List<StockPoint> pts)
    {
        StockPoint max = null;

        for (StockPoint pt : pts)
            if (max == null || pt.getX() > max.getX())
                max = pt;

        return max;
    }

    /**
     * Calculates whether a point is on the left side of a line defined by
     * two points.
     */
    private static boolean isLeftSide(StockPoint linePt1, StockPoint linePt2, StockPoint pt)
    {
        double val = (linePt2.getX() - linePt1.getX()) * (pt.getY() - linePt1.getY()) - (linePt2.getY() - linePt1.getY()) * (pt.getX() - linePt1.getX());
        return val > 0;
    }
    
    private static StockPoint getFurthest(StockPoint linePt1, StockPoint linePt2, List<StockPoint> pts)
    {
        StockPoint furthestPt = null;
        double furthestDist = 0;
        
        for (StockPoint pt : pts)
        {
            double dist = getDistance(linePt1, linePt2, pt);
            
            if (furthestPt == null || dist > furthestDist)
            {
                furthestPt = pt;
                furthestDist = dist;
            }
        }
        
        return furthestPt;
    }

    /**
     * Calculates the distance of a point from a line defined by two points.
     * Note this is not actual distance and should be used solely for
     * comparison.
     */
    private static double getDistance(StockPoint linePt1, StockPoint linePt2, StockPoint pt)
    {
        double lineX = linePt2.getX() - linePt1.getX();
        double lineY = linePt2.getY() - linePt1.getY();
        double val = lineX * (linePt1.getY() - pt.getY()) - lineY * (linePt1.getX() - pt.getX());
        return Math.abs(val);
    }
}
