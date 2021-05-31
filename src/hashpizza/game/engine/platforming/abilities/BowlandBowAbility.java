package hashpizza.game.engine.platforming.abilities;

import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.platforming.Player;
import hashpizza.game.engine.util.DeltaTimer;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;

/**
 * Bowland ability: gives the player a bow and arrow
 */
public class BowlandBowAbility extends Ability {

    public BowlandBowAbility() {
        super("Bow of Bowland", "Fires a barbed projectile that deals\ndamage to anything in its path ", Textures.getTexture("./res/misc/ability_icons/bowland_icon.png"), "bowland", Color.RED);
    }

    @Override
    public void onActivate(LevelScreen level, Player player) {
        player.startDrawingBow();
    }

    @Override
    public void onKeyRelease(LevelScreen level, Player player) {
        player.stopDrawingBow();

        setCooldownTimer(new DeltaTimer(3f));
    }
}
