package net.youtoolife.supernova.handlers.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Heuristic;


public class HeuristicImp implements Heuristic<Node> {
    @Override
    public float estimate(Node startNode, Node endNode) {
        /*int startIndex = startNode.getIndex();
        int endIndex = endNode.getIndex();

        int startY = startIndex / GraphGenerator.mapWidth;
        int startX = startIndex % GraphGenerator.mapWidth;

        int endY = endIndex / GraphGenerator.mapWidth;
        int endX = endIndex % GraphGenerator.mapWidth;

        // magnitude of differences on both axes is Manhattan distance (not ideal)
        float distance = Math.abs(startX - endX) + Math.abs(startY - endY);*/

        return (Math.abs(endNode.x - startNode.x) + Math.abs(endNode.y - startNode.y));
    }
}
