package scenes;

import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.somekenyan.maasai.Values;

import characters.PlaneHero;
import characters.SimpleChar;
import characters.VillainManager;
import characters.VillainManager.Villain;

import managers.ResourceManager;
import managers.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnSceneTouchListener{

	private PlaneHero planeHero;
	HUD controllers;
	SimpleChar s,d;
	Text scoreText;
	int score =0;
	PhysicsWorld physicsWorld;
	VillainManager vm;
	private boolean shootControl = false;
	private boolean mPlaceOnScreenControlsAtDifferentVerticalLocations = false;
	@Override
	public void createScene() {
		vm = new VillainManager(vbom, camera);
		createBackground();
		createPlayer();
		createControllers();
		
//		setOnSceneTouchListener(this);
	}
	
	private void createBackground(){
//		setBackground(new Background(Color.BLUE));
		AutoParallaxBackground background = new AutoParallaxBackground(0,0,0,5);
		setBackground(background);
		
		Sprite parallaxBackSprite = new Sprite(0,0,ResourceManager.getInstance().mParallaxBackTexture, vbom);
		parallaxBackSprite.setOffsetCenter(0, 0);
		background.attachParallaxEntity(new ParallaxEntity(0, parallaxBackSprite));

		Sprite parallaxMidSprite = new Sprite(0,Values.CAMERA_HEIGHT - ResourceManager.getInstance().mParallaxMidTexture.getHeight()-80, ResourceManager.getInstance().mParallaxMidTexture, vbom);
		parallaxMidSprite.setOffsetCenter(0,0);
		background.attachParallaxEntity(new ParallaxEntity(-5.0f, parallaxMidSprite));

		Sprite parallaxFrontSprite = new Sprite(0,0, ResourceManager.getInstance().mParallaxFrontTexture, vbom);
		parallaxFrontSprite.setOffsetCenter(0, 0);
		background.attachParallaxEntity(new ParallaxEntity(-10.0f, parallaxFrontSprite));

	}
	private void createControllers(){
		final PhysicsHandler physicsHandler = new PhysicsHandler(planeHero);
		planeHero.registerUpdateHandler(physicsHandler);

		/* Velocity control (left). */
		final AnalogOnScreenControl velocityOnScreenControl = new AnalogOnScreenControl(0, 0, camera,ResourceManager.getInstance().hudCircleRegion, ResourceManager.getInstance().monScreenKnob, 0.1f, vbom, new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				if(pValueX == 0 && pValueY == 0) {
					//Debug.e("X is "+planeHero.getX());
					if(planeHero.isRunning == true){
					//	planeHero.isRunning = false;
						//planeHero.stopAnimation();
					}
					physicsHandler.setVelocity(0, 0);
				}else{
					if(planeHero.isRunning == false){
						planeHero.isRunning = true;
						planeHero.startAnimation();
					}
					//planeHero.startAnimation();
					if(planeHero.getX() >= camera.getXMax()){
						if(pValueX >0)
							physicsHandler.setVelocity(0, pValueY * 100);
						else 
							physicsHandler.setVelocity(pValueX * 100, pValueY * 100);
					}else if(planeHero.getX() < 0 ){
						if(pValueX <0)
							physicsHandler.setVelocity(0, pValueY * 100);
						else 
							physicsHandler.setVelocity(pValueX * 100, pValueY * 100);					
					} else if(planeHero.getY() <= camera.getYMin()){
						if(pValueY <0)
							physicsHandler.setVelocity(pValueX * 100, 0);
						else 
							physicsHandler.setVelocity(pValueX * 100, pValueY * 100);
	
					}else if(planeHero.getY() >=camera.getYMax()){
						if(pValueX >0)
							physicsHandler.setVelocity(pValueX * 100, 0);
						else 
							physicsHandler.setVelocity(pValueX * 100, pValueY * 100);
			
					}else {
						physicsHandler.setVelocity(pValueX * 100, pValueY * 100);
	//					planeHero.setRotation(MathUtils.radToDeg((float)Math.atan2(pValueX, pValueY)));
					}
				}


			}

			@Override
			public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
			}
		});

		{
			final Sprite controlBase = velocityOnScreenControl.getControlBase();
			controlBase.setAlpha(0.5f);
			controlBase.setOffsetCenter(0, 0);
	
			setChildScene(velocityOnScreenControl);
		}


		/* Rotation control (right). */
		final float y = (this.mPlaceOnScreenControlsAtDifferentVerticalLocations) ? Values.CAMERA_HEIGHT : 0;
		final AnalogOnScreenControl rotationOnScreenControl = new AnalogOnScreenControl(Values.CAMERA_WIDTH, y, camera,ResourceManager.getInstance().hudCircleRegion, ResourceManager.getInstance().monScreenKnob, 0.1f, vbom, new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				if(pValueX == 0 && pValueY == 0) {
					shootControl = true;
			//		planeHero.setRotation(0);
				} else {
					if(shootControl){
						attachChild(planeHero.shoot(pValueX, pValueY));
						shootControl =false;
					}
//					planeHero.setRotation(MathUtils.radToDeg((float)Math.atan2(pValueX, pValueY)));
				}
			}

			@Override
			public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
				/* Nothing. */
				//attachChild(planeHero.shoot(9, 9));
			}
		});

		{
			final Sprite controlBase = rotationOnScreenControl.getControlBase();
			if(this.mPlaceOnScreenControlsAtDifferentVerticalLocations) {
				controlBase.setOffsetCenter(1, 1);
			} else {
				controlBase.setOffsetCenter(1, 0);
			}
			controlBase.setAlpha(0.5f);
	
			velocityOnScreenControl.setChildScene(rotationOnScreenControl);
		}
		
		this.registerUpdateHandler(new DetectUpdater());

	}
	private void addToScore(int i){
		score += i;
		scoreText.setText("Score:"+score);
	}

	private void createPlayer(){
		//s = new SimpleChar(10, 100, vbom, camera);
		//attachChild(s);
		planeHero = new PlaneHero(50, 120, vbom, camera);
		attachChild(planeHero);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		// TODO Auto-generated method stub
		return super
				.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}
	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return false;
	}
	
	private class DetectUpdater implements IUpdateHandler{

		@Override
		public void onUpdate(float pSecondsElapsed) {
			//vm.checkOffScreen(GameScene.this);
//			//LinkedList<Villain> villains = vm.getVillains();
			for(Sprite m : planeHero.getMissiles()){
				//if(!planeHero.checkMissile(m,GameScene.this)){
				if(vm.checkCollisions(m,GameScene.this)){
					GameScene.this.detachChild(m);
					planeHero.returnMissileToPool(m);
				}
				//}
				
			}
			//vm.checkOffScreen(GameScene.this);
			
			vm.generate(GameScene.this);
		//	if(arr!=null){
			//for(int i=0;i< arr.length; i++){
				//attachChild(arr[i]);
			//}}
		}

		@Override
		public void reset() {
			// TODO Auto-generated method stub
			
		}
		
		
	}
}
