package net.youtoolife.supernova.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

public class RMESettings implements Serializable {
	
	public int width = 1280;
	public int height = 768;
	
	public String lang = "ru";
	
	public ObjectMap<String, int[]> keys;
	
	public RMESettings() {
		
	}
	
	public RMESettings(FileHandle file) {
		
		keys = new ObjectMap<String, int[]>();
		keys.put("left", new int[] {Keys.A, Keys.LEFT});
		keys.put("up", new int[] {Keys.W, Keys.UP});
		keys.put("right", new int[] {Keys.D, Keys.RIGHT});
		keys.put("down", new int[] {Keys.S, Keys.DOWN});
		
		Json json = new Json();
		json.toJson(this, file);
	}
	
	
	public boolean cmpKeys(String keyName, int inKey) {
	  	int[] arr = keys.get(keyName);
	  	for (int a:arr)
	  		if (a == inKey)
	  			return true;
	  	return false;
	}

	@Override
	public void write(Json json) {
		json.writeValue("Width", width);
		json.writeValue("Height", height);
		json.writeValue("Keys", keys, ObjectMap.class);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void read(Json json, JsonValue jsonData) {
		width = jsonData.getInt("Width");
		height = jsonData.getInt("Height");
		keys = json.readValue("Keys", ObjectMap.class, jsonData);
	}

}
