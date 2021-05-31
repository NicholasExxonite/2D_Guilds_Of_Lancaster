package hashpizza.game.engine.ui.screens;

import hashpizza.game.engine.GameScreen;
import hashpizza.game.engine.GameSprite;
import hashpizza.game.engine.GameWindow;
import hashpizza.game.engine.platforming.LevelSchema;
import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.saving.GameSaveState;
import hashpizza.game.engine.ui.ButtonController;
import hashpizza.game.engine.ui.Text;
import hashpizza.game.engine.ui.UIButton;
import hashpizza.game.engine.ui.screens.levelselection.LevelSelectScreen;
import hashpizza.game.engine.ui.screens.main.MainMenuScreen;
import hashpizza.game.engine.util.*;
import org.jsfml.audio.Music;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

/**
 * Pause screen, to be displayed when pressing escape on pausable screens
 */
public class PauseScreen extends GameScreen {

    /**
     * Background music for the pause screen
     */
    private static final Music MUSIC_PAUSE_SCREEN = Sounds.getMusic("res/audio/music/pause.wav");

    /**
     * The previous screen to be resumed when exising the pause screen
     */
    private final GameScreen previousScreen;

    /**
     * The image of the previous screen used for the background
     */
    private GameSprite background;

    /**
     * Pause screen animations
     */
    private Animation pauseColourAnimation = new Animation(255, 128, 5, false, Animations.EASE_OUT);
    private Animation pauseScaleAnimation = new Animation(1.0f, 0.98f, 0.8f, false, Animations.EASE_OUT);

    /**
     * If the screen is being unpaused
     */
    private boolean unpausing;

    /**
     * The original scale of the background texture for the zoom out animation
     */
    private Vector2f originalScale;

    /**
     * Creates the pause screen
     *
     * @param window         the window parent
     * @param previousScreen the previous screen that was displayed before being paused
     * @param renderWindow   the render window, used for capturing the background image
     */
    public PauseScreen(GameWindow window, GameScreen previousScreen, RenderWindow renderWindow) {
        super(window);
        this.previousScreen = previousScreen;

        setMusic(MUSIC_PAUSE_SCREEN);

        if (previousScreen instanceof LevelScreen) {
            setBackgroundColor(((LevelScreen) previousScreen).getSchema().meta.foreground);
        } else {
            setBackgroundColor(Color.BLACK);
        }

        try {
            Texture t = new Texture();
            t.create(renderWindow.getSize().x, renderWindow.getSize().y);
            t.update(renderWindow); //creating a capture of the render window to use as our background

            background = new GameSprite(this, t) {
                @Override
                public void update(float delta) {
                    super.update(delta);

                    if (!pauseColourAnimation.isComplete()) { //changes opacity according to the colour animation
                        setColor(new Color(255, 255, 255, (int) pauseColourAnimation.update(delta)));
                    }

                    if (!pauseScaleAnimation.isComplete()) { //scale animations...
                        float scale = pauseScaleAnimation.update(delta);
                        setScale(originalScale.x * scale, originalScale.y * scale);
                    }

                    if (unpausing && pauseScaleAnimation.isComplete()) {
                        window.setActiveScreen(previousScreen); //if we've finished the unpause animation...
                    }
                }
            };

            background.setOrigin(background.getTexture().getSize().x * 0.5f, background.getTexture().getSize().y * 0.5f);
            background.setSize(GridUtils.SCREEN_WIDTH, GridUtils.SCREEN_HEIGHT);

            originalScale = background.getScale();
            background.setPosition(GridUtils.SCREEN_WIDTH * 0.5f, GridUtils.SCREEN_HEIGHT * 0.5f);

            addObject(background);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Pause screen titles:
        Text pauseTitle = new Text("Game Paused", Fonts.MEDIEVAL) {

            Animation pausePulseAnim = new Animation(0.98f, 1f, 1.25f, true, Animations.EASE_IN_OUT);

            @Override
            public void update(float delta) {
                float scale = pausePulseAnim.update(delta);

                setScale(scale, scale);

                super.update(delta);
            }
        };

        pauseTitle.setCharacterSize(48);
        pauseTitle.setOrigin(new Vector2f(pauseTitle.getLocalBounds().width * 0.5f, pauseTitle.getLocalBounds().height * 0.5f));
        pauseTitle.setPosition(960, 540);

        if (previousScreen instanceof LevelScreen) {
            LevelScreen ls = (LevelScreen) previousScreen;

            Text levelTitle = new Text("Level: " + ls.getSchema().meta.title, Fonts.MEDIEVAL);
            levelTitle.setPosition(960, 640);
            levelTitle.setOrigin(new Vector2f(levelTitle.getLocalBounds().width * 0.5f, levelTitle.getLocalBounds().height * 0.5f));

            addObject(levelTitle);
        }

        addObject(pauseTitle);

        //Pause screen buttons:
        ButtonController buttonController = new ButtonController();

        int buttonY = 550;

        boolean onLevelSelect = previousScreen instanceof LevelSelectScreen;

        UIButton backToMapButton = new UIButton(onLevelSelect ? "Exit to Main Menu" : "Exit to Level Selection", () -> {
            LevelSchema prevLevel = null;
            if (previousScreen instanceof LevelScreen) prevLevel = ((LevelScreen) previousScreen).getSchema();

            window.setActiveScreen(onLevelSelect ? new MainMenuScreen(window) : new LevelSelectScreen(window, GameSaveState.getSaveState().getLevelCompletionTimes().isEmpty() ? "cartmel" : null, prevLevel));
        }, new Vector2f(400, buttonY), new Vector2f(300, 100));

        buttonController.addButton(backToMapButton);

        buttonY += 110;

        UIButton exitGameButton = new UIButton("Exit Game", () -> {
            GameSaveState.save();
            System.exit(0);
        }, new Vector2f(400, buttonY), new Vector2f(300, 100));

        buttonController.addButton(exitGameButton);

        addObject(buttonController);
    }

    /**
     * Starts playing the unpause animation and then resumes the previous screen
     */
    public void unpause() {
        if (unpausing) return;

        if (background == null) { //is the case when the background texture failed to load, e.g. if the window is set to 0x0 for some reason
            getWindow().setActiveScreen(previousScreen);
        }

        unpausing = true;
        pauseColourAnimation = new Animation(pauseColourAnimation.update(0), 255, 5, false, Animations.EASE_OUT);
        pauseScaleAnimation = pauseScaleAnimation.inverse();
    }
}
