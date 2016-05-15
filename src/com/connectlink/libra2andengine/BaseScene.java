package com.connectlink.libra2andengine;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.app.Activity;

public abstract class BaseScene extends Scene {

	
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    protected Engine engine;
    protected BaseGameActivity activity;
    protected Camera camera;
    
    //---------------------------------------------
    // CONSTRUCTOR
    //---------------------------------------------
    
    public BaseScene(Engine engine, BaseGameActivity activity, Camera camera) {
    	this.engine = engine;
    	this.activity = activity;
    	this.camera = camera;
    }


	public abstract void loadResources();
    public abstract void createScene();
    public abstract void start();
    public abstract void close();
}
