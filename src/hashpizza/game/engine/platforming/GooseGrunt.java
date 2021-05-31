package hashpizza.game.engine.platforming;

import hashpizza.game.engine.util.AnimatedTexture;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Vector2f;


/**
 * Represents a goose enemy that can damage the player if it is collided with
 */
public class GooseGrunt extends Enemy {

    //Idle textures
    private static final ConstTexture TEXTURE_IDLE_1 = Textures.getTexture("./res/enemies/grunt-idle-1.png");
    private static final ConstTexture TEXTURE_IDLE_2 = Textures.getTexture("./res/enemies/grunt-idle-2.png");
    private static final ConstTexture TEXTURE_STUNNED = Textures.getTexture("./res/enemies/grunt-disabled.png");

    private final AnimatedTexture ANIMATED_TEXTURE_IDLE = new AnimatedTexture(1f, TEXTURE_IDLE_1, TEXTURE_IDLE_2);

    public GooseGrunt(LevelScreen screen, Vector2f position, boolean facingLeft) {
        super(screen, position, facingLeft, TEXTURE_IDLE_1);

        setPhysics(true);
        setHitbox(new FloatRect(28, 28, 76, 100));
    }

    @Override
    public void update(float delta) {
        if (isStunned()) {
            setTexture(TEXTURE_STUNNED);
            velocity = new Vector2f(0, velocity.y);
        } else {
            setTexture((TEXTURE_IDLE_1));
            velocity = new Vector2f(isFacingLeft() ? -0.5f : 0.5f, velocity.y);
            if (getCollisionAtPosition(calculateNextPosition(delta, true, false),
                    ((LevelScreen) getScreen()).getEntities()) instanceof Block || getCollisionAtPosition(calculateNextPosition(delta, true, false),
                    ((LevelScreen) getScreen()).getEntities()) instanceof Spikes) {
                setFacingLeft(!isFacingLeft()); //switch direction, mario goomba style
            }

            setTexture(ANIMATED_TEXTURE_IDLE.update(delta));
        }

        super.update(delta);
    }
}
