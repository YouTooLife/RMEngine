package net.youtoolife.supernova.handlers.ai;

import java.util.Iterator;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.utils.Array;

public class GraphPathImp implements GraphPath<Node>{
	
	private Array<Node> nodes = new Array<Node>();
	
	
	public GraphPathImp() {}

	@Override
	public Iterator<Node> iterator() {
		// TODO Auto-generated method stub
		return nodes.iterator();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return nodes.size;
	}

	@Override
	public Node get(int index) {
		// TODO Auto-generated method stub
		return nodes.get(index);
	}

	@Override
	public void add(Node node) {
		nodes.add(node);
		
	}

	@Override
	public void clear() {
		nodes.clear();
	}

	@Override
	public void reverse() {
		nodes.reverse();
	}

}
