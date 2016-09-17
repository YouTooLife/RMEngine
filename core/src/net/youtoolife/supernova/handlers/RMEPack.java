package net.youtoolife.supernova.handlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import net.youtoolife.supernova.models.Background;
import net.youtoolife.supernova.models.Bullet;
import net.youtoolife.supernova.models.CheckPoint;
import net.youtoolife.supernova.models.Door;
import net.youtoolife.supernova.models.ObjectX;
import net.youtoolife.supernova.models.Opponent;
import net.youtoolife.supernova.models.Player;
import net.youtoolife.supernova.models.SurfaceX;
import net.youtoolife.supernova.models.Wall;
import net.youtoolife.supernova.screens.Surface;

public class RMEPack implements Json.Serializable {
	
	private Player player;
	private Array<SurfaceX> surface;
	private Array<Wall> walls;
	private Array<Door> doors;
	private Array<CheckPoint> checkPoints;
	private Array<ObjectX> objects;
	private Array<Opponent> opps;
	private Array<Bullet> pBullets;
	private Array<Opponent> remOpps = new Array<Opponent>();
	private Array<Wall> remWalls = new Array<Wall>();
	private Array<CheckPoint> remCheckPoint = new Array<CheckPoint>();
	private Array<SurfaceX> remSurfaceX = new Array<SurfaceX>();
	private Array<Door> remDoor = new Array<Door>();
	private Array<ObjectX> remObjectX = new Array<ObjectX>();
	private Array<Bullet> remBullet = new Array<Bullet>();
	private Background background = new Background();
	public String sfx = "";
	
	private boolean game = false;
	
	public RMEPack() {
		
	}

	public RMEPack(Player player, Array<RMESprite> arr) {
		this.setPlayer(player);
	}

	public Player getPlayer() {
		if (player != null)
			return player;
		else
			return null;
	}

	public void setPlayer(Player player) {
		if (this.player != null)
		Surface.world.destroyBody(this.player.body);
		this.player = player;
	}
	
	public void del(float x, float y) {
		if (surface != null)
		for (SurfaceX sur:surface)
			if (sur.getBoundingRectangle().contains(x+128/2, y+128/2))
				removeSurfaceX(sur);
				//surface.removeValue(sur, false);
		
		if (walls != null)
			for (Wall sur:walls)
				if (sur.getBoundingRectangle().contains(x+128/2, y+128/2))
					//walls.removeValue(sur, false);
					removeWall(sur);
		if (doors != null)
			for (Door sur:doors)
				if (sur.getBoundingRectangle().contains(x+128/2, y+128/2))
					//doors.removeValue(sur, false);
					removeDoor(sur);
		if (objects != null)
			for (ObjectX sur:objects)
				if (sur.getBoundingRectangle().contains(x+128/2, y+128/2))
					//objects.removeValue(sur, false);
					removeObjectX(sur);
		if (checkPoints != null)
			for (CheckPoint sur:checkPoints)
				if (sur.getBoundingRectangle().contains(x+128/2, y+128/2))
					//checkPoints.removeValue(sur, false);
					removeCheckPoin(sur);
		if (opps != null)
			for (Opponent sur:opps)
				if (sur.getBoundingRectangle().contains(x+128/2, y+128/2))
					//opps.removeValue(sur, false);
					removeOpp(sur);
		
		
	}
	
	public void addBullet(Bullet sur) {
		if (pBullets == null)
			pBullets = new Array<Bullet>();
		pBullets.add(sur);
	}
	
	public void addSurface(SurfaceX sur) {
		if (surface == null)
			surface = new Array<SurfaceX>();
		surface.add(sur);
	}
	
	public void addWall(Wall wall) {
		if (getWalls() == null)
			setWalls(new Array<Wall>());
		getWalls().add(wall);
	}
	
	public void addDoor(Door door) {
		if (getDoors() == null)
			setDoors(new Array<Door>());
		getDoors().add(door);
	}
	
	public void addCheckPoint(CheckPoint checkPoint) {
		if (getCheckPoints() == null)
			setCheckPoints(new Array<CheckPoint>());
		getCheckPoints().add(checkPoint);
	}
	
	public void addObject(ObjectX object) {
		if (getObjects() == null)
			setObjects(new Array<ObjectX>());
		getObjects().add(object);
	}
	
	public void addOpponent(Opponent opponent) {
		if (getOpps() == null)
			setOpps(new Array<Opponent>());
		getOpps().add(opponent);
	}


	@Override
	public void write(Json json) {
		json.writeValue("surface", surface);
		json.writeValue("walls", getWalls());
		json.writeValue("doors", doors);
		json.writeValue("checkPoints", checkPoints);
		json.writeValue("objects", objects);
		json.writeValue("player", player);
		json.writeValue("opponents", opps);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(Json json, JsonValue jsonData) {
		surface = json.readValue("surface", Array.class, jsonData);
		setWalls(json.readValue("walls", Array.class, jsonData));
		doors = json.readValue("doors", Array.class, jsonData);
		checkPoints = json.readValue("checkPoints", Array.class, jsonData);
		objects = json.readValue("objects", Array.class, jsonData);
		player = json.readValue("player", Player.class, jsonData);
		opps = json.readValue("opponents", Array.class, jsonData);
	}
	
	public void update (float delta) {
		if (surface != null)
		for (SurfaceX sur:surface)
			sur.update(delta);
		if (getWalls() != null)
			for (Wall sur:getWalls())
				sur.update(delta);
		if (getDoors() != null)
			for (Door sur:getDoors())
				sur.update(delta);
		if (getObjects() != null)
			for (ObjectX sur:getObjects())
				sur.update(delta);
		if (getCheckPoints() != null)
			for (CheckPoint sur:getCheckPoints())
				sur.update(delta);
		
		if (player != null)
			if (isGame())
			player.update(delta);
		
		if (getOpps() != null)
			for (Opponent sur:getOpps())
				if (isGame())
				sur.update(delta);
		if (pBullets != null)
			for (Bullet sur:pBullets)
				if (isGame())
				sur.update(delta);
		
		removeFromWorld();
	}
	
	public void draw(SpriteBatch batcher) {
		if (surface != null)
		for (SurfaceX sur:surface)
			if (!sur.isDraw())
			sur.draw(batcher);
		if (walls != null)
			for (Wall sur:walls)
				if (!sur.isDraw())
				sur.draw(batcher);
		if (doors != null)
			for (Door sur:doors)
				if (!sur.isDraw())
				sur.draw(batcher);
		if (objects != null)
			for (ObjectX sur:objects)
				if (!sur.isDraw())
				sur.draw(batcher);
		if (checkPoints != null)
			for (CheckPoint sur:checkPoints)
				if (!sur.isDraw())
				sur.draw(batcher);

		if (player != null) {
			player.draw(batcher);
			player.drawCircle(batcher);
		}
		
		///
		if (opps != null)
			for (Opponent sur:opps)
				if (!sur.isDraw())
				sur.draw(batcher);
		if (pBullets != null)
			for (Bullet sur:pBullets)
				sur.draw(batcher);
	}
	
	public void drawShape(ShapeRenderer shapeRenderer) {
		if (surface != null)
		for (SurfaceX sur:surface)
			{
				if (sur.isDraw()) {
				shapeRenderer.setColor(sur.getColor());
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
				}
			}
		if (getWalls() != null)
			for (Wall sur:getWalls())
			{
				if (sur.isDraw()) {
					shapeRenderer.setColor(sur.getColor());
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
				}
			}
		if (getDoors() != null)
			for (Door sur:getDoors())
			{
				if (sur.isDraw()) {
					shapeRenderer.setColor(sur.getColor());
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
				}
			}
		if (getObjects() != null)
			for (ObjectX sur:getObjects())
			{
				if (sur.isDraw()) {
					shapeRenderer.setColor(sur.getColor());
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
				}
			}
		if (getCheckPoints() != null)
			for (CheckPoint sur:getCheckPoints())
			{
				if (sur.isDraw()) {
					shapeRenderer.setColor(sur.getColor());
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
				}
			}
		if (getOpps() != null)
			for (Opponent sur:getOpps())
			{
				if (sur.isDraw()) {
					shapeRenderer.setColor(sur.getColor());
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
				}
			}
	}
	
	public void drawShapeLine(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(new com.badlogic.gdx.graphics.Color(0.f, 0.f, 0.f, 1.f));
		if (surface != null)
		for (SurfaceX sur:surface)
			{
				//shapeRenderer.setColor(sur.getColor());
			if (sur.isRect())
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
			}
		if (getWalls() != null)
			for (Wall sur:getWalls())
			{
				//shapeRenderer.setColor(sur.getColor());
				if (sur.isRect())
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
			}
		if (getDoors() != null)
			for (Door sur:getDoors())
			{
				//shapeRenderer.setColor(sur.getColor());
				if (sur.isRect())
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
			}
		if (getObjects() != null)
			for (ObjectX sur:getObjects())
			{
				//shapeRenderer.setColor(sur.getColor());
				if (sur.isRect())
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
			}
		if (getCheckPoints() != null)
			for (CheckPoint sur:getCheckPoints())
			{
				//shapeRenderer.setColor(sur.getColor());
				if (sur.isRect())
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
			}
		if (getOpps() != null)
			for (Opponent sur:getOpps())
			{
				//shapeRenderer.setColor(sur.getColor());
				if (sur.isRect())
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
			}
	}
	
	public void drawBackground(SpriteBatch batcher) {
		background.draw(batcher);
	}
	
	public void removeFromWorld() {
		for (Opponent opp:remOpps) {
			opps.removeValue(opp, false);
			
		}
		for (Wall opp:remWalls) {
			walls.removeValue(opp, false);
		}
		for (CheckPoint opp:remCheckPoint) {
			checkPoints.removeValue(opp, false);
		}
		for (ObjectX opp:remObjectX) {
			objects.removeValue(opp, false);
		}
		for (Bullet opp:remBullet) {
			//.removeValue(opp, false);
		}
		for (Door opp:remDoor) {
			doors.removeValue(opp, false);
		}
		for (SurfaceX opp:remSurfaceX ) {
			surface.removeValue(opp, false);
		}
		
	}
	
	public void removeOpp(Opponent opp) {
		remOpps.add(opp);
	}
	public void removeCheckPoin(CheckPoint wall) {
		remCheckPoint.add(wall);
		Surface.world.destroyBody(wall.getBody());
	}
	public void removeWall(Wall wall) {
		remWalls.add(wall);
		Surface.world.destroyBody(wall.getBody());
	}
	public void removeSurfaceX(SurfaceX wall) {
		remSurfaceX.add(wall);
		//Surface.world.destroyBody(wall.getBody());
	}
	public void removeDoor(Door wall) {
		remDoor.add(wall);
		Surface.world.destroyBody(wall.getBody());
	}
	public void removeObjectX(ObjectX wall) {
		remObjectX.add(wall);
		Surface.world.destroyBody(wall.getBody());
	}
	public void removeBullet(Bullet wall) {
		remBullet.add(wall);
		//Surface.world.destroyBody(wall.getBody());
	}
	
	

	public Array<Wall> getWalls() {
		return walls;
	}
	
	public Array<Door> getDoors() {
		return doors;
	}
	
	public Array<ObjectX> getObjects() {
		return objects;
	}
	public Array<CheckPoint> getCheckPoints() {
		return checkPoints;
	}
	
	public Array<SurfaceX> getSurface() {
		return surface;
	}
	
	public Array<Opponent> getOpps() {
		return opps;
	}
	
	public Array<Bullet> getPBullet() {
		return pBullets;
	}

	public void setWalls(Array<Wall> walls) {
		this.walls = walls;
	}
	
	public void setOpps(Array<Opponent> opps) {
		this.opps = opps;
	}
	
	public void setDoors(Array<Door> doors) {
		this.doors = doors;
	}
	
	public void setObjects(Array<ObjectX> objects) {
		this.objects = objects;
	}
	public void setCheckPoints(Array<CheckPoint> checkPoins) {
		this.checkPoints = checkPoins;
	}

	public boolean isGame() {
		return game;
	}

	public void setGame(boolean game) {
		this.game = game;
	}
	
	public void dispose() {
		
	}
}
