package hashpizza.game.engine.ui;

import hashpizza.game.engine.GameObject;
import org.jsfml.graphics.ConstFont;

/**
 * A wrapper class for the JSFML text class as a game object
 */
public class Text extends org.jsfml.graphics.Text implements GameObject {

    /**
     * Creates a text object for the specified text and font
     *
     * @param text the text to render
     * @param font the font to use
     */
    public Text(String text, ConstFont font) {
        super(text, font);
    }

    @Override
    public void update(float delta) {

    }
}
