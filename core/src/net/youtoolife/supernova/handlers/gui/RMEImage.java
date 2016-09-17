package net.youtoolife.supernova.handlers.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import net.youtoolife.supernova.handlers.RMESprite;
import net.youtoolife.supernova.screens.MainMenu;

public class RMEImage extends RMESprite implements Json.Serializable {
	
	public Rectangle bounds;
	public String action = "";
	public String texture = "";
	public int id = 0;
	
	public RMEImage() {
		 super();
		 bounds = new Rectangle(0,0, 128, 128);
	}
	
	public RMEImage(Texture ws, float x, float y) {
		super(ws, x, y);
		bounds = new Rectangle(x, y, ws.getWidth(), ws.getHeight());
		setSize(ws.getWidth(), ws.getHeight());
		setColor(MainMenu.currentColor);
	}
	
	public void update(float delta) {
		draw(delta);
		bounds.setPosition(getX()+bounds.width, getY()+bounds.height);
	}
	
	@Override
	public void write(Json json) {
		json.writeValue("action", action);
		json.writeValue("id", id);
		super.write(json);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		action = jsonData.getString("action");
		id = jsonData.getInt("id");
		super.read(json, jsonData);
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getAction() {
		return action;
	}
}
