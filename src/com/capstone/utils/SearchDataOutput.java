package com.capstone.utils;

import java.io.File;
import java.util.List;

import com.capstone.entities.Anomalies;
import com.capstone.entities.SearchStocks;

public abstract class SearchDataOutput
{
    protected File file;

    public SearchDataOutput()
    {
        this.file = new File("output.txt");
    }

    /**
     * @param   file    File to specify as the output.
     */
    public SearchDataOutput(File file)
    {
        this.file = file;
    }

    /**
     * @param   file    File to specify as the output.
     */
    public void setFile(File file)
    {
        this.file = file;
    }

    public void outputToFile(SearchStocks search, Anomalies anomalies)
    {
    }
}
