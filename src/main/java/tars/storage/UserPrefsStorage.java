package tars.storage;

import tars.commons.exceptions.DataConversionException;
import tars.model.UserPrefs;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents a storage for {@link tars.model.UserPrefs}.
 */
public interface UserPrefsStorage {

    /**
     * Returns UserPrefs data from storage. Returns {@code Optional.empty()} if storage file is not
     * found.
     * 
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    /**
     * Saves the given {@link tars.model.UserPrefs} to the storage.
     * 
     * @param userPrefs cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

}
