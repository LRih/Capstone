package com.capstone.data;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.capstone.entities.StockPoint;
import com.capstone.utils.DateUtils;

/**
 * Takes raw stock data and performs the following normalization:
 *   close: (P(t) - P(t-1)) / P(t) then [min, max] => [0, 1]
 *   volume: sigmoid function with translation = average volume
 */
public class LinearSigmoidPreprocessor
{
    private static String RAW_DATA_FILENAME = "prices.csv";
    private static String NORMALIZED_DATA_FILENAME = "normalized.csv";

    private List<StockPoint> _data;
    private Map<String, List<StockPoint>> _stocks;

    private double _minDeltaClose;
    private double _maxDeltaClose;
    private double _meanVolume;

    private double _sigmoidBeta;


    /**
     * Performs preprocessing on data.
     */
    public final void preprocess()
    {
        preprocess(false);
    }
    public final void preprocess(double sigmoidBeta)
    {
        _sigmoidBeta = sigmoidBeta;
        preprocess(true);
    }
    private void preprocess(boolean sigmoidBetaProvided)
    {
        loadData();
        loadStocks();

        calcDeltaClose();
        calcMinMaxDeltaClose();
        calcVolumeMean();

        if (!sigmoidBetaProvided)
            calcSigmoidBeta();

        System.out.println("Sigmoid beta: " + _sigmoidBeta);

        normalizeClose();
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

            for (StockPoint stock : _data)
            {
                String str = DateUtils.DATETIME_FORMAT.format(stock.getListedDate()) + "," + stock.getStockSymbol() + "," +
                    stock.getNormalizedDeltaClose() + "," + stock.getNormalizedVolume() + "\n";
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

                StockPoint stockPoint = new StockPoint(split[0], split[1],
                    Double.parseDouble(split[2]), Double.parseDouble(split[3]),
                    Double.parseDouble(split[5]), Double.parseDouble(split[4]), Double.parseDouble(split[6]));

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
     * Normalize close change values based on min/max.
     */
    private void normalizeClose()
    {
        for (StockPoint s : _data)
        {
            s.setNormalizedDeltaClose(((s.getDeltaClose() - _minDeltaClose) / (_maxDeltaClose - _minDeltaClose)));
        }
    }

    /**
     * Normalize volume based on sigmoid function.
     */
    private void normalizeVolume()
    {
        for (StockPoint s : _data)
            s.setNormalizedVolume(calcSigmoid(s.getVolume()));
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
    private void calcMinMaxDeltaClose()
    {
        boolean assigned = false;
        _minDeltaClose = 0;
        _maxDeltaClose = 0;

        for (StockPoint stock : _data)
        {
            if (!assigned)
            {
                assigned = true;
                _minDeltaClose = stock.getDeltaClose();
                _maxDeltaClose = stock.getDeltaClose();
                continue;
            }

            if (stock.getDeltaClose() < _minDeltaClose)
                _minDeltaClose = stock.getDeltaClose();

            if (stock.getDeltaClose() > _maxDeltaClose)
                _maxDeltaClose = stock.getDeltaClose();
        }

        System.out.println("Min/max delta close: " + _minDeltaClose + " / " + _maxDeltaClose);
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
     * Current default is to take beta such that the min volume is normalized
     * as 0.01. To do this we plug in values to the sigmoid function and solve
     * for beta.
     */
    private void calcSigmoidBeta()
    {
        _sigmoidBeta = -Math.log(1 / 0.01 - 1) / (calcMinVolume() - _meanVolume);
    }

    private double calcVolumeSampleStdDev()
    {
        double stdDev = Math.sqrt(calcVolumeSampleVariance());

        System.out.println("StdDev volume: " + stdDev);

        return stdDev;
    }
    private double calcVolumeSampleVariance()
    {
        double variance = 0;

        for (StockPoint s : _data)
            variance += Math.pow(s.getVolume() - _meanVolume, 2);

        return variance / (_data.size() - 1);
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

        System.out.println("Min volumne: " + min);

        return min;
    }

    private double calcSigmoid(double value)
    {
        return 1 / (1 + Math.exp(-_sigmoidBeta * (value - _meanVolume)));
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
