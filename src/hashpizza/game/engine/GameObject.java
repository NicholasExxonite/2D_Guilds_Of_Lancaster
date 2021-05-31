package hashpizza.game.engine;

import org.jsfml.graphics.Drawable;

/**
 * Represents an object which can be added to a screen, and updated and drawn onto it when the screen updates
 */
public interface GameObject extends Drawable {

    /**
     * Called when the screen updates, to update this object. For example, to move it, handle collisions, update
     * animations, etc.
     *
     * @param delta the amount of time (in seconds) since the last update of this object. This should be used to
     *              calculate how much to update values by (e.g. movement), to prevent the game running slower
     *              on slower computers, and vice versa
     */
    void update(float delta);
}
