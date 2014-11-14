package com.jdj.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

// Source: http://programmersweb.blogspot.ca/2012/07/simple-libgdx-box2d-bouncing-ball.html
public class jdjgame extends ApplicationAdapter {
    static final float BOX_STEP = 1 / 60f;
    static final int velocity = 10; // set the velocity of the ball
    static final int BOX_POSITION_ITERATIONS = 2;
    static final float WORLD_TO_BOX = 0.01f;
    static final float BOX_WORLD_TO = 100f;
    World world = new World(new Vector2(0, -100), true);
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    SpriteBatch batch;
    Texture img;
    Sprite sprite;
    Body body;
    @Override
    public void create() {
        batch = new SpriteBatch();
        //sprite since it's going to move
        img = new Texture("penguin.png");
        sprite = new Sprite(img);
        camera = new OrthographicCamera();
        camera.viewportHeight = 320;
        camera.viewportWidth = 480;
        camera.position.set(camera.viewportWidth * .5f, camera.viewportHeight * .5f, 0f);
        camera.update();
        //Ground body
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(0, 10));
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape(); // imaginary floor, where the ball is bouncing
        groundBox.setAsBox((camera.viewportWidth) * 2, 10.0f);
        groundBody.createFixture(groundBox, 0.0f);
        //Dynamic Body
        sprite.setPosition(camera.viewportWidth / 2, camera.viewportHeight / 2);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2);
        body = world.createBody(bodyDef);
        CircleShape dynamicCircle = new CircleShape();
        dynamicCircle.setRadius(5f); // radius of the circle/shape
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicCircle;
        fixtureDef.density = 10.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1;
        body.createFixture(fixtureDef);
        debugRenderer = new Box2DDebugRenderer();
    }
    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(world, camera.combined);

        world.step(BOX_STEP, velocity, BOX_POSITION_ITERATIONS);
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        batch.begin();
        batch.draw(sprite, sprite.getX(), sprite.getY());
        batch.end();
    }
    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
