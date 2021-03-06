package tars.model.task;

import tars.commons.util.StringUtil;
import tars.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Task in TARS. Implementations should guarantee: details are
 * present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    public static final int BUFFER_LENGTH_ZERO = 0;
    public static final String STRING_TAGS = " Tags: ";
    public static final String STRING_STATUS = " Status: ";
    public static final String STRING_PRIORITY = " Priority: ";
    public static final String STRING_DATE_TIME = " DateTime: ";
    public static final String PRIORITY_HIGH = "high";
    public static final String PRIORITY_MEDIUM = "medium";
    public static final String PRIORITY_LOW = "low";

    public static final String PRIORITY_H = "h";
    public static final String PRIORITY_M = "m";
    public static final String PRIORITY_L = "l";

    Name getName();

    DateTime getDateTime();

    Priority getPriority();

    Status getStatus();


    /**
     * The returned TagList is a deep copy of the internal TagList, changes on the returned list
     * will not affect the task's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                        && other.getName().equals(this.getName()) // state checks here onwards
                        && other.getDateTime().equals(this.getDateTime())
                        && other.getPriority().equals(this.getPriority())
                        && other.getTags().equals(this.getTags())
                        && other.getStatus().equals(this.getStatus()));
    }

    /**
     * Formats the task as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName()).append(STRING_DATE_TIME).append(getDateTime())
                .append(STRING_PRIORITY).append(priorityString())
                .append(STRING_STATUS).append(getStatus().toString())
                .append(STRING_TAGS);
        getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Returns a string representation of this Task's priority
     */
    default String priorityString() {
        String level = StringUtil.EMPTY_STRING;
        switch (getPriority().toString()) {
            case PRIORITY_H:
                level = PRIORITY_HIGH;
                break;
            case PRIORITY_M:
                level = PRIORITY_MEDIUM;
                break;
            case PRIORITY_L:
                level = PRIORITY_LOW;
                break;
            default:
                level = StringUtil.EMPTY_STRING;
                break;
        }
        return level;
    }

    /**
     * Returns a string representation of this Task's tags
     */
    default String tagsString() {
        final StringBuffer buffer = new StringBuffer();
        final String separator = StringUtil.STRING_COMMA;
        getTags().forEach(tag -> buffer.append(tag).append(separator));
        if (buffer.length() == BUFFER_LENGTH_ZERO) {
            return StringUtil.EMPTY_STRING;
        } else {
            return buffer.substring(0, buffer.length() - separator.length());
        }
    }

}
