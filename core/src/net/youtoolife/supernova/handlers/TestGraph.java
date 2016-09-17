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
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;


public class TestGraph implements IndexedGraph<Node> {
    
	public TestGraph(int aSize) {
        super();
    }

    public void addNode(Node aNodes) {
    		//this.add(aNodes);
    }

    public Node getNode(int aIndex) {
        return this.getNode(aIndex);
    }


	@Override
	public Array<Connection<Node>> getConnections(Node fromNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIndex(Node node) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNodeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}