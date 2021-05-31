package hashpizza.game.engine.util;

import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.Texture;

import java.nio.file.Paths;

/**
 * Utility class for loading textures from files
 */
public final class Textures {

    /**
     * Texture to use when a texture can't be found
     */
    private static final ConstTexture MISSING_TEXTURE = getTexture("./res/missing_texture.png");

    /**
     * Finds and loads a texture from the specified path
     *
     * @param path the texture to find's path
     * @return the texture at the specified path, or the missing texture if it couldn't be found
     */
    public static ConstTexture getTexture(String path) {
        try {
            Texture text = new Texture();
            text.loadFromFile(Paths.get(path));

            return text;
        } catch (Exception ex) {
            ex.printStackTrace();
            return MISSING_TEXTURE;
        }
    }
}
