package net.youtoolife.supernova.models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import net.youtoolife.supernova.handlers.RMESprite;
import net.youtoolife.supernova.screens.Surface;

public class OpponentCore {
	
	private Opponent opp;
	
	float speedX = 70, speedY = 70;
	
	boolean goCenter = false;
	boolean goTolim = false, center = false;
	
	int limX;
	int limY;
	boolean limXX, limYY;
	boolean wentYY = false, wentYy = false, wentXx = false, wentXX = false;
	int went = -1;
	boolean bYY = false, bYy = false, bXx = false, bXX = false;
	
	public Rectangle bounds;
	
	public int way = 0, oldWay = 1;
	
	public OpponentCore(Opponent opponent) {
		this.opp = opponent;
	}
	
	
	
	//--------Collisions----------------//
	//
	public boolean canGo(float x, float y) {
		if (!isWall(x, y)&&!isOpponent(x, y)&&isSurface(x, y)&&!isPlayer(x, y)&&!isDoor(x, y))
			return true;
		else
			return false;
	}
	
	public boolean isWall(float x, float y) {
		if (Surface.pack.getWalls() != null)
		for (Wall wall: Surface.pack.getWalls())
			if (wall.getX() == x && wall.getY() == y)
				return true;
		return false;
	}
	
	public boolean isOpponent(float x, float y) {
		if (Surface.pack.getOpps() != null)
			for (int i = 0; i < Surface.pack.getOpps().size; i++)
			{
				Opponent opp = Surface.pack.getOpps().get(i);
				Rectangle bounds = new Rectangle(x, y, 128, 128);
				if (opp != getOpp() && opp.getBoundingRectangle().overlaps(bounds))
						return true;
			}
		/*for (Opponent opp: Surface.pack.getOpps())
			if (/*opp != getOpp() && opp.getX() == x && opp.getY() == y)
				return true;*/
		return false;
	}
	
	public boolean isPlayer(float x, float y) {
		Player player = Surface.pack.getPlayer();
		if (player != null) {
			
				Rectangle bounds = new Rectangle(x, y, 128, 128);
				if (player.getBoundingRectangle().overlaps(bounds))
						return true;
			}
		return false;
	}
	
	public boolean isSurface(float x, float y) {
		if (Surface.pack.getSurface() != null)
		for (SurfaceX opp: Surface.pack.getSurface())
			if (opp.getX() == x && opp.getY() == y)
				return true;
		return false;
	}
	
	public boolean isDoor(float x, float y) {
		if (Surface.pack.getDoors() != null)
		for (Door door: Surface.pack.getDoors())
			if (door.getX() == x && door.getY() == y)
				if (((getOpp().getColor().r*1000000
						+getOpp().getColor().g*1000
						+getOpp().getColor().b) !=
				(door.getColor().r*1000000+door.getColor().g*1000+door.getColor().b)))
				return true;
		return false;
	}
	
	public void collisionDoor() {
		Array<Door> doors = Surface.pack.getDoors();
		if (doors != null)
		for (int i = 0; i < doors.size; i++) {
			Door door = doors.get(i);
		if (door.getBoundingRectangle().overlaps(getOpp().getBoundingRectangle())) {
			if (((getOpp().getColor().r*1000000+
					getOpp().getColor().g*1000+
					getOpp().getColor().b) ==
				(door.getColor().r*1000000+door.getColor().g*1000+door.getColor().b)))
			//flag = true;
				doors.removeValue(door, false);
				//wallForce(door);
			//System.out.println((getColor().r*1000000+getColor().g*1000+getColor().b));
			//System.out.println((door.getColor().r*1000000+door.getColor().g*1000+door.getColor().b));
		} 
		}
	}
	
	public void collisions() {
		collisionDoor();
	}
	
	//-----------------------------//
	
	
	

	//------------Move-------------//
	public int go(float delta) {
		if (!limXX) {
			if (limX == getOpp().getX()) {
				limXX = true;
				return 1;
			}
		if (limX > getOpp().getX()) {
			//System.out.println("~x"+getOpp().getX()+"/"+limX);
			way = 4;
			getOpp().setPosition(getOpp().getX()+speedX*delta, getOpp().getY());
			if (limX <= getOpp().getX()){
				//System.out.println("x"+getOpp().getX()+"/"+limX);
				limXX = true;
				return 1;
			}
		}
		
		if (limX < getOpp().getX()) {
			//System.out.println("~-x"+getOpp().getX()+"/"+limX);
			way = 2;
			getOpp().setPosition(getOpp().getX()-speedX*delta, getOpp().getY());
			if (limX >= getOpp().getX()){
				//System.out.println("x"+getOpp().getX()+"/"+limX);
				limXX = true;
				return 1;
			}
		}
		}
		
		if (!limYY && limXX) {
			if (limY == getOpp().getY()) {
				limYY = true;
				return 2;
			}
			
		if (limY > getOpp().getY()) {
			//System.out.println("y");
			way = 1;
			getOpp().setPosition(getOpp().getX(), getOpp().getY()+speedY*delta);
			//return 1;
			if (limY <= getOpp().getY()){
				//System.out.println("+y"+getOpp().getY()+"/"+limY);
				limYY = true;
				return 2;
			}
		}
		
		if (limY < getOpp().getY()) {
			//System.out.println("y-");
			way = 3;
			getOpp().setPosition(getOpp().getX(), getOpp().getY()-speedY*delta);
			//return 1;
			if (limY >= getOpp().getY()){
				//System.out.println("-y"+getOpp().getY()+"/"+limY);
				limYY = true;
				return 2;
			}
		}
		}
		return 0;
	}
	
	public int getLim() {
		//if (bXX & bXx && bYY && bYy)
		//	bXX = bXx = bYY = bYy = false;
		Player player = Surface.pack.getPlayer();
		long px = (int)(player.getX()/128)*128;
		long py = (int)(player.getY()/128)*128;
		
		if ((getOpp().getX() == px)) {
			if (Math.abs(py-getOpp().getY()) == 128)
				return 0;
		}
		if ((getOpp().getY() == py)) {
			if (Math.abs(px-getOpp().getX()) == 128)
				return 0;
		}
		
		//------------XX------------//
		if (/*!bXX && (bXx||*/(getOpp().getX() < px)) {
			if (canGo(getOpp().getX()+128, getOpp().getY())) {
				limX = (int) (getOpp().getX()+128);
				limY = (int) getOpp().getY();
				//System.out.println(limX+":"+limY);
				//wentXX = wentXx = wentYY = wentYy = false;
				//went = 0;
				return 1;
			}
			else {
				if (!bYY&&(wentYY || getOpp().getY() <= py)) {
					if (canGo(getOpp().getX(), getOpp().getY()+128)) {
						limX = (int) (getOpp().getX());
						limY = (int) (getOpp().getY()+128);
						wentYY = true;
						//System.out.println(limX+":"+limY);
						return 1;
					}
					else {
							wentYY = false;
							wentYy = true;
							bYY = true;
							if (bYy && bYY) { 
								//bXX = true;
								wentYY = wentYy = false;
							}
					}
				}
				if (!bYy&&(wentYy || getOpp().getY() >= py)) {
					if (canGo(getOpp().getX(), getOpp().getY()-128)) {
						limX = (int) (getOpp().getX());
						limY = (int) (getOpp().getY()-128);
						//System.out.println(limX+":"+limY);
						wentYy = true;
						return 1;
					}
					else {
						wentYy = false;
						wentYY = true;
						bYy = true;
						if (bYy && bYY) { 
							//bXX = true;
							wentYY = wentYy = false;
						}
				}
				}
			}
		}
		
		//-----------------Xx------------//
		if (/*!bXx&&(bXX|| */(getOpp().getX() > px)) {
			if (canGo(getOpp().getX()-128, getOpp().getY())) {
				limX = (int) (getOpp().getX()-128);
				limY = (int) getOpp().getY();
				//System.out.println(limX+":"+limY);
				return 1;
			}
			else {
				if (!bYY&&(wentYY || getOpp().getY() <= py)) {
					if (canGo(getOpp().getX(), getOpp().getY()+128)) {
						limX = (int) (getOpp().getX());
						limY = (int) (getOpp().getY()+128);
						wentYY = true;
						//System.out.println(limX+":"+limY);
						return 1;
					}
					else {
							wentYY = false;
							wentYy = true;
							bYY = true;
							if (bYy && bYY) { 
								//bXx = true;
								wentYY = wentYy = false;
							}
					}
				}
				if (!bYy&&(wentYy || getOpp().getY() >= py)) {
					if (canGo(getOpp().getX(), getOpp().getY()-128)) {
						limX = (int) (getOpp().getX());
						limY = (int) (getOpp().getY()-128);
						//System.out.println(limX+":"+limY);
						wentYy = true;
						return 1;
					}
					else {
						wentYy = false;
						wentYY = true;
						bYy = true;
						if (bYy && bYY) { 
							//bXx = true;
							wentYY = wentYy = false;
						}
				}
				}
			}
		}
		
		
		
		//----------------YY---------------//
		if (/*!bYY && (bYy||*/(getOpp().getY() < py)) {
			if (canGo(getOpp().getX(), getOpp().getY()+128)) {
				limX = (int) (getOpp().getX());
				limY = (int) (getOpp().getY()+128);
				//System.out.println(limX+":"+limY);
				return 1;
				}
				else {
					if (!bXX&&(wentXX || getOpp().getX() <= px)) {
						if (canGo(getOpp().getX()+128, getOpp().getX())) {
							limX = (int) (getOpp().getX()+128);
							limY = (int) (getOpp().getY());
							wentXX = true;
							//System.out.println(limX+":"+limY);
							return 1;
						}
						else {
								wentXX = false;
								wentXx = true;
								bXX = true;
								if (bXx && bXX) { 
									//bYY = true;
									wentXX = wentXx = false;
								}
						}
					}
					if (!bXx&&(wentXx || getOpp().getX() >= px)) {
						if (canGo(getOpp().getX()-128, getOpp().getY())) {
							limX = (int) (getOpp().getX()-128);
							limY = (int) (getOpp().getY());
							//System.out.println(limX+":"+limY);
							wentXx = true;
							return 1;
						}
						else {
							wentXx = false;
							wentXX = true;
							bXx = true;
							if (bXx && bXX) { 
								//bYY = true;
								wentXX = wentXx = false;
							}
					}
					}
				}
			}
		
		//------------------Yy---------------//
		if (/*!bYy&&(bYY|| */(getOpp().getY() > py)) {
			if (canGo(getOpp().getX(), getOpp().getY()-128)) {
				limX = (int) (getOpp().getX());
				limY = (int) getOpp().getY()-128;
				//System.out.println(limX+":"+limY);
				return 1;
			}
			else {
				if (!bXX&&(wentXX || getOpp().getX() <= px)) {
					if (canGo(getOpp().getX()+128, getOpp().getY())) {
						limX = (int) (getOpp().getX()+128);
						limY = (int) (getOpp().getY());
						wentXX = true;
						//System.out.println(limX+":"+limY);
						return 1;
					}
					else {
							wentXX = false;
							wentXx = true;
							bXX = true;
							if (bXx && bXX) { 
								//bYy = true;
								wentXX = wentXx = false;
							}
					}
				}
				if (!bXx&&(wentXx || getOpp().getX() >= px)) {
					if (canGo(getOpp().getX()-128, getOpp().getX())) {
						limX = (int) (getOpp().getX()-128);
						limY = (int) (getOpp().getY());
						//System.out.println(limX+":"+limY);
						wentXx = true;
						return 1;
					}
					else {
						wentXx = false;
						wentXX = true;
						bXx = true;
						if (bXx && bXX) { 
							//bYy = true;
							wentXX = wentXx = false;
						}
				}
				}
			}
		}
		/*if (getOpp().getY() > py) {
			if (canGo(getOpp().getX(), getOpp().getY()-128)) {
				limX = (int) (getOpp().getX());
				limY = (int) (getOpp().getY()-128);
				//System.out.println(limX+":"+limY);
				return 1;
			}
		}*/
		return 0;
	}

	
	public int goCenter(float delta) {
		if (goCenter) {
			if (getOpp().getX() % 128 != 0) {
				
				
				//System.out.println(getOpp().getX()+" "+limX);
				if (getOpp().getX() > limX) {
					getOpp().setPosition(getOpp().getX()-speedX*delta, getOpp().getY());
					return 1;
				}
				else {
					if (getOpp().getX() < limX)
						getOpp().setPosition(limX, getOpp().getY());
					return 1;
				}
				
			}
			if (getOpp().getY() % 128 != 0) {
				
				if (getOpp().getY() > limY) {
					getOpp().setPosition(getOpp().getX(), getOpp().getY()-speedY*delta);
					return 1;
				}
				else {
					if (getOpp().getY() < limY)
						getOpp().setPosition(getOpp().getX(), limY);
					goCenter = false;
					center = true;
					return 1;
				}
				
			}
		}
		
		return 0;
	}
	//--------------------------------//
	
	
	
	
	public int core(float delta) {
		//-------Go to Center---------//
		if (!center )
		if (!goCenter && (getOpp().getX() % 128 != 0 || getOpp().getY() % 128 != 0)) {
			limX = (int)(getOpp().getX()/128)*128;
			limY = (int)(getOpp().getY() / 128)*128;
			goCenter = true;
		}
		else {
			goCenter = false;
			center = true;
		}
		
		if (goCenter && !center)
			goCenter(delta);
		//-----------------------//
		
		
		//-----------------------//
		collisions();
		return 0;
	}
	
	public Opponent getOpp() {
		return opp;
	}
	
	public int getWay() {
		return way;
	}
	public int getOldWay() {
		return oldWay;
	}

}