package tars.testutil;

import com.google.common.io.Files;

import guitests.guihandles.RsvTaskCardHandle;
import guitests.guihandles.TaskCardHandle;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import junit.framework.AssertionFailedError;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxToolkit;

import tars.TestApp;
import tars.commons.exceptions.IllegalValueException;
import tars.commons.util.FileUtil;
import tars.commons.util.XmlUtil;
import tars.model.Tars;
import tars.model.task.*;
import tars.model.task.rsv.RsvTask;
import tars.model.task.rsv.UniqueRsvTaskList;
import tars.model.tag.Tag;
import tars.model.tag.UniqueTagList;
import tars.storage.XmlSerializableTars;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    public static String LS = System.lineSeparator();
    
    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    public static String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

    public static final Task[] sampleTaskData = getSampleTaskData();
    
    public static final Tag[] sampleTagData = getSampleTagData();

    public static void assertThrows(Class<? extends Throwable> expected, Runnable executable) {
        try {
            executable.run();
        } catch (Throwable actualException) {
            if (!actualException.getClass().isAssignableFrom(expected)) {
                String message = String.format("Expected thrown: %s, actual: %s", expected.getName(),
                        actualException.getClass().getName());
                throw new AssertionFailedError(message);
            } else
                return;
        }
        throw new AssertionFailedError(
                String.format("Expected %s to be thrown, but nothing was thrown.", expected.getName()));
    }

    private static Task[] getSampleTaskData() {
        try {
            return new Task[] {
                    new Task(new Name("Task 1"), new DateTime("01/09/2016 1400", "02/09/2016 1400"), new Priority("h"),
                            new Status(false), new UniqueTagList()),
                    new Task(new Name("Task 2"), new DateTime("02/09/2016 1400", "03/09/2016 1400"), new Priority("m"),
                            new Status(false), new UniqueTagList()),
                    new Task(new Name("Task 3"), new DateTime("03/09/2016 1400", "04/09/2016 1400"), new Priority("l"),
                            new Status(false), new UniqueTagList()),
                    new Task(new Name("Task 4"), new DateTime("04/09/2016 1400", "05/09/2016 1400"), new Priority("h"),
                            new Status(false), new UniqueTagList()),
                    new Task(new Name("Task 5"), new DateTime("05/09/2016 1400", "06/09/2016 1400"), new Priority("m"),
                            new Status(false), new UniqueTagList()),
                    new Task(new Name("Task 6"), new DateTime("06/09/2016 1400", "07/09/2016 1400"), new Priority("l"),
                            new Status(false), new UniqueTagList()),
                    new Task(new Name("Task 7"), new DateTime("07/09/2016 1400", "08/09/2016 1400"), new Priority("h"),
                            new Status(false), new UniqueTagList()),
                    new Task(new Name("Task 8"), new DateTime("08/09/2016 1400", "09/09/2016 1400"), new Priority("m"),
                            new Status(false), new UniqueTagList()),
                    new Task(new Name("Task 9"), new DateTime("09/09/2016 1400", "10/09/2016 1400"), new Priority("l"),
                            new Status(false), new UniqueTagList()) };
        } catch (IllegalValueException e) {
            assert false;
            // not possible
            return null;
        }
    }

    private static Tag[] getSampleTagData() {
        try {
            return new Tag[] { new Tag("relatives"), new Tag("friends") };
        } catch (IllegalValueException e) {
            assert false;
            return null;
            // not possible
        }
    }

    public static List<Task> generateSampleTaskData() {
        return Arrays.asList(sampleTaskData);
    }

    /**
     * Appends the file name to the sandbox folder path. Creates the sandbox
     * folder if it doesn't exist.
     * 
     * @param fileName
     * @return
     */
    public static String getFilePathInSandboxFolder(String fileName) {
        try {
            FileUtil.createDirs(new File(SANDBOX_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER + fileName;
    }

    public static void createDataFileWithSampleData(String filePath) {
        createDataFileWithData(generateSampleStorageTars(), filePath);
    }

    public static <T> void createDataFileWithData(T data, String filePath) {
        try {
            File saveFileForTesting = new File(filePath);
            FileUtil.createIfMissing(saveFileForTesting);
            XmlUtil.saveDataToFile(saveFileForTesting, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... s) {
        createDataFileWithSampleData(TestApp.SAVE_LOCATION_FOR_TESTING);
    }

    public static Tars generateEmptyTars() {
        return new Tars(new UniqueTaskList(), new UniqueTagList(), new UniqueRsvTaskList());
    }

    public static XmlSerializableTars generateSampleStorageTars() {
        return new XmlSerializableTars(generateEmptyTars());
    }

    /**
     * Tweaks the {@code keyCodeCombination} to resolve the
     * {@code KeyCode.SHORTCUT} to their respective platform-specific keycodes
     */
    public static KeyCode[] scrub(KeyCodeCombination keyCodeCombination) {
        List<KeyCode> keys = new ArrayList<>();
        if (keyCodeCombination.getAlt() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.ALT);
        }
        if (keyCodeCombination.getShift() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.SHIFT);
        }
        if (keyCodeCombination.getMeta() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.META);
        }
        if (keyCodeCombination.getControl() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.CONTROL);
        }
        keys.add(keyCodeCombination.getCode());
        return keys.toArray(new KeyCode[] {});
    }

    public static boolean isHeadlessEnvironment() {
        String headlessProperty = System.getProperty("testfx.headless");
        return headlessProperty != null && headlessProperty.equals("true");
    }

    public static void captureScreenShot(String fileName) {
        File file = GuiTest.captureScreenshot();
        try {
            Files.copy(file, new File(fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String descOnFail(Object... comparedObjects) {
        return "Comparison failed \n"
                + Arrays.asList(comparedObjects).stream().map(Object::toString).collect(Collectors.joining("\n"));
    }

    public static void setFinalStatic(Field field, Object newValue)
            throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);
        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        // ~Modifier.FINAL is used to remove the final modifier from field so
        // that its value is no longer
        // final and can be changed
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    public static void initRuntime() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.hideStage();
    }

    public static void tearDownRuntime() throws Exception {
        FxToolkit.cleanupStages();
    }

    /**
     * Gets private method of a class Invoke the method using
     * method.invoke(objectInstance, params...)
     *
     * Caveat: only find method declared in the current Class, not inherited
     * from supertypes
     */
    public static Method getPrivateMethod(Class objectClass, String methodName) throws NoSuchMethodException {
        Method method = objectClass.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method;
    }

    public static void renameFile(File file, String newFileName) {
        try {
            Files.copy(file, new File(newFileName));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Gets mid point of a node relative to the screen.
     * 
     * @param node
     * @return
     */
    public static Point2D getScreenMidPoint(Node node) {
        double x = getScreenPos(node).getMinX() + node.getLayoutBounds().getWidth() / 2;
        double y = getScreenPos(node).getMinY() + node.getLayoutBounds().getHeight() / 2;
        return new Point2D(x, y);
    }

    /**
     * Gets mid point of a node relative to its scene.
     * 
     * @param node
     * @return
     */
    public static Point2D getSceneMidPoint(Node node) {
        double x = getScenePos(node).getMinX() + node.getLayoutBounds().getWidth() / 2;
        double y = getScenePos(node).getMinY() + node.getLayoutBounds().getHeight() / 2;
        return new Point2D(x, y);
    }

    /**
     * Gets the bound of the node relative to the parent scene.
     * 
     * @param node
     * @return
     */
    public static Bounds getScenePos(Node node) {
        return node.localToScene(node.getBoundsInLocal());
    }

    public static Bounds getScreenPos(Node node) {
        return node.localToScreen(node.getBoundsInLocal());
    }

    public static double getSceneMaxX(Scene scene) {
        return scene.getX() + scene.getWidth();
    }

    public static double getSceneMaxY(Scene scene) {
        return scene.getX() + scene.getHeight();
    }

    public static Object getLastElement(List<?> list) {
        return list.get(list.size() - 1);
    }

    /**
     * Removes a subset from the list of tasks.
     * 
     * @param tasks The list of tasks
     * @param tasksToRemove The subset of tasks.
     * @return The modified tasks after removal of the subset from tasks.
     */
    public static TestTask[] removeTasksFromList(final TestTask[] tasks, TestTask... tasksToRemove) {
        List<TestTask> listOfTasks = asList(tasks);
        listOfTasks.removeAll(asList(tasksToRemove));
        return listOfTasks.toArray(new TestTask[listOfTasks.size()]);
    }

    /**
     * Returns a copy of the list with the task at specified index removed.
     * 
     * @param list original list to copy from
     * @param targetIndexInOneIndexedFormat e.g. if the first element to be removed, 1 should be
     *        given as index.
     */
    public static TestTask[] removeTaskFromList(final TestTask[] list, int targetIndexInOneIndexedFormat) {
        return removeTasksFromList(list, list[targetIndexInOneIndexedFormat - 1]);
    }

    /**
     * Replaces tasks[i] with a task.
     * 
     * @param tasks The array of tasks.
     * @param task The replacement task
     * @param index The index of the task to be replaced.
     * @return
     */
    public static TestTask[] replaceTaskFromList(TestTask[] tasks, TestTask task, int index) {
        tasks[index] = task;
        return tasks;
    }

    /**
     * Appends tasks to the array of tasks.
     * 
     * @param tasks An array of tasks.
     * @param tasksToAdd The tasks that are to be appended behind the original array.
     * @return The modified array of tasks.
     */
    public static TestTask[] addTasksToList(final TestTask[] tasks, TestTask... tasksToAdd) {
        List<TestTask> listOfTasks = asList(tasks);
        listOfTasks.addAll(asList(tasksToAdd));
        return listOfTasks.toArray(new TestTask[listOfTasks.size()]);
    }
    
    /**
     * Appends Reserved Tasks to the array of rsvTasks
     * @param rsvTasks
     * @param rsvTasksToAdd
     * @return The modified array of rsv tasks
     */
    public static TestRsvTask[] addRsvTasksToList(final TestRsvTask[] rsvTasks, TestRsvTask... rsvTasksToAdd) {
        List<TestRsvTask> listOfRsvTasks = asList(rsvTasks);
        listOfRsvTasks.addAll(asList(rsvTasksToAdd));
        return listOfRsvTasks.toArray(new TestRsvTask[listOfRsvTasks.size()]);
    }
    
    /**
     * Removes a reserved task from the array of rsvTasks
     * @param rsvTasks
     * @param rsvTaskToDel
     * @return The modified array of rsv tasks
     */

    public static TestRsvTask[] delRsvTaskFromList(final TestRsvTask[] rsvTasks, TestRsvTask rsvTaskToDel) {
        List<TestRsvTask> listOfRsvTasks = asList(rsvTasks);
        listOfRsvTasks.remove(rsvTaskToDel);
        return listOfRsvTasks.toArray(new TestRsvTask[listOfRsvTasks.size()]);
    }

    // @@author A0124333U
    /**
     * Edits the task with index 1 on the list of tasks.
     * 
     * @param tasks An array of tasks.
     * @param indexToEdit Index of the task to edit.
     * @param nameToEdit Name of the task to edit.
     * @param priorityToEdit Priority of the task to edit.
     * @return The modified array of tasks.
     */
    public static TestTask[] editTask(final TestTask[] tasks, int indexToEdit, Name nameToEdit,
            Priority priorityToEdit) {
        List<TestTask> listOfTasks = asList(tasks);
        listOfTasks.get(indexToEdit).setName(nameToEdit);
        listOfTasks.get(indexToEdit).setPriority(priorityToEdit);

        return listOfTasks.toArray(new TestTask[listOfTasks.size()]);
    }

    // @@author A0121533W
    /**
     * Marks the task as done with index 1 in the list of tasks.
     * 
     * @param tasks An array of tasks.
     * @param indexes An array of indexes to mark
     * @return The modified array of marked tasks
     */
    public static TestTask[] markTasks(final TestTask[] tasks, int[] indexesToMark, Status status) {
        List<TestTask> listOfTasks = asList(tasks);
        for (int i = 0; i < indexesToMark.length; i++) {
            listOfTasks.get(i).setStatus(status);
        }

        return listOfTasks.toArray(new TestTask[listOfTasks.size()]);
    }
    // @@author

    private static <T> List<T> asList(T[] objs) {
        List<T> list = new ArrayList<>();
        for (T obj : objs) {
            list.add(obj);
        }
        return list;
    }

    public static boolean compareCardAndTask(TaskCardHandle card, ReadOnlyTask task) {
        return card.isSameTask(task);
    }

    public static boolean compareCardAndRsvTask(RsvTaskCardHandle card, RsvTask tasks) {
        return card.isSameRsvTask(tasks);
    }

}
