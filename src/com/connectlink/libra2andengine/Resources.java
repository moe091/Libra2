package com.connectlink.libra2andengine;
import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.engine.Engine;

import android.util.Log;

public class Resources {
private static Resources instance = null;
Music music;
Sound creaking;
Sound win;
BaseGameActivity activity;
Engine engine;

BitmapTextureAtlas topAtlas;
BitmapTextureAtlas bottomAtlas;

ITextureRegion topRegion;
ITextureRegion bottomRegion;

BuildableBitmapTextureAtlas balanceAtlas;

ITextureRegion rod;
ITextureRegion base;
ITextureRegion balanceRod;
ITextureRegion chain;
ITextureRegion pan;
ITextureRegion weight;
ITextureRegion winner;
ITextureRegion ok;
ITextureRegion icon;

	public static Resources getInstance() {
		if (instance == null)
			instance = new Resources();
		return instance;
	}

	public void setBase(BaseActivity base, Engine engine) {
		this.activity = base;
		this.engine = engine;
	}
	public void loadResources() {
		loadGraphics();
		try
		{		
			win = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sounds/fanfare.mp3");
			creaking = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sounds/creaking.mp3");
		    music = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "sounds/bgm.mp3");
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
	}

	private void loadGraphics() {
		//Need to split background in 2 and stitch it together because AndEngine doesn't support image dimensions > 1024
		topAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 1024, 1024);
		topRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(topAtlas, activity, "pizza_top.png", 0, 0);
		topAtlas.load();
		
		
		bottomAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 1024, 1024);
		bottomRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bottomAtlas, activity, "pizza_bottom.png", 0, 0);
		bottomAtlas.load();	
						
		
		balanceAtlas = new BuildableBitmapTextureAtlas(engine.getTextureManager(), 1024, 512);
		rod = BitmapTextureAtlasTextureRegionFactory.createFromAsset(balanceAtlas, activity, "baserod.png");
		base = BitmapTextureAtlasTextureRegionFactory.createFromAsset(balanceAtlas, activity, "balancebase.png");
		balanceRod = BitmapTextureAtlasTextureRegionFactory.createFromAsset(balanceAtlas, activity, "balancerod.png");
		chain = BitmapTextureAtlasTextureRegionFactory.createFromAsset(balanceAtlas, activity, "chain.png");
		pan = BitmapTextureAtlasTextureRegionFactory.createFromAsset(balanceAtlas, activity, "pan.png");
		ok = BitmapTextureAtlasTextureRegionFactory.createFromAsset(balanceAtlas, activity, "OK.png");
		winner = BitmapTextureAtlasTextureRegionFactory.createFromAsset(balanceAtlas, activity, "winner.png");
		icon = BitmapTextureAtlasTextureRegionFactory.createFromAsset(balanceAtlas, activity, "icon.png");
		
		weight = BitmapTextureAtlasTextureRegionFactory.createFromAsset(balanceAtlas, activity, "w.png");
		
		try {
			balanceAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
		} catch (TextureAtlasBuilderException e) {
			Log.e("PLAYSCENE", "ERROR BUILDING BALANCEATLAS");
		}
		balanceAtlas.load();
		
	}
}
