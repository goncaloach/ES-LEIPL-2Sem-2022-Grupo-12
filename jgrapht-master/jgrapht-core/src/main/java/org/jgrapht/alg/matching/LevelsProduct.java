package org.jgrapht.alg.matching;


import java.util.List;

public class LevelsProduct {
    private LevelsProductEven lpEven = new LevelsProductEven();
    private List<Integer> dirty;

    public int[] getEven2() {
        return lpEven.getEven2();
    }

    public void setEven2(int[] even) {
        lpEven.setEven(even);
    }

    public void setDirty(List<Integer> dirty) {
        this.dirty = dirty;
    }

    public int getEven(int v) {
        return lpEven.getEven(v);
    }

    public boolean isEven(int v) {
        return lpEven.isEven(v);
    }

    public void setEven(int v, int value) {
        lpEven.getEven2()[v] = value;
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
            lpEven.getEven2()[v] = DenseEdmondsMaximumCardinalityMatching.NIL;
            thisOdd[v] = DenseEdmondsMaximumCardinalityMatching.NIL;
        }
        dirty.clear();
    }
}
