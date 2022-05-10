package org.jgrapht.alg.matching;


import java.util.List;

public class LevelsProduct {
    private int[] even;
    private List<Integer> dirty;

    public int[] getEven2() {
        return even;
    }

    public void setEven2(int[] even) {
        this.even = even;
    }

    public void setDirty(List<Integer> dirty) {
        this.dirty = dirty;
    }

    public int getEven(int v) {
        return even[v];
    }

    public boolean isEven(int v) {
        return even[v] != DenseEdmondsMaximumCardinalityMatching.NIL;
    }

    public void setEven(int v, int value) {
        even[v] = value;
        if (value != DenseEdmondsMaximumCardinalityMatching.NIL) {
            dirty.add(v);
        }
    }

    public void setOdd(int v, int value, int[] thisOdd) {
        thisOdd[v] = value;
        if (value != DenseEdmondsMaximumCardinalityMatching.NIL) {
            dirty.add(v);
        }
    }

    public void reset(int[] thisOdd) {
        for (int v : dirty) {
            even[v] = DenseEdmondsMaximumCardinalityMatching.NIL;
            thisOdd[v] = DenseEdmondsMaximumCardinalityMatching.NIL;
        }
        dirty.clear();
    }
}
