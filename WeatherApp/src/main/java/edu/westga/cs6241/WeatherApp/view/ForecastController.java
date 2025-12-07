package edu.westga.cs6241.WeatherApp.view;

import edu.westga.cs6241.WeatherApp.model.DailySummary;
import edu.westga.cs6241.WeatherApp.model.Meteorologist;
import edu.westga.cs6241.WeatherApp.model.WeatherAPI;
import edu.westga.cs6241.WeatherApp.model.WeatherDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForecastController {

    private static final Logger LOGGER =
            Logger.getLogger(ForecastController.class.getName());

    @FXML
    private ComboBox<String> stationCombo;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<DailySummary> forecastTable;

    @FXML
    private TableColumn<DailySummary, LocalDate> dateColumn;

    @FXML
    private TableColumn<DailySummary, String> stationColumn;

    @FXML
    private TableColumn<DailySummary, Number> hiColumn;

    @FXML
    private TableColumn<DailySummary, Number> loColumn;

    @FXML
    private TableColumn<DailySummary, Number> precipColumn;

    @FXML
    private Label selectedStationLabel;

    @FXML
    private Label selectedDateLabel;

    @FXML
    private Label selectedHiLabel;

    @FXML
    private Label selectedLoLabel;

    @FXML
    private Label selectedPrecipLabel;

    @FXML
    private Button useFirstDateButton;

    private WeatherDatabase database;
    private WeatherAPI api;
    private Meteorologist meteorologist;

    private final ObservableList<DailySummary> allData =
            FXCollections.observableArrayList();

    private final ObservableList<String> stations =
            FXCollections.observableArrayList();

    private final ObservableList<DailySummary> forecast =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        stationCombo.setDisable(true);
        datePicker.setDisable(true);
        useFirstDateButton.setDisable(true);

        forecastTable.setItems(forecast);

        dateColumn.setCellValueFactory(c ->
        	new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getDate()));

        dateColumn.setCellValueFactory(c ->
        	new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getDate()));

		stationColumn.setCellValueFactory(c ->
		    new javafx.beans.property.SimpleStringProperty(c.getValue().getStationName()));
		
		hiColumn.setCellValueFactory(c ->
		    new javafx.beans.property.SimpleIntegerProperty(c.getValue().getHiTemp()));
		
		loColumn.setCellValueFactory(c ->
		    new javafx.beans.property.SimpleIntegerProperty(c.getValue().getLoTemp()));
		
		precipColumn.setCellValueFactory(c ->
		    new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrecip()));

        stationCombo.valueProperty().addListener(
                (obs, oldValue, newValue) -> {
                    updateUseFirstDateButtonDisabled();
                    refreshForecast();
                });

        datePicker.valueProperty().addListener(
                (obs, oldValue, newValue) -> refreshForecast());

        forecast.addListener(
                (ListChangeListener<DailySummary>) change -> updateSummaryLabels());

        updateSummaryLabels();
    }

    @FXML
    private void handleOpenCsv(ActionEvent event) {
        Window window = stationCombo.getScene().getWindow();

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open weather CSV");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files", "*.csv"));

        File file = chooser.showOpenDialog(window);
        if (file == null) {
            return;
        }

        try {
            database = WeatherDatabase.from(file.getAbsolutePath());
            api = database;
            meteorologist = new Meteorologist(api);

            allData.setAll(api.queryBy(ds -> true));
            populateStations();

            stationCombo.setDisable(false);
            datePicker.setDisable(false);
            updateUseFirstDateButtonDisabled();

            stationCombo.getSelectionModel().clearSelection();
            datePicker.setValue(null);
            forecast.clear();

            LOGGER.log(Level.INFO, "Loaded {0} rows from {1}",
                    new Object[]{allData.size(), file.getName()});
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to load CSV", ex);
            stations.clear();
            forecast.clear();
            stationCombo.setDisable(true);
            datePicker.setDisable(true);
            useFirstDateButton.setDisable(true);
            updateSummaryLabels();
        }
    }

    @FXML
    private void handleUseFirstDateForStation(ActionEvent event) {
        String station = stationCombo.getValue();
        if (station == null) {
            return;
        }

        allData.stream()
                .filter(ds -> ds.getStationName().equals(station))
                .map(DailySummary::getDate)
                .min(Comparator.naturalOrder())
                .ifPresent(datePicker::setValue);
    }

    private void populateStations() {
        Set<String> set = new LinkedHashSet<>();
        allData.stream()
                .map(DailySummary::getStationName)
                .sorted()
                .forEach(set::add);

        stations.setAll(set);
        stationCombo.setItems(stations);
    }

    private void refreshForecast() {
        if (meteorologist == null) {
            forecast.clear();
            return;
        }

        String station = stationCombo.getValue();
        LocalDate start = datePicker.getValue();

        if (station == null || start == null) {
            forecast.clear();
            return;
        }

        List<DailySummary> seven =
                meteorologist.sevenDayForecast(
                        station,
                        start.getYear(),
                        start.getMonthValue(),
                        start.getDayOfMonth());

        forecast.setAll(seven);
    }

    private void updateSummaryLabels() {
        if (forecast.isEmpty()) {
            selectedStationLabel.setText("-");
            selectedDateLabel.setText("-");
            selectedHiLabel.setText("-");
            selectedLoLabel.setText("-");
            selectedPrecipLabel.setText("-");
            return;
        }

        DailySummary first = forecast.getFirst();
        selectedStationLabel.setText(first.getStationName());
        selectedDateLabel.setText(first.getDate().toString());
        selectedHiLabel.setText(Integer.toString(first.getHiTemp()));
        selectedLoLabel.setText(Integer.toString(first.getLoTemp()));
        selectedPrecipLabel.setText(Double.toString(first.getPrecip()));
    }

    private void updateUseFirstDateButtonDisabled() {
        boolean stationSelected = stationCombo.getValue() != null;
        useFirstDateButton.setDisable(!stationSelected || allData.isEmpty());
    }
}
