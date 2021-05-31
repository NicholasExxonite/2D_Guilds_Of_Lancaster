package hashpizza.game.engine.platforming.abilities;

import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.platforming.Player;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;

/**
 * Cartmel ability: allows the player to guide
 */
public class CartmelGlideAbility extends Ability {

    public CartmelGlideAbility() {
        super("Wings of Cartmel", "When activated slows your fall\nspeed, allowing you to glide through\nthe air", Textures.getTexture("./res/misc/ability_icons/cartmel_icon.png"), "cartmel", new Color(255,31,131));
    }

    @Override
    public void onActivate(LevelScreen level, Player player) {
        player.setGliding(true);
    }

    @Override
    public void onKeyRelease(LevelScreen level, Player player) {
        player.setGliding(false);
    }
}
