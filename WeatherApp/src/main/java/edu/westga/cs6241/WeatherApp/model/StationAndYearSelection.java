package edu.westga.cs6241.WeatherApp.model;

public class StationAndYearSelection implements SelectionStrategy {
	private final String station;
    private final int year;
    
    public StationAndYearSelection (String station, int year) {
        this.station = station; 
        this.year = year;
    }
    
    public boolean matches(DailySummary ds) {
        return ds.getStationName().equals(station) && ds.getDate().getYear() == year;
    }
}
