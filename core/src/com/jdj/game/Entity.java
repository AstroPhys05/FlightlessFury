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
 * Created by Jose on 2015-01-07.
 */

public class Entity {
    Texture texture;
    Body body;
    Sprite sprite;
    BodyDef bodyDef;
    PolygonShape shape;
    int nRand;
    String sRandImg,sRandID;
    boolean destroyed = false,renew = false;

    public Entity(World world, String sSprite, String sID) {
        this.texture = new Texture(sSprite);
        this.sprite = new Sprite(texture);
        this.bodyDef = new BodyDef();//Box2d body uses the bodydef that contains position, type, etc.
        bodyDef.type = BodyDef.BodyType.StaticBody;//The StaticBody Keeps it stationary

        bodyDef.position.set(20,//Set the position to the sprite and convert to meters using 100 pixels = 1 meter
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
        body.createFixture(fixtureDef).setUserData(sID);//Pass the fixturedef to the body
        body.setTransform((Gdx.graphics.getWidth() - sprite.getWidth() / 2) / 100f,
                (Gdx.graphics.getHeight() / 7 / 3 + sprite.getHeight() / 2) / 100f, 0);//set the position to above ground
    }


    public void Renew(Penguin penguin){
        nRand = (int) (Math.random() * 2) + 1;
        if (nRand == 1) {
            sRandImg = "fish.png";
            sRandID = "fish";
        } else if (nRand == 2) {
            sRandImg = "powerup.png";
            sRandID = "powerup";
        }
        nRand = (int) (Math.random() * 50) + 1;
        if(nRand == 4){
            sRandImg = "jetpack.png";
            sRandID = "jetpack";
        }
        if(nRand == 3){
            sRandImg = "enemybird.png";
            sRandID = "enemybird";
        }
        nRand = (int) (Math.random() * (Gdx.graphics.getHeight()-Gdx.graphics.getHeight() / 7 / 3)) + 1;
        this.texture = new Texture(sRandImg);
        this.sprite = new Sprite(texture);
        body.getFixtureList().get(0).setUserData(sRandID);
        renew = false;
        body.setTransform(penguin.body.getPosition().x+Gdx.graphics.getWidth()/100f,
                nRand / 100f,0);
    }
    public void draw(SpriteBatch batch) {//Draw the sprite of the penguin
        sprite.setPosition((body.getPosition().x * 100f) - sprite.
                        getWidth() / 2,
                (body.getPosition().y * 100f) - sprite.getHeight() / 2)
        ;
        if(!destroyed) {
            batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(),
                    sprite.getOriginY(),
                    sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.
                            getScaleY(), sprite.getRotation());
        }
    }
}