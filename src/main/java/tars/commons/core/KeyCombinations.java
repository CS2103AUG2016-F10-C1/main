package tars.commons.core;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

// @@author A0124333U
/**
 * Container for all key combinations used by program
 */
public class KeyCombinations {

    public static final KeyCombination KEY_COMB_CTRL_RIGHT_ARROW =
            new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination KEY_COMB_CTRL_LEFT_ARROW =
            new KeyCodeCombination(KeyCode.LEFT, KeyCombination.CONTROL_DOWN);

    public static final KeyCombination KEY_COMB_CTRL_Z =
            new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination KEY_COMB_CTRL_Y =
            new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
    
}
