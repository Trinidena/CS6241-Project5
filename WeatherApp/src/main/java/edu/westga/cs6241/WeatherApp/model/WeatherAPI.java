package edu.westga.cs6241.WeatherApp.model;

import java.util.List;

public interface WeatherAPI {
    List<DailySummary> queryBy(SelectionStrategy strategy);
}
