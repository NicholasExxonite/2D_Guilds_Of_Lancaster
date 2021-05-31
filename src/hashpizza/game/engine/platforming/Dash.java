package hashpizza.game.engine.platforming;

import hashpizza.game.engine.util.AnimatedTexture;
import hashpizza.game.engine.util.DeltaTimer;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.system.Vector2f;

/**
 * Dash trail effect for using the fylde ability
 */
public class Dash extends GameEntity {

    /**
     * Textures
     */
    private static final ConstTexture TEXTURE_DASH = Textures.getTexture("./res/player/abilities/player-fylde-2.png");
    private static final ConstTexture TEXTURE_DASH_2 = Textures.getTexture("./res/player/abilities/player-fylde-3.png");

    /**
     * Animated texture, 500ms per frame
     */
    private final AnimatedTexture ANIMATED_TEXTURE_BACKGROUND = new AnimatedTexture(0.5f, TEXTURE_DASH, TEXTURE_DASH_2);

    /**
     * Creates a dash trail at the specified position
     *
     * @param screen   the screen parent
     * @param position the position for the dash, offset if facing left
     * @param left     whether the entity should face left
     */
    public Dash(LevelScreen screen, Vector2f position, boolean left) {
        super(screen, position, left, TEXTURE_DASH);

        setPhysics(false);
        setFacingLeft(left);
        setHitbox(null);

        if (left) {
            position = Vector2f.add(position, new Vector2f(-100, 0));
            setPosition(position);
        }
    }

    @Override
    public void update(float delta) {
        ANIMATED_TEXTURE_BACKGROUND.update(delta);

        if (ANIMATED_TEXTURE_BACKGROUND.isComplete()) {
            getScreen().removeObject(this);
        } else {
            setTexture(ANIMATED_TEXTURE_BACKGROUND.update(0));
        }

        super.update(delta);
    }
}