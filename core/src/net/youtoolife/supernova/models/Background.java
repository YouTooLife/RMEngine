package net.youtoolife.supernova.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.youtoolife.supernova.Assets;
import net.youtoolife.supernova.handlers.RMESprite;
import net.youtoolife.supernova.screens.Surface;

public class Background {
	
	OrthographicCamera guiCam;
	
	public Texture texture;
	
	Array<Vector2> vector2 = new Array<Vector2>(); 
	float width = (Surface.width/256+1), height = (Surface.height/256+1); 
	Vector2 lVec;
	
	public Background() {
		guiCam = Surface.guiCam;
		lVec = new Vector2(guiCam.position.x-Surface.width/2, guiCam.position.y-Surface.height/2);
		texture = Assets.getTexture("Engine/Background");
		for (int y = -5; y < height+5; y++)
			for (int x = -5; x < width+5; x++)
				vector2.add(new Vector2(lVec.x+x*256, lVec.y+y*256));
	}
	
	public Background(Texture texture) {
		this.texture = texture;
	}
	
	public void draw(SpriteBatch batcher) {
		update(Gdx.graphics.getDeltaTime());
		for (Vector2 vector:vector2)
			batcher.draw(texture, vector.x, vector.y);
	}
	
	
	public void update(float delta) {
		int index = 0;
		/*Vector2 guiVec = new Vector2(guiCam.position.x-Surface.width/2, 
				guiCam.position.y-Surface.height/2);
		if (lVec.x < guiVec.x)
			lVec.set(lVec.x+100*delta, lVec.y);
		if (lVec.x > guiVec.x)
			lVec.set(lVec.x-100*delta, lVec.y);
		if (lVec.y < guiVec.y)
			lVec.set(lVec.x, lVec.y+100*delta);
		if (lVec.y > guiVec.y)
			lVec.set(lVec.x, lVec.y-100*delta);*/
		
		Vector2 firstVec = vector2.get(index);
		firstVec.set(guiCam.position.x-Surface.width/2, guiCam.position.y-Surface.height/2);
		//firstVec.set(lVec.x, lVec.y);
		for (int y = -5; y < height+5; y++)
			for (int x = -5; x < width+5; x++) {
				if (index==0) {
					index++;
					continue;
				}
				vector2.get(index).set((firstVec.x+256*x), (firstVec.y+256*y));
				index++;
			}
	}
}
