package hashpizza.game.engine.platforming;

import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Vector2f;

/**
 * Represents a goose enemy that can damage the player if it is collided with
 */
public class GooseSpike extends Enemy {

    /**
     * Goose spike textures
     */
    private static final ConstTexture TEXTURE_IDLE_1 = Textures.getTexture("./res/enemies/spike-idle-1.png");
    private static final ConstTexture TEXTURE_IDLE_2 = Textures.getTexture("./res/enemies/spike-idle-2.png");
    private static final ConstTexture TEXTURE_STUNNED = Textures.getTexture("./res/enemies/spike-disabled.png");

    /**
     * How long the goose spike should remain idle for
     */
    private float idleAnimationFrameCounter = 0;

    public GooseSpike(LevelScreen screen, Vector2f position, boolean facingLeft) {
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
            setTexture(TEXTURE_IDLE_1);
            velocity = new Vector2f(isFacingLeft() ? -0.5f : 0.5f, velocity.y);
            if (getCollisionAtPosition(calculateNextPosition(delta, true, false), ((LevelScreen) getScreen()).getEntities()) instanceof Block ||
                    getCollisionAtPosition(calculateNextPosition(delta, true, false),
                            ((LevelScreen) getScreen()).getEntities()) instanceof Spikes) {
                setFacingLeft(!isFacingLeft()); //switch direction, mario goomba style
            }
            idleAnimationFrameCounter += delta;
            if (idleAnimationFrameCounter > 20) {
                idleAnimationFrameCounter = 0;

                if (getTexture() == TEXTURE_IDLE_1) {
                    setTexture(TEXTURE_IDLE_2);
                } else {
                    setTexture(TEXTURE_IDLE_1);
                }
            }
        }

        super.update(delta);
    }
}
