package characters;

import java.util.LinkedList;

import managers.ResourceManager;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;
import org.andengine.util.math.MathUtils;

public class PlaneHero extends AnimatedSprite {
	
	public boolean isRunning = false;
	public int velocity = 100;
	private Camera camera;
	private Sprite missile;
	private VertexBufferObjectManager vbom;
	public static MissilePool MISSILE_POOL;
	LinkedList<Sprite> missiles;
	public PlaneHero(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, Camera camera) {
		super(pX, pY,ResourceManager.getInstance().planeHeroRegion, pVertexBufferObjectManager);
		vbom = pVertexBufferObjectManager;
		missiles = new LinkedList<Sprite>();
		MISSILE_POOL = new MissilePool(ResourceManager.getInstance().missileRegion);
		//camera.setChaseEntity(this);
	}
	public void startAnimation(){
		long[] animate = new long[]{100,250,300,300,250,200,100};
		animate(animate, 0, 6, true);
	}
	public void stopAnimation(){
		this.stopAnimation();
	}
	
	public Sprite shoot(float pValueX, float pValueY){
		Sprite missile = MISSILE_POOL.obtainPoolItem();
		missile.setPosition(this.getX()+this.getWidth()-10, this.getY());
		missile.setRotation(MathUtils.radToDeg((float)Math.atan2(pValueX, pValueY)));
		PhysicsHandler handler = new PhysicsHandler(missile);
		missile.registerUpdateHandler(handler);
		handler.setVelocity(pValueX*200, pValueY*200);
		missiles.add(missile);
		return missile;
	}
	public LinkedList<Sprite> getMissiles(){
		return missiles;
	}
	
	public boolean checkMissile(Sprite missile, Scene s){
		/*if(missile.getX()>camera.getWidth()|| missile.getY()>camera.getHeight()){
			s.detachChild(missile);
			returnMissileToPool(missile);
			return true;
		}*/
		return true;
	}
	public void returnMissileToPool(Sprite s){
		MISSILE_POOL.recyclePoolItem(s);
	}
	public void move(int y){
		this.setPosition(this.getX()+y, this.getY()+y);
	}
	
	public class MissilePool extends GenericPool<Sprite>{

		private ITextureRegion mTextureRegion;
		
		public MissilePool(ITextureRegion mtexture) {
			if(mtexture ==null)
				throw new IllegalArgumentException("Texture must not be null");
			mTextureRegion= mtexture;
		}

		@Override
		protected void onHandleRecycleItem(Sprite pItem) {
			pItem.clearEntityModifiers();
			pItem.clearUpdateHandlers();
			pItem.setVisible(false);
			pItem.detachSelf();
			pItem.reset();
		}

		@Override
		protected Sprite onAllocatePoolItem() {
			// TODO Auto-generated method stub
			return new Sprite(0, 0, mTextureRegion, vbom);
		}	
	}
	

}
