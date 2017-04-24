/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.entities;

import java.util.Date;

/**
 *
 * @author Shadow
 */
public class SearchItem {
    private String symbol;
    private String articleShortName;
    private String articleLongName;
    private Date articleDate;
    private String URL;
    private String newsSource;
    
    public SearchItem ()
    {
        
    }
    
    public SearchItem (String symbol)
    {
        /**
         * @param  symbol   Symbol of stock's name that the search was made with.
         */
        this.symbol = symbol;
    }
    
    public Date getDate ()
    {
        /**
         * @return  the date of the article.
         */
        return articleDate;
    }
    
    public String getLongName ()
    {
        /**
         * @return  the article's long name.
         */
        return articleLongName;
        
    }
    
    public String getNewsSource ()
    {
        /**
         * @return  the news source of the article.
         */
        return newsSource;
    }
    
    public String getShortName ()
    {
        /**
         * @return  the article's short name.
         */
        return articleShortName;
    }
    
    public String getSymbol ()
    {
        /**
         * @return  the symbol associated with the search made.
         */
        return symbol;
    }
    
    public String getURL ()
    {
        /**
         * @return  the url link of the article.
         */
        return URL;
    }
    
    public void setDate (Date date)
    {
        /**
         * @param  date     the date of the article.
         */
        this.articleDate = date;
    }
    
    public void setLongName (String name)
    {
        /**
         * @param   name    the article's long name.
         */
        this.articleLongName = name;
        
    }
    
    public void setNewsSource (String newsSource)
    {
        /**
         * @param   newsSource  the news source of the article.
         */
        this.newsSource = newsSource;
    }
    
    public void setShortName (String name)
    {
        /**
         * @param   name    the article's short name.
         */
        this.articleShortName = name;
    }
    
    public void setSymbol (String symbol)
    {
        /**
         * @param   symbol  the symbol associated with the search made.
         */
        this.symbol = symbol;
    }
    
    public void setURL (String URL)
    {
        /**
         * @param   URL     the url link of the article.
         */
        this.URL = URL;
    }
}
