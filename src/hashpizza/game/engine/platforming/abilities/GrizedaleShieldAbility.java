package hashpizza.game.engine.platforming.abilities;

import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.platforming.Player;
import hashpizza.game.engine.util.DeltaTimer;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;

/**
 * Grizedale ability: gives the player a shield
 */
public class GrizedaleShieldAbility extends Ability {

    public GrizedaleShieldAbility() {
        super("Grizedale Concoction", "Drink the hardy beverage of the\nGrizedale guild to give you an extra\npick-me-up when damaged", Textures.getTexture("./res/misc/ability_icons/grizedale_icon.png"), "grizedale", new Color(0,106,255)
        );
    }

    @Override
    public void onActivate(LevelScreen level, Player player) {
        player.useShield();

        setCooldownTimer(new DeltaTimer(10f));
    }
}
