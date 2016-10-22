package net.youtoolife.supernova.models;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import net.youtoolife.supernova.handlers.RMEPack;
import net.youtoolife.supernova.handlers.RMESprite;
import net.youtoolife.supernova.handlers.ai.GraphGenerator;
import net.youtoolife.supernova.handlers.ai.GraphPathImp;
import net.youtoolife.supernova.handlers.ai.HeuristicImp;
import net.youtoolife.supernova.handlers.ai.Node;
import net.youtoolife.supernova.screens.Surface;

public class Opponent extends RMESprite implements Json.Serializable {
	
	public Rectangle bounds = new Rectangle(0,0, 128, 128);
	private int cl = 0;
	private boolean rect = false, draw = false;
	public int hp = 100;
	float speedX = 0.f, speedY = 0.f;
	private OpponentCore oppCore = new AngryStar(this);
	
	GraphPathImp path = Surface.pack.resultPath;
	
	Vector2 way = null;
	
	public Opponent() {
		 super();
		 
		 
		 RMEPack pack = Surface.pack;
			Vector2 target = new Vector2(pack.getPlayer().getX()/128, 
					pack.getPlayer().getY()/128);
			Vector2 source = new Vector2(getX()/128, getY()/128);
			
			
			
			Node sourceNode = GraphGenerator.getNodeAt((int)source.x, (int)source.y);
	        Node targetNode = GraphGenerator.getNodeAt((int)target.x, (int)target.y);
	        
			pack.pathFinder.searchNodePath(sourceNode, targetNode, new HeuristicImp(), path);
			
	}
	
	public Opponent(Texture ws, float x, float y, boolean draw, boolean rect) {
		super(ws, x, y);
		this.setDraw(draw);
		this.setRect(rect);
		bounds = new Rectangle(x, y, 128, 128);
		setSize(128, 128);
		setColor(Surface.currentColor);
		
		RMEPack pack = Surface.pack;
		Vector2 target = new Vector2(pack.getPlayer().getX()/128, 
				pack.getPlayer().getY()/128);
		Vector2 source = new Vector2(getX()/128, getY()/128);
		
		
		
		Node sourceNode = GraphGenerator.getNodeAt((int)source.x, (int)source.y);
        Node targetNode = GraphGenerator.getNodeAt((int)target.x, (int)target.y);
        
		pack.pathFinder.searchNodePath(sourceNode, targetNode, new HeuristicImp(), path);
		
	}
	
	public Opponent(Texture ws, int frame_cols, int frame_rows, int animStart, int animStop, boolean animActive, float animSpeed) {
		super(ws, frame_cols, frame_rows, animStart, animStop, animActive, animSpeed);
		
		
		
		
	}
	
	public void update(float delta) {
		draw(delta);
		bounds.setPosition(getX()+bounds.width, getY()+bounds.height);
		//oppCore.core(delta);
		move(delta);
		if (hp <= 0)
		Surface.pack.removeOpp(this);
	}
	
	public void drawWay(ShapeRenderer render) {
		
		
		
		
	}
	
	public void drawWayLine(ShapeRenderer render) {
		/*RMEPack pack = Surface.pack;
		Vector2 target = new Vector2(pack.getPlayer().getX(), 
				pack.getPlayer().getY());
		render.setColor(Color.RED);
		render.rect(target.x, target.y, 128, 128);
		render.setColor(Color.GREEN);
		if (way != null)
		render.rect(way.x, way.y, 128, 128);*/
		
		RMEPack pack = Surface.pack;
		Vector2 target = new Vector2(pack.getPlayer().getX()/128, 
				pack.getPlayer().getY()/128);
		Vector2 source = new Vector2(getX()/128, getY()/128);
		
		
		
		Node sourceNode = GraphGenerator.getNodeAt((int)source.x, (int)source.y);
        Node targetNode = GraphGenerator.getNodeAt((int)target.x, (int)target.y);
        
        path.clear();
        
		pack.pathFinder.searchNodePath(sourceNode, targetNode, new HeuristicImp(), path);
		
		
		render.setColor(Color.GOLD);
		if (path != null) {
			for (Node node:path)
				render.rect(node.x*128, node.y*128, 128, 128);
		}
	}
	
	public void move(float delta) {
		RMEPack pack = Surface.pack;
		
		if (path != null) {
			
		Node node = path.get(1); 
		
		
		if (node != null) {
			Vector2 vec = null;
			vec = new Vector2(node.x*128, node.y*128);
	
		
		
			int speed = 200;
			
			if (getX() < vec.x) {
				setPosition(getX() + speed*delta, getY());
			}
			if (getX() > vec.x) {
				setPosition(getX() - speed*delta, getY());
			}
			if (getY() < vec.y) {
				setPosition(getX(), getY() + speed*delta);
			}
			if (getY() > vec.y) {
				setPosition(getX(), getY() - speed*delta);
			}
			
		}
		}
		
		
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
}
