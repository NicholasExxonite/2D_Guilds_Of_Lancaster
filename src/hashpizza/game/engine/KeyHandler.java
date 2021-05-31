package hashpizza.game.engine;

import org.jsfml.window.Keyboard;

/**
 * ALlows responding to key presses
 */
public interface KeyHandler {

    /**
     * Handler for when a key is pressed down
     *
     * @param key the key that was pressed down
     */
    void onKeyPress(Keyboard.Key key);

    /**
     * Handler for a pressed key when it is released
     *
     * @param key the key that was released
     */
    void onKeyRelease(Keyboard.Key key);
}
