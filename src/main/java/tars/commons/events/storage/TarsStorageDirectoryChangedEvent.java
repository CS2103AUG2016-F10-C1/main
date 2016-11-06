package tars.commons.events.storage;

import tars.commons.core.Config;
import tars.commons.events.BaseEvent;

// @@author A0124333U
/**
 * An event where the user changes the Tars Storage Directory/File Path
 */
public class TarsStorageDirectoryChangedEvent extends BaseEvent {
    private static String MESSAGE_FILE_PATH_CHANGED = "File Path changed to %s";

    private final String newFilePath;
    private final Config newConfig;

    public TarsStorageDirectoryChangedEvent(String newFilePath,
            Config newConfig) {
        this.newFilePath = newFilePath;
        this.newConfig = newConfig;
    }

    public String getNewFilePath() {
        return this.newFilePath;
    }

    public Config getNewConfig() {
        return this.newConfig;
    }

    @Override
    public String toString() {
        return String.format(MESSAGE_FILE_PATH_CHANGED, this.newFilePath);
    }

}
