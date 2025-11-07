package edu.westga.cs6241.WeatherApp.tests.unit;

import edu.westga.cs6241.WeatherApp.model.DailySummary;
import edu.westga.cs6241.WeatherApp.model.WeatherDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherDatabaseFromCsvTest {

    private static final Logger LOGGER =
            Logger.getLogger(WeatherDatabaseFromCsvTest.class.getName());

    @Test
    void fromLoadsDailySummariesFromCsv(@TempDir Path tempDir) throws Exception {
        Path csvPath = tempDir.resolve("weather.csv");

        String csvContent = String.join(System.lineSeparator(),
                "STATION,NAME,DATE,PRCP,TMAX,TMIN",
                "S1,ALPHA,2012-01-01,1.00,80,60",
                "S1,ALPHA,2012-01-02,3.00,95,65"
        );

        Files.writeString(csvPath, csvContent, StandardCharsets.UTF_8);

        WeatherDatabase database = WeatherDatabase.from(csvPath.toString());
        List<DailySummary> results = database.queryBy(ds -> true);

        LOGGER.info("Loaded " + results.size() + " rows from CSV");

        DailySummary first = results.get(0);
        DailySummary second = results.get(1);

        assertAll(
                () -> assertEquals(2, results.size()),
                () -> assertEquals("ALPHA", first.getStationName()),
                () -> assertEquals(LocalDate.of(2012, 1, 1), first.getDate()),
                () -> assertEquals(1.0, first.getPrecip(), 1e-6),
                () -> assertEquals(80, first.getHiTemp()),
                () -> assertEquals(60, first.getLoTemp()),
                () -> assertEquals("ALPHA", second.getStationName()),
                () -> assertEquals(LocalDate.of(2012, 1, 2), second.getDate()),
                () -> assertEquals(3.0, second.getPrecip(), 1e-6),
                () -> assertEquals(95, second.getHiTemp()),
                () -> assertEquals(65, second.getLoTemp())
        );
    }
}
