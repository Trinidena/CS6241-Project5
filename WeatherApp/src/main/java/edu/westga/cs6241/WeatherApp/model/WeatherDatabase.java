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
                    .withFirstRecordAsHeader()
                    .parse(reader);

            for (CSVRecord record : parser) {
                String stationName = record.get("NAME");
                LocalDate date = LocalDate.parse(record.get("DATE"));
                double precip = Double.parseDouble(record.get("PRCP"));
                int hiTemp = Integer.parseInt(record.get("TMAX"));
                int loTemp = Integer.parseInt(record.get("TMIN"));

                DailySummary ds = new DailySummary(
                        stationName,
                        date,
                        precip,
                        hiTemp,
                        loTemp
                );
                summaries.add(ds);
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
