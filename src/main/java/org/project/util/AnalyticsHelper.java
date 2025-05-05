package org.project.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Date;

import org.project.database.DatabaseConnection;
import org.project.models.Transaction;

public class AnalyticsHelper
{
    private final int CATEGORIES = 6;
    public enum Categories { RESTAURANTS, TRANSPORTATION, HEALTH, SHOPPING, INSURANCE, GROCERIES };

    private final int MONTHS = 12;
    private int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    private static HashMap<String, double[]> yearSpending = new HashMap<>();
    private static HashMap<String, int[]> yearCategoryCounts = new HashMap<>();
    private static HashMap<String, int[]> totalTransactions = new HashMap<>();

    // this function is used when reading files
    public void computeAnalytics(String filePath, int user_id)
    {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement;

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line, sql, date;
            String[] split_date;
            String[] values;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            sql = "INSERT INTO transactions (user_id, amount, category, transaction_date) " +
                    "VALUES (?, ?, ?, ?)";

            statement = connection.prepareStatement(sql);

            while ((line = reader.readLine()) != null) {
                // split will split the csv file into tokens
                // split returns a String array containing each token
                values = line.split(",");

                statement.setInt(1, user_id);
                statement.setDouble(2, Double.parseDouble(values[2]));
                statement.setString(3, values[3]);

                date = values[0];
                split_date = date.split("/");

                if (split_date[0].length() == 1)
                    split_date[0] = "0" + split_date[0];

                if (split_date[1].length() == 1)
                    split_date[1] = "0" + split_date[1];

                date = split_date[0] + "/" + split_date[1] + "/" + split_date[2];

                statement.setDate(4, Date.valueOf(LocalDate.parse(date, formatter)));

                statement.executeUpdate();

                computeTotalSpending(values);
                countCategories(values);
            }

            statement.close();
            connection.close();
        }
        catch (IOException e) {
            System.err.println("File not found");
        }
        catch (SQLException e) {
            System.err.println("SQL Exception");
            e.printStackTrace();
        }
    }

    // sums up the total spending for each month
    // and stores it in the monthlySpending array
    private void computeTotalSpending(String[] values)
    {
        // the first column of the csv file holds the date
        String date = values[0];

        String year = getYear(date, '/');
        if (!yearSpending.containsKey(year))
            yearSpending.put(year, new double[MONTHS]);

        String month = getMonth(date, '/');
        if (!month.isEmpty()) {
            double[] monthlySpending = yearSpending.get(year);
            monthlySpending[Integer.parseInt(month) - 1] += Double.parseDouble(values[2]);
        }
    }

    private void countCategories(String[] values)
    {
        String date = values[0];
        String year = getYear(date, '/');

        if (!yearCategoryCounts.containsKey(year))
            yearCategoryCounts.put(year, new int[CATEGORIES]);

        if (!totalTransactions.containsKey(year))
            totalTransactions.put(year, new int[1]);

        int[] counts = yearCategoryCounts.get(year);

        int[] total = totalTransactions.get(year);
        total[0] += 1;

        String category = values[values.length - 1];
        updateCounts(category, counts);
    }

    // this function is used for manual transactions
    public void computeAnalytics(Transaction transaction, int user_id)
    {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement;

            String sql, date;
            String[] split_date;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            sql = "INSERT INTO transactions (user_id, amount, category, transaction_date) " +
                    "VALUES (?, ?, ?, ?)";

            statement = connection.prepareStatement(sql);

            statement.setInt(1, user_id);
            statement.setDouble(2, transaction.getAmount());
            statement.setString(3, transaction.getTransactType());

            date = transaction.getFormattedDate();
            split_date = date.split("/");

            if (split_date[0].length() == 1)
                split_date[0] = "0" + split_date[0];

            if (split_date[1].length() == 1)
                split_date[1] = "0" + split_date[1];

            date = split_date[0] + "/" + split_date[1] + "/" + split_date[2];

            statement.setDate(4, Date.valueOf(LocalDate.parse(date, formatter)));

            statement.executeUpdate();

            statement.close();
            connection.close();
        }
        catch (SQLException e) {
            System.err.println("SQL Exception");
            e.printStackTrace();
        }

        computeTotalSpending(transaction);
        countCategories(transaction);
    }

    // this function is used for manual transactions
    // NOTE: This function works correctly to update the bar chart
    private void computeTotalSpending(Transaction transaction)
    {
        String date = transaction.getFormattedDate();

        String year = getYear(date, '/');
        if (!yearSpending.containsKey(year))
            yearSpending.put(year, new double[MONTHS]);

        String month = getMonth(date, '/');
        if (!month.isEmpty()) {
            double[] monthlySpending = yearSpending.get(year);
            monthlySpending[Integer.parseInt(month) - 1] += transaction.getAmount();
        }
    }

    // this function is used for manual transactions
    private void countCategories(Transaction transaction)
    {
        String date = transaction.getFormattedDate();

        String year = getYear(date, '/');
        if (!yearCategoryCounts.containsKey(year))
            yearCategoryCounts.put(year, new int[CATEGORIES]);

        if (!totalTransactions.containsKey(year))
            totalTransactions.put(year, new int[1]);

        int[] counts = yearCategoryCounts.get(year);

        int[] total = totalTransactions.get(year);
        total[0] += 1;

        String category = transaction.getTransactType();
        updateCounts(category, counts);
    }

    public void computeAnalytics(double amount, String category, String date)
    {
        computeTotalSpending(amount, date);
        countCategories(category, date);
    }

    private void computeTotalSpending(double amount, String date)
    {
        String year = getYear(date, '/');
        if (!yearSpending.containsKey(year))
            yearSpending.put(year, new double[MONTHS]);

        String month = getMonth(date, '/');
        if (!month.isEmpty()) {
            double[] monthlySpending = yearSpending.get(year);
            monthlySpending[Integer.parseInt(month) - 1] += amount;
        }
    }

    private void countCategories(String category, String date)
    {
        String year = getYear(date, '/');
        if (!yearCategoryCounts.containsKey(year))
            yearCategoryCounts.put(year, new int[CATEGORIES]);

        if (!totalTransactions.containsKey(year))
            totalTransactions.put(year, new int[1]);

        int[] counts = yearCategoryCounts.get(year);

        int[] total = totalTransactions.get(year);
        total[0] += 1;

        updateCounts(category, counts);
    }

    private void updateCounts(String category, int[] counts)
    {
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

    private String getYear(String date, char delimiter)
    {
        int j = 0;
        for (int i = 0; i < 2; ++i) {
            while (date.charAt(j) != delimiter) ++j;
            j += 1;
        }

        return date.substring(j);
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

    public int getTotal(String year)
    {
        return totalTransactions.get(year)[0];
    }

    public int[] getCategoryCounts(String year)
    {
        return yearCategoryCounts.get(year);
    }

    public double[] getMonthlySpending(String year)
    {
        return yearSpending.get(year);
    }

    public int[] getDaysInMonth()
    {
        return daysInMonth;
    }

    public boolean hasKey(String year)
    {
        return yearSpending.containsKey(year);
    }

    public void clear()
    {
        yearSpending.clear();
        yearCategoryCounts.clear();
        totalTransactions.clear();
    }
}
