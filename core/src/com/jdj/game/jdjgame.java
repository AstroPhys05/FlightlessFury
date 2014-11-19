package com.jdj.game;
//TODO clean up code by making multiple files and grouping similar code
//TODO add a background
import com.badlogic.gdx.ApplicationAdapter;
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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.math.Matrix4;
public class jdjgame extends ApplicationAdapter {
    final float fPM = 100f;//convert pixels to meters since box2d uses meters
    Stage stage;
    TextButton bLaunch, bReset;
    TextButton.TextButtonStyle textButtonStyle;
    Texture texture;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;
    SpriteBatch batch;
    Sprite sPeng, sGround;
    Texture iPeng, iGround;
    World world;
    Body body;
    OrthographicCamera camera;
    int nWidth, nHeight;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
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
        iPeng = new Texture("penguin.png");
        iGround = new Texture("ground.jpg");
        sPeng = new Sprite(iPeng);
        sGround = new Sprite(iGround);
        nWidth = Gdx.graphics.getWidth();
        nHeight = Gdx.graphics.getHeight();
        world = new World(new Vector2(0, -9.8f), true);
        sGround.setSize(nWidth,nHeight/7);
        // Penguin Sprite and Physics body
        sPeng.setPosition(sPeng.getWidth(),sGround.getHeight());
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sPeng.getX() + sPeng.getWidth() / 2) / fPM,
                (sPeng.getY() + sPeng.getHeight() / 2) / fPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sPeng.getWidth() / 2 / fPM, sPeng.getHeight()
                / 2 / fPM);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 10.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;
        body.createFixture(fixtureDef);
        //Ground body & sprite
        sGround.setPosition(Gdx.graphics.getWidth() / 2 - sGround.getWidth() / 2, 0);
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set((sGround.getX() + sGround.getWidth() / 2) / fPM,
                (sGround.getY() + sGround.getHeight() / 2) / fPM );
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(sGround.getWidth() / 2 / fPM, sGround.getHeight()
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
        bLaunch.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //if bLaunch is pressed do stuff
                body.setLinearVelocity(5f,5f);
            }
        });
        //Button 2 : Reset
        bReset = new TextButton("RESET", textButtonStyle);
        bReset.setSize(nWidth/7,nWidth/7);
        bReset.setPosition(nWidth/7,nHeight-nWidth/7);
        bReset.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                body.setLinearVelocity(0,0);
                body.setAngularVelocity(0);
                body.setTransform((sPeng.getWidth()+ sPeng.getWidth()/2) / fPM,
                        (sGround.getHeight() + sPeng.getHeight() / 2) / fPM,0);
            }

        });
        stage.addActor(bLaunch);
        stage.addActor(bReset);
        debugRenderer = new Box2DDebugRenderer();//For Debugging : Shows Boxes around the sprites
    }


    @Override
    public void render() {
        world.step(1 / 60f, 6, 2);
        sPeng.setPosition((body.getPosition().x * fPM) - sPeng.
                        getWidth() / 2,
                (body.getPosition().y * fPM) - sPeng.getHeight() / 2)
        ;
        sPeng.setRotation((float)Math.toDegrees(body.getAngle()));
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugMatrix = batch.getProjectionMatrix().cpy().scale(fPM,
                fPM, 0);//For Debugging : Shows Boxes around the sprites

        camera.position.set(sPeng.getX(), sPeng.getY(),0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(sPeng, sPeng.getX(), sPeng.getY(),sPeng.getOriginX(),
                sPeng.getOriginY(),
                sPeng.getWidth(),sPeng.getHeight(),sPeng.getScaleX(),sPeng.
                        getScaleY(),sPeng.getRotation());
        batch.draw(sGround,sGround.getX(),sGround.getY(),sGround.getWidth(),sGround.getHeight());
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
