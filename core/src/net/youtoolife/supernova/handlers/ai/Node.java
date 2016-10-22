package net.youtoolife.supernova.handlers.ai;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class Node {

    public final int x;
    public final int y;
    public boolean isWall;
    public int index;
    private final Array<Connection<Node>> connections;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        //this.index = x * map.getHeight() + y;
        this.isWall = false;
        this.connections = new Array<Connection<Node>>();
    }

    public int getIndex () {
        return index;
    }
    
    public void creatConnection(Node toNode) {
    	connections.add(new ConnectionImp(this, toNode, 1));
    }

    public Array<Connection<Node>> getConnections () {
        return connections;
    }

    @Override
    public String toString() {
        return "Node: (" + x + ", " + y + ")";
    }

}

