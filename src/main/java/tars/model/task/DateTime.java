package tars.model.task;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import tars.commons.core.Messages;
import tars.commons.exceptions.IllegalValueException;

/**
 * Represents a Task's dateTime in tars.
 */
public class DateTime implements Comparable<DateTime> {

    public static final String MESSAGE_DATETIME_CONSTRAINTS =
            "Task datetime should be spaces or alphanumeric characters";

    private static final int COMPARE_TO_EQUALS = 0;
    private static final int COMPARE_TO_SMALLER = -1;
    private static final int COMPARE_TO_GREATER = 1;

    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("d/M/uuuu HHmm").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter stringFormatter =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HHmm");

    private static final String DATETIME_STRING_TO = " to ";
    private static final String DATETIME_STRING_EMPTY = "";
    private static final int DATETIME_SIZE_EMPTY = 0;

    public String startDateString;
    public String endDateString;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    /**
     * Default constructor
     */
    public DateTime() {}

    /**
     * Validates given task dateTime.
     *
     * @throws DateTimeException if given dateTime string is invalid.
     * @throws IllegalDateException end date occurring before start date.
     */
    public DateTime(String startDate, String endDate)
            throws DateTimeException, IllegalDateException {
        if (endDate != null && endDate.length() > DATETIME_SIZE_EMPTY) {
            this.endDate = LocalDateTime.parse(endDate, formatter);
            this.endDateString = this.endDate.format(stringFormatter);
        }

        if (startDate != null && startDate.length() > DATETIME_SIZE_EMPTY) {
            this.startDate = LocalDateTime.parse(startDate, formatter);
            this.startDateString = this.startDate.format(stringFormatter);
            if (this.endDate.isBefore(this.startDate)
                    || this.endDate.isEqual(this.startDate)) {
                throw new IllegalDateException(
                        Messages.MESSAGE_INVALID_END_DATE);
            }
        }
    }

    public DateTime(LocalDateTime startDate, LocalDateTime endDate) {
        assert (startDate != null && endDate != null);

        this.startDate = startDate;
        this.endDate = endDate;
        this.endDateString = this.endDate.format(stringFormatter);
        this.startDateString = this.startDate.format(stringFormatter);
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    @Override
    public String toString() {
        if (this.startDate != null && this.endDate != null) {
            return startDateString + DATETIME_STRING_TO + endDateString;
        } else if (this.endDate != null) {
            return endDateString;
        } else {
            return DATETIME_STRING_EMPTY;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateTime // instanceof handles nulls
                        && this.toString()
                                .equals(((DateTime) other).toString())); // state check
    }

    /**
     * Signals an error caused by end date occurring before start date
     */
    public class IllegalDateException extends IllegalValueException {
        public IllegalDateException(String message) {
            super(message);
        }
    }

    @Override
    public int compareTo(DateTime o) {
        if (this.startDate == null && this.endDate == null) {
            if (o.startDate == null && o.endDate == null) {
                return COMPARE_TO_EQUALS;
            } else {
                return COMPARE_TO_SMALLER;
            }
        }

        if (this.startDate == null && this.endDate != null) {
            if (o.startDate != null && o.endDate != null) {
                return this.endDate.compareTo(o.startDate);
            } else if (o.endDate != null) {
                return this.endDate.compareTo(o.endDate);
            } else {
                return COMPARE_TO_GREATER;
            }
        }

        if (this.startDate != null && this.endDate != null) {
            if (o.startDate != null && o.endDate != null) {
                int result = this.startDate.compareTo(o.startDate);
                if (result == COMPARE_TO_EQUALS) {
                    return this.endDate.compareTo(o.endDate);
                } else {
                    return result;
                }
            } else if (o.startDate == null && o.endDate != null) {
                return this.startDate.compareTo(o.endDate);
            } else {
                return COMPARE_TO_GREATER;
            }
        }

        return COMPARE_TO_EQUALS;
    }

    public void setStartDateTime(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDateTime(LocalDateTime endDate) {
        this.endDate = endDate;
    }

}
