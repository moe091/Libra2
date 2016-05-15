package com.connectlink.libra2andengine;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public class InputHandler implements IOnSceneTouchListener{
PlayScene scene;

	public InputHandler(PlayScene scene) {
		this.scene = scene;
	}
	
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent e) {
		if (scene.moveSprite != null) {
			scene.moveSprite.setPosition(e.getX(), e.getY());
			if (scene.selected != null)
				scene.selected.body.setTransform(e.getX(), e.getY(), 0);
		} 
		return false;
	}

}
