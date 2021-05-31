package hashpizza.game.engine.platforming;

import hashpizza.game.engine.util.AnimatedTexture;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

/**
 * When called, will do it's animation then will remove itself. Alternatively, use this code + animation class
 * + GameSprite to draw this in the game.
 */
public class DeathParticles extends GameEntity {

    /**
     * The death particle effect animated texture
     */
    private final AnimatedTexture ANIMATED_TEXTURE_PARTICLES = new AnimatedTexture(0.15f,
            Textures.getTexture("./res/misc/death-smoke-1.png"),
            Textures.getTexture("./res/misc/death-smoke-2.png"),
            Textures.getTexture("./res/misc/death-smoke-3.png"),
            Textures.getTexture("./res/misc/death-smoke-4.png"),
            Textures.getTexture("./res/misc/death-smoke-5.png"),
            Textures.getTexture("./res/misc/death-smoke-6.png"),
            Textures.getTexture("./res/misc/death-smoke-7.png"),
            Textures.getTexture("./res/misc/death-smoke-8.png")
    );

    /**
     * Creates an death particle effect at a location.
     *
     * @param screen   The screen which the effect will be draw to.
     * @param position The position of the particles on the screen (Vector)
     * @param color    The color of the particles when drawn (should be the color of the ability)
     */
    public DeathParticles(LevelScreen screen, Vector2f position, Color color) {
        super(screen, position, false, Textures.getTexture("./res/misc/death-smoke-1.png"));
        setColor(color);

        //setOrigin(screen.getPlayer().getLocalBounds().width * 0.25f, screen.getPlayer().getLocalBounds().height * 0.25f);

        setPhysics(false);
        setIgnoresCollisions(true);
        setHitbox(null);
    }

    /**
     * When spawned, the death particles will go through its effect and will remove itself once it is finished.
     */
    @Override
    public void update(float delta) {
        ANIMATED_TEXTURE_PARTICLES.update(delta);

        if (ANIMATED_TEXTURE_PARTICLES.isComplete()) {
            getScreen().removeObject(this);
            return;
        }

        setTexture(ANIMATED_TEXTURE_PARTICLES.update(0));

        super.update(delta);
    }

    @Override
    public boolean handleCollision(GameEntity collidesWith) {
        return false; //we don't collide with anything
    }
}
