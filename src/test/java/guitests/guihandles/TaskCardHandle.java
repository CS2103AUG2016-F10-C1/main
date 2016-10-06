package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.stage.Stage;
import tars.model.task.ReadOnlyTask;

/**
 * Provides a handle to a task card in the task list panel.
 */
public class TaskCardHandle extends GuiHandle {
    private static final String NAME_FIELD_ID = "#name";
    private static final String DATETIME_FIELD_ID = "#datetime";
    private static final String PRIORITY_FIELD_ID = "#priority";
    private static final String STATUS_FIELD_ID = "#status";

    private Node node;

    public TaskCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    public String gettaskName() {
        return getTextFromLabel(NAME_FIELD_ID);
    }

    public String getDateTime() {
        return getTextFromLabel(DATETIME_FIELD_ID);
    }

    public String getPriority() {
        return getTextFromLabel(PRIORITY_FIELD_ID);
    }

    public String getStatus() {
        return getTextFromLabel(STATUS_FIELD_ID);
    }

    public boolean isSameTask(ReadOnlyTask task){
        return gettaskName().equals(task.getName().taskName) && getPriority().equals(task.getPriority().toString())
                && getDateTime().equals(task.getDateTime().toString()) && getStatus().equals(task.getStatus().toString());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TaskCardHandle) {
            TaskCardHandle handle = (TaskCardHandle) obj;
            return gettaskName().equals(handle.gettaskName())
                    && getDateTime().equals(handle.getDateTime()); //TODO: compare the rest
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return gettaskName() + " " + getDateTime();
    }
}
