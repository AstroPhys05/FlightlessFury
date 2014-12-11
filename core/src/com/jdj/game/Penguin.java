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
        this.bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2) / 100f,
                (sprite.getY() + sprite.getHeight() / 2) / 100f);
        this.body = world.createBody(bodyDef);
        shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / 100f, sprite.getHeight()
                / 2 / 100f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 10.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;
        body.createFixture(fixtureDef);
    }

    public void UpdatePos() {
        sprite.setPosition((body.getPosition().x * 100f) - sprite.
                        getWidth() / 2,
                (body.getPosition().y * 100f) - sprite.getHeight() / 2)
        ;
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
    }

    public void ResetPos(){
        body.setLinearVelocity(0, 0);
        body.setAngularVelocity(0);
        body.setTransform((sprite.getWidth() + sprite.getWidth() / 2) / 100f,
                (Gdx.graphics.getHeight() / 7/3 + sprite.getHeight() / 2) / 100f, 0);
    }
    public void setVelocity(float x, float y) {
        body.setLinearVelocity(20f, 10f);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(),
                sprite.getOriginY(),
                sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.
                        getScaleY(), sprite.getRotation());
    }
}