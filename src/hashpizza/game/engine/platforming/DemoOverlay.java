package hashpizza.game.engine.platforming;

import hashpizza.game.engine.GameObject;
import hashpizza.game.engine.GameScreen;
import hashpizza.game.engine.GameSprite;
import hashpizza.game.engine.platforming.abilities.Abilities;
import hashpizza.game.engine.saving.GameSaveState;
import hashpizza.game.engine.util.*;
import org.jsfml.graphics.*;

/**
 * Overlay displayed during each level's demo, to display information about it
 */
public class DemoOverlay implements GameObject {

    private static final ConstTexture TEXTURE_DEMO_OVERLAY = Textures.getTexture("./res/misc/ui/level_demo_overlay.png");

    /**
     * Background gradient
     */
    private Sprite background;

    /**
     * The title of the level
     */
    private Text levelTitle;

    /**
     * The subtitle (either completed time or "Not completed")
     */
    private Text levelSubtitle;

    /**
     * Sprite for the level ability icon
     */
    private Sprite levelIcon;

    /**
     * Animations
     */
    private Animation opacityAnimation = new Animation(0f, 255f, 3f, false, Animations.EASE_OUT);
    private Animation titleMoveAnimation = new Animation(-7f, 0f, 1.25f, false, Animations.EASE_OUT);
    private Animation subtitleMoveAnimation = new Animation(-7f, 0f, 1.5f, false, Animations.EASE_OUT);

    /**
     * Creates a demo overlay for the specified level
     */
    public DemoOverlay(LevelSchema schema) {
        background = new Sprite(TEXTURE_DEMO_OVERLAY);

        levelTitle = new Text(schema.meta.title, Fonts.MEDIEVAL);
        levelTitle.setCharacterSize(48);
        levelTitle.setPosition(120, GridUtils.SCREEN_HEIGHT - 145);

        //get complete status
        long completionTime = GameSaveState.getSaveState().getLevelCompletionTimes().getOrDefault(schema.meta.filename, -1L);
        String completeMessage = "Not completed";

        if (completionTime > -1) {
            int mins = (int) (completionTime / 60000);
            int secs = (int) ((completionTime / 1000) % 60); //format mm:ss

            completeMessage = String.format("Best time: %d:%02d", mins, secs);
        }

        levelSubtitle = new Text(completeMessage, Fonts.PIXEL);
        levelSubtitle.setCharacterSize(32);
        levelSubtitle.setColor(new Color(200, 200, 200));
        levelSubtitle.setPosition(120, GridUtils.SCREEN_HEIGHT - 90);

        levelIcon = new Sprite(Textures.getTexture("./res/misc/ability_icons/" + schema.meta.filename + "_icon.png"));
        levelIcon.setPosition(40, GridUtils.SCREEN_HEIGHT - 125);
        levelIcon.setScale(2f, 2f);
    }

    @Override
    public void update(float delta) {
        float opacity = opacityAnimation.update(delta);
        Color colour = new Color(255, 255, 255, (int) opacity);

        background.setColor(colour);
        levelIcon.setColor(colour);
        levelTitle.setColor(colour);
        levelSubtitle.setColor(new Color(200, 200, 200, (int) opacity));

        levelTitle.setOrigin(0, titleMoveAnimation.update(delta));
        levelSubtitle.setOrigin(0, subtitleMoveAnimation.update(delta));
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        background.draw(renderTarget, renderStates);
        levelIcon.draw(renderTarget, renderStates);
        levelTitle.draw(renderTarget, renderStates);
        levelSubtitle.draw(renderTarget, renderStates);
    }
}
