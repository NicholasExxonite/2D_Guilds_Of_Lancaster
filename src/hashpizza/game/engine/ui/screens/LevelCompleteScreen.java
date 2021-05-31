package hashpizza.game.engine.ui.screens;

import hashpizza.game.engine.GameScreen;
import hashpizza.game.engine.GameSprite;
import hashpizza.game.engine.GameWindow;
import hashpizza.game.engine.platforming.LevelSchema;
import hashpizza.game.engine.platforming.abilities.Abilities;
import hashpizza.game.engine.saving.GameSaveState;
import hashpizza.game.engine.ui.ButtonController;
import hashpizza.game.engine.ui.Text;
import hashpizza.game.engine.ui.UIButton;
import hashpizza.game.engine.ui.screens.levelselection.LevelSelectScreen;
import hashpizza.game.engine.util.*;
import org.jsfml.audio.Music;
import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import java.util.Map;

/**
 * Screen to be displayed when a level has been completed
 */
public class LevelCompleteScreen extends GameScreen {

    /**
     * The background jingle for completing the level
     */
    private static final Music MUSIC_COMPLETE = Sounds.getMusic("./res/audio/music/level_completion.wav", false);

    /**
     * Displays the level complete screen for the specified level
     *
     * @param window         the window parent
     * @param schema         the level that was completed
     * @param completionTime how long it took the player to complete the level
     */
    public LevelCompleteScreen(GameWindow window, LevelSchema schema, long completionTime) {
        super(window);

        setMusic(MUSIC_COMPLETE);

        //if the level has an image, i.e an ability attatched to it
        if (Abilities.ofName(schema.meta.title.toLowerCase()) == null) {
            setBackgroundColor(new Color(55, 71, 79));
        } else {
            setBackgroundColor(schema.meta.foreground);
            GameSprite levelImage = new GameSprite(this, Abilities.ofName(schema.meta.filename).getImage());
            levelImage.setSize(100, 100);
            levelImage.setOrigin(levelImage.getLocalBounds().width * 0.5f, levelImage.getLocalBounds().height * 0.5f);
            levelImage.setPosition(GridUtils.SCREEN_WIDTH * 0.5f, 200);
            addObject(levelImage);
        }

        Text levelCompleteText = new Text("You have completed the " + schema.meta.title + " level!", Fonts.MEDIEVAL) {

            private Animation pulseScaleAnimation = new Animation(1f, 1.05f, 5f, true, Animations.EASE_IN_OUT);

            @Override
            public void update(float delta) {
                super.update(delta);

                float scale = pulseScaleAnimation.update(delta);
                setScale(scale, scale);
            }
        };
        levelCompleteText.setCharacterSize(90);
        levelCompleteText.setOrigin(levelCompleteText.getLocalBounds().width * 0.5f, 0);
        levelCompleteText.setPosition(GridUtils.SCREEN_WIDTH * 0.5f, 380);
        addObject(levelCompleteText);

        int mins = (int) (completionTime / 60000);
        int secs = (int) ((completionTime / 1000) % 60);

        Text completionTimeText = new Text(String.format("\n\nCompletion time: %d:%02d", mins, secs), Fonts.PIXEL);
        completionTimeText.setCharacterSize(50);
        completionTimeText.setPosition(GridUtils.SCREEN_WIDTH * 0.5f - completionTimeText.getLocalBounds().width / 2, 420);

        addObject(completionTimeText);

        addObject(new ButtonController().
                addButton(new UIButton("Level Select", () -> {
                    getWindow().setActiveScreen(new LevelSelectScreen(getWindow(), null, schema));
                }, new Vector2f(GridUtils.SCREEN_WIDTH * 0.5f, 700), new Vector2f(220, 60))));


        //save our time to the game save if we haven't completed the level before or if we've beaten our best time
        Map<String, Long> completionTimes = GameSaveState.getSaveState().getLevelCompletionTimes();
        if (completionTimes.getOrDefault(schema.meta.title.toLowerCase(), Long.MAX_VALUE) > completionTime) {
            completionTimes.put(schema.meta.title.toLowerCase(), completionTime);
            GameSaveState.save();
        }
    }
}
