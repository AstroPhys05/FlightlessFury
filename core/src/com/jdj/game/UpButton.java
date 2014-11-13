package com.jdj.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class UpButton extends ApplicationAdapter {

    //SpriteBatch batch;
    //Sprite image;
    //Sprite image2;
    //Texture img;

    /*public void make () {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        image2 = new Sprite();
    }*/

    /*public void draw () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }*/
    Penguin penguin;


    Stage stage;
    TextButton button;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;

    @Override
    public void create() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        skin = new Skin();
        buttonAtlas = new TextureAtlas("button.pack");
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("Button_Pressed");
        textButtonStyle.down = skin.getDrawable("Button_Released");
        textButtonStyle.checked = skin.getDrawable("Button_Pressed");
        button = new TextButton("", textButtonStyle);
        stage.addActor(button);
        /*button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                penguin = new Penguin();
                penguin.create();
                penguin.image2.setRotation(90);
            }
        });*/
    }


    public void render() {
        super.render();
        stage.draw();
        //batch.begin();
        //batch.draw(img, 0, 0);
        //batch.end();
    }
}