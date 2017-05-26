package com.capstone.utils;

import java.util.Random;

public final class MathUtils
{
    private MathUtils()
    {
        throw new AssertionError();
    }

    public static long rand(long min, long max)
    {
        return min + (long)(Math.random() * ((max - min) + 1));
    }
    public static double rand(double min, double max)
    {
        return min + (max - min) * new Random().nextDouble();
    }
}
