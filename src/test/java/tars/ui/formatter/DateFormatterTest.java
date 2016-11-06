package tars.ui.formatter;

import static org.junit.Assert.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

import tars.model.task.DateTime;
import tars.model.task.DateTime.IllegalDateException;

/**
 * Date formatter test
 * 
 * @@author A0139924W
 */
public class DateFormatterTest {

    @Test
    public void generateSingleDateFormat_todayCorrectFormat() {
        LocalDate testDate = LocalDate.now();
        LocalTime testTime = LocalTime.of(0, 0, 0);
        LocalDateTime testDateTime = LocalDateTime.of(testDate, testTime);

        assertEquals("Today at 12:00 AM",
                DateFormatter.generateSingleDateFormat(testDateTime));
    }

    @Test
    public void generateSingleDateFormat_tomorrowCorrectFormat() {
        LocalDate testDate = LocalDate.now().plusDays(1);
        LocalTime testTime = LocalTime.of(0, 0, 0);
        LocalDateTime testDateTime = LocalDateTime.of(testDate, testTime);

        assertEquals("Tomorrow at 12:00 AM",
                DateFormatter.generateSingleDateFormat(testDateTime));
    }

    @Test
    public void generateSingleDateFormat_otherDayCorrectFormat() {
        LocalDate testDate = LocalDate.of(2010, 10, 10);
        LocalTime testTime = LocalTime.of(0, 0, 0);
        LocalDateTime testDateTime = LocalDateTime.of(testDate, testTime);

        assertEquals("Sun, Oct 10 2010 12:00 AM",
                DateFormatter.generateSingleDateFormat(testDateTime));
    }

    @Test
    public void generateDateRangeFormat_sameDayCorrectFormat() {
        LocalDate testDateA = LocalDate.of(2010, 10, 10);
        LocalTime testTimeA = LocalTime.of(0, 0, 0);
        LocalDate testDateB = LocalDate.of(2010, 10, 10);
        LocalTime testTimeB = LocalTime.of(1, 0, 0);
        LocalDateTime testDateTimeA = LocalDateTime.of(testDateA, testTimeA);
        LocalDateTime testDateTimeB = LocalDateTime.of(testDateB, testTimeB);

        assertEquals("Sun, Oct 10 2010 12:00 AM - 01:00 AM", DateFormatter
                .generateDateRangeFormat(testDateTimeA, testDateTimeB));
    }

    @Test
    public void generateDateRangeFormat_diffDayCorrectFormat() {
        LocalDate testDateA = LocalDate.of(2010, 10, 10);
        LocalTime testTimeA = LocalTime.of(0, 0, 0);
        LocalDate testDateB = LocalDate.of(2010, 11, 10);
        LocalTime testTimeB = LocalTime.of(1, 0, 0);
        LocalDateTime testDateTimeA = LocalDateTime.of(testDateA, testTimeA);
        LocalDateTime testDateTimeB = LocalDateTime.of(testDateB, testTimeB);

        assertEquals("Sun, Oct 10 2010 12:00 AM - Wed, Nov 10 2010 01:00 AM",
                DateFormatter.generateDateRangeFormat(testDateTimeA,
                        testDateTimeB));
    }

    @Test
    public void formatDate_singleStartDateCorrectFormat()
            throws DateTimeException, IllegalDateException {
        DateTime dateTime = new DateTime("", "10/10/2010 1200");

        assertEquals("Sun, Oct 10 2010 12:00 PM",
                DateFormatter.formatDate(dateTime));
    }
}
