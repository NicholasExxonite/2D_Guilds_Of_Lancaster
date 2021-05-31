package hashpizza.game.engine.ui.screens.levelselection;

import hashpizza.game.engine.*;
import hashpizza.game.engine.platforming.LevelSchema;
import hashpizza.game.engine.platforming.LevelScreen;
import hashpizza.game.engine.ui.Dialogue;
import hashpizza.game.engine.util.GridUtils;
import hashpizza.game.engine.util.Sounds;
import hashpizza.game.engine.util.Textures;
import org.jsfml.audio.Music;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.window.Keyboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Level selection screen
 */
public class LevelSelectScreen extends GameScreen {

    /**
     * Level selection background music
     */
    private static final Music MUSIC_LEVEL_SELECT = Sounds.getMusic("./res/audio/music/level_select.wav");

    /**
     * All of the nodes on the map, i.e. the level names, position, metadata etc.
     */
    private List<LevelMapNode> mapNodes;

    /**
     * The currently hovered level
     */
    private int selectedNodeIndex = 0;

    /**
     * The rectangle to highlight currently selected levels
     */
    private LevelSelectionIndicator selectionIndicator;

    /**
     * The title to view above selected levels
     */
    private LevelSelectHoverTitle selectionHoverTitle;

    /**
     * Whether or not a level has been selected. If so, dialogue etc. will be showing
     */
    private boolean isLevelSelected = false;

    /**
     * Creates the level selection screen
     *
     * @param window               the window parent
     * @param forcedLevelSelection if not null, will force the selected level to start automatically
     * @param previousLevel        the previous level to automatically select
     */
    public LevelSelectScreen(GameWindow window, String forcedLevelSelection, LevelSchema previousLevel) {
        super(window);

        setMusic(MUSIC_LEVEL_SELECT);

        setPausable(true);

        mapNodes = new ArrayList<>();

        //level background
        ConstTexture bgTexture = Textures.getTexture("./res/misc/level-select.png");
        GameSprite bgSprite = new GameSprite(this, bgTexture);

        //set it to window size
        bgSprite.setScale((float) GridUtils.SCREEN_WIDTH / bgTexture.getSize().x, (float) GridUtils.SCREEN_HEIGHT / bgTexture.getSize().y);
        addObject(bgSprite);

        try {
            Files.walk(Paths.get("./levels")).forEach(p -> { //go through each file in the /levels/ folder...
                if (p.getFileName().toString().equals("levels")) return; //skip the folder
                try {
                    //load a level schema from the file
                    LevelSchema schema = LevelSchema.loadFromFile("./levels/" + p.getFileName().toString());

                    //load the texture from the file in the texture
                    mapNodes.add(new LevelMapNode(schema, Textures.getTexture("./res/misc/ability_icons/" + p.getFileName().getFileName().toString().split("\\.", 2)[0] + "_icon.png")));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //sort to make sure arrow keys work in correct order for cycling levels
        mapNodes.sort((o1, o2) -> (int) (o1.getPosition().x - o2.getPosition().x));

        if (forcedLevelSelection != null) {
            int ix = 0;
            for (LevelMapNode node : mapNodes) {
                if (node.getLevel().meta.filename.equalsIgnoreCase(forcedLevelSelection)) {
                    selectedNodeIndex = ix;
                    break;
                }
                ix++;
            }
        } else if (previousLevel != null) {
            int ix = 0;
            for (LevelMapNode node : mapNodes) {
                if (node.getLevel().meta.filename.equals(previousLevel.meta.filename)) {
                    selectedNodeIndex = ix;
                    break;
                }
                ix++;
            }
        }

        selectionIndicator = new LevelSelectionIndicator(this); //the red pulsing rectangle on the selected level
        selectionIndicator.setPosition(mapNodes.get(selectedNodeIndex).getPosition());
        selectionHoverTitle = new LevelSelectHoverTitle(mapNodes.get(selectedNodeIndex)); //the bubble above selected levels

        addObject(new LevelSelector());

        addObject(selectionIndicator);
        addObject(selectionHoverTitle);

        if (forcedLevelSelection != null) selectLevel(mapNodes.get(selectedNodeIndex).getLevel());
    }

    /**
     * Selects the specified level and starts it (shows the dialogue)
     *
     * @param level the level to select
     */
    public void selectLevel(LevelSchema level) {

        if (isLevelSelected) return; //if we've already selected a level don't do anything

        isLevelSelected = true;

        if (level.dialogue == null) { //if there's no dialogue just jump straight into it
            startLevel(level);
            return;
        }

        if (level.dialogue.length != 0) {
            Dialogue dialogue = new Dialogue(level.dialogue);
            dialogue.onComplete(() -> startLevel(level));

            addObject(dialogue);
        } else { //no dialogue, start directly...
            startLevel(level);
        }
    }

    /**
     * Starts the specified level (switches the active screen to it)
     *
     * @param level the level to start
     */
    public void startLevel(LevelSchema level) {
        getWindow().setActiveScreen(new LevelScreen(getWindow(), level));
    }

    /**
     * Moves the map node selection bubble to above the currently selected map node
     */
    public void updateMapNode() {
        selectionIndicator.setPosition(mapNodes.get(selectedNodeIndex).getPosition()); //update the bubble/indicator
        selectionHoverTitle.setNodePosition(mapNodes.get(selectedNodeIndex));
    }

    /**
     * Handles the key presses for updating which level is currently selected
     */
    public class LevelSelector implements GameObject, KeyHandler {

        @Override
        public void update(float delta) {
            //dummy object
        }

        @Override
        public void onKeyPress(Keyboard.Key key) {
            if (isLevelSelected) return; //if we've already selected a level (pressed enter), don't do anything

            if (key == Keyboard.Key.RIGHT || key == Keyboard.Key.D) {
                selectedNodeIndex++;
                if (selectedNodeIndex >= mapNodes.size()) selectedNodeIndex = 0; //loop back to start
            } else if (key == Keyboard.Key.LEFT || key == Keyboard.Key.A) {
                selectedNodeIndex--;
                if (selectedNodeIndex < 0) selectedNodeIndex = mapNodes.size() - 1; //loop back to end
            } else if (key == Keyboard.Key.RETURN) {
                selectLevel(mapNodes.get(selectedNodeIndex).getLevel()); //select the level when we press enter
            }

            updateMapNode();
        }

        @Override
        public void onKeyRelease(Keyboard.Key key) {

        }

        @Override
        public void draw(RenderTarget renderTarget, RenderStates renderStates) {
            //dummy method
        }
    }
}
