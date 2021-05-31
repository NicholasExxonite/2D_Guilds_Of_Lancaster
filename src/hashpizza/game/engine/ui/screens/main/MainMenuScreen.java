package hashpizza.game.engine.ui.screens.main;

import hashpizza.game.engine.GameScreen;
import hashpizza.game.engine.GameSprite;
import hashpizza.game.engine.GameWindow;
import hashpizza.game.engine.saving.GameSaveState;
import hashpizza.game.engine.ui.ButtonController;
import hashpizza.game.engine.ui.UIButton;
import hashpizza.game.engine.ui.screens.CreditsScreen;
import hashpizza.game.engine.ui.screens.StoryIntroductionScreen;
import hashpizza.game.engine.ui.screens.levelselection.LevelSelectScreen;
import hashpizza.game.engine.util.GridUtils;
import hashpizza.game.engine.util.Sounds;
import hashpizza.game.engine.util.Textures;
import org.jsfml.audio.Music;
import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

/**
 * This class holds the objects that should be on the main menu screen.
 */
public class MainMenuScreen extends GameScreen {

    /**
     * Main menu background music
     */
    public static final Music MUSIC_MAIN_MENU = Sounds.getMusic("./res/audio/music/main_menu.wav");

    /**
     * Creates the main menu with the specified window parent
     *
     * @param window the window parent
     */
    public MainMenuScreen(GameWindow window) {
        super(window);

        setMusic(MUSIC_MAIN_MENU);

        setBackgroundColor(new Color(55, 71, 79));

        //Logo:
        GameSprite logoImage = new GameSprite(this, Textures.getTexture("./res/misc/ui/title.png"));

        logoImage.setOrigin(logoImage.getLocalBounds().width * 0.5f, 0);
        logoImage.setPosition(window.getWidth() * 0.5f, 200);
        addObject(logoImage);

        GameSaveState saveState = GameSaveState.load();
        ButtonController bc = new ButtonController();

        int buttonY = 550;

        //Buttons:
        if (saveState != null) {
            UIButton resumeGameButton = new UIButton("Resume Game", () -> {
                window.setActiveScreen(new LevelSelectScreen(window,  GameSaveState.getSaveState().getLevelCompletionTimes().isEmpty() ? "cartmel" : null, null));
            }, new Vector2f(GridUtils.SCREEN_WIDTH * 0.5f, buttonY), new Vector2f(260, 75));

            buttonY += 150;

            bc.addButton(resumeGameButton);
        }

        UIButton newGameButton = new UIButton("New Game", () -> {
            GameSaveState.reset();
            window.setActiveScreen(new StoryIntroductionScreen(window));
        }, new Vector2f(GridUtils.SCREEN_WIDTH * 0.5f, buttonY), new Vector2f(260, 75));

        bc.addButton(newGameButton);

        UIButton exitButton = new UIButton("Exit Game", () -> {
            System.exit(0);
        }, new Vector2f(GridUtils.SCREEN_WIDTH * 0.5f, 900), new Vector2f(260, 40));

        bc.addButton(exitButton);

        UIButton attributionButton = new UIButton("Credits", () -> {
            window.setActiveScreen(new CreditsScreen(window));
        }, new Vector2f(GridUtils.SCREEN_WIDTH * 0.5f, 1000), new Vector2f(260, 40));

        bc.addButton(attributionButton);

        addObject(bc);
    }
}
