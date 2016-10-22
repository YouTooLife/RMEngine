package net.youtoolife.supernova.handlers.ai;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AStartPathFinding {
	 public final AStarMap map;
	 private final PathFinder<Node> pathfinder;
	 private final Heuristic<Node> heuristic;
	// public DefaultGraphPath<Node>mPath;
	 private final GraphPath<Connection<Node>> connectionPath;
	 private GraphPathImp mPath;
	 
	 
	 Array<Node> nodes = new Array<Node>();

    public AStartPathFinding(AStarMap map) {
        this.map = map;
        this.pathfinder = new IndexedAStarPathFinder<Node>(createGraph(map), false);
        this.connectionPath = new DefaultGraphPath<Connection<Node>>();
        mPath = new GraphPathImp(); // new DefaultGraphPath<Node>();
        
        this.heuristic = new Heuristic<Node>() {
            @Override
            public float estimate (Node node, Node endNode) {
                // Manhattan distance
                return Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y);
            }
	     };
    }
    
   /* public DefaultGraphPath<Node> findNextNodePath(Vector2 source, Vector2 target) {
        int sourceX = MathUtils.floor(source.x);
        int sourceY = MathUtils.floor(source.y);
        int targetX = MathUtils.floor(target.x);
        int targetY = MathUtils.floor(target.y);

        if (map == null
               || sourceX < 0 || sourceX >= map.getWidth()
               || sourceY < 0 || sourceY >= map.getHeight()
               || targetX < 0 || targetX >= map.getWidth()
               || targetY < 0 || targetY >= map.getHeight()) {
           return null;
        }
       
        Node sourceNode = map.getNodeAt(sourceX, sourceY);
        Node targetNode = map.getNodeAt(targetX, targetY);
        connectionPath.clear();
        pathfinder.searchNodePath(sourceNode, targetNode, heuristic, mPath);
        //pathfinder.searchConnectionPath(sourceNode, targetNode, heuristic, connectionPath);

        return mPath;
    }*/
    
    public GraphPathImp findGraphPath(Vector2 source, Vector2 target) {
        int sourceX = MathUtils.floor(source.x);
        int sourceY = MathUtils.floor(source.y);
        int targetX = MathUtils.floor(target.x);
        int targetY = MathUtils.floor(target.y);

        if (map == null
               || sourceX < 0 || sourceX >= map.getWidth()
               || sourceY < 0 || sourceY >= map.getHeight()
               || targetX < 0 || targetX >= map.getWidth()
               || targetY < 0 || targetY >= map.getHeight()) {
           return null;
        }
       
        Node sourceNode = map.getNodeAt(sourceX, sourceY);
        Node targetNode = map.getNodeAt(targetX, targetY);
        connectionPath.clear();
        pathfinder.searchNodePath(sourceNode, targetNode, heuristic, mPath);
        //pathfinder.searchConnectionPath(sourceNode, targetNode, heuristic, connectionPath);

        return mPath;
    }

    public Node findNextNode(Vector2 source, Vector2 target) {
        int sourceX = MathUtils.floor(source.x);
        int sourceY = MathUtils.floor(source.y);
        int targetX = MathUtils.floor(target.x);
        int targetY = MathUtils.floor(target.y);

        if (map == null
               || sourceX < 0 || sourceX >= map.getWidth()
               || sourceY < 0 || sourceY >= map.getHeight()
               || targetX < 0 || targetX >= map.getWidth()
               || targetY < 0 || targetY >= map.getHeight()) {
           return null;
        }
       
        Node sourceNode = map.getNodeAt(sourceX, sourceY);
        Node targetNode = map.getNodeAt(targetX, targetY);
        connectionPath.clear();
        pathfinder.searchNodePath(sourceNode, targetNode, heuristic, mPath);
        //pathfinder.searchConnectionPath(sourceNode, targetNode, heuristic, connectionPath);

        return connectionPath.getCount() == 0 ? null : connectionPath.get(0).getToNode();
    }
    
    public GraphPath<Connection<Node>> getPath() {
    	return connectionPath;
    }

    private static final int[][] NEIGHBORHOOD = new int[][] {
        new int[] {-1,  0},
        new int[] { 0, -1},
        new int[] { 0,  1},
        new int[] { 1,  0}
    };

    public static MyGraph createGraph (AStarMap map) {
        final int height = map.getHeight();
        final int width = map.getWidth();
        MyGraph graph = new MyGraph(map);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Node node = map.getNodeAt(x, y);
                if (node.isWall) {
                    continue;
                } 
                
                if (y != 0) {
                	Node neighbor = map.getNodeAt(x, y-1);
                	node.creatConnection(neighbor);
                }
                if (y != height-1) {
                	Node neighbor = map.getNodeAt(x, y+1);
                	node.creatConnection(neighbor);
                }
                if (x != 0) {
                	Node neighbor = map.getNodeAt(x-1, y);
                	node.creatConnection(neighbor);
                }
                if (x != width-1) {
                	Node neighbor = map.getNodeAt(x+1, y);
                	node.creatConnection(neighbor);
                }
                // Add a connection for each valid neighbor
                /*for (int offset = 0; offset < NEIGHBORHOOD.length; offset++) {
                    int neighborX = node.x + NEIGHBORHOOD[offset][0];
                    int neighborY = node.y + NEIGHBORHOOD[offset][1];
                    if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
                        Node neighbor = map.getNodeAt(neighborX, neighborY);
                        if (!neighbor.isWall) {
                            // Add connection to walkable neighbor
                            node.creatConnection(neighbor);
                        }
                    }
                }
                node.getConnections().shuffle();*/
                
            }
        }
        return graph;
    }

    private static class MyGraph implements IndexedGraph<Node> {
   		
        AStarMap map;

        public MyGraph (AStarMap map) {
        	super();
            this.map = map;
        }

        @Override
        public int getIndex(Node node) {
            return node.getIndex();
        }

        @Override
        public Array<Connection<Node>> getConnections(Node fromNode) {
            return fromNode.getConnections();
        }

        @Override
        public int getNodeCount() {
            return map.getHeight() * map.getHeight();
        }
   	 
    }
}