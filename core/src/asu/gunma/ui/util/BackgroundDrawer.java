package asu.gunma.ui.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class BackgroundDrawer {

    private SpriteBatch batch;
    private Texture grass;
    private Texture mountain;
    private Texture sky;
    private Texture cloud1;
    private Texture cloud2;

    private Random random;
    private double grassPosition;
    private double mountainPosition;
    private double cloud1Position;
    private double cloud2Position;

    private final double CLOUD_X_MAX = 1024;
    private final double CLOUD_Y_MAX = 600;

    public BackgroundDrawer(SpriteBatch batch) {
        this.batch = batch;
        this.grass = new Texture("background/grassx2.png");
        this.mountain = new Texture("background/mountainx3.png");
        this.sky = new Texture("background/skyx2.png");
        this.cloud1 = new Texture("background/cloud1.png");
        this.cloud2 = new Texture("background/cloud2.png");

        this.random = new Random();
        this.grassPosition = 0.0;
        this.mountainPosition = 100.0;
        this.cloud1Position = 0 + (CLOUD_X_MAX - 0) * random.nextDouble();
        this.cloud2Position = 0 + (CLOUD_X_MAX - 0) * random.nextDouble();
    }

    void show () {

    }

    public void render(boolean isPaused, boolean isGameOver) {
        if (!isPaused && !isGameOver) {
            this.grassPosition -= 0.6;
            this.mountainPosition -= 0.025;
            this.cloud1Position -= 0.2;
            this.cloud2Position -= 0.2;
        }

        this.batch.draw(this.sky,0, 0);
        this.batch.draw(this.mountain, (int) this.mountainPosition, 0);
        this.batch.draw(this.grass, (int) this.grassPosition, 0);
        if (this.grassPosition < -600) {
            this.grassPosition = 0;
        }
        if (this.cloud1Position < -300) {
            this.cloud1Position = 1000;
        }
        if (this.cloud2Position < -300) {
            this.cloud2Position = 1000;
        }
        this.batch.draw(this.cloud1, (int) this.cloud1Position, 340);

        this.batch.draw(this.cloud2, (int) this.cloud2Position, 410);
    }

    public void dispose() {
        this.grass.dispose();
        this.mountain.dispose();
        this.sky.dispose();
        this.cloud1.dispose();
        this.cloud2.dispose();
    }

}
