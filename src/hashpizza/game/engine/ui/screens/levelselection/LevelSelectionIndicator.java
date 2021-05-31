package hashpizza.game.engine.ui.screens.levelselection;

import hashpizza.game.engine.GameScreen;
import hashpizza.game.engine.GameSprite;
import hashpizza.game.engine.util.Animation;
import hashpizza.game.engine.util.Animations;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConstTexture;

/**
 * Level select indicator, which highlights the currently selected level on the world map
 */
public class LevelSelectionIndicator extends GameSprite {

    /**
     * The level selection texture
     */
    private static final ConstTexture TEXTURE_LEVEL_SELECT = Textures.getTexture("./res/misc/ui/level_select_indicator.png");

    /**
     * The level selector should pulse red
     */
    private Color colour = new Color(255, 0, 0, 0);

    /**
     * Colour animation for the selected level
     */
    private Animation colourAnim = new Animation(0, 1, 1, true, Animations.EASE_IN_OUT);

    /**
     * Creates the level selection indicator for the screen specified
     *
     * @param screen the screen
     */
    public LevelSelectionIndicator(GameScreen screen) {
        super(screen, TEXTURE_LEVEL_SELECT);
    }

    @Override
    public void update(float delta) {

        float prog = colourAnim.update(delta);

        int rgb = 80 + (int) (prog * (175));

        setColor(Color.add(colour, new Color(0, 0, 0, rgb)));
    }
}
