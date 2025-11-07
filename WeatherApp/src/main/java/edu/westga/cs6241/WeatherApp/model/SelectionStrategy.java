package edu.westga.cs6241.WeatherApp.model;

public interface SelectionStrategy {
    boolean matches(DailySummary ds);
}
