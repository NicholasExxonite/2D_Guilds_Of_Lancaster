package hashpizza.game.engine;

import hashpizza.game.engine.platforming.Shield;
import org.jsfml.audio.Music;
import org.jsfml.audio.Sound;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a screen which contains a collection of objects to render onto the window. One game screen can be
 * active on the window at a time
 */
public class GameScreen {

    /**
     * The background colour of the screen
     */
    private Color backgroundColor = Color.BLACK;

    /**
     * Whether or not this screen can be paused using the escape key
     */
    private boolean pausable;

    /**
     * The music to play in the background of the screen
     */
    private Music music = null;

    /**
     * The game window which will render this screen
     */
    private GameWindow window;

    /**
     * Object list for all the objects to be rendered to this screen
     */
    private List<GameObject> objects;

    /**
     * A collection of objects to be added on the next screen update - this is to prevent concurrent modification
     * exception
     */
    private List<GameObject> objectsToAdd;

    /**
     * A collection of objects to be removed on the next screen update - this is to prevent concurrent modification
     * exception
     */
    private List<GameObject> objectsToRemove;

    /**
     * Status flag as to whether objects have been modified (added/removed) since the last update
     */
    private boolean objectsModified = false;

    /**
     * Creates a game screen
     *
     * @param window the game window this screen will be rendered to
     */
    public GameScreen(GameWindow window) {
        this.window = window;

        objects = new ArrayList<>();
        objectsToAdd = new ArrayList<>();
        objectsToRemove = new ArrayList<>();
    }

    /**
     * Returns a list of all the objects to be rendered
     *
     * @return the object list
     */
    public List<GameObject> getObjects() {
        return objects;
    }

    /**
     * Returns a list of all the objects to be added on the next update
     *
     * @return the new object list
     */
    public List<GameObject> getObjectsToAdd() {
        return objectsToAdd;
    }

    /**
     * Returns a list of all the objects to be removed on the next update
     *
     * @return the object removal list
     */
    public List<GameObject> getObjectsToRemove() {
        return objectsToRemove;
    }

    /**
     * Adds an object to this screen
     *
     * @param obj the object to add
     */
    public void addObject(GameObject obj) {
        objectsToAdd.add(obj);
        objectsModified = true;
    }

    /**
     * Removes an object from this screen
     *
     * @param obj the object to remove
     */
    public void removeObject(GameObject obj) {
        objectsToRemove.add(obj);
        objectsModified = true;
    }

    /**
     * Removes all the given object's instances from game screen
     * @param obj
     */
    public void removeAllInstancesOf(GameObject obj)
    {
        for (GameObject o : objects)
        {

            if(o.getClass() == obj.getClass())
            {
                objectsToRemove.add(o);
            }
        }
        objectsModified = true;
    }

    /**
     * Returns whether this object will be removed on the next screen update
     *
     * @param object the object to check
     * @return whether or not this object will be removed on the next update
     */
    public boolean markedForRemoval(GameObject object) {
        return objectsToRemove.contains(object);
    }

    /**
     * Handles a JFML window event
     *
     * @param event the event to handle
     */
    public void handleEvent(Event event) {
        if (event instanceof KeyEvent) {
            KeyEvent ev = (KeyEvent) event;

            //get all objects which are key handlers to respond to this event...
            objects.forEach(o -> {
                if (o instanceof KeyHandler) {
                    KeyHandler kh = (KeyHandler) o;

                    if (event.type == Event.Type.KEY_PRESSED) {
                        kh.onKeyPress(ev.key);
                    } else {
                        kh.onKeyRelease(ev.key);
                    }
                }
            });
        }
    }

    /**
     * @return the background colour for this screen
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background colour for this screen
     *
     * @param backgroundColor the colour
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return the window which is rendering this screen
     */
    public GameWindow getWindow() {
        return window;
    }

    /**
     * @return whether or not this screen can be paused
     */
    public boolean isPausable() {
        return pausable;
    }

    /**
     * Sets whether or not this screen can be paused
     *
     * @param pausable true if this window can be paused
     */
    public void setPausable(boolean pausable) {
        this.pausable = pausable;
    }

    /**
     * @return the background music to play for this screen
     */
    public Music getMusic() {
        return music;
    }

    /**
     * Sets the background music to play for this screen
     *
     * @param music the music
     */
    public void setMusic(Music music) {
        this.music = music;
    }

    /**
     * @return whether or not objects have been modified (added/removed) since the last update
     */
    public boolean areObjectsModified() {
        return objectsModified;
    }

    /**
     * Draws all of the objects on this window to the screen and updates them with the specified
     * delta time
     *
     * @param target the render target to draw to
     * @param delta the time in seconds since the last update
     */
    public void update(RenderTarget target, float delta) {
        for (GameObject obj : getObjects()) {
            obj.update(delta); //update and draw each object for each game loop
            obj.draw(target, RenderStates.DEFAULT);
        }
    }

    /**
     * Resets objects modified flag after the screen update
     */
    public void postUpdate() {
        this.objectsModified = false;
    }
}
