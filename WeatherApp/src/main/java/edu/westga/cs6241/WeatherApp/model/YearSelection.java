package edu.westga.cs6241.WeatherApp.model;

public class YearSelection implements SelectionStrategy {
    private final int year;

    public YearSelection(int year) { this.year = year; }

    public boolean matches(DailySummary ds) {
        return ds.getDate().getYear() == year;
    }
}
