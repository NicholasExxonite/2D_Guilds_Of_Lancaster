package hashpizza.game.engine.platforming.abilities;

import hashpizza.game.engine.GameObject;
import hashpizza.game.engine.KeyHandler;
import hashpizza.game.engine.platforming.AbilityParticles;
import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.platforming.abilities.selector.AbilitySlot;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.window.Keyboard;

import java.util.List;

/**
 * Handles processing the ability keybinds and activating the associated abilities, if there is one in the selected
 * slot
 */
public class AbilityManager implements GameObject, KeyHandler {

    /**
     * The level these abilities apply to
     */
    private final LevelScreen level;

    /**
     * The abilities that the user has selected
     */
    private final List<AbilitySlot> selectedAbilities;

    /**
     * Creates the ability manager for the specified level
     *
     * @param level             the level the player is playing
     * @param selectedAbilities a list of the player's selected abilities
     */
    public AbilityManager(LevelScreen level, List<AbilitySlot> selectedAbilities) {

        this.level = level;
        this.selectedAbilities = selectedAbilities;
    }

    @Override
    public void update(float delta) {
        //update cooldown timers for all slots
        for (AbilitySlot as : selectedAbilities) {
            if (as.getAbility() != null) {
                Ability a = as.getAbility().getAbility();

                if (a != null && a.getCooldownTimer() != null) {
                    a.getCooldownTimer().update(delta);
                }
            }
        }
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        //dummy
    }

    private void setActive(int slotId, boolean active) {
        AbilitySlot slot = selectedAbilities.get(slotId);
        if (slot != null && slot.getAbility() != null && (slot.getAbility().getAbility().getCooldownTimer() == null || slot.getAbility().getAbility().getCooldownTimer().isComplete())) {
            slot.getAbility().setActivated(active);
            Ability ability = slot.getAbility().getAbility();

            if (active) {
                if (ability.isActive()) return;

                ability.setActive(true);
                ability.onActivate(level, level.getPlayer());
                level.addObject(new AbilityParticles(level, ability.getColor()));
            } else {
                ability.setActive(false);
                ability.onKeyRelease(level, level.getPlayer());
            }
        }
    }

    @Override
    public void onKeyPress(Keyboard.Key key) {
        if (key == Keyboard.Key.NUM1 || key == Keyboard.Key.J) {
            setActive(0, true);
        } else if (key == Keyboard.Key.NUM2 || key == Keyboard.Key.K) {
            setActive(1, true);
        } else if (key == Keyboard.Key.NUM3 || key == Keyboard.Key.L) {
            setActive(2, true);
        }
    }

    @Override
    public void onKeyRelease(Keyboard.Key key) {
        if (key == Keyboard.Key.NUM1 || key == Keyboard.Key.J) {
            setActive(0, false);
        } else if (key == Keyboard.Key.NUM2 || key == Keyboard.Key.K) {
            setActive(1, false);
        } else if (key == Keyboard.Key.NUM3 || key == Keyboard.Key.L) {
            setActive(2, false);
        }
    }
}
