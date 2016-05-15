package com.connectlink.libra2andengine;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.IGameInterface.OnPopulateSceneCallback;
import org.andengine.ui.activity.BaseGameActivity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class SplashScene extends BaseScene implements IUpdateHandler {
	BitmapTextureAtlas logoAtlas;
	ITextureRegion logoRegion;
	Sprite sLogo;
	
	public SplashScene(Engine engine, BaseGameActivity activity, Camera camera) {
		super(engine, activity, camera);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadResources() {
		
		logoAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 512, 256, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR);
		logoRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(logoAtlas, activity, "logo.png", 0, 0);
		logoAtlas.load();
	}

	@Override
	public void createScene() {
		//create sprite, place in middle of screen, and rotate it 45 degrees
		this.setBackground(new Background(255, 255, 255));
		sLogo = new Sprite(BaseActivity.WIDTH / 2, BaseActivity.HEIGHT / 2, logoRegion.getWidth() * 1.23f, logoRegion.getHeight() * 1.23f, logoRegion, engine.getVertexBufferObjectManager());
		this.attachChild(sLogo);
		System.out.println("stufff");
		
		engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback() 
	    {
	            public void onTimePassed(final TimerHandler pTimerHandler) 
	            {
	                System.out.println("stuff1");
	                engine.unregisterUpdateHandler(pTimerHandler);
	                PlayScene play = new PlayScene(engine, activity, camera);
	                play.createScene();
	                engine.setScene(play);
	                Resources.getInstance().music.setLooping(true);
	        		Resources.getInstance().music.play();
	                play.start();
	            }
	    }));
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}


}
