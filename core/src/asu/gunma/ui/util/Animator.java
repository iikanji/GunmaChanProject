package asu.gunma.ui.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {

    private Animation<TextureRegion> animation;
    private Texture spriteSheet;
    private float stateTime;

    private TextureRegion currentFrame;
    private TextureRegion idleFrame;

    public Animator(String fileName, int columns, int rows, float frameDuration) {

        this.spriteSheet = new Texture(Gdx.files.internal(fileName));
        TextureRegion[][] tmp = TextureRegion.split(this.spriteSheet,
                this.spriteSheet.getWidth() / columns,
                this.spriteSheet.getHeight() / rows);
        TextureRegion[] frames = new TextureRegion[columns * rows];

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        this.idleFrame = frames[0];

        this.animation = new Animation<TextureRegion>(frameDuration, frames);
        this.stateTime = 0f;
        this.currentFrame = null;
    }

    public TextureRegion getCurrentFrame(float delta) {
        this.stateTime += delta;
        this.currentFrame = animation.getKeyFrame(stateTime, true);
        return this.currentFrame;
    }

    public TextureRegion getIdleFrame() {
        return this.idleFrame;
    }

    public void dispose() {
        this.spriteSheet.dispose();
    }
}
