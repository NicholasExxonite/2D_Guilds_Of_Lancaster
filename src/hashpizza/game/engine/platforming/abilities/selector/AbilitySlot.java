package hashpizza.game.engine.platforming.abilities.selector;

import hashpizza.game.engine.GameObject;
import hashpizza.game.engine.util.Animation;
import hashpizza.game.engine.util.Animations;
import hashpizza.game.engine.util.Fonts;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * Represents an ability slot, which can be taken up by an ability slot sprite
 */
public class AbilitySlot implements GameObject {

    /**
     * The position of the ability slot
     */
    private final Vector2f position;

    /**
     * The scale that abilities within the slots should be
     */
    private final Vector2f scale;

    /**
     * The background slot sprite
     */
    private Sprite slotSprite;

    /**
     * The ability contained within this slot
     */
    private AbilitySlotSprite ability;

    /**
     * Text overlaid on the slot if the ability is in cooldown
     */
    private Text cooldownText;

    /**
     * Whether this slot is currently selected whilst the ability selector is active
     */
    private boolean selected;

    /**
     * Scale animation which is used when the ability slot switches between its selected state
     */
    private Animation scaleAnimation;

    /**
     * Creates an ability slot with the specified parameters
     *
     * @param position the position of the slot
     * @param scale    the scale of ability icons that can be contained within
     * @param ability  the current ability within the slot
     * @param texture  the texture to use for the ability slot
     */
    public AbilitySlot(Vector2f position, Vector2f scale, AbilitySlotSprite ability, ConstTexture texture) {

        this.position = position;
        this.scale = scale;
        this.ability = ability;

        slotSprite = new Sprite(texture);
        slotSprite.setOrigin(slotSprite.getLocalBounds().width * 0.5f, slotSprite.getLocalBounds().height * 0.5f);
        slotSprite.setPosition(position);

        cooldownText = new Text("", Fonts.PIXEL);
        cooldownText.setOrigin(cooldownText.getLocalBounds().width * 0.5f, cooldownText.getLocalBounds().height * 0.75f);
        cooldownText.setPosition(Vector2f.add(position, slotSprite.getOrigin()));
    }

    /**
     * Sets whether or not this slot is currently selected in the selector
     *
     * @param selected whether this slot is selected
     */
    public void setSelected(boolean selected) {

        this.selected = selected;

        if (selected) {
            scaleAnimation = new Animation(1.0f, 1.1f, 0.15f, false, Animations.EASE_IN_OUT);
        } else {
            scaleAnimation = new Animation(1.1f, 1.0f, 0.15f, false, Animations.EASE_IN_OUT);
        }
    }

    /**
     * @return whether this slot is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @return the slot ability slot, if an ability is in its slot
     */
    public Sprite getSlotSprite() {
        return slotSprite;
    }

    /**
     * @return the current ability for the slot
     */
    public AbilitySlotSprite getAbility() {
        return ability;
    }

    /**
     * Sets the ability sprite to display within this slot
     *
     * @param ability the ability sprite
     */
    public void setAbility(AbilitySlotSprite ability) {

        //do a swap of the slots if needed
        if (ability != null) {
            AbilitySlot slotB = ability.getSlot();

            ability.moveToSlot(this);

            if (this.ability != null) {
                this.ability.moveToSlot(slotB);
            }

            slotB.ability = this.ability;
        }

        this.ability = ability;
    }

    /**
     * @return the position of the ability slot
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * @return the scale of sprites that should be contained in this slot
     */
    public Vector2f getScale() {
        return scale;
    }

    @Override
    public void update(float delta) {
        if (ability != null) {
            ability.update(delta);

            //cooldown timer
            if (ability.getAbility() != null && ability.getAbility().getCooldownTimer() != null && !ability.getAbility().getCooldownTimer().isComplete()) {
                int remaining = (int) ability.getAbility().getCooldownTimer().getRemainingTime() + 1;

                cooldownText.setString("" + remaining);
            } else {
                cooldownText.setString("");
            }

            cooldownText.setOrigin(cooldownText.getLocalBounds().width * 0.5f, cooldownText.getLocalBounds().height * 0.75f);
        }

        if (scaleAnimation != null) {
            float scale = scaleAnimation.update(delta);

            slotSprite.setScale(scale, scale);

            if (scaleAnimation.isComplete()) {
                scaleAnimation = null;
            }
        }
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        slotSprite.draw(renderTarget, renderStates);
        cooldownText.draw(renderTarget, renderStates);
    }
}
