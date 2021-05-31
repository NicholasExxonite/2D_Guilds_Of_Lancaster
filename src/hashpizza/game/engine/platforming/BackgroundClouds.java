package hashpizza.game.engine.platforming;

import hashpizza.game.engine.GameObject;
import hashpizza.game.engine.util.GridUtils;
import hashpizza.game.engine.util.Textures;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * Background scrolling clouds
 */
public class BackgroundClouds implements GameObject {

    /**
     * Cloud texture
     */
    private static final ConstTexture TEXTURE_CLOUDS = Textures.getTexture("./res/misc/clouds.png");

    /**
     * Two sprites to allow for seamless scrolling
     */
    private Sprite clouds1, clouds2;

    /**
     * For the cloud scrolling
     */
    private float cloudOffset = 0;

    /**
     * Creates background clouds
     */
    public BackgroundClouds() {
        clouds1 = new Sprite(TEXTURE_CLOUDS);
        clouds1.setColor(new Color(255, 255, 255, 128));
        clouds2 = new Sprite(TEXTURE_CLOUDS);
        clouds2.setColor(new Color(255, 255, 255, 128));
        clouds2.setOrigin(new Vector2f(GridUtils.SCREEN_WIDTH, 0));
    }

    @Override
    public void update(float delta) {
        cloudOffset -= delta * 5f;

        if (cloudOffset < -GridUtils.SCREEN_WIDTH) {
            cloudOffset += GridUtils.SCREEN_WIDTH;
        }

        Vector2f pos = new Vector2f(cloudOffset, 0);

        clouds1.setPosition(pos);
        clouds2.setPosition(pos);
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        clouds1.draw(renderTarget, renderStates);
        clouds2.draw(renderTarget, renderStates);
    }
}
