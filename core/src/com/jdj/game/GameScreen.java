package com.jdj.game;
//TODO clean up code by making multiple files and grouping similar code
//TODO COMMENT MORE
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
    TextButton bLaunch, bReset;
    Boolean bLaunchPressed = false;
    TextButton.TextButtonStyle textButtonStyle;
    Texture texture;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;
    SpriteBatch batch;
    Sprite spPeng, spGround, spBg;
    Texture iPeng, iGround, iBg;
    World world;
    Body body, groundBody;
    OrthographicCamera camera;
    int nWidth, nHeight;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    float scrollTimer = 0.0f;
    Penguin penguin;
    Accelerometer accelerometer;
    //debugging
    @Override
    public void create() {
        batch = new SpriteBatch();
        stage = new Stage();
        skin = new Skin();
        Gdx.input.setInputProcessor(stage);
        texture = new Texture(Gdx.files.internal("LiberationMono.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image
        font = new BitmapFont(Gdx.files.internal("LiberationMono.fnt"), new TextureRegion(texture), false);
        font.setScale(1f, 1f);//scale to other devices - need to test it

        buttonAtlas = new TextureAtlas("buttons.pack");
        //iPeng = new Texture("penguin.png");
        iGround = new Texture("ground.jpg");
        iBg = new Texture("city.jpg");
        //spPeng = new Sprite(iPeng);
        spGround = new Sprite(iGround);
        nWidth = Gdx.graphics.getWidth();
        nHeight = Gdx.graphics.getHeight();
        world = new World(new Vector2(0f, -9.8f), true);
        spGround.setSize(nWidth*2, nHeight / 7/3);

        penguin = new Penguin(world);
        iBg.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);

        spBg = new Sprite(iBg);
        //spBg.setSize(nWidth*7,nHeight*7);

        //Ground body & sprite
        spGround.setPosition(0, 0);
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set((spGround.getX() + spGround.getWidth() / 2) / fPM,
                (spGround.getY() + spGround.getHeight() / 2) / fPM);
        groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(spGround.getWidth() / 2 / fPM, spGround.getHeight()
                / 2 / fPM);
        groundBody.createFixture(groundBox, 0.0f);
//
        //Button 1 : Launch
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("Launch_Pressed");
        textButtonStyle.down = skin.getDrawable("Launch_Unpressed");
        textButtonStyle.checked = skin.getDrawable("Launch_Pressed");
        textButtonStyle.font.setColor(Color.WHITE);
        bLaunch = new TextButton("LAUNCH", textButtonStyle);
        bLaunch.setSize(nWidth / 7, nWidth / 7);
        bLaunch.setPosition(0, nHeight - nWidth / 7);//Set it to top left corner
        camera = new OrthographicCamera(nWidth, nHeight);
        camera.position.y = spGround.getY()+camera.viewportHeight/2;
        bLaunch.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                bLaunchPressed = true;
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                bLaunchPressed = false;
            }
        });
        //Button 2 : Reset
        bReset = new TextButton("RESET", textButtonStyle);
        bReset.setSize(nWidth / 7, nWidth / 7);
        bReset.setPosition(nWidth / 7, nHeight - nWidth / 7);
        bReset.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                penguin.ResetPos();
                camera.position.y = spGround.getY()+camera.viewportHeight/2;
                scrollTimer = 0f;
            }

        });
        stage.addActor(bLaunch);
        stage.addActor(bReset);
        debugRenderer = new Box2DDebugRenderer();//For Debugging : Shows Boxes around the sprites

    }


    @Override
    public void render() {
        if(bLaunchPressed){
            penguin.setVelocity(20f,10f);
        }
        penguin.body.setAngularVelocity(-accelerometer.accelY()/3);

        groundBody.setTransform(camera.position.x/fPM,groundBody.getPosition().y,0);
        penguin.UpdatePos();
        world.step(1 / 60f, 6, 2);
        spGround.setPosition((groundBody.getPosition().x * fPM) - spGround.
                        getWidth() / 2,
                (groundBody.getPosition().y * fPM) - spGround.getHeight() / 2)
        ;
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugMatrix = batch.getProjectionMatrix().cpy().scale(fPM,
                fPM, 0);//For Debugging : Shows Boxes around the sprites
        camera.position.x = penguin.sprite.getX()+ penguin.sprite.getWidth()/2;
        camera.position.y= penguin.sprite.getY()+penguin.sprite.getHeight()/2;
        if(camera.position.y<=spGround.getY()+camera.viewportHeight/2){
            camera.position.y = spGround.getY()+camera.viewportHeight/2;
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);



        scrollTimer += penguin.body.getLinearVelocity().x/(1000);//May need to change the divisor to get a realistic sized velocity
        if(scrollTimer>1.0f) {
            scrollTimer = 0.0f;
        }

        spBg.setU(scrollTimer);

        spBg.setU2((scrollTimer+1));

        batch.begin();
        batch.draw(spBg,camera.position.x-camera.viewportWidth/2,groundBody.getPosition().y);


        penguin.draw(batch);
        batch.end();

        stage.draw();

        debugRenderer.render(world, debugMatrix);//For Debugging : Shows Boxes around the sprites
    }

    @Override
    public void dispose() {
        batch.dispose();
        iPeng.dispose();
        world.dispose();
        texture.dispose();
        font.dispose();
    }
}