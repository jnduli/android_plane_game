package managers;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

import com.somekenyan.maasai.GameActivity;

public class ResourceManager {
	public static final ResourceManager INSTANCE = new ResourceManager();
	
	public Engine engine;
	public GameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	
	public BuildableBitmapTextureAtlas gameTextureAtlas, gameBackgroundTextureAtlas;
	public ITiledTextureRegion playerRegion;
	public ITiledTextureRegion planeHeroRegion, villain1Region;
	public ITextureRegion mParallaxBackTexture,mParallaxMidTexture,mParallaxFrontTexture;
	public ITextureRegion hudCircleRegion, missileRegion, monScreenKnob, monScreenMenu;
	public Font font;
	
    public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vertexBufferObjectManager){
    	ResourceManager.getInstance().engine = engine;
    	ResourceManager.getInstance().activity = activity;
    	ResourceManager.getInstance().camera =(BoundCamera) camera;
    	ResourceManager.getInstance().vbom = vertexBufferObjectManager;
    }
    public static ResourceManager getInstance(){
    	return INSTANCE;
    }
    public void loadGameResources(){
    	loadGameGraphics();
    	loadGameFonts();
    }
    public void loadMenuResources(){}
    
    public void loadGameFonts(){
		FontFactory.setAssetBasePath("fonts/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = FontFactory.createStrokeFromAsset(activity.getFontManager(),mainFontTexture, activity.getAssets(), "font.ttf", 50, true,Color.WHITE, 2, android.graphics.Color.BLACK);
		font.load();

    	
    }
	public void loadGameGraphics(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);
		playerRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "mine.png", 7, 1);
		planeHeroRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "planeherotiled.png", 7, 1);
		villain1Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "villain1.png", 7, 1);
		missileRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "missile.png");
		hudCircleRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hudcircle.png");
		monScreenKnob = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "onscreen_control_knob.png");
		
		gameBackgroundTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		mParallaxBackTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBackgroundTextureAtlas, activity, "parallax_background_layer_back.png");
		mParallaxMidTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBackgroundTextureAtlas, activity, "parallax_background_layer_mid.png");
		mParallaxFrontTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBackgroundTextureAtlas, activity, "parallax_background_layer_front.png");
		try {
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
			gameTextureAtlas.load();
			gameBackgroundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			gameBackgroundTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}


}
