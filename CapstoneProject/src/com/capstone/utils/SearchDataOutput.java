/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.utils;

import java.io.File;
import java.util.List;

import com.capstone.entities.Anomalies;
import com.capstone.entities.SearchStocks;

/**
 *
 * @author Shadow
 */
public abstract class SearchDataOutput {
    protected File file;
    
    public SearchDataOutput ()
    {
        this.file = new File("output.txt");
    }
    
    public SearchDataOutput (File file)
    {
        /**
         * @param   file    File to specify as the output.
         */
        this.file = file;
    }
    
    public void setFile (File file)
    {
        /**
         * @param   file    File to specify as the output.
         */
        this.file = file;
    }
    
    public void outputToFile (SearchStocks search, Anomalies anomalies)
    {
        
    }
}