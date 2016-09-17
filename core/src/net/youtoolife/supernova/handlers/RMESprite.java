package net.youtoolife.supernova.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import net.youtoolife.supernova.Assets;

public class RMESprite extends Sprite implements Json.Serializable {
   
    private String texture = "";
    
    //-------Animation------//
  	private int    FRAME_COLS ;
    private int    FRAME_ROWS;
      
    private int animStart, animStop;
    private float animSpeed;
    private boolean animActive = false;
    
    private float stateTime; 
    
    private Animation           walkAnimation;
    private Texture             walkSheet;
    private TextureRegion[]         walkFrames;
    private TextureRegion           currentFrame;
    //private RMEParamStr param;
    
    public RMESprite(Texture texture, float x, float y) {
    	super(texture, 0, 0, texture.getWidth(), texture.getHeight());
    	//super(new TextureRegion(texture, 128, 128));
    		setX(x);
    		setY(y);
    		//bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    		setCurrentFrame(new TextureRegion(texture));
    		//initParam(texture, x, y, 0, 0, 0, 0, false, 0);
    }
	
	public RMESprite(Texture ws, int frame_cols, int frame_rows, int animStart, int animStop, boolean animActive, float animSpeed) {
		super(ws, 0, 0, ws.getWidth(), ws.getHeight());
		//super(new TextureRegion(ws, ws.getWidth()/frame_cols, ws.getHeight()/frame_rows));
		/*super(new TextureRegion.split(ws, ws.getWidth()/frame_cols
				, ws.getHeight()/frame_rows)[0][0], 0, 0,0,0);*/
		setAnimation(ws, frame_cols, frame_rows, animStart, animStop, animSpeed, animActive);
		//bounds = new Rectangle(0, 0, ws.getWidth(), ws.getHeight());
		//initParam(ws, 0, 0, frame_cols, frame_rows, animStart, animStop, animActive, animSpeed);
	}
	
    public RMESprite() {
    		super();
    }
 
    ///-///////////
	public void setAnimation(Texture ws, int frame_cols, int frame_rows, int animStart, int animStop, float animSpeed, boolean animActive) {
		this.setAnimActive(animActive);
        walkSheet = ws;
        setFRAME_COLS(frame_cols);
        setFRAME_ROWS(frame_rows);
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/getFRAME_COLS(), walkSheet.getHeight()/getFRAME_ROWS());              // #10
        //walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        walkFrames = new TextureRegion[(animStop-animStart+1)];
        int index = 0;
        for (int i = 0; i < getFRAME_ROWS(); i++) {
            for (int j = 0; j < getFRAME_COLS(); j++) {
            		if (index >= animStart && index <= animStop) {
                walkFrames[index-animStart] = tmp[i][j];
            		}
            		index++;
            }
        }
        walkAnimation = new Animation(animSpeed, walkFrames);
        stateTime = 0f;
        if (!animActive) 
        	setCurrentFrame(walkFrames[0]);
        setSize(walkFrames[0].getRegionWidth(), walkFrames[0].getRegionHeight());
        //setSize(walkFrames[0].getRegionWidth(), walkFrames[0].getRegionHeight());
    }
	
	public void stopAnim() {
		setAnimActive(false);
		setCurrentFrame(walkAnimation.getKeyFrame(stateTime));
	}
	
	public void continueAnim() {
		setAnimActive(true);
	}
	
	public void draw(float delta) {
		//setPosition(body.getPosition().x*MP, body.getPosition().y*MP);
		if (isAnimActive()) updateAnim(delta);
		setRegion(getCurrentFrame());
		//setTexture(get);
		//bounds.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
		//bounds.setPosition(getX(), getY());
	}
	
	public void updateAnim(float delta) {
		stateTime += Gdx.graphics.getDeltaTime();
		setCurrentFrame(walkAnimation.getKeyFrame(stateTime, true));
	}

	@Override
	public void write(Json json) {
		json.writeValue("x", getX());
		json.writeValue("y", getY());
		json.writeValue("w", getWidth());
		json.writeValue("h", getHeight());
		json.writeValue("t", Assets.getTextureName(getTexture()));
		//json.writeValue("t", currentFrame);
		json.writeValue("FC", getFRAME_COLS());
		json.writeValue("FR", getFRAME_ROWS());
		json.writeValue("as", getAnimStart());
		json.writeValue("aS", getAnimStop());
		json.writeValue("a", isAnimActive());
		json.writeValue("AS", getAnimSpeed());
		json.writeValue("color", getColor());
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		
		setTextureName(jsonData.getString("t"));
		setTexture(Assets.getTexture(getTextureName()));
		
		setRegion(0, 0, getTexture().getWidth(), getTexture().getHeight());
		//setRegion(0, 0, 128, 128);
		setSize(Math.abs(getTexture().getWidth()), Math.abs(getTexture().getHeight()));
		//setSize(Math.abs(128), Math.abs(128));
		setOrigin(getTexture().getWidth() / 2, getTexture().getHeight()/ 2);
		//setOrigin(128 / 2, 128/ 2);
		
		setCurrentFrame(new TextureRegion(getTexture()));
		setPosition(jsonData.getFloat("x"), jsonData.getFloat("y"));
		setSize(jsonData.getFloat("w"), jsonData.getFloat("h"));
		setFRAME_COLS(jsonData.getInt("FC"));
		setFRAME_ROWS(jsonData.getInt("FR"));
		setAnimStart(jsonData.getInt("as"));
		setAnimStop(jsonData.getInt("aS"));
		setAnimActive(jsonData.getBoolean("a"));
		setAnimSpeed(jsonData.getFloat("AS"));
		setColor(json.readValue("color", Color.class, jsonData));
		if (animActive)
			setAnimation(getTexture(), getFRAME_COLS(), getFRAME_COLS(), getAnimStart(), getAnimStop(), getAnimSpeed(), isAnimActive());
	}

	public int getFRAME_COLS() {
		return FRAME_COLS;
	}

	public void setFRAME_COLS(int fRAME_COLS) {
		FRAME_COLS = fRAME_COLS;
	}

	public int getFRAME_ROWS() {
		return FRAME_ROWS;
	}

	public void setFRAME_ROWS(int fRAME_ROWS) {
		FRAME_ROWS = fRAME_ROWS;
	}

	public int getAnimStart() {
		return animStart;
	}

	public void setAnimStart(int animStart) {
		this.animStart = animStart;
	}

	public int getAnimStop() {
		return animStop;
	}

	public void setAnimStop(int animStop) {
		this.animStop = animStop;
	}

	public boolean isAnimActive() {
		return animActive;
	}

	public void setAnimActive(boolean animActive) {
		this.animActive = animActive;
	}

	public float getAnimSpeed() {
		return animSpeed;
	}

	public void setAnimSpeed(float animSpeed) {
		this.animSpeed = animSpeed;
	}

	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(TextureRegion currentFrame) {
		this.currentFrame = currentFrame;
	}

	public String getTextureName() {
		return texture;
	}

	public void setTextureName(String texture) {
		this.texture = texture;
	}

}

/*private void initParam(Texture texture, float x, float y,
int frame_cols, int frame_rows, int animStart, int animStop, boolean animActive, float animSpeed) {
param = new RMEParamStr();
param.x = x;
param.y = y;
param.width = getWidth();
param.height = getHeight();
param.texture = Assets.getTextureName(texture);
param.FRAME_COLS = frame_cols;
param.FRAME_ROWS = frame_rows;
param.animStart = animStart;
param.animStop = animStop;
param.animActive = animActive;
param.animSpeed = animSpeed;
}

public RMESprite(RMEParamStr paramStr) {
super(Assets.getTexture(paramStr.texture));
param = paramStr;
Texture texture = Assets.getTexture(param.texture); 
setX(param.x);
setY(param.y);
setSize(param.width, param.height);
if (param.animActive)
setAnimation(texture, param.FRAME_COLS, param.FRAME_ROWS, param.animStart, param.animStop, param.animSpeed, param.animActive);
}

public RMEParamStr getParamStr() {
return param;
}

*/
