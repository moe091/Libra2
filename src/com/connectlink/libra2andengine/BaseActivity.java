package com.connectlink.libra2andengine;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.SharedPreferences;
public class BaseActivity extends BaseGameActivity {
	public static final float WIDTH = 480;
	public static final float HEIGHT = 800;
	public static SharedPrefs prefs = new SharedPrefs();
	private SplashScene splash;
	private PlayScene game;
	
	Font mFont;
	Scene scene;
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
	    return new LimitedFPSEngine(pEngineOptions, 60); //CHECK THIS - check fps, see If i need a lower maxfps, or to use a different kind of engine
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {

		Camera mCamera = new Camera(0, 0, WIDTH, HEIGHT);
		EngineOptions options = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), mCamera);
		options.getAudioOptions().setNeedsSound(true).setNeedsMusic(true);
		System.out.println("!!!!!!!!!!!!!!!!!!!!! " + options.getTouchOptions().getTouchEventIntervalMilliseconds());
		options.getRenderOptions().setDithering(true);

		//options.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return options;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		prefs.setActivity(this);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("graphics/");
		splash = new SplashScene(this.mEngine, this, this.mEngine.getCamera());
		splash.loadResources();
		mEngine.registerUpdateHandler(new FPSLogger());
		Resources.getInstance().setBase(this, this.mEngine);
		Resources.getInstance().loadResources();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		
		pOnCreateSceneCallback.onCreateSceneFinished(splash);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) {
		splash.createScene();
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	private void loadGraphics() {
		//point andEngine to the graphics directory(relative to the assets directory)
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("graphics/");
		
		
	}
	
	@Override
	protected void onPause()
	{
	    super.onPause();
	    if (this.isGameLoaded()) {
	    	Resources.getInstance().music.pause();
	    	Resources.getInstance().win.pause();
	    	Resources.getInstance().creaking.pause();
	    }
	}

	@Override
	protected synchronized void onResume()
	{
	    super.onResume();
	    System.gc();
	    if (this.isGameLoaded()) {
	    	Resources.getInstance().music.resume();
	    	Resources.getInstance().win.resume();
	    	Resources.getInstance().creaking.resume();
	    }
	}

}
