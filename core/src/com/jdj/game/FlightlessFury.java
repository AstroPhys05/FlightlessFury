package com.jdj.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Jose on 2015-01-19.
 */
public class FlightlessFury extends Game {
    // used by all screens
    public SpriteBatch batch;
    public int nWidth,nHeight;
    @Override
    public void create () {
        batch = new SpriteBatch();
        setScreen(new MainMenuScreen(this));
        nWidth = Gdx.graphics.getWidth();
        nHeight = Gdx.graphics.getHeight();
    }

    @Override
    public void render() {
        super.render();
    }
}
