package edu.westga.cs6241.WeatherApp.tests.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.westga.cs6241.WeatherApp.model.DailySummary;
import edu.westga.cs6241.WeatherApp.model.Meteorologist;
import edu.westga.cs6241.WeatherApp.model.SelectionStrategy;
import edu.westga.cs6241.WeatherApp.model.WeatherAPI;


class TestMeteorologist {
	
	private WeatherAPI mockAPI;
	private Meteorologist theMeterologist;
	
	private static DailySummary theDailySummary (String st, String ymd, double precip, int hi, int lo) {
        return new DailySummary(st, LocalDate.parse(ymd), precip, hi, lo);
	}
	
	@BeforeEach
	public void setup() {
		this.mockAPI = mock(WeatherAPI.class);
		this.theMeterologist = new Meteorologist(this.mockAPI);
	}
	
	@Test
	void testfindGlobalHiTempReturnsMaxAcrossAllStationsInYear() {
		when(mockAPI.queryBy(any())).thenReturn(List.of(
				theDailySummary("ATL", "2024-01-01", 0.0, 55, 34),
				theDailySummary("NYC", "2024-06-05", 0.1, 97, 70),
				theDailySummary("LAX", "2024-08-20", 0.0, 101, 72)
        ));
		
		
		assertEquals(101, this.theMeterologist.findGlobalHiTemp(2024));
		
		verify(mockAPI, times(1)).queryBy(any(SelectionStrategy.class));
        verifyNoMoreInteractions(mockAPI);
	}
	
	 @Test
	    void getTotalRainfallSumsOnlyWhatAPIReturns() {
	        when(mockAPI.queryBy(any())).thenReturn(List.of(
	        		theDailySummary("ATL", "2023-02-01", 1.2, 60, 40),
	        		theDailySummary("ATL", "2023-02-02", 0.0, 58, 39),
	        		theDailySummary("ATL", "2023-02-03", 2.6, 55, 37)
	        ));

	        int total = theMeterologist.getTotalRainfall("ATL", 2023);

	        assertEquals( (int)(1.2 + 0.0 + 2.6), total);
	        verify(mockAPI).queryBy(any(SelectionStrategy.class));
	    }
	 
	 @Test
	    void sevenDayForecast_returnsExactlySevenWhenAvailable() {
	       
	        when(mockAPI.queryBy(any())).thenReturn((List.of(
	        	theDailySummary("ATL","2025-03-01",0.1,60,40),
	        	theDailySummary("ATL","2025-03-02",0.0,61,41),
	        	theDailySummary("ATL","2025-03-03",0.0,62,42),
	        	theDailySummary("ATL","2025-03-04",0.0,63,43),
	        	theDailySummary("ATL","2025-03-05",0.0,64,44),
	        	theDailySummary("ATL","2025-03-06",0.0,65,45),
	        	theDailySummary("ATL","2025-03-07",0.0,66,46),
	        	theDailySummary("ATL","2025-03-08",0.0,67,47),
	        	theDailySummary("ATL","2025-03-09",0.0,68,48),
	        	theDailySummary("ATL","2025-03-10",0.0,69,49)
	        )));

	        var out = theMeterologist.sevenDayForecast("ATL", 2025, 3, 3);

	        assertEquals(7, out.size());
	        assertEquals(LocalDate.parse("2025-03-03"), out.get(0).getDate());
	        assertEquals(LocalDate.parse("2025-03-09"), out.get(6).getDate());
	        verify(mockAPI).queryBy(any(SelectionStrategy.class));
	    }
	 
	 @Test
	    void sevenDayForecast_returnsShortListNearEndOfData() {
	        when(mockAPI.queryBy(any())).thenReturn(List.of(
	        	theDailySummary("ATL","2025-12-29",0.0,50,30),
	        	theDailySummary("ATL","2025-12-30",0.0,51,31),
	            theDailySummary("ATL","2025-12-31",0.0,52,32)
	        ));

	        var out = theMeterologist.sevenDayForecast("ATL", 2025, 12, 30);

	        assertEquals(2, out.size());
	        assertEquals(LocalDate.parse("2025-12-30"), out.get(0).getDate());
	        assertEquals(LocalDate.parse("2025-12-31"), out.get(1).getDate());
	        verify(mockAPI).queryBy(any(SelectionStrategy.class));
	    }
	 
	 @Test
	    void sevenDayForecast_returnsEmptyWhenStartDateNotPresent() {
	        when(mockAPI.queryBy(any())).thenReturn(List.of(
	        	theDailySummary("ATL","2025-03-01",0.0,60,40),
	            theDailySummary("ATL","2025-03-03",0.0,62,42) 
	        ));

	        var out = theMeterologist.sevenDayForecast("ATL", 2025, 3, 2);

	        assertTrue(out.isEmpty());
	        verify(mockAPI).queryBy(any(SelectionStrategy.class));
	    }
}
