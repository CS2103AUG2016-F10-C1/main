package tars.model.tag;

import tars.commons.exceptions.IllegalValueException;
import tars.commons.util.StringUtil;

/**
 * Represents a Tag in tars. Guarantees: immutable; name is valid as declared in
 * {@link #isValidTagName(String)}
 */
public class Tag implements ReadOnlyTag {

    public static final String MESSAGE_TAG_CONSTRAINTS =
            "Tags names should be alphanumeric";
    public static final String TAG_VALIDATION_REGEX = "\\p{Alnum}+";

    public String tagName;

    public Tag() {}

    /**
     * Validates given tag name.
     *
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public Tag(String name) throws IllegalValueException {
        assert name != null;
        name = name.trim();
        if (!isValidTagName(name)) {
            throw new IllegalValueException(MESSAGE_TAG_CONSTRAINTS);
        }
        this.tagName = name;
    }

    /**
     * Copy constructor.
     * 
     * @throws IllegalValueException
     */
    public Tag(ReadOnlyTag source) throws IllegalValueException {
        this(source.getAsText());
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(TAG_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                        && this.tagName.equals(((Tag) other).tagName)); // state check
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return String.format(StringUtil.STRING_SQUARE_BRACKET_OPEN + "%s"
                + StringUtil.STRING_SQUARE_BRACKET_CLOSE, tagName);
    }

    @Override
    public String getAsText() {
        return tagName;
    }

}
