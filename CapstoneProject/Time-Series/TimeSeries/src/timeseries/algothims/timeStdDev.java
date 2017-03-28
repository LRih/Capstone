/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseries.algothims;

import java.util.LinkedList;
import timeseries.entities.Stock;
import timeseries.entities.StockPoint;

/**
 *
 * @author daniel
 */
public class timeStdDev extends timeAlgorithm {
    public timeStdDev(Stock stock)
    {
        /**
         * @param   stock   Stock dataset that contains the data to be used.
         */
        super(stock);
    }
    
    
    public LinkedList<StockPoint> getAnomalies()
    {
        /**
         * @return  returns all anomalies found in the calculations.
         */
        return anomalies;
    }
}
