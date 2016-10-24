package net.youtoolife.supernova.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class RMEShader extends RMESprite implements Serializable {
	
	public String name;
	public ShaderProgram shader;
	public Mesh mesh;
	
	
	public float width, height;
	public Vector2 resolution;
	public Color color;
	public float time = 0;
	
	public RMEShader() {
		
	}
	
	public RMEShader(String file) {
		loadShader(file);
	}
	
	
	public void update(float delta) {
		time += delta;
	}
	
	private void loadShader(String file) {
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		resolution = new Vector2(width, height);
		
		
		String vertexShader;
		String fragmentShader;
		vertexShader = Gdx.files.local("shaders/vertex.glsl").readString();
		fragmentShader = Gdx.files.local("shaders/"+file).readString();
		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) throw 
		new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
		
		
		mesh = new Mesh(true, 4, 6, new VertexAttribute(Usage.Position, 2, "a_position"));
		mesh.setVertices(new float[]{
				0, 0,
                width, 0,
                0, height,
                width, height});
		mesh.setIndices(new short[]{0, 1, 3, 0, 3, 2});
	}

	@Override
	public void write(Json json) {
		
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		
	}

}
