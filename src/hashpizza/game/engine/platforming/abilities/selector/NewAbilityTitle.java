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
 * Bubble for displaying information about a newly unlocked ability
 */
public class NewAbilityTitle implements GameObject {

    /**
     * Bubble texture
     */
    private static final ConstTexture TEXTURE_ABILITY_BUBBLE = Textures.getTexture("./res/misc/new-ability.png");

    /**
     * The bubble does a slight animation from left to right
     */
    private Animation slideAnimation = new Animation(0f, 5f, 2.5f, true, Animations.EASE_IN_OUT);

    /**
     * The ability name
     */
    private Text abilityTitle;

    /**
     * The ability description
     */
    private Text abilityDescription;

    /**
     * The bubble background sprite
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
     * Creates a bubble for 'new' abilities, when the user has just unlocked them
     */
    public NewAbilityTitle() {

        abilityTitle = new Text();
        abilityTitle.setFont(Fonts.MEDIEVAL);
        abilityTitle.setColor(Color.WHITE);
        abilityTitle.setCharacterSize(22);

        abilityDescription = new Text();
        abilityDescription.setFont(Fonts.PIXEL);
        abilityDescription.setColor(Color.WHITE);
        abilityDescription.setCharacterSize(15);

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

        abilityTitle.setPosition(Vector2f.add(pos, new Vector2f(w - 60, -25)));
        abilityDescription.setPosition(Vector2f.add(pos, new Vector2f(w - 60, 5)));

        bubble.setPosition(Vector2f.add(pos, new Vector2f(w - 75, -(bubble.getLocalBounds().height * 0.5f))));
    }

    /**
     * Sets the selected ability that this bubble should show information about
     */
    public void setAbility(Ability ability) {
        if (ability == null) return;

        abilityTitle.setString(ability.getDisplayName());
        abilityDescription.setString(ability.getDescription());
    }

    @Override
    public void update(float delta) {
        float offset = slideAnimation.update(delta);

        bubble.setOrigin(offset, 0);

        abilityTitle.setOrigin(offset, 0);
        abilityDescription.setOrigin(offset, 0);
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        bubble.draw(renderTarget, renderStates);

        abilityTitle.draw(renderTarget, renderStates);
        abilityDescription.draw(renderTarget, renderStates);
    }
}
