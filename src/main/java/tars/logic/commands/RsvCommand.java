package tars.logic.commands;

import java.time.DateTimeException;
import java.util.HashSet;
import java.util.Set;

import tars.commons.core.Messages;
import tars.commons.exceptions.DuplicateTaskException;
import tars.commons.exceptions.IllegalValueException;
import tars.model.task.DateTime;
import tars.model.task.Name;
import tars.model.task.Priority;
import tars.model.task.rsv.RsvTask;

/**
 * Adds a reserved task which has a list of reserved datetimes that can
 * confirmed later on.
 * 
 * @@author A0124333U
 */

public class RsvCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "rsv";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reserves one or more timeslot for a task.\n"
            + "Parameters: TASK [-dt DATETIME] [ADDITIONAL DATETIME]\n" + "Example: " + COMMAND_WORD
            + " Meet John Doe -dt 26/09/2016 0900 to 1030, 28/09/2016 1000 to 1130";
    
    public static final String MESSAGE_DATETIME_NOTFOUND = "At least one DateTime is required!\n" + MESSAGE_USAGE;

    public static final String MESSAGE_SUCCESS = "New task reserved: %1$s";
    public static final String MESSAGE_UNDO = "Removed %1$s";
    public static final String MESSAGE_REDO = "Reserved %1$s";

    private final RsvTask toReserve;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException
     *             if any of the raw values are invalid
     * @throws DateTimeException
     *             if given dateTime string is invalid.
     */

    public RsvCommand(String name, Set<String[]> dateTimeStringSet, String priority, Set<String> tags)
            throws IllegalValueException {

        Set<DateTime> dateTimeSet = new HashSet<>();
        for (String[] dateTimeStringArray : dateTimeStringSet) {
            dateTimeSet.add(new DateTime(dateTimeStringArray[0], dateTimeStringArray[1]));
        }

        this.toReserve = new RsvTask(new Name(name), dateTimeSet, new Priority(priority), new HashSet<String>(tags));

    }

    @Override
    public CommandResult undo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CommandResult redo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addRsvTask(toReserve);
            model.getUndoableCmdHist().push(this);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toReserve.toString()));
        } catch (DuplicateTaskException e) {
            return new CommandResult(Messages.MESSAGE_DUPLICATE_TASK);
        }

    }

}
