package com.connectlink.libra2andengine;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.util.adt.align.HorizontalAlign;

import android.graphics.Color;
import android.graphics.Typeface;

public class Timer {
int seconds;
int minutes;
int hours;
long start;
String secs;
String mins;
String hrs;
PlayScene scene;
String text;
Text sText;
Text scoreText;
Text winsText;
Font font;

String wins;
String score;
	public Timer(PlayScene scene) {
		start = System.nanoTime();
		font = FontFactory.create(scene.activity.getFontManager(), scene.activity.getTextureManager(), 128, 128, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 22, Color.WHITE);
		font.load();
		text = "00:00:00";
		score = "00000000";
		wins = "pt # 0000";
		
		sText = new Text(100, 25, font, text, new TextOptions(HorizontalAlign.CENTER), scene.activity.getVertexBufferObjectManager());
		scoreText = new Text(195, 25, font, score, new TextOptions(HorizontalAlign.CENTER), scene.activity.getVertexBufferObjectManager());
		winsText = new Text(300, 25, font, wins, new TextOptions(HorizontalAlign.CENTER), scene.activity.getVertexBufferObjectManager());
		scene.topRect.attachChild(winsText);
		scene.topRect.attachChild(scoreText);
		scene.topRect.attachChild(sText);
		this.scene = scene;
	}
	
	public void start() {
		scene.engine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() 
	    {
	            public void onTimePassed(final TimerHandler pTimerHandler) 
	            {
	            	seconds++;
	            	if (seconds > 59) {
	            		seconds = 0;
	            		minutes++;
	            		if (minutes > 59) {
	            			minutes = 0;
	            			hours++;
	            		}
	            	}
	            	updateText();
	            }
	    }));
	}
	
	public void updateScore() {
		score = String.format("%08d", BaseActivity.prefs.getScore());
		wins = "pt #" + String.format("%04d", BaseActivity.prefs.getWins());
		
		scoreText.setText(score);
		winsText.setText(wins);
	}
	
	public void updateText() {
		
		secs = String.format("%02d", seconds);
		mins = String.format("%02d", minutes);
		hrs = String.format("%02d", hours);
		
		text = (hours +":"+ mins +":"+ secs);
		
		sText.setText(text);
		System.out.println(text);
		start();
	}

	public long getMs() {
		long ms = seconds * 1000;
		ms+= minutes * 60 * 1000;
		ms+= hours * 60 * 60 * 1000;
		return ms;
	}
}
