package hashpizza.game.engine.ui.screens;

import hashpizza.game.engine.GameScreen;
import hashpizza.game.engine.GameWindow;
import hashpizza.game.engine.KeyHandler;
import hashpizza.game.engine.ui.Text;
import hashpizza.game.engine.ui.screens.levelselection.LevelSelectScreen;
import hashpizza.game.engine.util.*;
import org.jsfml.audio.Music;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConstFont;
import org.jsfml.window.Keyboard;

/**
 * The story introduction screen
 */
public class StoryIntroductionScreen extends GameScreen {

    /**
     * The intro background music
     */
    private static final Music MUSIC_STORY_INTRO = Sounds.getMusic("res/audio/music/pause.wav");

    /**
     * The lines of text to display for the intro
     */
    private static final String[] INTRO_LINES = new String[]{
            "The medieval of the town of Lancaster: a peaceful town made up of 8 powerful guilds, each with their own ability.",
            "The guilds were once united - 40 years ago during the Battle of the Roses, in which the Hero of Lancaster,\nwho brought together all 8 guilds through the mastery of their abilities, defeated the evil Geese.",
            "But since then, the guilds have slowly started to spread apart.",
            "Nowadays, life is much more simple. No longer about defending Lancaster, guilds have slowly adapted their ways to be more practical.",
            "However, this all changed when a young talent from the Cartmel Guild noticed a change in the landscape over the Great Lonsdale Wall..."
    };

    /**
     * Creates the story intro screen
     *
     * @param window the game window parent
     */
    public StoryIntroductionScreen(GameWindow window) {
        super(window);

        setMusic(MUSIC_STORY_INTRO);
        setBackgroundColor(Color.BLACK);

        addObject(new StoryIntroText(INTRO_LINES, Fonts.MEDIEVAL));
    }

    /**
     * Represents the story intro text, which should do a fade and scale animation when it is displayed
     */
    public class StoryIntroText extends Text implements KeyHandler {

        private Animation lineFadeAnimation = new Animation(0, 300f, 6f, true, Animations.EASE_OUT);
        private Animation lineScaleAnimation = new Animation(0.95f, 1.0f, 11f, false, Animations.EASE_OUT);

        private DeltaTimer lineTimer = new DeltaTimer(12);

        private String[] lines;

        private int currentLine = -1;

        /**
         * Creates the story intro text
         *
         * @param lines the lines of text to display
         * @param font  the font to use
         */
        public StoryIntroText(String[] lines, ConstFont font) {
            super("", font);

            this.lines = lines;

            setColor(Color.WHITE);
            setCharacterSize(32);

            setPosition(GridUtils.SCREEN_WIDTH * 0.5f, GridUtils.SCREEN_HEIGHT * 0.5f);

            nextLine();
        }

        /**
         * Shows the next line of the story intro
         */
        public void nextLine() {
            currentLine++;

            if (currentLine >= lines.length) {
                //show the level select when it is complete, and we should force them to start at cartmel
                getWindow().setActiveScreen(new LevelSelectScreen(getWindow(), "cartmel", null));
                return;
            }

            setString(lines[currentLine]);
            setOrigin(getLocalBounds().width * 0.5f, getLocalBounds().height * 0.5f);

            lineFadeAnimation.reset();
            lineScaleAnimation.reset();
            lineTimer.reset();
        }

        @Override
        public void update(float delta) {
            super.update(delta);

            setColor(new Color(255, 255, 255, (int) lineFadeAnimation.update(delta)));

            float scale = lineScaleAnimation.update(delta);

            setScale(scale, scale);

            if (lineTimer.update(delta)) { //if the line timer is complete, show the next line!
                nextLine();
            }
        }

        @Override
        public void onKeyPress(Keyboard.Key key) {
            nextLine(); //show the next line when any key is pressed
        }

        @Override
        public void onKeyRelease(Keyboard.Key key) {

        }
    }
}
