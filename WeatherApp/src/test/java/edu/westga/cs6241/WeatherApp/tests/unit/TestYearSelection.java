package edu.westga.cs6241.WeatherApp.tests.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import edu.westga.cs6241.WeatherApp.model.DailySummary;
import edu.westga.cs6241.WeatherApp.model.YearSelection;

class TestYearSelection {

	@Test
    void matches_returnsTrue_whenYearMatches() {
        DailySummary theDailySummary = mock(DailySummary.class);
        when(theDailySummary.getDate()).thenReturn(LocalDate.of(2025, 3, 1));

        YearSelection strat = new YearSelection(2025);

        assertTrue(strat.matches(theDailySummary));

        // Only date should be consulted
        verify(theDailySummary, times(1)).getDate();
        verify(theDailySummary, never()).getStationName();
        verify(theDailySummary, never()).getPrecip();
        verify(theDailySummary, never()).getHiTemp();
        verify(theDailySummary, never()).getLoTemp();
        verifyNoMoreInteractions(theDailySummary);
    }

    @Test
    void matches_returnsFalse_whenYearDiffers() {
        DailySummary theDailySummary = mock(DailySummary.class);
        when(theDailySummary.getDate()).thenReturn(LocalDate.of(2024, 12, 31));

        YearSelection strat = new YearSelection(2025);

        assertFalse(strat.matches(theDailySummary));
        verify(theDailySummary).getDate();
        verifyNoMoreInteractions(theDailySummary);
    }

    @Test
    void matches_throwsWhenDateIsNull() {
        // If your contract allows/denies null, test accordingly.
        DailySummary theDailySummary = mock(DailySummary.class);
        when(theDailySummary.getDate()).thenReturn(null);

        YearSelection strat = new YearSelection(2025);

        assertThrows(NullPointerException.class, () -> strat.matches(theDailySummary));
        verify(theDailySummary).getDate();
        verifyNoMoreInteractions(theDailySummary);
    }

    @Test
    void matches_throwsWhenDailySummaryIsNull() {
        YearSelection strat = new YearSelection(2025);
        assertThrows(NullPointerException.class, () -> strat.matches(null));
    }

}
