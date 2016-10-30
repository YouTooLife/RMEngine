package net.youtoolife.supernova.models;


import static net.youtoolife.supernova.handlers.B2DVars.MP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import net.youtoolife.supernova.Assets;
import net.youtoolife.supernova.handlers.B2DVars;
import net.youtoolife.supernova.handlers.RMEPack;
import net.youtoolife.supernova.handlers.RMESprite;
import net.youtoolife.supernova.screens.Surface;

public class Player extends RMESprite implements Json.Serializable {
	
	 int speed = 250;
	 float speedX = 0.f, speedY = 0.f;
	//Circle bounds;
	 public Rectangle bounds;
	 public float rot = 0;
	 
	 public Color color = new Color(1.f, 0.f, 0.f, 1.f);
	 public RMESprite circle = new RMESprite(Assets.getTexture("Player/neba"), 0, 0);
	 
	 public int hp = 100;
	 private float sX, sY;
	 private Vector2 checkPoint = new Vector2();
	 private int checkPointCharge = 3;
	 
	 boolean fire = false;
	 float charge = 3.f;
	 float space = 30;
	 boolean attack = false;
	 
	 World world = null;
	 public Body body = null;
	 
	 
	 public Vector2 keyForce = new Vector2(0,0);
	 
	 private boolean fall = false;
	 
	 public Player() {
		 super();
		 bounds = new Rectangle(0,0, 128/9f*7, 128/9f*7);
	 }
	 
	 public void createBody() {
 		
	    	if (world == null)
	    		world = Surface.world;
	    	
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyDef.BodyType.DynamicBody;
			//System.out.println("ORIG: "+getX()+":"+getY());
			bodyDef.position.set(
					(getX())/MP,
					(getY())/MP);
			bodyDef.fixedRotation = true;
			
			body = world.createBody(bodyDef);
			
			CircleShape shape = new CircleShape();
			shape.setRadius(55.f/MP);
			
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = shape;
			fixtureDef.density = 100.0f;
			fixtureDef.friction = 150.f/MP;
			fixtureDef.filter.maskBits = B2DVars.PLAYER_BIT;
			fixtureDef.filter.categoryBits = B2DVars.DOOR_BIT | B2DVars.SURFACE_BIT | B2DVars.WALL_BIT;
			fixtureDef.restitution = 10.01f/MP; 
			body.createFixture(fixtureDef);
			body.setUserData(this);
			shape.dispose();
			//System.out.print("body: "+body.getUserData());
		}
	 
	 public void update(float delta) {
			draw(delta);
			
			collisionSurface();
			circle.setPosition(getX()+getWidth()/2-circle.getWidth()/2, getY()+getHeight()/2-circle.getHeight()/2);
			//bounds.setPosition(getX()+getWidth()/2, getY()+getHeight()/2);
			//bounds.setRadius(getRegionWidth()/2-getHeight()/2/8);
			bounds.setPosition(getX()+bounds.width/9f*1.3f, getY()+bounds.height/9f*1.3f);
			inputHandler(delta);
			
			//collision();
			
			if (attack) {
			if (rot < 360)
			rot+=delta*200;
			else rot = 0;
			
			circle.setRotation(rot-45);
			Color cl = circle.getColor();
			if (charge < 5) {
				charge+=delta;
				if (charge >= 5)
					charge = 5;
			}
			if (cl.a < 1)
				circle.setColor(new Color(cl.r, cl.g, cl.b, 1/5.f*charge));
			}
			
			if (space < 30) {
				space+=delta;
				if (space >= 30) {
					attack = false;
				}
			}
			fall(delta);
			
			if (body != null)
				setPosition(body.getPosition().x*MP, body.getPosition().y*MP);
			
			if (hp <= 0)
				goToCheckPoint();
			//this.rotate(delta*50);
			//if (Math.abs(speedX)>3)
			//	this.rotate(delta*-speedX*35);
			//else
			//	this.rotate(delta*-speedY*35);
			/*if (speedX > 1 && Math.abs(speedY) < 1) {
				if (getRotation() > -90)
					rotate(-50*delta);
				if (getRotation() < -90)
					rotate(50*delta);
			}
			if (speedX < -1 && Math.abs(speedY) < 1) {
				if (getRotation() < 90)
					rotate(50*delta);
				if (getRotation() > 90)
					rotate(-50*delta);
			}
			if (speedY > 1 && Math.abs(speedX) < 1) { 
				if (getRotation() > 0)
					rotate(-50*delta);
				if (getRotation() < 0)
					rotate(50*delta);
			}
			if (speedY < -1 && Math.abs(speedX) < 1) { 
				if (getRotation() > 180)
					rotate(-50*delta);
				if (getRotation() < 180)
					rotate(50*delta);
			}
			if (speedX > 1 && speedX > 1) { 
				if (getRotation() > -45)
					rotate(-50*delta);
				if (getRotation() < 45)
					rotate(50*delta);
			}
			if (speedX < 1 && speedX > 1) { 
				if (getRotation() > -45)
					rotate(-50*delta);
				if (getRotation() < -45)
					rotate(50*delta); 
			}
			if (speedX < 1 && speedX < 1) { 
				if (getRotation() > (90+45))
					rotate(-50*delta);
				if (getRotation() < (90+45))
					rotate(50*delta);
			}
			if (speedX > 1 && speedX < 1) { 
				if (getRotation() > -(90+45))
					rotate(-50*delta);
				if (getRotation() < -(90+45))
					rotate(50*delta);
			}*/
			
			
			
		}
	

	public Player(Texture ws, float x, float y) {
		super(ws, x, y);
		//bounds = new Circle(x, y, ws.getWidth());
		//bounds = new Rectangle(x, y, ws.getWidth()/9f*7, ws.getHeight()/9f*7);
		bounds = new Rectangle(x, y, 128/9f*7, 128/9f*7);
		checkPoint.set(x, y);
		//setColor(color);
		createBody();
	}
	
	public Player(Texture ws, int frame_cols, int frame_rows, int animStart, int animStop, boolean animActive, float animSpeed) {
		super(ws, frame_cols, frame_rows, animStart, animStop, animActive, animSpeed);
		//bounds = new Circle(0, 0, ws.getWidth());
		bounds = new Rectangle(0, 0, ws.getWidth()/9f*7, ws.getHeight()/9f*7);
		createBody();
	}
	
	public void fall(float delta) {
		if (fall) {
			setSize(getWidth()-300*delta, getHeight()-300*delta);
			if (getWidth() < 10) 
				goToCheckPoint();
		}
	}
	
	
	public void goToCheckPoint() {
		
		if (checkPointCharge > 0) {
			setPosition(checkPoint.x, checkPoint.y);
			hp = 100;
			setAlpha(1.f);
			checkPointCharge--;
		}
		
	}
	
	/*public void wallForce(RMESprite wall) {
		speedX = 0;
		speedY = 0;
		int max = 20;
		if (bounds.y+bounds.height <= (wall.getBoundingRectangle().y+max)) {
			System.out.println("DOWN");
			setPosition(getX(), getY()-(bounds.y+bounds.height-wall.getBoundingRectangle().y));
		}
		if (bounds.y >= (wall.getBoundingRectangle().y+wall.getBoundingRectangle().height-max)) {
			System.out.println("UP");
			setPosition(getX(), getY()+(wall.getBoundingRectangle().y+wall.getBoundingRectangle().height-bounds.y));
		}
		if (bounds.x+bounds.width <= (wall.getBoundingRectangle().x+max)) {
			System.out.println("LEFT");
			setPosition(getX()-(bounds.x+bounds.width-wall.getBoundingRectangle().x), getY());
		}
		if (bounds.x >= (wall.getBoundingRectangle().x+wall.getBoundingRectangle().width-max)) {
			System.out.println("RIGHT");
			setPosition(getX()+(wall.getBoundingRectangle().x+wall.getBoundingRectangle().width-bounds.x), getY());
		}
	}*/
	
	/*public void collisionDoor() {
		Array<Door> doors = Surface.pack.getDoors();
		if (doors != null)
		for (int i = 0; i < doors.size; i++) {
			Door door = doors.get(i);
		if (door.getBoundingRectangle().overlaps(bounds)) {
			if (((getColor().r*1000000+getColor().g*1000+getColor().b) ==
				(door.getColor().r*1000000+door.getColor().g*1000+door.getColor().b)))
			//flag = true;
				doors.removeValue(door, false);
			else
				wallForce(door);
			//System.out.println((getColor().r*1000000+getColor().g*1000+getColor().b));
			//System.out.println((door.getColor().r*1000000+door.getColor().g*1000+door.getColor().b));
		} 
		}
	}
	
	public void collisionOpponent() {
		Array<Opponent> doors = Surface.pack.getOpps();
		if (doors != null)
		for (int i = 0; i < doors.size; i++) {
			Opponent door = doors.get(i);
		if (door.getBoundingRectangle().overlaps(bounds)) {
			
				wallForce(door);
			//System.out.println((getColor().r*1000000+getColor().g*1000+getColor().b));
			//System.out.println((door.getColor().r*1000000+door.getColor().g*1000+door.getColor().b));
			} 
		}
	}
	
	public void collisionWall() {
		Array<Wall> walls = Surface.pack.getWalls();
		if (walls != null)
		for (int i = 0; i < Surface.pack.getWalls().size; i++)
		if (walls.get(i).getBoundingRectangle().overlaps(bounds)) {
			//color.set(1.f, 0.f, 0.f, 0.f);
			wallForce(walls.get(i));
		}
	}
	
	public void collisionCheckPoint() {
		Array<CheckPoint> checkPoints = Surface.pack.getCheckPoints();
		if (checkPoints != null)
		for (CheckPoint checkPoint:checkPoints)
		if (checkPoint.getBoundingRectangle().overlaps(bounds)) {
			this.checkPoint.set(checkPoint.getX(), checkPoint.getY());
			this.checkPointCharge = 3;
		}
	}
	
	public void collisionSurface() {
		Array<SurfaceX> surfaces = Surface.pack.getSurface();
		fall = true;
		if (surfaces != null)
		for (SurfaceX surface: surfaces)
		if (surface.getBoundingRectangle().overlaps(bounds)) {
			if (fall)
				setSize(getTexture().getWidth(), getTexture().getHeight());
			fall = false;
			break;
		}
	}
	
	public void collisionObject() {
		Array<ObjectX> objs = Surface.pack.getObjects();
		if (objs != null)
		for (ObjectX object: objs)
		if (object.getBoundingRectangle().overlaps(bounds)) {
			//setColor(object.getColor());
			setColorP(object.getColor());
			objs.removeValue(object, false);
		}
	}
	
	public void collision() {
		collisionSurface();
		collisionWall();
		collisionDoor();
		collisionCheckPoint();
		collisionObject();
		collisionOpponent();
	}*/
	
	public void collisionSurface() {
		Array<SurfaceX> surfaces = Surface.pack.getSurface();
		fall = true;
		if (surfaces != null)
		for (SurfaceX surface: surfaces)
		if (surface.getBoundingRectangle().overlaps(bounds)) {
			if (fall)
				setSize(getTexture().getWidth(), getTexture().getHeight());
			fall = false;
			break;
		}
	}
	
	public void draw(SpriteBatch batcher) {
		super.draw(batcher);
		drawCircle(batcher);
	}
	
	public void drawCircle(SpriteBatch batcher) {
		if (attack)
		circle.draw(batcher);
	}
	
	public void setColorP(Color cl) {
		setColor(cl);
		circle.setColor(cl);
	}
	
	public void setFall(boolean fall) {
		this.fall = fall;
	}
	
	

	private void inputHandler(float delta) {
		/*if (Gdx.input.isKeyPressed(Keys.RIGHT) && speedX < 7) speedX += delta*10;
		if (Gdx.input.isKeyPressed(Keys.LEFT) && speedX > -7) speedX -= delta*10;
		if (Gdx.input.isKeyPressed(Keys.UP) && speedY < 7) speedY += delta*10;
		if (Gdx.input.isKeyPressed(Keys.DOWN) && speedY > -7) speedY -= delta*10;
		setPosition(getX()+speedX, getY()+speedY);
		if (speedX > 0) speedX-=delta;
		if (speedX < 0) speedX+=delta;
		if (speedY > 0) speedY-=delta;
		if (speedY < 0) speedY+=delta;
		*/
		//keyForce.x = 0;
		Vector2 lv = body.getLinearVelocity();
		float force = 2900.f*delta*10;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        	//if (keyForce.x > -7)
        		//keyForce.x -= delta*10;
        	if (lv.x > -7)
        		body.applyForceToCenter(new Vector2(-force,0), false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        	//if (keyForce.x < 7)
             //   keyForce.x += delta*10;
        	if (lv.x < 7)
        		body.applyForceToCenter(new Vector2(force,0), false);
        }

        //keyForce.y = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        	//if (keyForce.y > -7)
                //keyForce.y -= delta*10;
        	if (lv.y > -7)
        		body.applyForceToCenter(new Vector2(0, -force), false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        	//if (keyForce.y < 7)
                //keyForce.y += delta*10;
        	if (lv.y < 7)
        		body.applyForceToCenter(new Vector2(0,force), false);
   
        }
        //body.setLinearVelocity(keyForce.x, keyForce.y);
        //body.applyForceToCenter(keyForce, true);
        //body.applyLinearImpulse(new Vector2(keyForce.x/Math.abs(keyForce.x), keyForce.y/Math.abs(keyForce.y)), Vector2.Zero, true);
        /*if (keyForce.x > 0) keyForce.x-=delta;
		if (keyForce.x < 0) keyForce.x+=delta;
		if (keyForce.y > 0) keyForce.y-=delta;
		if (keyForce.y < 0) keyForce.y+=delta;*/
        force = 600.f*delta*10;
        if (lv.x > 0) body.applyForceToCenter(new Vector2(-force,0), false);
		if (lv.x < 0) body.applyForceToCenter(new Vector2(force,0), false);
		if (lv.y > 0) body.applyForceToCenter(new Vector2(0,-force), false);
		if (lv.y < 0) body.applyForceToCenter(new Vector2(0,force), false);
		
		if (!fire&Gdx.input.isKeyPressed(Keys.SPACE)){
			fire = true;
			
			if (attack) {
			float x = (float) (128*Math.cos((rot+90)*Math.PI/180));
			float y = (float) (128*Math.sin((rot+90)*Math.PI/180));
			Surface.pack.addBullet(new Bullet(Assets.getTexture("Player/neb"), getX()+x, getY()+y, rot, circle.getColor()));
			}
			//circle.setColor(new Color(cl.r, cl.g, cl.b, 0));
			charge = space = 0;
			attack = true;
		}
		if (fire&!Gdx.input.isKeyPressed(Keys.SPACE)){
			fire = false;
		}
	}
	
	@Override
	public void write(Json json) {
		json.writeValue("hp", hp);
		super.write(json);

	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		hp = jsonData.getInt("hp");
		sX = jsonData.getFloat("x");
		sY = jsonData.getFloat("y");
		super.read(json, jsonData);
		checkPoint.set(sX, sY);
		
		createBody();

	}
	
	public float getCharge() {
		return charge;
	}
	
	public Body getBody() {
		return this.body;
	}

}
