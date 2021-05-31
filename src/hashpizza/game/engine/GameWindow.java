package hashpizza.game.engine;

import hashpizza.game.engine.ui.screens.PauseScreen;
import hashpizza.game.engine.ui.screens.SplashScreen;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;
import org.jsfml.window.event.Event;

/**
 * Represents the window containing a game screen, which has all of the objects to render
 */
public class GameWindow {

    /**
     * The screen that's currently active to render onto the window
     */
    private GameScreen activeScreen;

    /**
     * If the game is paused, we will switch to the pause screen, when it is un-paused it should resume
     * whatever screen is stored here
     */
    private GameScreen prePauseScreen;

    /**
     * The window's width
     */
    private int width;

    /**
     * The window's height
     */
    private int height;

    /**
     * Slow-motion mode toggle, used for debugging the game's movement etc.
     */
    private boolean debugSlowMotion = false;

    /**
     * Creates the game window with the specified width and height
     *
     * @param width  the width of the window
     * @param height the height of the window
     */
    public GameWindow(int width, int height) {

        RenderWindow window = new RenderWindow();
        window.create(new VideoMode(this.width = width, this.height = height), "Guilds of Lancaster", WindowStyle.NONE);
        window.setMouseCursorVisible(false);
        window.setVerticalSyncEnabled(true);
        //window.setFramerateLimit(60);

        Image logo = Textures.getTexture("./res/icon.png").copyToImage();
        window.setIcon(logo);

        setActiveScreen(new SplashScreen(this)); //start at the splash screen
        //setActiveScreen(new LevelSelectScreen(this)); //start at the splash screen

        Clock clock = new Clock();

        while (window.isOpen()) {
            float delta = clock.restart().asSeconds(); //time in seconds since last draw

            delta = Math.min(0.5f, delta); //max the delta out to try to avoid tunneling

            if (debugSlowMotion) delta *= 0.1f; //in slow-mo mode, run at 10x slower speed

            window.clear(activeScreen == null ? Color.BLACK : activeScreen.getBackgroundColor());

            if (activeScreen != null) {
                //update and draw all objects onto the screen
                activeScreen.update(window, delta);

                //handle removing and adding objects here
                activeScreen.getObjects().removeAll(activeScreen.getObjectsToRemove());
                activeScreen.getObjectsToRemove().clear();

                activeScreen.getObjects().addAll(activeScreen.getObjectsToAdd());
                activeScreen.getObjectsToAdd().clear();

                activeScreen.postUpdate();
            }

            window.display();

            //handle window events
            for (Event ev : window.pollEvents()) {
                if (ev.type == Event.Type.CLOSED) {
                    window.close();
                } else if (ev.type == Event.Type.KEY_PRESSED || ev.type == Event.Type.KEY_RELEASED) {
                    activeScreen.handleEvent(ev);

                    //handling pauses...
                    if (ev.asKeyEvent().key == Keyboard.Key.ESCAPE && ev.type == Event.Type.KEY_PRESSED) {
                        if (activeScreen instanceof PauseScreen) { //if it's already paused, unpause
                            ((PauseScreen) activeScreen).unpause();
                            prePauseScreen = null;
                        } else if (activeScreen.isPausable()) { //if the screen is pausable, pause it
                            prePauseScreen = activeScreen;
                            setActiveScreen(new PauseScreen(this, prePauseScreen, window));
                        }
                    } else if (ev.asKeyEvent().key == Keyboard.Key.P && ev.type == Event.Type.KEY_PRESSED) {
                        debugSlowMotion = !debugSlowMotion;
                    }
                }
            }
        }
    }

    /**
     * @return the window width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the window height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Changes the current active screen
     *
     * @param screen the GameScreen which will be the new active screen
     */
    public void setActiveScreen(GameScreen screen) {

        //whether the previous audio should carry on playing - it should if it is the same as the new screen's audio
        boolean continuePlaying = activeScreen != null && activeScreen.getMusic() != null && activeScreen.getMusic().equals(screen.getMusic());

        if (!continuePlaying) {
            if (activeScreen != null && activeScreen.getMusic() != null) {
                activeScreen.getMusic().stop(); //stop previous music
            }

            if (screen.getMusic() != null) {
                screen.getMusic().setPlayingOffset(Time.ZERO);
                screen.getMusic().play(); //start the new music
            }
        }

        activeScreen = screen;
    }
}
