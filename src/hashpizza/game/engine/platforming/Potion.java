package hashpizza.game.engine.platforming;

import hashpizza.game.engine.util.AnimatedTexture;
import hashpizza.game.engine.util.DeltaTimer;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.system.Vector2f;

/**
 * Potion used by the Pendle ability
 */
public class Potion extends GameEntity {

    /**
     * The textures to use for the potion
     */
    private static ConstTexture TEXTURE_POTION = Textures.getTexture("./res/misc/potion.png");
    private static ConstTexture TEXTURE_POTION_SPLASH = Textures.getTexture("./res/misc/poison0.png");
    private static ConstTexture TEXTURE_POTION_SPLASH_2 = Textures.getTexture("./res/misc/poison1.png");
    private static ConstTexture TEXTURE_POTION_SPLASH_3 = Textures.getTexture("./res/misc/poison2.png");
    private static ConstTexture TEXTURE_POTION_SPLASH_4 = Textures.getTexture("./res/misc/poison3.png");

    /**
     * Animated splash texture
     */
    private static AnimatedTexture ANIMATED_TEXTURE_POTION = new AnimatedTexture(0.2f, TEXTURE_POTION_SPLASH, TEXTURE_POTION_SPLASH_2, TEXTURE_POTION_SPLASH_3, TEXTURE_POTION_SPLASH_4);

    /**
     * Timer until this object is automatically removed
     */
    private DeltaTimer leaveTimer = new DeltaTimer(3.5f);

    /**
     * Creates a potion
     *
     * @param screen   the level screen for this potion
     * @param position the location
     * @param left     if this potion should be thrown to the left (otherwise right)
     */
    public Potion(LevelScreen screen, Vector2f position, boolean left) {
        super(screen, position, left, TEXTURE_POTION);

        velocity = new Vector2f((isFacingLeft() ? -1 : 1) * 3, -5);
    }

    @Override
    public void update(float delta) {
        if (isOnGround()) {
            setTexture(ANIMATED_TEXTURE_POTION.update(delta));
            velocity = new Vector2f(0, 0);
        }

        if (leaveTimer.update(delta)) { //time is up!
            getScreen().removeObject(this);
        }

        super.update(delta);
    }

    @Override
    public boolean handleCollision(GameEntity collidesWith) {
        if (collidesWith instanceof Potion || collidesWith instanceof Player) return false;
        if (collidesWith instanceof Enemy) getScreen().removeObject(collidesWith);
        return true;
    }
}