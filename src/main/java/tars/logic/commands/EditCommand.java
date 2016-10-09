package tars.logic.commands;

import tars.commons.core.Messages;
import tars.commons.core.UnmodifiableObservableList;
import tars.commons.exceptions.IllegalValueException;
import tars.model.tag.UniqueTagList.TagNotFoundException;
import tars.model.task.*;
import tars.model.task.UniqueTaskList.TaskNotFoundException;
import java.time.DateTimeException;


/**
 * Edits a task identified using it's last displayed index from tars.
 */
public class EditCommand extends Command {

	public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": edit a task in tars. "
            + "Parameters: INDEX (must be a positive integer) -n NAME -dt DATETIME -p PRIORITY "
            + "-ta TAGTOADD -tr TAGTOREMOVE\n"
            + "Example: " + COMMAND_WORD
            + " 1 -n Lunch with John -dt 10/09/2016 1200 to 10/09/2016 1300 -p l -ta lunch -tr dinner";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited task: %1$s";
        
    public final int targetIndex;

    private String[] argsToEdit;
    
    /**
     * Convenience constructor using raw values.
     */
    public EditCommand(int targetIndex, String [] argsToEdit) {
        this.targetIndex = targetIndex;
        this.argsToEdit = argsToEdit;
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();
        
        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        
        ReadOnlyTask toEdit = lastShownList.get(targetIndex - 1);

        try {
            Task editedTask = model.editTask(toEdit, targetIndex, this.argsToEdit);
            toEdit = editedTask;
        } catch (TaskNotFoundException tnfe) {
            return new CommandResult("The target task cannot be missing");
        } catch (DateTimeException | IllegalValueException | TagNotFoundException e) {
            return new CommandResult(e.getMessage());
        }
        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, toEdit));
    }

}
