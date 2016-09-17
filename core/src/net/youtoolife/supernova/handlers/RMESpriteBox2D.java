package net.youtoolife.supernova.handlers;

import static net.youtoolife.supernova.handlers.B2DVars.MP;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class RMESpriteBox2D extends RMESprite implements Json.Serializable {
	
	//--Physics--//
		protected Body body = null;

	    public RMESpriteBox2D(Texture texture, float x, float y) {
	    		super(texture, x, y);
	    }
		
		public RMESpriteBox2D(Texture ws, int frame_cols, int frame_rows, int animStart, int animStop, boolean animActive, float animSpeed) {
			super(ws, frame_cols, frame_rows, animStart, animStop, animActive, animSpeed);
		}
		
	    public RMESpriteBox2D() {
	    		super();
	    }
	    
	    
	    public Body getBody() {
	    		return body;
	    }
	    
	    public void setBody(Body body) {
	    		this.body = body;
	    }
		
		public void draw(float delta) {
		}

		@Override
		public void write(Json json) {
			super.write(json);
		}

		@Override
		public void read(Json json, JsonValue jsonData) {
			super.read(json, jsonData);
		}

}
