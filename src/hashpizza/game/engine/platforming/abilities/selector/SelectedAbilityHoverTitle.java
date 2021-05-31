package hashpizza.game.engine.platforming.abilities.selector;

import hashpizza.game.engine.GameObject;
import hashpizza.game.engine.platforming.abilities.Ability;
import hashpizza.game.engine.util.Animation;
import hashpizza.game.engine.util.Animations;
import hashpizza.game.engine.util.Fonts;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * Displays information about the currently hovered ability in the level selector
 */
public class SelectedAbilityHoverTitle implements GameObject {

    /**
     * Texture for the bubble
     */
    private static final ConstTexture TEXTURE_ABILITY_BUBBLE = Textures.getTexture("./res/misc/chat-bubble.png");

    /**
     * The ability name
     */
    private Text abilityTitle;

    /**
     * The ability description
     */
    private Text abilityDescription;

    /**
     * Bubble sprite
     */
    private Sprite bubble;

    /**
     * The position of the bubble
     */
    private Vector2f position;

    /**
     * Movement animations
     */
    private Animation positionAnimationX;
    private Animation positionAnimationY;

    /**
     * Creates the hover title. Requires a selected ability to display its information
     */
    public SelectedAbilityHoverTitle() {

        abilityTitle = new Text();
        abilityTitle.setFont(Fonts.MEDIEVAL);
        abilityTitle.setColor(Color.WHITE);

        abilityDescription = new Text();
        abilityDescription.setFont(Fonts.PIXEL);
        abilityDescription.setColor(Color.WHITE);
        abilityDescription.setCharacterSize(18);

        bubble = new Sprite(TEXTURE_ABILITY_BUBBLE);
    }

    /**
     * Sets the position of the bubble (and animates it)
     *
     * @param position the new position
     */
    public void setPosition(Vector2f position) {
        if (this.position != null) {
            positionAnimationX = new Animation(this.position.x, position.x, 0.2f, false, Animations.EASE_IN_OUT);
            positionAnimationY = new Animation(this.position.y, position.y, 0.2f, false, Animations.EASE_IN_OUT);
        } else {
            updatePosition(position);
        }

        this.position = position;
    }

    /**
     * Internally updates the bubble's position, moving the objects
     *
     * @param pos the new position
     */
    private void updatePosition(Vector2f pos) {

        float w = -bubble.getLocalBounds().width * 0.5f;

        abilityTitle.setPosition(Vector2f.add(pos, new Vector2f(w + 10, -190)));
        abilityDescription.setPosition(Vector2f.add(pos, new Vector2f(w + 10, -150)));

        bubble.setPosition(Vector2f.add(pos, new Vector2f(w, -200)));
    }

    /**
     * Sets the selected ability slot that this bubble should show information about
     *
     * @param slot the selected slot
     */
    public void setSelectedSlot(AbilitySlot slot) {
        setPosition(slot.getPosition());

        if (slot.getAbility() == null) { //empty slot
            abilityTitle.setString("");
            abilityDescription.setString("");
            return;
        }

        Ability a = slot.getAbility().getAbility();

        abilityTitle.setString(a.getDisplayName());
        abilityDescription.setString(a.getDescription());
    }

    @Override
    public void update(float delta) {
        if (positionAnimationX != null && !positionAnimationX.isComplete()) {
            float x = positionAnimationX.update(delta);
            float y = positionAnimationY.update(delta);

            Vector2f pos = new Vector2f(x, y);

            updatePosition(pos);
        }
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        bubble.draw(renderTarget, renderStates);

        abilityTitle.draw(renderTarget, renderStates);
        abilityDescription.draw(renderTarget, renderStates);
    }
}
