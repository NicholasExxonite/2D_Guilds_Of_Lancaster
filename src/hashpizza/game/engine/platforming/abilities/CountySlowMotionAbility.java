package hashpizza.game.engine.platforming.abilities;

import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.platforming.Player;
import hashpizza.game.engine.util.DeltaTimer;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;

/**
 * County ability: Slows down the speed of all enemies in the world for 3s
 */
public class CountySlowMotionAbility extends Ability {

    public CountySlowMotionAbility() {
        super("Calm of County", "Slows down the speed of everything,\nother than yourself in the world", Textures.getTexture("./res/misc/ability_icons/county_icon.png"), "county", new Color(255,183,0));
    }

    @Override
    public void onActivate(LevelScreen level, Player player) {
        level.setSlowMotionTimer(new DeltaTimer(3f));
        setCooldownTimer(new DeltaTimer(5));
    }
}
