package net.youtoolife.supernova.handlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import net.youtoolife.supernova.handlers.gui.RMEButton;
import net.youtoolife.supernova.handlers.gui.RMEImage;
import net.youtoolife.supernova.models.Background;

public class RMEGUI implements Json.Serializable {

	private Array<RMEButton> btns;
	private Array<RMEButton> remBtns = new Array<RMEButton>();
	private Array<RMEImage> imgs;
	private Array<RMEImage> remImgs = new Array<RMEImage>();
	private Background background = new Background();
	public String sfx = "";
	
	private boolean game = false;
	
	public RMEGUI() {
		
	}
	

	
	public void del(float x, float y) {
		
		if (btns != null)
			for (RMEButton sur:btns)
				if (sur.getBoundingRectangle().contains(x, y))
					btns.removeValue(sur, false);
		if (imgs != null)
			for (RMEImage sur:imgs)
				if (sur.getBoundingRectangle().contains(x, y))
					imgs.removeValue(sur, false);
		
	}

	
	public void addButton(RMEButton button) {
		if (getButtons() == null)
			setButtons(new Array<RMEButton>());
		getButtons().add(button);
	}
	public void addImg(RMEImage img) {
		if (getImg() == null)
			setImg(new Array<RMEImage>());
		getImg().add(img);
	}
	
	


	


	@Override
	public void write(Json json) {
		json.writeValue("buttons", getButtons());
		json.writeValue("images", getImg());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(Json json, JsonValue jsonData) {
		setButtons(json.readValue("buttons", Array.class, jsonData));
		setImg(json.readValue("images", Array.class, jsonData));
	}
	
	public void update (float delta) {
		
		if (getButtons() != null)
			for (RMEButton sur:getButtons())
				sur.update(delta);
		
		removeFromWorld();
	}
	
	public void draw(SpriteBatch batcher) {
	
		if (getButtons() != null)
			for (RMEButton sur:btns)
				//if (!sur.isDraw())
				sur.draw(batcher);
		if (getImg() != null)
			for (RMEImage sur:imgs)
				//if (!sur.isDraw())
				sur.draw(batcher);

	}
	
	public void drawShape(ShapeRenderer shapeRenderer) {
		
		/*if (getWalls() != null)
			for (Wall sur:getWalls())
			{
				if (sur.isDraw()) {
					shapeRenderer.setColor(sur.getColor());
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
				}
			}*/
	}
	
	public void drawShapeLine(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(new com.badlogic.gdx.graphics.Color(0.f, 0.f, 0.f, 1.f));
		
		/*if (getWalls() != null)
			for (Wall sur:getWalls())
			{
				//shapeRenderer.setColor(sur.getColor());
				if (sur.isRect())
				shapeRenderer.rect(sur.getX(), sur.getY(), sur.getWidth(), sur.getHeight());
			}*/
	}
	
	public void drawBackground(SpriteBatch batcher) {
		background.draw(batcher);
	}
	
	public void removeFromWorld() {
		for (RMEButton opp:remBtns) {
				btns.removeValue(opp, false);
		}
		for (RMEImage opp:remImgs) {
			imgs.removeValue(opp, false);
	}
	}
	
	public void removeButton(RMEButton btn) {
		remBtns.add(btn);
	}
	public void removeImg(RMEImage btn) {
		remImgs.add(btn);
	}
	

	public Array<RMEButton> getButtons() {
		return btns;
	}


	public void setButtons(Array<RMEButton> buttons) {
		this.btns = buttons;
	}
	private void setImg(Array<RMEImage> array) {
		imgs = array;
	}


	private Array<RMEImage> getImg() {
	
		return imgs;
	}
	

	public boolean isGame() {
		return game;
	}

	public void setGame(boolean game) {
		this.game = game;
	}
}
