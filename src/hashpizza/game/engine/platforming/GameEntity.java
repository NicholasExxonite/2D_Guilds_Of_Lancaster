/**
 * Any characters featured within a level is Game character.
 */
package hashpizza.game.engine.platforming;

import hashpizza.game.engine.GameSprite;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.util.List;

/**
 * Represents a game sprite which has collision, velocity, acceleration and health, and can interact with other entities
 * in the game world
 */
public class GameEntity extends GameSprite {

    /**
     * Displays all entity hitboxes if true
     */
    private static final boolean displayHitboxes = false; //for debug only

    /**
     * The maximum speed entities can reach in the x direction
     */
    private static final float TERMINAL_VELOCITY_X = 100;

    /**
     * The maximum speed entities can reach in the y direction
     */
    private static final float TERMINAL_VELOCITY_Y = 100;

    /**
     * The collision hitbox of this entity
     */
    private FloatRect hitbox;

    /**
     * The collision hitbox of this entity with bounds in relation to the entire screen applied, i.e. it has an exact
     * size as well as offset to represent it within the screen rather than just the size. Calculated from the hitbox
     * variable when it is updated
     */
    private FloatRect globalHitbox;

    /**
     * The velocity of this entity, i.e. how much to move it per time unit (update with delta time)
     */
    Vector2f velocity;

    /**
     * The acceleration of this entity, i.e. how fact our velocity is increasing/decreasing
     */
    Vector2f acceleration;

    /**
     * The amount of health this entity has remaining
     */
    private int health = 1;

    /**
     * Whether or not this entity is dead
     */
    private boolean dead = false;

    /**
     * Whether or not this entity is facing left. If so, the texture will be mirrored horizontally in order to flip
     * the texture and make it appear that it is facing left
     */
    private boolean facingLeft;

    /**
     * Whether this entity responds to collisions and gravity (if also enabled)
     */
    private boolean hasPhysics = true;

    /**
     * Whether this entity responds to gravity
     */
    private boolean usesGravity = true;

    /**
     * Whether this entity skips collision handling
     */
    private boolean ignoresCollisions = false;

    /**
     * Creates a new game entity to be displayed in the specified game screen. By default, it will respond to physics
     *
     * @param screen     the screen to display this entity in
     * @param position   the position of this entity
     * @param facingLeft whether or not this entity is facing the left
     * @param texture    the texture of this entity
     */
    public GameEntity(LevelScreen screen, Vector2f position, boolean facingLeft, ConstTexture texture) {
        super(screen, texture);

        this.facingLeft = facingLeft;
        this.hitbox = new FloatRect(0, 0, getLocalBounds().width, getLocalBounds().height); //originally set to the full width and height of this entity

        setPosition(position);
        setOrigin(getLocalBounds().width * 0.5f, getLocalBounds().height * 0.5f);

        recalculateHitbox();

        this.velocity = Vector2f.ZERO;
        this.acceleration = Vector2f.ZERO;
    }

    /**
     * Re-calculates the global-bounds hitbox to take into perspective this entity's position within the entire level
     */
    private void recalculateHitbox() {
        if (hitbox == null) {
            globalHitbox = null;
            return;
        }

        globalHitbox = new FloatRect(getPosition().x + hitbox.left, getPosition().y + hitbox.top, hitbox.width, hitbox.top + hitbox.height);
    }

    /**
     * Changes the size of this entity's hitbox, which is used for calculating collision with itself and other entities
     *
     * @param hitbox the new hitbox for this entity
     */
    public void setHitbox(FloatRect hitbox) {

        this.hitbox = hitbox;
        recalculateHitbox();
    }

    /**
     * @return this entity's hitbox, expanded by 0.2 on all sides
     */
    public FloatRect getExpandedHitbox() {
        return new FloatRect(globalHitbox.left - 0.2f, globalHitbox.top - 0.2f, globalHitbox.width + 0.2f, globalHitbox.height + 0.2f);
    }

    /**
     * @return this entity's hitbox in the scope of the entire screen
     */
    public FloatRect getGlobalHitbox() {
        return globalHitbox;
    }

    @Override
    public void setPosition(Vector2f position) {
        super.setPosition(new Vector2f(position.x + getLocalBounds().width * 0.5f, position.y + getLocalBounds().height * 0.5f));

        recalculateHitbox();
    }

    @Override
    public Vector2f getPosition() {
        Vector2f position = super.getPosition();
        return new Vector2f(position.x - getLocalBounds().width * 0.5f, position.y - getLocalBounds().height * 0.5f);
    }

    @Override
    public void update(float delta) {
        if (!((LevelScreen) getScreen()).isActive()) return;

        super.update(delta);
        if (health < 1)
            dead = true;

        //X and Y position calculations need to be performed separately so we know whether we should stop movement
        //in the X direction, or movement in the Y direction if a collision is identified

        if (hasPhysics) {
            if (usesGravity) velocity = Vector2f.add(velocity, new Vector2f(0, 7.5f * delta)); //apply gravity here

            Vector2f nextPos = getPosition(); //by default, we don't move, so the next position is the same as the current one

            if (!ignoresCollisions) {

                Vector2f nextPosX = calculateNextPosition(delta, true, false);
                Vector2f nextPosY = calculateNextPosition(delta, false, true);

                GameEntity ge = getCollisionAtPosition(nextPosX, ((LevelScreen) getScreen()).getEntities());
                if (ge == null) { //if we can move in x direction...
                    nextPos = new Vector2f(nextPosX.x, nextPos.y); //change the next x position to our new one

                } else { //take the colliding's velocity
                    velocity = new Vector2f(ge.velocity.x, velocity.y);
                }

                if (getCollisionAtPosition(nextPosY, ((LevelScreen) getScreen()).getEntities()) == null) { //if we can move in y direction...
                    nextPos = new Vector2f(nextPos.x, nextPosY.y); //change the next y position to our new one

                } else {
                    acceleration = new Vector2f(acceleration.x, 0);
                    velocity = new Vector2f(velocity.x, 0);
                }
            } else {
                nextPos = calculateNextPosition(delta, true, true);
            }

            setPosition(nextPos);
        }

        if (facingLeft) {
            setScale(-1, 1);
        } else {
            setScale(1, 1);
        }
    }

    /**
     * Returns whether or not this entity is facing left
     *
     * @return true if this entity is facing left
     */
    public boolean isFacingLeft() {
        return facingLeft;
    }

    /**
     * Method to set this entity facing to the left. This will flip the sprite's texture on the X axis
     *
     * @param facingLeft whether this entity is facing left
     */
    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    /**
     * Returns whether or not this entity responds to physics
     *
     * @return true if this entity responds to physics
     */
    public boolean hasPhysics() {
        return hasPhysics;
    }

    /**
     * Returns whether or not this entity responds to gravity
     *
     * @return true if this entity responds to gravity
     */
    public boolean hasGravity() {
        return usesGravity;
    }

    /**
     * Sets whether or not this entity responds to physics
     *
     * @param hasPhysics true if this entity responds to physics
     */
    public void setPhysics(boolean hasPhysics) {
        this.hasPhysics = hasPhysics;
    }

    /**
     * Sets whether or not this entity responds to gravity
     *
     * @param hasGravity true if this entity responds to gravity
     */
    public void setGravity(boolean hasGravity) {
        this.usesGravity = hasGravity;
    }

    public void dealDamage(int amount) {
        setHealth(health - amount);
    }

    public void setHealth(int health) {
        this.health = Math.max(0, health);

        boolean wasDead = dead;
        dead = this.health <= 0;

        //DON'T fire if the entity is dead AND they were dead

        if (dead) {
            getScreen().addObject(new DeathParticles(((LevelScreen) getScreen()), getPosition(), Color.WHITE));
        }

        if (!wasDead || !dead) {
            onDamage();
        }

        System.out.println(this + " HEALTH: " + this.health);
        System.out.println("DEAD? " + dead);
    }

    /**
     * @return the health of the entity
     */
    public int getHealth() {
        return health;
    }

    /**
     * For sub-classes to handle if needed
     */
    public void onDamage() {

    }

    /**
     * Deals damage to another entity
     */
    public void doDamage(GameEntity entity) {
        entity.dealDamage(1);
    }

    /**
     * Utility method to determine if the entity is on the ground. It does this by determining whether moving downwards
     * would result in a collision with another entity (i.e. the ground)
     *
     * @return whether the entity is on the ground
     */
    public boolean isOnGround() {
        return getCollisionAtPosition(Vector2f.add(getPosition(), new Vector2f(0f, 0.2f)), ((LevelScreen) getScreen()).getEntities()) != null;
    }

    /**
     * Returns the game entity that this entity will collide with if at this position. Returns null if there will be
     * no collision.
     *
     * @param position the position to check if we can move to
     * @param objects  the objects within this level screen
     * @return the game entity at this position if there would be a collision, otherwise null
     */
    public GameEntity getCollisionAtPosition(Vector2f position, List<GameEntity> objects) {
        if (globalHitbox == null) return null;

        FloatRect hb = new FloatRect(position.x, position.y, globalHitbox.width, globalHitbox.height); //left, top, width, height

        for (GameEntity obj : objects) {
            if (getScreen().markedForRemoval(obj)) continue;

            if (obj != this && collidesWith(hb, obj)) {
                if (!handleCollision(obj)) continue;
                if (!obj.handleCollision(this)) continue; //todo testing
                //System.out.println(this + " (" + globalHitbox + ") COLLIDES WITH " + obj + " (" + obj.globalHitbox + ")");
                return obj; //collision!
            }
        }

        return null;
    }

    /**
     * Calculates the position this entity will be if velocity is applied to it. X and Y velocity can be
     * calculated separately (for collision purposes) by making use of the boolean parameters
     *
     * @param delta     the game delta time to apply to the velocities
     * @param applyXVel whether to apply the x velocity to the current position
     * @param applyYVel whether to apply the y velocity to the current position
     * @return a vector representing the new calculated position
     */
    public Vector2f calculateNextPosition(float delta, boolean applyXVel, boolean applyYVel) {

        float xVel = velocity.x;
        float yVel = velocity.y;

        if (Math.abs(xVel) > TERMINAL_VELOCITY_X) {
            xVel = xVel < 0 ? -TERMINAL_VELOCITY_X : TERMINAL_VELOCITY_X;
        }

        if (Math.abs(yVel) > TERMINAL_VELOCITY_Y) {
            yVel = yVel < 0 ? -TERMINAL_VELOCITY_Y : TERMINAL_VELOCITY_Y;
        }

        //float orientation = getRotation() * 180f / (float) Math.PI;
        //delta is multiplied by 100 since it will normally be very low
        return new Vector2f(getPosition().x + (applyXVel ? xVel * delta * 100 : 0), getPosition().y + (applyYVel ? yVel * delta * 100 : 0));
    }

    /**
     * Checks whether the specified hitbox collides with the hitbox of the other specified entity
     *
     * @param hitbox the hitbox to compare against the other entity's
     * @param entity the entity to compare against
     * @return true if there is collision with the other entity
     */
    public boolean collidesWith(FloatRect hitbox, GameEntity entity) {
        if (globalHitbox == null) return false;

        if (hitbox == null) hitbox = this.globalHitbox; //use our existing hitbox if no other one is supplied

        return entity.globalHitbox != null && hitbox.intersection(entity.globalHitbox) != null;
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        super.draw(renderTarget, renderStates);

        if (displayHitboxes && globalHitbox != null) {
            RectangleShape rs = new RectangleShape();
            rs.setOutlineColor(Color.RED);
            rs.setOutlineThickness(3f);
            rs.setFillColor(new Color(0, 0, 0, 0));
            rs.draw(renderTarget, renderStates);
            rs.setSize(new Vector2f(globalHitbox.width, globalHitbox.height));
            rs.setPosition(new Vector2f(globalHitbox.left, globalHitbox.top));
            rs.draw(renderTarget, renderStates);
        }
    }

    /**
     * TODO this
     *
     * @param collidesWith
     * @return true if collision should be counted!!!
     */
    public boolean handleCollision(GameEntity collidesWith) {
        return true;
    }

    public boolean isIgnoresCollisions() {
        return ignoresCollisions;
    }

    public void setIgnoresCollisions(boolean ignoresCollisions) {
        this.ignoresCollisions = ignoresCollisions;
    }
}
