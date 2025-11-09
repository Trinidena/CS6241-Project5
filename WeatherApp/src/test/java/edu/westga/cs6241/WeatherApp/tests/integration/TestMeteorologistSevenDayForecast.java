package edu.westga.cs6241.WeatherApp.tests.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import edu.westga.cs6241.WeatherApp.model.Meteorologist;
import edu.westga.cs6241.WeatherApp.model.WeatherDatabase;

class TestMeteorologistSevenDayForecast {
	
	 private static void write(Path file, String content) throws IOException {
	        Files.writeString(file, content);
	    }
	
	 @Test
	    void sevenDayForecast_returnsExactlySevenWhenAvailable(@TempDir Path tmp) throws IOException {
	        Path csv = tmp.resolve("wx.csv");
	        var sb = new StringBuilder("NAME,DATE,PRCP,TMAX,TMIN\n");
	        for (int d = 1; d <= 10; d++) {
	            sb.append(String.format("ATL,2025-03-%02d,0.0,%d,%d%n", d, 60 + d, 40 + d));
	        }
	        write(csv, sb.toString());

	        var db  = WeatherDatabase.from(csv.toString());
	        var met = new Meteorologist(db);

	        var out = met.sevenDayForecast("ATL", 2025, 3, 3);

	        assertEquals(7, out.size());
	        assertEquals(LocalDate.parse("2025-03-03"), out.get(0).getDate());
	        assertEquals(LocalDate.parse("2025-03-09"), out.get(6).getDate());
	    }

	    @Test
	    void sevenDayForecast_shorterNearEndOfData(@TempDir Path tmp) throws IOException {
	        Path csv = tmp.resolve("wx.csv");
	        write(csv,
	        	"NAME,DATE,PRCP,TMAX,TMIN\n" +
	            "ATL,2025-12-29,0.0,50,30\n" +
	            "ATL,2025-12-30,0.0,51,31\n" +
	            "ATL,2025-12-31,0.0,52,32\n"
	        );

	        var db  = WeatherDatabase.from(csv.toString());
	        var met = new Meteorologist(db);

	        var out = met.sevenDayForecast("ATL", 2025, 12, 30);

	        assertEquals(2, out.size());
	        assertEquals(LocalDate.parse("2025-12-30"), out.get(0).getDate());
	        assertEquals(LocalDate.parse("2025-12-31"), out.get(1).getDate());
	    }

	    @Test
	    void sevenDayForecast_emptyWhenStartDateMissing(@TempDir Path tmp) throws IOException {
	        Path csv = tmp.resolve("wx.csv");
	        write(csv,
	        	"NAME,DATE,PRCP,TMAX,TMIN\n" +
	            "ATL,2025-03-01,0.0,60,40\n" +
	            "ATL,2025-03-03,0.0,62,42\n" 
	        );

	        var db  = WeatherDatabase.from(csv.toString());
	        var met = new Meteorologist(db);

	        assertTrue(met.sevenDayForecast("ATL", 2025, 3, 2).isEmpty());
	    }

}
