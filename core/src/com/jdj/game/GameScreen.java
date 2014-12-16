package com.jdj.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.Matrix4;
//SideScrolling
//https://code.google.com/p/libgdx-users/wiki/ScrollingTexture

//Box2d Physics
//http://gamedevelopment.tutsplus.com/tutorials/how-to-create-a-custom-2d-physics-engine-the-basics-and-impulse-resolution--gamedev-6331
//https://github.com/libgdx/libgdx/wiki/Box2d
//https://www.youtube.com/watch?v=IDZMRDb1A_M#t=812
//http://programmersweb.blogspot.ca/2012/07/simple-libgdx-box2d-bouncing-ball.html

//OrthoCam
//http://www.gamefromscratch.com/post/2013/11/06/LibGDX-Tutorial-7-Camera-basics.aspx
//https://github.com/libgdx/libgdx/wiki/Orthographic-camera
public class GameScreen extends Game {
    final float fPM = 100f;//convert pixels to meters since box2d uses meters
    Stage stage;//The libgdx Stage to add the buttons
    Button bLaunch, bReset;//the libgdx buttons LAUNCH and RESET
    Texture texture;
    BitmapFont font;
    SpriteBatch batch;//everything gets drawn with this
    Sprite spBg;//uses the raw textures to create a file
    Texture iBg;//the raw input
    World world;//Box2d Physics world
//    Body groundBody;//
    OrthographicCamera camera;
    int nWidth, nHeight;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    float scrollTimer = 0.0f;
    Penguin penguin;
    Accelerometer accelerometer;
    Ground ground;
    //debugging
    @Override
    public void create() {
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        iBg = new Texture("city.jpg");
        nWidth = Gdx.graphics.getWidth();//Dimensions of the device
        camera = new OrthographicCamera(nWidth, nHeight);//libgdx orthographic camera

        penguin = new Penguin(world);//Make a penguin and pass the world class to do the physics
        ground = new Ground(world);

        iBg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);//Set the texture wrap to repeat to use sidescrolling

        spBg = new Sprite(iBg);//Background image

//        //Ground body & sprite
//        spGround.setPosition(0, 0);//Position of sprite
//        BodyDef groundBodyDef = new BodyDef();//Box2d bodydef is used by the body
//        groundBodyDef.position.set((spGround.getX() + spGround.getWidth() / 2) / fPM,
//                (spGround.getY() + spGround.getHeight() / 2) / fPM);//Set the initial position
//        groundBody = world.createBody(groundBodyDef);//Pass the body def to the body
//        PolygonShape groundBox = new PolygonShape();//make a box shaped hitbox
//        groundBox.setAsBox(spGround.getWidth() / 2 / fPM, spGround.getHeight()
//                / 2 / fPM);//set the size using the conversion ratio since box2d uses meters
//        groundBody.createFixture(groundBox, 0.0f);//make it fixed



        camera.position.y = ground.groundBody.getPosition().y + camera.viewportHeight / 2;
        bReset = new Button("RESET");

        stage.addActor(bLaunch.textButton);//add the buttons to the stage
        stage.addActor(bReset.textButton);
        debugRenderer = new Box2DDebugRenderer();//For Debugging : Shows Boxes around the sprites

    }


    @Override
    public void render() {
        if (bLaunch.buttonPressed) {
            penguin.setVelocity(20f, 10f);//set the velocity when launch is pressed
        }
        if (bReset.buttonPressed) {
            penguin.ResetPos();//Reset penguin position
            camera.position.y = ground.groundBody.getPosition().y + camera.viewportHeight / 2;//reset camera position
            scrollTimer = 0f;//reset scrollTimer
        }
        penguin.body.setAngularVelocity(-accelerometer.accelY() / 3);//set the angular velocity of penguin to the accelerometer value

        ground.UpdatePos(camera);//Update ground position
        penguin.UpdatePos();//update the sprites position to the body

        world.step(1 / 60f, 6, 2);//Step the simulation of the box2d world to 60fps
        Gdx.gl.glClearColor(1, 1, 1, 1);//clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugMatrix = batch.getProjectionMatrix().cpy().scale(fPM,
                fPM, 0);//For Debugging : Shows Boxes around the sprites

        camera.position.x = penguin.sprite.getX() + penguin.sprite.getWidth() / 2;//set the camera position to follow the penguin's position
        camera.position.y = penguin.sprite.getY() + penguin.sprite.getHeight() / 2;
        if (camera.position.y <= ground.groundBody.getPosition().y + camera.viewportHeight / 2) {
            camera.position.y = ground.groundBody.getPosition().y + camera.viewportHeight / 2;
        }

        camera.update();//update once all changes are made
        batch.setProjectionMatrix(camera.combined);//This sets the batch to what the camera sees


        //Scroll the background using the body's velocity
        scrollTimer += penguin.body.getLinearVelocity().x / (1000);//May need to change the divisor to get a realistic sized velocity
        if (scrollTimer > 1.0f) {
            scrollTimer = 0.0f;
        }

        spBg.setU(scrollTimer);

        spBg.setU2((scrollTimer + 1));

        //Draw everything
        batch.begin();
        batch.draw(spBg, camera.position.x - camera.viewportWidth / 2, ground.groundBody.getPosition().y);
        penguin.draw(batch);
        batch.end();
        stage.draw();//draws everything inside the stage

        debugRenderer.render(world, debugMatrix);//For Debugging : Shows Boxes around the sprites
    }

    @Override
    public void dispose() {//dispose so that less memory is used when exited
        batch.dispose();
        world.dispose();
        texture.dispose();
        font.dispose();
    }
}
