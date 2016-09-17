package net.youtoolife.supernova.models;

import static net.youtoolife.supernova.handlers.B2DVars.MP;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import net.youtoolife.supernova.handlers.B2DVars;
import net.youtoolife.supernova.handlers.RMESprite;
import net.youtoolife.supernova.handlers.RMESpriteBox2D;
import net.youtoolife.supernova.screens.Surface;

public class Wall extends RMESprite implements Json.Serializable {
	
	//public Rectangle bounds;
	private int cl = 0;
	private boolean rect = false, draw = false;
	private int hp = 1000;
	
	World world = null;
	Body body = null;
	
	public Wall() {
		 super();
		 //bounds = new Rectangle(0,0, 128, 128);
	}
	
	public void createBody() {
 		
    	if (world == null)
    		world = Surface.world;
    	
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		
		bodyDef.position.set(
				(getX()-getWidth()/2.f)/MP,
				(getY()-getHeight()/2.f)/MP);
		bodyDef.fixedRotation = true;
		body = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		Vector2[] vert = {new Vector2(0,0), 
				new Vector2(getWidth()/MP,0), 
				new Vector2(getWidth()/MP,getHeight()/MP), 
				new Vector2(0,getHeight()/MP)};
		shape.set(vert);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1000.f;
		fixtureDef.friction = 150.f/MP;
		fixtureDef.filter.maskBits = B2DVars.WALL_BIT;
		fixtureDef.filter.categoryBits = B2DVars.PLAYER_BIT;
		//fixtureDef.restitution = 0.01f/MP; 
		body.createFixture(fixtureDef);
		body.setUserData(this);
		shape.dispose();
	}
	
	public Wall(Texture ws, float x, float y, boolean draw, boolean rect) {
		super(ws, x, y);
		this.setDraw(draw);
		this.setRect(rect);
		//bounds = new Rectangle(x, y, 128, 128);
		setSize(128, 128);
		setColor(Surface.currentColor);
		createBody();
	}
	
	public Wall(Texture ws, int frame_cols, int frame_rows, int animStart, int animStop, boolean animActive, float animSpeed) {
		super(ws, frame_cols, frame_rows, animStart, animStop, animActive, animSpeed);
		createBody();
	}
	
	public void update(float delta) {
		draw(delta);
		//bounds.setPosition(getX()+bounds.width, getY()+bounds.height);
		if (hp <= 0)
		Surface.pack.removeWall(this);
		
		//if (body != null)
		//	setPosition(body.getPosition().x*MP+getWidth()/2, body.getPosition().y*MP+getHeight()/2);
	}
	
	@Override
	public void write(Json json) {
		json.writeValue("hp", hp);
		json.writeValue("ctype", cl);
		json.writeValue("drect", isRect());
		json.writeValue("draw", isDraw());
		super.write(json);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		hp = jsonData.getInt("hp");
		cl = jsonData.getInt("ctype");
		setRect(jsonData.getBoolean("drect"));
		setDraw(jsonData.getBoolean("draw"));
		super.read(json, jsonData);
		createBody();
	}

	public boolean isDraw() {
		return draw;
	}

	public void setDraw(boolean draw) {
		this.draw = draw;
	}

	public boolean isRect() {
		return rect;
	}

	public void setRect(boolean rect) {
		this.rect = rect;
	}
	
	public void addHP(float hp) {
		this.hp += hp;
	}
	
	public int getHP() {
		return hp;
	}
	
	public Body getBody() {
		return this.body;
	}
}
