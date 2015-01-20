package com.jdj.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MainMenuScreen extends ScreenAdapter {
    FlightlessFury game;
    MenuButton bStart, bOptions, bHS;//Start button, Options button, High Scores button
    Stage stage;
    Texture iBg;
    Sprite spBg;
    SpriteBatch batch;
    public MainMenuScreen(FlightlessFury game) {
        batch = new SpriteBatch();
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);//Makes buttons clickable
        bStart = new MenuButton("playgame", Gdx.graphics.getWidth()/3- Gdx.graphics.getWidth() / 10 / 2, Gdx.graphics.getHeight()/3- Gdx.graphics.getWidth() / 10 / 2);
        bHS = new MenuButton("highscores", Gdx.graphics.getWidth()/3*2- Gdx.graphics.getWidth() / 10 / 2, Gdx.graphics.getHeight()/3- Gdx.graphics.getWidth() / 10 / 2);
        bOptions = new MenuButton("settings", Gdx.graphics.getWidth()/2- Gdx.graphics.getWidth() / 10 / 2, Gdx.graphics.getHeight()/3- Gdx.graphics.getWidth() / 10 / 2);
        stage.addActor(bStart.button);
        stage.addActor(bHS.button);
        stage.addActor(bOptions.button);
        iBg = new Texture("backgrounder.png");
        spBg = new Sprite(iBg);

    }

    public void update() {
        if (bStart.pressed) {
            game.setScreen(new GameScreen(game));
        }
    }

    public void draw() {
        Gdx.gl.glClearColor(1, 1, 1, 1);//clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(spBg, 0,0,game.nWidth,game.nHeight);
        batch.end();
        stage.draw();
    }

    @Override
    public void render(float delta) {
        update();
        draw();
        if (bStart.pressed) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void pause() {
    }
}
