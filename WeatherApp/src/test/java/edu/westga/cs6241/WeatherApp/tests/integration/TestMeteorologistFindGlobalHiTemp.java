package edu.westga.cs6241.WeatherApp.tests.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import edu.westga.cs6241.WeatherApp.model.Meteorologist;
import edu.westga.cs6241.WeatherApp.model.WeatherDatabase;

class TestMeteorologistFindGlobalHiTemp {

	private static void write(Path file, String content) throws IOException {
        Files.writeString(file, content);
    }

    @Test
    void findGlobalHiTemp_maxAcrossStationsSameYear(@TempDir Path tmp) throws IOException {
        Path csv = tmp.resolve("wx.csv");
        write(csv,
        	"NAME,DATE,PRCP,TMAX,TMIN\n" +
            "ATL,2024-01-01,0.0,55,34\n" +
            "NYC,2024-06-05,0.1,97,70\n" +
            "LAX,2024-08-20,0.0,101,72\n" +
            "SEA,2024-09-01,0.0,88,60\n" +
            "ATL,2023-01-01,0.0,110,0\n" 
        );

        var db  = WeatherDatabase.from(csv.toString()); 
        var met = new Meteorologist(db);

        assertEquals(101, met.findGlobalHiTemp(2024));
    }

    @Test
    void findGlobalHiTemp_handlesTies(@TempDir Path tmp) throws IOException {
        Path csv = tmp.resolve("wx.csv");
        write(csv,
        	"NAME,DATE,PRCP,TMAX,TMIN\n" +
            "ATL,2025-07-01,0.0,100,70\n" +
            "PHX,2025-07-02,0.0,100,80\n"
        );

        var db  = WeatherDatabase.from(csv.toString());
        var met = new Meteorologist(db);

        assertEquals(100, met.findGlobalHiTemp(2025));
    }

    @Test
    void findGlobalHiTempNoDataForYearReturnsMinInt(@TempDir Path tmp) throws IOException {
        Path csv = tmp.resolve("wx.csv");
        write(csv,
        	"NAME,DATE,PRCP,TMAX,TMIN\n" +
            "ATL,2023-01-01,0.0,10,0\n"
        );

        var db  = WeatherDatabase.from(csv.toString());
        var met = new Meteorologist(db);

        assertThrows(NoSuchElementException.class, () -> met.findGlobalHiTemp(2024));
    }

}
