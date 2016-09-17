package net.youtoolife.tools.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.youtoolife.supernova.RMEBuilder;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 1330;
		//config.height = 764;
		config.width = 1280;
		config.height = 768;
		//config.fullscreen = true;
		new LwjglApplication(new RMEBuilder(), config);
		//
	}
}
