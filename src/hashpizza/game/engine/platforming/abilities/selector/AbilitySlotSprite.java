package hashpizza.game.engine.platforming.abilities.selector;

import hashpizza.game.engine.GameSprite;
import hashpizza.game.engine.platforming.abilities.Ability;
import hashpizza.game.engine.util.Animation;
import hashpizza.game.engine.util.Animations;
import org.jsfml.graphics.Color;

/**
 * Handles the ability images that can be displayed inside of ability slots
 */
public class AbilitySlotSprite extends GameSprite {

    /**
     * The ability that this sprite represents
     */
    private final Ability ability;

    /**
     * The current slot that this ability is in
     */
    private AbilitySlot slot;

    /**
     * Movement animations
     */
    private Animation movementAnimationX;
    private Animation movementAnimationY;

    /**
     * Scaling animations
     */
    private Animation scaleAnimationX;
    private Animation scaleAnimationY;

    /**
     * Whether the ability is in progress
     */
    private boolean activated;

    /**
     * Creates an ability slot sprite for the specified ability, to be placed in the specified slot, or null
     *
     * @param ability the ability itself
     * @param slot    the slot to place this ability in
     */
    public AbilitySlotSprite(Ability ability, AbilitySlot slot) {
        super(null, ability.getImage());

        this.ability = ability;
        this.slot = slot;

        setPosition(slot.getPosition());
        setScale(slot.getScale());
        setOrigin(getTexture().getSize().x * 0.5f, getTexture().getSize().y * 0.5f);
    }

    /**
     * Moves this ability to the new specified slot
     *
     * @param slot the new slot to move to
     */
    public void moveToSlot(AbilitySlot slot) {
        //animate its position
        movementAnimationX = new Animation(getPosition().x, slot.getPosition().x, 0.6f, false, Animations.EASE_OUT);
        movementAnimationY = new Animation(getPosition().y, slot.getPosition().y, 0.6f, false, Animations.EASE_OUT);

        scaleAnimationX = new Animation(getScale().x, slot.getScale().x, 0.6f, false, Animations.EASE_OUT);
        scaleAnimationY = new Animation(getScale().y, slot.getScale().y, 0.6f, false, Animations.EASE_OUT);

        this.slot = slot;
    }

    /**
     * Set activated when the player is using the ability
     *
     * @param activated whether the player is using the ability
     */
    public void setActivated(boolean activated) {

        if (activated) {
            setColor(Color.YELLOW);
        } else {
            setColor(Color.WHITE);
        }
    }

    @Override
    public void update(float delta) {
        if (movementAnimationX != null) { //movement animations!
            float x = movementAnimationX.update(delta);
            float y = movementAnimationY.update(delta);

            setPosition(x, y);

            if (movementAnimationX.isComplete()) {
                movementAnimationX = null;
                movementAnimationY = null;
            }
        }

        if (scaleAnimationX != null) {
            float x = scaleAnimationX.update(delta);
            float y = scaleAnimationY.update(delta);

            setScale(x, y);

            if (scaleAnimationX.isComplete()) {
                scaleAnimationX = null;
                scaleAnimationY = null;
            }
        }

        if (ability != null && ability.getCooldownTimer() != null && !ability.getCooldownTimer().isComplete()) {
            setColor(new Color(128, 128, 128)); //slightly greyed out
        } else {
            setColor(activated ? Color.YELLOW : Color.WHITE);
        }
    }

    /**
     * @return the ability slot that this ability is currently in
     */
    public AbilitySlot getSlot() {
        return slot;
    }

    /**
     * @return the ability represented by this slot sprite
     */
    public Ability getAbility() {
        return ability;
    }
}
