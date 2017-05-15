package com.capstone;

import com.capstone.algorithms.TimeARMA;
import com.capstone.algorithms.TimeStdDev;
import com.capstone.data.SigmoidSigmoidPreprocessor;
import com.capstone.entities.Anomalies;
import com.capstone.entities.SearchStocks;
import com.capstone.entities.Stock;
import com.capstone.entities.StockPoint;
import com.capstone.utils.SearchDataCSV;
import com.capstone.utils.SearchDataImport;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Serves as the main entry point for the application, allowing various options
 * and parameters to be set which modify the application's behaviour.
 */
public final class Main
{
    private static final String OPT_HELP = "help";
    private static final String OPT_SEARCH = "search";

    public static void main(String[] args) throws ParseException
    {
        Options options = createOptions();

        // parse arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try
        {
            cmd = parser.parse(createOptions(), args);
        }
        catch (ParseException e)
        {
            printHelp(options);
            return;
        }

        handleCmd(cmd, options);
    }

    private static void handleCmd(CommandLine cmd, Options options)
    {
        if (cmd.hasOption(OPT_SEARCH))
            handleSearch(cmd);
        else
            printHelp(options);
    }

    private static void handleHelp(CommandLine cmd)
    {
        // TODO
    }

    private static void handleSearch(CommandLine cmd)
    {
        String valStr = cmd.getOptionValue(OPT_SEARCH);
        char valChar = valStr == null ? 'd' : valStr.charAt(0);

        // argument given
        if (valStr != null)
        {
            if (valStr.length() > 1 || (valChar != 'a' && valChar != 'd' && valChar != 'p' && valChar != 's'))
            {
                printSearchHelp();
                return;
            }
        }

        Map<String, List<StockPoint>> _stocks;

        SigmoidSigmoidPreprocessor preprocessor = new SigmoidSigmoidPreprocessor();
        preprocessor.preprocess();
        _stocks = preprocessor.nameMap();

        LinkedList<StockPoint> anomalies;

        Anomalies _anomalies = new Anomalies();
        _anomalies.addAnomalyType(Anomalies.Type.STDDEV);
        _anomalies.addAnomalyType(Anomalies.Type.ARMA);

        /**
         * Runs each stock in a bubble for time-series detection.
         */
        for (String key : _stocks.keySet())
        {
            // Creates a new stock unit for each time-series
            Stock unitStock = new Stock();

            // Adds all stock that are relevant to stock class.
            for (int i = 0; i < _stocks.get(key).size(); i++)
                unitStock.addStock(_stocks.get(key).get(i));

            // Runs each time-series algorithm to find anomalies.
            TimeStdDev algorithmStdDev = new TimeStdDev(unitStock);
            anomalies = algorithmStdDev.findAnomalies();
            for (StockPoint stockPoint : anomalies)
            {
                _anomalies.addAnomaly(Anomalies.Type.STDDEV, stockPoint);
            }

            TimeARMA algorithmARMA = new TimeARMA(unitStock);
            algorithmARMA.setPValue(2);
            algorithmARMA.setQValue(3);

            anomalies = algorithmARMA.findAnomalies();
            for (StockPoint stockPoint : anomalies)
                _anomalies.addAnomaly(Anomalies.Type.ARMA, stockPoint);
        }

        // Outputs to file
        _anomalies.outputToFile("anomalies.csv");

        // Search Functionality
        SearchDataImport data = new SearchDataImport();
        data.setSearchFolder(".\\searchData");
        SearchStocks searchStocks = data.importData();

        SearchDataCSV outputCSV = new SearchDataCSV(new File("output.searches.csv"), _stocks);

        outputCSV.outputToFile(searchStocks, _anomalies, valChar);
    }

    private static Options createOptions()
    {
        Options options = new Options();

        options.addOption(OptionBuilder.withArgName("type").hasOptionalArg().withDescription("search for anomalies").create(OPT_SEARCH));
        options.addOption(OptionBuilder.withArgName("cmd").hasArg().withDescription("obtain information on arg").create("help"));

        return options;
    }

    private static void printHelp(Options options)
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("app", options);
    }

    private static void printSearchHelp()
    {
        System.out.println("-search accepts the following switches:");
        System.out.println("  a   article information outputted(implies p in addition)");
        System.out.println("  d   debug Mode, all information outputted");
        System.out.println("  p   stock Price information outputted");
        System.out.println("  s   statistics outputted");
    }
}
