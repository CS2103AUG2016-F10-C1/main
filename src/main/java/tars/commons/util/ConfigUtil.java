package tars.commons.util;

import tars.commons.core.Config;
import tars.commons.core.LogsCenter;
import tars.commons.exceptions.DataConversionException;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * A class for accessing the Config File.
 */
public class ConfigUtil {

    private static final Logger logger = LogsCenter.getLogger(ConfigUtil.class);
    private static final String configFilePath = "config.json";
    private static String LOG_MESSAGE_CONFIG_FILE_NOT_FOUND =
            "Config file %s not found";
    private static String LOG_MESSAGE_CONFIG_FILE_READING_ERROR =
            "Error reading from config file %1$s: %2$s";

    /**
     * Returns the Config object from the given file or {@code Optional.empty()} object if the file
     * is not found. If any values are missing from the file, default values will be used, as long
     * as the file is a valid json file.
     * 
     * @param configFilePath cannot be null.
     * @throws DataConversionException if the file format is not as expected.
     */
    public static Optional<Config> readConfig(String configFilePath)
            throws DataConversionException {

        assert configFilePath != null;

        File configFile = new File(configFilePath);

        if (!configFile.exists()) {
            logger.info(String.format(LOG_MESSAGE_CONFIG_FILE_NOT_FOUND,
                    configFile));
            return Optional.empty();
        }

        Config config;

        try {
            config = FileUtil.deserializeObjectFromJsonFile(configFile,
                    Config.class);
        } catch (IOException e) {
            logger.warning(String.format(LOG_MESSAGE_CONFIG_FILE_READING_ERROR,
                    configFile, e));
            throw new DataConversionException(e);
        }

        return Optional.of(config);
    }

    /**
     * Saves the Config object to the specified file. Overwrites existing file if it exists, creates
     * a new file if it doesn't.
     * 
     * @param config cannot be null
     * @param configFilePath cannot be null
     * @throws IOException if there was an error during writing to the file
     */
    public static void saveConfig(Config config, String configFilePath)
            throws IOException {
        assert config != null;
        assert configFilePath != null;

        FileUtil.serializeObjectToJsonFile(new File(configFilePath), config);
    }

    public static void updateConfig(Config newConfig) throws IOException {
        saveConfig(newConfig, configFilePath);
    }

}
