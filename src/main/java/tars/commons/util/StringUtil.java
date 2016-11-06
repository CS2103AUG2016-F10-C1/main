package tars.commons.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import tars.commons.exceptions.IllegalValueException;
import tars.commons.exceptions.InvalidRangeException;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    public static final String REGEX_WHITESPACE = "\\s+";
    private static final String REGEX_UNSIGNED_INTEGER = "^0*[1-9]\\d*$";
    private static final String INVALID_INDEX_ENTERED = "Invalid index entered";
    private static final String UNEXPECTED_ERROR_IN_GETING_INDEX_FROM_STRING =
            "Unexpected error in geting index from String.";
    public static final String STRING_NEWLINE = "\n";
    public static final String STRING_COLON = ": ";
    public static final String STRING_COMMA = ", ";
    public static final String STRING_FULLSTOP = ". ";
    public static final String STRING_FORWARD_SLASH = "/";
    public static final String STRING_SQUARE_BRACKET_OPEN = "[";
    public static final String STRING_SQUARE_BRACKET_CLOSE = "]";
    public static final String EMPTY_STRING = "";
    public static final String STRING_WHITESPACE = " ";
    private static final int EMPTY_STREAM_LIST = 0;
    public static final int EMPTY_STRING_LENGTH = 0;
    public static final int START_INDEX = 0;
    public static final int LAST_INDEX = 1;
    public static final int INVALID_POSITION = -1;

    private static final String RANGE_SEPARATOR = "..";

    public static boolean containsIgnoreCase(String source, String query) {
        String[] split = source.toLowerCase().split(REGEX_WHITESPACE);
        List<String> strings = Arrays.asList(split);
        return strings.stream().filter(s -> s.equals(query.toLowerCase()))
                .count() > EMPTY_STREAM_LIST;
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + STRING_NEWLINE + sw.toString();
    }

    /**
     * Returns true if s represents an unsigned integer e.g. 1, 2, 3, ... <br>
     * Will return false for null, empty string, "-1", "0", "+1", and " 2 " (untrimmed) "3 0"
     * (contains whitespace).
     * 
     * @param s should be trimmed.
     */
    public static boolean isUnsignedInteger(String s) {
        return s != null && s.matches(REGEX_UNSIGNED_INTEGER);
    }

    /**
     * Handles three different cases of strings and return them in the appropriate format
     * 
     * @@author A0121533W
     */
    public static String indexString(String s)
            throws InvalidRangeException, IllegalValueException {
        if (s.isEmpty()) {
            return s;
        }
        if (isSingleNumber(s)) {
            return getIndexForSingleNumber(s);
        } else if (isListOfIndexes(s)) {
            return getIndexesForList(s);
        } else if (isRangeOfIndexes(s)) {
            return getIndexesForRange(s);
        } else {
            throw new IllegalValueException(
                    UNEXPECTED_ERROR_IN_GETING_INDEX_FROM_STRING);
        }
    }

    private static boolean isSingleNumber(String s) {
        return (s.indexOf(STRING_WHITESPACE) == INVALID_POSITION
                && !s.contains(RANGE_SEPARATOR));
    }

    /**
     * Returns a valid single index
     */
    private static String getIndexForSingleNumber(String s)
            throws IllegalValueException {
        if (!isUnsignedInteger(s)) {
            throw new IllegalValueException(INVALID_INDEX_ENTERED);
        }
        return s;
    }

    private static boolean isListOfIndexes(String s) {
        return (s.indexOf(STRING_WHITESPACE) != INVALID_POSITION
                && !s.contains(RANGE_SEPARATOR));
    }

    /**
     * Returns a valid list of indexes
     * 
     * @@author A0121533W
     */
    private static String getIndexesForList(String s)
            throws IllegalValueException {
        String indexString = EMPTY_STRING;
        String[] indexArray = s.split(STRING_WHITESPACE);
        for (int i = START_INDEX; i < indexArray.length; i++) {
            String index = getIndexForSingleNumber(indexArray[i]);
            indexString += index + STRING_WHITESPACE;
        }
        return indexString.trim();
    }

    private static boolean isRangeOfIndexes(String s) {
        return s.contains(RANGE_SEPARATOR);
    }

    /**
     * Returns a list of indexes separated by white space from a range of indexes
     * 
     * @@author A0121533W
     */
    private static String getIndexesForRange(String s)
            throws IllegalValueException, InvalidRangeException {
        String rangeToReturn = EMPTY_STRING;

        int toIndex = s.indexOf(RANGE_SEPARATOR);
        String start = s.substring(START_INDEX, toIndex);
        String end = s.substring(toIndex + RANGE_SEPARATOR.length());

        start = getIndexForSingleNumber(start);
        end = getIndexForSingleNumber(end);

        int startInt = Integer.parseInt(start);
        int endInt = Integer.parseInt(end);

        if (startInt > endInt) {
            throw new InvalidRangeException();
        }

        for (int i = startInt; i <= endInt; i++) {
            rangeToReturn += String.valueOf(i) + STRING_WHITESPACE;
        }

        return rangeToReturn.trim();
    }

}
