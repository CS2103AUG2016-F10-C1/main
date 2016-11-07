package tars.logic.commands;

import static tars.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.HashSet;
import java.util.Set;

import tars.commons.core.Messages;
import tars.commons.core.UnmodifiableObservableList;
import tars.commons.exceptions.DuplicateTaskException;
import tars.commons.exceptions.IllegalValueException;
import tars.commons.util.StringUtil;
import tars.model.tag.Tag;
import tars.model.tag.UniqueTagList;
import tars.model.task.Priority;
import tars.model.task.Status;
import tars.model.task.Task;
import tars.model.task.UniqueTaskList.TaskNotFoundException;
import tars.model.task.rsv.RsvTask;
import tars.model.task.rsv.UniqueRsvTaskList.RsvTaskNotFoundException;

// @@author A0124333U
/**
 * Confirms a specified datetime for a reserved task and add it into the task list
 */
public class ConfirmCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "confirm";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD + ": Confirms a datetime for a reserved task"
                    + " and adds it to the task list.\n"
                    + "Parameters: <RSV_TASK_INDEX> <DATETIME_INDEX> [/p PRIORITY] [/t TAG_NAME ...]\n"
                    + "Example: " + COMMAND_WORD + " 1 3 /p h /t tag";

    public static final String MESSAGE_CONFIRM_SUCCESS =
            "Task Confirmation Success! New task added: %1$s";

    private final int taskIndex;
    private final int dateTimeIndex;
    private final String priority;
    private final Set<Tag> tagSet = new HashSet<>();

    private String conflictingTaskList = StringUtil.EMPTY_STRING;
    private Task toConfirm;
    private RsvTask rsvTask;

    public ConfirmCommand(int taskIndex, int dateTimeIndex, String priority,
            Set<String> tags) throws IllegalValueException {
        this.taskIndex = taskIndex;
        this.dateTimeIndex = dateTimeIndex;
        this.priority = priority;

        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        UnmodifiableObservableList<RsvTask> lastShownList =
                model.getFilteredRsvTaskList();

        if (lastShownList.size() < taskIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(
                    Messages.MESSAGE_INVALID_RSV_TASK_DISPLAYED_INDEX);
        }

        rsvTask = lastShownList
                .get(taskIndex - StringUtil.DISPLAYED_INDEX_OFFSET);

        if (rsvTask.getDateTimeList().size() < dateTimeIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(
                    Messages.MESSAGE_INVALID_DATETIME_DISPLAYED_INDEX);
        }

        try {
            toConfirm = new Task(rsvTask.getName(), rsvTask.getDateTimeList()
                    .get((dateTimeIndex - StringUtil.DISPLAYED_INDEX_OFFSET)),
                    new Priority(priority), new Status(),
                    new UniqueTagList(tagSet));
        } catch (IllegalValueException ive) {
            return new CommandResult(String
                    .format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }

        try {
            model.deleteRsvTask(rsvTask);
        } catch (RsvTaskNotFoundException rtnfe) {
            return new CommandResult(Messages.MESSAGE_RSV_TASK_CANNOT_BE_FOUND);
        }

        try {
            conflictingTaskList +=
                    model.getTaskConflictingDateTimeWarningMessage(
                            toConfirm.getDateTime());
            model.addTask(toConfirm);
        } catch (DuplicateTaskException e) {
            return new CommandResult(Messages.MESSAGE_DUPLICATE_TASK);
        }

        model.getUndoableCmdHist().push(this);
        return new CommandResult(getSuccessMessageSummary());
    }

    private String getSuccessMessageSummary() {
        String summary =
                String.format(MESSAGE_CONFIRM_SUCCESS, toConfirm.toString());

        if (!conflictingTaskList.isEmpty()) {
            summary += StringUtil.STRING_NEWLINE
                    + Messages.MESSAGE_CONFLICTING_TASKS_WARNING
                    + conflictingTaskList;
        }

        return summary;
    }

    // @@author

    // @@author A0139924W
    @Override
    public CommandResult undo() {
        try {
            model.addRsvTask(rsvTask);
            model.deleteTask(toConfirm);
        } catch (DuplicateTaskException e) {
            return new CommandResult(
                    String.format(UndoCommand.MESSAGE_UNSUCCESS,
                            Messages.MESSAGE_DUPLICATE_TASK));
        } catch (TaskNotFoundException e) {
            return new CommandResult(
                    String.format(UndoCommand.MESSAGE_UNSUCCESS,
                            Messages.MESSAGE_TASK_CANNOT_BE_FOUND));
        }

        return new CommandResult(String.format(UndoCommand.MESSAGE_SUCCESS,
                StringUtil.EMPTY_STRING));
    }

    // @@author A0139924W
    @Override
    public CommandResult redo() {
        try {
            model.deleteRsvTask(rsvTask);
            model.addTask(toConfirm);
        } catch (DuplicateTaskException e) {
            return new CommandResult(
                    String.format(RedoCommand.MESSAGE_UNSUCCESS,
                            Messages.MESSAGE_DUPLICATE_TASK));
        } catch (RsvTaskNotFoundException e) {
            return new CommandResult(
                    String.format(RedoCommand.MESSAGE_UNSUCCESS,
                            Messages.MESSAGE_RSV_TASK_CANNOT_BE_FOUND));
        }

        return new CommandResult(String.format(RedoCommand.MESSAGE_SUCCESS,
                StringUtil.EMPTY_STRING));
    }

}
