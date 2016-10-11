package tars.model.task;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import tars.commons.exceptions.IllegalValueException;

/**
 * Represents a Task's datetime in tars.
 */
public class DateTime {
    public static final String MESSAGE_DATETIME_CONSTRAINTS = "Task datetime should be spaces or alphanumeric characters";

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public String startDateString;
    public String endDateString;
    
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/uuuu HHmm")
                .withResolverStyle(ResolverStyle.STRICT);
        DateTimeFormatter stringFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HHmm");

        if (endDate != null && endDate.length() > 0) {
            this.endDate = LocalDateTime.parse(endDate, formatter);
            this.endDateString = this.endDate.format(stringFormatter);
        }

        if (startDate != null && startDate.length() > 0) {
            this.startDate = LocalDateTime.parse(startDate, formatter);
            this.startDateString = this.startDate.format(stringFormatter);
            if (this.endDate.isBefore(this.startDate) || this.endDate.isEqual(this.startDate)) {
                throw new IllegalDateException("End dateTime should be after start dateTime.");
            }
        }

    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        if (this.startDate != null && this.endDate != null) {
            return startDateString + " to " + endDateString;
        } else if (this.startDate == null && this.endDate != null) {
            return endDateString;
        } else {
            return "";
        }
    }
    
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateTime // instanceof handles nulls
                && this.toString().equals(((DateTime) other).toString())); // state check
    }

    /**
     * Signals an error caused by end date occurring before start date
     */
    public class IllegalDateException extends IllegalValueException {
        public IllegalDateException(String message) {
            super(message);
        }
    }
    
}