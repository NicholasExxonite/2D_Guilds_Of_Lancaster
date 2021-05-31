package hashpizza.game.engine.platforming.abilities.selector;

import hashpizza.game.engine.GameObject;
import hashpizza.game.engine.KeyHandler;
import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.platforming.abilities.Abilities;
import hashpizza.game.engine.platforming.abilities.Ability;
import hashpizza.game.engine.platforming.abilities.AbilityManager;
import hashpizza.game.engine.util.*;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Ability selector which handles selecting abilities at the start of the game and holds all of the slots used
 */
public class AbilitySelector implements GameObject, KeyHandler {

    /**
     * Textures for the ability slot backgrounds
     */
    private static final ConstTexture TEXTURE_ABILITY_SLOT = Textures.getTexture("./res/misc/ability_icons/ability_slot.png");
    private static final ConstTexture TEXTURE_ABILITY_SLOT_LARGE = Textures.getTexture("./res/misc/ability_icons/ability_slot_large.png");
    private static final ConstTexture TEXTURE_TRANSPARENT = Textures.getTexture("./res/misc/transparent.png");
    private static final ConstTexture TEXTURE_KEY_PROMPT = Textures.getTexture("./res/misc/ui/ability-key-prompt.png");
    private static final ConstTexture TEXTURE_VIGNETTE = Textures.getTexture("./res/misc/vignette.png");

    /**
     * The active level screen
     */
    private final LevelScreen screen;

    /**
     * Ability for if we are forcing the player to use a specified ability
     */
    private Ability forcedLevelAbility;

    /**
     * The image displaying the prompt of the 1, 2, 3 keys
     */
    private Sprite keyPromptImage;

    /**
     * Darkens the background when the ability selector is active
     */
    private Sprite backgroundDarken;

    /**
     * Bubble to display above currently hovered ability
     */
    private SelectedAbilityHoverTitle hoverTitle;

    /**
     * The ability title for displaying when new abilities are unlocked
     */
    private NewAbilityTitle newAbilityTitle;

    /**
     * The title of the ability
     */
    private Text selectorTitle;

    /**
     * The 'press space to continue' prompt
     */
    private Text selectorContinue;

    /**
     * The currently selected slot
     */
    private int selectedSlot = -1;

    /**
     * List of all of the slots
     */
    private List<AbilitySlot> slots = new ArrayList<>();

    /**
     * Animations
     */
    private Animation textScaleAnimation = new Animation(1.0f, 1.1f, 2f, true, Animations.EASE_IN_OUT);
    private Animation keyPromptAnimation = new Animation(0f, 5f, 2f, true, Animations.EASE_IN_OUT);
    private Animation keyPromptFadeAnimation = new Animation(255f, 0f, 0.6f, false, Animations.EASE_OUT);
    private Animation backgroundDarkenFadeAnimation = new Animation(255f, 0f, 0.6f, false, Animations.EASE_OUT);

    /**
     * Timer for removing the 1, 2, 3 key-prompt
     */
    private float keyPromptLife;

    /**
     * Whether the ability selector is currently shown
     */
    public boolean active = true;

    /**
     * Creates the ability selector
     *
     * @param screen             the screen parent
     * @param forcedLevelAbility an ability that the player is forced to use
     */
    public AbilitySelector(LevelScreen screen, Ability forcedLevelAbility) {
        this.screen = screen;

        this.forcedLevelAbility = forcedLevelAbility;

        selectorTitle = new Text();
        selectorTitle.setFont(Fonts.MEDIEVAL);
        selectorTitle.setString("Select your abilities");
        selectorTitle.setCharacterSize(60);

        selectorTitle.setOrigin(selectorTitle.getLocalBounds().width * 0.5f, selectorTitle.getLocalBounds().height * 0.5f);
        selectorTitle.setPosition(GridUtils.SCREEN_WIDTH * 0.5f, 100);

        selectorContinue = new Text();
        selectorContinue.setFont(Fonts.PIXEL);
        selectorContinue.setString("Press space to start");
        selectorContinue.setCharacterSize(38);

        selectorContinue.setOrigin(selectorContinue.getLocalBounds().width * 0.5f, 0);
        selectorContinue.setPosition(GridUtils.SCREEN_WIDTH * 0.5f, 820);

        List<Ability> availableAbilities = Abilities.getUnlocked(); //only show our unlocked abilities

        int x = 0;

        //the unlocked ability slots
        float allAbilitySlotPos = GridUtils.SCREEN_WIDTH * 0.5f;
        float allAbilitySlotPadding = 30f;
        float allAbilitySlotSize = TEXTURE_ABILITY_SLOT.getSize().x;

        allAbilitySlotPos -= (allAbilitySlotSize + allAbilitySlotPadding) * availableAbilities.size() * 0.5f;
        allAbilitySlotPos -= allAbilitySlotSize * 0.5f;
        allAbilitySlotPos -= allAbilitySlotPadding * 0.5f;

        for (Ability a : availableAbilities) {
            a.setCooldownTimer(null);

            x++;

            allAbilitySlotPos += allAbilitySlotSize + allAbilitySlotPadding;

            AbilitySlot as = new AbilitySlot(new Vector2f(allAbilitySlotPos, 470), new Vector2f(2f, 2f), null, TEXTURE_ABILITY_SLOT);
            AbilitySlotSprite abs = new AbilitySlotSprite(a, as);

            as.setAbility(abs);

            if (x == 1) as.setSelected(true);

            slots.add(as);

            selectedSlot = 0;
        }

        //the 3 slots to allow the user to select their 3 active abilities
        float abilitySlotPos = GridUtils.SCREEN_WIDTH * 0.5f;
        float abilitySlotPadding = 60f;
        float abilitySlotY = selectedSlot == -1 ? 500 : 700;
        float abilitySlotSize = TEXTURE_ABILITY_SLOT_LARGE.getSize().x;

        abilitySlotPos -= (abilitySlotPadding + abilitySlotSize) * 2;

        for (int i = 0; i < 3; i++) {
            abilitySlotPos += abilitySlotSize + abilitySlotPadding;

            AbilitySlot as = new AbilitySlot(new Vector2f(abilitySlotPos, abilitySlotY), new Vector2f(4f, 4f), null, TEXTURE_ABILITY_SLOT_LARGE);
            if (i == 0 && forcedLevelAbility != null) { //display a sprite if we're forced to use an ability
                as.setAbility(new AbilitySlotSprite(forcedLevelAbility, as));
                newAbilityTitle = new NewAbilityTitle();
                newAbilityTitle.setPosition(new Vector2f(abilitySlotPos - abilitySlotSize, abilitySlotY));
                newAbilityTitle.setAbility(forcedLevelAbility);
            }

            slots.add(as);
        }

        hoverTitle = new SelectedAbilityHoverTitle();
        if (selectedSlot != -1) {
            hoverTitle.setSelectedSlot(slots.get(selectedSlot));
        } else {
            hoverTitle.setPosition(new Vector2f(-500, -500));
        }

        keyPromptImage = new Sprite(TEXTURE_KEY_PROMPT);
        backgroundDarken = new Sprite(TEXTURE_VIGNETTE); //darken background whilst the ability selector is active
    }

    /**
     * @return all of the ability slots
     */
    public List<AbilitySlot> getSlots() {
        return slots;
    }

    @Override
    public void update(float delta) {
        //Updating all animations
        if (active) {
            float scale = textScaleAnimation.update(delta);

            selectorContinue.setOrigin(selectorContinue.getLocalBounds().width * 0.5f, -scale * 100);

            hoverTitle.update(delta);

            if (newAbilityTitle != null) newAbilityTitle.update(delta);
        } else {
            if (!keyPromptFadeAnimation.isComplete()) {

                keyPromptImage.setOrigin(keyPromptAnimation.update(delta), 0);
                keyPromptLife += delta;

                if (keyPromptLife >= 10f) {
                    float opacity = keyPromptFadeAnimation.update(delta);
                    keyPromptImage.setColor(new Color(255, 255, 255, (int) opacity));
                }
            }

            if (!backgroundDarkenFadeAnimation.isComplete()) {
                float opacity = backgroundDarkenFadeAnimation.update(delta);
                backgroundDarken.setColor(new Color(255, 255, 255, (int) opacity));
            }
        }

        for (AbilitySlot slot : slots) {
            slot.update(delta);
        }
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {

        //draw all slots if the ability selector is shown
        if (active) {
            for (AbilitySlot slot : slots) {
                slot.draw(renderTarget, renderStates);
            }
        }

        for (AbilitySlot slot : slots) {
            if (slot.getAbility() != null) {
                slot.getAbility().draw(renderTarget, renderStates);
            }
        }

        //draw all slots if the ability selector is not shown, this it to allow the cooldown
        //text to render correctly in the right order
        if (!active) {
            for (AbilitySlot slot : slots) {
                slot.draw(renderTarget, renderStates);
            }
        }

        if (!backgroundDarkenFadeAnimation.isComplete()) {
            backgroundDarken.draw(renderTarget, renderStates);
        }

        if (active) {
            selectorTitle.draw(renderTarget, renderStates);
            selectorContinue.draw(renderTarget, renderStates);
            hoverTitle.draw(renderTarget, renderStates);

            if (newAbilityTitle != null) newAbilityTitle.draw(renderTarget, renderStates);
        } else if (!keyPromptFadeAnimation.isComplete()) {
            keyPromptImage.draw(renderTarget, renderStates);
        }
    }

    @Override
    public void onKeyRelease(Keyboard.Key key) {
        if (active) {
            if (key == Keyboard.Key.SPACE) {

                AbilitySlot slot1 = slots.get(slots.size() - 3);
                AbilitySlot slot2 = slots.get(slots.size() - 2);
                AbilitySlot slot3 = slots.get(slots.size() - 1);

                slots.clear();

                //create GUI slots in top left
                AbilitySlot smallSlot1 = new AbilitySlot(new Vector2f(40, 40), new Vector2f(2f, 2f), null, TEXTURE_TRANSPARENT);
                AbilitySlot smallSlot2 = new AbilitySlot(new Vector2f(90, 40), new Vector2f(2f, 2f), null, TEXTURE_TRANSPARENT);
                AbilitySlot smallSlot3 = new AbilitySlot(new Vector2f(140, 40), new Vector2f(2f, 2f), null, TEXTURE_TRANSPARENT);

                slots.add(smallSlot1);
                slots.add(smallSlot2);
                slots.add(smallSlot3);

                smallSlot1.setAbility(slot1.getAbility());
                smallSlot2.setAbility(slot2.getAbility());
                smallSlot3.setAbility(slot3.getAbility());

                active = false;
                screen.completedAbilitySelection(); //tell the level screen that we've finished ability selection

                AbilitySlot lastSlot = null;
                if (smallSlot3.getAbility() != null) {
                    lastSlot = smallSlot3;
                } else if (smallSlot2.getAbility() != null) {
                    lastSlot = smallSlot2;
                } else if (smallSlot1.getAbility() != null) {
                    lastSlot = smallSlot1;
                }

                keyPromptImage.setPosition(lastSlot == null ? -300 : lastSlot.getPosition().x + 35, 10);
            }

            if (selectedSlot == -1) return; //we only have one forced ability - don't need to allow ability switching

            //swapping around abilities in 1, 2, 3 slots whilst the selector is active
            if ((key == Keyboard.Key.NUM1 || key == Keyboard.Key.J) && forcedLevelAbility == null) {
                swapAbilities(slots.size() - 3, selectedSlot);
            } else if (key == Keyboard.Key.NUM2 || key == Keyboard.Key.K) {
                swapAbilities(slots.size() - 2, selectedSlot);
            } else if (key == Keyboard.Key.NUM3 || key == Keyboard.Key.L) {
                swapAbilities(slots.size() - 1, selectedSlot);
            } else if (key == Keyboard.Key.RETURN || key == Keyboard.Key.S || key == Keyboard.Key.W) {

                for (int i = slots.size() - 3; i <= slots.size() - 1; i++) {
                    if (slots.get(i).getAbility() == null) {
                        swapAbilities(i, selectedSlot);
                        return;
                    }
                }

                //cycling between abilities
            } else if (key == Keyboard.Key.LEFT || key == Keyboard.Key.A) {
                slots.get(selectedSlot).setSelected(false);
                selectedSlot--;

                if (selectedSlot < 0) {
                    selectedSlot = slots.size() - 4;
                }

                slots.get(selectedSlot).setSelected(true);
                hoverTitle.setSelectedSlot(slots.get(selectedSlot));

            } else if (key == Keyboard.Key.RIGHT || key == Keyboard.Key.D) {
                slots.get(selectedSlot).setSelected(false);
                selectedSlot++;

                if (selectedSlot >= slots.size() - 3) {
                    selectedSlot = 0;
                }

                slots.get(selectedSlot).setSelected(true);
                hoverTitle.setSelectedSlot(slots.get(selectedSlot));
            }
        }
    }

    /**
     * Swaps the specified abilities in the specified slots around
     *
     * @param fromSlot the first slot
     * @param toSlot   the second slot
     */
    private void swapAbilities(int fromSlot, int toSlot) {
        AbilitySlot slot1 = slots.get(fromSlot);
        AbilitySlot slot2 = slots.get(toSlot);

        if (slot2.getAbility() != null) {
            slot1.setAbility(slot2.getAbility());
        } else {
            slot2.setAbility(slot1.getAbility());
        }

        hoverTitle.setSelectedSlot(slots.get(selectedSlot)); //to update the message displayed
    }

    @Override
    public void onKeyPress(Keyboard.Key key) {

    }
}
