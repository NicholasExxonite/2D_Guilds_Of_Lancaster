package hashpizza.game.engine.platforming;

import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConstTexture;

/**
 * A type of block which will damage the player if it collides with them
 */
public class Spikes extends Block {

    /**
     * The spike texture
     */
    private static final ConstTexture TEXTURE_SPIKE = Textures.getTexture("./res/misc/spike.png");

    /**
     * Creates a spike block at the specified location
     *
     * @param screen the level screen for this block
     * @param x      the x grid co-ordinate
     * @param y      the y grid co-ordinate
     * @param colour the colouration of the block to apply
     */
    public Spikes(LevelScreen screen, int x, int y, Color colour) {
        super(screen, x, y, colour, TEXTURE_SPIKE);
    }

    @Override
    public void update(float delta) {
        Player player = ((LevelScreen) getScreen()).getPlayer();
        if (player != null && collidesWith(getExpandedHitbox(), player)) {
            ((LevelScreen) getScreen()).getPlayer().dealDamage(1); //damage the player if we contact it
        }

        super.update(delta);
    }
}
