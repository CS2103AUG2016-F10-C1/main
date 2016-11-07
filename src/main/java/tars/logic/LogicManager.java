package tars.logic;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import tars.commons.core.ComponentManager;
import tars.commons.core.LogsCenter;
import tars.logic.commands.Command;
import tars.logic.commands.CommandResult;
import tars.logic.commands.RedoCommand;
import tars.logic.commands.UndoCommand;
import tars.logic.parser.Parser;
import tars.model.Model;
import tars.model.task.ReadOnlyTask;
import tars.model.task.rsv.RsvTask;
import tars.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {

    private static final String LOG_USER_COMMAND =
            "----------------[USER COMMAND][%1$s]";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);
    private final Model model;
    private final Parser parser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new Parser();
    }

    @Override
    public CommandResult execute(String commandText) {
        logger.info(String.format(LOG_USER_COMMAND, commandText));
        Command command = parser.parseCommand(commandText);
        command.setData(model);

        if (!isReUndoAbleCommand(command)) {
            model.getRedoableCmdHist().clear();
        }

        return command.execute();
    }

    /**
     * Checks if the command is an instance of redo or undo command
     * 
     * @param command
     */
    private boolean isReUndoAbleCommand(Command command) {
        return (command instanceof UndoCommand
                || command instanceof RedoCommand);
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
        return model.getTars().getTaskList();
    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }

    @Override
    public ObservableList<RsvTask> getFilteredRsvTaskList() {
        return model.getFilteredRsvTaskList();
    }

}
