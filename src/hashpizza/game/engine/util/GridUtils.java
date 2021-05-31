package hashpizza.game.engine.util;

import org.jsfml.system.Vector2f;

/**
 * Static utilities to do with mapping grid co-ordinates onto the actual pixel values needed to show entities in the
 * correct position on the screen
 */
public final class GridUtils {

    /**
     * The number of horizontal tiles that can be displayed per screen
     */
    public static final int X_TILES_PER_SCREEN = 32;

    /**
     * The number of vertical tiles that can be displayed per screen
     */
    public static final int Y_TILES_PER_SCREEN = 18;

    /**
     * The screen width, in pixels
     */
    public static final int SCREEN_WIDTH = 1920;

    /**
     * The screen height, in pixels
     */
    public static final int SCREEN_HEIGHT = 1080;

    /**
     * The width of each tile
     */
    public static final int TILE_WIDTH = SCREEN_WIDTH / X_TILES_PER_SCREEN;

    /**
     * The height of each tile
     */
    public static final int TILE_HEIGHT = SCREEN_HEIGHT / Y_TILES_PER_SCREEN;

    /**
     * Returns a vector mapping in pixel co-ordinates of the specified grid-coordinates
     *
     * @param x the x co-ordinate
     * @param y the y co-ordinate
     * @return the pixel mapping
     */
    public static Vector2f convertGridCoordinatesToPixels(int x, int y) {
        return new Vector2f(TILE_WIDTH * x, TILE_HEIGHT * y);
    }
}
