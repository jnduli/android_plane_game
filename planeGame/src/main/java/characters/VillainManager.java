package characters;

import java.util.LinkedList;

import managers.ResourceManager;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.widget.Toast;

import com.somekenyan.maasai.Values;

import characters.PlaneHero.MissilePool;

public class VillainManager {
	LinkedList<Villain> villains;
	VertexBufferObjectManager vbom;
	Camera camera;
	public static VillainPool VILLAIN_POOL;
	
	public VillainManager(VertexBufferObjectManager vbo, Camera camera){
		vbom = vbo;
		this.camera = camera;
		villains = new LinkedList<VillainManager.Villain>();
		VILLAIN_POOL = new VillainPool(ResourceManager.getInstance().villain1Region);
	}
	public Villain[] generate(Scene s){
		Villain [] array = null;
		if(villains.isEmpty()){
			//array = new Villain[1];
			Villain v= VILLAIN_POOL.obtainPoolItem();
			s.attachChild(v);
			villains.add(v);	
		}else{
		if(Values.CAMERA_WIDTH -villains.getLast().getX() >170){
//			int num = MathUtils.random(0, 2);
	//		array = new Villain[num];
		//	for(int i=0; i<num; i++){
			
				Villain v = VILLAIN_POOL.obtainPoolItem();
				v.setPosition(Values.CAMERA_WIDTH ,MathUtils.random(0, Values.CAMERA_HEIGHT));
				s.attachChild(v);
				villains.add(v);
		//	}
		}
		}
		return array;
	}
	public void checkOffScreen(Scene sc){
		for(Villain v: villains){
			if(v.getX()+v.getWidth()<camera.getXMin()){
				sc.detachChild(v);
				if(villains.remove(v))
					VILLAIN_POOL.recyclePoolItem(v);
			}			
		}
	}
	public boolean checkCollisions(Sprite s,Scene sc){
		for(Villain v: villains){
			if (s.collidesWith(v)){
				sc.detachChild(v);
				if(villains.remove(v))
					VILLAIN_POOL.recyclePoolItem(v);
				return true;
			}
			//return false;
		}
		return false;
	}
	public LinkedList<Villain> getVillains(){
		return villains;
	}
	
	
	public class Villain extends AnimatedSprite{

		public Villain(float pX, float pY, ITiledTextureRegion itexture, VertexBufferObjectManager pVertexBufferObjectManager, Camera camera) {
			super(pX, pY,itexture, pVertexBufferObjectManager);
			createVelocity();
			startAnimation();
		}
		
		public Villain(ITiledTextureRegion itexture, VertexBufferObjectManager vbom, Camera camera){
			this(Values.CAMERA_WIDTH ,MathUtils.random(0, Values.CAMERA_HEIGHT),itexture, vbom, camera);			
		}
		public void startAnimation(){
			long[] animate = new long[]{100,250,300,300,250,200,100};
			animate(animate, 0, 6, true);
		}
		
		public void createVelocity(){
			PhysicsHandler handler = new PhysicsHandler(this);
			this.registerUpdateHandler(handler);
			handler.setVelocityX(-50);
		}
		public void checkCollision(Sprite s){
			
		}
		
	}
	public class VillainPool extends GenericPool<Villain>{
		private ITiledTextureRegion mTextureRegion;
		
		public VillainPool(ITiledTextureRegion mtexture) {
			if(mtexture ==null)
				throw new IllegalArgumentException("Texture must not be null");
			mTextureRegion= mtexture;
		}

		protected void onHandleRecycleItem(Sprite pItem) {
			//pItem.detachSelf();
			pItem.clearEntityModifiers();
			pItem.clearUpdateHandlers();
			pItem.setIgnoreUpdate(true);
			pItem.setVisible(false);
			
			pItem.reset();
		}

		@Override
		protected Villain onAllocatePoolItem() {
			return new Villain(mTextureRegion,vbom, camera);
		}	
		
	}

}
