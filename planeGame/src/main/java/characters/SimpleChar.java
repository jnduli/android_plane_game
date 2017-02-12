package characters;

import managers.ResourceManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;



public class SimpleChar extends AnimatedSprite {

	public SimpleChar(float pX, float pY, VertexBufferObjectManager vbom, Camera camera) {
		super(pX, pY, ResourceManager.getInstance().playerRegion, vbom);
		camera.setChaseEntity(this);
	}
	public void setRunning()
	{
		int pX=10, pY=100;
	    final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100,100,100,100,100 };       
	    animate(PLAYER_ANIMATE, 0, 6, true);
	    //registerEntityModifier(new MoveModifier(0.5f,pX,pY,pX+100,pY+100));
	}


}
