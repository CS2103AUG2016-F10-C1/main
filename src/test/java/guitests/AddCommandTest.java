package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;

import tars.commons.core.Messages;
import tars.commons.exceptions.IllegalValueException;
import tars.model.task.Name;
import tars.model.task.Priority;
import tars.testutil.TestTask;
import tars.testutil.TestUtil;

import static org.junit.Assert.assertTrue;

public class AddCommandTest extends TarsGuiTest {

    @Test
    public void add() {
        // add one task
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = td.taskH;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        // add another task
        taskToAdd = td.taskI;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        // add duplicate task
        commandBox.runCommand(td.taskH.getAddCommand());
        assertResultMessage(Messages.MESSAGE_DUPLICATE_TASK);
        assertTrue(taskListPanel.isListMatching(currentList));

        // add to empty list
        commandBox.runCommand("clear");
        assertAddSuccess(td.taskA);

        // invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertAddSuccess(TestTask taskToAdd, TestTask... currentList) {
        commandBox.runCommand(taskToAdd.getAddCommand());

        // confirm the new card contains the right data
        TaskCardHandle addedCard = taskListPanel.navigateToTask(taskToAdd.getName().taskName);
        assertMatching(taskToAdd, addedCard);

        // confirm the list now contains all previous tasks plus the new task
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }

    //@@author A0140022H
    @Test
    public void addRecurring() {
        commandBox.runCommand("clear");
        TestTask[] recurringList = new TestTask[0];
        recurringList = TestUtil.addTasksToList(recurringList, td.taskC, td.taskD);
        try {
            recurringList[1].setName(new Name("Task C"));
            recurringList[1].setPriority(new Priority("l"));
        } catch (IllegalValueException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        commandBox.runCommand("add Task C -dt 03/09/2016 1400 to 04/09/2016 1400 -p l -r 2 every day");
        assertTrue(taskListPanel.isListMatching(recurringList));
    }
}
