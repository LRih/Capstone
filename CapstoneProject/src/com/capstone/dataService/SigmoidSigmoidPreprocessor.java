package com.capstone.dataService;

import com.capstone.entities.StockPoint;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Takes raw stock data and performs the following normalization:
 *   close: (P(t) - P(t-1)) / P(t) then sigmoid function with translation = average close
 *   volume: sigmoid function with translation = average volume
 *
 * @author Richard Liu
 */
public class SigmoidSigmoidPreprocessor
{
    private static String RAW_DATA_FILENAME = "prices.csv";
    private static String NORMALIZED_DATA_FILENAME = "normalized.csv";

    private List<StockPoint> _data;
    private Map<String, List<StockPoint>> _stocks;

    private double _meanDeltaClose;
    private double _meanVolume;

    private double _sigmoidBetaDeltaClose;
    private double _sigmoidBetaVolume;


    /**
     * Performs preprocessing on data and saves it to file.
     */
    public void preprocess()
    {
        loadData();
        loadStocks();

        calcDeltaClose();

        calcDeltaCloseMean();
        calcVolumeMean();

        calcDeltaCloseSigmoidBeta();
        calcVolumeSigmoidBeta();

        System.out.println("Sigmoid beta (delta close): " + _sigmoidBetaDeltaClose);
        System.out.println("Sigmoid beta (volume): " + _sigmoidBetaVolume);

        normalizeDeltaClose();
        normalizeVolume();
    }


    /**
     * Write preprocessed data to file.
     */
    public final void writeData()
    {
        if (_stocks == null)
            throw new RuntimeException("Preprocess function must first be called");

        try
        {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(NORMALIZED_DATA_FILENAME)));
            writer.write("date,symbol,rateOfReturn,volume\n"); // write header line

            DateFormat writeDf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            for (StockPoint stock : _data)
            {
                String str = writeDf.format(stock.getListedDate()) + "," + stock.getStockSymbol() + "," + stock.getNormalizedDeltaClose() + "," + stock.getNormalizedVolume() + "\n";
                writer.write(str);
            }

            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public final Map<String, List<StockPoint>> stocks()
    {
        if (_stocks == null)
            throw new RuntimeException("Preprocess function must first be called");

        return _stocks;
    }


    private void loadData()
    {
        _data = new ArrayList<StockPoint>();

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(RAW_DATA_FILENAME)));
            reader.readLine(); // skip reading header line

            DateFormat readDf = new SimpleDateFormat("yyyy-MM-dd");

            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] split = line.split(",");

                // malformed data
                if (split.length != 7)
                {
                    System.out.println("Failed to load malformed data: " + line);
                    continue;
                }

                StockPoint stockPoint = new StockPoint();

                // Sets variables
                stockPoint.setListedDate(split[0]);
                stockPoint.setStockSymbol(split[1]);

                stockPoint.setPriceOpen(Double.parseDouble(split[2]));
                stockPoint.setPriceClose(Double.parseDouble(split[3]));
                stockPoint.setPriceLow(Double.parseDouble(split[4]));
                stockPoint.setPriceHigh(Double.parseDouble(split[5]));
                stockPoint.setVolume(Double.parseDouble(split[6]));

                _data.add(stockPoint);
            }

            reader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Collections.sort(_data, new DateSymbolComparator());

        System.out.println(_data.size() + " data points loaded");
    }
    private void loadStocks()
    {
        _stocks = new HashMap<String, List<StockPoint>>();

        for (StockPoint s : _data)
        {
            if (!_stocks.containsKey(s.getStockSymbol()))
                _stocks.put(s.getStockSymbol(), new ArrayList<StockPoint>());

            _stocks.get(s.getStockSymbol()).add(s);
        }
    }

    /**
     * Normalize delta close based on sigmoid function.
     */
    private void normalizeDeltaClose()
    {
        for (StockPoint s : _data)
            s.setNormalizedDeltaClose(calcDeltaCloseSigmoid(s.getDeltaClose()));
    }

    /**
     * Normalize volume based on sigmoid function.
     */
    private void normalizeVolume()
    {
        for (StockPoint s : _data)
            s.setNormalizedVolume(calcVolumeSigmoid(s.getVolume()));
    }

    private void calcDeltaClose()
    {
        for (String name : _stocks.keySet())
        {
            for (int i = 1; i < _stocks.get(name).size(); i++)
            {
                StockPoint s0 = _stocks.get(name).get(i - 1);
                StockPoint s1 = _stocks.get(name).get(i);

                s1.setDeltaClose(s1.getPriceClose() - s0.getPriceClose());
            }
        }
    }

    private void calcDeltaCloseMean()
    {
        _meanDeltaClose = 0;

        for (StockPoint s : _data)
            _meanDeltaClose += s.getDeltaClose();

        _meanDeltaClose /= _data.size();

        System.out.println("Mean delta close: " + _meanDeltaClose);
    }
    private void calcVolumeMean()
    {
        _meanVolume = 0;

        for (StockPoint s : _data)
            _meanVolume += s.getVolume();

        _meanVolume /= _data.size();

        System.out.println("Mean volume: " + _meanVolume);
    }

    /**
     * Takes beta such that a delta close 3 std devs from the mean normalizes
     * to 0.01.
     */
    private void calcDeltaCloseSigmoidBeta()
    {
        double stdDev3 = _meanDeltaClose - calcDeltaCloseSampleStdDev() * 3;
        _sigmoidBetaDeltaClose = -Math.log(1 / 0.01 - 1) / (stdDev3 - _meanDeltaClose);
    }

    /**
     * Takes beta such that the min volume is normalized as 0.01. To do this we
     * plug in values to the sigmoid function and solve for beta.
     */
    private void calcVolumeSigmoidBeta()
    {
        _sigmoidBetaVolume = -Math.log(1 / 0.01 - 1) / (calcMinVolume() - _meanVolume);
    }

    private double calcDeltaCloseSampleStdDev()
    {
        double stdDev = Math.sqrt(calcDeltaCloseSampleVariance());

        System.out.println("StdDev delta close: " + stdDev);

        return stdDev;
    }
    private double calcDeltaCloseSampleVariance()
    {
        double variance = 0;

        for (StockPoint s : _data)
            variance += Math.pow(s.getDeltaClose() - _meanDeltaClose, 2);

        return variance / (_data.size() - 1);
    }

    private double calcMaxDeltaClose()
    {
        boolean assigned = false;
        double max = 0;

        for (StockPoint stock : _data)
        {
            if (!assigned)
            {
                assigned = true;
                max = stock.getDeltaClose();
                continue;
            }

            if (stock.getDeltaClose() > max)
                max = stock.getDeltaClose();
        }

        System.out.println("Max delta close: " + max);

        return max;
    }
    private double calcMinVolume()
    {
        boolean assigned = false;
        double min = 0;

        for (StockPoint stock : _data)
        {
            if (!assigned)
            {
                assigned = true;
                min = stock.getVolume();
                continue;
            }

            if (stock.getVolume() < min)
                min = stock.getVolume();
        }

        System.out.println("Min volume: " + min);

        return min;
    }

    private double calcDeltaCloseSigmoid(double value)
    {
        return calcSigmoid(value, _sigmoidBetaDeltaClose, _meanDeltaClose);
    }
    private double calcVolumeSigmoid(double value)
    {
        return calcSigmoid(value, _sigmoidBetaVolume, _meanVolume);
    }
    private static double calcSigmoid(double value, double beta, double mean)
    {
        return 1 / (1 + Math.exp(-beta * (value - mean)));
    }


    /**
     * For sorting stocks based on date then symbol.
     */
    private static final class DateSymbolComparator implements Comparator<StockPoint>
    {
        public final int compare(StockPoint s1, StockPoint s2)
        {
            int dateCmp = s1.getListedDate().compareTo(s2.getListedDate());
            if (dateCmp != 0)
                return dateCmp;

            return s1.getStockSymbol().compareTo(s2.getStockSymbol());
        }
    }
}
