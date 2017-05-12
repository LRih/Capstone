package com.capstone.algorithms;

import com.capstone.dataService.SigmoidSigmoidPreprocessor;
import com.capstone.entities.ClusterGroup;
import com.capstone.entities.StockPoint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class ThreadedJI
{
    private int _k;

    private SigmoidSigmoidPreprocessor _preprocessor;
    private Map<Date, List<StockPoint>> _dateIndex;
    private Map<Date, KMeans> _clusterings = new HashMap<Date, KMeans>();

    private int _threads;
    private List<String> _names;
    private boolean[] _nameDone;


    public ThreadedJI(int k, int threads)
    {
        _k = k;
        _threads = threads;
    }


    public final void calculate(SigmoidSigmoidPreprocessor preprocessor)
    {
        calculate(preprocessor, "1000-01-01", "3000-01-01");
    }
    public final void calculate(SigmoidSigmoidPreprocessor preprocessor, String startDate, String endDate)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            Date start = df.parse(startDate);
            Date end = df.parse(endDate);

            _preprocessor = preprocessor;

            _names = new ArrayList<String>(_preprocessor.nameMap().keySet());
            _nameDone = new boolean[_names.size()];

            // trim dates to specified range
            _dateIndex = new TreeMap<Date, List<StockPoint>>();
            for (Date date : _preprocessor.dateMap().keySet())
                if (!date.before(start) && !date.after(end))
                    _dateIndex.put(date, _preprocessor.getStocksByDate(date));

            _clusterings.clear();

            clusterDates();
            computeJaccardIndex();
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void clusterDates()
    {
        System.out.println("Clustering with k=" + _k);

        Date lastDate = null;
        for (Date date : _dateIndex.keySet())
        {
            KMeans clustering = new KMeans(_k);

            // use cluster centers from previous day
            if (lastDate != null)
            {
                List<ClusterGroup> centers = copyCenters(_clusterings.get(lastDate).clusters());
                clustering.clusterize(_preprocessor.getStocksByDate(date), centers);
            }
            else
                clustering.clusterize(_preprocessor.getStocksByDate(date));

            _clusterings.put(date, clustering);
            lastDate = date;
        }
    }

    private void computeJaccardIndex()
    {
        System.out.println("Starting jaccard index computation (k=" + _k + ")");

        long ms = System.currentTimeMillis();

        // start threads
        Thread[] threads = new Thread[_threads];
        for (int i = 0; i < _threads; i++)
        {
            threads[i] = new JITask();
            threads[i].start();
        }

        // wait for threads to finish
        try
        {
            for (Thread thread : threads)
                thread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        System.out.println("Completed Jaccard computations (" + (System.currentTimeMillis() - ms) + " ms)");
    }
    private void computeJaccardIndex(String stockName)
    {
        long ms = System.currentTimeMillis();
        for (Date date : _dateIndex.keySet())
            computeJaccardIndex(stockName, date);

        System.out.println("Computed: " + stockName + " (" + (System.currentTimeMillis() - ms) + " ms)");
    }
    private void computeJaccardIndex(String stockName, Date dateJ)
    {
        double index = 0;

        StockPoint sJ = _preprocessor.getStockByNameDate(stockName, dateJ);

        for (Date dateL : _dateIndex.keySet())
        {
            StockPoint sL = _preprocessor.getStockByNameDate(stockName, dateL);

            // assume no movement if stock doesn't exist
            if (sJ == null || sL == null)
                index += 1;
            else
            {
                int clusterJ = sJ.getCluster();
                int clusterL = sL.getCluster();

                Map<String, StockPoint> gJ = _clusterings.get(dateJ).clusters().get(clusterJ).data;
                Map<String, StockPoint> gL = _clusterings.get(dateL).clusters().get(clusterL).data;

                int intersection = getIntersection(gJ, gL).size();
                int union = getUnion(gJ, gL, intersection);

                index += intersection / (double)union;
            }
        }

        index /= _dateIndex.size();

        if (sJ != null)
            sJ.setJIndex(index);
    }

    public final void writeToSingleFile()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<StockPoint> pts = new ArrayList<StockPoint>();

        for (Date date : _dateIndex.keySet())
        {
            for (String name : _preprocessor.nameMap().keySet())
            {
                StockPoint pt = _preprocessor.getStockByNameDate(name, date);
                if (pt != null)
                    pts.add(pt);
            }
        }

        Collections.sort(pts, new StockPoint.JaccardIndexComparator());

        try
        {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("jaccard.csv")));
            writer.write("stock,date,jaccardindex (k=" + _k + ")\n");

            for (StockPoint pt : pts)
                writer.write(pt.getStockSymbol() + "," + df.format(pt.getListedDate()) + "," + pt.getJIndex() + "\n");

            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public final void writeToIndividualFiles()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            for (String name : _preprocessor.nameMap().keySet())
            {
                File file = new File("jaccard/" + name + ".csv");
                file.getParentFile().mkdirs();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                writer.write("date,jaccardindex (k=" + _k + ")\n");

                for (Date date : _dateIndex.keySet())
                {
                    StockPoint pt = _preprocessor.getStockByNameDate(name, date);
                    if (pt != null)
                        writer.write(df.format(pt.getListedDate()) + "," + pt.getJIndex() + "\n");
                }

                writer.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public final void writeAnomaliesFile(double thresStdDev)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("jaccard.anomalies.csv")));
            writer.write("stock,date,jaccardindex (k=" + _k + ")\n");

            for (StockPoint pt : getAnomalies(thresStdDev))
                writer.write(pt.getStockSymbol() + "," + df.format(pt.getListedDate()) + "," + pt.getJIndex() + "\n");

            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private static List<ClusterGroup> copyCenters(List<ClusterGroup> clusters)
    {
        List<ClusterGroup> centers = new ArrayList<ClusterGroup>();

        for (ClusterGroup cluster : clusters)
            centers.add(new ClusterGroup(cluster.centerX, cluster.centerY));

        return centers;
    }

    private static int getUnion(Map<String, StockPoint> a, Map<String, StockPoint> b, int intersection)
    {
        return a.size() + b.size() - intersection;
    }
    private static List<StockPoint> getIntersection(Map<String, StockPoint> a, Map<String, StockPoint> b)
    {
        List<StockPoint> result = new ArrayList<StockPoint>();

        // only add to results if stock exists in both lists
        for (StockPoint pt : a.values())
            if (b.containsKey(pt.getStockSymbol()))
                result.add(pt);

        return result;
    }


    public final KMeans getClustering(Date date)
    {
        return _clusterings.get(date);
    }

    public final List<StockPoint> getAnomalies(double thresStdDev)
    {
        List<StockPoint> pts = new ArrayList<StockPoint>();

        for (String name : _preprocessor.nameMap().keySet())
        {
            double mean = calcMean(name);
            double stdDev = calcStdDev(name);
            double lower = mean - stdDev * thresStdDev;
            double upper = mean + stdDev * thresStdDev;

            for (Date date : _dateIndex.keySet())
            {
                StockPoint pt = _preprocessor.getStockByNameDate(name, date);
                if (pt != null && (pt.getJIndex() < lower || pt.getJIndex() > upper))
                    pts.add(pt);
            }
        }

        return pts;
    }

    private double calcStdDev(String stockName)
    {
        return Math.sqrt(calcSampleVariance(stockName));
    }
    private double calcSampleVariance(String stockName)
    {
        double mean = calcMean(stockName);
        double variance = 0;
        int count = 0;

        for (Date date : _dateIndex.keySet())
        {
            StockPoint pt = _preprocessor.getStockByNameDate(stockName, date);
            if (pt != null)
            {
                variance += Math.pow(pt.getJIndex() - mean, 2);
                count++;
            }
        }

        return variance / (count - 1);
    }
    private double calcMean(String stockName)
    {
        double mean = 0;
        int count = 0;

        for (Date date : _dateIndex.keySet())
        {
            StockPoint pt = _preprocessor.getStockByNameDate(stockName, date);
            if (pt != null)
            {
                mean += pt.getJIndex();
                count++;
            }
        }

        mean /= count;

        return mean;
    }


    private synchronized boolean takeTask(int index)
    {
        if (!_nameDone[index])
        {
            _nameDone[index] = true;
            return true;
        }

        return false;
    }


    private final class JITask extends Thread
    {
        public final void run()
        {
            for (int i = 0; i < _names.size(); i++)
                if (takeTask(i))
                    computeJaccardIndex(_names.get(i));
        }
    }
}
