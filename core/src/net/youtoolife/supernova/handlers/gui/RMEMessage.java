package net.youtoolife.supernova.handlers.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;

import net.youtoolife.supernova.Assets;
import net.youtoolife.supernova.screens.Surface;

public class RMEMessage {
	
	public String msg;
	public Array<String> strs;
	public String sfx;
	public float ctime = 0;
	public float time;
	
	private BitmapFont font;
	public String fontName = "HelveticaNeue.fnt";
	
	
	public RMEMessage(String args) {
		String[] str = args.split("::");
		msg = str[0];
		time = Float.parseFloat(str[1]);
		if (str.length > 2)
			sfx = str[2];
		if (str.length > 3)
			fontName = str[3];
		init();
	}
	
	private void init() {
		font = Assets.getFontByName(fontName);
	}
	
	
	public void draw(SpriteBatch batch, ShapeRenderer rect) {
		
		if (ctime < time) {
			ctime += Gdx.graphics.getDeltaTime();
			if (ctime >= time) {
				Surface.pack.removeMsg(this);
			}
		}
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		rect.setColor(new Color(0.f, 0.f, 0.f, 0.85f));
		rect.begin(ShapeType.Filled);
		rect.rect(Surface.guiCam.position.x-Surface.width/4-10, 
				Surface.guiCam.position.y-Surface.height/3.5f-Surface.height/5+10, 
				Surface.width/2+20, Surface.height/3.5f+20);
		rect.end();
		
		batch.begin();
		font.draw(batch, msg, Surface.guiCam.position.x-Surface.width/4, 
				Surface.guiCam.position.y-Surface.height/5, Gdx.graphics.getWidth()/2, 1, true);
		batch.end();
	}


}
