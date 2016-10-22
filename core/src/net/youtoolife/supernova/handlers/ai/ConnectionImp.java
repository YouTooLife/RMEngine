package net.youtoolife.supernova.handlers.ai;

import com.badlogic.gdx.ai.pfa.Connection;

public class ConnectionImp implements Connection<Node> {
	
	private Node toNode;
	private Node fromNode;
	private float cost;
	
	public ConnectionImp(Node fromNode, Node toNode, float cost) {
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.cost = cost;
	}

	@Override
	public float getCost() {
		// TODO Auto-generated method stub
		return cost;
	}

	@Override
	public Node getFromNode() {
		// TODO Auto-generated method stub
		return fromNode;
	}

	@Override
	public Node getToNode() {
		// TODO Auto-generated method stub
		return toNode;
	}

}
