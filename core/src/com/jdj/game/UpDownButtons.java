package com.jdj.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Me on 27/11/2014.
 */
public class UpDownButtons implements ApplicationListener {
    Penguin penguin;

    private Stage Stage;
    private TextButton UpButton, DownButton;
    private TextButton.TextButtonStyle UpTextButtonStyle, DownTextButtonStyle;
    private BitmapFont Font;
    private Skin UpSkin, DownSkin;
    private TextureAtlas UpButtonAtlas, DownButtonAtlas;
    boolean bUpPressed = false, bDownPressed = false;

    public void setPenguin(Penguin penguin_){
        penguin = penguin_;
    }

    public void create() {
        Stage = new Stage();
        Gdx.input.setInputProcessor(Stage);
        Font = new BitmapFont();

        UpSkin = new Skin();
        UpButtonAtlas = new TextureAtlas("UpButtons.atlas");
        UpSkin.addRegions(UpButtonAtlas);
        UpTextButtonStyle = new TextButton.TextButtonStyle();
        UpTextButtonStyle.font = Font;
        UpTextButtonStyle.up = UpSkin.getDrawable("upbuttonpressed");                               //http://stackoverflow.com/questions/21488311/libgdx-how-to-create-a-button
        UpTextButtonStyle.down = UpSkin.getDrawable("upbuttonunpressed");
        UpTextButtonStyle.checked = UpSkin.getDrawable("upbuttonpressed");
        UpButton = new TextButton("", UpTextButtonStyle);
        Stage.addActor(UpButton);
        UpButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                bUpPressed = true;
                return true;                                                                        //We got the action listener from the following website...
            }                                                                                       //http://gamedev.stackexchange.com/questions/60123/registering-inputlistener-in-libgdx
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                bUpPressed = false;
            }
        });

        DownSkin = new Skin();
        DownButtonAtlas = new TextureAtlas("DownButtons.atlas");
        DownSkin.addRegions(DownButtonAtlas);
        DownTextButtonStyle = new TextButton.TextButtonStyle();
        DownTextButtonStyle.font = Font;
        DownTextButtonStyle.up = DownSkin.getDrawable("downbuttonpressed");                         //We learned to add buttons from the following website...
        DownTextButtonStyle.down = DownSkin.getDrawable("downbuttonunpressed");                     //http://stackoverflow.com/questions/21488311/libgdx-how-to-create-a-button
        DownTextButtonStyle.checked = DownSkin.getDrawable("downbuttonpressed");
        DownButton = new TextButton("", DownTextButtonStyle);
        DownButton.setPosition(Gdx.graphics.getWidth()-UpButton.getWidth(),0);
        Stage.addActor(DownButton);
        DownButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                bDownPressed = true;
                return true;
            }                                                                                       //We got the action listener from the following website...
            @Override                                                                               //http://gamedev.stackexchange.com/questions/60123/registering-inputlistener-in-libgdx
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                bDownPressed = false;
            }
        });
    }

    @Override
    public void resize(int width, int height) {

    }

    public void render() {
        if (bUpPressed){
            penguin.peng.rotate((float)3);
        }else if (bDownPressed){
            penguin.peng.rotate((float)-3);
        }
        Stage.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
