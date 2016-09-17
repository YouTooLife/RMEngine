package net.youtoolife.supernova.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import net.youtoolife.supernova.Assets;
import net.youtoolife.supernova.RMEBuilder;
import net.youtoolife.supernova.handlers.ManhattanDistanceHeuristic;
import net.youtoolife.supernova.handlers.Node;
import net.youtoolife.supernova.handlers.RMECrypt;
import net.youtoolife.supernova.handlers.RMEPack;
import net.youtoolife.supernova.handlers.RMESound;
import net.youtoolife.supernova.handlers.TestGraph;
import net.youtoolife.supernova.handlers.WorldContactListener;
import net.youtoolife.supernova.models.CheckPoint;
import net.youtoolife.supernova.models.Door;
import net.youtoolife.supernova.models.ObjectX;
import net.youtoolife.supernova.models.Opponent;
import net.youtoolife.supernova.models.Player;
import net.youtoolife.supernova.models.SurfaceX;
import net.youtoolife.supernova.models.Wall;
import static net.youtoolife.supernova.handlers.B2DVars.PM;
import static net.youtoolife.supernova.handlers.B2DVars.MP;

public class Surface extends ScreenAdapter {
	
	public static World world;
	private Box2DDebugRenderer b2dr;
	
	public static float width = Gdx.graphics.getWidth(), height = Gdx.graphics.getHeight();
	
	RMEBuilder game;
	public static OrthographicCamera guiCam = new OrthographicCamera(width, height); 
	public static OrthographicCamera debugCam = new OrthographicCamera(width/MP, height/MP); 
	
	ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	Rectangle rect = new Rectangle(0, 0, 128, 128);	
	Rectangle rect2 = new Rectangle(100, 0, 100, 100);
	
	Texture bg;
	
	boolean touched = false;
	float sX, X, sY, Y, sx, sy;
	
	Stage stage;
	Skin skin;
	
	Array<Label> labels = new Array<Label>();
	Array<Sprite> types = new Array<Sprite>();
	public static Array<Sprite> images = new Array<Sprite>();
	Array<String> imageNames = new Array<String>();

	public static String currentType = "", currentImg = "";
	public static int idImg = 0, idType = 0;
	
	public static RMEPack pack = new RMEPack();
	
	private BitmapFont dbgMsg; 
	
	private Boolean guiTouch = false, 
			del = false,
			debug = true, drawRect = false, drawShape = false, sNode = false;
	
	private int cl = 0;
	public static Color currentColor = new Color(1.f, 1.f, 1.f, 1.f);
	private float alpha = 1.f;
	
	private BitmapFont curColor = new BitmapFont();
	private BitmapFont curAlpha = new BitmapFont(); 
	private BitmapFont delMsg = new BitmapFont(); 
	private BitmapFont pdebug = new BitmapFont(); 
	
	
	/////
	ShapeRenderer pathSR;
    IndexedAStarPathFinder<Node> mPathFinder;
    TestGraph mGraph;
    DefaultGraphPath<Node> mPath;
    ManhattanDistanceHeuristic mHeuristic;
    Array<Node> mNodes = new Array<Node>();
    Node startNode = null; //Hardcoded for now
    Node endNode = null; //Hardcoded for now

	public Surface (final RMEBuilder game) {
		this.game = game;
		
		world = new World(new Vector2(0.f, 
				0.f), false);
		b2dr = new Box2DDebugRenderer();
		
		world.setContactListener(new WorldContactListener());
		
		guiCam.position.set(width / 2, height / 2, 0);
		
		Gdx.input.setInputProcessor(new InputProcessor() {
			
			private void setColor(int cl) {
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
				if (touched) {
					touched = false;
					if (!guiTouch) {
						doClick();
						addObject();
					}
					//System.out.println(X+" - "+Y);
					guiTouch = false;
				}
					return true;
			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				if (touched && !guiTouch) {
					doClick();
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
					upDateClick();
					
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
				guiCam.zoom = guiCam.zoom + amount*Gdx.graphics.getDeltaTime()*5;
				return true;
			}
			
			@Override
			public boolean mouseMoved(int screenX, int screenY) {

				return false;
			}
			
			@Override
			public boolean keyUp(int keycode) {
				
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
					if (pack.getPlayer() != null) {
						
						endNode = surfaceNodeAt(pack.getPlayer().getX()+10, pack.getPlayer().getY()+10);
						calculatePath();
					}
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
							/*FileHandle filehandle = Gdx.files.local(text+".levelc");
							RMECrypt crypt = new RMECrypt();
							String s = crypt.decrypt(filehandle.readBytes(), "YouTooLife1911");
							pack = json.fromJson(RMEPack.class, s);
							*/
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
			}
		});

		createGui();
		refreshTypes();
		
		dbgMsg = new BitmapFont(Gdx.files.local("HelveticaNeue.fnt"));
		
		//dbgMsg.setColor(Color.PURPLE);
		//dbgMsg.setScale(1.5f);
		delMsg.setColor(Color.RED);
		//delMsg.s
		
		
		mGraph = new TestGraph(200);
        mPath = new DefaultGraphPath<Node>();
        mHeuristic = new ManhattanDistanceHeuristic();
        
        //calc();
        //RMESound.playTrack("03");
        //RMESound.play();
	}
	
	private void calc() {
		int index = 0;
		for (SurfaceX sur:pack.getSurface()) {
        	mNodes.add(new Node(sur.getX(), sur.getY(), index++));
        	mGraph.addNode(mNodes.peek());
        }
        for (Node node: mNodes) {
        		addNodeNeighbour(node, node.mX-node.TILE_SIZE/2, node.mY+node.TILE_SIZE/2);
        		addNodeNeighbour(node, node.mX+(node.TILE_SIZE/2)*3, node.mY+node.TILE_SIZE/2);
        		addNodeNeighbour(node, node.mX+node.TILE_SIZE/2, node.mY-node.TILE_SIZE/2);
        		addNodeNeighbour(node, node.mX+node.TILE_SIZE/2, node.mY+(node.TILE_SIZE/2)*3);
        }
	   //mPathFinder = new IndexedAStarPathFinder<Node>(mGraph, true);
	        
	}
	
	public Node surfaceNodeAt(float x, float y) {
		for (Node node: mNodes)
			if (x >= node.mX && x <= (node.mX+node.TILE_SIZE) && y <= (node.mY+node.TILE_SIZE) && y >= node.mY)
				return node;
		return null;
	}
	
	private void addNodeNeighbour(Node aNode, float aX, float aY) {
		Node node = surfaceNodeAt(aX, aY);
		if (node != null)
            aNode.addNeighbour(node);
    }

    private void calculatePath() {
        mPath.clear();
        mPathFinder.searchNodePath(startNode, endNode, mHeuristic, mPath);

        if (mPath.nodes.size == 0) {
            System.out.println("-----No path found-----");
        } else {
            System.out.println("-----Found path-----");
        }
        // Loop throw every node in the solution and select it.
        for (Node node : mPath.nodes) {
            node.select();
            System.out.println(node);
        }
    }
	
	public void createGui() {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new StretchViewport(width, height));
		//Gdx.input.setInputProcessor(stage);
	}
	
	private void upDateClick() {
		float w = width, h = height, 
				dw = w/guiCam.viewportWidth, dh = h/guiCam.viewportHeight, 
				cX = Gdx.input.getX(), cY = Gdx.input.getY();
		
				sx = cX/dw; 
				sy = guiCam.viewportHeight - cY/dh;
				
				sx += guiCam.position.x-(width/2);
				sy += guiCam.position.y-(height/2);
	}
	
	private void doClick() {

		upDateClick();
		
		/*
		 * 
		sx -= guiCam.position.x-(width/2);
		sy -= guiCam.position.y-(height/2);
		X = (int) (sx / 128)*128;
		Y = (int) (sy / 128)*128;
		//System.out.println("Windows:\n"+x+" - "+y);
		//System.out.println(X+" - "+Y);
		*/
		//sx = sx;
		//sy = y;
		X = (int) (sx / 128)*128;
		Y = (int) (sy / 128)*128;
		//System.out.println("Absolutly:\n"+x+" - "+y);
		//System.out.println(X+" - "+Y);
	}

	private void addObject() {
		
		if (del)
			for (int i = 0; i < rect.width/128; i++)
				for (int j = 0; j < rect.height/128; j++)
				pack.del(rect.getX()+128*i, rect.getY()+128*j);
		
		if (sNode) {
			startNode = surfaceNodeAt(rect.getX()+rect.width/2, rect.getY()+rect.height/2);
			calc();
		}
		
		
		if (!del && !sNode) {
			for (int i = 0; i < rect.width/128; i++)
				for (int j = 0; j < rect.height/128; j++) {
					
					///////--Surface---///
					if (currentType.equalsIgnoreCase("Surface")) {
					//if (!drawShape)
						pack.addSurface(new SurfaceX(Assets.getTexture(currentType+"/"+currentImg),
								rect.getX()+128*i, rect.getY()+128*j, drawShape, drawRect));
					//else
					//	pack.addSurface(new SurfaceX(
					//			rect.getX()+128*i, rect.getY()+128*j, drawRect));
					}
					///////--Wall---///
					if (currentType.equalsIgnoreCase("Wall")) {
					//	if (!drawShape)
						Wall wall  = new Wall(Assets.getTexture(currentType+"/"+currentImg),
								rect.getX()+128*i, rect.getY()+128*j, drawShape, drawRect);
						//wall.createBody();
						pack.addWall(wall);
						
					}
					if (currentType.equalsIgnoreCase("Door")) {
							//	if (!drawShape)
								pack.addDoor(new Door(Assets.getTexture(currentType+"/"+currentImg), 
										rect.getX()+128*i, rect.getY()+128*j, drawShape, drawRect));
					}
					if (currentType.equalsIgnoreCase("Object")) {
									//	if (!drawShape)
						pack.addObject(new ObjectX(Assets.getTexture(currentType+"/"+currentImg), 
									rect.getX()+128*i, rect.getY()+128*j));
					}
						if (currentType.equalsIgnoreCase("CheckPoint")) {
							//	if (!drawShape)
								pack.addCheckPoint(new CheckPoint(Assets.getTexture(currentType+"/"+currentImg), 
										rect.getX()+128*i, rect.getY()+128*j, drawShape, drawRect));
					//else
					//	pack.addWall(new Wall(
					//			rect.getX()+128*i, rect.getY()+128*j, drawRect));
					}
						
					if (pack.getPlayer() != null)
					if (currentType.equalsIgnoreCase("Opponent")) {
							//	if (!drawShape)
								pack.addOpponent(new Opponent(Assets.getTexture(currentType+"/"+currentImg), 
										rect.getX()+128*i, rect.getY()+128*j, drawShape, drawRect));
					}
						
					if (currentType.equalsIgnoreCase("Player")) 
						pack.setPlayer(new Player(Assets.getTexture(currentType+"/"+currentImg), rect.getX()+128*i, rect.getY()+128*j));
				
				}
		}
	}
	public void refreshTypes() {
		labels.clear();
		types.clear();
		images.clear();
		FileHandle file = Gdx.files.local("Types");
		System.out.println(file.isDirectory());
		if (file.isDirectory()) {
			FileHandle[] files = file.list();
			System.out.println(files.length);
			int iy = 0;
			for (int i = 0; i < files.length; i++) {
				if (!files[i].name().contains(".")) {
					Sprite sprite = new Sprite(Assets.getTexture("field"));
					System.out.println("1");
					sprite.setSize(100, 20);
					sprite.setPosition(10, height - 25 * iy - 150);
					types.add(sprite);
					Label label = new Label(files[i].name(), skin);
					label.setPosition(
							sprite.getX() + sprite.getWidth() / 2
									- label.getWidth() / 2,
							sprite.getY() + sprite.getHeight() / 2
									- label.getHeight() / 2);
					labels.add(label);
					stage.addActor(labels.get(labels.size - 1));
					iy++;
				}
			}
			currentType = labels.get(0).getText().toString();
			System.out.println(currentType);
			idType = 0;
		}
		refreshImages(currentType);
	}

	public void refreshImages(String type) {
		images.clear();
		imageNames.clear();
		FileHandle file = Gdx.files.local("Types/" + type);
		System.out.println(file.isDirectory());
		if (file.isDirectory()) {
			FileHandle[] files = file.list();
			System.out.println(files.length);
			int posX = 0, posY = 0;
			for (int i = 0; i < files.length; i++) {
				if (files[i].name().contains(".png")
						|| files[i].name().contains(".PNG")
						|| files[i].name().contains(".jpg")
						|| files[i].name().contains(".JPG")) {
					System.out.println(files[i]);
					Sprite sprite = new Sprite(new Texture(files[i]));
					sprite.setSize(50, 50);
					sprite.setPosition(width - 50 * 3 - 5 * 3 + 55 * posX,
							height - 55 * posY - 150);
					images.add(sprite);
					imageNames.add(files[i].nameWithoutExtension());
					System.out.println(files[i].nameWithoutExtension());
					posX++;
					if (posX > 2) {
						posX = 0;
						posY++;
					}
				}
			}
			currentImg = imageNames.get(0);
			System.out.println(currentImg);
			idImg = 0;
		}
	}
	
	@Override
	public void show () {
		
	}
	
	public void handleInput(float delta) {
		
		if (Gdx.input.justTouched()) {
			if (guiCam.zoom != 1.f) {
				guiCam.zoom = 1.f;
			
			}
		}

		if (!pack.isGame()) {
		int speed = 350;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)
				|| Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			speed = 1500;
		} else {
			speed = 350;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)||Gdx.input.isKeyPressed(Keys.D)) {
			guiCam.position.x = guiCam.position.x + speed * delta;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)||Gdx.input.isKeyPressed(Keys.A)) {
			guiCam.position.x = guiCam.position.x - speed * delta;
		}
		if (Gdx.input.isKeyPressed(Keys.UP)||Gdx.input.isKeyPressed(Keys.W)) {
			guiCam.position.y = guiCam.position.y + speed * delta;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)||Gdx.input.isKeyPressed(Keys.S)) {
			guiCam.position.y = guiCam.position.y - speed * delta;
		}
		}
	}

	private void upDateGUI() {
		for (int i = 0; i < types.size; i++)
			types.get(i).setPosition(guiCam.position.x-(width/2)+10,
					((guiCam.position.y-(height/2)) + height) - 25 * i - 150);
		//	types.get(i).setPosition(10, height - 25 * i - 100);
		
		int posX = 0, posY = 0;
		for (int i = 0; i < images.size; i++) {
			images.get(i).setPosition(guiCam.position.x-(width/2)+width - 50 * 3 - 5 * 3 + 55 * posX,
					guiCam.position.y-(height/2) +	height - 55 * posY - 150);
		posX++;
			if (posX > 2) {
			posX = 0;
			posY++;
			}
		}
	}

	public void update (float delta) {
		world.step(delta, 6, 2);
		
		handleInput(delta);
		
		pack.update(delta);
		
		upDateGUI();
		
		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			if (alpha > 0) alpha -= 0.5*delta;
			if (alpha < 0.f) alpha = 0.f;
			currentColor.set(currentColor.r, currentColor.g, currentColor.b, alpha);
		}
		if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
			if (alpha < 1.f) alpha += 0.5*delta;
			if (alpha > 1.f) alpha = 1.f;
			currentColor.set(currentColor.r, currentColor.g, currentColor.b, alpha);
		}
		
		if (pack.isGame()) {
			/*if (guiCam.position.x < pack.getPlayer().getX() + pack.getPlayer().getWidth()/2)
				guiCam.position.x += 400*delta;
			if (guiCam.position.x > pack.getPlayer().getX() + pack.getPlayer().getWidth()/2)
				guiCam.position.x -= 400*delta;
			if (guiCam.position.y < pack.getPlayer().getY() + pack.getPlayer().getHeight()/2)
				guiCam.position.y += 400*delta;
			if (guiCam.position.y > pack.getPlayer().getY() + pack.getPlayer().getHeight()/2)
				guiCam.position.y -= 400*delta;*/
			guiCam.position.x = pack.getPlayer().getX() + pack.getPlayer().getWidth()/2;
			guiCam.position.y = pack.getPlayer().getY() + pack.getPlayer().getHeight()/2;
		}
	}
	

	public void draw () {
		
		//Matrix4 debugMatrix = guiCam.combined.cpy().scale(MP, MP, 0.f);
	    //guiCam.update();
		GL20 gl = Gdx.gl;
		/*gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL20.GL_BLEND);
		gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);*/
		gl.glEnable(GL20.GL_BLEND);
		gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		gl.glClearColor(0, 0, 0, 0);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		debugCam.position.x = (guiCam.position.x-128.f/2)/MP;
		debugCam.position.y = (guiCam.position.y-128.f/2)/MP;
		debugCam.update();
		game.batcher.setProjectionMatrix(guiCam.combined);
		
		game.batcher.disableBlending();
		game.batcher.begin();
		pack.drawBackground(game.batcher);
		game.batcher.end();
		
		
		//game.batcher.enableBlending();
		//game.batcher.begin();

		
		///pack.draw(game.batcher);
		
		
	/*shapeRenderer.setProjectionMatrix(guiCam.combined);
	shapeRenderer.begin(ShapeType.Line);
	////---GUI------//
	if (debug) {
    	shapeRenderer.setColor(0.f, 1.f, 0.f, 0.f);
		shapeRenderer.box(types.get(idType).getX(), types.get(idType).getY(), 0,
				types.get(idType).getWidth(), types.get(idType).getHeight(), 0);
		shapeRenderer.box(images.get(idImg).getX(), images.get(idImg).getY(), 0,
				images.get(idImg).getWidth(), images.get(idImg).getHeight(), 0);
	}
	pack.drawShapeLine(shapeRenderer);
    shapeRenderer.end();
    
	*/	
		
		gl.glEnable(GL20.GL_BLEND);
		gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setProjectionMatrix(guiCam.combined);
	    shapeRenderer.begin(ShapeType.Line);
	    
	    ////---HOLST---////
	    if (debug) {
	    shapeRenderer.setColor(new Color(1, 1, 1, 0.2f));
	    for (int x = 0; x < 10000/128+1; x++) {
	    		shapeRenderer.line(new Vector2(x*128, 0), new Vector2(x*128, 10000));
	    }
	    for (int y = 0; y < 10000/128; y++) {
			shapeRenderer.line(new Vector2(0, y*128), new Vector2(10000, y*128));
		}
	    
	    }
	    shapeRenderer.end();
	    
	    ///---Objects---///
	    shapeRenderer.begin(ShapeType.Filled);
		pack.drawShape(shapeRenderer);
		shapeRenderer.end();	
		
		
		game.batcher.enableBlending();
		game.batcher.begin();
	/////-------GAME-------////
		pack.draw(game.batcher);
		
			////----GUI----///
			if (debug) {
			for (Sprite sprite : types) {
				sprite.draw(game.batcher);
			}
			for (Sprite sprite : images) {
				sprite.draw(game.batcher);
			}
			}
			game.batcher.end();
	    
	    if (debug) {
	    	
	    	if (!guiTouch && touched)
	    		shapeRenderer.setColor(0.f, 1.f, 0.f, 1.f);
	    else
	    		shapeRenderer.setColor(0.f, 0.f, 1.f, 1.f);
	    	shapeRenderer.begin(ShapeType.Line);
	    	
	    shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
	    
	    if (debug) {
	    	shapeRenderer.setColor(0.f, 1.f, 0.f, 0.f);
			shapeRenderer.box(types.get(idType).getX(), types.get(idType).getY(), 0,
					types.get(idType).getWidth(), types.get(idType).getHeight(), 0);
			shapeRenderer.box(images.get(idImg).getX(), images.get(idImg).getY(), 0,
					images.get(idImg).getWidth(), images.get(idImg).getHeight(), 0);
		}
		pack.drawShapeLine(shapeRenderer);
		
		
	    shapeRenderer.end();
	    
	    shapeRenderer.begin(ShapeType.Filled);
	  //GUI
		if (drawShape) {
		shapeRenderer.setColor(currentColor);
		shapeRenderer.rect(guiCam.position.x-(width/2)+10.f + 100.f, 
				guiCam.position.y-(height/2)+height-10.f - 85.f, 64, 64);
		}
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
		for (Node node: mNodes)
			node.render(shapeRenderer);
		shapeRenderer.end();
		
		
	    stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		
		///----dbg---///
		game.batcher.begin();
			dbgMsg.draw(game.batcher, 
					"FPS: "+String.valueOf(Gdx.graphics.getFramesPerSecond()),
					guiCam.position.x-(width/2)+10.f,
					guiCam.position.y-(height/2)+height-10.f);
			if (pack.getPlayer() != null)
			pdebug.draw(game.batcher, "Xv: "+pack.getPlayer().getBody().getLinearVelocity().x
					+" Yv:"+pack.getPlayer().getBody().getLinearVelocity().y,
					guiCam.position.x-(width/2)+10.f,
					guiCam.position.y-(height/2)+height-30.f);
			if (del) {
				delMsg.draw(game.batcher, 
						"X",
						guiCam.position.x-(width/2)+40.f,
						guiCam.position.y-(height/2)+height-10.f - 30.f);
			}
			if (drawShape) {
				curColor.setColor(currentColor);
				curColor.draw(game.batcher, 
						"Color: "+(cl==0?"WHITE":(cl==1?"RED":(cl==2?"GREEN":"BLUE"))),
						guiCam.position.x-(width/2)+10.f , 
						guiCam.position.y-(height/2)+height-10.f - 45.f);
				curAlpha.draw(game.batcher, "Alpha: "+String.valueOf(alpha), 
						guiCam.position.x-(width/2)+10.f, 
						guiCam.position.y-(height/2)+height-10.f - 45.f - 20.f);
			}
			game.batcher.end();
	    }
	    
	    //b2dr.render(world, debugMatrix);
	    b2dr.render(world, debugCam.combined);
	    
	}

	

	@Override
	public void render (float delta) {
		update(delta);
		draw();
	}

	@Override
	public void pause () {
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		//System.out.println(stage.getViewport().getViewportWidth());
	}
	
	@Override
	public void dispose() {
		world.dispose();
	}

}
