package com.capstone.entities;

import java.util.HashMap;
import java.util.Map;

public final class ClusterGroup
{
    public double centerX;
    public double centerY;

    public Map<String, StockPoint> data = new HashMap<String, StockPoint>();


    public ClusterGroup(double centerX, double centerY)
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
