/*******************************************************************************
 * Copyright 2014 Christoffer Hindelid.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package net.youtoolife.supernova.handlers;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class Node {

	public final int TILE_SIZE = 128;
	private final static int SPACE_BETWEEN_TILES = 2;
	
	/** Index that needs to be unique for every node and starts from 0. */
	private int mIndex;
	
	/** Whether or not this tile is in the path. */
	private boolean mSelected = false;
	
	/** X pos of node. */
	public final float mX;
	/** Y pos of node. */
	public final float mY;
	
	/** The neighbours of this node. i.e to which node can we travel to from here. */ 
	Array<Connection<Node>> mConnections = new Array<Connection<Node>>();
	
	/** @param aIndex needs to be unique for every node and starts from 0. */
	public Node(float aX, float aY, int aIndex) {
		mIndex = aIndex;
		mX = aX;
		mY = aY;
	}
	
	public int getIndex() {
		return mIndex;
	}

	public Array<Connection<Node>> getConnections() {
		return mConnections;
	}
	
	public void addNeighbour(Node aNode) {
	    if (null != aNode) {
	        mConnections.add(new DefaultConnection<Node>(this, aNode));
	    }
	}
	
	public void select() {
	    mSelected = true;
	}
	
	/** Render this tile as a white square or red if included in the found path. */
	public void render(ShapeRenderer aShapeRenderer) {
		if (mSelected) {
		    aShapeRenderer.setColor(Color.RED);
		} else {
		    aShapeRenderer.setColor(Color.WHITE);
		}
		aShapeRenderer.line(mX, mY, mX, mY + TILE_SIZE - SPACE_BETWEEN_TILES);
        aShapeRenderer.line(mX, mY, mX + TILE_SIZE - SPACE_BETWEEN_TILES, mY);
        aShapeRenderer.line(mX, mY + TILE_SIZE - SPACE_BETWEEN_TILES, mX + TILE_SIZE - SPACE_BETWEEN_TILES, mY + TILE_SIZE - SPACE_BETWEEN_TILES);
        aShapeRenderer.line(mX + TILE_SIZE - SPACE_BETWEEN_TILES, mY, mX + TILE_SIZE - SPACE_BETWEEN_TILES, mY + TILE_SIZE - SPACE_BETWEEN_TILES);
	}

	public String toString() {
		return String.format("Index:%d x:%d y:%d", mIndex, mX, mY);
	}

}
