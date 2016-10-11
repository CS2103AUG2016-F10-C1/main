package tars.model;

import tars.commons.core.UnmodifiableObservableList;
import tars.commons.exceptions.DuplicateTaskException;
import tars.commons.exceptions.IllegalValueException;
import tars.commons.flags.Flag;
import tars.logic.commands.Command;
import tars.model.task.Task;
import tars.model.tag.UniqueTagList.TagNotFoundException;
import tars.model.task.ReadOnlyTask;
import tars.model.task.UniqueTaskList;

import java.time.DateTimeException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTars newData);

    /** Returns the Tars */
    ReadOnlyTars getTars();

    /** Edits the given task and returns the edited task */
    Task editTask(ReadOnlyTask toEdit, HashMap<Flag, String> argsToEdit) throws UniqueTaskList.TaskNotFoundException, 
    DateTimeException, IllegalValueException, TagNotFoundException;

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task) throws DuplicateTaskException;

    /** Marks tasks as done or undone. */
    void mark(ArrayList<ReadOnlyTask> toMarkList, String status) throws DuplicateTaskException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);

    /** Returns the undoable command history stack */
    Stack<Command> getUndoableCmdHist();

}
