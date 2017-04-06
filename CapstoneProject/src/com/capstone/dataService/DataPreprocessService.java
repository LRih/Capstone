package com.capstone.dataService;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Takes raw stock data and performs the following normalization:
 *   close: (P(t) - P(t-1)) / P(t) then [min, max] => [0, 1]
 *   volume: sigmoid function with translation = average volume
 *
 * @author Richard Liu
 */
public class DataPreprocessService
{
    private static String RAW_DATA_FILENAME = "prices.csv";
    private static String NORMALIZED_DATA_FILENAME = "normalized.csv";

    private List<Stock> _data;
    private Map<String, List<Stock>> _stocks;

    private double _minDeltaClose;
    private double _maxDeltaClose;
    private double _meanVolume;

    private double _sigmoidBeta;


    /**
     * Performs preprocessing on data and saves it to file.
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

        writeData();
    }


    private void loadData()
    {
        _data = new ArrayList<Stock>();

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

                split[0] = split[0].split(" ")[0]; // remove time
                Date date = readDf.parse(split[0]);

                String name = split[1];
                double close = Double.parseDouble(split[3]);
                long volume = (long)Double.parseDouble(split[6]);

                _data.add(new Stock(date, name, close, volume));
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
        _stocks = new HashMap<String, List<Stock>>();

        for (Stock s : _data)
        {
            if (!_stocks.containsKey(s.symbol))
                _stocks.put(s.symbol, new ArrayList<Stock>());

            _stocks.get(s.symbol).add(s);
        }
    }

    /**
     * Write preprocessed data to file.
     */
    private void writeData()
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(NORMALIZED_DATA_FILENAME)));
            writer.write("date,symbol,closechange,volume\n"); // write header line

            DateFormat writeDf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            for (Stock stock : _data)
            {
                String str = writeDf.format(stock.date) + "," + stock.symbol + "," + stock.normalizedDeltaClose + "," + stock.normalizedVolume + "\n";
                writer.write(str);
            }

            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Normalize close change values based on min/max.
     */
    private void normalizeClose()
    {
        for (Stock s : _data)
            s.normalizedDeltaClose = (s.deltaClose - _minDeltaClose) / (_maxDeltaClose - _minDeltaClose);
    }

    /**
     * Normalize volume based on sigmoid function.
     */
    private void normalizeVolume()
    {
        for (Stock s : _data)
            s.normalizedVolume = calcSigmoid(s.volume);
    }

    private void calcDeltaClose()
    {
        for (String name : _stocks.keySet())
        {
            for (int i = 1; i < _stocks.get(name).size(); i++)
            {
                Stock s0 = _stocks.get(name).get(i - 1);
                Stock s1 = _stocks.get(name).get(i);

                s1.deltaClose = s1.close - s0.close;
            }
        }
    }
    private void calcMinMaxDeltaClose()
    {
        boolean assigned = false;
        _minDeltaClose = 0;
        _maxDeltaClose = 0;

        for (Stock stock : _data)
        {
            if (!assigned)
            {
                assigned = true;
                _minDeltaClose = stock.deltaClose;
                _maxDeltaClose = stock.deltaClose;
                continue;
            }

            if (stock.deltaClose < _minDeltaClose)
                _minDeltaClose = stock.deltaClose;

            if (stock.deltaClose > _maxDeltaClose)
                _maxDeltaClose = stock.deltaClose;
        }

        System.out.println("Min/max delta close: " + _minDeltaClose + " / " + _maxDeltaClose);
    }
    private void calcVolumeMean()
    {
        _meanVolume = 0;

        for (Stock s : _data)
            _meanVolume += s.volume;

        _meanVolume /= _data.size();

        System.out.println("Mean volume: " + _meanVolume);
    }

    /**
     * Current default is to take beta such that the min volume is normalized
     * to normalized as 0.01. To do this we plug in values to the sigmoid
     * function and solve for beta.
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

        for (Stock s : _data)
            variance += Math.pow(s.volume - _meanVolume, 2);

        return variance / (_data.size() - 1);
    }

    private double calcMinVolume()
    {
        boolean assigned = false;
        double min = 0;

        for (Stock stock : _data)
        {
            if (!assigned)
            {
                assigned = true;
                min = stock.volume;
                continue;
            }

            if (stock.volume < min)
                min = stock.volume;
        }

        System.out.println("Min volumne: " + min);

        return min;
    }

    private double calcSigmoid(double value)
    {
        return 1 / (1 + Math.exp(-_sigmoidBeta * (value - _meanVolume)));
    }


    /**
     * Represents a data point in the preprocessing.
     */
    private static class Stock
    {
        public final Date date;
        public final String symbol;
        public final double close;
        public final long volume;

        public double deltaClose;
        public double normalizedDeltaClose;
        public double normalizedVolume;

        public Stock(Date date, String symbol, double close, long volume)
        {
            this.date = date;
            this.symbol = symbol;
            this.close = close;
            this.volume = volume;
        }

        public final String toString()
        {
            return date + ", " + symbol + ", " + close + ", " + volume;
        }
    }

    /**
     * For sorting stocks based on date then symbol.
     */
    private static final class DateSymbolComparator implements Comparator<Stock>
    {
        public final int compare(Stock s1, Stock s2)
        {
            int dateCmp = s1.date.compareTo(s2.date);
            if (dateCmp != 0)
                return dateCmp;

            return s1.symbol.compareTo(s2.symbol);
        }
    }
}
