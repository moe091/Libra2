package com.connectlink.libra2andengine;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Button extends Sprite{
PlayScene scene;
	public Button(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, vertexBufferObjectManager);
		// TODO Auto-generated constructor stub
	}
	
	public boolean onAreaTouched(final TouchEvent e, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if (e.isActionDown())
			tap();
		return true;
	}
	
	private void tap() {
		scene.clickOK();
	}

}
