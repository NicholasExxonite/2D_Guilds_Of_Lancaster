package hashpizza.game.engine.platforming;

import hashpizza.game.engine.util.DeltaTimer;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Vector2f;

/**
 * Represents a goose enemy that spits at the player and can damage the player if it is collided with
 */
public class GooseSpitter extends Enemy {

    /**
     * Goose idle textures
     */
    private static final ConstTexture TEXTURE_1 = Textures.getTexture("./res/enemies/spitter-1.png");
    private static final ConstTexture TEXTURE_2 = Textures.getTexture("./res/enemies/spitter-2.png");
    private static final ConstTexture TEXTURE_3 = Textures.getTexture("./res/enemies/spitter-3.png");
    private static final ConstTexture TEXTURE_STUNNED = Textures.getTexture("./res/enemies/spitter-disabled.png");

    /**
     * Offsets for creating the spit entities
     */
    private final Vector2f SPIT_OFFSET_LEFT = new Vector2f(-45, 35);
    private final Vector2f SPIT_OFFSET = new Vector2f(120, 35);

    /**
     * Timer for goose animations and spitting
     */
    private float spitterTimer;

    /**
     * Cooldown between subsequent spits and texture updates after a geese has spat
     */
    private DeltaTimer spitterCooldown = null;

    /**
     * Creates a goose spitter enemy
     *
     * @param screen     the screen parent
     * @param position   the position of the spitter
     * @param facingLeft whether or not it is facing left
     */
    public GooseSpitter(LevelScreen screen, Vector2f position, boolean facingLeft) {
        super(screen, position, facingLeft, TEXTURE_1);

        setPhysics(true);
        setHitbox(new FloatRect(28, 28, 76, 100));
    }

    @Override
    public void update(float delta) {
        if (!((LevelScreen) getScreen()).isActive()) return; //don't do anything in demo mode

        /*
        If the spitter gets stunned. This section of the code manages that.
         */
        if (isStunned()) {
            setTexture(TEXTURE_STUNNED);
        } else {
            /*
             * Faces the player
             *
             */
            if (((LevelScreen) getScreen()).getPlayer() != null) {
                setFacingLeft(((LevelScreen) getScreen()).getPlayer().getPosition().x < getPosition().x);
            }
            /*
            The spitter spits every few seconds. This section of the code manages that.
             */
            if (spitterCooldown != null) {
                if (spitterCooldown.update(delta)) { //if the timer is complete, we can remove it and carry on now
                    spitterCooldown = null;
                }
            } else {
                spitterTimer += delta;
                if (spitterTimer > 3f) {
                    setTexture(TEXTURE_3);
                    this.spit();
                } else if (spitterTimer > 2f) {
                    setTexture(TEXTURE_2);
                } else {
                    setTexture(TEXTURE_1);
                }
            }
        }

        super.update(delta);
    }

    /**
     * Makes the geese spit from its current location
     */
    public void spit() {
        Vector2f spitPos = Vector2f.add(getPosition(), isFacingLeft() ? SPIT_OFFSET_LEFT : SPIT_OFFSET);

        GooseSpit spit = new GooseSpit((LevelScreen) getScreen(), spitPos, isFacingLeft());
        getScreen().addObject(spit);

        spitterCooldown = new DeltaTimer(1);
        spitterTimer = 0;
    }
}
