package net.youtoolife.supernova;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.youtoolife.supernova.handlers.RMESound;
import net.youtoolife.supernova.screens.Surface;

public class RMEBuilder extends Game {
	
	public SpriteBatch batcher;
	public static boolean bassMode = true;

	@Override
	public void create () {
		batcher = new SpriteBatch();
		
		if (bassMode)
		RMESound.initNativeBass();
		Assets.load();
		setScreen(new Surface(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
