package com.connectlink.libra2andengine;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.align.HorizontalAlign;

import android.graphics.Typeface;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Weights {
PlayScene scene;
Font font;
Text text;
ITextureRegion weight;
WeightSprite[] weights;
int numerator = 1;
	public Weights(PlayScene scene) {
		this.font = FontFactory.create(scene.activity.getFontManager(), scene.activity.getTextureManager(), 128, 128, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.font.load();
		weights = new WeightSprite[11];
		this.scene = scene;
	}
	
	
	
	public void createWeightSprites() {
		String text;
		Text wText;
		
		for (int i = 0; i < 6; i++) {
			text = "1/" + ((i + 1) * 2);
			wText = new Text(40, 50, this.font, text, new TextOptions(HorizontalAlign.CENTER), scene.activity.getVertexBufferObjectManager());
			wText.setScale(0.60f);
			
			weights[i] = new WeightSprite(50f + (i * 60), 165f, Resources.getInstance().weight, scene.engine.getVertexBufferObjectManager(), scene, numerator, (i + 1) * 2, false);
			
			scene.attachChild(weights[i]);
		}
		for (int i = 1; i < 6; i++) {
			text = "1/" + ((i * 2) + 1);
			wText = new Text(40, 50, this.font, text, new TextOptions(HorizontalAlign.CENTER), scene.activity.getVertexBufferObjectManager());
			wText.setScale(0.60f);
			
			weights[i + 5] = new WeightSprite(50f + (i * 60), 85f, Resources.getInstance().weight, scene.engine.getVertexBufferObjectManager(), scene, numerator, (i * 2) + 1, false);
			
			scene.attachChild(weights[i + 5]);
		}
	}
	
	public WeightSprite createWeight(int numerator, int denominator, float x, float y) {	
		String text =  numerator + "/" + denominator;
		Text wText = new Text(40, 50, this.font, text, new TextOptions(HorizontalAlign.CENTER), scene.activity.getVertexBufferObjectManager());
		wText.setScale(0.60f);
		
		WeightSprite weightSprite = new WeightSprite(x, y, Resources.getInstance().weight, scene.engine.getVertexBufferObjectManager(), scene, numerator, denominator, true);
		weightSprite.setScale(0.54f);
		weightSprite.hasBody = true;
		
		FixtureDef fixture = PhysicsFactory.createFixtureDef(numerator / (denominator * 1.0f), 0.2f, 0.2f);
		Body body = PhysicsFactory.createBoxBody(scene.getWorld(), weightSprite, BodyType.DynamicBody, fixture);
		weightSprite.body = body;

		return weightSprite;
	}




}
