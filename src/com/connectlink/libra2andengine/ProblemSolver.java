package com.connectlink.libra2andengine;

import org.andengine.entity.sprite.Sprite;
import java.util.*;
public class ProblemSolver {
	public static final float panx1 = 350;
	public static final float panx2 = 420;
	private final int maxNumerator;
	private final int maxDenominator;
	Random rand = new Random();
	PlayScene scene;
	int[] numerators = {5, 7, 2, 5, 7, 9, 8, 11, 10, 4, 3, 5, 13, 3};
	int[] denominators = {6, 10, 3, 12, 12, 20, 15, 30, 24, 9, 8, 8, 22, 4};
	static private int randomInt(final int min, final int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}

	public ProblemSolver(PlayScene scene) {
		this(5, 7, scene);
	}

	public ProblemSolver(int maxNumerator, int maxDeniminator, PlayScene scene) {
		this.scene = scene;
		this.maxNumerator   = maxNumerator;
		this.maxDenominator = maxDeniminator;
	}
	
	public boolean isOnPan(float x, float y, Sprite pan) {
		if (x > pan.getX() - (pan.getWidthScaled() / 2) && x < pan.getX() + (pan.getWidth() / 2) && (y > pan.getY() && y < pan.getY() + 80)) {
			return true;
		} else {
			return false;
		}
	}
	
	public WeightSprite makeRandomWeight() {
		int num = rand.nextInt(numerators.length);
		return scene.weights.createWeight(numerators[num], denominators[num], 75, 400);
	}
	
}
