package hashpizza.game.engine.ui;

import hashpizza.game.engine.GameObject;
import hashpizza.game.engine.KeyHandler;
import hashpizza.game.engine.util.Sounds;
import org.jsfml.audio.Sound;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.window.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Button controllers hold all of the buttons that can be displayed on a game screen, to allow for up and down key
 * handling and enter key selection etc.
 */
public class ButtonController implements GameObject, KeyHandler {

    /**
     * Selected button change audio
     */
    private static final Sound SOUND_BUTTON_SELECT = Sounds.getSound("./res/audio/interface/next_select.wav");

    /**
     * The buttons that will be displayed on the screen
     */
    private List<UIButton> buttons = new ArrayList<>();

    /**
     * The index of the currently selected button
     */
    private int selectedIndex = 0;

    /**
     * Whether enter (button select key) is pressed
     */
    private boolean isEnterPressed = false;

    /**
     * Adds the specified button to the screen
     *
     * @param button the button to add
     * @return this button controller
     */
    public ButtonController addButton(UIButton button) {
        buttons.add(button);

        if (buttons.size() == 1) { //the first button should be selected
            button.setState(UIButton.ButtonState.HOVER);
        }

        return this;
    }

    /**
     * Removes the specified button from this screen
     *
     * @param button the button to remove
     */
    public void removeButton(UIButton button) {
        buttons.remove(button);

        if (buttons.size() >= selectedIndex) {
            selectedIndex = 0;
        }
    }

    @Override
    public void update(float delta) {
        buttons.forEach(b -> b.update(delta));
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        buttons.forEach(b -> b.draw(renderTarget, renderStates));
    }

    /**
     * Updates the selection based on which button is currently selected
     */
    private void updateSelection() {
        SOUND_BUTTON_SELECT.play();

        int ix = 0;
        for (UIButton b : buttons) {
            if (ix == selectedIndex) {
                b.setState(isEnterPressed ? UIButton.ButtonState.PRESSED : UIButton.ButtonState.HOVER);
            } else {
                b.setState(UIButton.ButtonState.NORMAL);
            }
            ix++;
        }
    }

    @Override
    public void onKeyPress(Keyboard.Key key) {
        if (key == Keyboard.Key.DOWN || key == Keyboard.Key.S) {
            selectedIndex++;

            if (selectedIndex >= buttons.size()) selectedIndex = 0; //loop round to first button
            updateSelection();
        } else if (key == Keyboard.Key.UP || key == Keyboard.Key.W) {
            selectedIndex--;

            if (selectedIndex < 0) selectedIndex = buttons.size() - 1; //loop round to last button
            updateSelection();
        } else if (key == Keyboard.Key.RETURN) {
            isEnterPressed = true;
            updateSelection();
        }
    }

    @Override
    public void onKeyRelease(Keyboard.Key key) {
        if (key == Keyboard.Key.RETURN) {
            isEnterPressed = false;
            if (!buttons.isEmpty()) {
                buttons.get(selectedIndex).getHandler().handleEvent();
                updateSelection();
            }
        }
    }
}
