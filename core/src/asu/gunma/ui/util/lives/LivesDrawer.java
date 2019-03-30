package asu.gunma.ui.util.lives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LivesDrawer {

    private SpriteBatch batch;
    private Life[] lives;
    private int livesLeft;

    private Texture activeHeart;
    private Texture inactiveHeart;

    private final int X_POSITION_START = 10;
    private final int Y_POSITION = Gdx.graphics.getHeight() - 35;
    private final int LIVES_START = 5;

    public LivesDrawer(SpriteBatch batch) {
        this.batch = batch;
        this.livesLeft = this.LIVES_START;
        this.lives = new Life[this.LIVES_START];

        for(int i = 0; i < this.lives.length; i++) {
            this.lives[i] = new Life(this.X_POSITION_START + (i*35), this.Y_POSITION);
        }

        this.activeHeart = new Texture("background/life.png");
        this.inactiveHeart = new Texture("background/life_gone.png");
    }

    public void render() {
        for(int i = 0; i < this.lives.length; i++) {
            if (this.lives[i].getActive()) {
                this.batch.draw(this.activeHeart, this.lives[i].getxPos(), this.lives[i].getyPos());
            } else {
                this.batch.draw(this.inactiveHeart, this.lives[i].getxPos(), this.lives[i].getyPos());
            }
        }
    }

    public void takeLife() {
        this.livesLeft--;
        this.lives[this.livesLeft].setActive(false);
    }

    public void dispose() {
        this.activeHeart.dispose();
        this.inactiveHeart.dispose();
    }

}
