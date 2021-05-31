package hashpizza.game.engine.platforming.abilities;

import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.platforming.Player;
import hashpizza.game.engine.util.DeltaTimer;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;

/**
 * Fylde ability: allows the player to dash
 */
public class FyldeDashAbility extends Ability {

    public FyldeDashAbility() {
        super("Fylde Windwalk", "Gives you the speed of the wind,\nallowing you to dash small distances", Textures.getTexture("./res/misc/ability_icons/fylde_icon.png"), "fylde", new Color(255,145,0));
    }

    @Override
    public void onActivate(LevelScreen level, Player player) {
        player.dash();
    }

    @Override
    public void onKeyRelease(LevelScreen level, Player player) {
        setCooldownTimer(new DeltaTimer(3f));
    }
}