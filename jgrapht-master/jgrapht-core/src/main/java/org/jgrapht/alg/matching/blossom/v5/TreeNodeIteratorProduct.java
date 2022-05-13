package org.jgrapht.alg.matching.blossom.v5;


public class TreeNodeIteratorProduct {
    private BlossomVNode currentNode;
    private BlossomVNode treeRoot;

    public void setCurrentNode(BlossomVNode currentNode) {
        this.currentNode = currentNode;
    }

    public void setTreeRoot(BlossomVNode treeRoot) {
        this.treeRoot = treeRoot;
    }

    /**
     * Advances the iterator to the next tree node
     * @return  the next tree node
     */
    public BlossomVNode advance() {
        if (currentNode == null) {
            return null;
        } else if (currentNode.firstTreeChild != null) {
            currentNode = currentNode.firstTreeChild;
            return currentNode;
        } else {
            while (currentNode != treeRoot && currentNode.treeSiblingNext == null) {
                currentNode = currentNode.parentEdge.getOpposite(currentNode);
            }
            currentNode = currentNode.treeSiblingNext;
            if (currentNode == treeRoot.treeSiblingNext) {
                currentNode = null;
            }
            return currentNode;
        }
    }
}
