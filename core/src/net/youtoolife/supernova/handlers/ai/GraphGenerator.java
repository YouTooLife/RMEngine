package net.youtoolife.supernova.handlers.ai;

import com.badlogic.gdx.utils.Array;



public class GraphGenerator {
	public static Node[][] map;
	public static int mapHeight, mapWidth;
	
	 public static GraphImp genGraph(int width, int height, Node[][] map2, Array<Node> nodes2) {
		 
		 	map = map2;//new Node[height][width];
		 	
		 	Array<Node> nodes = nodes2;//new Array<Node>();
	        
	        mapHeight = width;
	        mapWidth = height;

	       

	        for (int y = 0; y < mapHeight; y++) {
	            for (int x = 0; x < mapWidth; x++) {
	                Node node = getNodeAt(x, y);
	                if (node.isWall) {
	                    continue;
	                } 
	                
	                if (y != 0) {
	                	Node neighbor = getNodeAt(x, y-1);
	                	node.creatConnection(neighbor);
	                }
	                if (y != mapHeight-1) {
	                	Node neighbor = getNodeAt(x, y+1);
	                	node.creatConnection(neighbor);
	                }
	                if (x != 0) {
	                	Node neighbor = getNodeAt(x-1, y);
	                	node.creatConnection(neighbor);
	                }
	                if (x != mapWidth-1) {
	                	Node neighbor = getNodeAt(x+1, y);
	                	node.creatConnection(neighbor);
	                }
	                
	            }
	            }
	        System.out.println("\n\n\n-------------\n\n"+mapToString());

	        return new GraphImp(nodes);
	    }
	 
	
	 
	 public static Node getNodeAt(int x, int y) {
	        return map[y][x];
	    }
	 
	    public static String mapToString() {
	        StringBuilder stringBuilder = new StringBuilder();
	        for (int y = 0; y < mapHeight; y++) {
	            for (int x = 0; x < mapWidth; x++) {
	               stringBuilder.append(map[y][x].isWall ? "#" : " ");
	            }
	            stringBuilder.append("\n");
	        }
	        return stringBuilder.toString();
	    }

}
