package edu.westga.cs6241.WeatherApp.model;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class WeatherDatabase implements WeatherAPI {
    private final List<DailySummary> summaries;

    private WeatherDatabase(List<DailySummary> summaries) {
        this.summaries = summaries;
    }

    public static WeatherDatabase from(String filename) throws IOException {
        List<DailySummary> summaries = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {
            CSVParser parser = CSVFormat.DEFAULT
                .withHeader("stationName", "date", "precip", "hiTemp", "loTemp")
                .withSkipHeaderRecord()
                .parse(reader);

            for (CSVRecord record : parser) {
                summaries.add(new DailySummary(
                    record.get("stationName"),
                    LocalDate.parse(record.get("date")),
                    Double.parseDouble(record.get("precip")),
                    Integer.parseInt(record.get("hiTemp")),
                    Integer.parseInt(record.get("loTemp"))
                ));
            }
        }
        return new WeatherDatabase(summaries);
    }

    public List<DailySummary> queryBy(SelectionStrategy strategy) {
        return summaries.stream()
                .filter(strategy::matches)
                .toList();
    }
}
