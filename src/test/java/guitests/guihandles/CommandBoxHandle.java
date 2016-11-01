package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Stage;
import tars.commons.core.KeyCombinations;

/**
 * A handle to the Command Box in the GUI.
 */
public class CommandBoxHandle extends GuiHandle {

    private static final String COMMAND_INPUT_FIELD_ID = "#commandTextField";

    public CommandBoxHandle(GuiRobot guiRobot, Stage primaryStage,
            String stageTitle) {
        super(guiRobot, primaryStage, stageTitle);
    }

    public void enterCommand(String command) {
        setTextField(COMMAND_INPUT_FIELD_ID, command);
    }

    public String getCommandInput() {
        return getTextFieldText(COMMAND_INPUT_FIELD_ID);
    }

    /**
     * Enters the given command in the Command Box and presses enter.
     */
    public void runCommand(String command) {
        enterCommand(command);
        pressEnter();
        guiRobot.sleep(200); // Give time for the command to take effect
    }

    public HelpPanelHandle runHelpCommand() {
        enterCommand("help");
        pressEnter();
        return getHelpPanelHandle();
    }

    public HelpPanelHandle getHelpPanelHandle() {
        return new HelpPanelHandle(guiRobot, primaryStage);
    }

    public OverviewPanelHandle getOverviewPanelHandle() {
        return new OverviewPanelHandle(guiRobot, primaryStage);
    }

    public RsvTaskListPanelHandle getRsvTaskListPanelHandle() {
        return new RsvTaskListPanelHandle(guiRobot, primaryStage);
    }

    public void pressUpKey() {
        guiRobot.type(KeyCode.UP).sleep(500);
    }

    public void pressDownKey() {
        guiRobot.type(KeyCode.DOWN).sleep(500);
    }

    public void pressCtrlRightArrowKeys() {
        guiRobot.push(
                (KeyCodeCombination) KeyCombinations.KEY_COMB_CTRL_RIGHT_ARROW);
    }

    public void pressCtrlLeftArrowKeys() {
        guiRobot.push(
                (KeyCodeCombination) KeyCombinations.KEY_COMB_CTRL_LEFT_ARROW);
    }

    public void pressCtrlZArrowKeys() {
        guiRobot.push((KeyCodeCombination) KeyCombinations.KEY_COMB_CTRL_Z);
    }

    public void pressCtrlYArrowKeys() {
        guiRobot.push((KeyCodeCombination) KeyCombinations.KEY_COMB_CTRL_Y);
    }

}
