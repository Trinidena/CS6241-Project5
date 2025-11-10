package edu.westga.cs6241.WeatherApp.tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import edu.westga.cs6241.WeatherApp.model.ClimateReporter;
import edu.westga.cs6241.WeatherApp.model.WeatherDatabase;

class TestClimateReporter {

    private static void write(Path file, String content) throws IOException {
        Files.writeString(file, content);
    }

    @Test
    void totalFrozenPrecipitationSumsOnlyColdDaysForStationAndYear(@TempDir Path tmp) throws IOException {
        Path csv = tmp.resolve("wx.csv");
        write(csv,
            "NAME,DATE,PRCP,TMAX,TMIN\n" +
            "ATL,2025-01-01,1.0,40,30\n" +
            "ATL,2025-01-02,2.5,45,35\n" +
            "ATL,2024-12-31,5.0,30,20\n" +
            "BHM,2025-01-01,7.0,25,10\n"
        );

        var db = WeatherDatabase.from(csv.toString());
        var reporter = new ClimateReporter(db);

        double total = reporter.getTotalFrozenPrecipitation("ATL", 2025);

        assertEquals(1.0, total, 1e-6);
    }

    @Test
    void totalFrozenPrecipitationReturnsZeroWhenNoMatchingDays(@TempDir Path tmp) throws IOException {
        Path csv = tmp.resolve("wx.csv");
        write(csv,
            "NAME,DATE,PRCP,TMAX,TMIN\n" +
            "ATL,2025-01-01,1.0,40,40\n" +
            "BHM,2025-01-01,1.0,20,10\n"
        );

        var db = WeatherDatabase.from(csv.toString());
        var reporter = new ClimateReporter(db);

        double total = reporter.getTotalFrozenPrecipitation("ATL", 2025);

        assertEquals(0.0, total, 1e-6);
    }

    @Test
    void yearlyAverageTempUsesMeanOfHiAndLoAcrossYear(@TempDir Path tmp) throws IOException {
        Path csv = tmp.resolve("wx.csv");
        write(csv,
            "NAME,DATE,PRCP,TMAX,TMIN\n" +
            "ATL,2025-01-01,0.0,70,50\n" +
            "ATL,2025-01-02,0.0,80,40\n" +
            "ATL,2024-12-31,0.0,0,0\n" +
            "BHM,2025-01-01,0.0,100,0\n"
        );

        var db = WeatherDatabase.from(csv.toString());
        var reporter = new ClimateReporter(db);

        double avg = reporter.getYearlyAverageTemp("ATL", 2025);

        assertEquals(60.0, avg, 1e-6);
    }

    @Test
    void yearlyAverageTempReturnsZeroWhenNoData(@TempDir Path tmp) throws IOException {
        Path csv = tmp.resolve("wx.csv");
        write(csv,
            "NAME,DATE,PRCP,TMAX,TMIN\n" +
            "ATL,2024-01-01,0.0,70,50\n"
        );

        var db = WeatherDatabase.from(csv.toString());
        var reporter = new ClimateReporter(db);

        double avg = reporter.getYearlyAverageTemp("ATL", 2025);

        assertEquals(0.0, avg, 1e-6);
    }

    @Test
    void extremeDaysCountsHighAndLowExtremeDaysInYear(@TempDir Path tmp) throws IOException {
        Path csv = tmp.resolve("wx.csv");
        write(csv,
            "NAME,DATE,PRCP,TMAX,TMIN\n" +
            "ATL,2025-01-01,0.0,101,20\n" +
            "ATL,2025-01-02,0.0,50,-11\n" +
            "ATL,2025-01-03,0.0,100,-10\n" +
            "ATL,2024-01-01,0.0,105,0\n"
        );

        var db = WeatherDatabase.from(csv.toString());
        var reporter = new ClimateReporter(db);

        int count = reporter.getExtremeDaysIn(2025);

        assertEquals(2, count);
    }
}
