package com.connectlink.libra2andengine;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.list.SmartList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.connectlinkcorp.common.Rational;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;

public class PlayScene extends BaseScene {
	PlayScene play;
	ProblemSolver solver = new ProblemSolver(this);
	Timer time;

	private Vector2 startPoint;
	private BitmapTextureAtlas topAtlas;
	private BitmapTextureAtlas bottomAtlas;
	
	private ITextureRegion topRegion;
	private ITextureRegion bottomRegion;
	
	private Sprite bgTop;
	private Sprite bgBottom;
	private Sprite sRod;
	private Sprite sBase;
	private Sprite sBalanceRod;
	private Sprite sLeftPan;
	private Sprite sRightPan;
	private Sprite sLeftChainL;
	private Sprite sLeftChainR;
	private Sprite sRightChainL;
	private Sprite sRightChainR;
	private Sprite sIcon;
	
	private PhysicsWorld world;
	
	
	private int leftNum;
	private int leftDen;
	
	private float rightWeight;
	private ArrayList<WeightSprite> wSprites = new ArrayList<WeightSprite>();
	private boolean won = false;
	
	Weights weights = new Weights(this);
	WeightSprite selected = null;
	WeightSprite moveSprite;
	
	Rectangle topRect;
	
	public PlayScene(Engine engine, BaseGameActivity activity, Camera camera) {
		super(engine, activity, camera);
	}
	


	@Override
	public void createScene() {
		InputHandler handler = new InputHandler(this);
		this.setTouchAreaBindingOnActionDownEnabled(false);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
		this.setOnSceneTouchListener(handler);
		
		this.setBackground(new Background(0, 255, 255));
		
		bgTop = new Sprite(0, 0, BaseActivity.WIDTH, BaseActivity.HEIGHT / 2,  Resources.getInstance().topRegion, activity.getVertexBufferObjectManager());
		bgBottom = new Sprite(0, BaseActivity.HEIGHT / 2, BaseActivity.WIDTH, BaseActivity.HEIGHT / 2, Resources.getInstance().bottomRegion, activity.getVertexBufferObjectManager());
		bgTop.setAnchorCenter(0, 0);
		bgBottom.setAnchorCenter(0, 0);
		bgBottom.setPosition(0, 0);
		bgTop.setPosition(0, BaseActivity.HEIGHT / 2);
		
		this.attachChild(bgTop);
		this.attachChild(bgBottom);
		
		createBalance();
		

	}

	public void start() {
		
		createPhysics();
		
		weights.createWeightSprites();
		time.updateScore();
	}
	private void createBalance() {
		
		//Balance body pieces
		sBase = new Sprite(0, 0, Resources.getInstance().base, activity.getVertexBufferObjectManager());
		sBase.setScale(0.4f);
		
		sRod = new Sprite(0, 0, Resources.getInstance().rod, activity.getVertexBufferObjectManager());
		sRod.setScale(0.6f, 0.7f);
		
		sBalanceRod = new Sprite(0, 0, Resources.getInstance().balanceRod, activity.getVertexBufferObjectManager());
		sBalanceRod.setScale(0.6f);
		
		sIcon = new Sprite(25, 25, Resources.getInstance().icon, activity.getVertexBufferObjectManager());
		sIcon.setScale(0.25f);
		
		//Pan Pieces
		sLeftPan = new Sprite(0, 0, Resources.getInstance().pan, activity.getVertexBufferObjectManager());
		sLeftPan.setScale(0.6f);
		
		sRightPan = new Sprite(0, 0, Resources.getInstance().pan, activity.getVertexBufferObjectManager());
		sRightPan.setScale(0.6f);
		
		sLeftChainL = new Sprite(0, 0, Resources.getInstance().chain, activity.getVertexBufferObjectManager());
		sLeftChainL.setScale(0.5f);
		sLeftChainR = new Sprite(0, 0, Resources.getInstance().chain, activity.getVertexBufferObjectManager());
		sLeftChainR.setScale(0.5f);
		
		sRightChainL = new Sprite(0, 0, Resources.getInstance().chain, activity.getVertexBufferObjectManager());
		sRightChainL.setScale(0.5f);
		sRightChainR = new Sprite(0, 0, Resources.getInstance().chain, activity.getVertexBufferObjectManager());
		sRightChainR.setScale(0.5f);
		
		
		//Set Positions
		
		sRod.setPosition(BaseActivity.WIDTH / 2, 400);
		sBalanceRod.setPosition(BaseActivity.WIDTH / 2, 400 + (sRod.getHeightScaled() / 2) + (sBalanceRod.getHeightScaled() / 2) - 20);
		sBase.setPosition(BaseActivity.WIDTH / 2, 400 - (sBase.getHeightScaled() / 2 + sRod.getHeightScaled() / 2));
		sRightPan.setPosition(BaseActivity.WIDTH * 0.83f, 370);
		sLeftPan.setPosition(BaseActivity.WIDTH * 0.17f, 370);
		
		sLeftChainL.setPosition(50, 430);
		sLeftChainL.setRotation(-25);
		sLeftChainR.setPosition(110, 430);
		sLeftChainR.setRotation(25);
		
		sRightChainR.setPosition(BaseActivity.WIDTH - 50, 430);
		sRightChainR.setRotation(25);
		sRightChainL.setPosition(BaseActivity.WIDTH - 110, 430);
		sRightChainL.setRotation(-25);
		
		
		//Attach sprites to scene
		this.attachChild(sLeftChainL);
		this.attachChild(sLeftChainR);
		this.attachChild(sRightChainR);
		this.attachChild(sRightChainL);
		this.attachChild(sBalanceRod);
		this.attachChild(sRightPan);
		this.attachChild(sLeftPan);
		this.attachChild(sBase);
		this.attachChild(sRod);
		
		topRect = new Rectangle(BaseActivity.WIDTH / 2, BaseActivity.HEIGHT - 25, BaseActivity.WIDTH, 50, activity.getVertexBufferObjectManager());
		topRect.setColor(0.1f, 0.1f, 0.1f);
		this.attachChild(topRect);
		topRect.attachChild(sIcon);
		time = new Timer(this);
		time.start();
	}
	
	private void createPhysics()
	{
		//Set up world
	    world = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false); 
	    registerUpdateHandler(getWorld());

	    
	    //Create fixture def to be reused
	    FixtureDef fixture = new FixtureDef();
	    fixture = PhysicsFactory.createFixtureDef(1, 0.2f, 0);
		
		
	    //create balance body
	    Body rodBody = PhysicsFactory.createBoxBody(getWorld(), sRod, BodyType.StaticBody, fixture);
	    getWorld().registerPhysicsConnector(new PhysicsConnector(sRod, rodBody));
	    
	    Body balanceRodBody = PhysicsFactory.createBoxBody(getWorld(), sBalanceRod, BodyType.DynamicBody, fixture);
	    getWorld().registerPhysicsConnector(new PhysicsConnector(sBalanceRod, balanceRodBody));
	    //add base
	    
	    //make jointdef
	    RevoluteJointDef jointDef = new RevoluteJointDef();
	    jointDef.collideConnected = false;
	    //make joint
	    jointDef.initialize(rodBody, balanceRodBody, balanceRodBody.getWorldCenter());
	    jointDef.enableLimit = true;
	    jointDef.lowerAngle = -0.3f;
	    jointDef.upperAngle = 0.3f;
	    getWorld().createJoint(jointDef);
	    
	    
	    
	    //__________________________________________Create Left Pan____________________________________________
	    //make bodies
	    Body leftChainLBody = PhysicsFactory.createBoxBody(getWorld(), sLeftChainL, BodyType.DynamicBody, fixture);
	    getWorld().registerPhysicsConnector(new PhysicsConnector(sLeftChainL, leftChainLBody));
	    
	    Body leftChainRBody = PhysicsFactory.createBoxBody(getWorld(), sLeftChainR, BodyType.DynamicBody, fixture);
	    getWorld().registerPhysicsConnector(new PhysicsConnector(sLeftChainR, leftChainRBody));
	    
	    Body leftPanBody = PhysicsFactory.createBoxBody(getWorld(), sLeftPan, BodyType.DynamicBody, fixture);
	    getWorld().registerPhysicsConnector(new PhysicsConnector(sLeftPan, leftPanBody));
	    
	    //make joints
		RevoluteJointDef joint = new RevoluteJointDef();
		
		joint.initialize(balanceRodBody, leftChainLBody, new Vector2(75.0f / 30, 465.0f / 30));
		getWorld().createJoint(joint);
		
		joint.initialize(balanceRodBody, leftChainRBody, new Vector2(75.0f / 30, 465.0f / 30));
		getWorld().createJoint(joint);
		
		joint.initialize(leftPanBody, leftChainLBody, new Vector2(20 / 30.0f, 350 / 30.0f));
		getWorld().createJoint(joint);
		
		joint.initialize(leftPanBody, leftChainRBody, new Vector2(130 / 30.0f, 350 / 30.0f));
		getWorld().createJoint(joint);
		//___________________________________________________________________________________________________\\
		
		
		
		//_________________________________________Create Right Pan___________________________________________\\
	    //make bodies
	    Body rightChainLBody = PhysicsFactory.createBoxBody(getWorld(), sRightChainL, BodyType.DynamicBody, fixture);
	    getWorld().registerPhysicsConnector(new PhysicsConnector(sRightChainL, rightChainLBody));
	    
	    Body rightChainRBody = PhysicsFactory.createBoxBody(getWorld(), sRightChainR, BodyType.DynamicBody, fixture);
	    getWorld().registerPhysicsConnector(new PhysicsConnector(sRightChainR, rightChainRBody));
	    
	    Body rightPanBody = PhysicsFactory.createBoxBody(getWorld(), sRightPan, BodyType.DynamicBody, fixture);
	    getWorld().registerPhysicsConnector(new PhysicsConnector(sRightPan, rightPanBody));
	    
		
	    //make joints
		joint.initialize(balanceRodBody, rightChainLBody, new Vector2(375 / 30.0f, 465.0f / 30));
		getWorld().createJoint(joint);
		
		joint.initialize(balanceRodBody, rightChainRBody, new Vector2(375 / 30.0f, 465.0f / 30));
		getWorld().createJoint(joint);
		
		joint.initialize(rightPanBody, rightChainLBody, new Vector2(320 / 30.0f, 350 / 30.0f));
		getWorld().createJoint(joint);
		
		joint.initialize(rightPanBody, rightChainRBody, new Vector2(430 / 30.0f, 350 / 30.0f));
		getWorld().createJoint(joint);
		//___________________________________________________________________________________________________\\

        
        leftChainLBody.applyAngularImpulse(20f);
        rightChainLBody.applyAngularImpulse(-20f);
        
        setLeftPan(solver.makeRandomWeight());
    } 

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
//
	public PhysicsWorld getWorld() {
		return world;
	}

	public void makeWeight() {
		if (selected!= null && selected.hasBody) { //if selected exists and has a body
			if (moveSprite != null && moveSprite.getY() > sRod.getY() - sRod.getHeightScaled() / 2 && moveSprite.getY() < sRod.getY() + sRod.getHeightScaled() / 2 && moveSprite.getX() > BaseActivity.WIDTH / 2) { //and is in the correct range
				selected.body.setTransform(moveSprite.getX(), moveSprite.getY(), 0); //move selecteds body to movesprites location
			}
		}
		
		if (moveSprite != null && moveSprite.getY() > sRod.getY() - sRod.getHeightScaled() / 2 && moveSprite.getY() < sRod.getY() + sRod.getHeightScaled() / 2 && moveSprite.getX() > BaseActivity.WIDTH / 2) {
			wSprites.add(0, weights.createWeight(1, moveSprite.denominator, moveSprite.getX(), moveSprite.getY()));
			getWorld().registerPhysicsConnector(new PhysicsConnector(wSprites.get(0), wSprites.get(0).body));
			Resources.getInstance().creaking.play();
			this.attachChild(wSprites.get(0));
		}
		
		reRegister();

		if (Math.abs(rightWeight - getPanWeight(sRightPan)) < 0.0001) {
			if (won == false) {
				won = true;
				win();	
			}
		}
            	
	}
	
	public void reRegister() {
		for (int i = 0; i < this.getTouchAreas().size(); i++) {
			this.unregisterTouchArea(this.getTouchAreas().get(i));
		}
		for (int i = 0; i < weights.weights.length; i++) {
			this.registerTouchArea(weights.weights[i]);
		}
		for (int i = 0; i < wSprites.size(); i++) {
			this.registerTouchArea(wSprites.get(i));
		}
		for (int i = 0; i < weights.weights.length; i++) {
			this.registerTouchArea(weights.weights[i]);
		}
	}
	
	public float getPanWeight(Sprite pan) {
		float w = 0;
		for (int i = 0; i < wSprites.size() && wSprites.get(i) != null; i++) {
			if (solver.isOnPan(wSprites.get(i).getX(), wSprites.get(i).getY(), pan)) {
				w += ((wSprites.get(i).numerator * 1.0) / wSprites.get(i).denominator);
			}
				
		}
		return w;
	}
	
	
	public void setLeftPan(WeightSprite w) {
		w.touchable = false;
		getWorld().registerPhysicsConnector(new PhysicsConnector(w, w.body));
		this.attachChild(w);
		rightWeight = (float) w.numerator / (float) w.denominator;
		leftNum = w.numerator;
		leftDen = w.denominator;
	}
	
	public void win() {
		final int score = (int) getScore(time.getMs());
		play = new PlayScene(engine, activity, camera);
		time.updateScore();
		System.out.println("SCORE+++++++++++++++++++++++++ " + BaseActivity.prefs.getScore() + "                wins: " + BaseActivity.prefs.getWins());
		BaseActivity.prefs.addScore(score);
		BaseActivity.prefs.addWin();
        final Handler uiThreadHandler = new Handler(Looper.getMainLooper()); // Only the maint thread can create views
		uiThreadHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
				final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
				alertDialog.setTitle("You Made It!");
				alertDialog.setMessage(String.format("Congratulations!\nYou made %s/%s and got %s pt.",
						leftNum, leftDen, score));
				alertDialog.setIcon(android.R.drawable.btn_star_big_on);
				alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						clickOK();
					}
					
				});
				alertDialog.create();
				alertDialog.show();
				Resources.getInstance().win.play();
				won = false;
			}
		}, 2000);

	
	}
	
	public void clickOK() {
		
        play.createScene();
        play.start();
		engine.setScene(play);

		this.dispose();
	}
	
	public void registerTouch(Sprite sprite) {
		
	}
	public long getScore(long elapsedMS) {
		// calculates the score based on game play time
		final long MAX_SCORED_TIME_MS = TimeUnit.MINUTES.toMillis(3);
		final long LEAST_SCORE = 100;
		final long basicScore = (long)Math.pow( (MAX_SCORED_TIME_MS - Math.min(elapsedMS, MAX_SCORED_TIME_MS)) / 1000, 2);
		return  LEAST_SCORE + basicScore/10;
	}



	@Override
	public void loadResources() {
		// TODO Auto-generated method stub
		
	}
}













