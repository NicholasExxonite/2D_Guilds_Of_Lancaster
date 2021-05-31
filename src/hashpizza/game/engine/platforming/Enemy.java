package hashpizza.game.engine.platforming;

import hashpizza.game.engine.util.DeltaTimer;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.system.Vector2f;


/**
 * Represents an enemy that can damage the player
 */
public class Enemy extends GameEntity {

    /**
     * How long this eenemy should be stunned for
     */
    private DeltaTimer stunTimer;

    /**
     * Creates an enemy to be displayed on the screen
     *
     * @param screen     the level screen that this enemy is contained in
     * @param position   the position of the enemy
     * @param facingLeft whether the entity should be rendered facing left (mirrored landscape)
     * @param texture    the texture to use for this entity
     */
    public Enemy(LevelScreen screen, Vector2f position, boolean facingLeft, ConstTexture texture) {
        super(screen, position, facingLeft, texture);
    }

    @Override
    public void update(float delta) {
        if (stunTimer != null) {
            boolean complete = stunTimer.update(delta);
            if (complete) stunTimer = null;
        }

        super.update(delta);
    }

    /**
     * Sets the duration that this enemy that should be stunned for, or null if this enemy should no longer be
     * stunned
     *
     * @param stunTimer how long to stun this enemy for
     */
    public void setStunTimer(DeltaTimer stunTimer) {
        this.stunTimer = stunTimer;
    }

    /**
     * @return the stun timer, for how long until this enemy stops being stunned, or null if it is not stunned
     */
    public DeltaTimer getStunTimer() {
        return stunTimer;
    }

    /**
     * @return whether this enemy is currently stunned (by the lonsdale ability)
     */
    public boolean isStunned() {
        return stunTimer != null && !stunTimer.isComplete();
    }

    @Override
    public boolean handleCollision(GameEntity collidesWith) {
        if (collidesWith instanceof Player) {
            collidesWith.dealDamage(1);
            return false;
        }

        return true;
    }
}
