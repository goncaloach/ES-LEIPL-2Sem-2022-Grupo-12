package org.jgrapht.alg.matching.blossom.v5;


import org.jgrapht.alg.matching.blossom.v5.BlossomVEdge.BlossomNodesIterator;

public class BlossomNodesIteratorProduct {
    private BlossomVNode root;
    private BlossomVNode currentNode;
    private BlossomVEdge blossomFormingEdge;

    public void setRoot(BlossomVNode root) {
        this.root = root;
    }

    public void setCurrentNode(BlossomVNode currentNode) {
        this.currentNode = currentNode;
    }

    public void setBlossomFormingEdge(BlossomVEdge blossomFormingEdge) {
        this.blossomFormingEdge = blossomFormingEdge;
    }

    /**
     * Advances this iterator to the next node in the blossom
     * @return  an unvisited node in the blossom
     */
    public BlossomVNode advance(BlossomNodesIterator blossomNodesIterator) {
        if (currentNode == null) {
            return null;
        }
        if (currentNode == root && blossomNodesIterator.getCurrentDirection() == 0) {
            blossomNodesIterator.setCurrentDirection(1);
            currentNode = blossomFormingEdge.head[1];
            if (currentNode == root) {
                currentNode = null;
            }
        } else if (currentNode.getTreeParent() == root && blossomNodesIterator.getCurrentDirection() == 1) {
            currentNode = null;
        } else {
            currentNode = currentNode.getTreeParent();
        }
        return currentNode;
    }
}
