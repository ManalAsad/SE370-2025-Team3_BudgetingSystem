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
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    @FXML
    public void initialize() {
        setupDateChoices();
        setupDateControls();

        // I did not call loadCsvData() because I am currently using
        // hard-coded values for testing, will add use of csv file next
        //loadCsvData("src/main/resources/testBudget.csv"); //This is where I stored the example csv file. The format was '2023-01-05,1000,Bills'
        //generateReport();

        initPieChart();
        initBarChart();

        // the pie chart and bar chart are displayed within a Border Pane
        border.setCenter(pieChart);
        border.setBottom(barChart);
    }

    private void initPieChart()
    {
        chartData = FXCollections.observableArrayList();

        // using hard-coded values right now to make
        // sure that the pie chart is displayed correctly
        // Note: The integer values for the pie chart do not represent percentages
        chartData.add(new PieChart.Data("Restaurants & Dining", 36));
        chartData.add(new PieChart.Data("Transportation", 9));
        chartData.add(new PieChart.Data("Health", 4));
        chartData.add(new PieChart.Data("Shopping & Entertainment", 3));
        chartData.add(new PieChart.Data("Insurance", 3));
        chartData.add(new PieChart.Data("Groceries", 4));

        pieChart = new PieChart(chartData);

        // adding Tooltips to pie chart
        int percent;
        int[] counts = { 36, 9, 4, 3, 3, 4 };
        for (int i = 0; i < 6; ++i) {
            percent = (int) (Math.round(((double) counts[i] / 59) * 100.0));
            Tooltip tooltip = new Tooltip(percent + "%");
            tooltip.setShowDelay(Duration.seconds(0));
            Tooltip.install(pieChart.getData().get(i).getNode(), tooltip);
        }

        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(60);
        pieChart.setLegendVisible(false);

        pieChart.setPrefSize(400, 500);
        pieChart.setMinHeight(500);
    }

    private void initBarChart()
    {
        // setting up the axes for the bar chart
        xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        yAxis = new NumberAxis();
        yAxis.setLabel("Average");

        barChart = new BarChart<>(xAxis, yAxis);
        series = new XYChart.Series<>();

        // adding the data for the bar chart
        // using hard-coded values right now to make
        // sure that the bar chart is displayed correctly
        series.getData().add(new XYChart.Data<>("January", 84));
        series.getData().add(new XYChart.Data<>("February", 107));
        series.getData().add(new XYChart.Data<>("March", 58));
        series.getData().add(new XYChart.Data<>("April", 52));
        series.getData().add(new XYChart.Data<>("May", 126));
        series.getData().add(new XYChart.Data<>("June", 117));
        series.getData().add(new XYChart.Data<>("July", 90));
        series.getData().add(new XYChart.Data<>("August", 93));
        series.getData().add(new XYChart.Data<>("September", 117));
        series.getData().add(new XYChart.Data<>("October", 74));
        series.getData().add(new XYChart.Data<>("November", 103));
        series.getData().add(new XYChart.Data<>("December", 68));

        barChart.getData().add(series);
        barChart.setLegendVisible(false);

        barChart.setPrefSize(600, 300);
        barChart.setMinHeight(300);
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

    private void loadCsvData(String filePath) {
        try {
            Files.lines(Paths.get(filePath))    //read all lines from csv
                    .map(line -> line.split(","))   //splits by ,
                    .filter(columns -> columns.length >= 3) //each line has 3 columns
                    .forEach(columns -> {
                        try {
                            transactionData.add(new Transaction(LocalDate.parse(columns[0].trim()), Double.parseDouble(columns[1].trim()), columns[2].trim())); //parse into format
                        }
                        catch (Exception e) {
                            System.err.println("Skipping invalid line: " + String.join(",", columns));
                        }
                    });
        }
        catch (IOException e) {
            showAlert("Error", "Couldn't load CSV: " + e.getMessage());
        }
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

    private void updateCharts(ObservableList<Transaction> transactions, String givenPeriodOfTime) { //chart update, ELSE STWTEMENTE NOT INCLUDED

        //pieChart.getData().clear();
        //barChart.getData().clear();

        if (transactions.isEmpty()) {
            //pieChart.setTitle("No data here :( ");
            //barChart.setTitle("No data here also :( ");
            return;
        }
        updatePieChart(transactions);
        updateBarChart(transactions, givenPeriodOfTime);
    }

//--------------------------This is where the magic happens!!!!!!!!!------------------------------------------------------------------------
    private void updatePieChart(ObservableList<Transaction> transactions) {

    }

    private void updateBarChart(ObservableList<Transaction> transactions, String givenPeriodOfTime) {

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
