package com.jdj.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Jose on 2014-12-04.
 */
public class PengWorld {
    World world = new World(new Vector2(0,-9.8f), true);
    Penguin penguin;
}
