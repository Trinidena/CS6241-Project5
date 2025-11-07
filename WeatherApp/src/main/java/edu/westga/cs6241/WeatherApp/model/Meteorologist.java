package edu.westga.cs6241.WeatherApp.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class Meteorologist {
    private final WeatherAPI api;

    public Meteorologist(WeatherAPI api) {
        this.api = api;
    }

    public int findGlobalHiTemp(int year) {
        return api.queryBy(new YearSelection(year)).stream()
                .mapToInt(DailySummary::getHiTemp)
                .max()
                .orElseThrow();
    }

    public int getTotalRainfall(String station, int year) {
        return (int) api.queryBy(ds ->
                ds.getStationName().equals(station)
                && ds.getDate().getYear() == year)
                .stream()
                .mapToDouble(DailySummary::getPrecip)
                .sum();
    }

    public List<DailySummary> sevenDayForecast(String station, int year, int month, int day) {
        LocalDate start = LocalDate.of(year, month, day);
        List<DailySummary> all = api.queryBy(ds -> ds.getStationName().equals(station));
        all.sort(Comparator.comparing(DailySummary::getDate));
        int index = IntStream.range(0, all.size())
                .filter(i -> all.get(i).getDate().equals(start))
                .findFirst().orElse(-1);
        if (index == -1) return Collections.emptyList();
        return all.subList(index, Math.min(index + 7, all.size()));
    }
}
