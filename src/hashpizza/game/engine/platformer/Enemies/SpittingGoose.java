package hashpizza.game.engine.platformer.Enemies;

import hashpizza.game.engine.GameCanvas;
import hashpizza.game.engine.GameSprite;
import hashpizza.game.engine.platformer.Level;
import hashpizza.game.engine.util.Vector;

import java.awt.*;

public class SpittingGoose extends GameSprite {
    private boolean isAlive;
    private Level level;
    private int x = 200, y = 400;

    public SpittingGoose(GameCanvas canvas, Level level)
    {
        super(canvas, "player.png", 64, 64);
        super.position = new Vector(x, y);
        this.level = level;
        isAlive = true;

        hitbox = new Rectangle((int) position.getX(), (int)position.getY(), width, height);
    }


    @Override
    public void update(long delta) {
        //stationary enemy
        velocity.setX(0);
        velocity.setY(0);

        position.add(velocity);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

