package hashpizza.game.engine.platforming;

import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.system.Vector2f;

/**
 * Represents an arrow, which can be shot by the player
 */
public class Arrow extends GameEntity {

    /**
     * Arrow texture
     */
    private static final ConstTexture TEXTURE_ARROW = Textures.getTexture("./res/misc/arrow.png");

    /**
     * Creates an arrow, shot by the player
     *
     * @param screen       the screen parent
     * @param position     the position of the arrow
     * @param facingLeft   whether the entity should go left
     * @param drawDistance the bow's draw distance, this affects how fast the arrow can travel
     *                     (bounded, larger draw distance = faster arrow)
     */
    public Arrow(LevelScreen screen, Vector2f position, boolean facingLeft, float drawDistance) {
        super(screen, position, facingLeft, TEXTURE_ARROW);

        setGravity(false);

        velocity = new Vector2f((facingLeft ? -1 : 1) * Math.min(3f, Math.max(0.5f, drawDistance)) * 6f, 0);
    }

    @Override
    public void update(float delta) {

        super.update(delta);
    }

    @Override
    public boolean handleCollision(GameEntity collidesWith) {
        if (collidesWith instanceof Arrow || collidesWith instanceof Player)
            return false; //no collision with other arrows or the player

        if (collidesWith instanceof Enemy) {
            collidesWith.setHealth(0);
            getScreen().removeObject(collidesWith); //kill the object on collision
        }

        getScreen().removeObject(this); //remove ourselves on valid collision

        return true;
    }
}
