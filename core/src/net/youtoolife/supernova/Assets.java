package net.youtoolife.supernova;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import net.youtoolife.supernova.handlers.RMESound;

public class Assets {
	
	public static Array<Texture> textures;
	private static Array<String> textureNames;
	
	public static Texture field;
	
	//public static String login = System.getProperty("user.name"), passWord = "toor";
	
	public static void load () {
		if (RMEBuilder.bassMode)
		RMESound.loadFiles("SFX/");
		loadIntTextures("textures/");
		loadTextures("textures/");
	}
	
	private static void loadIntTextures(String dir) {
		//field = new Texture(Gdx.files.internal(dir+"field.png"));
	}
	
	private static void loadTextures(String dir) {
		
		textures = new Array<Texture>();
		textureNames = new Array<String>();
		getSubDir(Gdx.files.local(dir));
	}
	
	public static Texture getTexture(String name) {
		Texture texture = null;
		try{
		texture = textures.get(textureNames.indexOf("textures/"+name, false));
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println(name);
		}
		return texture;
	}
	
	public static String getTextureName(Texture texture) {
		String name = null;
		try{
		name = textureNames.get(textures.indexOf(texture, false));
		//System.out.println(name);
		name = name.substring(name.lastIndexOf("textures/")+9);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println(texture.toString());
		}
		return name;
	}

	private static void getSubDir(FileHandle s) {
		FileHandle dir = s;
		FileHandle[] files = dir.list();
		for (FileHandle file: files) {	
			if(file.isDirectory())
				getSubDir(file);
			if (file.name().contains(".png")
					||file.name().contains(".jpg")
					||file.name().contains(".PNG")
					||file.name().contains(".JPG")) {
				
				Texture texture = new Texture(file);
				textures.add(texture);
				textureNames.add(s+"/"+file.nameWithoutExtension());
				//
			}
		}
	}
	
	
}
