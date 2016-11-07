package tars.logic.commands;

// @@author A0139924W
/**
 * Redo an undoable command.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redo successfully.\n%1$s";
    public static final String MESSAGE_UNSUCCESS = "Redo unsuccessfully.\n%1$s";

    public static final String MESSAGE_EMPTY_REDO_CMD_HIST =
            "No more actions that can be redo.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Redo a previous command.\n" + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        assert model != null;

        if (model.getRedoableCmdHist().isEmpty()) {
            return new CommandResult(MESSAGE_EMPTY_REDO_CMD_HIST);
        }

        UndoableCommand command =
                (UndoableCommand) model.getRedoableCmdHist().pop();
        model.getUndoableCmdHist().push(command);
        return command.redo();
    }
}
