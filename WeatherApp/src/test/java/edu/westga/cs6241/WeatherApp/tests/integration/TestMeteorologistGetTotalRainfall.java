package edu.westga.cs6241.WeatherApp.tests.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import edu.westga.cs6241.WeatherApp.model.Meteorologist;
import edu.westga.cs6241.WeatherApp.model.WeatherDatabase;

class TestMeteorologistGetTotalRainfall {

	 private static void write(Path file, String content) throws IOException {
	        Files.writeString(file, content);
	    }
	
	@Test
    void getTotalRainfallSumsByStationAndYear(@TempDir Path tmp) throws IOException {
        Path csv = tmp.resolve("wx.csv");
        write(csv,
        	"NAME,DATE,PRCP,TMAX,TMIN\n" +
            "ATL,2023-02-01,1.2,60,40\n" +
            "ATL,2023-02-02,0.0,61,41\n" +
            "ATL,2023-02-03,2.6,62,42\n" +
            "NYC,2023-02-01,5.0,40,30\n" // other station
        );

        var db  = WeatherDatabase.from(csv.toString());
        var met = new Meteorologist(db);

       
        assertEquals((int)(1.2 + 0.0 + 2.6), met.getTotalRainfall("ATL", 2023));
    }

    @Test
    void getTotalRainfallZeroWhenNoMatches(@TempDir Path tmp) throws IOException {
        Path csv = tmp.resolve("wx.csv");
        write(csv,
        	"NAME,DATE,PRCP,TMAX,TMIN\n" +
            "ATL,2024-01-01,1.0,60,40\n"
        );

        var db  = WeatherDatabase.from(csv.toString());
        var met = new Meteorologist(db);

        assertEquals(0, met.getTotalRainfall("NYC", 2024));
    }

    @Test
    void getTotalRainfallFractionBehaviorIsDeterministic(@TempDir Path tmp) throws IOException {
        Path csv = tmp.resolve("wx.csv");
        write(csv,
        	"NAME,DATE,PRCP,TMAX,TMIN\n" +
            "ATL,2025-01-01,0.9,50,30\n" +
            "ATL,2025-01-02,0.9,51,31\n"
        );

        var db  = WeatherDatabase.from(csv.toString());
        var met = new Meteorologist(db);

        assertEquals(1, met.getTotalRainfall("ATL", 2025));
    }

}
