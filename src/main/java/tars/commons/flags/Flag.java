package tars.commons.flags;

/**
 * Container for user input prefixes.
 */
public class Flag {

    public static final String NAME = "-n";
    public static final String PRIORITY = "-p";
    public static final String DATETIME = "-dt";
    public static final String TAG = "-t";
    public static final String ADDTAG = "-ta";
    public static final String REMOVETAG = "-tr";
    public static final String DONE = "-do";
    public static final String UNDONE = "-ud";

    public String prefix;
    public boolean hasMultiple;

    public Flag(String prefix, boolean hasMultiple) {
        this.prefix = prefix;
        this.hasMultiple = hasMultiple;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Flag // instanceof handles nulls
                        && this.prefix.equals(((Flag) other).prefix)); // state check
    }

    @Override
    public int hashCode() {
        return prefix.hashCode();
    }

}
