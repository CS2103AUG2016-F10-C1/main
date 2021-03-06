package tars.commons.events.ui;

import javafx.scene.input.KeyCombination;
import tars.commons.events.BaseEvent;

// @@author A0124333U
/**
 * Indicates that the user has pressed a key combination
 */
public class KeyCombinationPressedEvent extends BaseEvent {

    private KeyCombination keyComb;

    public KeyCombinationPressedEvent(KeyCombination keyComb) {
        this.keyComb = keyComb;
    }

    public KeyCombination getKeyCombination() {
        return keyComb;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
