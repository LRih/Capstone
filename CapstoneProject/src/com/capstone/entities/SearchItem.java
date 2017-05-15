package com.capstone.entities;

import java.util.Date;

public final class SearchItem
{
    private String symbol;
    private String articleShortName;
    private String articleLongName;
    private Date articleDate;
    private String URL;
    private String newsSource;

    public SearchItem()
    {

    }

    /**
     * @param symbol Symbol of stock's name that the search was made with.
     */
    public SearchItem(String symbol)
    {
        this.symbol = symbol;
    }

    /**
     * @return the date of the article.
     */
    public Date getDate()
    {
        return articleDate;
    }

    /**
     * @return the article's long name.
     */
    public String getLongName()
    {
        return articleLongName;

    }

    /**
     * @return the news source of the article.
     */
    public String getNewsSource()
    {
        return newsSource;
    }

    /**
     * @return the article's short name.
     */
    public String getShortName()
    {
        return articleShortName;
    }

    /**
     * @return the symbol associated with the search made.
     */
    public String getStockSymbol()
    {
        return symbol;
    }

    /**
     * @return the url link of the article.
     */
    public String getURL()
    {
        return URL;
    }

    /**
     * @param  date     the date of the article.
     */
    public void setDate(Date date)
    {
        this.articleDate = date;
    }


    /**
     * @param   name    the article's long name.
     */
    public void setLongName(String name)
    {
        this.articleLongName = name;

    }

    /**
     * @param   newsSource  the news source of the article.
     */
    public void setNewsSource(String newsSource)
    {
        this.newsSource = newsSource;
    }

    /**
     * @param   name    the article's short name.
     */
    public void setShortName(String name)
    {
        this.articleShortName = name;
    }

    /**
     * @param   symbol  the symbol associated with the search made.
     */
    public void setStockSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    /**
     * @param   URL     the url link of the article.
     */
    public void setURL(String URL)
    {
        this.URL = URL;
    }
}
