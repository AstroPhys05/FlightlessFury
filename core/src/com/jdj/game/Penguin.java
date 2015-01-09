package com.jdj.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Jose on 2014-12-03.
 */
public class Penguin {
    Texture texture;
    Body body;
    Sprite sprite;
    BodyDef bodyDef;
    PolygonShape shape;
    double dVel;//Velocity and Direction of x and y components
    Boolean hasFuel = true, launched = false, isDead = false;
    double dFuel = 100;
    int nFuel = 100, nDistance = 0,nFish = 0;

    float x, y;

    public Penguin(World world) {
        this.texture = new Texture("penguin.png");
        this.sprite = new Sprite(texture);
        this.bodyDef = new BodyDef();//Box2d body uses the bodydef that contains position, type, etc.
        bodyDef.type = BodyDef.BodyType.DynamicBody;//The DynamicBody allows movement and collision

        bodyDef.position.set(0,
                (Gdx.graphics.getHeight() / 7 / 3 + sprite.getHeight() / 2) / 100f);
        body = world.createBody(bodyDef);
        shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / 100f, sprite.getHeight()
                / 2 / 100f);//Set the body's shape to a box
        FixtureDef fixtureDef = new FixtureDef();//Fixture def defines how the body acts on other box2d bodys
        fixtureDef.shape = shape;//set to the box shape created
        fixtureDef.density = 10.0f;//Give it a density
        fixtureDef.friction = 0.3f;//Give it a small force of friction
        fixtureDef.restitution = 0.5f;//Give it a "bounciness"
        body.createFixture(fixtureDef).setUserData("penguin");//Pass the fixturedef to the body
        x = (float) (250 * Math.cos(body.getAngle()));
        y = (float) (500 * Math.sin(body.getAngle()));

    }

    public void Update() {//Constantly called in render to update the sprite based on how the body changes
        dVel = Math.sqrt((Math.pow(body.getLinearVelocity().x, 2)) + (Math.pow(body.getLinearVelocity().y, 2)));
        if (hasFuel) {

            body.setLinearVelocity((float) (dVel * Math.cos(body.getAngle())), (float) (dVel * Math.sin(body.getAngle())));
            if (dVel < 10 && launched) {
                body.applyForceToCenter(new Vector2(x, y), true);
            }
        }
        if (dVel > 5 && hasFuel) {
            body.setAngularVelocity(-Accelerometer.accelY() / 3);//set the angular velocity of penguin to the accelerometer value
        }

        sprite.setPosition((body.getPosition().x * 100f) - sprite.
                        getWidth() / 2,
                (body.getPosition().y * 100f) - sprite.getHeight() / 2)
        ;
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        if (launched && nFuel > 0&& dFuel>0) {
            dFuel -= 0.1;
            nFuel = (int) dFuel;
        }
        hasFuel = nFuel > 0;
        if (!hasFuel && dVel < 1) {
            isDead = true;
        }
        nDistance = (int) body.getPosition().x;
    }
    public void SuperSpeed(){
        body.setLinearVelocity((float) (120 * Math.cos(body.getAngle())),0);
    }

    public void ResetPos() {//Reset velocities to 0 and position to the initial
        body.setLinearVelocity(0, 0);
        body.applyForceToCenter(0, 0, true);
        body.setAngularVelocity(0);
        body.setTransform(0,(Gdx.graphics.getHeight() / 7 / 3 + sprite.getHeight() / 2) / 100f, 0);
        nFuel = 100;
        dFuel = 100;
        launched = false;
        hasFuel = true;
        nDistance = 0;
        nFish = 0;
        isDead = false;
    }


    public void draw(SpriteBatch batch) {//Draw the sprite of the penguin
        batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(),
                sprite.getOriginY(),
                sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.
                        getScaleY(), sprite.getRotation());
    }
}