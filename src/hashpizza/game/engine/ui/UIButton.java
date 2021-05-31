package hashpizza.game.engine.ui;

import hashpizza.game.engine.GameObject;
import hashpizza.game.engine.util.Animation;
import hashpizza.game.engine.util.Animations;
import hashpizza.game.engine.util.Fonts;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * Represents a button that can have a corresponding listener for when it is selected. Should be used in conjunction with
 * a ButtonController in order to process key-binds
 */
public class UIButton implements GameObject {

    /**
     * The visible state of the button
     */
    private ButtonState state = ButtonState.NORMAL;

    /**
     * A callback used for handling when the button is pressed
     */
    private UIListener handler;

    /**
     * Button background rectangle
     */
    private RectangleShape buttonBackground;

    /**
     * Button outline, for when it is selected
     */
    private RectangleShape buttonSelectionOutline;

    /**
     * The text on the button
     */
    private Text buttonText;

    /**
     * Animation to pulse the outline if the button is selected
     */
    private Animation colourAnim = new Animation(128f, 255f, 2f, true, Animations.EASE_IN_OUT);

    /**
     * Creates a button with the specified parameters
     *
     * @param caption  the text to display on the button
     * @param handler  the callback to handle clicking on the button
     * @param position the location of the button
     * @param size     the size of  the button
     */
    public UIButton(String caption, UIListener handler, Vector2f position, Vector2f size) {
        buttonBackground = new RectangleShape(size);
        buttonBackground.setFillColor(ButtonState.NORMAL.colour);
        buttonBackground.setOrigin(buttonBackground.getLocalBounds().width * 0.5f, buttonBackground.getLocalBounds().height * 0.5f);
        buttonBackground.setPosition(position);

        buttonSelectionOutline = new RectangleShape(Vector2f.add(size, new Vector2f(5, 5)));
        buttonSelectionOutline.setFillColor(new Color(0, 0, 0, 0)); //no fill
        buttonSelectionOutline.setOutlineThickness(3);
        buttonSelectionOutline.setOutlineColor(ButtonState.HOVER.colour);
        buttonSelectionOutline.setOrigin(buttonSelectionOutline.getLocalBounds().width * 0.5f, buttonSelectionOutline.getLocalBounds().height * 0.5f);
        buttonSelectionOutline.setPosition(Vector2f.add(position, new Vector2f(3, 3)));

        buttonText = new Text();
        buttonText.setCharacterSize(48);
        buttonText.setColor(new Color(55, 71, 79));
        buttonText.setFont(Fonts.PIXEL);
        buttonText.setCharacterSize(28);
        buttonText.setString(caption);
        buttonText.setOrigin(buttonText.getLocalBounds().width * 0.5f, buttonText.getLocalBounds().height * 0.5f);
        buttonText.setPosition(new Vector2f(position.x, position.y - 10));

        this.handler = handler;
    }

    @Override
    public void update(float delta) {
        if (state == ButtonState.HOVER) {
            float anim = colourAnim.update(delta);
            buttonSelectionOutline.setOutlineColor(new Color(state.colour.r, state.colour.g, state.colour.b, (int) anim));
        }
    }

    /**
     * Sets the current state (and therefore colour) of the button
     *
     * @param state the new state
     */
    public void setState(ButtonState state) {
        this.state = state;

        buttonBackground.setFillColor(state.colour);

        if (state != ButtonState.HOVER) {
            buttonSelectionOutline.setOutlineColor(state.colour);
        }

        colourAnim.reset();
    }

    /**
     * @return the UI callback handler for handling clicking the button
     */
    public UIListener getHandler() {
        return handler;
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        buttonBackground.draw(renderTarget, renderStates);
        buttonText.draw(renderTarget, renderStates);

        if (state != ButtonState.NORMAL) {
            buttonSelectionOutline.draw(renderTarget, renderStates);
        }
    }

    /**
     * The different states that the button can be in
     */
    public enum ButtonState {
        PRESSED(new Color(213, 21, 88)), HOVER(new Color(233, 30, 99)), NORMAL(new Color(255, 255, 255));

        /**
         * The colour of the button background in this state
         */
        Color colour;

        /**
         * Creates a button state with the specified colour
         *
         * @param colour the colour of the button state
         */
        ButtonState(Color colour) {
            this.colour = colour;
        }
    }
}
