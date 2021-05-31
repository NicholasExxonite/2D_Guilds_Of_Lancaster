package hashpizza.game.engine.platforming.abilities;

import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.platforming.Player;
import hashpizza.game.engine.util.DeltaTimer;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;

/**
 * Lonsdale ability: stuns all enemies on a stage
 */
public class LonsdaleRoarAbility extends Ability {

    public LonsdaleRoarAbility() {
        super("Lonsdale Roar", "Gives the player the roar of a lion,\nfreezing enemies with fear", Textures.getTexture("./res/misc/ability_icons/lonsdale_icon.png"), "lonsdale",new Color(0,187,255));
    }

    @Override
    public void onActivate(LevelScreen level, Player player) {
        player.roar();
        setCooldownTimer(new DeltaTimer(20f));
    }
}
