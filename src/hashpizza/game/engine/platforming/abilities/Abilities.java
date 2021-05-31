package hashpizza.game.engine.platforming.abilities;

import hashpizza.game.engine.saving.GameSaveState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to hold all of the abilities
 */
public final class Abilities {

    /**
     * All of the game's abilities
     */
    private static List<Ability> abilities = new ArrayList<>();

    static {
        abilities.add(new BowlandBowAbility());
        abilities.add(new CartmelGlideAbility());
        abilities.add(new FurnessJumpAbility());
        abilities.add(new CountySlowMotionAbility());
        abilities.add(new FyldeDashAbility());
        abilities.add(new GrizedaleShieldAbility());
        abilities.add(new LonsdaleRoarAbility());
        abilities.add(new PendlePotionAbility());
    }

    /**
     * Gets the ability for the specified internal name
     *
     * @param internalName the internal name of the ability
     * @return the ability for the specified internal name
     */
    public static Ability ofName(String internalName) {
        return abilities.stream().filter(b -> b.getInternalName().equals(internalName)).findAny().orElse(null);
    }

    /**
     * Gets all of the player's unlocked abilities
     * @return the player's unlocked abilities
     */
    public static List<Ability> getUnlocked() {

        GameSaveState save = GameSaveState.getSaveState();

        return abilities.stream().filter(a -> save.getLevelCompletionTimes().containsKey(a.getInternalName())).collect(Collectors.toList());
    }
}
