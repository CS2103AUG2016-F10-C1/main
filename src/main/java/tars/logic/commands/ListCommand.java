package tars.logic.commands;

import java.util.Set;

import tars.commons.core.EventsCenter;
import tars.commons.events.ui.ScrollToTopEvent;

import static tars.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

// @@author A0140022H
/**
 * Lists all tasks in tars to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "ls";

    public static final String MESSAGE_SUCCESS = "Listed all tasks";
    public static final String MESSAGE_SUCCESS_DATETIME =
            "Listed all tasks by earliest datetime first";
    public static final String MESSAGE_SUCCESS_DATETIME_DESCENDING =
            "Listed all tasks by latest datetime first";
    public static final String MESSAGE_SUCCESS_PRIORITY =
            "Listed all tasks by priority from low to high";
    public static final String MESSAGE_SUCCESS_PRIORITY_DESCENDING =
            "Listed all tasks by priority from high to low";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD + ": Lists all tasks.\n" + "Parameters: [KEYWORD] "
                    + "Example: " + COMMAND_WORD + " /dt";

    private static final String LIST_ARG_DATETIME = "/dt";
    private static final String LIST_ARG_PRIORITY = "/p";
    private static final String LIST_KEYWORD_DESCENDING = "dsc";

    private Set<String> keywords;

    public ListCommand() {}

    public ListCommand(Set<String> arguments) {
        this.keywords = arguments;
    }

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ScrollToTopEvent());
        if (keywords != null && !keywords.isEmpty()) {
            return listByKeyword();
        } else {
            model.updateFilteredListToShowAll();
            return new CommandResult(MESSAGE_SUCCESS);
        }
    }

    private CommandResult listByKeyword() {
        if (keywords.contains(LIST_ARG_DATETIME)
                || keywords.contains(LIST_ARG_PRIORITY)
                || keywords.contains(LIST_KEYWORD_DESCENDING)) {

            model.sortFilteredTaskList(keywords);

            if (keywords.contains(LIST_KEYWORD_DESCENDING)) {
                if (keywords.contains(LIST_ARG_DATETIME))
                    return new CommandResult(
                            MESSAGE_SUCCESS_DATETIME_DESCENDING);
                else
                    return new CommandResult(
                            MESSAGE_SUCCESS_PRIORITY_DESCENDING);
            } else {
                if (keywords.contains(LIST_ARG_DATETIME))
                    return new CommandResult(MESSAGE_SUCCESS_DATETIME);
                else
                    return new CommandResult(MESSAGE_SUCCESS_PRIORITY);
            }
        } else {
            model.updateFilteredListToShowAll();
            return new CommandResult(String
                    .format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }
    }
}
