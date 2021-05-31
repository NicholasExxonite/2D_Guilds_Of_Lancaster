package hashpizza.game.engine.saving;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles saving, loading, and updating the game save file
 */
public class GameSaveState {

    /**
     * The location for the save file
     */
    private static final Path SAVE_LOCATION = Paths.get("./save.txt");

    /**
     * A map of the completed levels, storing the best time for each (in ms)
     */
    private Map<String, Long> levelCompletionTimes = new HashMap<>();

    /**
     * The epoch time of the last update of this save
     */
    private long lastUpdate;

    /**
     * Only one save is active at a time
     */
    private static GameSaveState saveInstance = new GameSaveState();

    /**
     * Loads the save instance from the save file
     *
     * @return the loaded save state, null if the file is empty
     */
    public static GameSaveState load() {
        saveInstance = new GameSaveState();

        if (!Files.exists(SAVE_LOCATION)) return null;

        try {
            int ix = 0;
            for (String line : Files.readAllLines(SAVE_LOCATION)) {
                if (ix == 0) {
                    //first line is our last save time
                    saveInstance.lastUpdate = Long.parseLong(line);
                } else {
                    //format:
                    //bowland:2302

                    String[] levelSplit = line.split(":", 2);

                    saveInstance.levelCompletionTimes.put(levelSplit[0], Long.parseLong(levelSplit[1]));
                }
                ix++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return saveInstance;
    }

    /**
     * Resets the save state to an empty save
     */
    public static void reset() {
        saveInstance = new GameSaveState();
    }

    /**
     * Saves the current save state to the save file
     */
    public static void save() {
        List<String> save = new ArrayList<>();
        save.add(System.currentTimeMillis() + "");

        for (Map.Entry<String, Long> entry : saveInstance.levelCompletionTimes.entrySet()) {
            save.add(entry.getKey() + ":" + entry.getValue());
        }

        try {
            Files.write(SAVE_LOCATION, save);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return the active save state
     */
    public static GameSaveState getSaveState() {
        return saveInstance;
    }

    /**
     * @return the map of each level name and how long the player has taken to complete it (their best time)
     */
    public Map<String, Long> getLevelCompletionTimes() {
        return levelCompletionTimes;
    }

    /**
     * @return epoch time of the last save file update
     */
    public long getLastUpdate() {
        return lastUpdate;
    }
}
