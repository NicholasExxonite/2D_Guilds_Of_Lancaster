package hashpizza.game.engine.platforming;

import hashpizza.game.engine.GameObject;
import hashpizza.game.engine.KeyHandler;
import hashpizza.game.engine.util.*;
import org.jsfml.audio.Sound;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

/**
 * Represents the player, who can be controlled by using the keyboard
 */
public class Player extends GameEntity implements KeyHandler {

    /**
     * Textures
     */
    private static final ConstTexture TEXTURE_IDLE = Textures.getTexture("./res/player/player-idle.png");
    private static final ConstTexture TEXTURE_RUN = Textures.getTexture("./res/player/player-run.png");
    private static final ConstTexture TEXTURE_RUN_2 = Textures.getTexture("./res/player/player-run-2.png");
    private static final ConstTexture TEXTURE_JUMP = Textures.getTexture("./res/player/player-jump.png");
    private static final ConstTexture TEXTURE_BOW_1 = Textures.getTexture("./res/player/abilities/player-bowland-1.png");
    private static final ConstTexture TEXTURE_BOW_2 = Textures.getTexture("./res/player/abilities/player-bowland-2.png");
    private static final ConstTexture TEXTURE_BOW_3 = Textures.getTexture("./res/player/abilities/player-bowland-3.png");
    private static final ConstTexture TEXTURE_BOW_4 = Textures.getTexture("./res/player/abilities/player-bowland-4.png");
    private static final ConstTexture TEXTURE_BOW_5 = Textures.getTexture("./res/player/abilities/player-bowland-5.png");
    private static final ConstTexture[] BOW_TEXTURES = new ConstTexture[]{TEXTURE_BOW_1, TEXTURE_BOW_2, TEXTURE_BOW_3, TEXTURE_BOW_4};
    private static final ConstTexture TEXTURE_DASH_PLAYER = Textures.getTexture("./res/player/abilities/player-fylde-1.png");
    private static final ConstTexture TEXTURE_SLOWFALL = Textures.getTexture("./res/player/abilities/player-cartmel-prettybutterfly.png");
    private static final ConstTexture TEXTURE_DOUBLEJUMP = Textures.getTexture("./res/player/abilities/player-furness.png");
    private static final ConstTexture TEXTURE_SLOWTIME = Textures.getTexture("./res/player/abilities/player-county.png");
    private static final ConstTexture TEXTURE_ROAR = Textures.getTexture(("./res/player/abilities/player-lonsdale.png"));

    /**
     * Animated textures
     */
    private static AnimatedTexture ANIMATION_RUN = new AnimatedTexture(0.8f, TEXTURE_RUN, TEXTURE_RUN_2);

    /**
     * Sound effects
     */
    private static final Sound SOUND_STEP = Sounds.getSound("./res/audio/player/footsteps_dirt.wav");
    private static final Sound SOUND_JUMP = Sounds.getSound("res/audio/player/jump.wav");
    private static final Sound SOUND_SHOOT_ARROW = Sounds.getSound("res/audio/player/shoot_arrow.wav");
    private static final Sound SOUND_THROW = Sounds.getSound("res/audio/player/throw.wav");
    private static final Sound SOUND_DASH = Sounds.getSound("res/audio/player/dash.wav");
    private static final Sound SOUND_GRUNT = Sounds.getSound("res/audio/player/grunt.wav");
    private static final Sound SOUND_LION_ROAR = Sounds.getSound("res/audio/player/LionRoar.wav");

    /**
     * The maximum X velocities for the player
     */
    private static final float VELOCITY_CAP_X = 4;
    private static final float VELOCITY_CAP_X_DASHING = 10;

    /**
     * Movement keys
     */
    private boolean leftPressed, rightPressed, jumpPressed;

    /**
     * Ability flags & timers
     */
    private boolean gliding, dashUsed, doubleJumping, hasShield, drawingBow, roarUsed;
    private float bowDrawDuration;
    private float deadAnimationFrameCounter, deadAnimationCounter = -1;

    /**
     * Whether the player can currently double jump (for the furness ability)
     */
    private boolean canDoubleJump;

    /**
     * The duration of each dash
     */
    private DeltaTimer dashTimer = new DeltaTimer(0.5f);

    /**
     * The duration of each shield usage
     */
    private DeltaTimer shieldTimer = new DeltaTimer(3f);

    /**
     * The duration of the roar
     */
    private DeltaTimer roarTimer = new DeltaTimer(1f);

    /**
     * Where the player respawns when they die
     */
    private Vector2f spawnLocation;

    /**
     * Creates the player instance at the specified location
     *
     * @param screen   the level screen
     * @param position the position to spawn the player
     */
    public Player(LevelScreen screen, Vector2f position) {
        super(screen, position, false, TEXTURE_IDLE);

        setSpawnLocation(position);
    }

    /**
     * Update method is called every frame
     */
    @Override
    public void update(float delta) {

        //Abilities: shield
        if (hasShield) {
            setHealth(Integer.MAX_VALUE);

            if (shieldTimer.update(delta)) {
                setHealth(1);

                shieldTimer.reset();
                hasShield = false;
            }
        }

        //Abilities: dash
        if (!dashTimer.update(delta)) {
            if (dashUsed) {
                setTexture(TEXTURE_DASH_PLAYER);
                dashUsed = true;
                velocity = new Vector2f((isFacingLeft() ? -1 : 1) * 10, velocity.y);
                Dash dash = new Dash((LevelScreen) getScreen(), getPosition(), isFacingLeft());
                getScreen().addObject(dash);
            }
        } else {
            dashUsed = false;
            setGravity(true);
            dashTimer.reset();
        }

        if (leftPressed && !rightPressed) { //Movement: left
            float newVel = velocity.x - (0.1f * delta * 60);

            velocity = new Vector2f(newVel, velocity.y);
            setFacingLeft(true);

        } else if (rightPressed && !leftPressed) { //Movement: right
            float newVel = velocity.x + (0.1f * delta * 60);

            velocity = new Vector2f(newVel, velocity.y);
            setFacingLeft(false);

        } else {
            float newVel = velocity.x * 0.95f;
            velocity = new Vector2f(newVel, velocity.y);
        }

        float velCapX = dashUsed ? VELOCITY_CAP_X_DASHING : VELOCITY_CAP_X;

        if (Math.abs(velocity.x) > velCapX) {
            velocity = new Vector2f((velocity.x < 0 ? -1 : 1) * velCapX, velocity.y);
        }

        if (isOnGround() && jumpPressed) {
            SOUND_JUMP.play();
            velocity = Vector2f.add(velocity, new Vector2f(0, -5.5f));
            System.out.println(System.currentTimeMillis());
            canDoubleJump = true;
        } else if (!isOnGround() && doubleJumping && canDoubleJump) {
            SOUND_JUMP.play();
            velocity = Vector2f.add(velocity, new Vector2f(0, -3.5f));
            canDoubleJump = false;
        }


        //roar
        if(roarUsed){
            setTexture(TEXTURE_ROAR);
            if(roarTimer.update(delta)){
                roarTimer.reset();
                roarUsed = false;
            }
        }
        else if (dashUsed) {
            SOUND_DASH.play();
            setTexture(TEXTURE_DASH_PLAYER);
        } else if (drawingBow) {
            bowDrawDuration += delta;
            int textureIndex = (int) (bowDrawDuration * 2.5f);
            if (textureIndex >= BOW_TEXTURES.length) textureIndex = BOW_TEXTURES.length - 1;

            setTexture(BOW_TEXTURES[textureIndex]);
        } else if (doubleJumping && !canDoubleJump) {
            setTexture(TEXTURE_DOUBLEJUMP);
        } else if (!isOnGround()) {
            if (gliding) {
                setTexture(TEXTURE_SLOWFALL);
                velocity = new Vector2f(velocity.x, -0.0005f);
            } else {
                setTexture(TEXTURE_JUMP);
            }
        } else if (Math.abs(velocity.x) < 0.05) {
            setTexture(TEXTURE_IDLE);
        } else {
            ConstTexture runTexture = ANIMATION_RUN.update(delta * Math.abs(velocity.x));
            setTexture(runTexture);

            if (ANIMATION_RUN.textureChanged()) {

                SOUND_STEP.play();
            }
        }

        if (deadAnimationCounter != -1) {
            deadAnimationFrameCounter += delta;
            if (deadAnimationFrameCounter > 0.1) {
                deadAnimationFrameCounter = 0;

                if (getColor() == Color.RED) {
                    setColor(Color.WHITE);
                } else {
                    setColor(Color.RED);
                }
            }

            deadAnimationCounter += delta;

            if (deadAnimationCounter >= 3.5f) {
                setColor(Color.WHITE);

                deadAnimationCounter = -1;
                deadAnimationFrameCounter = 0;
            }
        }

        float x = getPosition().x;
        float y = getPosition().y;

        if (x >= GridUtils.SCREEN_WIDTH) { //if the player has reached the end of the current screen
            ((LevelScreen) getScreen()).nextScreen();
        } else if (x <= 0) { //if the player attempts to backtrack off the left of the screen, move them forwards
            setPosition(0, getPosition().y);
        }

        if (y >= GridUtils.SCREEN_HEIGHT) { //if they fall off the stage then kill the player
            setHealth(0);
        }

        super.update(delta);
    }

    /**
     * Sets the player's spawn location to the specified location
     *
     * @param spawnLocation the spawn location
     */
    public void setSpawnLocation(Vector2f spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    /**
     * Sets whether the player should be dashing. Has no effect if dashing is true but the player is already
     * dashing
     */
    public void dash() {
        dashUsed = true;
        setGravity(false);
    }

    /**
     * Sets whether the player should be gliding
     *
     * @param gliding whether the player should be gliding
     */
    public void setGliding(boolean gliding) {

        this.gliding = gliding;
    }

    /**
     * @return whether or not the player is double jumping (whilst the furness ability key is held down)
     */
    public boolean isDoubleJumping() {
        return doubleJumping;
    }

    /**
     * Sets whether or not the player should be able to double jump
     *
     * @param doubleJumping whether the player should be able to double jump
     */
    public void setDoubleJumping(boolean doubleJumping) {

        if (!canDoubleJump && doubleJumping) return;

        this.doubleJumping = doubleJumping;
    }

    /**
     * Sets the player to be drawing their bow
     */
    public void startDrawingBow() {
        if (drawingBow) return;

        drawingBow = true;
        bowDrawDuration = 0;
    }

    /**
     * Releases the bow and shoots an arrow if the player was drawing their bow
     */
    public void stopDrawingBow() {
        if (drawingBow) shootArrow();

        drawingBow = false;
    }

    /**
     * Shoots an arrow from the player's location
     */
    public void shootArrow() {
        Arrow arrow = new Arrow((LevelScreen) getScreen(), Vector2f.add(getPosition(), new Vector2f(getLocalBounds().width * 0.5f, getLocalBounds().height * 0.5f)), isFacingLeft(), bowDrawDuration);
        getScreen().addObject(arrow);

        SOUND_SHOOT_ARROW.play();

        bowDrawDuration = 0;
    }

    /**
     * Throws poison from the player's location
     */
    public void throwPoison() {
        Potion potion = new Potion((LevelScreen) getScreen(), getPosition(), this.isFacingLeft());
        getScreen().addObject(potion);

        SOUND_THROW.play();
    }

    /**
     * Stuns all of the objects on the screen
     */
    public void roar() {
        roarUsed = true;
        SOUND_LION_ROAR.play();
        for (GameObject object : getScreen().getObjects()) {
            if (object instanceof Enemy) {
                ((Enemy) object).setStunTimer(new DeltaTimer(5));
            }
        }
    }

    /**
     * Activates the shield from the Grizedale ability
     */
    public void useShield() {
        hasShield = true;

        shieldTimer.reset();

      //  Shield shield = new Shield((LevelScreen) getScreen(), getPosition());
       // getScreen().addObject(shield);
    }

    @Override
    public void onKeyPress(Keyboard.Key key) {
        if ((key == Keyboard.Key.LEFT) || key == Keyboard.Key.A) {
            leftPressed = true;
        } else if (key == Keyboard.Key.RIGHT || key == Keyboard.Key.D) {
            rightPressed = true;
        } else if (key == Keyboard.Key.SPACE || key == Keyboard.Key.W || key == Keyboard.Key.UP) {
            if (isOnGround()) {
                jumpPressed = true;
            }
        }
    }

    @Override
    public void onKeyRelease(Keyboard.Key key) {
        if (key == Keyboard.Key.LEFT || key == Keyboard.Key.A) {
            leftPressed = false;
        } else if (key == Keyboard.Key.RIGHT || key == Keyboard.Key.D) {
            rightPressed = false;
        } else if (key == Keyboard.Key.SPACE || key == Keyboard.Key.W || key == Keyboard.Key.UP) {
            jumpPressed = false;
            //jumpPressedInAir = false;
        }
    }

    @Override
    public void onDamage() {
        SOUND_GRUNT.play();

        if (isDead()) {
            setColor(Color.RED);

            setPosition(spawnLocation);

            setHealth(1);

            deadAnimationCounter = 0;
        }
    }
}
