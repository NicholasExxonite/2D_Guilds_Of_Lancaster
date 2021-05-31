package hashpizza.game.engine.platforming;

import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.system.Vector2f;

/**
 * Represents a spit projectile fired by a goose spitter
 */
public class GooseSpit extends Enemy {

    /**
     * The texture for the spit entity
     */
    private static final ConstTexture TEXTURE_SPIT = Textures.getTexture("./res/misc/spit.png");

    /**
     * Creates a goose spit entity
     *
     * @param screen   the screen parent
     * @param position the position of the spit
     * @param left     whether the spit should travel left
     */
    public GooseSpit(LevelScreen screen, Vector2f position, boolean left) {
        super(screen, position, left, TEXTURE_SPIT);

        setGravity(false);
        velocity = new Vector2f(left ? -4 : 4, 0);
    }

    @Override
    public void update(float delta) {

        super.update(delta);
    }

    @Override
    public boolean handleCollision(GameEntity collidesWith) {
        if (collidesWith instanceof GooseSpit) return false;
        if (collidesWith instanceof Player) {
            ((LevelScreen) getScreen()).getPlayer().dealDamage(1);
        }

        getScreen().removeObject(this); //remove ourselves if we collide with anything

        return false; //spit has no physical collision
    }
}
