package org.project.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import org.project.models.Transaction;

public class AnalyticsHelper
{
    private final int CATEGORIES = 6;
    public enum Categories { RESTAURANTS, TRANSPORTATION, HEALTH, SHOPPING, INSURANCE, GROCERIES };
    // stores the total count for each category
    // index 0 == category count for Restaurants & Dining
    // index 1 == category count for Transportation, etc.
    private int[] counts = new int[CATEGORIES];
    // stores the total number of transactions
    private int total;

    private final int MONTHS = 12;
    // stores the total monthly spending for each month
    // index 0 == January, index 1 == February, etc.
    private double[] monthlySpending = new double[MONTHS];
    private int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    // sums up the total spending for each month
    // and stores it in the monthlySpending array
    // this function is used when reading files
    public void computeTotalSpending(String filePath)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            String[] values;

            while ((line = reader.readLine()) != null) {
                // split will split the csv file into tokens
                // split returns a String array containing each token
                values = line.split(",");

                // the first column of the csv file holds the date
                String date = values[0];
                String month = getMonth(date, '/');
                if (!month.isEmpty())
                    monthlySpending[Integer.parseInt(month) - 1] += Double.parseDouble(values[2]);
            }
        }
        catch (IOException e) {
            System.err.println("File not found");
        }
    }

    // this function is used for manual transactions
    // NOTE: This function works correctly to update the bar chart
    public void computeTotalSpending(Transaction transaction)
    {
        String date = transaction.getFormattedDate();
        String month = getMonth(date, '/');

        if (!month.isEmpty())
            monthlySpending[Integer.parseInt(month) - 1] += transaction.getAmount();
    }

    // this function is used when reading files
    public void countCategories(String filePath)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            String[] values;

            while ((line = reader.readLine()) != null) {
                total++;
                values = line.split(",");

                String category = values[values.length - 1];
                switch (category) {
                    case "Restaurants & Dining":
                        counts[AnalyticsHelper.Categories.RESTAURANTS.ordinal()]++;
                        break;
                    case "Transportation":
                        counts[AnalyticsHelper.Categories.TRANSPORTATION.ordinal()]++;
                        break;
                    case "Health":
                        counts[AnalyticsHelper.Categories.HEALTH.ordinal()]++;
                        break;
                    case "Shopping & Entertainment":
                        counts[AnalyticsHelper.Categories.SHOPPING.ordinal()]++;
                        break;
                    case "Insurance":
                        counts[AnalyticsHelper.Categories.INSURANCE.ordinal()]++;
                        break;
                    case "Groceries":
                        counts[AnalyticsHelper.Categories.GROCERIES.ordinal()]++;
                        break;
                }
            }
        }
        catch (IOException e) {
            System.out.println("File not found");
        }
    }

    // this function is used for manual transactions
    // NOTE: This function does not work correctly to update the pie chart
    public void countCategories(Transaction transaction)
    {
        total++;

        String category = transaction.getTransactType();
        switch (category) {
            case "Restaurants & Dining":
                counts[AnalyticsHelper.Categories.RESTAURANTS.ordinal()]++;
                break;
            case "Transportation":
                counts[AnalyticsHelper.Categories.TRANSPORTATION.ordinal()]++;
                break;
            case "Health":
                counts[AnalyticsHelper.Categories.HEALTH.ordinal()]++;
                break;
            case "Shopping & Entertainment":
                counts[AnalyticsHelper.Categories.SHOPPING.ordinal()]++;
                break;
            case "Insurance":
                counts[AnalyticsHelper.Categories.INSURANCE.ordinal()]++;
                break;
            case "Groceries":
                counts[AnalyticsHelper.Categories.GROCERIES.ordinal()]++;
                break;
        }
    }

    private String getMonth(String date, char delimiter)
    {
        int i = 0;
        String month = "";

        while (date.charAt(i) != delimiter) {
            month += date.charAt(i);
            ++i;
        }

        return (month.length() == 2 && month.charAt(0) == '0') ? month.charAt(1) + "" : month;
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

    public int[] getDaysInMonth()
    {
        return daysInMonth;
    }
}
