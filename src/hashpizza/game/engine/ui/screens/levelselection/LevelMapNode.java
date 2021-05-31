package hashpizza.game.engine.ui.screens.levelselection;

import hashpizza.game.engine.platforming.LevelSchema;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.system.Vector2f;

/**
 * Creates a level map node which contain's a level's position, its icon, and the level's data itself
 */
public class LevelMapNode {

    /**
     * The position of this level on the world-map
     */
    private final Vector2f position;

    /**
     * The level data
     */
    private final LevelSchema level;

    /**
     * The level's ability icon
     */
    private final ConstTexture abilityIcon;

    /**
     * Creates a level map node for the specified level
     *
     * @param level       the level
     * @param abilityIcon the level's ability icon
     */
    public LevelMapNode(LevelSchema level, ConstTexture abilityIcon) {

        this.level = level;
        this.abilityIcon = abilityIcon;

        position = new Vector2f(level.meta.mapX, level.meta.mapY);
    }

    /**
     * @return the position of this node on the map
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * @return the level's schema/metadata
     */
    public LevelSchema getLevel() {
        return level;
    }

    /**
     * @return the icon to display for this level
     */
    public ConstTexture getAbilityIcon() {
        return abilityIcon;
    }
}
