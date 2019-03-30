package asu.gunma.ui.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class BackgroundDrawer {

    private SpriteBatch batch;
    private Texture grass;
    private Texture mountain;
    private Texture sky;
    private Texture cloudA1;
    private Texture cloudA2;
    private Texture cloudB1;
    private Texture cloudB2;
    private Texture cloudB3;

    private Random random;
    private double grassPosition;
    private double mountainPosition;
    private double cloudA1Position;
    private double cloudA2Position;
    private double cloudB1Position;
    private double cloudB2Position;
    private double cloudB3Position;

    private final double CLOUD_X_MAX = 1024;
    private final double CLOUD_Y_MAX = 600;

    // This is a final defined in GameScreen
    private int screen_bottom_adjust;

    public BackgroundDrawer(SpriteBatch batch, int screen_bottom_adjust) {
        this.batch = batch;
        this.screen_bottom_adjust = screen_bottom_adjust;

        this.grass = new Texture("background/grassx2.png");
        this.mountain = new Texture("background/mountainx3.png");
        this.sky = new Texture("background/skyx2.png");

        this.cloudA1 = new Texture("background/cloud1.png");
        this.cloudA2 = new Texture("background/cloud1.png");
        this.cloudB1 = new Texture("background/cloud2.png");
        this.cloudB2 = new Texture("background/cloud2.png");
        this.cloudB3 = new Texture("background/cloud2.png");

        this.random = new Random();
        this.grassPosition = 0.0;
        this.mountainPosition = 300.0;
        this.cloudA1Position = 0 + (CLOUD_X_MAX - 0) * random.nextDouble();
        this.cloudA2Position = 0 + (CLOUD_X_MAX - 0) * random.nextDouble();
        this.cloudB1Position = 0 + (CLOUD_X_MAX - 0) * random.nextDouble();
        this.cloudB2Position = 0 + (CLOUD_X_MAX - 0) * random.nextDouble();
        this.cloudB3Position = 0 + (CLOUD_X_MAX - 0) * random.nextDouble();
    }

    void show () {

    }

    public void render(boolean isPaused, boolean isGameOver) {
        if (!isPaused && !isGameOver) {
            this.grassPosition -= 0.6;
            this.mountainPosition -= 0.035;
            this.cloudA1Position -= 0.15;
            this.cloudA2Position -= 0.15;
            this.cloudB1Position -= 0.15;
            this.cloudB2Position -= 0.15;
            this.cloudB3Position -= 0.15;
        }

        this.batch.draw(this.sky,0, this.screen_bottom_adjust);
        this.batch.draw(this.mountain, (int) this.mountainPosition, this.screen_bottom_adjust);
        this.batch.draw(this.grass, (int) this.grassPosition, 0); // This is not adjust; this is why everything else is adjusted
        if (this.grassPosition < -600) {
            this.grassPosition = 0;
        }
        if (this.cloudA1Position < -300) {
            this.cloudA1Position = 1000;
        }
        if (this.cloudA2Position < -300) {
            this.cloudA2Position = 1000;
        }
        if (this.cloudB1Position < -300) {
            this.cloudB1Position = 1000;
        }
        if (this.cloudB2Position < -300) {
            this.cloudB2Position = 1000;
        }
        if (this.cloudB3Position < -300) {
            this.cloudB3Position = 1000;
        }

        this.batch.draw(this.cloudA1, (int) this.cloudA1Position, 350 + this.screen_bottom_adjust);
        this.batch.draw(this.cloudA2, (int) this.cloudA2Position, 500 + this.screen_bottom_adjust);
        this.batch.draw(this.cloudB1, (int) this.cloudB1Position, 500 + this.screen_bottom_adjust);
        this.batch.draw(this.cloudB2, (int) this.cloudB2Position, 410 + this.screen_bottom_adjust);
        this.batch.draw(this.cloudB3, (int) this.cloudB3Position, 410 + this.screen_bottom_adjust);
    }

    public void dispose() {
        this.grass.dispose();
        this.mountain.dispose();
        this.sky.dispose();
        this.cloudA1.dispose();
        this.cloudA2.dispose();
        this.cloudB1.dispose();
        this.cloudB2.dispose();
        this.cloudB3.dispose();
    }

}
