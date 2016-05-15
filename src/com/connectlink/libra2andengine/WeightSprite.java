package com.connectlink.libra2andengine;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;

import com.badlogic.gdx.physics.box2d.Body;

import android.graphics.Typeface;


public class WeightSprite extends Sprite {
float ratio;
int denominator;
int numerator;
int index;
PlayScene scene;
boolean moveable = false;
boolean hasBody;
boolean touchable = true;
WeightSprite child;
Body body;
Font font;

	public WeightSprite(float f, float g, ITextureRegion weight, VertexBufferObjectManager vertexBufferObjectManager, PlayScene scene, int numerator, int denominator, boolean hasBody) {		
		super(f, g, Resources.getInstance().weight, vertexBufferObjectManager);
		this.hasBody = hasBody;
		System.out.println("HAS BODY = " + hasBody);
		this.font = FontFactory.create(scene.activity.getFontManager(), scene.activity.getTextureManager(), 128, 128, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.font.load();
		
		this.denominator = denominator;
		this.numerator = numerator;
		String text =  numerator + "/" + denominator;
		Text wText = new Text(40, 50, this.font, text, new TextOptions(HorizontalAlign.CENTER), scene.activity.getVertexBufferObjectManager());
		wText.setScale(0.60f);
		
		this.scene = scene;
		this.setScale(0.54f);
		this.attachChild(wText);
		scene.registerTouchArea(this);
	}
	
	
	@Override
	public boolean onAreaTouched(final TouchEvent e, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if (touchable) {
			if (scene.moveSprite == null && e.isActionDown()) {
				System.out.println("MOEBUG: touch down on: " + numerator + "/" + denominator + " body?" + hasBody);
				
				if (hasBody) {
					scene.selected = this;
				}
				scene.moveSprite = new WeightSprite(e.getX(), e.getY(), this.getTextureRegion(), this.getVertexBufferObjectManager(), scene, numerator, denominator, false);
				scene.moveSprite.child = this;
				scene.attachChild(scene.moveSprite);
				scene.registerTouchArea(scene.moveSprite);
			}
			if (e.isActionMove() && scene.moveSprite != null) {
				scene.moveSprite.setPosition(e.getX(), e.getY());
				if (scene.selected != null)
					scene.selected.body.setTransform(e.getX(), e.getY(), 0);
			}
			if (e.getAction() == TouchEvent.ACTION_UP) {

				//unregister();
				scene.unregisterTouchArea(scene.moveSprite);
				scene.makeWeight();
				
				if (scene.moveSprite != null) {
					scene.moveSprite.detachSelf();
					scene.moveSprite.dispose();
					scene.moveSprite = null;
				}
	
			}
		}
		return true;
	
	}
	
	public void unregister() {
		scene.unregisterTouchArea(this);
		if (this.child != null)
			scene.unregisterTouchArea(this.child);
	}
	
	@Override
	public void dispose() {
		scene.unregisterTouchArea(this);
		if (this.child != null)
			scene.unregisterTouchArea(this.child);
		super.dispose();
	}
	
	public float getWeight() {
		return (float) numerator / denominator;
	}

}
