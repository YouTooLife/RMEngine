package net.youtoolife.supernova.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import net.youtoolife.supernova.handlers.RMESprite;
import net.youtoolife.supernova.screens.Surface;

public class Bullet extends RMESprite {
	
	int speed = 1000;
	float force = 100;
	 public Rectangle bounds;
	 public float rot = 0;
	 private float sx, sy;
	// public Color color = new Color(1.f, 1.f, 1.f, 0.f);
	

	public Bullet(Texture ws, float x, float y, float rot, Color color) {
		super(ws, x, y);
		//bounds = new Circle(x, y, ws.getWidth());
		//bounds = new Rectangle(x, y, ws.getWidth()/9f*7, ws.getHeight()/9f*7);
		bounds = new Rectangle(x, y, 32/9f*7, 32/9f*7);
		if (rot > 360)
		this.rot = rot % 360;
		else
			this.rot = rot;	
		setColor(color);
		sx = x;
		sy = y;
		//System.out.println("R- "+this.rot+"  x - "+getX()+" Y - "+getY()+" nYx50t45 = "+((getX())*Math.tan((this.rot/180*Math.PI))));
	}
	
	public Bullet(Texture ws, int frame_cols, int frame_rows, int animStart, int animStop, boolean animActive, float animSpeed) {
		super(ws, frame_cols, frame_rows, animStart, animStop, animActive, animSpeed);
		//bounds = new Circle(0, 0, ws.getWidth());
		//bounds = new Rectangle(0, 0, ws.getWidth()/9f*7, ws.getHeight()/9f*7);
	}
	
	public void removeSelf() {
		Surface.pack.getPBullet().removeValue(this, false);
	}
	
	public void collision(float delta) {
		collisionWall();
		collisionOpponent();
	}
	
	public void update(float delta) {
		draw(delta);
		
		bounds.setPosition(getX()+bounds.width/9f*1.3f, getY()+bounds.height/9f*1.3f);
		//inputHandler(delta);
		float x = (float) (getX()+ Math.cos((rot+90)*Math.PI/180)*speed*delta);
		float y = (float) (getY()+ Math.sin((rot+90)*Math.PI/180)*speed*delta);
		this.setPosition(x, y);
		
		double r = Math.sqrt(Math.pow((getX()-sx), 2)+Math.pow(getY()-sy, 2));
		
		if (r > 2000) {
			///System.out.println("!!!");
			removeSelf();
		}
		collision(delta);
	}
	
	public void collisionDoor() {
		Array<Door> doors = Surface.pack.getDoors();
		if (doors != null)
		for (int i = 0; i < doors.size; i++) {
			Door door = doors.get(i);
		if (door.getBoundingRectangle().overlaps(bounds)) {
			//if (((getColor().r*1000000+getColor().g*1000+getColor().b) ==
			//	(door.getColor().r*1000000+door.getColor().g*1000+door.getColor().b)))
			//flag = true;
			//	doors.removeValue(door, false);
			//else
				//wallForce(door);
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
			float r = getColor().r*getColor().a/5.f+door.getColor().r;
			if (r > 1.f)
				r = 1;
			float g = getColor().g*getColor().a/5.f+door.getColor().g;
			if (g > 1.f)
				g = 1;
			float b = getColor().b*getColor().a/5.f+door.getColor().b;
			if (b > 1.f)
				b = 1;
			System.out.println(r+" "+g+" "+b);
			door.setColor(new Color(r, 
					g, 
					b, door.getColor().a));
			
			//Player p = Surface.pack.getPlayer();
			if (getColor().r==1.f&&r==getColor().r||
					getColor().g==1.f&&g==getColor().g||
							getColor().b==1.f&&b==getColor().b) {
			//System.out.println((getColor().r*1000000+getColor().g*1000+getColor().b));
			//System.out.println((door.getColor().r*1000000+door.getColor().g*1000+door.getColor().b));
			door.hp -=force*getColor().a;
			door.setAlpha(1/1000.f*door.hp);
			if (door.getColor().a < 0.1f)
				door.setAlpha(0.1f);
			
			}
			removeSelf();
			} 
		}
	}
	
	public void collisionWall() {
		Array<Wall> walls = Surface.pack.getWalls();
		if (walls != null)
		for (int i = 0; i < Surface.pack.getWalls().size; i++) {
			Wall wall = walls.get(i);
			if (wall.getBoundingRectangle().overlaps(bounds)) {
				wall.addHP(-force*getColor().a);
				wall.setColor(getColor());
				wall.setAlpha(1/1000.f*wall.getHP());
				System.out.println(wall.getHP());
				System.out.println(wall.getColor().a);
				removeSelf();
			}
		}
	}

}