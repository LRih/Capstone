package com.capstone.utils;

import java.text.SimpleDateFormat;

public final class DateUtils
{
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private DateUtils()
    {
        throw new AssertionError();
    }
}
