package tars.ui;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tars.MainApp;
import tars.commons.core.ComponentManager;
import tars.commons.core.Config;
import tars.commons.core.LogsCenter;
import tars.commons.events.storage.DataSavingExceptionEvent;
import tars.commons.events.ui.RsvTaskAddedEvent;
import tars.commons.events.ui.ScrollToTopEvent;
import tars.commons.events.ui.ShowHelpRequestEvent;
import tars.commons.events.ui.TaskAddedEvent;
import tars.commons.util.StringUtil;
import tars.logic.Logic;
import tars.model.UserPrefs;

import java.util.logging.Logger;

/**
 * The manager of the UI component.
 */
public class UiManager extends ComponentManager implements Ui {
    
    private static final String LOG_MESSAGE_STARTING_UI = "Starting UI...";
    private static final String LOG_MESSAGE_INITIALIZING_FATAL_ERROR =
            "Fatal error during initializing";
    private static final String LOG_MESSAGE_SCROLL_TO_RSVTASK =
            "Scrolling to newly added rsvtask";
    private static final String LOG_MESSAGE_SCROLL_TO_TASK =
            "Scrolling to newly added task";
    private static final String LOG_MESSAGE_SCROLL_TO_TOP = "Scrolling to top";
    private static final String STYLESHEETS_VIEW_TARS_THEME_CSS =
            "view/TarsTheme.css";
    private static final String ALERT_SAVE_DATA_TO_FILE_FAILURE =
            "Could not save data to file";
    private static final String ALERT_SAVE_DATA_FAILURE = "Could not save data";
    private static final String ALERT_FILE_OP_ERROR = "File Op Error";

    private static final Logger logger = LogsCenter.getLogger(UiManager.class);
    private static final String ICON_APPLICATION = "/images/tars_icon_32.png";
    private static final int TOP_OF_LIST = 0;

    private Logic logic;
    private Config config;
    private UserPrefs prefs;
    private MainWindow mainWindow;

    public UiManager(Logic logic, Config config, UserPrefs prefs) {
        super();
        this.logic = logic;
        this.config = config;
        this.prefs = prefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info(LOG_MESSAGE_STARTING_UI);
        primaryStage.setTitle(config.getAppTitle());

        // Set the application icon.
        primaryStage.getIcons().add(getImage(ICON_APPLICATION));

        try {
            mainWindow = MainWindow.load(primaryStage, config, prefs, logic);
            mainWindow.show(); // This should be called before creating other UI parts
            mainWindow.fillInnerParts();

        } catch (Throwable e) {
            logger.severe(StringUtil.getDetails(e));
            showFatalErrorDialogAndShutdown(
                    LOG_MESSAGE_INITIALIZING_FATAL_ERROR, e);
        }
    }

    @Override
    public void stop() {
        prefs.updateLastUsedGuiSetting(mainWindow.getCurrentGuiSetting());
        mainWindow.hide();
    }

    private void showFileOperationAlertAndWait(String description,
            String details, Throwable cause) {
        // final String content = details + ":\n" + cause.toString();
        final String content = details + StringUtil.STRING_COLON
                + StringUtil.STRING_NEWLINE + cause.toString();
        showAlertDialogAndWait(AlertType.ERROR, ALERT_FILE_OP_ERROR,
                description, content);
    }

    private Image getImage(String imagePath) {
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    private void showAlertDialogAndWait(Alert.AlertType type, String title,
            String headerText, String contentText) {
        showAlertDialogAndWait(mainWindow.getPrimaryStage(), type, title,
                headerText, contentText);
    }

    private static void showAlertDialogAndWait(Stage owner, AlertType type,
            String title, String headerText, String contentText) {
        final Alert alert = new Alert(type);
        alert.getDialogPane().getStylesheets()
                .add(STYLESHEETS_VIEW_TARS_THEME_CSS);
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logger.severe(title + StringUtil.STRING_WHITESPACE + e.getMessage()
                + StringUtil.getDetails(e));
        showAlertDialogAndWait(Alert.AlertType.ERROR, title, e.getMessage(),
                e.toString());
        Platform.exit();
        System.exit(1);
    }

    // ==================== Event Handling Code ====================

    @Subscribe
    private void handleDataSavingExceptionEvent(
            DataSavingExceptionEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showFileOperationAlertAndWait(ALERT_SAVE_DATA_FAILURE,
                ALERT_SAVE_DATA_TO_FILE_FAILURE, event.exception);
    }

    // @@author A0140022H
    @Subscribe
    private void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        mainWindow.getEventsHandler();
        MainWindowEventsHandler.handleHelp(mainWindow.getHelpPanel(),
                event.getHelpRequestEventArgs());
    }
    // @@author

    @Subscribe
    private void handleTaskAddedEvent(TaskAddedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event,
                LOG_MESSAGE_SCROLL_TO_TASK));
        mainWindow.getTaskListPanel().scrollTo(event.targetIndex);
    }

    @Subscribe
    private void handleRsvTaskAddedEvent(RsvTaskAddedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event,
                LOG_MESSAGE_SCROLL_TO_RSVTASK));
        mainWindow.getTaskListPanel().scrollTo(event.targetIndex);
    }

    @Subscribe
    private void handleScrollToTopEvent(ScrollToTopEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event,
                LOG_MESSAGE_SCROLL_TO_TOP));
        mainWindow.getTaskListPanel().scrollTo(TOP_OF_LIST);
    }

}
