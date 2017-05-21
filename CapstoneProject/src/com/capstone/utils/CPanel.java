package com.capstone.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.capstone.algorithms.KMeans;
import com.capstone.algorithms.QuickHull;
import com.capstone.entities.ClusterGroup;
import com.capstone.entities.StockPoint;

/**
 * A panel for drawing points with clusters.
 */
public class CPanel extends JPanel
{
    private static final int MIN_COORDINATE = 0;
    private static final int MAX_COORDINATE = 1;

    private static final int DOT_RADIUS = 5;
    private static final int CENTER_WIDTH = 3;
    private static final int HULL_WIDTH = 1;

    private static final Color[] COLORS = {
        new Color(233, 30, 99),
        new Color(244, 67, 54),
        new Color(156, 39, 176),
        new Color(103, 58, 183),
        new Color(63, 81, 181),
        new Color(33, 150, 243),
        new Color(76, 175, 80),
        new Color(255, 193, 7),
        new Color(96, 125, 139),
        new Color(121, 85, 72),
    };

    private List<ClusterGroup> _clusters;


    public CPanel()
    {
        setBackground(Color.WHITE);
    }


    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        if (_clusters == null)
            return;

        Graphics2D g = (Graphics2D)graphics;

        drawData(g);
        drawCenters(g);
        drawHulls(g);
    }

    private void drawData(Graphics2D g)
    {
        for (int i = 0; i < _clusters.size(); i++)
        {
            ClusterGroup cluster = _clusters.get(i);
            g.setColor(COLORS[i]);

            for (StockPoint s : cluster.data.values())
            {
                int x = (int)(s.getX() / (float)MAX_COORDINATE * getWidth()) - DOT_RADIUS;
                int y = (int)(s.getY() / (float)MAX_COORDINATE * getHeight()) - DOT_RADIUS;

                g.fillOval(x, y, DOT_RADIUS * 2, DOT_RADIUS * 2);
            }
        }
    }

    private void drawCenters(Graphics2D g)
    {
        g.setStroke(new BasicStroke(CENTER_WIDTH));

        for (int i = 0; i < _clusters.size(); i++)
        {
            ClusterGroup cluster = _clusters.get(i);
            g.setColor(COLORS[i]);

            int x = (int)(cluster.centerX / (float)MAX_COORDINATE * getWidth());
            int y = (int)(cluster.centerY / (float)MAX_COORDINATE * getHeight());

            g.drawLine(x - DOT_RADIUS, y - DOT_RADIUS, x + DOT_RADIUS, y + DOT_RADIUS);
            g.drawLine(x + DOT_RADIUS, y - DOT_RADIUS, x - DOT_RADIUS, y + DOT_RADIUS);
        }
    }

    private void drawHulls(Graphics2D g)
    {
        g.setStroke(new BasicStroke(HULL_WIDTH));
        g.setColor(Color.BLACK);

        for (int i = 0; i < _clusters.size(); i++)
        {
            ClusterGroup cluster = _clusters.get(i);

            List<StockPoint> hull = new QuickHull().getHull(new ArrayList<StockPoint>(cluster.data.values()));

            if (hull.isEmpty())
                continue;

            Path2D path = new Path2D.Double();

            StockPoint s0 = hull.get(0);
            int x0 = (int)(s0.getX() / (float)MAX_COORDINATE * getWidth());
            int y0 = (int)(s0.getY() / (float)MAX_COORDINATE * getHeight());
            path.moveTo(x0, y0);

            for (int j = 1; j < hull.size(); j++)
            {
                StockPoint s = hull.get(j);
                int x = (int)(s.getX() / (float)MAX_COORDINATE * getWidth());
                int y = (int)(s.getY() / (float)MAX_COORDINATE * getHeight());
                path.lineTo(x, y);
            }

            path.closePath();

            g.draw(path);
        }
    }

    public void setClusterer(KMeans clusterer)
    {
        _clusters = clusterer.clusters();
        repaint();
    }
    public void setClusterer(List<ClusterGroup> clusters)
    {
        _clusters = clusters;
        repaint();
    }
}