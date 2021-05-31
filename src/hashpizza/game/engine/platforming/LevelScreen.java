package hashpizza.game.engine.platforming;

import hashpizza.game.engine.GameObject;
import hashpizza.game.engine.GameScreen;
import hashpizza.game.engine.GameSprite;
import hashpizza.game.engine.GameWindow;
import hashpizza.game.engine.platforming.abilities.Abilities;
import hashpizza.game.engine.platforming.abilities.Ability;
import hashpizza.game.engine.platforming.abilities.AbilityManager;
import hashpizza.game.engine.platforming.abilities.selector.AbilitySelector;
import hashpizza.game.engine.saving.GameSaveState;
import hashpizza.game.engine.ui.screens.LevelCompleteScreen;
import hashpizza.game.engine.util.*;
import org.jsfml.audio.Music;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A level screen handles displaying the contents of actual game level screens
 */
public class LevelScreen extends GameScreen {

    /**
     * The background music for the game's levels
     */
    private static final Music MUSIC_LEVEL = Sounds.getMusic("./res/audio/music/level.wav");

    /**
     * The level schema for the current level. Contains all of the tile data etc. used to add the entities onto the
     * screen
     */
    private LevelSchema schema;

    /**
     * The current screen within the level
     */
    private int currentScreenId = 0;

    /**
     * The player entity
     */
    private Player player;

    /**
     * A subset of this screen's objects, filtered to only the game's entities. Used to handle physics, collision
     * logic, screen transitions, etc.
     */
    private List<GameEntity> entities = new ArrayList<>();

    /**
     * The background image
     */
    private GameSprite background;

    /**
     * The background clouds
     */
    private GameObject clouds;

    /**
     * The time at which the user started the level, used for calculating completion time
     */
    private long startTime; //todo pause time should be factored in and excluded

    /**
     * Used to orchestrate changing the screen during the brief demo played at the start of loading a level
     */
    private float levelDemoTimer = 1f;

    /**
     * Handles pressing and activating the abilities the user has chosen
     */
    private AbilityManager abilityManager;

    /**
     * Handles selecting the abilities at the start of the level
     */
    private AbilitySelector abilitySelector;

    /**
     * Used for performing the transition animation when switching screens during the demo
     */
    private List<GameEntity> newScreenEntities;

    /**
     * Overlay displaying the level title and icon
     */
    private DemoOverlay demoOverlay;

    /**
     * Handles slow-motion used for the county ability
     */
    private DeltaTimer slowMotionTimer;

    /**
     * Creates a level screen
     *
     * @param window the window
     * @param schema the level schema to load
     */
    public LevelScreen(GameWindow window, LevelSchema schema) {
        super(window);

        setMusic(MUSIC_LEVEL);

        setPausable(true);

        this.schema = schema;

        background = new GameSprite(this, Textures.getTexture("./res/misc/full-background.png")) {
            @Override
            public void update(float delta) {
                if (player != null) { //parallax effect - so it follows the player in the opposite direction to where they move
                    background.setPosition(960 - (player.getPosition().x / 40), 540);
                }
            }
        };
        background.setOrigin(background.getLocalBounds().width / 2, background.getLocalBounds().height / 2);
        background.setScale(1.1f, 1.1f); //slightly bigger than the window size to allow the parallax
        background.setPosition(GridUtils.SCREEN_WIDTH * 0.5f, GridUtils.SCREEN_HEIGHT * 0.5f);
        background.setColor(Color.CYAN);

        clouds = new BackgroundClouds();

        addObject(background);
        addObject(clouds);

        demoOverlay = new DemoOverlay(schema);

        displayLevelScreen(schema.screens[0], true, false); //load and display the first screen

        addObject(new GameObject() { //demo controller, used to handle displaying each screen of the level when the player starts it
            @Override
            public void update(float delta) {
                if (levelDemoTimer < 0) return;

                levelDemoTimer -= delta;

                if (levelDemoTimer <= 0) { //when it's time to change the screen
                    levelDemoTimer = 1.8f;

                    currentScreenId++;

                    if (currentScreenId >= schema.screens.length) { //if the demo is now over
                        currentScreenId = 0;
                        levelDemoTimer = -1;
                        displayLevelScreen(schema.screens[0], false, false);

                        removeObject(this);

                        GameSaveState save = GameSaveState.getSaveState();

                        //checks if we're required to use an ability - not the case if the player has already
                        //completed the level
                        Ability forcedAbility = null;

                        if (!save.getLevelCompletionTimes().containsKey(schema.meta.title.toLowerCase()))
                            forcedAbility = Abilities.ofName(schema.meta.title.toLowerCase());

                        addObject(abilitySelector = new AbilitySelector(LevelScreen.this, forcedAbility));

                    } else {
                        displayLevelScreen(schema.screens[currentScreenId], true, true);
                    }
                }
            }

            @Override
            public void draw(RenderTarget renderTarget, RenderStates renderStates) {
                //dummy
            }
        });
    }

    /**
     * When the user has selected their abilities, this creates the ability manager to handle the 1,2,3/j,k,l keybinds
     * for activating the abilities etc.
     */
    public void completedAbilitySelection() {
        addObject(abilityManager = new AbilityManager(LevelScreen.this, abilitySelector.getSlots()));

        startTime = System.currentTimeMillis();
    }

    /**
     * Displays the next screen of the game. If they're already on the last screen, will display the level
     * complete screen instead
     */
    public void nextScreen() {
        if (++currentScreenId >= schema.screens.length) {
            getWindow().setActiveScreen(new LevelCompleteScreen(getWindow(), schema, System.currentTimeMillis() - startTime));
        } else {
            displayLevelScreen(schema.screens[currentScreenId], false, false);
        }
    }

    /**
     * Adds an entity to the screen. Required to properly handle the screen transition during the game demo
     *
     * @param entity the entity to add to the screen
     */
    private void addNewScreenEntity(GameEntity entity) {

        newScreenEntities.add(entity);
        addObject(entity);
    }

    /**
     * Displays the specified level data
     *
     * @param data   the level data to display/add to the screen
     * @param isDemo whether or not the game is in demo mode, if so it won't add the player
     * @param slide  whether to perform the sliding screen transition
     */
    public void displayLevelScreen(LevelSchema.LevelScreenData data, boolean isDemo, boolean slide) {

        //get all the previous entities on the screen, used if the slide animation will be used
        List<GameEntity> prevEntities = getObjects().stream().filter(e -> (e instanceof GameEntity) && !(e instanceof Player)).map(e -> (GameEntity) e).collect(Collectors.toList());

        newScreenEntities = new ArrayList<>();

        if (abilitySelector != null)
            removeObject(abilitySelector); //will be re-added later - to ensure it is always displayed on top

        if (player != null) {
            Vector2f pos = new Vector2f(0, player.getPosition().y - 0.5f);
            player.setPosition(pos);
            player.setSpawnLocation(pos); //change their spawn location when the screen switches
        }

        int x = 0;
        int y = 0;

        //parsing the tile map of each level
        for (String s : data.tileMap) {
            for (char c : s.toCharArray()) {
                if (c == '1') {
                    addNewScreenEntity(new Block(this, x, y, schema.meta.foreground, Block.BLOCK));
                } else if (c == '2') {
                    addNewScreenEntity(new Block(this, x, y, schema.meta.foreground, Block.BLOCK)    //this is just a temporary tests to try out lifts
                    {

                        float totalTime = 0;
                        int moveDirection = 1;

                        @Override
                        public void update(float delta) {
                            velocity = new Vector2f(moveDirection, 0f);

                            totalTime += delta;

                            // setPosition(Vector2f.add(getPosition(), new Vector2f(30f * delta * moveDirection, 0f)));
                            if (totalTime > 2f) {
                                moveDirection *= -1;
                                totalTime = 0;
                            }

                            super.update(delta);
                        }
                    });
                } else if (c == '3') {
                    addNewScreenEntity(new Spikes(this, x, y, schema.meta.foreground));
                } else if (c == 'g') {
                    addNewScreenEntity(new Block(this, x, y, null, Block.GRASS));
                } else if (c == 'd') {
                    addNewScreenEntity(new Block(this, x, y, null, Block.DIRT));
                } else if (c == 'G') {
                    addNewScreenEntity(new GooseGrunt(this, GridUtils.convertGridCoordinatesToPixels(x, y), true));
                } else if (c == 'S') {
                    addNewScreenEntity(new GooseSpitter(this, GridUtils.convertGridCoordinatesToPixels(x, y), true));
                } else if (c == 'V') {
                    addNewScreenEntity(new GooseSpike(this, GridUtils.convertGridCoordinatesToPixels(x, y), true));
                } else if (c == 'P' && !isDemo) {
                    player = new Player(this, GridUtils.convertGridCoordinatesToPixels(x, y));
                    addNewScreenEntity(player);
                }
                x++;
            }

            x = 0;
            y++;
        }

        removeObject(demoOverlay);

        if (isDemo) {
            addObject(demoOverlay); //to stay on top
        }

        if (abilitySelector != null) {
            addObject(abilitySelector); //to ensure it is rendered on top of everything else
        }

        if (!slide) { //just delete the previous objects
            for (GameEntity e : prevEntities) {
                removeObject(e);
            }

            return;
        }

        List<GameEntity> newEntities = new ArrayList<>(newScreenEntities);

        for (GameEntity e : newEntities) {
            e.setOrigin(e.getOrigin().x - GridUtils.SCREEN_WIDTH, e.getOrigin().y); //set them to render off the right side of the screen
        }

        addObject(new GameObject() {
            Animation screenChangeAnimation = new Animation(0, -GridUtils.SCREEN_WIDTH, 1.0f, false, Animations.EASE_IN_OUT);

            float prevScreenChangePos;

            @Override
            public void update(float delta) {
                float screenChangePos = screenChangeAnimation.update(delta);
                float diff = prevScreenChangePos - screenChangePos;

                prevScreenChangePos = screenChangePos;

                //slide the previous entities from their current pos to off the left of it
                for (GameEntity e : prevEntities) {
                    e.setOrigin(e.getOrigin().x + diff, e.getOrigin().y);
                }

                //slide the new entities from off the right of the screen to their actual position on screen
                for (GameEntity e : newEntities) {
                    e.setOrigin(e.getOrigin().x + diff, e.getOrigin().y);
                }

                if (screenChangeAnimation.isComplete()) {
                    prevEntities.forEach(e -> removeObject(e));
                    removeObject(this);
                }
            }

            @Override
            public void draw(RenderTarget renderTarget, RenderStates renderStates) {
                //dummy method
            }
        });
    }

    @Override
    public void postUpdate() {
        if (areObjectsModified()) {
            //make sure the entity list is up to date
            entities = getObjects().stream().filter(o -> o instanceof GameEntity).map(o -> (GameEntity) o).collect(Collectors.toList());
        }

        super.postUpdate();
    }

    /**
     * Gets the subset of the screen's objects that are game entities
     *
     * @return the game entities on this screen
     */
    public List<GameEntity> getEntities() {
        return entities;
    }

    /**
     * @return the level schema loaded by this level
     */
    public LevelSchema getSchema() {
        return schema;
    }

    /**
     * @return the player instance on this level
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Whether or not the level is active and the player can play (i.e. the demo has ended)
     *
     * @return if the level is active
     */
    public boolean isActive() {
        return abilitySelector != null && !abilitySelector.active;
    }

    /**
     * Gets the epoch time that the player started the level at
     *
     * @return the time that the player started the level
     */
    public long getStartTime() {
        return startTime;
    }

    public void setSlowMotionTimer(DeltaTimer slowMotionTimer) {

        this.slowMotionTimer = slowMotionTimer;
    }

    /**
     * @return the slow-motion timer, used for the county ability
     */
    public DeltaTimer getSlowMotionTimer() {
        return slowMotionTimer;
    }

    @Override
    public void update(RenderTarget target, float delta) {

        boolean slowMo = slowMotionTimer != null && !slowMotionTimer.update(delta); //check if slow-mo timer is not complete

        for (GameObject obj : getObjects()) {
            obj.update(slowMo && (obj instanceof Enemy) ? delta * 0.25f : delta); //update and draw each object for each game loop
            obj.draw(target, RenderStates.DEFAULT);
        }
    }
}
