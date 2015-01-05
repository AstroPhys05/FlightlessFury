package com.jdj.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.math.Matrix4;
//SideScrolling
//https://code.google.com/p/libgdx-users/wiki/ScrollingTexture

//Box2d Physics
//http://gamedevelopment.tutsplus.com/tutorials/how-to-create-a-custom-2d-physics-engine-the-basics-and-impulse-resolution--gamedev-6331
//https://github.com/libgdx/libgdx/wiki/Box2d
//https://www.youtube.com/watch?v=IDZMRDb1A_M#t=812
//http://programmersweb.blogspot.ca/2012/07/simple-libgdx-box2d-bouncing-ball.html

//Buttons
//http://stackoverflow.com/questions/21488311/libgdx-how-to-create-a-button
//https://github.com/libgdx/libgdx/wiki/Texture-packer
//http://gamedev.stackexchange.com/questions/60123/registering-inputlistener-in-libgdx

//OrthoCam
//http://www.gamefromscratch.com/post/2013/11/06/LibGDX-Tutorial-7-Camera-basics.aspx
//https://github.com/libgdx/libgdx/wiki/Orthographic-camera
public class GameScreen extends Game {
    final float fPM = 100f;//convert pixels to meters since box2d uses meters
    Stage stage;
    Button bReset,bLaunch;
    Texture texture;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;
    SpriteBatch batch;
    Sprite spGround;
    Sprite spBg;
    Sprite spPU;
    Texture iGround;
    Texture iBg;
    Texture iPU;
    World world;
    Body groundBody;
    Body PUBody;
    OrthographicCamera camera;
    int nWidth, nHeight;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    float scrollTimer = 0.0f;
    Penguin penguin;
    Accelerometer accelerometer;
    Sound sound1;
    Sound sound2;
    //debugging
    @Override
    public void create() {
        batch = new SpriteBatch();
        stage = new Stage();
        skin = new Skin();
        sound1 = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.ogg"));
        sound2 = Gdx.audio.newSound(Gdx.files.internal("sfx_point.ogg"));
        Gdx.input.setInputProcessor(stage);//Makes the buttons clickable
        texture = new Texture(Gdx.files.internal("LiberationMono.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
        font = new BitmapFont(Gdx.files.internal("LiberationMono.fnt"), new TextureRegion(texture), false);
        font.setScale(1f, 1f);//scale to other devices - need to test it

        buttonAtlas = new TextureAtlas("buttons.pack");
        iGround = new Texture("ground.jpg");
        iBg = new Texture("mario.jpg");
        iPU = new Texture("powerup.png");
        spGround = new Sprite(iGround);
        nWidth = Gdx.graphics.getWidth();//Dimensions of the device
        nHeight = Gdx.graphics.getHeight();
        world = new World(new Vector2(0f,-9.8f), true);//Create the box2d physics world with 0 gravity in the x-direction and -9.8m/s/s in the y direction
        spGround.setSize(nWidth*2, nHeight / 7/3);

        penguin = new Penguin(world);
        iBg.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        spBg = new Sprite(iBg);

        spPU = new Sprite(iPU);


        //Ground body & sprite
        spGround.setPosition(0, 0);//Position of sprite
        BodyDef groundBodyDef = new BodyDef();//Box2d bodydef is used by the body
        groundBodyDef.position.set((spGround.getX() + spGround.getWidth() / 2) / fPM,
                (spGround.getY() + spGround.getHeight() / 2) / fPM);//Set the initial position
        groundBody = world.createBody(groundBodyDef);//Pass the body def to the body
        PolygonShape groundBox = new PolygonShape();//make a box shaped hitbox
        groundBox.setAsBox(spGround.getWidth() / 2 / fPM, spGround.getHeight()
                / 2 / fPM);//set the size using the conversion ratio since box2d uses meters
        groundBody.createFixture(groundBox, 0.0f);//make it fixed

        // Power-UP
        BodyDef PUBodyDef = new BodyDef();
        PUBody = world.createBody(PUBodyDef);//Pass the body def to the body
        PolygonShape PUBox = new PolygonShape();//make a box shaped hitbox
        PUBox.setAsBox(spPU.getWidth() / 2 / fPM, spPU.getHeight()
                / 2 / fPM);//set the size using the conversion ratio since box2d uses meters
        PUBody.createFixture(PUBox, 0.0f);//make it fixed
        //Camera
        camera = new OrthographicCamera(nWidth, nHeight);//libgdx orthographic camera
        camera.position.y = spGround.getY()+camera.viewportHeight/2;//set the position to above the ground

        bLaunch = new Button("LAUNCH",0,nHeight - nWidth / 7);
        bReset = new Button("RESET",nWidth / 7, nHeight - nWidth / 7);
        stage.addActor(bLaunch.button);//add the buttons to the stage
        stage.addActor(bReset.button);
        debugRenderer = new Box2DDebugRenderer();//For Debugging : Shows Boxes around the sprites

    }


    @Override
    public void render() {
        if(bLaunch.pressed){//If the launch is pressed
            penguin.setVelocity(20f,10f);//set the velocity (x,y)
            sound1.play(); //play sound effect for wing





        }
        if(bReset.pressed){//If the reset is pressed
            penguin.ResetPos();//Reset penguin position
            camera.position.y = spGround.getY()+camera.viewportHeight/2;//reset camera position
            scrollTimer = 0f;//reset scrollTimer
            sound2.play(); //play sound effect for point

        }
        penguin.body.setAngularVelocity(-accelerometer.accelY()/3);//set the angular velocity of penguin to the accelerometer value

        groundBody.setTransform(camera.position.x/fPM,groundBody.getPosition().y,0);//keep the ground on the bottom of the camera
        penguin.UpdatePos();//update the sprites position to the body
        world.step(1 / 60f, 6, 2);//Step the simulation of the box2d world to 60fps
        spGround.setPosition((groundBody.getPosition().x * fPM) - spGround.
                        getWidth() / 2,
                (groundBody.getPosition().y * fPM) - spGround.getHeight() / 2)
        ;
        Gdx.gl.glClearColor(1, 1, 1, 1);//clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugMatrix = batch.getProjectionMatrix().cpy().scale(fPM,
                fPM, 0);//For Debugging : Shows Boxes around the sprites

        camera.position.x = penguin.sprite.getX()+ penguin.sprite.getWidth()/2;//set the camera position to follow the penguin's position
        camera.position.y= penguin.sprite.getY()+penguin.sprite.getHeight()/2;
        if(camera.position.y<=spGround.getY()+camera.viewportHeight/2){
            camera.position.y = spGround.getY()+camera.viewportHeight/2;
        }

        camera.update();//update once all changes are made
        batch.setProjectionMatrix(camera.combined);//This sets the batch to what the camera sees


        //Scroll the background using the body's velocity
        scrollTimer += penguin.body.getLinearVelocity().x/(1000);//May need to change the divisor to get a realistic sized velocity
        if(scrollTimer>1.0f) {
            scrollTimer = 0.0f;
        }
        spBg.setU(scrollTimer);
        spBg.setU2((scrollTimer + 1));

        //Draw everything
        batch.begin();
        batch.draw(spBg, camera.position.x - camera.viewportWidth / 2, groundBody.getPosition().y,nWidth,nHeight);
        batch.draw(spPU,100,100,spPU.getWidth()*nWidth/700,spPU.getHeight()*nWidth/700); // drawing the power up image
        penguin.draw(batch);
        batch.end();
        stage.draw();//draws everything inside the stage

        debugRenderer.render(world, debugMatrix);//For Debugging : Shows Boxes around the sprites
    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
        texture.dispose();
        font.dispose();
    }
}