package tars.commons.util;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tars.model.task.DateTime;

/**
 * Date Time Utility package
 */
public class DateTimeUtil {

    private static final String DATETIME_DAY = "day";
    private static final String DATETIME_WEEK = "week";
    private static final String DATETIME_MONTH = "month";
    private static final String DATETIME_YEAR = "year";
    private static final int DATETIME_INCREMENT = 1;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("d/M/uuuu HHmm");
    private static final DateTimeFormatter stringFormatter =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HHmm");
    private static final DateTimeFormatter stringFormatterWithoutTime =
            DateTimeFormatter.ofPattern("dd/MM/uuuu");
    private static final DateTimeFormatter stringFormatterWithoutDate =
            DateTimeFormatter.ofPattern("HHmm");

    /**
     * Extracts the new task's dateTime from the string arguments.
     * 
     * @@author A0139924W
     * @return String[] with first index being the startDate time and second index being the end
     *         date time
     */
    public static String[] parseStringToDateTime(String dateTimeArg) {
        return NattyDateTimeUtil.parseStringToDateTime(dateTimeArg);
    }

    /**
     * Checks if given endDateTime is within the start and end of this week
     * 
     * @@author A0121533W
     */
    public static boolean isWithinWeek(LocalDateTime endDateTime) {
        if (endDateTime == null) {
            return false;
        } else {
            LocalDateTime today =
                    LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            LocalDateTime startThisWeek =
                    today.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
            LocalDateTime endThisWeek =
                    today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            return endDateTime.isAfter(startThisWeek)
                    && endDateTime.isBefore(endThisWeek);
        }
    }

    /**
     * Checks if given endDateTime is before the end of today
     * 
     * @@author A0121533W
     */
    public static boolean isOverDue(LocalDateTime endDateTime) {
        if (endDateTime == null) {
            return false;
        } else {
            LocalDateTime now = LocalDateTime.now();
            return endDateTime.isBefore(now);
        }
    }

    /**
     * Checks whether the dateTimeQuery falls within the range of the dateTimeSource
     * 
     * @@author A0124333U
     * @param dateTimeSource
     * @param dateTimeQuery
     */
    public static boolean isDateTimeWithinRange(DateTime dateTimeSource,
            DateTime dateTimeQuery) {
        boolean isTaskDateWithinRange = true;

        // Return false if task is a floating task (i.e. no start or end
        // dateTime
        if (dateTimeSource.getEndDate() == null) {
            return false;
        }

        // Case 1: dateTimeQuery has a range of date (i.e. startDateTime &
        // endDateTime != null)
        if (dateTimeQuery.getStartDate() != null) {

            if (dateTimeSource.getEndDate()
                    .isBefore(dateTimeQuery.getStartDate())) {
                return false;
            }

            // Case 1a: dateTimeSource has a range of date
            if (dateTimeSource.getStartDate() != null) {
                if (dateTimeSource.getStartDate()
                        .isAfter(dateTimeQuery.getEndDate())) {
                    return false;
                }
            } else { // Case 1b: dateTimeSource only has a endDateTime
                if (dateTimeSource.getEndDate()
                        .isAfter(dateTimeQuery.getEndDate())) {
                    return false;
                }
            }
        } else { // Case 2: dateTimeQuery only has a endDateTime

            // Case 2a: dateTimeSource has a range of date
            if (dateTimeSource.getStartDate() != null) {
                if (dateTimeQuery.getEndDate()
                        .isBefore(dateTimeSource.getStartDate())
                        || dateTimeQuery.getEndDate()
                        .isAfter(dateTimeSource.getEndDate())) {
                    return false;
                }
            } else { // Case 2b: dateTimeSource only has a endDateTime
                if (!dateTimeQuery.getEndDate()
                        .equals(dateTimeSource.getEndDate())) {
                    return false;
                }
            }
        }

        return isTaskDateWithinRange;
    }

    /**
     * Returns an arraylist of free datetime slots in a specified date
     * 
     */
    public static ArrayList<DateTime> getListOfFreeTimeSlotsInDate(
            DateTime dateToCheck,
            ArrayList<DateTime> listOfFilledTimeSlotsInDate) {
        ArrayList<DateTime> listOfFreeTimeSlots = new ArrayList<DateTime>();
        LocalDateTime startDateTime = dateToCheck.getStartDate();
        LocalDateTime endDateTime;

        for (DateTime dt : listOfFilledTimeSlotsInDate) {
            if (dt.getStartDate() == null) {
                continue;
            } else {
                endDateTime = dt.getStartDate();
            }

            if (startDateTime.isBefore(endDateTime)) {
                listOfFreeTimeSlots
                .add(new DateTime(startDateTime, endDateTime));
            }

            if (startDateTime.isBefore(dt.getEndDate())) {
                startDateTime = dt.getEndDate();
            }
        }

        if (startDateTime.isBefore(dateToCheck.getEndDate())) {
            listOfFreeTimeSlots
            .add(new DateTime(startDateTime, dateToCheck.getEndDate()));
        }

        return listOfFreeTimeSlots;
    }

    public static String getDayAndDateString(DateTime dateTime) {
        StringBuilder sb = new StringBuilder();

        sb.append(dateTime.getEndDate().getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH)).append(", ")
        .append(dateTime.getEndDate()
                .format(stringFormatterWithoutTime));

        return sb.toString();
    }

    public static String getStringOfFreeDateTimeInDate(DateTime dateToCheck,
            ArrayList<DateTime> listOfFreeTimeSlotsInDate) {
        StringBuilder sb = new StringBuilder();

        sb.append(getDayAndDateString(dateToCheck)).append(":");

        int counter = 1;

        for (DateTime dt : listOfFreeTimeSlotsInDate) {
            sb.append("\n").append(counter).append(". ")
            .append(dt.getStartDate()
                    .format(stringFormatterWithoutDate))
            .append("hrs to ")
            .append(dt.getEndDate().format(stringFormatterWithoutDate))
            .append("hrs (")
            .append(getDurationInMinutesBetweenTwoLocalDateTime(
                    dt.getStartDate(), dt.getEndDate()))
            .append(")");

            counter++;
        }

        return sb.toString();

    }

    public static String getDurationInMinutesBetweenTwoLocalDateTime(
            LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Duration duration = Duration.between(startDateTime, endDateTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        return hours + " hr " + minutes + " min";
    }

    /**
     * Modify the date based on the new hour, min and sec
     * 
     * @@author A0139924W
     */
    public static Date setDateTime(Date toBeEdit, int hour, int min, int sec) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toBeEdit);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        toBeEdit = calendar.getTime();

        return toBeEdit;
    }

    /**
     * Modifies the date based on the frequency for recurring tasks
     * 
     * @@author A0140022H
     */
    public static String modifyDate(String dateToModify, String frequency) {
        LocalDateTime date = LocalDateTime.parse(dateToModify, formatter);

        switch (frequency.toLowerCase()) {
            case DATETIME_DAY:
                date = date.plusDays(DATETIME_INCREMENT);
                break;
            case DATETIME_WEEK:
                date = date.plusWeeks(DATETIME_INCREMENT);
                break;
            case DATETIME_MONTH:
                date = date.plusMonths(DATETIME_INCREMENT);
                break;
            case DATETIME_YEAR:
                date = date.plusYears(DATETIME_INCREMENT);
                break;
        }

        dateToModify = date.format(stringFormatter);
        return dateToModify;
    }

    public static LocalDateTime setLocalTime(LocalDateTime dateTime, int hour,
            int min, int sec) {
        return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(),
                dateTime.getDayOfMonth(), hour, min, sec);
    }
}
