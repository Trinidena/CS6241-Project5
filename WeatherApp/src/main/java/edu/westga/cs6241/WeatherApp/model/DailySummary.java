package edu.westga.cs6241.WeatherApp.model;

import java.time.LocalDate;

public class DailySummary {
    private final String stationName;
    private final LocalDate date;
    private final double precip;
    private final int hiTemp;
    private final int loTemp;

    public DailySummary(String stationName, LocalDate date, double precip, int hiTemp, int loTemp) {
        this.stationName = stationName;
        this.date = date;
        this.precip = precip;
        this.hiTemp = hiTemp;
        this.loTemp = loTemp;
    }

    public String getStationName() { return stationName; }
    public LocalDate getDate() { return date; }
    public double getPrecip() { return precip; }
    public int getHiTemp() { return hiTemp; }
    public int getLoTemp() { return loTemp; }
}
