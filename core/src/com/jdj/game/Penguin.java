package com.jdj.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Me on 10/11/2014.
 */
public class Penguin {
    SpriteBatch batch;
    Sprite peng;
    Texture img;

    public void create () {
        batch = new SpriteBatch();
        img = new Texture("penguin.png");
        peng = new Sprite(img);
        peng.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
    }

    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        peng.draw(batch);
        batch.end();
    }
}
