package tars.model.task;

/**
 * Represents a Task's status in tars.
 */
public class Status {
    private static final String MESSAGE_STATUS_DONE = "Done";
    private static final String MESSAGE_STATUS_UNDONE = "Undone";

    public boolean status;

    /** 
     * Default constructor
     */
    public Status() {
        status = false;
    }
    
    /**
     * For storage
     */
    public Status(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        if (status) {
            return MESSAGE_STATUS_DONE;
        } else {
            return MESSAGE_STATUS_UNDONE;
        }
    }
    
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Status // instanceof handles nulls
                && this.toString().equals(((Status) other).toString())); // state check
    }

    public void setDone() {
        this.status = true;
    }
    
    public void setUndone() {
        this.status = false;
    }

}
