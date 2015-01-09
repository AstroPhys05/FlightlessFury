package com.jdj.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Jose on 2014-12-16.
 */
public class Ground {
    int nWidth = Gdx.graphics.getWidth(), nHeight = Gdx.graphics.getHeight();
    Body body;

    Ground(World world) {
        BodyDef groundBodyDef = new BodyDef();//Box2d bodydef is used by the body
        groundBodyDef.position.set(0, 0);//Set the initial position
        body = world.createBody(groundBodyDef);//Pass the body def to the body
        PolygonShape groundBox = new PolygonShape();//make a box shaped hitbox
        groundBox.setAsBox(nWidth * 2 / 2 / 100f, nHeight / 7 / 3 / 100f);//set the size using the conversion ratio since box2d uses meters
        body.createFixture(groundBox, 0.0f);//make it fixed

    }

    public void UpdatePos(OrthographicCamera camera) {//Constantly called in render to update the position
        body.setTransform(camera.position.x / 100f, body.getPosition().y, 0);//keep the ground on the bottom of the camera
    }
}
