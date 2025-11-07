package edu.westga.cs6241.WeatherApp.model;

public class ClimateReporter {
    private final WeatherAPI api;

    public ClimateReporter(WeatherAPI api) {
        this.api = api;
    }

    public double getTotalFrozenPrecipitation(String station, int year) {
        return api.queryBy(ds ->
                ds.getStationName().equals(station)
                && ds.getDate().getYear() == year
                && ds.getLoTemp() <= 32)
                .stream()
                .mapToDouble(DailySummary::getPrecip)
                .sum();
    }

    public double getYearlyAverageTemp(String station, int year) {
        return api.queryBy(ds ->
                ds.getStationName().equals(station)
                && ds.getDate().getYear() == year)
                .stream()
                .mapToDouble(ds -> (ds.getHiTemp() + ds.getLoTemp()) / 2.0)
                .average()
                .orElse(0.0);
    }

    public int getExtremeDaysIn(int year) {
        return (int) api.queryBy(ds -> ds.getDate().getYear() == year
                && (ds.getHiTemp() > 100 || ds.getLoTemp() < -10))
                .size();
    }
}
