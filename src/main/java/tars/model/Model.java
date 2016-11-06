package tars.model;

import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

import javafx.collections.ObservableList;
import tars.commons.core.UnmodifiableObservableList;
import tars.commons.exceptions.DuplicateTaskException;
import tars.commons.exceptions.IllegalValueException;
import tars.logic.commands.Command;
import tars.model.tag.ReadOnlyTag;
import tars.model.tag.Tag;
import tars.model.tag.UniqueTagList.DuplicateTagException;
import tars.model.tag.UniqueTagList.TagNotFoundException;
import tars.model.task.DateTime;
import tars.model.task.ReadOnlyTask;
import tars.model.task.Status;
import tars.model.task.Task;
import tars.model.task.TaskQuery;
import tars.model.task.UniqueTaskList;
import tars.model.task.rsv.RsvTask;
import tars.model.task.rsv.UniqueRsvTaskList.RsvTaskNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTars newData);
    
    /**@@author A0124333U 
     * Overwrites current data with data from a new file path. */
    public void overwriteDataFromNewFilePath(ReadOnlyTars newData);

    /**@@author A0124333U 
     * Returns the Tars */
    ReadOnlyTars getTars();
    
    /** Undo an edited task */
    void replaceTask(ReadOnlyTask toUndo, Task replacement) throws DuplicateTaskException;

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task) throws DuplicateTaskException;
    
    /** 
     * Deletes the reserved task.
     * 
     * @@author A0124333U
     */
    void deleteRsvTask(RsvTask target) throws RsvTaskNotFoundException;
    
    /** Adds the given reserved task */
    void addRsvTask(RsvTask rsvTask) throws DuplicateTaskException;
    
    /** Checks for tasks with conflicting datetime and returns a string of all conflicting tasks */
    String getTaskConflictingDateTimeWarningMessage(DateTime dateTimeToCheck);

    /** Rename all task with the old tag with new tag name */
    void renameTasksWithNewTag(ReadOnlyTag toBeRenamed, Tag newTag)
            throws IllegalValueException, TagNotFoundException, DuplicateTagException;

    /** Remove the tag from all task */
    ArrayList<ReadOnlyTask> removeTagFromAllTasks(ReadOnlyTag toBeDeleted)
            throws DuplicateTagException, IllegalValueException, TagNotFoundException;
    
    /** Add tag to all task */
    void addTagToAllTasks(ReadOnlyTag toBeAdded, ArrayList<ReadOnlyTask> toBeEdited)
            throws DuplicateTagException, IllegalValueException, TagNotFoundException;

    /** Marks tasks as done or undone. */
    void mark(ArrayList<ReadOnlyTask> toMarkList, Status status) throws DuplicateTaskException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();
    
    /** Returns the filtered task list as an {@code UnmodifiableObservableList<RsvTask>} */
    UnmodifiableObservableList<RsvTask> getFilteredRsvTaskList();

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();
   
    /**
     * Updates the filter of the filtered task list to filter by the given keywords of each given
     * task attribute
     * 
     * @@author A0124333U
     */
    void updateFilteredTaskListUsingFlags(TaskQuery taskQuery);
    
    /** Updates the filter of the filtered task list to filter by the given keywords of a string 
     * consisting of all the attributes of each task*/
    void updateFilteredTaskListUsingQuickSearch(ArrayList<String> lazySearchKeywords);

    /** Returns the undoable command history stack */
    Stack<Command> getUndoableCmdHist();
    
    /** Returns the redoable command history stack */
    Stack<Command> getRedoableCmdHist();
    
    /** Returns the unique tag list as an {@code ObservableList<? extends ReadOnlyTag>} */
    ObservableList<? extends ReadOnlyTag> getUniqueTagList();
    
    /**
     * Returns an ArrayList of DateTime in a specified date
     * 
     * @@author A0124333U
     */
    public ArrayList<DateTime> getListOfFilledTimeSlotsInDate(DateTime dateToCheck);

    /**
     * Sorts the filtered task list by the given keywords
     * 
     * @@author A0140022H
     */
	void sortFilteredTaskList(Set<String> keywords);

	//@@author A0140022H
	void updateFilteredTaskListUsingDate(DateTime dateTime);

}
