package com.connectlink.libra2andengine;

import android.app.Activity;
import android.content.SharedPreferences;

public class SharedPrefs {
Activity activity;

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	
	public int getScore() {
		int score;
		SharedPreferences myPrefs = activity.getSharedPreferences("myPrefs", activity.MODE_WORLD_READABLE);
	    score = myPrefs.getInt("score", 0);
	    return score;
	}
	public void addScore(int score) {
		SharedPreferences myPrefs = activity.getSharedPreferences("myPrefs", activity.MODE_WORLD_READABLE);
	    SharedPreferences.Editor prefsEditor = myPrefs.edit();
	    prefsEditor.putInt("score", score + getScore());
	    prefsEditor.commit();
	}
	
	public int getWins() {
		int wins;
		SharedPreferences myPrefs = activity.getSharedPreferences("myPrefs", activity.MODE_WORLD_READABLE);
	    wins = myPrefs.getInt("wins", 0);
	    return wins;
	}
	public void addWin() {
		SharedPreferences myPrefs = activity.getSharedPreferences("myPrefs", activity.MODE_WORLD_READABLE);
	    SharedPreferences.Editor prefsEditor = myPrefs.edit();
	    prefsEditor.putInt("wins", getWins() + 1);
	    prefsEditor.commit();
	}
}
