package net.youtoolife.supernova;


import java.awt.datatransfer.StringSelection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

import net.youtoolife.supernova.handlers.RMESettings;
import net.youtoolife.supernova.handlers.RMESound;

public class Assets {
	
	
	public static boolean bassMode = true;
	public static String lang = "ru";
	public static String login = System.getProperty("user.name"), passWord = "toor";
	
	public static RMESettings settings;
	
	public static Array<Texture> textures;
	private static Array<String> textureNames;
	
	public static ObjectMap<String, String> strs;
	public static ObjectMap<String, BitmapFont> fonts;
	
	public static Texture field;
	
	
	
	public static void load () {
		
		if (bassMode) {
			RMESound.initNativeBass();
			RMESound.loadFiles("sounds/");
			RMESound.loadFiles("SFX/");
		}
		loadIntTextures("textures/");
		loadTextures("textures/");
		loadStrings("contents/");
		loadSettings("contents/");
		loadFonts("contents/");
	}
	
	
	private static void loadFonts(String dir) {
		
		fonts = new ObjectMap<String, BitmapFont>();
		FileHandle[] files = Gdx.files.local(dir).list();
		for (FileHandle file:files) {
			if (file.exists() && file.extension().equalsIgnoreCase("fnt")) {
				System.out.println(file);
				fonts.put(file.name(), new BitmapFont(file));
			}
		}
		
		System.out.println("Bitmapfonts succesfully loaded");
		
	}
	
	
	
	public static String getStr(String key) {
		if (strs.containsKey(key)) {
			return strs.get(key);
		}
		else {
			strs.put(key, key);
			FileHandle file = Gdx.files.local("contents/"+lang+"/strings.jsl");
			Json json = new Json();
			json.toJson(strs, file);
			return key;
		}
		
	}
	
	public static BitmapFont getFontByName(String name) {
		return fonts.get(name);
	}
	
	
	
	
	private static void loadSettings(String dir) {
		
		FileHandle file = Gdx.files.local(dir+"settings.jsf");
		Json json = new Json();
		if (file.exists()) {
			settings = json.fromJson(RMESettings.class, file);
			System.out.println("Settings succesfully loaded");
		}
		else
			settings = new RMESettings(file);
		
	}
	
	@SuppressWarnings("unchecked")
	private static void loadStrings(String dir) {
		strs = new ObjectMap<String, String>();
		
		FileHandle file = Gdx.files.local(dir+lang+"/strings.jsl");
		Json json = new Json();
		if (file.exists()) 
		{
			strs = json.fromJson(ObjectMap.class, file);
			System.out.println("Strings succesfully loaded");
		}
		else
			json.toJson(strs, file);
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
