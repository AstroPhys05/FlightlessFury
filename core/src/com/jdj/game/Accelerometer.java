package com.jdj.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by Jash on 04/12/2014.
 */
////https://github.com/libgdx/libgdx/wiki/Accelerometer
public class Accelerometer {
    public static float accelY(){
        return Gdx.input.getAccelerometerY();
    }
}