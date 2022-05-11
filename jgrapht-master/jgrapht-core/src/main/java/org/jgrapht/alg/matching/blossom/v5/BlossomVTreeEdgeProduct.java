package org.jgrapht.alg.matching.blossom.v5;


import org.jheaps.MergeableAddressableHeap;

class BlossomVTreeEdgeProduct {
    private MergeableAddressableHeap<Double, BlossomVEdge> plusMinusEdges0;
    private MergeableAddressableHeap<Double, BlossomVEdge> plusMinusEdges1;

    public void setPlusMinusEdges0(MergeableAddressableHeap<Double, BlossomVEdge> plusMinusEdges0) {
        this.plusMinusEdges0 = plusMinusEdges0;
    }

    public void setPlusMinusEdges1(MergeableAddressableHeap<Double, BlossomVEdge> plusMinusEdges1) {
        this.plusMinusEdges1 = plusMinusEdges1;
    }

    /**
     * Returns the current heap of (-, +) cross-tree edges. Always returns a heap different from {@code  getCurrentPlusMinusHeap(currentDir)}
     * @param currentDir  the current direction of this edge
     * @return  returns current heap of (-, +) cross-tree edges
     */
    public MergeableAddressableHeap<Double, BlossomVEdge> getCurrentMinusPlusHeap(int currentDir) {
        return currentDir == 0 ? plusMinusEdges0 : plusMinusEdges1;
    }

    /**
     * Returns the current heap of (+, -) cross-tree edges. Always returns a heap different from {@code  getCurrentMinusPlusHeap(currentDir)}
     * @param currentDir  the current direction of this edge
     * @return  returns current heap of (+, -) cross-tree edges
     */
    public MergeableAddressableHeap<Double, BlossomVEdge> getCurrentPlusMinusHeap(int currentDir) {
        return currentDir == 0 ? plusMinusEdges1 : plusMinusEdges0;
    }
}
