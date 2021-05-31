package hashpizza.game.engine.util;

/**
 * Utility class used for setting timers which should last a specific amount of in-game time, measured using delta-time
 */
public class DeltaTimer {

    /**
     * The total length of this timer
     */
    private float duration;

    /**
     * How much of this timer has already elapsed
     */
    private float elapsed;

    /**
     * Creates a delta timer with the specified duration
     *
     * @param duration the duration of the timer
     */
    public DeltaTimer(float duration) {

        this.duration = duration;
    }

    /**
     * Updates the timer with the delta time specified
     *
     * @param delta the time to append to the timer
     * @return true if the timer is now complete
     */
    public boolean update(float delta) {
        elapsed += delta;

        return isComplete();
    }

    /**
     * @return whether or not the timer is complete
     */
    public boolean isComplete() {
        return elapsed >= duration;
    }

    /**
     * @return the time remaining until the timer is complete
     */
    public float getRemainingTime() {
        return isComplete() ? 0 : duration - elapsed;
    }

    /**
     * Resets this timer back to the start
     */
    public void reset() {
        elapsed = 0;
    }
}
