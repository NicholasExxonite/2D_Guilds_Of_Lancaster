package hashpizza.game.engine.util;

/**
 * Class to allow for animation keyframes to be calculated based on a start and end value. Will loop the animation backwards
 * after it is completed, repeating infinitely if 'shouldInvert' is true
 */
public class Animation {

    /**
     * The start value of this animation
     */
    private final float startValue;

    /**
     * The end value of this animation
     */
    private final float endValue;

    /**
     * The amount of time this animation will take to complete
     */
    private final float timeToComplete;

    /**
     * The animation type (easing etc.) to use
     */
    private final Animations animationType;

    /**
     * The amount of progress currently made on this animation
     */
    private float progress;

    /**
     * Whether this animation is currently going forwards or is inverting
     */
    private boolean forward = true;

    /**
     * Whether or not this animation will invert and loop, or only run once
     */
    private boolean shouldInvert;

    /**
     * Creates an animation with the specified parameters
     *
     * @param startValue     the initial value to be animated
     * @param endValue       the end value when the animation is completed
     * @param timeToComplete the amount of time to animate the animation in one direction
     * @param shouldInvert   whether or not the animation should auto invert/loop
     * @param animationType  the type of animation to use (easing etc. applied)
     */
    public Animation(float startValue, float endValue, float timeToComplete, boolean shouldInvert, Animations animationType) {

        this.startValue = startValue;
        this.endValue = endValue;
        this.timeToComplete = timeToComplete;
        this.shouldInvert = shouldInvert;
        this.animationType = animationType;
    }

    /**
     * Updates the value for the element being animated with the specified delta time
     *
     * @param delta delta time to update by
     * @return the new value for this animation
     */
    public float update(float delta) {
        progress += delta;
        if (progress >= timeToComplete) { //reverse the animation
            if (!shouldInvert) return endValue;

            progress -= timeToComplete;
            forward = !forward;
        }

        return animationType.calculate(forward ? startValue : endValue, forward ? endValue : startValue, progress / timeToComplete);
    }

    /**
     * Gets whether the animation is completed
     *
     * @return true if the animation is complete
     */
    public boolean isComplete() {
        return progress >= timeToComplete;
    }

    /**
     * Resets the animation progress
     */
    public void reset() {
        progress = 0;
        forward = true;
    }

    /**
     * Creates a clone of this animation which flips the start and end values
     *
     * @return the inverse of this animation
     */
    public Animation inverse() {
        return new Animation(endValue, startValue, timeToComplete, shouldInvert, animationType);
    }
}
