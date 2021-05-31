package hashpizza.game;

import hashpizza.game.engine.GameWindow;
import hashpizza.game.engine.util.GridUtils;

/**
 * Bootstrap to run the game
 */
public class Main {

    public static void main(String[] args) {
        new GameWindow(GridUtils.SCREEN_WIDTH, GridUtils.SCREEN_HEIGHT);
    }
}