package hashpizza.game.engine.platforming.abilities;

import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.platforming.Player;
import hashpizza.game.engine.util.DeltaTimer;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.Color;

/**
 * Furness ability: allows the player to double jump
 */
public class FurnessJumpAbility extends Ability {

    public FurnessJumpAbility() {
        super("Furness Footwear", "Gives you the ability to jump in mid\nair, allowing you to reach greater\nheights", Textures.getTexture("./res/misc/ability_icons/furness_icon.png"), "furness", new Color(221,0,255));
    }

    @Override
    public void onActivate(LevelScreen level, Player player) {
        player.setDoubleJumping(true);
        System.out.println("called!!");
    }

    @Override
    public void onKeyRelease(LevelScreen level, Player player) {
        if (!player.isDoubleJumping()) return;

        player.setDoubleJumping(false);

        setCooldownTimer(new DeltaTimer(3));
    }
}
