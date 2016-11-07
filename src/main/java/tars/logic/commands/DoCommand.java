package tars.logic.commands;

import tars.commons.exceptions.DuplicateTaskException;
import tars.commons.exceptions.InvalidRangeException;
import tars.commons.exceptions.InvalidTaskDisplayedException;
import tars.commons.util.StringUtil;
import tars.model.task.*;

import java.util.ArrayList;

// @@author A0121533W
/**
 * Marks a task identified using it's last displayed index from tars as done.
 */
public class DoCommand extends Command {

    public static final String COMMAND_WORD = "do";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task based on its index in the task list as done.\n"
            + "Parameters: <INDEX> [INDEX ...]\n" + "Example: "
            + COMMAND_WORD + " 3 5 7\n" + "OR " + COMMAND_WORD + " 1..3";

    private String toDo;

    private MarkTaskUtil tracker;

    /**
     * Convenience constructor using raw values.
     * 
     * @throws InvalidRangeException
     */
    public DoCommand(String toDo) {
        this.toDo = toDo;
        this.tracker = new MarkTaskUtil();
    }

    @Override
    public CommandResult execute() {
        assert model != null;

        try {
            handleMarkDone();
        } catch (InvalidTaskDisplayedException e) {
            return new CommandResult(e.getMessage());
        } catch (DuplicateTaskException dte) {
            return new CommandResult(dte.getMessage());
        }
        return new CommandResult(tracker.getResultFromTracker());
    }

    /**
     * Marks status of task in model as done
     */
    private void handleMarkDone()
            throws InvalidTaskDisplayedException, DuplicateTaskException {
        Status done = new Status(true);
        ArrayList<ReadOnlyTask> markDoneTasks = tracker.getTasksFromIndexes(
                model, this.toDo.split(StringUtil.STRING_WHITESPACE), done);
        model.mark(markDoneTasks, done);
    }

}
