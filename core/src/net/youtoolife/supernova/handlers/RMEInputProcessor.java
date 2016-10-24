package net.youtoolife.supernova.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;

import net.youtoolife.supernova.screens.MainMenu;
import net.youtoolife.supernova.screens.Surface;

public class RMEInputProcessor implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	/*

	private void setColor(int cl) {
		
		Color currentColor = Surface.currentColor;
		float alpha = Surface.alpha;
		
		switch (cl) {
		case 0:
			currentColor = new Color(1.f, 1.f, 1.f, alpha);
			break;
		case 1:
			currentColor = new Color(1.f, 0.f, 0.f, alpha);
			break;
		case 2:
			currentColor = new Color(0.f, 1.f, 0.f, alpha);
			break;
		case 3:
			currentColor = new Color(0.f, 0.f, 1.f, alpha);
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		boolean touched = Surface.touched;
		boolean guiTouch = Surface.guiTouch;
		if (touched) {
			touched = false;
			if (!guiTouch) {
				Surface.doClick();
				Surface.addObject();
			}
			//System.out.println(X+" - "+Y);
			guiTouch = false;
		}
			return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		boolean touched = Surface.touched;
		boolean guiTouch = Surface.guiTouch;
		
		if (touched && !guiTouch) {
			Surface.doClick();
			if (X >= sX) {
				rect.setX(sX);
				rect.setWidth(X+128-sX);
			} else
			{
				rect.setX(X);
				rect.setWidth(sX+128-X);
			}
			if (Y >= sY) {
				rect.setY(sY);
				rect.setHeight(Y+128-sY);
			} else
			{
				rect.setY(Y);
				rect.setHeight(sY+128-Y);
			}
			}
		return true;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (!touched && guiCam.zoom == 1.f) {
			Surface.upDateClick();
			
				for (Sprite sprite : types)
					if (sprite.getBoundingRectangle().contains(sx, sy)) {
						idType = types.indexOf(sprite, false);
						currentType = labels.get(idType).getText().toString();
						System.out.println(currentType);
						refreshImages(currentType);
						guiTouch = true;
					}
			
				for (Sprite sprite : images)
					if (sprite.getBoundingRectangle().contains(sx, sy)) {
						idImg = images.indexOf(sprite, false);
						currentImg = imageNames.get(idImg);
						System.out.println(currentImg);
						guiTouch = true;
					}
		if (!guiTouch) {
			doClick();
		sX = X; sY = Y;
		rect.set(sX, sY, 128, 128);
		System.out.println(sX+" - "+sY);
		}
		touched = true;
		}
		return true;
	}
	
	@Override
	public boolean scrolled(int amount) {
		OrthographicCamera guiCam = Surface.guiCam;
		guiCam.zoom = guiCam.zoom + amount*Gdx.graphics.getDeltaTime()*5;
		return true;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		
		RMEPack pack = Surface.pack;
		
		
		////
		if (keycode == Keys.R) {
			if (pack.isGame() && pack.getPlayer() != null)
			pack.getPlayer().goToCheckPoint();
		}
		///
		
		if (keycode == Keys.G) {
			if (!debug)
				debug = true;
			else
				debug = false;
		}
		if (keycode == Keys.X) {
			if (!del)
				del = true;
			else
				del = false;
		}
		if (keycode == Keys.U) {
			if (!sNode)
				sNode = true;
			else
				sNode = false;
		}
		
		
		if (keycode == Keys.Y) {
				sp = sp?false:true;
		}
		
		
		if (keycode == Keys.Z) {
			if (!drawShape)
				drawShape = true;
			else
				drawShape = false;
		}
		if (keycode == Keys.B) {
			if (!drawRect)
				drawRect = true;
			else
				drawRect = false;
		}
		
		if (keycode == Keys.Q) {
			cl--;
			if (cl < 0)
				cl = 3;
			setColor(cl);
		}
		if (keycode == Keys.NUM_3) {
			alpha = 1.f;
			currentColor.a = alpha;
		}
		if (keycode == Keys.E) {
			cl++;
			if (cl > 3)
				cl = 0;
			setColor(cl);
		}
		
		if (keycode == Keys.P) {
			if (!pack.isGame() && pack.getPlayer() != null)
				pack.setGame(true);
			else
				pack.setGame(false);
		}
		if (keycode == Keys.M) {
			Gdx.input.getTextInput(new TextInputListener() {
				
				@Override
				public void input(String text) {
					pack.sfx = text;
					
					
				}
				
				@Override
				public void canceled() {
					
					
				}
			}, "File name:", "","");
		}
		
		
		if (keycode == Keys.N) {
			game.setScreen(new MainMenu(game));
		}
		
		if (keycode == Keys.O) {
			Gdx.input.getTextInput(new TextInputListener() {
				
				@Override
				public void input(String text) {
					Json json = new Json();
					json.toJson(pack, new FileHandle(text+".jMap"));
					
					RMECrypt crypt = new RMECrypt();
					String s = json.toJson(pack);
					FileHandle filehandle = Gdx.files.local(text+".level");
					filehandle.writeBytes(crypt.encrypt(s, "YouTooLife1911"), false);
					
					
				}
				
				@Override
				public void canceled() {
					
					
				}
			}, "File name:", "level0","");
		}
		if (keycode == Keys.L) {
			Gdx.input.getTextInput(new TextInputListener() {
				
				@Override
				public void input(String text) {
					Json json = new Json();
		
					pack = json.fromJson(RMEPack.class, new FileHandle(text+".jMap"));
				}
				
				@Override
				public void canceled() {
					
				}
			}, "File name:", "level0","");
		}
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}*/

}
