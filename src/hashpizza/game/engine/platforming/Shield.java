package hashpizza.game.engine.platforming;

import hashpizza.game.engine.util.Animation;
import hashpizza.game.engine.util.DeltaTimer;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.system.Vector2f;

/**
 * Shield used by the Grizedale ability
 */
public class Shield extends GameEntity {

    /**
     * The texture of the shield
     */
    private static ConstTexture TEXTURE_SHIELD = Textures.getTexture("./res/player/abilities/player-grizedale.png");

    /**
     * To know when the shield will expire
     */
    private final DeltaTimer expiryTimer;

    /**
     * The shield should fade to disappear
     */
    private Animation expiryOpacityAnimation;

    /**
     * Creates a shield
     *
     * @param screen      the level screen
     * @param position    the position for the shield to be
     * @param expiryTimer the timer until the shield should disappear
     */
    public Shield(LevelScreen screen, Vector2f position, DeltaTimer expiryTimer) {
        super(screen, position, false, TEXTURE_SHIELD);
        this.expiryTimer = expiryTimer;

        setHitbox(null);
        setPhysics(false);
    }

    @Override
    public void update(float delta) {
        setPosition(((LevelScreen) getScreen()).getPlayer().getPosition());

        super.update(delta);
    }
}
