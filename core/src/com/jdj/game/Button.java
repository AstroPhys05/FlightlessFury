package com.jdj.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Jose on 2014-12-04.
 */
//Buttons
//http://stackoverflow.com/questions/21488311/libgdx-how-to-create-a-button
//https://github.com/libgdx/libgdx/wiki/Texture-packer
//http://gamedev.stackexchange.com/questions/60123/registering-inputlistener-in-libgdx
public class Button {
    TextButton button;// the libgdx buttons LAUNCH and RESET
    Boolean pressed = false;//set the default pressed to false
    TextButton.TextButtonStyle style;//make a textbutton style : fonts, etc.
    Texture texture;
    BitmapFont font;
    Skin skin;//uses texture atlas to create skin
    TextureAtlas buttonAtlas;//the texture atlas of the button
    int nWidth = Gdx.graphics.getWidth();

    Button(String sText,float xPos, float yPos) {
        skin = new Skin();
        texture = new Texture(Gdx.files.internal("LiberationMono.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
        font = new BitmapFont(Gdx.files.internal("LiberationMono.fnt"), new TextureRegion(texture), false);
        font.setScale(1f, 1f);//scale to other devices - need to test it
        buttonAtlas = new TextureAtlas("buttons.pack");//the button's texture atlas uses the image file
        skin.addRegions(buttonAtlas);//add the texture atlas to the button skin
        style = new TextButton.TextButtonStyle();//set the style using the font
        style.font = font;
        style.up = skin.getDrawable("Launch_Pressed");//the .pack file has defines individual image files for each setting
        style.down = skin.getDrawable("Launch_Unpressed");
        style.checked = skin.getDrawable("Launch_Pressed");
        style.font.setColor(Color.WHITE);//Button text color = white
        button = new TextButton(sText, style);//Set the text and pass the style
        button.setSize(nWidth / 7, nWidth / 7);//set the size to 1/7th the screen width to keep it scaling
        button.setPosition(xPos, yPos);//Set it to the passed coordinates
        button.addListener(new InputListener() {//listener for button
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pressed = false;
            }
        });
    }
}
