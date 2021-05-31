package hashpizza.game.engine.ui.screens;

import hashpizza.game.engine.GameScreen;
import hashpizza.game.engine.GameWindow;
import hashpizza.game.engine.ui.ButtonController;
import hashpizza.game.engine.ui.Text;
import hashpizza.game.engine.ui.UIButton;
import hashpizza.game.engine.ui.screens.main.MainMenuScreen;
import hashpizza.game.engine.util.Fonts;
import hashpizza.game.engine.util.GridUtils;
import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Screen to display credits for audio etc. resources
 */
public class CreditsScreen extends GameScreen {

    /**
     * Creates the credits screen
     *
     * @param window the window parent
     */
    public CreditsScreen(GameWindow window) {
        super(window);

        setMusic(MainMenuScreen.MUSIC_MAIN_MENU);

        setBackgroundColor(new Color(55, 71, 79));

        try {
            //load from credits.txt file
            Text text = new Text(Files.readString(Paths.get("./res/credits.txt")), Fonts.PIXEL);
            text.setPosition(15, 15);

            addObject(text);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ButtonController bc = new ButtonController();

        bc.addButton(new UIButton("Main Menu", () -> {
            window.setActiveScreen(new MainMenuScreen(window));
        }, new Vector2f(GridUtils.SCREEN_WIDTH * 0.5f, 1000), new Vector2f(260, 40)));

        addObject(bc);
    }
}
