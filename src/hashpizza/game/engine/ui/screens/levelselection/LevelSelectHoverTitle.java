package hashpizza.game.engine.ui.screens.levelselection;

import hashpizza.game.engine.GameObject;
import hashpizza.game.engine.saving.GameSaveState;
import hashpizza.game.engine.util.Animation;
import hashpizza.game.engine.util.Animations;
import hashpizza.game.engine.util.Fonts;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * Class for the bubble which displays above selected levels, showing their name and completed time
 */
public class LevelSelectHoverTitle implements GameObject {

    /**
     * Bubble for completed levels
     */
    private static final ConstTexture TEXTURE_BUBBLE_COMPLETE = Textures.getTexture("./res/misc/ui/speech_bubble_complete.png");

    /**
     * Bubble for incomplete levels
     */
    private static final ConstTexture TEXTURE_BUBBLE_NOT_COMPLETE = Textures.getTexture("./res/misc/ui/speech_bubble.png");

    /**
     * The node that the bubble is tied to
     */
    private LevelMapNode nodePosition;

    /**
     * The title for the level
     */
    private Text levelTitle;

    /**
     * The text to display on the second line of the bubble
     */
    private Text levelCompletionTime;

    /**
     * The icon for the bubble
     */
    private Sprite levelIcon;

    /**
     * The speech bubble sprite
     */
    private Sprite speechBubble;

    /**
     * Animations for smoothly moving the bubble from point to point when switching levels
     */
    private Animation posAnimationX, posAnimationY;

    /**
     * Handles the animation of the bubble floating up and down slightly
     */
    private Animation floatAnimation = new Animation(5, -5f, 6f, true, Animations.EASE_IN_OUT);

    /**
     * Creates the level selection hover title
     *
     * @param nodePosition the position of the node that the buuble is showing data about
     */
    public LevelSelectHoverTitle(LevelMapNode nodePosition) {

        levelTitle = new Text();
        levelTitle.setFont(Fonts.MEDIEVAL);
        levelTitle.setColor(Color.BLACK);

        levelCompletionTime = new Text();
        levelCompletionTime.setFont(Fonts.PIXEL);
        levelCompletionTime.setColor(Color.BLACK);

        speechBubble = new Sprite(TEXTURE_BUBBLE_NOT_COMPLETE);
        //centre about the middle of the speech bubble
        speechBubble.setOrigin(speechBubble.getLocalBounds().width * 0.5f, speechBubble.getLocalBounds().height);

        setNodePosition(nodePosition);
    }

    /**
     * @return the position of the node
     */
    public LevelMapNode getNodePosition() {
        return nodePosition;
    }

    /**
     * Sets the node position and moves the bubble/updates the text in it accordingly
     *
     * @param nodePosition the new node position to move to
     */
    public void setNodePosition(LevelMapNode nodePosition) {

        //check if this level has been completed in the save state, it is -1 if it hasn't been completed
        long completionTime = GameSaveState.getSaveState().getLevelCompletionTimes().getOrDefault(nodePosition.getLevel().meta.title.toLowerCase(), -1L);

        String completeMessage = "Not completed";

        if (completionTime > -1) {
            int mins = (int) (completionTime / 60000);
            int secs = (int) ((completionTime / 1000) % 60); //format mm:ss

            completeMessage = String.format("Best time: %d:%02d", mins, secs);

            speechBubble.setTexture(TEXTURE_BUBBLE_COMPLETE);
        } else {
            speechBubble.setTexture(TEXTURE_BUBBLE_NOT_COMPLETE);
        }

        levelTitle.setString(nodePosition.getLevel().meta.title);
        levelCompletionTime.setString(completeMessage);

        levelIcon = new Sprite(nodePosition.getAbilityIcon());

        if (this.nodePosition == null) { //handle animations to smoothly move the bubble
            posAnimationX = new Animation(nodePosition.getPosition().x, nodePosition.getPosition().x, 0.1f, false, Animations.LINEAR);
            posAnimationY = new Animation(nodePosition.getPosition().y, nodePosition.getPosition().y, 0.1f, false, Animations.LINEAR);

        } else {
            posAnimationX = new Animation(this.nodePosition.getPosition().x, nodePosition.getPosition().x, 0.2f, false, Animations.EASE_OUT);
            posAnimationY = new Animation(this.nodePosition.getPosition().y, nodePosition.getPosition().y, 0.2f, false, Animations.EASE_OUT);
        }

        this.nodePosition = nodePosition;
    }

    @Override
    public void update(float delta) {
        if (posAnimationX != null && !posAnimationX.isComplete()) {
            //if the animation is in progress, use it to update the bubble's position

            float posX = posAnimationX.update(delta);
            float posY = posAnimationY.update(delta);

            Vector2f pos = new Vector2f(posX, posY);

            speechBubble.setPosition(Vector2f.add(pos, new Vector2f(34, 0)));
            levelTitle.setPosition(Vector2f.add(pos, new Vector2f(-110, -120)));
            levelCompletionTime.setPosition(Vector2f.add(pos, new Vector2f(-110, -85)));
            levelIcon.setPosition(Vector2f.add(pos, new Vector2f(-160, -100)));

        } else {
            posAnimationX = null;
            posAnimationY = null;
        }

        //for the floaty-bubble animation
        Vector2f floatOffset = new Vector2f(0, floatAnimation.update(delta));

        speechBubble.setOrigin(Vector2f.add(new Vector2f(speechBubble.getLocalBounds().width * 0.5f, speechBubble.getLocalBounds().height), floatOffset));
        levelTitle.setOrigin(floatOffset);
        levelCompletionTime.setOrigin(floatOffset);
        levelIcon.setOrigin(floatOffset);
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        speechBubble.draw(renderTarget, renderStates);
        levelTitle.draw(renderTarget, renderStates);
        levelCompletionTime.draw(renderTarget, renderStates);
        levelIcon.draw(renderTarget, renderStates);
    }
}
