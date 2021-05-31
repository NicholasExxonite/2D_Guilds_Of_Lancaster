package hashpizza.game.engine.platforming.abilities;

import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.platforming.Player;
import hashpizza.game.engine.util.DeltaTimer;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;

/**
 * Pendle ability: allows the player to throw a poison bottle
 */
public class PendlePotionAbility extends Ability {

    public PendlePotionAbility() {
        super("Pendle Potion", "Vile poisonous projectile that\nexplodes on impact, disabling anything\nit comes into contact with", Textures.getTexture("./res/misc/ability_icons/pendle_icon.png"), "pendle", new Color(149,255,0));
    }

    @Override
    public void onActivate(LevelScreen level, Player player) {
        player.throwPoison();
    }

    public void onKeyRelease(LevelScreen level, Player player) {
        setCooldownTimer(new DeltaTimer(3f));
    }

}
