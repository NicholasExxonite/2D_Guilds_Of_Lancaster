package hashpizza.game.engine.ui.screens;

import hashpizza.game.engine.GameScreen;
import hashpizza.game.engine.GameSprite;
import hashpizza.game.engine.GameWindow;
import hashpizza.game.engine.ui.screens.main.MainMenuScreen;
import hashpizza.game.engine.util.Animations;
import hashpizza.game.engine.util.Sounds;
import hashpizza.game.engine.util.Textures;
import org.jsfml.audio.Music;
import org.jsfml.graphics.Color;

/**
 * The splash screen with the pizza logo
 */
public class SplashScreen extends GameScreen {

    /**
     * The music to play on the splash screen
     */
    private static final Music MUSIC_SPLASH_SCREEN = Sounds.getMusic("res/audio/music/logo.wav");

    /**
     * Creates the splash screen
     *
     * @param window the window parent
     */
    public SplashScreen(GameWindow window) {
        super(window);

        setMusic(MUSIC_SPLASH_SCREEN);

        setBackgroundColor(new Color(255, 255, 255)); //white

        GameSprite logo = new GameSprite(this, Textures.getTexture("./res/misc/ui/hashpizza-logo.png")) {

            float animationProgress = 0f;

            @Override
            public void update(float delta) {
                animationProgress += delta * 1.2f;

                if (animationProgress >= 1.2f) {

                    //when we're done, show the main menu
                    window.setActiveScreen(new MainMenuScreen(window));
                }

                //logo zoom easing animation

                float prog = Math.min(animationProgress, 1f);

                setColor(new Color(255, 255, 255, (int) (255 * prog)));
                setScale(Animations.EASE_OUT.calculate(0.3f, 0.5f, prog), Animations.EASE_OUT.calculate(0.3f, 0.5f, prog));

                super.update(delta);
            }
        };

        logo.setOrigin(logo.getLocalBounds().width * 0.5f, logo.getLocalBounds().height * 0.5f);
        logo.setPosition(960, 540); //centre screen

        logo.setScale(0.2f, 0.2f);

        addObject(logo);
    }
}
