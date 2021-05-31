package hashpizza.game.engine.ui;

import hashpizza.game.engine.GameObject;
import hashpizza.game.engine.KeyHandler;
import hashpizza.game.engine.util.*;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

/**
 * Class to handle on-screen dialogue
 */
public class Dialogue implements GameObject, KeyHandler {

    /**
     * Background texture for the dialogue UI
     */
    private static final ConstTexture TEXTURE_BACKGROUND = Textures.getTexture("./res/misc/ui/dialogue.png");

    /**
     * The text displayed in the dialogue box
     */
    private Text dialogueText;

    /**
     * The text displayed in the character name box
     */
    private Text characterName;

    /**
     * The background texture sprite
     */
    private Sprite background;

    /**
     * Effect to darken the rest of the screen when the dialogue UI is active
     */
    private RectangleShape backgroundDarken;

    /**
     * Text animation so text comes in character-by-character
     */
    private Animation textAnimation;

    /**
     * List of all the pages of dialogue
     */
    private DialogueEntry[] entries;

    /**
     * The index of the current dialogue entry to display
     */
    private int currentDialogue = -1;

    /**
     * Callback to handle when the dialogue is complete
     */
    private UIListener completionHandler;

    /**
     * Animation to fade the background darker when the UI is active
     */
    private Animation backgroundDarkenOpacityAnimation = new Animation(0, 40, 3f, false, Animations.EASE_OUT);

    /**
     * Creates a dialogue UI with the specified dialogue entries
     *
     * @param entries the text entry pages to display
     */
    public Dialogue(DialogueEntry[] entries) {
        this.entries = entries;

        dialogueText = new Text();
        characterName = new Text();

        dialogueText.setFont(Fonts.PIXEL);
        characterName.setFont(Fonts.MEDIEVAL);

        dialogueText.setColor(Color.BLACK);

        background = new Sprite(TEXTURE_BACKGROUND);
        background.setColor(new Color(255, 255, 255, 220));

        background.setPosition((GridUtils.SCREEN_WIDTH - background.getLocalBounds().width) / 2, GridUtils.SCREEN_HEIGHT - 30 - background.getLocalBounds().height);

        characterName.setPosition(60, 800);

        dialogueText.setPosition(60, 875);

        Texture t = (Texture) Fonts.PIXEL.getTexture(40);
        t.setSmooth(false); //helps make the text smoother

        dialogueText.setCharacterSize(40);

        backgroundDarken = new RectangleShape(new Vector2f(GridUtils.SCREEN_WIDTH, GridUtils.SCREEN_HEIGHT));
        backgroundDarken.setFillColor(new Color(0, 0, 0, 0));

        showNextLine();
    }

    /**
     * Sets the callback handler for when the dialogue has finished
     *
     * @param handler the event handler
     */
    public void onComplete(UIListener handler) {
        completionHandler = handler;
    }

    /**
     * Shows and animates the next line of dialogue
     */
    public void showNextLine() {
        currentDialogue++;

        if (isComplete()) { //call our callback if we're done
            if (completionHandler != null) {
                completionHandler.handleEvent();
            }

            return;
        }

        DialogueEntry currentEntry = entries[currentDialogue];

        //animation length depends on how long the actual text is
        textAnimation = new Animation(0, currentEntry.getText().length(), 0.01f * currentEntry.getText().length(), false, Animations.LINEAR);

        characterName.setString(entries[currentDialogue].getFrom());
    }

    /**
     * @return if all of the dialogue (for each page) has been displayed
     */
    public boolean isComplete() {
        return currentDialogue >= entries.length;
    }

    @Override
    public void update(float delta) {
        if (isComplete()) return;

        int length = (int) textAnimation.update(delta);
        String fullText = entries[currentDialogue].getText();
        dialogueText.setString(fullText.substring(0, length));

        float bgDark = backgroundDarkenOpacityAnimation.update(delta);
        backgroundDarken.setFillColor(new Color(0, 0, 0, (int) bgDark));
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        backgroundDarken.draw(renderTarget, renderStates);
        background.draw(renderTarget, renderStates);
        dialogueText.draw(renderTarget, renderStates);
        characterName.draw(renderTarget, renderStates);
    }

    @Override
    public void onKeyPress(Keyboard.Key key) {
        if (key == Keyboard.Key.RETURN) {
            showNextLine();
        }
    }

    @Override
    public void onKeyRelease(Keyboard.Key key) {

    }
}
