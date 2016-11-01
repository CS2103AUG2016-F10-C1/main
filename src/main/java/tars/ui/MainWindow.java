package tars.ui;

import javafx.scene.input.KeyEvent;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tars.commons.core.Config;
import tars.commons.core.GuiSettings;
import tars.commons.core.KeyCombinations;
import tars.commons.core.LogsCenter;
import tars.commons.events.ui.CommandBoxTextFieldValueChangedEvent;
import tars.commons.events.ui.ExitAppRequestEvent;
import tars.commons.events.ui.KeyCombinationPressedEvent;
import tars.logic.Logic;
import tars.logic.commands.ConfirmCommand;
import tars.logic.commands.RsvCommand;
import tars.model.UserPrefs;

/**
 * The Main Window. Provides the basic application layout containing a menu bar
 * and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart {

    private static final String ICON = "/images/tars_icon_32.png";
    private final Logger logger = LogsCenter.getLogger(MainWindow.class);
    private static final String FXML = "MainWindow.fxml";
    public static final int MIN_HEIGHT = 600;
    public static final int MIN_WIDTH = 450;

    private double xOffset = 0;
    private double yOffset = 0;

    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private Header header;
    private TaskListPanel taskListPanel;
    private RsvTaskListPanel rsvTaskListPanel;
    private ResultDisplay resultDisplay;
    private StatusBarFooter statusBarFooter;
    private HelpPanel helpPanel;
    private ThisWeekPanel thisWeekPanel;
    private CommandBox commandBox;
    private Config config;
    private UserPrefs userPrefs;

    // Handles to elements of this Ui container
    private VBox rootLayout;
    private Scene scene;

    private String tarsName;

    public static final int OVERVIEW_PANEL_TAB_PANE_INDEX = 0;
    public static final int RSV_TASK_LIST_PANEL_TAB_PANE_INDEX = 1;
    public static final int HELP_PANEL_TAB_PANE_INDEX = 2;

    @FXML
    private AnchorPane commandBoxPlaceholder;
    @FXML
    private AnchorPane headerPlaceholder;
    @FXML
    private AnchorPane taskListPanelPlaceholder;
    @FXML
    private AnchorPane rsvTaskListPanelPlaceholder;
    @FXML
    private AnchorPane resultDisplayPlaceholder;
    @FXML
    private AnchorPane statusbarPlaceholder;
    @FXML
    private AnchorPane thisWeekPanelPlaceholder;
    @FXML
    private AnchorPane helpPanelPlaceholder;

    @FXML
    private Label taskListLabel;

    @FXML
    private TabPane tabPane;
    @FXML
    private AnchorPane thisWeekTabAnchorPane;
    @FXML
    private AnchorPane rsvTabAnchorPane;
    @FXML
    private AnchorPane helpTabAnchorPane;

    @Override
    public void setNode(Node node) {
        rootLayout = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    public static MainWindow load(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {

        MainWindow mainWindow = UiPartLoader.loadUiPart(primaryStage, new MainWindow());
        mainWindow.configure(config.getAppTitle(), config.getTarsName(), config, prefs, logic);
        return mainWindow;
    }

    private void configure(String appTitle, String tarsName, Config config, UserPrefs prefs, Logic logic) {

        // Set dependencies
        this.logic = logic;
        this.tarsName = tarsName;
        this.config = config;
        this.userPrefs = prefs;

        // Configure the UI
        setTitle(appTitle);
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);

        addMouseEventHandler();
        addTabPaneHandler();

        registerAsAnEventHandler(this);

        scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

    }

    private void addMouseEventHandler() {
        rootLayout.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        rootLayout.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    private void addTabPaneHandler() {
        rootLayout.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.RIGHT) {
                    cycleTabPaneRight();
                    event.consume();
                } else if (event.getCode() == KeyCode.LEFT) {
                    cycleTabPaneLeft();
                    event.consume();
                }
            }
        });
    }

    private void cycleTabPaneRight() {
        if (tabPane.getSelectionModel().isSelected(HELP_PANEL_TAB_PANE_INDEX)) {
            tabPane.getSelectionModel().selectFirst();
        } else {
            tabPane.getSelectionModel().selectNext();
        }
    }

    private void cycleTabPaneLeft() {
        if (tabPane.getSelectionModel().isSelected(OVERVIEW_PANEL_TAB_PANE_INDEX)) {
            tabPane.getSelectionModel().selectLast();
        } else {
            tabPane.getSelectionModel().selectPrevious();
        }
    }

    void fillInnerParts() {
        header = Header.load(primaryStage, headerPlaceholder);
        taskListPanel = TaskListPanel.load(primaryStage, getTaskListPlaceholder(), logic.getFilteredTaskList());
        rsvTaskListPanel = RsvTaskListPanel.load(primaryStage, getRsvTaskListPlaceholder(),
                logic.getFilteredRsvTaskList());
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(), config.getTarsFilePath());
        commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
        helpPanel = HelpPanel.load(primaryStage, getHelpPanelPlaceholder());
        thisWeekPanel = ThisWeekPanel.load(primaryStage, getThisWeekPanelPlaceholder(), logic.getFilteredTaskList());
    }

    /**
     * @@author A0124333U 
     * A method to reload the status bar footer
     */
    public void reloadStatusBarFooter(String newTarsFilePath) {
        statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(), newTarsFilePath);
    }
    
    //@@author

    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getStatusbarPlaceholder() {
        return statusbarPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }

    public AnchorPane getTaskListPlaceholder() {
        return taskListPanelPlaceholder;
    }

    public AnchorPane getRsvTaskListPlaceholder() {
        return rsvTaskListPanelPlaceholder;
    }

    public AnchorPane getHelpPanelPlaceholder() {
        return helpPanelPlaceholder;
    }

    public AnchorPane getThisWeekPanelPlaceholder() {
        return thisWeekPanelPlaceholder;
    }

    public void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    protected void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    public GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(), (int) primaryStage.getX(),
                (int) primaryStage.getY());
    }

    @FXML
    public void handleHelp() {
        tabPane.getSelectionModel().select(HELP_PANEL_TAB_PANE_INDEX);
    }

    public void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public TaskListPanel getTaskListPanel() {
        return this.taskListPanel;
    }

    public RsvTaskListPanel getRsvTaskListPanel() {
        return this.rsvTaskListPanel;
    }

    // ==================== Event Handling Code =================================================================
    
    @Subscribe
    private void KeyCombinationPressedEventHandler(KeyCombinationPressedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, event.getKeyCombination().getDisplayText()));
        if (event.getKeyCombination() == KeyCombinations.KEY_COMB_CTRL_RIGHT_ARROW) {
            cycleTabPaneRight();
        } else if (event.getKeyCombination() == KeyCombinations.KEY_COMB_CTRL_LEFT_ARROW) {
            cycleTabPaneLeft();
        }
    }
    
    @Subscribe
    private void CommandBoxTextFieldValueChangedEventHandler(CommandBoxTextFieldValueChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, event.getTextFieldValue() + " command detected."));
        if (event.getTextFieldValue().equals(RsvCommand.COMMAND_WORD) || event.getTextFieldValue().equals(ConfirmCommand.COMMAND_WORD)) {
            tabPane.getSelectionModel().select(RSV_TASK_LIST_PANEL_TAB_PANE_INDEX);
        }
    }
    

}
