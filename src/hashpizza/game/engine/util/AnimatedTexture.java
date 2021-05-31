package hashpizza.game.engine.util;

import org.jsfml.graphics.ConstTexture;

/**
 * Represents a texture that can be animated using specified animation texture frames
 */
public class AnimatedTexture {

    /**
     * The duration (in delta-time) that each frame should last for
     */
    private final float durationPerFrame;

    /**
     * The frames in this animation
     */
    private final ConstTexture[] frames;

    /**
     * The current position of this animation
     */
    private float currentPosition;

    /**
     * The previous frame that was displayed in the last update
     */
    private int prevFrame;

    /**
     * Whether or not this texture has changed since the last update
     */
    private boolean textureHasChanged;

    /**
     * Whether or not this texture animation has completed at least once
     */
    private boolean complete;

    /**
     * Creates an animated texture with the specified parameters
     *
     * @param durationPerFrame the duration that each frame should last
     * @param frames           the frames to display for this animation
     */
    public AnimatedTexture(float durationPerFrame, ConstTexture... frames) {

        this.durationPerFrame = durationPerFrame;
        this.frames = frames;
    }

    /**
     * Jumps immediately to the next frame of animation
     *
     * @return the next frame of this animation
     */
    public ConstTexture nextFrame() {

        return update(durationPerFrame); //add the frame duration to it to force it to jump to next frame
    }

    /**
     * Updates this animated texture with the specified delta time
     *
     * @param delta the time to add to the current position within this animation
     * @return the current texture for this animation
     */
    public ConstTexture update(float delta) {

        currentPosition += delta;

        if (currentPosition > durationPerFrame * frames.length) {
            currentPosition %= durationPerFrame * frames.length; //reset when the animation has completed
            complete = true;
        }

        //the current frame of animation
        int activeFrame = (int) (currentPosition / durationPerFrame);

        textureHasChanged = prevFrame != activeFrame;
        prevFrame = activeFrame;

        if (activeFrame > frames.length) activeFrame = frames.length - 1; //out of bounds bug fix

        return frames[activeFrame];
    }

    /**
     * @return whether or not the texture has changed since the last update
     */
    public boolean textureChanged() {
        return textureHasChanged;
    }

    /**
     * @return whether or not the animation has finished
     */
    public boolean isComplete() {
        return complete;
    }
}
