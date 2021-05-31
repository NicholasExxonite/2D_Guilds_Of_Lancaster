package hashpizza.game.engine;

import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.Sprite;

/**
 * Class to represent a textured object to render to the screen.
 * Uses the JSFML library to handle drawing internally
 */
public class GameSprite extends Sprite implements GameObject {

    /**
     * The screen that this sprite belongs to
     */
    private GameScreen screen;

    /**
     * Creates a game sprite with the specified texture
     *
     * @param texture the texture to use
     */
    public GameSprite(GameScreen screen, ConstTexture texture) {
        super(texture);

        this.screen = screen;
    }

    /**
     * Sets the sprite to an exact width and height, using the scale feature
     *
     * @param width  the width to set this sprite to
     * @param height the height to set this sprite to
     */
    public void setSize(float width, float height) {
        setScale(width / getTexture().getSize().x, height / getTexture().getSize().y);
    }

    /**
     * Gets the screen
     *
     * @return the screen that this sprite belongs to
     */
    public GameScreen getScreen() {
        return screen;
    }

    @Override
    public void update(float delta) {

    }
}
