package hashpizza.game.engine.platforming.abilities;

import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.platforming.Player;
import hashpizza.game.engine.util.DeltaTimer;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConstTexture;

/**
 * Represents a game ability
 */
public abstract class Ability {

    /**
     * The ability's display name
     */
    private final String displayName;

    /**
     * A description of the ability
     */
    private final String description;

    /**
     * The ability's icon
     */
    private final ConstTexture image;

    /**
     * The internal name for the ability, should correspond to the level file name
     */
    private final String internalName;

    /**
     * The colour of the ability's effect
     */
    private Color color;

    /**
     * Whether the ability is currently in progress
     */
    private boolean active;

    /**
     * Cooldown between using the ability multiple times
     */
    private DeltaTimer cooldownTimer;

    /**
     * Creates an ability for the specified parameters
     *
     * @param displayName  the name
     * @param description  the description
     * @param image        the ability's icon
     * @param internalName the ability's internal name
     * @param color        the colour for ability
     */
    public Ability(String displayName, String description, ConstTexture image, String internalName, Color color) {

        this.displayName = displayName;
        this.description = description;
        this.image = image;
        this.internalName = internalName;
        this.color = color;
    }

    /**
     * @return the ability's display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the ability's image
     */
    public ConstTexture getImage() {
        return image;
    }

    /**
     * @return the internal ability name
     */
    public String getInternalName() {
        return internalName;
    }

    /**
     * @return whether the ability is currently activated
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether or not to activate the ability
     *
     * @param active whether it should be active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Handles activating the ability
     *
     * @param level the level active
     * @param player the level's player
     */
    public abstract void onActivate(LevelScreen level, Player player);

    /**
     * Handles releasing the ability slot's key
     *
     * @param level the level active
     * @param player the level's player
     */
    public void onKeyRelease(LevelScreen level, Player player) {

    }

    /**
     * @return the description of the game
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the cooldown until the ability can be used next
     */
    public DeltaTimer getCooldownTimer() {
        return cooldownTimer;
    }


    /**
     * @return the ability's colour
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the cooldown for the next ability usage
     * @param cooldownTimer the cooldown
     */
    public void setCooldownTimer(DeltaTimer cooldownTimer) {
        this.cooldownTimer = cooldownTimer;
    }
}
