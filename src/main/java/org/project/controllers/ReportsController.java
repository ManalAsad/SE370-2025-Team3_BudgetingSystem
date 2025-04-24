package org.project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.project.models.Transaction;
import java.time.LocalDate;
import java.util.HashMap;

import org.project.util.FileHandler;
import org.project.util.ManualHandler;
import org.project.util.AnalyticsHelper;
import org.project.util.AnalyticsStorage;
import org.project.util.AnalyticsHelper.Categories;

public class ReportsController {
    @FXML private ComboBox<String> timePeriod;
    @FXML private HBox daySel, weekSel, monthSel, yearSel;
    @FXML private DatePicker dayDatePicker, weekStartDatePicker, weekEndDatePicker;
    @FXML private ComboBox<String> month;
    @FXML private ComboBox<Integer> monthYear;
    @FXML private ComboBox<Integer> year;

    // Not sure if we can make use of this, since charts
    // need actual data like Strings or Integers
    private ObservableList<Transaction> transactionData = FXCollections.observableArrayList();

    @FXML
    private BorderPane border;
    private PieChart pieChart;
    private ObservableList<PieChart.Data> chartData;

    private BarChart<String, Number> barChart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series<String, Number> series;

    private final int CATEGORIES = 6;
    private final int MONTHS = 12;
    private HashMap<Integer, String> map;

    @FXML
    public void initialize() {
        initDataCollection();

        String filePath;

        AnalyticsHelper analyticsHelper = new AnalyticsHelper();
        AnalyticsStorage storage = new AnalyticsStorage();

        FileHandler fileHandler = new FileHandler();
        ManualHandler manualHandler = new ManualHandler();
        Transaction transaction;

        // checking if files were uploaded
        if (!fileHandler.isEmpty()) {
            for (int i = 0; i < fileHandler.getSize(); ++i) {
                filePath = fileHandler.getFile(i);
                if (!filePath.isEmpty()) {
                    analyticsHelper.countCategories(filePath);
                    analyticsHelper.computeTotalSpending(filePath);

                    storage.updateTransactionTotal(analyticsHelper.getTotal());
                    storage.updateCategoryCounts(analyticsHelper.getCategoryCounts());
                    storage.updateMonthlySpending(analyticsHelper.getMonthlySpending());
                }
            }
        }

        // checking if manual transactions were added
        if (!manualHandler.isEmpty()) {
            for (int i = 0; i < manualHandler.getSize(); ++i) {
                transaction = manualHandler.getTransaction(i);
                analyticsHelper.countCategories(transaction);
                analyticsHelper.computeTotalSpending(transaction);

                storage.updateTransactionTotal(analyticsHelper.getTotal());
                storage.updateCategoryCounts(analyticsHelper.getCategoryCounts());
                storage.updateMonthlySpending(analyticsHelper.getMonthlySpending());
            }
        }

        if (!fileHandler.isEmpty() || !manualHandler.isEmpty()) {
            initPieChart(analyticsHelper.getCategoryCounts(), analyticsHelper.getTotal());
            initBarChart(analyticsHelper.getMonthlySpending(), analyticsHelper.getDaysInMonth());

            // the pie chart and bar chart are displayed within a border pane
            border.setCenter(pieChart);
            border.setBottom(barChart);
        }

        setupDateChoices();
        setupDateControls();
    }

    private void initDataCollection()
    {
        map = new HashMap<>();

        map.put(0, "January");
        map.put(1, "February");
        map.put(2, "March");
        map.put(3, "April");
        map.put(4, "May");
        map.put(5, "June");
        map.put(6, "July");
        map.put(7, "August");
        map.put(8, "September");
        map.put(9, "October");
        map.put(10, "November");
        map.put(11, "December");
    }

    private void initPieChart(int[] counts, int total)
    {
        chartData = FXCollections.observableArrayList();

        // adding the data for the pie chart
        // Note: The integer values for the pie chart do not represent percentages
        chartData.add(new PieChart.Data("Restaurants & Dining", counts[Categories.RESTAURANTS.ordinal()]));
        chartData.add(new PieChart.Data("Transportation", counts[Categories.TRANSPORTATION.ordinal()]));
        chartData.add(new PieChart.Data("Health", counts[Categories.HEALTH.ordinal()]));
        chartData.add(new PieChart.Data("Shopping & Entertainment", counts[Categories.SHOPPING.ordinal()]));
        chartData.add(new PieChart.Data("Insurance", counts[Categories.INSURANCE.ordinal()]));
        chartData.add(new PieChart.Data("Groceries", counts[Categories.GROCERIES.ordinal()]));

        pieChart = new PieChart(chartData);

        // adding Tooltips to pie chart
        setTooltips(pieChart, counts, total);

        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(60);
        pieChart.setLegendVisible(false);

        pieChart.setPrefSize(400, 500);
        pieChart.setMinHeight(500);
    }

    private void initBarChart(double[] monthlySpending, int[] daysInMonth)
    {
        // setting up the axes for the bar chart
        xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        yAxis = new NumberAxis();
        yAxis.setLabel("Average");

        barChart = new BarChart<>(xAxis, yAxis);
        series = new XYChart.Series<>();

        // adding the data for the bar chart
        int average;
        for (int i = 0; i < MONTHS; ++i) {
            if (((int) monthlySpending[i]) == 0)
                continue;

            average = (int) (Math.round(monthlySpending[i] / (double) daysInMonth[i]));
            series.getData().add(new XYChart.Data<>(map.get(i), average));
        }

        barChart.getData().add(series);
        barChart.setLegendVisible(false);

        barChart.setPrefSize(600, 300);
        barChart.setMinHeight(300);
    }

    private void setTooltips(PieChart chart, int[] counts, int total)
    {
        int percent;

        for (int i = 0; i < CATEGORIES; ++i) {
            percent = (int) (Math.round(((double) counts[i] / total) * 100.0));
            Tooltip tooltip = new Tooltip(percent + "%");
            tooltip.setShowDelay(Duration.seconds(0));
            Tooltip.install(chart.getData().get(i).getNode(), tooltip);
        }
    }

    private void setupDateChoices() {
        timePeriod.setItems(FXCollections.observableArrayList(     //time givenPeriodOfTime selection
                "All", "Day", "Week", "Month", "Year"
        ));

        month.setItems(FXCollections.observableArrayList(      //month selection
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        ));

        int currentYear = LocalDate.now().getYear();            //year selection
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int year = currentYear - 10; year <= currentYear + 10; year++) {       //just an example, +10 and -10 years from 2025; decide set up later?
            years.add(year);
        }
        monthYear.setItems(years);
        year.setItems(years);

        timePeriod.getSelectionModel().select("All");      //set default values upon open
        dayDatePicker.setValue(LocalDate.now());
        weekStartDatePicker.setValue(LocalDate.now());
        weekEndDatePicker.setValue(LocalDate.now().plusDays(6));
        month.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        monthYear.getSelectionModel().select(Integer.valueOf(LocalDate.now().getYear()));
        year.getSelectionModel().select(Integer.valueOf(LocalDate.now().getYear()));
    }

    private void setupDateControls() {
        hideControls(); //hide controls, makes it pretty :O

        //listeners for time givenPeriodOfTime combo
        timePeriod.valueProperty().addListener((obs, oldVal, newVal) -> {
            hideControls();
            switch (newVal) {
                case "Day":
                    daySel.setVisible(true);
                    daySel.setManaged(true);
                    break;
                case "Week":
                    weekSel.setVisible(true);
                    weekSel.setManaged(true);
                    break;
                case "Month":
                    monthSel.setVisible(true);
                    monthSel.setManaged(true);
                    break;
                case "Year":
                    yearSel.setVisible(true);
                    yearSel.setManaged(true);
                    break;
            }
            //generateReport();
        });

        // this adds listeners for dates (1 day)
        dayDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> generateReport());

        //adds listener for dates (1 week)
        weekStartDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                weekEndDatePicker.setValue(newVal.plusDays(6));
                //generateReport();
            }
        });

        //month and year listener
        month.valueProperty().addListener((obs, oldVal, newVal) -> generateReport());
        monthYear.valueProperty().addListener((obs, oldVal, newVal) -> generateReport());

        //year listener
        year.valueProperty().addListener((obs, oldVal, newVal) -> generateReport());
    }

    private void hideControls() {   //hide for looks
        daySel.setVisible(false);
        daySel.setManaged(false);
        weekSel.setVisible(false);
        weekSel.setManaged(false);
        monthSel.setVisible(false);
        monthSel.setManaged(false);
        yearSel.setVisible(false);
        yearSel.setManaged(false);
    }

    /* Not sure if we can make use of this function
    *  Also not sure how anything with ObservableList<Transaction> can be used.
    *  This is due to the fact that the charts need values like Strings and Integers
    *  We will most likely need to use the ObservableList class, however, its declaration
    *  will need to be modified
    */
    @FXML
    private void generateReport() {
        String givenPeriodOfTime = timePeriod.getValue();   //get the value of a given period of time
        ObservableList<Transaction> filteredData = "All".equals(givenPeriodOfTime)
                ? transactionData   //no filter if 'all'
                : filterTransactions(getStartDate(givenPeriodOfTime), getEndDate(givenPeriodOfTime));   //pass start and end of dates for the filtering

        //updateCharts(filteredData, givenPeriodOfTime);
    }

    private LocalDate getStartDate(String givenPeriodOfTime) {  //return a given start day
        switch (givenPeriodOfTime) {
            case "Day":
                return dayDatePicker.getValue();
            case "Week":
                return weekStartDatePicker.getValue();
            case "Month":
                return LocalDate.of(monthYear.getValue(), month.getSelectionModel().getSelectedIndex() + 1, 1);
            case "Year":
                return LocalDate.of(year.getValue(), 1, 1);
            default:
                return LocalDate.now();
        }
    }

    private LocalDate getEndDate(String givenPeriodOfTime) {    //return a given end date
        LocalDate startDate = getStartDate(givenPeriodOfTime);

        switch (givenPeriodOfTime) {
            case "Day":
                return dayDatePicker.getValue();
            case "Week":
                return weekEndDatePicker.getValue();
            case "Month":
                return startDate.withDayOfMonth(startDate.lengthOfMonth());
            case "Year":
                return LocalDate.of(year.getValue(), 12, 31);
            default:
                return LocalDate.now();
        }
    }

    private ObservableList<Transaction> filterTransactions(LocalDate startDate, LocalDate endDate) {    //filter transactions based on the start and end weekly range
        return transactionData.filtered(t -> {
            LocalDate transactionDate = t.getDate();
            return !transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate);
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
