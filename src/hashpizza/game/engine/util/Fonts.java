package hashpizza.game.engine.util;

import org.jsfml.graphics.Font;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Utility class to allow access to the game's fonts
 */
public final class Fonts {

    /**
     * Pixel-art font file, used for subtitles etc.
     */
    public static final Font PIXEL = new Font();

    /**
     * Medieval font file, used for titles etc.
     */
    public static final Font MEDIEVAL = new Font();

    static {
        try {
            PIXEL.loadFromFile(Paths.get("./res/Pixellari.ttf"));
            MEDIEVAL.loadFromFile(Paths.get("./res/euphorigenic.ttf"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
