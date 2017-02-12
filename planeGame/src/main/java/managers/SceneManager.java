package managers;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;



import scenes.BaseScene;
import scenes.GameScene;


public class SceneManager {
	
	private final static SceneManager INSTANCE = new SceneManager();
	
	private BaseScene splashScene, menuScene, gameScene, loadingScene, currentScene;
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;
	private Engine engine = ResourceManager.getInstance().engine;
	
	public enum SceneType{
		SCENE_MENU, SCENE_GAME, SCENE_LOADING, SCENE_SPLASH
	};
	public static SceneManager getInstance(){
		return INSTANCE;
	}
	public void setScene(BaseScene scene){
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}
	public void setScene(SceneType sceneType)
	    {
	        switch (sceneType)
	        {
	            case SCENE_MENU:
	                setScene(menuScene);
	                break;
	            case SCENE_GAME:
	                setScene(gameScene);
	                break;
	            case SCENE_SPLASH:
	                setScene(splashScene);
	                break;
	            case SCENE_LOADING:
	                setScene(loadingScene);
	                break;
	            default:
	                break;
	        }
	    }

	  public SceneType getCurrentSceneType(){
		  return currentSceneType;
	  }
	    
	  public BaseScene getCurrentScene(){
		  return currentScene;
	  }

	public void createGameScene(OnCreateSceneCallback ocsc){
		ResourceManager.getInstance().loadGameResources();
		gameScene = new GameScene();
		setScene(gameScene);
		currentScene = gameScene;
		ocsc.onCreateSceneFinished(gameScene);
		
	}
	

}
