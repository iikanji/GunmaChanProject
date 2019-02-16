package asu.gunma.ui.screen.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import asu.gunma.speech.ActionResolver;

public class FlashcardScreen implements Screen {

    private Game game;
    public ActionResolver speechGDX;
    public Music music;

    private Stage stage;

    public FlashcardScreen (Game game, ActionResolver speechGDX, Screen previous) {
        this.game = game;
        this.speechGDX = speechGDX;
        this.music = music;
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(1, .8f, 1, 1);
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        //batch.begin();
        //batch.draw(texture, Gdx.graphics.getWidth()/2 - texture.getWidth()/4 + 400, Gdx.graphics.getHeight()/4 - texture.getHeight()/2 + 400, texture.getWidth()/2, texture.getHeight()/2);
        //batch.end();

        stage.act(delta); // optional to pass delta value
        stage.draw();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
