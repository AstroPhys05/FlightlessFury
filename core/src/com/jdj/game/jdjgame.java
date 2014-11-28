// Text Input
//Source: http://tutorial-libgdx-android.blogspot.ca/2014/02/handling-inputs-text.html
//https://github.com/libgdx/libgdx/wiki/File-handling#writing-to-a-file
package com.jdj.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class jdjgame implements ApplicationListener {

    private SpriteBatch batch ;
    private BitmapFont font ;

    private String sInitial;
    private String sDialogue;
    private String sMessage;
    // FileHandle file = Gdx.files.external("file.txt");
    private boolean afficher;
    @Override
    public void create() {


        sInitial = "Your name:";
        sDialogue = "Flightless Fury";
        afficher = false;

        batch = new SpriteBatch();
        font = new BitmapFont();

        Gdx.input.getTextInput(new TextInputListener() {

            @Override
            public void input(String sName) {
                sMessage = "Hello, <"+sName+"> Welcome Flightless Fury  ";
                // file.writeString("welcome",true);
                afficher =true;
            }

            @Override
            public void canceled() {
                sMessage = "I guess you don't want to play..";
                afficher =true;
            }
        }, sDialogue, sInitial);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void render() {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(afficher)
        {
            batch.begin();
            font.draw(batch, sMessage, 10, 200);
            batch.end();
        }
    }

    @Override
    public void resize(int arg0, int arg1) {
    }

    @Override
    public void resume() {
    }
}