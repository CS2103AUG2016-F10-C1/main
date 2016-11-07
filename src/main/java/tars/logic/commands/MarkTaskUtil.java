package tars.logic.commands;

import java.util.ArrayList;

import tars.commons.core.Messages;
import tars.commons.core.UnmodifiableObservableList;
import tars.commons.exceptions.InvalidTaskDisplayedException;
import tars.commons.util.StringUtil;
import tars.model.Model;
import tars.model.task.ReadOnlyTask;
import tars.model.task.Status;

// @@author A0121533W
/**
 * Tracks changes made (if any) during do and ud command
 */
public class MarkTaskUtil {

    private ArrayList<Integer> markDoneTasks;
    private ArrayList<Integer> markUndoneTasks;
    private ArrayList<Integer> alreadyDoneTasks;
    private ArrayList<Integer> alreadyUndoneTasks;

    public static final String SUCCESS_DONE =
            "Task: %1$s marked done successfully.\n";
    public static final String SUCCESS_UNDONE =
            "Task: %1$s marked undone successfully.\n";
    public static final String ALREADY_DONE =
            "Task: %1$s already marked done.\n";
    public static final String ALREADY_UNDONE =
            "Task: %1$s already marked undone.\n";

    /**
     * Constructor
     */
    public MarkTaskUtil() {
        this.markDoneTasks = new ArrayList<Integer>();
        this.markUndoneTasks = new ArrayList<Integer>();
        this.alreadyDoneTasks = new ArrayList<Integer>();
        this.alreadyUndoneTasks = new ArrayList<Integer>();
    }

    /**
     * Adds target index of task to relevant "To Mark List" based on status
     */
    public void addToMark(int targetIndex, Status status) {
        if (status.status) {
            addToMarkDoneTask(targetIndex);
        } else {
            addToMarkUndoneTask(targetIndex);
        }

    }

    /**
     * Adds target index of task to relevant "Already Marked List" based on status
     */
    public void addAlreadyMarked(int targetIndex, Status status) {
        if (status.status) {
            addToAlreadyDoneTasks(targetIndex);
        } else {
            addToAlreadyUndoneTasks(targetIndex);
        }
    }

    /**
     * Return string for each tasks index in the specific ArrayLists
     */
    public String getResult() {
        String markDoneTasksString = getIndexesString(markDoneTasks);
        String markUndoneTasksString = getIndexesString(markUndoneTasks);
        String alreadyDoneTasksString = getIndexesString(alreadyDoneTasks);
        String alreadyUndoneTasksString = getIndexesString(alreadyUndoneTasks);

        String result =
                formatResults(markDoneTasksString, markUndoneTasksString,
                        alreadyDoneTasksString, alreadyUndoneTasksString);

        return result;
    }

    private String formatResults(String markDoneTasksString,
            String markUndoneTasksString, String alreadyDoneTasksString,
            String alreadyUndoneTasksString) {

        String markDoneResult =
                getResultFromString(markDoneTasksString, SUCCESS_DONE);
        String markUndoneResult =
                getResultFromString(markUndoneTasksString, SUCCESS_UNDONE);
        String alreadyDoneResult =
                getResultFromString(alreadyDoneTasksString, ALREADY_DONE);
        String aldreadyUndoneResult =
                getResultFromString(alreadyUndoneTasksString, ALREADY_UNDONE);

        return markDoneResult + markUndoneResult + alreadyDoneResult
                + aldreadyUndoneResult;
    }

    /**
     * Formats results of changes made
     */
    private String getResultFromString(String tasksString, String format) {
        String result = StringUtil.EMPTY_STRING;
        if (!tasksString.isEmpty()) {
            result = String.format(format, tasksString);
        }
        return result;
    }

    /**
     * Gets String of indexes separated by comma
     */
    private String getIndexesString(ArrayList<Integer> list) {
        String toReturn = StringUtil.EMPTY_STRING;
        if (!list.isEmpty()) {
            for (int i = StringUtil.START_INDEX; i < list.size() - 1; i++) {
                toReturn +=
                        Integer.toString(list.get(i)) + StringUtil.STRING_COMMA;
            }
            // Add last index
            toReturn += Integer.toString(list.get(list.size() - 1));
        }
        return toReturn;
    }

    private void addToMarkDoneTask(int index) {
        this.markDoneTasks.add(index);
    }

    private void addToMarkUndoneTask(int index) {
        this.markUndoneTasks.add(index);
    }

    private void addToAlreadyDoneTasks(int index) {
        this.alreadyDoneTasks.add(index);
    }

    private void addToAlreadyUndoneTasks(int index) {
        this.alreadyUndoneTasks.add(index);
    }

    /**
     * Returns feedback message of mark command to user
     */
    public String getResultFromTracker() {
        String commandResult = getResult();
        return commandResult;
    }

    /**
     * Gets Tasks to mark from indexes
     */
    public ArrayList<ReadOnlyTask> getTasksFromIndexes(Model model,
            String[] indexes, Status status)
            throws InvalidTaskDisplayedException {
        UnmodifiableObservableList<ReadOnlyTask> lastShownList =
                model.getFilteredTaskList();
        ArrayList<ReadOnlyTask> tasksList = new ArrayList<ReadOnlyTask>();

        for (int i = 0; i < indexes.length; i++) {
            int targetIndex = Integer.valueOf(indexes[i]);
            if (lastShownList.size() < targetIndex) {
                throw new InvalidTaskDisplayedException(
                        Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
            }

            ReadOnlyTask task = lastShownList.get(targetIndex - 1);
            if (!task.getStatus().equals(status)) {
                tasksList.add(task);
                addToMark(targetIndex, status);
            } else {
                addAlreadyMarked(targetIndex, status);
            }
        }
        return tasksList;
    }

}
