package com.jdj.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public Penguin(World world) {
        this.texture = new Texture("penguin.png");
        this.sprite = new Sprite(texture);
        this.bodyDef = new BodyDef();//Box2d body uses the bodydef that contains position, type, etc.
        bodyDef.type = BodyDef.BodyType.DynamicBody;//The DynamicBody allows movement and collision

        bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2) / 100f,//Set the position to the sprite and convert to meters using 100 pixels = 1 meter
                (sprite.getY() + sprite.getHeight() / 2) / 100f);
        body = world.createBody(bodyDef);
        shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / 100f, sprite.getHeight()
                / 2 / 100f);//Set the body's shape to a box
        FixtureDef fixtureDef = new FixtureDef();//Fixture def defines how the body acts on other box2d bodys
        fixtureDef.shape = shape;//set to the box shape created
        fixtureDef.density = 10.0f;//Give it a density
        fixtureDef.friction = 0.3f;//Give it a small force of friction
        fixtureDef.restitution = 0.5f;//Give it a "bounciness"
        body.createFixture(fixtureDef);//Pass the fixturedef to the body
    }

    public void UpdatePos() {//Constantly called in render to update the sprite based on how the body changes
        sprite.setPosition((body.getPosition().x * 100f) - sprite.
                        getWidth() / 2,
                (body.getPosition().y * 100f) - sprite.getHeight() / 2)
        ;
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
    }

    public void ResetPos(){//Reset velocities to 0 and position to the initial
        body.setLinearVelocity(0, 0);
        body.setAngularVelocity(0);
        body.setTransform((sprite.getWidth() + sprite.getWidth() / 2) / 100f,
                (Gdx.graphics.getHeight() / 7/3 + sprite.getHeight() / 2) / 100f, 0);
    }
    public void setVelocity(float x, float y) {
        body.setLinearVelocity(x, y);
    }//takes x and y velocity and sets the box2d body to that velocity

    public void draw(SpriteBatch batch) {//Draw the sprite of the penguin
        batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(),
                sprite.getOriginY(),
                sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.
                        getScaleY(), sprite.getRotation());
    }
}