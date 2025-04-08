package org.project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;

    private ObservableList<Transaction> transactionData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupDateChoices();
        setupDateControls();
        loadCsvData("src/main/resources/testBudget.csv"); //This is where I stored the example csv file. The format was '2023-01-05,1000,Bills'
        generateReport();
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
            generateReport();
        });

        // this adds listeners for dates (1 day)
        dayDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> generateReport());

        //adds listener for dates (1 week)
        weekStartDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                weekEndDatePicker.setValue(newVal.plusDays(6));
                generateReport();
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

    @FXML
    private void generateReport() {
        String givenPeriodOfTime = timePeriod.getValue();   //get the value of a given period of time
        ObservableList<Transaction> filteredData = "All".equals(givenPeriodOfTime)
                ? transactionData   //no filter if 'all'
                : filterTransactions(getStartDate(givenPeriodOfTime), getEndDate(givenPeriodOfTime));   //pass start and end of dates for the filtering

        updateCharts(filteredData, givenPeriodOfTime);
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
        pieChart.getData().clear();
        barChart.getData().clear();

        if (transactions.isEmpty()) {
            pieChart.setTitle("No data here :( ");
            barChart.setTitle("No data here also :( ");
            return;
        }
        updatePieChart(transactions);
        updateBarChart(transactions, givenPeriodOfTime);
    }

//--------------------------This is where the magic happens!!!!!!!!!------------------------------------------------------------------------
    private void updatePieChart(ObservableList<Transaction> transactions) {

        pieChart.setTitle("What you've spent");
    }

    private void updateBarChart(ObservableList<Transaction> transactions, String givenPeriodOfTime) {
        barChart.setTitle("Also what you've spent");
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
