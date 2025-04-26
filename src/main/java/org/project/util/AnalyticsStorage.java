package org.project.util;

// stores updated values for things like:
// - total category counts
// - total number of transactions
// - total monthly spending for each month
public class AnalyticsStorage
{
    private static final int CATEGORIES = 6;
    // stores the total count for each category
    private static int[] counts = new int[CATEGORIES];
    private static final int MONTHS = 12;
    // stores the total monthly spending for each month
    private static double[] monthlySpending = new double[MONTHS];
    // stores the total number of transactions
    private static int total;

    public void updateCategoryCounts(int[] updatedCounts)
    {
        counts = updatedCounts;
    }

    public void updateMonthlySpending(double[] updatedMonthlySpending)
    {
        monthlySpending = updatedMonthlySpending;
    }

    public void updateTransactionTotal(int updatedTotal)
    {
        total = updatedTotal;
    }

    public int getTotal()
    {
        return total;
    }

    public int[] getCategoryCounts()
    {
        return counts;
    }

    public double[] getMonthlySpending()
    {
        return monthlySpending;
    }
}

