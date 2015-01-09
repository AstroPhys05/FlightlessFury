package com.jdj.game;
//TODO add sounds
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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
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
    Button bReset, bLaunch;
    Texture texture;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;
    SpriteBatch batch;
    Sprite spBg,spGameOver;
    Texture iBg,iGameOver;
    World world;
    OrthographicCamera camera;
    int nWidth, nHeight;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    float scrollTimer = 0.0f;
    Penguin penguin;
    Entity entity;
    Sound sound1;
    Sound sound2;
    Ground ground;

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
        font.setColor(Color.RED);
        buttonAtlas = new TextureAtlas("buttons.pack");
        iBg = new Texture("mario.jpg");
        iGameOver = new Texture("Game_Over.png");
        nWidth = Gdx.graphics.getWidth();//Dimensions of the device
        nHeight = Gdx.graphics.getHeight();
        world = new World(new Vector2(0f, -9.8f), true);//Create the box2d physics world with 0 gravity in the x-direction and -9.8m/s/s in the y direction

        penguin = new Penguin(world);
        entity = new Entity(world, "fish.png", "fish");

        iBg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        spBg = new Sprite(iBg);
        spGameOver = new Sprite(iGameOver);

        ground = new Ground(world);

        //Camera
        camera = new OrthographicCamera(nWidth, nHeight);//libgdx orthographic camera
        camera.position.y = ground.body.getPosition().y + camera.viewportHeight / 2;//set the position to above the ground

        bLaunch = new Button("LAUNCH", nWidth / 2 - nWidth / 7 / 2, nHeight / 2 - nWidth / 7 / 2, sound1);
        bReset = new Button("RESET", 0, nHeight - nWidth / 7, sound2);
        stage.addActor(bLaunch.button);//add the buttons to the stage
        stage.addActor(bReset.button);
        debugRenderer = new Box2DDebugRenderer();//For Debugging : Shows Boxes around the sprites
        //Contact Listener
        world.setContactListener(new ContactListener() {
            @Override
            public void endContact(Contact contact) {//called when to fixtures no longer collide
            }

            @Override
            public void beginContact(Contact contact) {//called when to fixtures collide

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                WorldManifold manifold = contact.getWorldManifold();
                Fixture fa = contact.getFixtureA();
                Fixture fb = contact.getFixtureB();
                for (int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
                    if ((fa.getUserData() != null && fa.getUserData().equals("penguin")) && (fb.getUserData() != null && fb.getUserData().equals("powerup"))) {//if the penguin hits the power up it goes through
                        contact.setEnabled(false);
                        entity.renew = true;
                        penguin.dFuel+=20;
                        if(penguin.dFuel>100){
                            penguin.dFuel=100;}
                    }
                    if ((fa.getUserData() != null && fa.getUserData().equals("penguin")) && (fb.getUserData() != null && fb.getUserData().equals("fish"))) {//if the penguin hits the fish it goes through
                        contact.setEnabled(false);
                        entity.renew = true;
                        penguin.dFuel+=20;
                        if(penguin.dFuel>100){
                            penguin.dFuel=100;}
                        penguin.nFish+=1;//its doubled for some reason
                    }
                    if ((fa.getUserData() != null && fa.getUserData().equals("penguin")) && (fb.getUserData() != null && fb.getUserData().equals("enemybird"))) {//if the penguin hits the fish it goes through
                        entity.renew = true;
                        penguin.dFuel =0;
                        penguin.nFuel = 0;
                        penguin.body.setAngularVelocity(500);
                    }
                    if ((fa.getUserData() != null && fa.getUserData().equals("penguin")) && (fb.getUserData() != null && fb.getUserData().equals("jetpack"))) {//if the penguin hits the fish it goes through
                        penguin.SuperSpeed();
                        contact.setEnabled(false);
                        entity.renew = true;
                    }
                }
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }


    @Override
    public void render() {
        if (bLaunch.pressed) {//If the launch is pressed
            penguin.launched = true;
            bLaunch.button.remove();
        }
        if(penguin.sprite.getY()>camera.viewportHeight+penguin.sprite.getHeight()*3){
            penguin.hasFuel = false;
        }
        if (bReset.pressed) {//If the reset is pressed
            penguin.ResetPos();//Reset penguin position
            camera.position.y = ground.body.getPosition().y + camera.viewportHeight / 2;//reset camera position
            scrollTimer = 0f;//reset scrollTimer
            stage.addActor(bLaunch.button);
            entity.Renew(penguin);
        }
        if(entity.renew){
            entity.Renew(penguin);
        }
        if(entity.sprite.getX()<camera.position.x-camera.viewportWidth){
            entity.Renew(penguin);
        }
        ground.UpdatePos(camera);
        penguin.Update();//update the sprites position to the body & other values
        world.step(1 / 60f, 6, 2);//Step the simulation of the box2d world to 60fps
        Gdx.gl.glClearColor(1, 1, 1, 1);//clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugMatrix = batch.getProjectionMatrix().cpy().scale(fPM,
                fPM, 0);//For Debugging : Shows Boxes around the sprites

        camera.position.x = penguin.sprite.getX() + penguin.sprite.getWidth()*2;//set the camera position to follow the penguin's position

        camera.update();//update once all changes are made
        batch.setProjectionMatrix(camera.combined);//This sets the batch to what the camera sees


        //Scroll the background using the body's velocity
        scrollTimer += penguin.body.getLinearVelocity().x / (1070);//May need to change the divisor to get a realistic sized velocity
        if (scrollTimer > 1.0f) {
            scrollTimer = 0.0f;
        }
        spBg.setU(scrollTimer);
        spBg.setU2((scrollTimer + 1));

        //Draw everything
        batch.begin();
        batch.draw(spBg, camera.position.x - camera.viewportWidth / 2, ground.body.getPosition().y, nWidth, nHeight);
        penguin.draw(batch);
        entity.draw(batch);
        if(penguin.isDead){
            batch.draw(spGameOver, camera.position.x - camera.viewportWidth / 2, ground.body.getPosition().y, nWidth, nHeight);

        }
        font.draw(batch, "Speed:" + ((double) Math.round(penguin.dVel *3.6* 10) / 10) + "km/h", camera.position.x, nHeight - nHeight / 15);//round speed to 1 decimal place
        font.draw(batch, "Fuel:" + penguin.nFuel + "%", camera.position.x, nHeight - 2 * nHeight / 15);
        font.draw(batch, "Distance:" + penguin.nDistance + " m", camera.position.x + camera.viewportWidth / 4, nHeight - nHeight / 15);
        font.draw(batch, "Fish Eaten:" + penguin.nFish, camera.position.x + camera.viewportWidth / 4,  nHeight - 2 * nHeight / 15);

        batch.end();
        stage.draw();//draws everything inside the stage

       //debugRenderer.render(world, debugMatrix);//For Debugging : Shows Boxes around the sprites
    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
        texture.dispose();
        font.dispose();
    }
}