package com.jdj.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Jose on 2014-12-03.
 */
public class Penguin {
    Texture texture;
    Body body;
    Sprite sprite;
    BodyDef bodyDef;

    public Penguin(World world) {
        this.texture = new Texture("penguin.png");
        this.sprite = new Sprite(texture);
        this.bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2) / 100f,
                (sprite.getY() + sprite.getHeight() / 2) / 100f);
        this.body = world.createBody(bodyDef);
    }

    public void UpdatePos(){
        sprite.setPosition((body.getPosition().x * 100f) - sprite.
                        getWidth() / 2,
                (body.getPosition().y * 100f) - sprite.getHeight() / 2)
        ;

    }
}