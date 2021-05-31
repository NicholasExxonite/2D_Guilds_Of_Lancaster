package hashpizza.game.engine.util;

import org.jsfml.audio.Music;
import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for loading sounds and music from file paths
 */
public final class Sounds {

    /**
     * Loads a sound from the specified path
     *
     * @param path the path to load the sound from
     * @return the sound from the specified path, or null if it can't be found
     */
    public static Sound getSound(String path) {
        try {
            SoundBuffer soundBuffer = new SoundBuffer();
            soundBuffer.loadFromFile(Paths.get(path)); //load the sound into the buffer

            return new Sound(soundBuffer);

        } catch (Exception ex) {
            ex.printStackTrace();
            return new Sound((SoundBuffer) null);
        }
    }

    /**
     * Loads a music file from the specified path. It will be set to loop by default
     *
     * @param path the path to load the music from
     * @return the music from the specified path, or null if it can't be found
     */
    public static Music getMusic(String path) {
        try {
            Music music = new Music();
            music.openFromFile(Path.of(path));

            music.setLoop(true); //loop by default

            return music;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static Music getMusic(String path, boolean doesLoop) {
        try {
            Music music = new Music();
            music.openFromFile(Path.of(path));

            music.setLoop(doesLoop); //loop by default


            return music;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
