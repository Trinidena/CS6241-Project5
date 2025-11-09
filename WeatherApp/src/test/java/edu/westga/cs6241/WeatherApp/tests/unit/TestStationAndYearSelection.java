package edu.westga.cs6241.WeatherApp.tests.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import edu.westga.cs6241.WeatherApp.model.DailySummary;
import edu.westga.cs6241.WeatherApp.model.StationAndYearSelection;

class TestStationAndYearSelection {

	 @Test
	    void matchesTrueThenStationAndYearMatch() {
	        DailySummary theDailySummary = mock(DailySummary.class);
	        when(theDailySummary.getStationName()).thenReturn("ATL");
	        when(theDailySummary.getDate()).thenReturn(LocalDate.of(2025, 6, 15));

	        var strat = new StationAndYearSelection("ATL", 2025);

	        assertTrue(strat.matches(theDailySummary));

	        InOrder inOrder = Mockito.inOrder(theDailySummary);
	        inOrder.verify(theDailySummary).getStationName();
	        inOrder.verify(theDailySummary).getDate();
	        verifyNoMoreInteractions(theDailySummary);
	    }
	 
	 @Test
	    void matchesFalseWhenStationDiffersAndShortCircuitsBeforeDate() {
	        DailySummary ds = mock(DailySummary.class);
	        when(ds.getStationName()).thenReturn("NYC"); 

	        var strat = new StationAndYearSelection("ATL", 2025);

	        assertFalse(strat.matches(ds));

	        verify(ds, times(1)).getStationName();
	        verify(ds, never()).getDate();   
	        verifyNoMoreInteractions(ds);
	    }

	    @Test
	    void matchesFalseWhenYearDiffers() {
	        DailySummary ds = mock(DailySummary.class);
	        when(ds.getStationName()).thenReturn("ATL"); 
	        when(ds.getDate()).thenReturn(LocalDate.of(2024, 12, 31)); 

	        var strat = new StationAndYearSelection("ATL", 2025);

	        assertFalse(strat.matches(ds));


	        verify(ds).getStationName();
	        verify(ds).getDate();
	        verifyNoMoreInteractions(ds);
	    }

	    @Test
	    void matchesIsCaseSensitiveBecauseStringEqualsIsUsed() {
	        DailySummary ds = mock(DailySummary.class);
	        when(ds.getStationName()).thenReturn("atl"); 
	        when(ds.getDate()).thenReturn(LocalDate.of(2025, 1, 1));

	        var strat = new StationAndYearSelection("ATL", 2025);

	        assertFalse(strat.matches(ds));
	        verify(ds).getStationName();

	        verify(ds, atMostOnce()).getDate();
	        verifyNoMoreInteractions(ds);
	    }

	    @Test
	    void matchesReturnsFalseIfCtorStationIsNullAndSummaryStationIsNonNull() {
	        DailySummary ds = mock(DailySummary.class);
	        when(ds.getStationName()).thenReturn("ATL");

	        var strat = new StationAndYearSelection(null, 2025);

	        assertFalse(strat.matches(ds));

	        verify(ds).getStationName();
	        verify(ds, never()).getDate(); 
	    }

	    @Test
	    void matchesThrowsNpeWhenSummaryStationIsNull() {
	        DailySummary ds = mock(DailySummary.class);
	        when(ds.getStationName()).thenReturn(null); // this triggers NPE in .equals(station)

	        var strat = new StationAndYearSelection("ATL", 2025);

	        assertThrows(NullPointerException.class, () -> strat.matches(ds));
	        verify(ds).getStationName();
	        verifyNoMoreInteractions(ds);
	    }

	    @Test
	    void matchesThrowsNpeWhenSummaryDateIsNullAndStationMatches() {
	        DailySummary ds = mock(DailySummary.class);
	        when(ds.getStationName()).thenReturn("ATL");
	        when(ds.getDate()).thenReturn(null);

	        var strat = new StationAndYearSelection("ATL", 2025);

	        assertThrows(NullPointerException.class, () -> strat.matches(ds));

	        verify(ds).getStationName();
	        verify(ds).getDate();
	        verifyNoMoreInteractions(ds);
	    }

}
