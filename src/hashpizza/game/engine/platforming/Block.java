package hashpizza.game.engine.platforming;

import hashpizza.game.engine.util.GridUtils;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

/**
 * Represents a block with collision which can be displayed onto the window
 */
public class Block extends GameEntity {

    /**
     * Tile textures
     */
    static final ConstTexture BLOCK = Textures.getTexture("./res/misc/base-foreground.png");
    static final ConstTexture DIRT = Textures.getTexture("./res/misc/dirt-foreground.png");
    static final ConstTexture GRASS = Textures.getTexture("./res/misc/grass-foreground.png");

    /**
     * Creates a block
     *
     * @param screen  the screen to display this entity on
     * @param x       the x grid co-ordinate
     * @param y       the y grid co-ordinate
     * @param colour  the colouring to apply to this block
     * @param texture the texture to use for this block
     */
    public Block(LevelScreen screen, int x, int y, Color colour, ConstTexture texture) {
        super(screen, Vector2f.ZERO, false, texture);

        setPosition(GridUtils.convertGridCoordinatesToPixels(x, y));
        setIgnoresCollisions(true);

        RectangleShape rs = new RectangleShape(new Vector2f(GridUtils.TILE_WIDTH, GridUtils.TILE_HEIGHT));
        rs.setPosition(GridUtils.convertGridCoordinatesToPixels(x, y));

        if (colour != null) setColor(colour);
        setPhysics(true);
        setGravity(false);
    }
}
