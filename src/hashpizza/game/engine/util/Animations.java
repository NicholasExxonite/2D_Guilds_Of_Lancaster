package hashpizza.game.engine.util;

/**
 * Utility class to generate animations for use within the GUI and other areas of the game
 */
public enum Animations {

    EASE_OUT((startValue, endValue, progress) -> {
        endValue -= startValue; //to just get the amount to change by
        progress--;
        return (((progress * progress * progress) + 1) * endValue) + startValue;
    }),
    EASE_IN_OUT((startValue, endValue, progress) -> {
        endValue -= startValue;
        progress += progress;

        if (progress < 1) { //first half of animation
            return (endValue * 0.5f) * progress * progress * progress + startValue;
        }

        progress -= 2;
        return (endValue * 0.5f) * (progress * progress * progress + 2) + startValue;
    }),
    LINEAR(((startValue, endValue, progress) -> startValue + ((endValue - startValue) * progress)));

    private final AnimationCalculator calculator;

    /**
     * Creates a new animation enum with the specified calculations to generate the correct easing
     *
     * @param calculator the animation calculator for this type of animation
     */
    Animations(AnimationCalculator calculator) {

        this.calculator = calculator;
    }

    /**
     * Calculates the value that the animation should be at with the specified parameters
     *
     * @param startValue the start value for the animation
     * @param endValue   the end value for the animation
     * @param progress   the progress (between 0 and 1) that this animation is to being completed
     * @return the value that the animation is currently at
     */
    public float calculate(float startValue, float endValue, float progress) {
        return calculator.calculateValue(startValue, endValue, Math.max(Math.min(progress, 1f), 0f));
    }

    /**
     * Interface implemented by animations to calculate values at the specified progress (between 0.0 and 1.0), with
     * the specified starting and end values
     */
    interface AnimationCalculator {

        /**
         * Calculates the animation progression at the specified progress, with the specified start and end values
         *
         * @param startValue the starting value
         * @param endValue   the ending value
         * @param progress   the progression of the animation (0.0 - 1.0)
         * @return the calculated animation value for the specified progress
         */
        float calculateValue(float startValue, float endValue, float progress);
    }
}
