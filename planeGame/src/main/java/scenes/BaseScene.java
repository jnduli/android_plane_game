package scenes;

import managers.ResourceManager;
import managers.SceneManager.SceneType;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

public abstract class BaseScene extends Scene {
	protected ResourceManager resourceManager;
	protected Engine engine;
	protected Activity activity;
	protected BoundCamera camera;
	protected VertexBufferObjectManager vbom;
	
	public BaseScene(){
		this.resourceManager = ResourceManager.getInstance();
		this.engine = resourceManager.engine;
		this.activity = resourceManager.activity;
		this.camera = resourceManager.camera;
		this.vbom = resourceManager.vbom;
		createScene();
	}
	
	public abstract void createScene();
	public abstract void onBackKeyPressed();
	public abstract void disposeScene();
	public abstract SceneType getSceneType();
}
