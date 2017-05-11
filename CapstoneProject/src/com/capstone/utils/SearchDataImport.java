/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* Code from https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm */
package com.capstone.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.String;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import com.capstone.entities.SearchItem;
import com.capstone.entities.SearchStocks;

/**
 *
 * @author Shadow
 */
public class SearchDataImport {
    private boolean debugMode;
    private File folder;
    
    public SearchDataImport ()
    {
        debugMode = false;
    }
    
    public SearchDataImport (boolean debugMode)
    {
        this.debugMode = debugMode;
    }
    
    public void setSearchFolder (String folder)
    {
        this.folder = new File(folder);
    }
    
    public String getSearchFolder ()
    {
        return folder.getPath();
    }
    
    
    
    public SearchStocks importData()
    {
        File inputFile;
        SearchStocks _searchStocks = new SearchStocks();
        
        ArrayList<String> xmlFiles = new ArrayList<String>();
        
        System.out.printf("Importing Search Results...");
        for (File file : folder.listFiles()) 
        {
            if (file.getName().endsWith((".xml"))) {
                String filename = file.getName();
                filename = filename.substring(0, filename.lastIndexOf("."));
                
                _searchStocks.addSearchItemsList(importFileData (file, filename));
        
            }
        }
        System.out.println("Completed!");
        
        return _searchStocks;
    }
    
    private ArrayList<SearchItem> importFileData (File inputFile, String stockSymbol)
    {
        int records = 0;
        ArrayList<SearchItem> _searches = new ArrayList<SearchItem>();
                
        try 
        {	
            //File inputFile = new File("single.xml");
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("rec");
            //System.out.println("----------------------------");

            // Gets total length and puts into memory due to large amount of calls that will be made.
            records = nList.getLength();

            for (int record = 0; record < records; record++)
            {
                //System.out.println(record);
                Node nNode = nList.item(record);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) 
                {
                    Element eElement = (Element) nNode; 
                    SearchItem searchItem = new SearchItem(stockSymbol);


                    searchItem.setLongName(eElement.getElementsByTagName("atl").item(0).getTextContent());
                    
                    //searchItem.setShortName(eElement.getElementsByTagName("au").item(0).getTextContent());
                    searchItem.setNewsSource(eElement.getElementsByTagName("jtl").item(0).getTextContent());
                    searchItem.setStockSymbol(stockSymbol);
                    searchItem.setURL(eElement.getElementsByTagName("url").item(0).getTextContent());


                    //Element headerElement = (Element) eElement.getElementsByTagName("header").item(0);

                    //System.out.println("shortDB: " + headerElement.getAttribute("shortDbName"));
                    //System.out.println("longDB: " + headerElement.getAttribute("longDbName"));


                    Element dtElement = (Element) eElement.getElementsByTagName("dt").item(0);

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date eDate = df.parse(dtElement.getAttribute("year") + "-" +
                           dtElement.getAttribute("month") + "-" + 
                           dtElement.getAttribute("day"));

                    searchItem.setDate(eDate);
                    _searches.add(searchItem);
                }
            }
            
            
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        return _searches;
    }
}
