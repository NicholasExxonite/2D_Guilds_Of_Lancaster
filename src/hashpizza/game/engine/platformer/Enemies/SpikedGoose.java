package hashpizza.game.engine.platformer.Enemies;

import hashpizza.game.engine.GameCanvas;
import hashpizza.game.engine.GameSprite;
import hashpizza.game.engine.platformer.Level;
import hashpizza.game.engine.util.Vector;

import java.awt.*;

public class SpikedGoose extends GameSprite {
    private Level level;
    private boolean isDisabled;
    private boolean isAlive, isMoving, goLeft, goRight, isVulnerable;

    public SpikedGoose(GameCanvas canvas, Level level)
    {
        super(canvas, "player.png", 64, 64);

        super.position = new Vector(600, 400);
        this.isAlive = true;
        this.isMoving = true; //We need to determine when it'll be stationary and when moving.
        this.isDisabled = false;

        hitbox = new Rectangle((int) position.getX(), (int) position.getY(), width, height);
    }

    @Override
    public void update(long delta) {
        if(isMoving && isAlive)
        {
            if(position.getX()< 800)
            {
                goRight = true;
            }
            if(position.getX() == 0)
            {
                goLeft = false;
            }
            if(position.getX() == 800)
            {
                goRight = false;
                goLeft = true;
            }
            if(goRight && !goLeft)
            {
                velocity.setX(5);
            }
            else if(!goRight && goLeft)
                velocity.setX(-5);
        }
        else if(!isMoving && isAlive)
        {
            velocity.setX(0);
        }
        position.add(velocity);
    }

    /**
     * A method for the SpikedGoose class. When the spiked goose is stunned, it removes it's 'hat',
     * and is vulnerable to getting jumped on.
     */
    public void stunned()
    {
        //condition for being stunned

        isVulnerable = true;
    }

    /**
     * A Method for the interaction between the spiked goose and the player character. If the goose
     * is stunned and a player jumps on it, it dies.
     * If it's not vulnerable then the player takes damage.
     */
    public void playerContact()
    {
        //if player jumps on it and it's vulnerable, kill it
        if(isVulnerable){
            this.isAlive = false;
        }
        else if(!isVulnerable)
        {
            //the player should take damage.
        }
    }
}
